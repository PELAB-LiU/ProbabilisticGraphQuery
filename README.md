# Probabilistic Graph Query

This repository contains the source code, measurement data, and development environment that can be used to use, develop, or verify our results.

## Structure
* [Devenv](devenv) folder contains a directory with docker files. The provided dockerfile builds a container that has all dependencies configured for running our measurement campaign. Furthermore, it contains a web-based remote desktop to access a configured development environment.
* [Measurements](measurements) contains our measurement data and post processing scrips. (Access via [localhost:3000](localhost:3000))
* [Sources](sources) contains the project source code.

## Setup

### Install docker
* Install docker engine following the [official guide](https://docs.docker.com/engine/install/).
    * Ensure that you can run docker as a non-root user. See [post-installation guide](https://docs.docker.com/engine/install/linux-postinstall/).

#### Build

Change directory to the repository. Expected location is `.../ProbabilisticGraphQuery$`

Build your own docker image with the following command:
```
docker build -t pgq-devenv devenv/dockerfile
```
Note that downloading eclipse takes a lot of time. Be patient, or download the eclipse instance manually and replace `RUN wget 'https://www.eclipse.org/downloads ...` with `COPY <your file> eclipse.tar.gz`

Alternatively, import the image using the following command:
```
docker image load -i <my image file>
``` 

#### Create container

Create container with the following command
```
docker run -d -u root \
  --name=pgq-devenv \
  --security-opt seccomp=unconfined \
  -p 3000:3000 \
  -p 3001:3001 \
  --shm-size="1gb" \
  -v $(pwd):/config/ProbabilisticGraphQuery \
  pgq-devenv
```

(Delayed) Don't forget to set user to abc for docker exec.
```
docker exec -u abc -it -w /config pgq-devenv /bin/bash
```

(Delayed) You can stop the container with:
```
docker container stop pgq-devenv
```
(Delayed) You can restart the container with:
```
docker container stop pgq-devenv
```

### Configuring container

* Connect to the running container from a browser of your choice via [localhost:3000](localhost:3000)
* Donload jar dependencies.
  * `/config/ProbabilisticGraphQuery/sources/hu.bme.mit.delta.api/libs/download.sh`
  * `/config/ProbabilisticGraphQuery/sources/hu.bme.mit.inf.measurement.utilities/libs/download.sh`
* Launch Eclipse from `Applications -> Development -> Modeling 2024-03`
* Pick a workplace. (Any directory works, but we recommend picking `/config/ProbabilisticGraphQuery/devenv/workplace`)
* Import all projects from  `/config/ProbabilisticGraphQuery/sources` (`File -> Import -> General -> Existing Projects into Workspace -> Next -> Select root directory -> /config/ProbabilisticGraphQuery/sources -> Projects -> Select All -> Finish`)
* Change missing API baseline preferences for `org.eclipse.viatra.query.runtime.rete` to `Warning` or `Ignore`. (`Window -> Preferences -> Plug-in Development -> API Baselines -> Missing API baseline -> Ignore`)
* Import launch configurations from `/config/ProbabilisticGraphQuery/devenv/launch_configs` (`File -> Import -> Run/Debug -> Launch Configurations -> Next -> From Directory -> /config/ProbabilisticGraphQuery/devenv/launch_configs -> tick launch_configs -> Finish`)

### Using the container
* Export runnable jar for measurements: `File -> Export -> Java -> Runnable JAR file -> Next -> Launch configuration (select any, e.g., SAT - Scale - hu.bme.mit.inf.measurement) -> Export destination: /config/ProbabilisticGraphQuery/measurements/pgq.jar -> select Extract required libraries into generated JAR -> Finish -> (Ignore warnings)` 

### Running measurements 
Connect to a bash terminal
```
docker exec -u abc -it -w /config/ProbabilisticGraphQuery/measurements pgq-devenv /bin/bash
```
Run measurements the following commands. (Expected runtime is a few days.) 
```
./measure.sh
```
```
./measure-scale.sh
```

### Post process measurement data

TODO 

## License
* Source code is provided under [Eclipse Public License - v 2.0](https://www.eclipse.org/legal/epl-2.0/) unless other license is indicated
* Delta is provided under [Apache License 2.0](https://github.com/ftsrg/theta/blob/master/LICENSE)


