# esw-prototype


## Subprojects

* sequencer-api 
* sequencer-framework
* location-agent-simulator
* sequencer-ui-app

## Build Instructions

The build is based on sbt and depends on libraries published to bintray from the 
[csw-prod](https://github.com/tmtsoftware/csw-prod) project.


## Pre-requisites before running Components

run csw-services.sh script
    - Clone csw-prod
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
~sequencer-scripts-test/reStart tcs darknight 7000

```

- Connect to ammonite REPL
```bash
ssh repl@localhost -p7100
```
  
 - Sample command in ammonite REPL- 
 `sequenceFeeder.feed(
    CommandList(Seq(Setup(Prefix("test"), CommandName("setup-tcs"), Some(ObsId("test-obsId")))))
  )`
 

## Run React4s (scala.js) app

Open <HOST_IP>:<SEQUENCER_PORT> 
    - e.g. localhost:7000 which will connect to tcs sequencer

 
## Run typescript app

```javascript 
npm install
npm start
```
