import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class CheckOut {
    private final IPriceRuleSet ruleSet;
    private List<String> skus = new LinkedList<>();

    public CheckOut(IPriceRuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    public void scan(String s) {
        skus.add(s);
    }

    public int total() {
        return skus.stream()
                .collect(groupingBy(x -> x, counting()))
                .entrySet().stream()
                .map(x -> ruleSet.calculatePriceFor(x.getKey(), x.getValue()))
                .mapToInt(Integer::intValue)
                .sum();
    }
}

