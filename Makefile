.PHONY: prep clean dev format build build-native run run-native help

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

prep:
	@ln -sf $(CURDIR)/.hooks/pre-commit.sh .git/hooks/pre-commit
	@echo "Hook installed";

clean: .deps-backend
	@./mvnw --quiet clean;
	@echo "Cleaned build artifacts";

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

help:
	@echo "Available targets:"
	@echo "  prep              - Install git hook"
	@echo "  clean             - Clean build artifacts"
	@echo "  dev               - Start app in development mode"
	@echo "  format            - Format code using Spotless"
	@echo "  build             - Build app in JVM mode"
	@echo "  build-native      - Build app in native mode"
	@echo "  run               - Run app in JVM mode"
	@echo "  run-native        - Run app in native mode"
	@echo "  help              - Show this help message"
