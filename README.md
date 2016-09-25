# Kata09 - Back To The Checkout
http://codekata.com/kata/kata09-back-to-the-checkout/

Time limit: 3 to 4 hours.

## Theory
Understanding the problem took most of the time when working on that exercise.

My first impression was that we are dealing with a relatively simple functional problem. I have an unordered sequence of possibly repeating elements which need to be *grouped* by identity:
```Clojure
(group-by identity "abaccadcaa")
=> {\a [\a \a \a \a \a], \b [\b], \c [\c \c \c], \d [\d]}
```
Then, in order to apply different rules based on whether I have a single element or a batch of size n, each sub-sequence needs to be *partitioned*:
```Clojure
(partition 2 2 nil [\a \a \a \a \a])
=> ((\a \a) (\a \a) (\a))
```
Now I just have to count the partitions of *n* to get the number of complete batches and count the element(s) of the last incomplete partition (if existing).

On paper this seemed straight-forward, but when starting to implement the algorithm in Java, I couldn't quite wrap my head around it. (I love functional problems, but that doesn't mean that I'm particularly good at them - yet.) When thinking about it, I realized there is a much simpler solution. I really only need the quantity of every item and that is basically it.

First I get the *frequency* of all elements:
```Clojure
(frequencies "abaccadcaa")
=> {\a 5, \b 1, \c 3, \d 1}
```
In Java this is as easy as grouping-by-identity with a counting-collector as the second argument:
```Java
Map<String, Long> result = elements.stream().collect(groupingBy(identity(), counting()));
```
For each pair of `name` and `quantity` I just need the number of complete batches of *n* and the number of elements in the last incomplete batch:
```Java
int batchCount = quantity / batchSize;
int remainder = quantity % batchSize;
```
The final price would be: `batchCount * specialPrice + remainder * unitPrice`.

## Technical
On the implementation is not much to say, really. The provided integration-test suite documents the API of the underlying class and even the dependency on a given rule-set. In terms of extensibility and architecture is not much left to decide.

### Architecture
I'm an advocate of de-coupled architectures mostly using layers technically cut. When cutting by domain it would be the hexagonal approach. In this case however we are talking about exactly three classes, so I just threw them into one single package.

### Extensibility
#### SpecialPrice versus UnitPrice
The tricky case involves three properties for a price:
- price per unit
- price per batch
- batch-size

There are however items which only have a unit-price which would be the simple case. One approach could be to use abstraction and use two types of `IPrice`: `UnitPrice` and `SpecialPrice`. Alternatively `SpecialPrice` could inherit from `UnitPrice` without using an interface. I decided against this approach due to the increasing complexity involved with inheritance. Especially in our example it is easy to represent both cases with `SpecialPrice` by overloading the constructor and giving price-per-unit and price-per-batch the same values while setting the batch-size to 1:
```Java
public SpecialPrice(int unitPrice) {
    this.unitPrice = unitPrice;
    this.batchSize = 1;
    this.batchPrice = unitPrice;
}
```

In a productive world one can be sure there *will* be more variations in the future which might require re-considering this decision. However, since a price is always closely coupled to the price-rule using it, it makes not much sense to force a generalization upon it at this early stage. Let's solve the problems not before they arise.

#### PriceRule versus PriceRuleCollection
When passing the price-rules to the checkout-constructor I considered allowing a collection of rules to be applied: `public CheckOut(IPriceRuleSet ruleSet)` versus `public CheckOut(Collection<IPriceRuleSet> ruleSets)`. I conciously decided against a collection for the following reasons.

From the domain's point of view chaining multiple rule-sets is a very rare use-case. This is due to the fact that a rule will not only map prices, but also reduce them, which makes it impossible for a second rule to work on the same elements again. One valid use-case however could be a general discount for a product, which will be applied after the batches have been calculated/reduced.

From the technical point of view I don't think the checkout should have the responsibility of chaining rules and/or handling multiple rules. The checkout should only sum the prices and apply a rule to them. How the rule looks like and whether we chain multiple rules should not be the business of the checkout. Therefor I would go for using a "composite rule" in that case:
```Java
public class CompositePriceRuleSet implements IPriceRuleSet {
    public CompositePriceRuleSet(Collection<IPriceRuleSet> ruleSets) {...}
}
```
This class accepts a collection of rule-sets and is responsible for chaining them.

## Conclusion
It took me approximately 3 hours and 45 minutes to complete the exercise, not including the time to write this pamphlet.
