#!/bin/bash

# Common utility function for release scripts

fetchVersionFile() {
    echo "$(git rev-parse --show-toplevel)/gradle.properties"
}

fetchCurrentVersion() {
    echo $(grep -Eo 'VERSION_NAME=.*' "$(fetchVersionFile)") | awk -F'=' '{print $2}'
}