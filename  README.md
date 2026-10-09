# 🚀 Lovable Clone — AI-Powered React Application Builder

An AI-powered application builder that turns natural-language prompts into React applications. The platform combines an interactive frontend, a Spring Boot backend, LLM-powered code generation, persistent project files, and Kubernetes-based application previews.

This project explores how AI-assisted development tools can combine **LLM orchestration, file management, real-time streaming, containerized execution, and reverse-proxy routing** in one application.

## ✨ Features

- 🤖 **AI-powered code generation** — Generate and modify application files using natural-language prompts.
- 💬 **Streaming AI responses** — Stream generated content and structured events to the frontend.
- 📁 **Project and file management** — Create projects and manage their generated source files.
- ⚛️ **React application previews** — Run generated applications in Kubernetes runner pods.
- ☁️ **Object storage** — Store project files and starter templates in MinIO.
- 🔀 **Preview routing** — Route project-specific domains through a reverse proxy.
- 🔐 **Authentication and authorization** — Protect application endpoints using Spring Security and JWT.
- 💳 **Subscription and billing integration** — Support subscription plans and payment workflows.
- 🗄️ **Persistent storage** — Store application metadata and relational data in PostgreSQL.
- ⚡ **Redis integration** — Support preview-routing state and related infrastructure workflows.

## 🛠️ Technology Stack

| Layer | Technologies | Purpose |
|---|---|---|
| Frontend | React, TypeScript, Vite | User interface and development experience |
| UI | Tailwind CSS, DaisyUI, Radix UI | Styling and reusable interface components |
| Code editor | CodeMirror | Editing generated source code |
| Backend | Java, Spring Boot, Spring AI | API layer, business logic, and AI orchestration |
| API and streaming | REST APIs, Server-Sent Events (SSE) | Client-server communication and streaming responses |
| Authentication | Spring Security, JWT | Authentication and endpoint protection |
| Database | PostgreSQL, Spring Data JPA, Hibernate | Relational persistence |
| Object storage | MinIO | Project source files and templates |
| Cache and routing state | Redis | Preview-routing support |
| AI integration | Spring AI ChatClient, OpenRouter | LLM interaction |
| Application execution | Kubernetes, Node.js, Vite | Generated application preview workflow |
| Kubernetes integration | Fabric8 Kubernetes Client | Kubernetes resource orchestration |
| Build tools | Maven, npm, pnpm | Backend and frontend dependency management |
| Payments | Stripe | Checkout and subscription-related workflows |
| Testing | JUnit, Spring Boot Test, Vitest | Backend and frontend tests |

## 🏗️ Architecture

The core business application uses a **monolithic Spring Boot backend** for API handling, authentication, project management, persistence, AI orchestration, and billing. Supporting infrastructure handles object storage, preview routing, and execution of generated applications.

### 🧠 AI Code Generation Architecture

The frontend sends a prompt to the backend. The backend prepares project context, invokes the LLM, parses structured output, persists generated files, and streams events back to the client.

![AI Code Generation Architecture](docs/images/ai-design-architecture.png)

### 🚀 Code Execution and Preview Architecture

Generated project files are synchronized from MinIO into a runner container. Dependencies are installed and the Vite development server is started. A reverse proxy routes browser requests to the appropriate project preview.

![Code Execution and Preview Architecture](docs/images/code-execution-architecture.png)

### 🔄 High-Level Request Flow

1. The user creates a project or submits a prompt.
2. The frontend sends the request to the Spring Boot backend.
3. The backend gathers project context and invokes the LLM.
4. Generated content is parsed into structured messages and file events.
5. Project files and application metadata are persisted.
6. When a preview is requested, the backend coordinates the execution environment.
7. The runner starts the generated application.
8. The reverse proxy routes requests to the corresponding preview.

## 📂 Project Structure

The following is a representative structure. Keep directory names synchronized with the actual repository as it evolves.

```text
lovable-clone/
├── README.md
├── docs/
│   └── images/
│       ├── ai-design-architecture.png
│       ├── code-execution-architecture.png
│       └── er-diagram.png
├── pom.xml
├── mvnw
├── services.docker-compose.yml
├── proxy/
│   ├── Dockerfile
│   ├── index.js
│   └── package.json
├── k8s/
│   ├── infra.yml
│   ├── runner-pods.yml
│   ├── lovable-clone-proxy.yml
│   └── policy.yml
├── react-vite-tailwind-daisyui-starter/
└── src/
    ├── main/
    │   ├── java/com/projectlove/lovable_clone/
    │   │   ├── config/
    │   │   ├── controllers/
    │   │   ├── dto/
    │   │   ├── entity/
    │   │   ├── enums/
    │   │   ├── error/
    │   │   ├── llm/
    │   │   ├── mapper/
    │   │   ├── repository/
    │   │   ├── security/
    │   │   └── Services/
    │   └── resources/
    ├── lovable-frontend/
    │   ├── src/
    │   ├── package.json
    │   └── vite.config.ts
    └── test/
```

