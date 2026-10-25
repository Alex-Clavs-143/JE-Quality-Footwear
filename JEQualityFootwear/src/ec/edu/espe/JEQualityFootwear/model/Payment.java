package ec.edu.espe.JEQualityFootwear.model;

public class Payment {
    private float amountReceived;
    private float changeGiven;
    private float totalAmount;

    public Payment(float amountReceived, float totalAmount) {
        this.amountReceived = amountReceived;
        this.totalAmount = totalAmount;
        this.changeGiven = 0.0f;
    }

    public float calculateChange(float amountReceived) {
        this.amountReceived = amountReceived;
        if (amountReceived >= totalAmount) {
            this.changeGiven = amountReceived - totalAmount;
        } else {
            this.changeGiven = -1.0f;
        }
        return this.changeGiven;
    }

    public boolean processPayment() {
        if (this.amountReceived >= this.totalAmount && this.totalAmount > 0) {
            calculateChange(this.amountReceived);
            return true;
        }
        return false;
    }

    public float getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(float amountReceived) {
        this.amountReceived = amountReceived;
    }

    public float getChangeGiven() {
        return changeGiven;
    }

    public void setChangeGiven(float changeGiven) {
        this.changeGiven = changeGiven;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Payment{total=$" + totalAmount + ", efectivoRecibido=$" + amountReceived + ", vueltoEntregado=$" + changeGiven + "}";
    }
}