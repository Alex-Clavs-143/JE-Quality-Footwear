package ec.edu.espe.JEQualityFootwear.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Inventory {
    private List<Product> products;
    private Date lastAuditDate;

    public Inventory() {
        this.products = new ArrayList<>();
        this.lastAuditDate = new Date();
    }

    public void addProduct(Product product) {
        if (product != null && findProduct(product.getProductId()) == null) {
            products.add(product);
        }
    }

    public boolean removeProduct(String productId) {
        Product target = findProduct(productId);
        if (target != null) {
            return products.remove(target);
        }
        return false;
    }

    public Product findProduct(String productId) {
        for (Product product : products) {
            if (product.getProductId().equalsIgnoreCase(productId)) {
                return product;
            }
        }
        return null;
    }

    public int checkStock(Product product) {
        if (product != null) {
            return product.getCurrentStock();
        }
        return 0;
    }

    public List<Product> filterBySection(String genderSection) {
        List<Product> filtered = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory() != null &&
                product.getCategory().getGender().equalsIgnoreCase(genderSection)) {
                filtered.add(product);
            }
        }
        return filtered;
    }

    public boolean performAudit(String productId, int physicalCount) {
        Product product = findProduct(productId);
        if (product != null && physicalCount >= 0) {
            product.setCurrentStock(physicalCount);
            this.lastAuditDate = new Date();
            return true;
        }
        return false;
    }

    public List<Product> getBestSellingProducts() {
        List<Product> sorted = new ArrayList<>(products);
        sorted.sort(Comparator.comparingInt(Product::getUnitsSold).reversed());
        return sorted;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Date getLastAuditDate() {
        return lastAuditDate;
    }

    public void setLastAuditDate(Date lastAuditDate) {
        this.lastAuditDate = lastAuditDate;
    }

    @Override
    public String toString() {
        return "Inventory{" + "totalModelos=" + products.size() + ", ultimaAuditoria=" + lastAuditDate + '}';
    }
}