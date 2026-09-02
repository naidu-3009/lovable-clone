package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.DeploymentService;
import com.projectlove.lovable_clone.dto.deploy.DeployResponse;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class KubernetesDeploymentServiceImpl implements DeploymentService {


    private final KubernetesClient kubernetesClient;

    private final String NAMESPACE="lovable-clone";
    private final String POOL_LABEL="status";
    private final String PROJECT_LABEL="project-id";
    private final String IDLE="idle";
    private final String BUSY="busy";
    private final String SYNCER_CONTAINER="syncer";
    private final String RUNNER_CONTAINER="runner";
    private final String REVERSE_PROXY_PORT="8090";

    @Override
    public DeployResponse deploy(Long projectId) {
        String domain="project-"+projectId+".app.domain.com";
        Pod existingPod=findActivePod(projectId);
        if(existingPod!=null){
            return new DeployResponse("http"+domain+":"+REVERSE_PROXY_PORT);
        }
        return claimAndStartNewPod(projectId,domain);
    }

    private DeployResponse claimAndStartNewPod(Long projectId, String domain) {
        Pod pod = kubernetesClient.pods().inNamespace(NAMESPACE)
                .withLabel(POOL_LABEL, IDLE)
                .list().getItems().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No idle runners available. Please scale up the runner-pool."));

        String podName = pod.getMetadata().getName();
        log.info("Claiming pod {} for project {}", podName, projectId);

        kubernetesClient.pods().inNamespace(NAMESPACE).withName(podName).edit(p -> {
            p.getMetadata().getLabels().put(POOL_LABEL, BUSY);
            p.getMetadata().getLabels().put(PROJECT_LABEL, projectId.toString());
            return p;
        });

        // Syncer Commands
        String initialSyncCmd = String.format(
                "mc mirror --overwrite myminio/projects/%d/ /app/",
                projectId);

        log.info("Starting initial sync for the project {} in the pod {}",projectId,podName);
        //executing that command in the container
        execCommand(podName,SYNCER_CONTAINER,"sh","-c",initialSyncCmd);

        String watchCmd = String.format(
                //we are just creating a process which keeps on comparing contents in our minio and our pod
                "nohup mc mirror --overwrite --watch myminio/projects/%d/ /app/ > /app/sync.log 2>&1 &",
                projectId);
        execCommand(podName, SYNCER_CONTAINER, "sh", "-c", watchCmd);


        // Runner Commands
        String startCmd = "npm install && nohup npm run dev -- --host 0.0.0.0 --port 5173 > /app/dev.log 2>&1 &";

        log.info("Starting dev server for project {}...", projectId);
        execCommand(podName, RUNNER_CONTAINER, "sh", "-c", startCmd);


        log.info("Deployment successful: http://{}:{}", domain, REVERSE_PROXY_PORT);
        return new DeployResponse("http://" + domain + ":" + REVERSE_PROXY_PORT);



    }

    private void execCommand(String podName, String container, String... command) {
        log.debug("Exec in {}:{} -> {}", podName, container, String.join(" ", command));

        CompletableFuture<String> data = new CompletableFuture<>();
        try (ExecWatch ignored = kubernetesClient.pods().inNamespace(NAMESPACE).withName(podName)
                .inContainer(container)
                .writingOutput(new ByteArrayOutputStream())
                .writingError(new ByteArrayOutputStream())
                .usingListener(new ExecListener() {
                    @Override
                    public void onClose(int code, String reason) {
                        data.complete("Done");
                    }
                })
                .exec(command)) {

            // Wait briefly to ensure command fired (Fabric8 exec is async)
            // For long running background jobs (nohup), we don't wait for "Done"
            if (command[command.length - 1].trim().endsWith("&")) {
                Thread.sleep(500);
            } else {
                data.get(30, TimeUnit.SECONDS); // Block for synchronous setup commands (npm install)
            }

        } catch (Exception e) {
            log.error("Exec failed", e);
            throw new RuntimeException("Pod Execution Failed", e);
        }
    }

    Pod findActivePod(Long projectId) {
        return kubernetesClient.pods().inNamespace(NAMESPACE)
                .withLabel(PROJECT_LABEL, projectId.toString())
                .withLabel(POOL_LABEL, BUSY) // Only find active/busy ones
                .list().getItems().stream()
                .filter(pod -> pod.getStatus().getPhase().equals("Running"))
                .findFirst()
                .orElse(null);
    }
}
