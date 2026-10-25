package ec.edu.espe.JEQualityFootwear.model;

public class Person {
    private String id;
    private String name;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;

    public Person(String id, String name, String phoneNumber, String email, String username, String password) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getInfo() {
        return "ID/RUC: " + id + " | Nombre: " + name + " | Tel: " + phoneNumber + " | Email: " + email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Person{id='" + id + "', name='" + name + "', phoneNumber='" + phoneNumber + "', email='" + email + "', username='" + username + "'}";
    }
}