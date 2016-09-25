public class SpecialPrice {
    private int unitPrice;
    private int specialBatchSize;
    private int specialPrice;

    public SpecialPrice(int unitPrice, int specialBatchSize, int specialPrice) {
        this.unitPrice = unitPrice;
        this.specialBatchSize = specialBatchSize;
        this.specialPrice = specialPrice;
    }

    public SpecialPrice(int unitPrice) {
        this.unitPrice = unitPrice;
        this.specialBatchSize = 1;
        this.specialPrice = unitPrice;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public int getSpecialBatchSize() {
        return specialBatchSize;
    }

    public int getSpecialPrice() {
        return specialPrice;
    }
}
