import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SpecialPriceRuleSetTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void throwsIfRuleNotFound() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(equalTo("No price rule found for 'A'"));

        SpecialPriceRuleSet sut = new SpecialPriceRuleSet(new HashMap<>());
        sut.calculateTotalPrice(asList("A"));
    }

    @Test
    public void canHandleUnitPrice() {
        Map<String, SpecialPrice> priceRules = new HashMap<>();
        priceRules.put("A", new SpecialPrice(10));

        SpecialPriceRuleSet sut = new SpecialPriceRuleSet(priceRules);

        assertThat(sut.calculateTotalPrice(asList("A")), is(10));
        assertThat(sut.calculateTotalPrice(asList("A", "A")), is(20));
        assertThat(sut.calculateTotalPrice(asList("A", "A", "A")), is(30));
    }

    @Test
    public void canHandleSpecialPrice() {
        Map<String, SpecialPrice> priceRules = new HashMap<>();
        priceRules.put("A", new SpecialPrice(10, 3, 20));

        SpecialPriceRuleSet sut = new SpecialPriceRuleSet(priceRules);

        assertThat(sut.calculateTotalPrice(asList("A")), is(10));
        assertThat(sut.calculateTotalPrice(asList("A", "A")), is(20));
        assertThat(sut.calculateTotalPrice(asList("A", "A", "A")), is(20));
        assertThat(sut.calculateTotalPrice(asList("A", "A", "A", "A")), is(30));
    }

    @Test
    public void canHandleZeroPrice() {
        Map<String, SpecialPrice> priceRules = new HashMap<>();
        priceRules.put("A", new SpecialPrice(0));

        SpecialPriceRuleSet sut = new SpecialPriceRuleSet(priceRules);

        assertThat(sut.calculateTotalPrice(asList("A")), is(0));
        assertThat(sut.calculateTotalPrice(asList("A", "A")), is(0));
    }


}