CONTAINER_ENGINE := $(shell if command -v podman &>/dev/null; then echo podman; else echo docker; fi)

.PHONY: prep clean test dev format build build-native run run-native container-build container-run container-stop container-logs container-destroy help

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
	@./mvnw --quiet clean;
	@echo "Cleaned build artifacts";

test: .deps-backend
	@./mvnw clean test;

dev: .deps-backend
	@./mvnw clean quarkus:dev

format: .deps-backend
	@./mvnw spotless:apply

build: .deps-backend
	@./mvnw clean package -DskipTests

build-native:
	@./mvnw clean package -Dnative -DskipTests

run: .deps-backend
	@java -jar ./target/quarkus-template-*-runner.jar

run-native:
	@./target/quarkus-template-*-runner

container-build: .deps-container
	@$(CONTAINER_ENGINE) compose build

container-run: .deps-container
	@$(CONTAINER_ENGINE) compose up --detach

container-stop: .deps-container
	@$(CONTAINER_ENGINE) compose down

container-logs: .deps-container
	@$(CONTAINER_ENGINE) compose logs --follow

container-destroy: .deps-container
	@$(CONTAINER_ENGINE) compose down --volumes --rmi local

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
