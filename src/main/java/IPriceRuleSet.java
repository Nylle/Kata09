import java.util.Map;

public interface IPriceRuleSet {
    /***
     * Calculates the price for an article with the provided name and quantity.
     *
     * @param name
     * @param quantity
     * @return
     */
    int calculatePriceFor(String name, long quantity);
}
