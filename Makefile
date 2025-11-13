CONTAINER_ENGINE := $(shell if command -v podman >/dev/null 2>&1; then echo podman; else echo docker; fi)
REVISION := $(shell git rev-parse --short HEAD)

.PHONY: prep clean test dev format check-updates build build-native run run-native container-build container-run container-stop container-logs container-destroy help

define CHECK_DEPENDENCY
	@for cmd in $(1); do \
		if ! command -v $$cmd &>/dev/null; then \
			echo "Couldn't find $$cmd!"; \
			exit 1; \
		fi; \
	done
endef

.deps-backend:
	$(call CHECK_DEPENDENCY, java, javac)

.deps-container:
	$(call CHECK_DEPENDENCY, $(CONTAINER_ENGINE))

prep:
	@ln -sf $(CURDIR)/.hooks/pre-commit.sh .git/hooks/pre-commit
	@echo "Hook installed";

clean: .deps-backend
	@./mvnw --quiet --batch-mode clean;
	@echo "Cleaned build artifacts";

test: .deps-backend
	@./mvnw clean test -Drevision=$(REVISION);

dev: .deps-backend
	@./mvnw clean quarkus:dev

format: .deps-backend
	@./mvnw spotless:apply

check-updates: .deps-backend
	@./mvnw versions:display-property-updates

build: .deps-backend
	@./mvnw clean verify -Drevision=$(REVISION)

build-native:
	@./mvnw clean verify -Dnative -Drevision=$(REVISION)

run: .deps-backend
	@java -jar ./target/quarkus-template-*-runner.jar

run-native:
	@./target/quarkus-template-*-runner

container-build: .deps-container
	@REVISION=$(REVISION) $(CONTAINER_ENGINE) compose build

container-run: .deps-container
	@REVISION=$(REVISION) $(CONTAINER_ENGINE) compose up --detach

container-stop: .deps-container
	@REVISION=$(REVISION) $(CONTAINER_ENGINE) compose down

container-logs: .deps-container
	@REVISION=$(REVISION) $(CONTAINER_ENGINE) compose logs --follow

container-destroy: .deps-container
	@REVISION=$(REVISION) $(CONTAINER_ENGINE) compose down --volumes --rmi local

help:
	@echo "Available targets:"
	@echo "  prep              - Install git hook"
	@echo "  clean             - Clean build artifacts"
	@echo "  test              - Run tests"
	@echo "  dev               - Start app in development mode"
	@echo "  format            - Format code using Spotless"
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
