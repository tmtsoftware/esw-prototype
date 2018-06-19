# esw-prototype


## Subprojects

* sequencer-api 
* sequencer-framework
* location-agent-simulator

## Build Instructions

The build is based on sbt and depends on libraries published to bintray from the 
[csw-prod](https://github.com/tmtsoftware/csw-prod) project.

- Clone csw-prod and checkout to commit 0e6741fac
    - ```git checkout 0e6741fac```

## Pre-requisites before running Components

Make sure that the necessary environment variables are set. For example:

* Set the environment variables (Replace IP address and port with your own values):
```bash
export clusterSeeds=192.168.178.77:7777
```
for bash shell, or 
```csh
setenv clusterSeeds 192.168.178.77:7777
```

run cluster-seed
    - Clone csw-prod
    - Run sbt universal:stage
    - ```$cd target/universal/stage/bin```
    - ```$./csw-cluster-seed --clusterPort=3552```
    
* csw-prod and run sbt universal:publishLocal 
    - ```git checkout 0e6741fac```
    - ```sbt universal:publishLocal```

### Run redis-sentinal and redis-master 
redis-server.conf and redis-sentinal.conf is checked into project directory. Run 
redis-server and redis-sentinal using following commands. 
    -  install redis (mac- ```brew install redis```)
    - ```redis-server redis/redis-server.conf```
    - ```redis-sentinel redis/redis-sentinel.conf```
     

### Run location-agent-simulator app
    - ``` sbt location-agent-simulator/run ```

### Run sample assembly
  Follow instructions in [readme](https://github.com/Poorva17/sample-assembly-hcd)

## Running esw-sequencer

 - Run e.g. `sbt 'sequencer-framework/test:runMain runner.TestSequencerApp tcs darknight 7000'` 
 - Connect to ammonite REPL - `ssh repl@localhost -p7100`
 - Sample command - 
 `sequenceFeeder.feed(
    CommandList(Seq(Setup(Prefix("test"), CommandName("setup-tcs"), Some(ObsId("test-obsId")))))
  )`
 