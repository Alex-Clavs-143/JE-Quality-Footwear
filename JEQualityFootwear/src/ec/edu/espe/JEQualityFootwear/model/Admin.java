package ec.edu.espe.JEQualityFootwear.model;

public class Admin extends StaffMember {

    public Admin(int employeeCode, float salary, String id, String name, String phoneNumber, String email, String username, String password) {
        super(employeeCode, salary, id, name, phoneNumber, email, username, password);
    }

    public void manageUser(Person person) {
        if (person != null) {
            System.out.println("[ADMIN] Gestionando perfil de usuario: " + person.getInfo());
        }
    }

    @Override
    public float calculateSalary() {
        return super.getSalary();
    }

    @Override
    public String toString() {
        return "Admin{employeeCode=" + getEmployeeCode() + ", nombre='" + getName() + "', sueldo=$" + getSalary() + ", " + super.getInfo() + "}";
    }
}