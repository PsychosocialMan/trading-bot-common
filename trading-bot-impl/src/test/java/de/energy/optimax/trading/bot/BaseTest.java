package de.energy.optimax.trading.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@SpringBootTest(
        classes = {TradingBotConfiguration.class}
)
public abstract class BaseTest extends AbstractTestNGSpringContextTests {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
}
