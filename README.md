# Quarkus Template

[![Build](https://img.shields.io/github/actions/workflow/status/zbhavyai/quarkus-template/build.yml?label=Build)](https://github.com/zbhavyai/quarkus-template/actions/workflows/build.yml)
[![Release](https://img.shields.io/github/actions/workflow/status/zbhavyai/quarkus-template/release.yml?label=Release)](https://github.com/zbhavyai/quarkus-template/actions/workflows/release.yml)
[![License](https://img.shields.io/github/license/zbhavyai/quarkus-template?label=License)](https://github.com/zbhavyai/quarkus-template/blob/main/LICENSE)

A starter template for writing backend application using [Quarkus](https://quarkus.io/), the supersonic-subatomic framework for Java.

> [!NOTE]
> This is not a full-blown backend application written in Quarkus, rather just enough to get started with a new project with some useful extensions and properties configured that are suitable for my own needs.

## Highlights

-  Opinionated project structure.
-  Opinionated configurations in `application.properties`.
-  Opinionated settings and extension recommendations for VS Code (I use IntelliJ IDEA though :smile:, refer to my settings [here](https://github.com/zbhavyai/fedora-setup/tree/main/roles/intellij_idea/files) if interested).
-  Formatting using `google-java-format` and enforced using Spotless plugin and a pre-commit hook.
-  Dockerfile and Docker Compose for building and running the application in containers.
-  Automatic switching between Podman and Docker based on installation.
-  Make targets for common tasks like formatting, building, and running the application locally and in containers.
-  CI/CD using GitHub Actions and Bitbucket Pipelines to test, build, and release the application.

## Development

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

## Packaging and running

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
