@echo off

cd /D "%~dp0"

docker build -t pgq-devenv .
