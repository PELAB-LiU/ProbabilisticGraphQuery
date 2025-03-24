@echo off

cd /D "%~dp0"

docker run -d -u root ^
  -v \\wsl$\Ubuntu\tmp\.X11-unix:/tmp/.X11-unix ^
  -v \\wsl$\Ubuntu\mnt\wslg:/mnt/wslg ^
  --name=pgq-devenv-storm ^
  --security-opt seccomp=unconfined ^
  -p 4100:3389 ^
  --shm-size="1gb" ^
  -v %~dp0\..:/config/ProbabilisticGraphQuery ^
  pgq-devenv 