# Wordcount application (Jakarta EE version)

# Summary
The wordcount-ee application is the result of the case: counting words. Some develop/configuraton info:
* the application is build with Java 11, Maven 3.6.3 and uses JUnit4.
* during development the Eclipse 2021-03 IDE is used
* development is done on a Linux workstation
* Open Liberty is used as Jakarta EE server
* Arquillian is used as test framework


# Build and test application

```
git clone <repo url>
cd wordcount-ee
mvn clean package
mvn liberty:create liberty:install-feature
mvn liberty:configure-arquillian

mvn failsafe:integration-test
```
The included unit tests are ran automatically and shoud show a result which looks a lot like the output below:

```
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 10.32 s - in nl.krebos.poc.wordcount.ApplicationWordCountIT

[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0

```
  
# Run OpenLiberty standalone:
It is also possible to run the OpenLiberty server locally. Then it is possible to run tests from the command line of for example with a browser with json capabilities.
```
mvn liberty:run
```

# Test standalone via command line with curl
Below some examples of 

```
curl --header "Content-Type: application/json" --request POST --data '{"text":"The sun shines over the lake"}' http://localhost:9080/wordcountee/calculate/highestfrequency

curl --header "Content-Type: application/json" --request POST --data '{"text":"", "n":9}' http://localhost:9080/wordcountee/calculate/mostfrequentnwords

curl --header "Content-Type: application/json" --request POST \
  --data '{"text":"The sun shines over the lake", "word": "the"}' \
  http://localhost:9080/wordcountee/calculate/frequencyforword

curl --header "Content-Type: application/json" --request POST \
  --data '{"text":"The sun shines over the lake", "n":3}' \
  http://localhost:9080/wordcountee/calculate/mostfrequentnwords
  
curl --header "Content-Type: application/json" --request POST \
  --data '{"text":"The sun shines over the lake", "n":9}' \
  http://localhost:9080/wordcountee/calculate/mostfrequentnwords
```
