#!/usr/bin/env bash

set -euo pipefail

block() {
    echo -e "\n\n"
    echo "$@"
    echo "[ERROR] Commit blocked."
    exit 1
}

lint_checks() {
    local staged
    staged=$(git diff --name-only --cached --exit-code -- '*.java')
    ret=$?
    if [ $ret -eq 0 ]; then
        return 0
    fi

    (./mvnw --quiet spotless:check) || block "[ERROR] Lint checks failed; run 'make format' to fix."
}

(lint_checks) || exit $?
