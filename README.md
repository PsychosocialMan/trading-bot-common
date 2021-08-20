# trading-bot-common
Trading bot for participation in auctions.

Simple using is to import library trading-bot-common.

For example in Maven:

    <dependency>
      <groupId>de.energy.optimax</groupId>
      <artifactId>trading-bot-common</artifactId>
      <version>1.0.0-RC</version>
    </dependency>
    
Or add jar files implicitly to your classpath.

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
