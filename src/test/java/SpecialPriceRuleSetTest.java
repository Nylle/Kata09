import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SpecialPriceRuleSetTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void throwsIfRuleNotFound() {
        String unknownArticleName = "A";

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(equalTo(String.format("No price rule found for '%s'", unknownArticleName)));

        SpecialPriceRuleSet sut = new SpecialPriceRuleSet(new HashMap<>());
        sut.calculatePriceFor(unknownArticleName, 1);
    }

    @Test
    public void canHandleSpecialPrice() {
        Map<String, SpecialPrice> priceRules = new HashMap<>();
        priceRules.put("A", new SpecialPrice(10, 3, 20));

        SpecialPriceRuleSet sut = new SpecialPriceRuleSet(priceRules);

        assertThat(sut.calculatePriceFor("A", 1), is(10));
        assertThat(sut.calculatePriceFor("A", 2), is(20));
        assertThat(sut.calculatePriceFor("A", 3), is(20));
        assertThat(sut.calculatePriceFor("A", 4), is(30));
    }

    @Test
    public void canHandleSpecialPriceWithoutSpecialPrice() {
        Map<String, SpecialPrice> priceRules = new HashMap<>();
        priceRules.put("A", new SpecialPrice(10));

        SpecialPriceRuleSet sut = new SpecialPriceRuleSet(priceRules);

        assertThat(sut.calculatePriceFor("A", 1), is(10));
        assertThat(sut.calculatePriceFor("A", 2), is(20));
        assertThat(sut.calculatePriceFor("A", 3), is(30));
    }

    @Test
    public void canHandleZeroPrice() {
        Map<String, SpecialPrice> priceRules = new HashMap<>();
        priceRules.put("A", new SpecialPrice(0));

        SpecialPriceRuleSet sut = new SpecialPriceRuleSet(priceRules);

        assertThat(sut.calculatePriceFor("A", 1), is(0));
        assertThat(sut.calculatePriceFor("A", 2), is(0));
    }


}