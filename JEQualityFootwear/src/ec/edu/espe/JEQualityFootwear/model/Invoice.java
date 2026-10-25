package ec.edu.espe.JEQualityFootwear.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Invoice {
    public static final float VAT_RATE = 0.15f;
    public static final String STORE_RUC = "1790012345001";
    public static final String STORE_NAME = "CALZADO JORGE ENRIQUEZ - JE QUALITY FOOTWEAR";

    private String invoiceNumber;
    private Date date;
    private Customer customer;
    private Seller seller;
    private List<Product> purchasedItems;
    private List<Integer> quantities;
    private float subtotal;
    private float vatAmount;
    private float totalAmount;
    private float cashTendered;
    private float changeDue;
    private String issuanceChannel;

    public Invoice(String invoiceNumber, Date date, Customer customer, Seller seller,
                   List<Product> purchasedItems, List<Integer> quantities, String issuanceChannel) {
        this.invoiceNumber = invoiceNumber;
        this.date = date;
        this.customer = customer;
        this.seller = seller;
        this.purchasedItems = purchasedItems;
        this.quantities = quantities;
        this.issuanceChannel = issuanceChannel;
        calculateTotal();
    }

    public final float calculateTotal() {
        this.subtotal = 0.0f;
        if (purchasedItems != null && quantities != null) {
            for (int i = 0; i < purchasedItems.size(); i++) {
                this.subtotal += purchasedItems.get(i).getPrice() * quantities.get(i);
            }
        }
        this.vatAmount = this.subtotal * VAT_RATE;
        this.totalAmount = this.subtotal + this.vatAmount;
        return this.totalAmount;
    }

    public String getDetails() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String details = "\n======================================================================\n"
                + "           " + STORE_NAME + "\n"
                + "                     RUC: " + STORE_RUC + "\n"
                + "======================================================================\n"
                + "FACTURA Nro      : " + invoiceNumber + "\n"
                + "Fecha de Emision : " + dateFormat.format(date) + "\n"
                + "Canal de Entrega : " + issuanceChannel + "\n"
                + "----------------------------------------------------------------------\n"
                + "DATOS DEL CLIENTE:\n"
                + "  Nombre         : " + customer.getName() + "\n"
                + "  Cedula / RUC   : " + customer.getId() + "\n"
                + "  Correo         : " + customer.getEmail() + "\n"
                + "----------------------------------------------------------------------\n"
                + "ATENDIDO POR:\n"
                + "  Vendedor       : " + seller.getName() + " (Cod. Empleado: " + seller.getEmployeeCode() + ")\n"
                + "----------------------------------------------------------------------\n"
                + "CODIGO | MODELO | TALLA | COLOR | CANT | P.UNIT | SUBTOTAL\n"
                + "----------------------------------------------------------------------\n";

        for (int i = 0; i < purchasedItems.size(); i++) {
            Product product = purchasedItems.get(i);
            int quantity = quantities.get(i);
            float lineSubtotal = product.getPrice() * quantity;
            details = details + product.getProductId() + " | " + product.getName() + " | " + product.getSize() + " | "
                    + product.getColor() + " | " + quantity + " | $" + product.getPrice() + " | $" + lineSubtotal + "\n";
        }

        details = details + "----------------------------------------------------------------------\n"
                + "SUBTOTAL SIN IMPUESTOS : $" + subtotal + " USD\n"
                + "IVA (15%)              : $" + vatAmount + " USD\n"
                + "TOTAL A PAGAR          : $" + totalAmount + " USD\n"
                + "EFECTIVO RECIBIDO      : $" + cashTendered + " USD\n"
                + "VUELTO ENTREGADO       : $" + changeDue + " USD\n"
                + "======================================================================\n";
        return details;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public List<Product> getPurchasedItems() {
        return purchasedItems;
    }

    public void setPurchasedItems(List<Product> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }

    public List<Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<Integer> quantities) {
        this.quantities = quantities;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public float getVatAmount() {
        return vatAmount;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public float getCashTendered() {
        return cashTendered;
    }

    public void setCashTendered(float cashTendered) {
        this.cashTendered = cashTendered;
    }

    public float getChangeDue() {
        return changeDue;
    }

    public void setChangeDue(float changeDue) {
        this.changeDue = changeDue;
    }

    public String getIssuanceChannel() {
        return issuanceChannel;
    }

    public void setIssuanceChannel(String issuanceChannel) {
        this.issuanceChannel = issuanceChannel;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return "Factura[" + invoiceNumber + " | Fecha: " + dateFormat.format(date)
                + " | Cliente: " + customer.getName()
                + " | Vendedor: " + seller.getName() + " (ID:" + seller.getEmployeeCode() + ")"
                + " | Total: $" + totalAmount
                + " | Canal: " + issuanceChannel + "]";
    }
}