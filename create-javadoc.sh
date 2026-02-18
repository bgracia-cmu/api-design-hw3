#!/bin/bash

rm -rf ./docs
javadoc -d ./docs -Xdoclint:none *.java