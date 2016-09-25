import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class SpecialPriceRuleSet implements IPriceRuleSet {

    private Map<String, SpecialPrice> priceRules;

    public SpecialPriceRuleSet(Map<String, SpecialPrice> priceRules) {
        this.priceRules = priceRules;
    }

    @Override
    public int calculateTotalPrice(Collection<String> skus) {
        return skus.stream()
                .collect(groupingBy(x -> x, counting()))
                .entrySet().stream()
                .map(x -> calculatePriceFor(x.getKey(), x.getValue()))
                .mapToInt(Integer::intValue)
                .sum();
    }

    private int calculatePriceFor(String name, long quantity) {
        if(!priceRules.containsKey(name)) {
            throw new IllegalArgumentException(String.format("No price rule found for '%s'", name));
        }

        SpecialPrice price = priceRules.get(name);

        int specialBatchSize = price.getBatchSize();
        int specialPrice = price.getBatchPrice();
        int unitPrice = price.getUnitPrice();

        int batchCount = (int) (quantity / specialBatchSize);
        int remainder = (int) (quantity % specialBatchSize);

        return batchCount * specialPrice + remainder * unitPrice;
    }
}
