#!/usr/bin/env bash
set -euo pipefail

function block() {
    echo -e "\n\n"
    echo "$@"
    echo "[ERROR] Commit blocked."
    exit 1
}

function format_checks() {
    local staged
    staged=$(git diff --name-only --cached --exit-code -- '*.java')
    ret=$?
    if [ $ret -eq 0 ]; then
        return 0
    fi

    (./mvnw --quiet --batch-mode spotless:check) || block "[ERROR] Formatting checks failed; run 'make format' to fix."
}

(format_checks) || exit $?
