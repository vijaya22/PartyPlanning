# PartyPlanning

### Description
This repo contains code for a program that helps to find the customers who can be invited to a food and drinks party to an office in Dublin. As the customers who are located in the radius of 100km are needed to be invited to the party, this code helps to find such customers by reading the input file which contains coordinates (longitudes, latitudes) of the customers along with their id and names.

### Technologies Used
Java

### Installation / Setup
* Kindly ensure you have a working java environment on the system. For this you can also refer: 
https://docs.oracle.com/javase/10/install/installation-jdk-and-jre-macos.htm#JSJIG-GUID-577CEA7C-E51C-416D-B9C6-B1469F45AC78

* Clone this repository
```
    $ git clone https://github.com/vijaya22/PartyPlanning.git
```
### Running the Code

* Go to the directory where you have cloned the repository using command line terminal.
Move upto the src folder.

* You need to compile the code first to generate the class file. To do this, run: 
```
   javac -cp ".:./Jars/json-20201115.jar" CustomerFilter.java
```
on your command line terminal. This creates the java class file.

* Now you need to run this class file to run the application. To do this, run : 
```
   java -cp ".:./Jars/json-20201115.jar" CustomerFilter
```
on your command line terminal. This starts the application.



### Tests
A set of test cases has been written to the CustomerFilterTest.java file.

To run these test cases, again you need to compile and run the CustomerFilterTest.java file.

For this run these commands on your terminal,

* first compile the file using:
```
   javac -cp Jars/*:. CustomerFilterTest.java
```
* then run using: 
```
   java -cp Jars/*:. org.junit.runner.JUnitCore CustomerFilterTest
```
