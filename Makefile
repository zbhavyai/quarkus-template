CONTAINER_ENGINE := $(shell if command -v podman &>/dev/null; then echo podman; else echo docker; fi)

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

.PHONY: prep
prep:
	@ln -sf $(CURDIR)/.hooks/pre-commit .git/hooks/pre-commit
	@echo "Hook installed";

.PHONY: clean
clean: .deps-backend
	@./mvnw --quiet clean;
	@echo "Cleaned build artifacts";

.PHONY: dev
dev: .deps-backend
	@./mvnw clean quarkus:dev

.PHONY: format
format: .deps-backend
	@./mvnw spotless:apply

.PHONY: build
build: .deps-backend
	@./mvnw clean package -DskipTests

.PHONY: build-native
build-native:
	@./mvnw clean package -Dnative -DskipTests

.PHONY: run
run: .deps-backend
	@java -jar ./target/quarkus-template-*-runner.jar

.PHONY: run-native
run-native:
	@./target/quarkus-template-*-runner

.PHONY: container-build
container-build: .deps-container
	@$(CONTAINER_ENGINE) compose build

.PHONY: container-run
container-run: .deps-container
	@$(CONTAINER_ENGINE) compose up --detach

.PHONY: container-stop
container-stop: .deps-container
	@$(CONTAINER_ENGINE) compose down

.PHONY: container-logs
container-logs: .deps-container
	@$(CONTAINER_ENGINE) compose logs --follow

.PHONY: container-destroy
container-destroy: .deps-container
	@$(CONTAINER_ENGINE) compose down --volumes --rmi local

.PHONY: help
help:
	@echo "Available targets:"
	@echo "  prep              - Install git hooks"
	@echo "  clean             - Clean build artifacts"
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
