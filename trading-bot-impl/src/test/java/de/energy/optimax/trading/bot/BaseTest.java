package de.energy.optimax.trading.bot;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@SpringBootTest(
        classes = {TradingBotConfiguration.class}
)
public abstract class BaseTest extends AbstractTestNGSpringContextTests {
}
