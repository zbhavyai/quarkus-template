# Quarkus Template

[![Build](https://img.shields.io/github/actions/workflow/status/zbhavyai/quarkus-template/build.yml?label=Build)](https://github.com/zbhavyai/quarkus-template/actions/workflows/build.yml)
[![Release](https://img.shields.io/github/actions/workflow/status/zbhavyai/quarkus-template/release.yml?label=Release)](https://github.com/zbhavyai/quarkus-template/actions/workflows/release.yml)
[![License](https://img.shields.io/github/license/zbhavyai/quarkus-template?label=License)](https://github.com/zbhavyai/quarkus-template/blob/main/LICENSE)

A **starter template** for building backend applications with [Quarkus](https://quarkus.io/), the _supersonic-subatomic_ Java framework.

> [!NOTE]
> This is not a production-ready backend. It's a boilerplate with extensions, configs, and workflows I find useful for new projects.

## :sparkles: Features

-  **Opinionated setup**

   -  Predefined project structure and `application.properties`.
   -  Recommended VS Code settings (and some IntelliJ configs are [here](https://github.com/zbhavyai/fedora-setup/tree/main/roles/intellij_idea/files) if interested).

-  **Code quality**

   -  Formatting via [`google-java-format`](https://github.com/google/google-java-format).
   -  Enforced with [Spotless](https://github.com/diffplug/spotless) and a pre-commit hook.

-  **Containerization**

   -  `Dockerfile` + `docker-compose.yml`.
   -  Automatic Podman/Docker detection.

-  **Developer tooling**

   -  `Makefile` targets for formatting, building, running, and container workflows.

-  **CI/CD ready**

   -  GitHub Actions + Bitbucket Pipelines for test, build, and release.

## :rocket: Getting started

-  Before you start development on this project, run the prep target. This will install a hook that would check your commit for code formatting issues.

   ```shell
   make prep
   ```

-  Run the application in dev mode that enables live coding. Quarkus dev UI would be accessible at [http://127.0.0.1:8080/q/dev-ui/welcome](http://127.0.0.1:8080/q/dev-ui/welcome).

   ```shell
   make dev
   ```

-  To format the code, enable [google-java-format](https://github.com/google/google-java-format) and in your IDE and turn on the auto-formatting on save. You may also run spotless plugin manually.

   ```shell
   make format
   ```

## :package: Packaging and running

1. Build the application

   ```shell
   make build

   # OR

   make container-build
   ```

1. Run the application

   ```shell
   make run

   # OR

   make container-run
   ```

## :page_facing_up: License

The Quarkus Template is licensed under the terms of the [MIT license](LICENSE).
