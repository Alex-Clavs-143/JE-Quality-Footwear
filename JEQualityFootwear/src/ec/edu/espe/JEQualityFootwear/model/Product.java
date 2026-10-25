package ec.edu.espe.JEQualityFootwear.model;

public class Product {
    private String productId;
    private String name;
    private float price;
    private int currentStock;
    private Category category;
    private String color;
    private int size;
    private int unitsSold;

    public Product(String productId, String name, float price, int currentStock, Category category, String color, int size) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.currentStock = currentStock;
        this.category = category;
        this.color = color;
        this.size = size;
        this.unitsSold = 0;
    }

    public void updateStock(int amount) {
        if (this.currentStock + amount >= 0) {
            this.currentStock += amount;
            if (amount < 0) {
                this.unitsSold += Math.abs(amount);
            }
        } else {
            System.out.println("[ALERTA] Operacion rechazada: El stock no puede quedar en negativo.");
        }
    }

    public void assignCategory(Category category) {
        this.category = category;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        if (price >= 0) {
            this.price = price;
        }
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        if (currentStock >= 0) {
            this.currentStock = currentStock;
        }
    }

    public Category getCategory() {
        return category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(int unitsSold) {
        this.unitsSold = unitsSold;
    }

    @Override
    public String toString() {
        return "[" + productId + "] " + name + " | Cat: " + (category != null ? category.toString() : "Sin Categoria")
                + " | Talla: " + size + " | Color: " + color + " | Precio: $" + price
                + " | Stock: " + currentStock + " | Vendidos: " + unitsSold;
    }
}