## ⚙️ Prerequisites

Install and configure the tools required by your local setup:

- Java version compatible with the project's Maven configuration.
- Maven or the included Maven Wrapper.
- Node.js and npm.
- pnpm for generated React applications.
- Docker and Docker Compose.
- A Kubernetes cluster, such as `kind`.
- `kubectl` configured to access the cluster.
- MinIO and Redis.
- AI-provider and payment-provider credentials for the corresponding features.

Check your environment:

```bash
java -version
./mvnw -version
node --version
npm --version
pnpm --version
docker --version
kubectl get nodes
```

On Windows, use `mvnw.cmd` instead of `./mvnw` where appropriate.

## 🔐 Environment Configuration

Configure the application using environment variables or a local configuration file that is excluded from version control.

Typical configuration categories include:

- PostgreSQL connection settings.
- MinIO endpoint and credentials.
- Redis host and port.
- AI-provider API key and model settings.
- JWT signing secret.
- Stripe API and webhook credentials.
- Kubernetes namespace and preview settings.

Use placeholder values in examples and never commit real credentials, API keys, signing secrets, or payment-provider secrets. If a credential was committed previously, rotate it; moving it into an environment variable does not invalidate an already exposed secret.

## 🐳 Local Infrastructure and Port Forwarding

During local development, Docker and Kubernetes were used together. Redis and the reverse proxy needed to be reachable from the laptop so the application could communicate with those services and route project previews.

**Docker port mapping** exposes a container port on the host. **Kubernetes port forwarding** forwards a pod or service port to a local port. Use the commands below when Redis and the proxy are exposed as Kubernetes services in the `lovable-clone` namespace.

### 🔀 Forward the reverse-proxy service

```bash
kubectl port-forward -n lovable-clone svc/lovable-clone-proxy-svc 8090:80
```

This forwards local port `8090` to port `80` on the Kubernetes reverse-proxy service. Keep this terminal session running while testing previews.

### ⚡ Forward the Redis service

```bash
kubectl port-forward -n lovable-clone svc/redis-service 6379:6379
```

This forwards local port `6379` to port `6379` on the Redis service. Keep this terminal session running while testing features that depend on the connection.

If Redis or the proxy is running directly as a Docker container instead, configure the corresponding Docker port mapping and application host/port settings. `kubectl port-forward` applies to Kubernetes pods and services, not directly to standalone Docker containers.

## ☸️ Kubernetes Deployment Workflow

The Kubernetes manifests define the supporting infrastructure, runner workloads, reverse proxy, and network policies. Run the commands from the directory containing these manifest files.

### 1. Apply the manifests

In Windows PowerShell:

```powershell
kubectl apply -f .\infra.yml
kubectl apply -f .\runner-pods.yml
kubectl apply -f .\lovable-clone-proxy.yml
kubectl apply -f .\policy.yml
```

These commands apply the desired configurations for infrastructure, runner pods, the proxy, and network policies.

### 2. Check pod and service status

```bash
kubectl get pods -n lovable-clone
kubectl get services -n lovable-clone
```

Inspect an individual pod when something does not start correctly:

```bash
kubectl describe pod <pod-name> -n lovable-clone
```

Check pod status and logs when diagnosing issues such as `ImagePullBackOff`, `ErrImagePull`, `RunContainerError`, or repeated container restarts.

### 3. Port-forward a runner pod

```bash
kubectl port-forward pod/runner-pool-xxxxxxxxxxxx <laptop-port>:<pod-port> -n lovable-clone
```

Replace the pod name and port placeholders with the actual values for the runner you are investigating.

### 4. Delete pods when required

```bash
kubectl delete pod --all -n lovable-clone
```

**Caution:** this deletes every pod in the `lovable-clone` namespace. Controllers may recreate managed pods. Prefer deleting a specific pod when that is sufficient.

## 🧪 Debugging Runner and Syncer Containers

A runner pool can contain separate containers responsible for executing the application and synchronizing its files. Inspect each container independently when debugging preview failures.

### 📂 Inspect the runner's application directory

```bash
kubectl exec runner-xxxxxxxxxxxx -n lovable-clone -c runner -- ls -la /app
```

