package ec.edu.espe.JEQualityFootwear.model;

public class Seller extends StaffMember {
    private float accumulatedCommission;
    private int completedSalesCount;

    public Seller(int employeeCode, float salary, String id, String name, String phoneNumber, String email, String username, String password) {
        super(employeeCode, salary, id, name, phoneNumber, email, username, password);
        this.accumulatedCommission = 0.0f;
        this.completedSalesCount = 0;
    }

    public void sell() {
        this.completedSalesCount++;
    }

    public void addCommission(float commissionAmount) {
        if (commissionAmount > 0) {
            this.accumulatedCommission += commissionAmount;
        }
    }

    @Override
    public float calculateSalary() {
        return super.getSalary() + this.accumulatedCommission;
    }

    public float getAccumulatedCommission() {
        return accumulatedCommission;
    }

    public void setAccumulatedCommission(float accumulatedCommission) {
        this.accumulatedCommission = accumulatedCommission;
    }

    public int getCompletedSalesCount() {
        return completedSalesCount;
    }

    public void setCompletedSalesCount(int completedSalesCount) {
        this.completedSalesCount = completedSalesCount;
    }

    @Override
    public String toString() {
        return "Seller{employeeCode=" + getEmployeeCode() + ", nombre='" + getName()
                + "', sueldoBase=$" + getSalary()
                + ", comisiones=$" + accumulatedCommission
                + ", sueldoTotal=$" + calculateSalary()
                + ", ventasRealizadas=" + completedSalesCount + "}";
    }
}