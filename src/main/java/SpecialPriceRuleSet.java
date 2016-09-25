import java.util.Map;

public class SpecialPriceRuleSet implements IPriceRuleSet {

    private Map<String, SpecialPrice> priceRules;

    public SpecialPriceRuleSet(Map<String, SpecialPrice> priceRules) {
        this.priceRules = priceRules;
    }

    @Override
    public int calculatePriceFor(String name, long quantity) {
        if(!priceRules.containsKey(name)) {
            throw new IllegalArgumentException(String.format("No price rule found for '%s'", name));
        }

        SpecialPrice price = priceRules.get(name);

        int specialBatchSize = price.getSpecialBatchSize();
        int specialPrice = price.getSpecialPrice();
        int unitPrice = price.getUnitPrice();

        int batchCount = (int) (quantity / specialBatchSize);
        int remainder = (int) (quantity % specialBatchSize);

        return batchCount * specialPrice + remainder * unitPrice;
    }
}
