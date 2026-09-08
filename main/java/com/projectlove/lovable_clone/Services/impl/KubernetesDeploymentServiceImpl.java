        package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.DeploymentService;
import com.projectlove.lovable_clone.dto.deploy.DeployResponse;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class KubernetesDeploymentServiceImpl implements DeploymentService {

    private final KubernetesClient kubernetesClient;

    private final String NAMESPACE = "lovable-clone";
    private final String POOL_LABEL = "status";
    private final String PROJECT_LABEL = "project-id";

    private final String IDLE = "idle";
    private final String BUSY = "busy";

    private final String SYNCER_CONTAINER = "syncer";
    private final String RUNNER_CONTAINER = "runner";

    private final String REVERSE_PROXY_PORT = "8090";

    @Override
    public DeployResponse deploy(Long projectId) {

        String domain = "project-" + projectId + ".app.domain.com";

        /*
         * If this project is already running, don't create another deployment.
         */
        Pod existingPod = findActivePod(projectId);

        if (existingPod != null) {

            log.info(
                    "Project {} is already running in pod {}",
                    projectId,
                    existingPod.getMetadata().getName()
            );

            return new DeployResponse(
                    "http://" + domain + ":" + REVERSE_PROXY_PORT
            );
        }

        return claimAndStartNewPod(projectId, domain);
    }

    private DeployResponse claimAndStartNewPod(
            Long projectId,
            String domain
    ) {

        /*
         * Find only:
         *
         * app=runner
         * status=idle
         * Pod phase=Running
         */
        Pod pod = kubernetesClient.pods()
                .inNamespace(NAMESPACE)
                .withLabel("app", "runner")
                .withLabel(POOL_LABEL, IDLE)
                .list()
                .getItems()
                .stream()
                .filter(p -> p.getStatus() != null)
                .filter(p ->
                        "Running".equals(p.getStatus().getPhase())
                )
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "No idle runners available. Please scale up the runner-pool."
                        )
                );

        String podName = pod.getMetadata().getName();

        log.info(
                "Claiming pod {} for project {}",
                podName,
                projectId
        );

        /*
         * Claim the idle runner.
         *
         * idle -> busy
         * project-id -> current project
         */
        kubernetesClient.pods()
                .inNamespace(NAMESPACE)
                .withName(podName)
                .edit(p -> {

                    p.getMetadata()
                            .getLabels()
                            .put(POOL_LABEL, BUSY);

                    p.getMetadata()
                            .getLabels()
                            .put(
                                    PROJECT_LABEL,
                                    projectId.toString()
                            );

                    return p;
                });

        // ---------------------------------------------------------
        // Initial MinIO sync
        // ---------------------------------------------------------

        /*
         * MinIO structure:
         *
         * projectslovable/
         *   {projectId}/
         *     react-vite-tailwind-daisyui-starter-main/
         *
         * We copy the contents directly into /app.
         *
         * Therefore:
         *
         * /app/package.json
         * /app/src
         * /app/index.html
         */

        String initialSyncCmd = String.format(
                "for item in /app/* /app/.[!.]* /app/..?*; do " +
                        "[ -e \"$item\" ] || continue; " +
                        "[ \"$(basename \"$item\")\" = \"node_modules\" ] && continue; " +
                        "rm -rf \"$item\"; " +
                        "done && " +
                        "mc mirror --overwrite " +
                        "myminio/projectslovable/%d/react-vite-tailwind-daisyui-starter-main/ " +
                        "/app/",
                projectId
        );

        log.info(
                "Starting initial sync for project {} in pod {}",
                projectId,
                podName
        );

        execCommand(
                podName,
                SYNCER_CONTAINER,
                "sh",
                "-c",
                initialSyncCmd
        );

        // ---------------------------------------------------------
        // Continuous MinIO watcher
        // ---------------------------------------------------------

        /*
         * Keep syncing project changes from MinIO.
         *
         * IMPORTANT:
         * This runs in the background because it is a long-running
         * watcher.
         */

        String watchCmd = String.format(
                "nohup mc mirror --overwrite --watch " +
                        "myminio/projectslovable/%d/react-vite-tailwind-daisyui-starter-main/ " +
                        "/app/ " +
                        "> /app/sync.log 2>&1 </dev/null &",
                projectId
        );

        log.info(
                "Starting MinIO watcher for project {}...",
                projectId
        );

        execCommand(
                podName,
                SYNCER_CONTAINER,
                "sh",
                "-c",
                watchCmd
        );

        // ---------------------------------------------------------
        // Dependencies
        // ---------------------------------------------------------

        /*
         * IMPORTANT:
         *
         * We preserve /app/node_modules during MinIO sync.
         *
         * If node_modules already exists:
         *
         *     DO NOT INSTALL AGAIN.
         *
         * If this is a brand-new runner:
         *
         *     Install dependencies once.
         *
         * This prevents pnpm install from happening on every
         * deployment using the same runner.
         */

        String installCmd =
                "cd /app && " +
                        "if [ -d node_modules ] && " +
                        "[ -f node_modules/.modules.yaml ]; then " +
                        "echo 'node_modules already exists; skipping pnpm install'; " +
                        "else " +
                        "echo 'node_modules missing; installing dependencies'; " +
                        "pnpm install --prefer-offline; " +
                        "fi";

        log.info(
                "Checking dependencies for project {}...",
                projectId
        );

        execCommand(
                podName,
                RUNNER_CONTAINER,
                300L,
                "sh",
                "-c",
                installCmd
        );

        // ---------------------------------------------------------
        // Start Vite
        // ---------------------------------------------------------

        /*
         * Start Vite in the background.
         *
         * No npm/pnpm install here.
         */

        String startCmd =
                "cd /app && " +
                        "nohup pnpm exec vite --host 0.0.0.0 --port 5173 " +
                        "> /app/dev.log 2>&1 </dev/null &";

        log.info(
                "Starting dev server for project {}...",
                projectId
        );

        execCommand(
                podName,
                RUNNER_CONTAINER,
                "sh",
                "-c",
                startCmd
        );

        log.info(
                "Deployment successful: http://{}:{}",
                domain,
                REVERSE_PROXY_PORT
        );

        return new DeployResponse(
                "http://" + domain + ":" + REVERSE_PROXY_PORT
        );
    }

    // ---------------------------------------------------------
    // Kubernetes Exec
    // ---------------------------------------------------------

    private void execCommand(
            String podName,
            String container,
            String... command
    ) {

        execCommand(
                podName,
                container,
                300L,
                command
        );
    }

    private void execCommand(
            String podName,
            String container,
            long timeoutSeconds,
            String... command
    ) {

        log.debug(
                "Exec in {}:{} -> {}",
                podName,
                container,
                String.join(" ", command)
        );

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        ByteArrayOutputStream error =
                new ByteArrayOutputStream();

        try (
                ExecWatch watch =
                        kubernetesClient
                                .pods()
                                .inNamespace(NAMESPACE)
                                .withName(podName)
                                .inContainer(container)
                                .writingOutput(output)
                                .writingError(error)
                                .exec(command)
        ) {

            /*
             * For synchronous commands, wait for the REAL process
             * exit code.
             *
             * This is different from WebSocket onClose().
             */
            int exitCode = watch
                    .exitCode()
                    .get(
                            timeoutSeconds,
                            TimeUnit.SECONDS
                    );

            String stdout =
                    output.toString(StandardCharsets.UTF_8);

            String stderr =
                    error.toString(StandardCharsets.UTF_8);

            log.debug(
                    "Exec exit code: {}",
                    exitCode
            );

            if (!stdout.isBlank()) {

                log.debug(
                        "Exec stdout:\n{}",
                        stdout
                );
            }

            if (!stderr.isBlank()) {

                log.warn(
                        "Exec stderr:\n{}",
                        stderr
                );
            }

            if (exitCode != 0) {

                throw new RuntimeException(
                        "Command failed with exit code "
                                + exitCode
                                + "\nstdout:\n"
                                + stdout
                                + "\nstderr:\n"
                                + stderr
                );
            }

        } catch (Exception e) {

            log.error(
                    "Exec failed in {}:{} -> {}",
                    podName,
                    container,
                    String.join(" ", command),
                    e
            );

            throw new RuntimeException(
                    "Pod execution failed",
                    e
            );
        }
    }

    // ---------------------------------------------------------
    // Find existing project pod
    // ---------------------------------------------------------

    private Pod findActivePod(Long projectId) {

        return kubernetesClient
                .pods()
                .inNamespace(NAMESPACE)
                .withLabel(
                        PROJECT_LABEL,
                        projectId.toString()
                )
                .withLabel(
                        POOL_LABEL,
                        BUSY
                )
                .list()
                .getItems()
                .stream()
                .filter(pod ->
                        pod.getStatus() != null &&
                                "Running".equals(
                                        pod.getStatus().getPhase()
                                )
                )
                .findFirst()
                .orElse(null);
    }
}
