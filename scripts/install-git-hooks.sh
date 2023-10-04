#!/bin/sh

set -e

PROJECT_PATH="$(git rev-parse --show-toplevel)"

cd "$PROJECT_PATH"
\cp -r scripts/git-hooks/. "$(git rev-parse --git-path hooks)/"
echo "git hooks installed successfully!"
