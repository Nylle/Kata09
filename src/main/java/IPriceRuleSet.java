import java.util.Collection;

public interface IPriceRuleSet {
    /***
     * Calculates the total price for a collection of SKUs.
     *
     * @param skus
     * @return
     */
    int calculateTotalPrice(Collection<String> skus);
}
