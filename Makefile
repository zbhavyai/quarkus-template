CONTAINER_ENGINE := $(shell if command -v podman >/dev/null 2>&1; then echo podman; else echo docker; fi)
REVISION := $(shell git rev-parse --short HEAD)

.PHONY: prep clean test dev format check-updates build build-native run run-native container-build container-run container-stop container-logs container-destroy help

prep:
	@ln -sf $(CURDIR)/.hooks/pre-commit.sh .git/hooks/pre-commit
	@echo "Hook installed";

clean:
	@./mvnw --quiet --batch-mode clean;
	@echo "Cleaned build artifacts";

test:
	@./mvnw clean test -Drevision=$(REVISION);

dev:
	@./mvnw clean quarkus:dev

format:
	@./mvnw spotless:apply

check-updates:
	@./mvnw versions:display-property-updates
	@./mvnw versions:display-extension-updates

build:
	@./mvnw clean verify -Drevision=$(REVISION)

build-native:
	@./mvnw clean verify -Dnative -Drevision=$(REVISION)

run:
	@java -jar ./target/quarkus-template-*-runner.jar

run-native:
	@./target/quarkus-template-*-runner

container-build:
	@REVISION=$(REVISION) $(CONTAINER_ENGINE) compose build

container-run:
	@REVISION=$(REVISION) $(CONTAINER_ENGINE) compose up --detach

container-stop:
	@REVISION=$(REVISION) $(CONTAINER_ENGINE) compose down

container-logs:
	@REVISION=$(REVISION) $(CONTAINER_ENGINE) compose logs --follow

container-destroy:
	@REVISION=$(REVISION) $(CONTAINER_ENGINE) compose down --volumes --rmi local

help:
	@echo "Available targets:"
	@echo "  prep              - Install git hook"
	@echo "  clean             - Clean build artifacts"
	@echo "  test              - Run tests"
	@echo "  dev               - Start app in development mode"
	@echo "  format            - Format code using Spotless"
	@echo "  check-updates     - Check for dependency updates in the pom.xml"
	@echo "  build             - Build app in JVM mode"
	@echo "  build-native      - Build app in native mode"
	@echo "  run               - Run app in JVM mode"
	@echo "  run-native        - Run app in native mode"
	@echo "  container-build   - Build app in containers and create container image"
	@echo "  container-run     - Run app container"
	@echo "  container-stop    - Stop app container"
	@echo "  container-logs    - Show app container logs"
	@echo "  container-destroy - Stop and delete app container, networks, volumes, and images"
	@echo "  help              - Show this help message"
