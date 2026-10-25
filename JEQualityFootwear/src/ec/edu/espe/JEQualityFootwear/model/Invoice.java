package ec.edu.espe.JEQualityFootwear.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Invoice {
    public static final float VAT_RATE = 0.15f; // 15% IVA legal en Ecuador
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
    private String issuanceChannel; // "FISICA (IMPRESA EN CAJA)" o "ELECTRONICA (PDF VIA EMAIL)"

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("\n======================================================================\n");
        sb.append("           ").append(STORE_NAME).append("\n");
        sb.append("                     RUC: ").append(STORE_RUC).append("\n");
        sb.append("======================================================================\n");
        sb.append("FACTURA Nro      : ").append(invoiceNumber).append("\n");
        sb.append("Fecha de Emision : ").append(sdf.format(date)).append("\n");
        sb.append("Canal de Entrega : ").append(issuanceChannel).append("\n");
        sb.append("----------------------------------------------------------------------\n");
        sb.append("DATOS DEL CLIENTE:\n");
        sb.append("  Nombre         : ").append(customer.getName()).append("\n");
        sb.append("  Cedula / RUC   : ").append(customer.getId()).append("\n");
        sb.append("  Correo         : ").append(customer.getEmail()).append("\n");
        sb.append("----------------------------------------------------------------------\n");
        sb.append("ATENDIDO POR:\n");
        sb.append("  Vendedor       : ").append(seller.getName())
          .append(" (Cod. Empleado: ").append(seller.getEmployeeCode()).append(")\n");
        sb.append("----------------------------------------------------------------------\n");
        sb.append(String.format("%-8s %-24s %-6s %-10s %-5s %-9s %-9s\n",
                "CODIGO", "MODELO", "TALLA", "COLOR", "CANT", "P.UNIT", "SUBTOTAL"));
        sb.append("----------------------------------------------------------------------\n");
        for (int i = 0; i < purchasedItems.size(); i++) {
            Product p = purchasedItems.get(i);
            int qty = quantities.get(i);
            float lineSubtotal = p.getPrice() * qty;
            sb.append(String.format("%-8s %-24s %-6d %-10s %-5d ",
                    p.getProductId(), p.getName(), p.getSize(), p.getColor(), qty));
            sb.append(String.format("$%-8.2f ", p.getPrice()));
            sb.append(String.format("$%-8.2f\n", lineSubtotal));
        }
        
        sb.append("----------------------------------------------------------------------\n");
        sb.append(String.format("SUBTOTAL SIN IMPUESTOS : $%8.2f USD\n", subtotal));
        sb.append(String.format("IVA (15%%)              : $%8.2f USD\n", vatAmount));
        sb.append(String.format("TOTAL A PAGAR          : $%8.2f USD\n", totalAmount));
        sb.append(String.format("EFECTIVO RECIBIDO      : $%8.2f USD\n", cashTendered));
        sb.append(String.format("VUELTO ENTREGADO       : $%8.2f USD\n", changeDue));
        sb.append("======================================================================\n");
        return sb.toString();
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return "Factura[" + invoiceNumber + " | Fecha: " + sdf.format(date) +
               " | Cliente: " + customer.getName() +
               " | Vendedor: " + seller.getName() + " (ID:" + seller.getEmployeeCode() + ")" +
               " | Total: $" + String.format("%.2f", totalAmount) +
               " | Canal: " + issuanceChannel + "]";
    }
}