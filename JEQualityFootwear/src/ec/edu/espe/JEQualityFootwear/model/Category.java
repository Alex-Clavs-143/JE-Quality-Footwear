package ec.edu.espe.JEQualityFootwear.model;

public class Category {
    private String name;
    private String gender;

    public Category(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }

    public String getCategoryName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float sale(Product product) {
        if (product != null) {
            return product.getPrice();
        }
        return 0.0f;
    }

    @Override
    public String toString() {
        return gender + " - " + name;
    }
}