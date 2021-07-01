# Masters Of Renaissance

**Software Engineering Final Project A.Y. 2020-2021**

Java implementation of the board game [Masters of Renaissance](https://craniointernational.com/products/masters-of-renaissance/) by Cranio Creations.

**Professor:** Prof. Pierluigi San Pietro

## Gruppo PSP15

- ###     Riccardo Izzo ([@RiccardoIzzo](https://github.com/RiccardoIzzo))
- ###     Gabriele Lazzarelli ([@glazzarelli](https://github.com/glazzarelli))
- ###     Andrea Nocito ([@AndreaNocito](https://github.com/AndreaNocito))


## Implemented features
| Functionality | Status |
|:-----------------------|:------------------------------------:|
| Basic rules | [![GREEN](http://placehold.it/15/44bb44/44bb44)]() |
| Complete rules | [![GREEN](http://placehold.it/15/44bb44/44bb44)]() |
| Socket |[![GREEN](http://placehold.it/15/44bb44/44bb44)]() |
| CLI | [![GREEN](http://placehold.it/15/44bb44/44bb44)]() |
| GUI |[![GREEN](http://placehold.it/15/44bb44/44bb44)]() |
| Multiple games | [![GREEN](http://placehold.it/15/44bb44/44bb44)]()|
| Disconnections | [![GREEN](http://placehold.it/15/44bb44/44bb44)]()|
| Local game | [![RED](http://placehold.it/15/f03c15/f03c15)]() |
| Persistence | [![RED](http://placehold.it/15/f03c15/f03c15)]() |
| Parameters editor | [![RED](http://placehold.it/15/f03c15/f03c15)]() |



## Execution

### System requirements
- [Java SE 15](https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html)
- Maven framework - please refer to [Maven Installation](https://maven.apache.org/install.html)
- OS: Windows, MacOS or Linux

Check your Java and Maven version by typing the following commands:
```
java -version
mvn -version
```

(**WINDOWS ONLY**) To enable ANSI colors on Windows it is recommended to install [GNU Unifont](http://unifoundry.com/unifont/index.html) with one of the following alternatives:
1. [ConEmu](https://conemu.github.io/) terminal
2. Windows Services for Linux (WSL)

### Quick start guide
1. Check the system requirements
2. Clone this repo
3. In the main folder of the repo run the following command:
```
mvn clean package
```
4. Open a terminal, go to the target folder and execute
```
java -jar PSP15-1.0-SNAPSHOT.jar
```
From the jar it's possible to start both the server and the client (CLI/GUI)

## Test coverage

| Package | Coverage (Line) |
|:-----------------------|:------------------------------------:|
| Model | 638/733 (87%)
| Controller | 43/102 (42%)

The low percentage on controller coverage is due to the fat model implementation.  
The uncovered methods are related to the network.

