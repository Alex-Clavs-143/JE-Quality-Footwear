package ec.edu.espe.JEQualityFootwear.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends Person {
    private int customerCode;
    private transient List<Invoice> purchaseHistory;

    public Customer(int customerCode, String id, String name, String phoneNumber, String email, String username, String password) {
        super(id, name, phoneNumber, email, username, password);
        this.customerCode = customerCode;
        this.purchaseHistory = new ArrayList<>();
    }

    public void getInvoice(Invoice invoice) {
        if (invoice != null) {
            this.purchaseHistory.add(invoice);
        }
    }

    public int getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(int customerCode) {
        this.customerCode = customerCode;
    }

    public List<Invoice> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory(List<Invoice> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    @Override
    public String toString() {
        return "Customer{customerCode=" + customerCode + ", " + super.getInfo() + ", comprasRealizadas=" + purchaseHistory.size() + "}";
    }
}