# trading-bot-common
Trading bot for participation in auctions.


#### How to install
 - clone repository with `git clone`
 - build the project and deploy it to maven local with `gradle clean build publishToMavenLocal`
 - add `mavenLocal()` to your repository settings
 - add dependencies

For example in Maven:

    <dependency>
      <groupId>de.energy.optimax</groupId>
      <artifactId>trading-bot-api</artifactId>
      <version>1.0.0-RC</version>
    </dependency>
    <dependency>
      <groupId>de.energy.optimax</groupId>
      <artifactId>trading-bot-impl</artifactId>
      <version>1.0.0-RC</version>
    </dependency>    
    
Or add jar files explicitly to your classpath.

Next step is using `Bidder` interface and its implementation. 

If you are using Spring, just add to your configuration annotation `@ImportSmartBidder`. 
Then autowire Bidder interface. For example:

     @Configuration
     public class YourConfig {
         private Bidder bidder;
         
         @Autowired
         public YourConfig(Bidder bidder){
             this.bidder = bidder;
         }
     }

If you don't use Spring application, you can use `TradingBotFactory`.

Usage example:

     var bidder = TradingBotFactory.getBidder();
     
Javadocs stores all the explanations of the used algorithms.
