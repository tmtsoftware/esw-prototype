# esw-prototype


## Subprojects

* ocs-api 
* ocs-framework
* ocs-react4s-app
* ocs-gateway
* ocs-client
* ocs-react-app
* ocs-testkit
* react4s-facade


## Build Instructions

The build is based on sbt and depends on libraries published to bintray from the 
[csw](https://github.com/tmtsoftware/csw) project.


## Pre-requisites before running Components

run csw-services.sh script using [tmt-deploy readme](https://github.com/tmtsoftware/tmt-deploy)
    - Clone csw-prod
    ```git checkout <sha>```
     (sha is mentioned in Libs.scala for csw-prod dependencies)
    - Run sbt universal:stage
    - ```$cd target/universal/stage/bin```
    - ```$./csw-services.sh start -i en0```

### Run sample assembly
Necessary environment variables  - 
Export clusterSeeds=ip:5552 - Use your own ip
Follow instructions in [readme](https://github.com/Poorva17/sample-assembly-hcd)

## Running esw-sequencer
Necessary environment variables  -
Export clusterSeeds=ip:5552 - Use your own ip
 - Run e.g. 
 ```sbtshell
sbt 
sequencer-scripts-test/reStart iris darknight 8000

```

- Connect to ammonite REPL
```bash
ssh repl@localhost -p8000
```
  

## Run ocs gateway
```sbtshell
sbt
ocs-gateway/reStart
```
Default port for gateway is 9090 you can override it 
e.g.
```sbtshell
sbt
ocs-gateway/reStart 9000
```

## Run scala.js app
```sbtshell
sbt
ocs-react4s-app/fastOptJS::startWebpackDevServer
```

### Open scala.js web app in browser
Open <HOST>:<PORT> 
e.g - localhost:8080 (with webpack dev server)
OR
Open index.html from intelliJ
 
## Run typescript app (ocs-react-app)

```javascript 
npm install
npm start
```

## ocs-client
ocs-client module exposes componentFactory which is simpler API for resolving assembly or sequencer
It also provides standalone app which grants access to components registered with Location service via componentFactory 

```sbtshell
sbt ocs-client/run
```

## ocs-testkit
It provides mocks for cswServices and sequencerCommandService. It allows to unit test scripts logic by mocking cswServices.
Find sample unit test case for script for iris [here](https://github.com/tmtsoftware/sequencer-scripts/blob/master/tests/iris/IrisSharedTest.scala)


## react4s-facade
This module provides facade/interface for reactJs and react-dom javascript libraries to be used in ScalaJs.