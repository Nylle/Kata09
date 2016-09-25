public class SpecialPrice {
    private int unitPrice;
    private int batchSize;
    private int batchPrice;

    public SpecialPrice(int unitPrice, int batchSize, int batchPrice) {
        this.unitPrice = unitPrice;
        this.batchSize = batchSize;
        this.batchPrice = batchPrice;
    }

    public SpecialPrice(int unitPrice) {
        this.unitPrice = unitPrice;
        this.batchSize = 1;
        this.batchPrice = unitPrice;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public int getBatchPrice() {
        return batchPrice;
    }
}