Use this to check whether the expected application files exist in the runner container.

### ☁️ Inspect project files in MinIO through the syncer

```bash
kubectl exec runner-xxxxxxxxxxxx -n lovable-clone -c syncer -- mc ls myminio/projectslovable/1/react-vite-tailwind-daisyui-starter-main/
```

Replace `1` with the actual project ID. This command assumes the MinIO alias, bucket, and template path match your environment.

### 🔄 Synchronize project files into the runner

```bash
kubectl exec runner-xxxxxxxxxxxx -n lovable-clone -c syncer -- mc mirror --overwrite myminio/projectslovable/<projectId>/react-vite-tailwind-daisyui-starter-main/ /app/
```

Replace `<projectId>` with the actual project ID before running the command. The placeholder should not be passed literally. This mirrors the selected project's files into `/app/` in the environment shared with the syncer.

### 📦 Install application dependencies

```bash
kubectl exec runner-xxxxxxxxxxxx -n lovable-clone -c runner -- sh -c "cd /app && pnpm install --reporter=append-only"
```

This installs the dependencies declared in the application's package manifest. If pnpm reports `ERR_PNPM_NO_PKG_MANIFEST`, check whether `package.json` exists in `/app`. Verify the source template path, sync destination, and directory structure before retrying installation.

### ▶️ Start the React development server

```bash
kubectl exec runner-xxxxxxxxxxxx -n lovable-clone -c runner -- sh -c "cd /app && pnpm run dev --host 0.0.0.0 --port 5173"
```

The Vite development server listens on port `5173` and binds to `0.0.0.0`, allowing access from outside the container when the networking and routing configuration permits it.

These commands are useful for manual debugging. In the normal application workflow, deployment orchestration should handle file synchronization, dependency installation, and application startup.

## 🌐 Local Preview Domains on Windows

The preview workflow uses project-specific hostnames. When accessing these domains locally, each hostname must resolve to the address used by the local proxy setup.

### 1. Edit the Windows hosts file

Open the following file with administrator privileges:

```text
C:\Windows\System32\drivers\etc\hosts
```

Add an entry for each project hostname you want to access, pointing it to the local machine when using a local proxy port-forward:

```text
127.0.0.1 project-1.app.domain.com
127.0.0.1 project-2.app.domain.com
127.0.0.1 project-3.app.domain.com
```

Replace these example hostnames with the actual hostnames configured by your reverse proxy.

### 2. Flush the DNS cache

Run this command in Command Prompt after saving the file:

```cmd
ipconfig /flushdns
```

### 3. Keep the proxy port-forward running

```bash
kubectl port-forward -n lovable-clone svc/lovable-clone-proxy-svc 8090:80
```

Access the preview using the hostname and forwarded proxy port, for example:

```text
http://project-1.app.domain.com:8090
```

The standard Windows hosts file does not support wildcard entries such as `*.app.domain.com`. Add an explicit entry for each project domain you want to test.

If a domain does not resolve or the preview does not load, check the hosts entry, DNS cache, proxy port-forward, proxy routing configuration, and runner status.

## 🧠 AI Generation Workflow

The AI workflow coordinates prompt handling, project context, model interaction, response parsing, and file persistence.

1. The frontend submits the user's prompt.
2. The backend builds the relevant project context.
3. The LLM generates a response containing messages and file changes.
4. The response parser interprets the structured output.
5. File changes are persisted to object storage.
6. The backend streams the resulting events to the frontend.

Keeping response parsing and file persistence separate from the controller helps make the generation workflow easier to test and maintain.

## 🚀 Application Preview Workflow

The preview workflow connects generated source code to an executable React application.

1. A project is created or modified.
2. Project files are stored in MinIO.
3. The deployment workflow identifies the project and its execution environment.
4. The syncer mirrors the appropriate files into the runner.
5. Dependencies are installed in the runner.
6. The Vite development server starts on port `5173`.
7. The reverse proxy routes the project hostname to the appropriate preview.
8. Redis and the proxy support the configured preview-routing workflow.

This design separates core application orchestration from the environment in which generated application code executes.

## 🗄️ Database Design

The relational data model represents users, projects, project membership, generated files, chat interactions, previews, and subscription information.

![Database Entity Relationship Diagram](docs/images/er-diagram.png)

The principal entities include:

- `USER`
- `PROJECT`
- `PROJECT_OWNERSHIP`
- `PROJECT_MEMBER`
- `PROJECT_FILE`
- `PREVIEW`
- `CHAT_SESSION`
- `CHAT_MESSAGE`
- `SUBSCRIPTION`
- `PLAN`
- `USAGE_LOG`

