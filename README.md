# Data Engineer - Technical Assessment - Shopping Basket

## How to run the project (executing a JAR file)
- Clone the repo
- At the root directory (directory containing the build.sbt) run `java -cp project/shopping_basket-assembly-0.1.0-SNAPSHOT.jar com.ibtisam.priceBasket.PriceBasket [your items here]`
    * Example: `java -cp project/shopping_basket-assembly-0.1.0-SNAPSHOT.jar com.ibtisam.priceBasket.PriceBasket Apples` 

## VSCode Installation Setup
- Clone the repo
- Make sure you have the latest Java JDK installed (I had OpenJDK 19.0.2 installed at the time of developing)
- Install 'sbt'
    * sbt is a Scala Build Tool. This is similar to Maven and Gradle for Java.
    * To install Scala, it is recommended to use cs setup, the Scala installer powered by Coursier. It installs everything necessary to use the latest Scala release from a command line.
- Install the 'Metals' extension on VSCode. You should get a pop up when you open up VSCode telling you to install the Metals language server. Pop will only 
    * Behind the scenes, Metals uses Bloop to import sbt builds, but you don't need Bloop installed on your machine to run this step. Metals uses Bloop as an embedded build server. 
    * You may also be prompted by `sbt bloopinstall` in VSCode to install Bloop after the Metals installation. Press 'Install' to install Bloop.
    * Bloop helps to connect Metals with sbt. Bloop acts as a bridge between the sbt build tool and Metals. When you open a Scala project in an editor with the Metals extension installed, Metals uses Bloop to communicate with sbt and obtain build information and project settings.


## How to run the project (in the command line):
- `cd` into root directory containing `built.sbt`
- Run sbt. This will open up the sbt console running the sbt server.
- Type `run`to run the programme.

## How to test the project (in the command line):
- Make sure sbt server is running in the console
- Type `test` to run all the tests 
    * Or your can specifiy the target scala test class/file you want to run e.g. `testOnly *TestStarter`

## Background details and Requirements

Write a program driven by unit tests that can price a basket of goods taking into account some special offers.
The goods that can be purchased, together with their normal prices are:

- Soup – 65p per tin
- Bread – 80p per loaf
- Milk – £1.30 per bottle
- Apples – £1.00 per bag

Current special offers
- Apples have a 10% discount off their normal price this week
- Buy 2 tins of soup and get a loaf of bread for half price

The program should accept a list of items in the basket and output the subtotal, the special offer discounts and the final price.
- Input should be via the command line in the form `PriceBasket item1 item2 item3 ...`

Output should be to the console, for example:
```
Subtotal: £3.10
Apples 10% off: 10p
Total price: £3.00
```

If no special offers are applicable the code should output:
```
Subtotal: £1.30
(No offers available)
Total price: £1.30
```