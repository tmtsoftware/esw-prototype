# esw-prototype


## Subprojects

* ocs-api 
* ocs-framework
* ocs-react4s-app
* ocs-gateway

## Build Instructions

The build is based on sbt and depends on libraries published to bintray from the 
[csw-prod](https://github.com/tmtsoftware/csw-prod) project.


## Pre-requisites before running Components

run csw-services.sh script
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
 
## Run typescript app

```javascript 
npm install
npm start
```
