@echo off

cd /D "%~dp0"

docker run -d -u root ^
  -v \\wsl$\Ubuntu\tmp\.X11-unix:/tmp/.X11-unix ^
  -v \\wsl$\Ubuntu\mnt\wslg:/mnt/wslg ^
  --name=pgq-devenv ^
  --security-opt seccomp=unconfined ^
  -p 3000:3000 ^
  -p 3001:3001 ^
  --shm-size="1gb" ^
  -v %~dp0\..:/config/ProbabilisticGraphQuery ^
  pgq-devenv