package ec.edu.espe.JEQualityFootwear.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalesManagement {
    private int systemId;
    private float commission; // Porcentaje de comision por venta (ej. 0.05f = 5%)
    private float sellerSalary;
    private float cashRegisterBalance;
    private int invoiceSequence;
    private List<Invoice> salesRegistry;
    private List<StaffMember> staffList;
    private List<Customer> customerDirectory;

    public SalesManagement(int systemId, float commission, float sellerSalary) {
        this.systemId = systemId;
        this.commission = commission;
        this.sellerSalary = sellerSalary;
        this.cashRegisterBalance = 200.00f; // Fondo inicial de caja chica ($)
        this.invoiceSequence = 1;
        this.salesRegistry = new ArrayList<>();
        this.staffList = new ArrayList<>();
        this.customerDirectory = new ArrayList<>();
    }

    public Invoice registerSale(Customer customer, Seller seller, List<Product> carrito,
                                List<Integer> quantities, int systemId, Payment payment, String issuanceChannel) {
        String seqNumber = String.format("001-001-%06d", invoiceSequence++);
        Invoice invoice = new Invoice(seqNumber, new Date(), customer, seller, carrito, quantities, issuanceChannel);
        invoice.setCashTendered(payment.getAmountReceived());
        invoice.setChangeDue(payment.getChangeGiven());

        // Descuento automatico de stock en tiempo real (RF-03)
        for (int i = 0; i < carrito.size(); i++) {
            Product product = carrito.get(i);
            int qty = quantities.get(i);
            product.updateStock(-qty);
        }

        // Contabilidad de caja registradora (US006 / RF-09)
        this.cashRegisterBalance += invoice.getTotalAmount();

        // Registro de comision para el vendedor (US007 / RF-11)
        float earnedCommission = invoice.getSubtotal() * this.commission;
        seller.addCommission(earnedCommission);
        seller.sell();

        // Vinculacion con cliente e historial general (US005 / RF-08)
        customer.getInvoice(invoice);
        this.salesRegistry.add(invoice);

        return invoice;
    }

    public Payment processCashPayment(float amountReceived, float totalAmount) {
        Payment payment = new Payment(amountReceived, totalAmount);
        payment.processPayment();
        return payment;
    }

    public String generateReceipt(Invoice invoice) {
        if (invoice == null) {
            return "[ERROR] No se pudo generar el comprobante.";
        }
        StringBuilder receipt = new StringBuilder(invoice.getDetails());
        if (invoice.getIssuanceChannel().contains("FISICA")) {
            receipt.append("[SIMULACION IMPRESORA POS] -> Imprimiendo comprobante fisico en papel termico...\n");
            receipt.append("[SIMULACION IMPRESORA POS] -> Factura fisica entregada al cliente en mostrador.\n");
        }
        if (invoice.getIssuanceChannel().contains("ELECTRONICA")) {
            receipt.append("[SIMULACION SERVIDOR SMTP / PDF] -> Generando archivo digital ")
                   .append(invoice.getInvoiceNumber()).append(".pdf...\n");
            receipt.append("[SIMULACION SERVIDOR SMTP / PDF] -> Factura electronica enviada exitosamente a: ")
                   .append(invoice.getCustomer().getEmail()).append("\n");
        }
        return receipt.toString();
    }

    public void addCommissionToSeller() {
        for (StaffMember staff : staffList) {
            if (staff instanceof Seller) {
                Seller s = (Seller) staff;
                this.sellerSalary = s.calculateSalary();
            }
        }
    }

    public float getSellerSalary(Seller seller) {
        if (seller != null) {
            return seller.calculateSalary();
        }
        return 0.0f;
    }

    public Person authenticateUser(String username, String password) {
        for (StaffMember staff : staffList) {
            if (staff.getUsername().equalsIgnoreCase(username) && staff.getPassword().equals(password)) {
                return staff;
            }
        }
        for (Customer customer : customerDirectory) {
            if (customer.getUsername().equalsIgnoreCase(username) && customer.getPassword().equals(password)) {
                return customer;
            }
        }
        return null;
    }

    public Person findPersonByIdOrUsername(String query) {
        for (Customer customer : customerDirectory) {
            if (customer.getId().equalsIgnoreCase(query) || customer.getUsername().equalsIgnoreCase(query)) {
                return customer;
            }
        }
        for (StaffMember staff : staffList) {
            if (staff.getId().equalsIgnoreCase(query) || staff.getUsername().equalsIgnoreCase(query)) {
                return staff;
            }
        }
        return null;
    }

    public StaffMember findStaffByCode(int employeeCode) {
        for (StaffMember staff : staffList) {
            if (staff.getEmployeeCode() == employeeCode) {
                return staff;
            }
        }
        return null;
    }

    public boolean removeStaffByCode(int employeeCode) {
        StaffMember target = findStaffByCode(employeeCode);
        if (target != null) {
            return staffList.remove(target);
        }
        return false;
    }

    public Customer findCustomerById(String cedulaOrRuc) {
        for (Customer c : customerDirectory) {
            if (c.getId().equalsIgnoreCase(cedulaOrRuc)) {
                return c;
            }
        }
        return null;
    }

    public boolean removeCustomerAccount(Customer customer) {
        return customerDirectory.remove(customer);
    }

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public float getCashRegisterBalance() {
        return cashRegisterBalance;
    }

    public void setCashRegisterBalance(float cashRegisterBalance) {
        this.cashRegisterBalance = cashRegisterBalance;
    }

    public List<Invoice> getSalesRegistry() {
        return salesRegistry;
    }

    public List<StaffMember> getStaffList() {
        return staffList;
    }

    public List<Customer> getCustomerDirectory() {
        return customerDirectory;
    }

    @Override
    public String toString() {
        return "SalesManagement{" + "systemId=" + systemId +
               ", comisionPorcentaje=" + (commission * 100) + "%" +
               ", ventasRegistradas=" + salesRegistry.size() +
               ", saldoEnCaja=$" + String.format("%.2f", cashRegisterBalance) + '}';
    }
}