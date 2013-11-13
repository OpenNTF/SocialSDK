@echo off

set NODEJS_HOME=../tools/node-v0.8.6-x86
set PATH=%NODEJS_HOME%;%APPDATA%/npm;%PATH%;

npm -g install yuidocjs