Refer to the diagram for relationships between these entities. Keep the diagram synchronized with the entity classes and database schema as they evolve.

## 🔌 API Overview

The backend exposes APIs for the application's core workflows.

| Area | Example endpoints |
|---|---|
| Authentication | `/api/auth/signup`, `/api/auth/login`, `/api/auth/me` |
| Projects | `/api/projects`, `/api/projects/{projectId}` |
| Project files | `/api/projects/{projectId}/files` |
| AI chat | `/api/chat/stream` |
| Project members | `/api/projects/{projectId}/members` |
| Plans and subscriptions | `/api/plans`, `/api/me/subscription` |
| Payments | `/api/payment/checkout`, `/api/payment/portal` |
| Preview deployment | `/api/projects/{id}/deploy` |

This is a high-level overview, not a complete API specification. Refer to the controller mappings in the repository for exact HTTP methods, request schemas, authorization rules, and response formats.

## 🧪 Testing

The project includes backend and frontend tests.

- **Backend:** JUnit and Spring Boot Test.
- **Frontend:** Vitest and related React testing utilities.

Run the backend test suite:

```bash
./mvnw test
```

Run the frontend tests from the frontend directory using the test script declared in its `package.json`:

```bash
cd src/lovable-frontend
npm test
```

If the frontend test script has a different name, use the script actually declared in `package.json`. Tests should cover critical behavior such as structured LLM response parsing, event handling, and frontend stream parsing. Add integration tests for persistence, authorization, and preview deployment as the project evolves.

## 🧰 Troubleshooting Notes and Lessons Learned

### 🐳 Container image availability

During development, an official MinIO client image became unavailable, so I replaced it with a third-party image that could be pulled successfully.

**Lesson:** container images and tags are external dependencies. Verify image availability, architecture compatibility, and publisher trustworthiness before relying on an image in deployment manifests.

### 📁 Missing application files in the runner

A runner may start without the expected application files if synchronization uses the wrong project path or destination.

**Debugging approach:**

1. Inspect the source path in MinIO.
2. Inspect the runner's `/app` directory.
3. Verify the project ID and template directory.
4. Confirm synchronization completes before dependency installation.
5. Check that `package.json` exists in the directory used by pnpm.

### 🌐 Preview domain does not resolve

A correctly running runner does not guarantee that the preview is accessible.

**Debugging approach:**

1. Confirm the hostname is present in the Windows hosts file.
2. Run `ipconfig /flushdns`.
3. Verify the reverse-proxy port-forward.
4. Inspect the proxy's project-to-runner route.
5. Confirm the Vite server is listening on the expected port.

### 🔌 Redis or proxy is unreachable

When local routing fails, verify which networking mode is active and whether the service is reachable through the intended host port or Kubernetes port-forward. Keep the forwarding process running while testing, and confirm that the application is configured to use the matching host and port.

## 🏛️ Engineering Decisions and Lessons

- **Layered backend design:** keep controllers focused on HTTP concerns, services responsible for business logic, repositories responsible for persistence, and DTOs responsible for API contracts.
- **Bottom-up migration:** when restructuring code, migrate and validate entities and DTOs first, followed by mappers, repositories, services, and finally controllers. This helps surface dependency problems earlier.
- **Separate execution from orchestration:** the backend coordinates previews while runner containers execute generated applications.
- **Explicit file synchronization:** validate source and destination paths before installing dependencies or starting the preview server.
- **Observable deployment workflows:** inspect pods, containers, filesystem state, and logs independently rather than assuming a successful deployment means the preview is healthy.
- **Reproducible local setup:** document port mappings, port-forwarding commands, hostname configuration, and required infrastructure so another developer can reproduce the workflow.

## 🔒 Security Considerations

- Keep secrets out of source control and README examples.
- Restrict Kubernetes permissions and namespace access.
- Treat generated application code as untrusted.
- Apply appropriate network policies and execution isolation.
- Protect project files and enforce project-level authorization.
- Validate access to subscription and payment workflows.
- Use maintained, trusted container images and pin versions where practical.

## 🛣️ Future Improvements

- Add automated integration tests for the end-to-end preview lifecycle.
- Introduce health checks and readiness validation for runner workloads.
- Improve cleanup of inactive runner resources.
- Add structured deployment logs and actionable error messages.
- Make local domain routing easier to configure.
- Improve reproducibility through pinned dependencies and container image versions.
- Add automated schema migrations and stronger database change management.

---

**Built to explore AI-powered application generation, backend engineering, and Kubernetes-based preview execution.**
