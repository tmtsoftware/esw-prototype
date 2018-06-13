# esw-prototype


## Subprojects

* sequencer-api 
* sequencer-framework

## Build Instructions

The build is based on sbt and depends on libraries published to bintray from the 
[csw-prod](https://github.com/tmtsoftware/csw-prod) project.


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
    
* Clone csw-prod and run sbt universal:publishLocal

## Running esw-sequencer

 - Run e.g. `sbt 'sequencer-framework/test:runMain runner.TestSequencerApp tcs darknight 7000'` 
 - Connect to ammonite REPL - `ssh repl@localhost -p7100`
 - Sample command - 
 `sequenceFeeder.feed(
    CommandList(Seq(Setup(Prefix("test"), CommandName("setup-tcs"), Some(ObsId("test-obsId")))))
  )`
 