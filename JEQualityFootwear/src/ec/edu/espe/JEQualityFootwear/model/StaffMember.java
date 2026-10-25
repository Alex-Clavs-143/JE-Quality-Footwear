package ec.edu.espe.JEQualityFootwear.model;

public class StaffMember extends Person {
    private int employeeCode;
    private float salary;

    public StaffMember(int employeeCode, float salary, String id, String name, String phoneNumber, String email, String username, String password) {
        super(id, name, phoneNumber, email, username, password);
        this.employeeCode = employeeCode;
        this.salary = salary;
    }

    public float calculateSalary() {
        return salary;
    }

    public int getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(int employeeCode) {
        this.employeeCode = employeeCode;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "StaffMember{employeeCode=" + employeeCode + ", baseSalary=$" + salary + ", " + super.getInfo() + "}";
    }
}