package ec.edu.espe.JEQualityFootwear.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PromotionCalendar {
    private String branchName;
    private String branchAddress;
    private String contactPhone;
    private int openingHour;
    private int closingHour;
    private List<String> scheduledPromotions;

    public PromotionCalendar(String branchName, String branchAddress, String contactPhone, int openingHour, int closingHour) {
        this.branchName = branchName;
        this.branchAddress = branchAddress;
        this.contactPhone = contactPhone;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.scheduledPromotions = new ArrayList<>();
    }

    public void addPromotion(String dateRange, String description, String modelsIncluded) {
        String event = "Fechas: [" + dateRange + "] | Promo: " + description + " | Modelos: " + modelsIncluded;
        this.scheduledPromotions.add(event);
    }

    public boolean isStoreOpen() {
        int currentHour = LocalTime.now().getHour();
        return currentHour >= openingHour && currentHour < closingHour;
    }

    public String getStoreStatusReport() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String currentTime = LocalTime.now().format(formatter);
        String status = isStoreOpen() ? "ABIERTO (Atendiendo en mostrador)" : "CERRADO (Fuera de horario comercial)";
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- INFORMACION DE SUCURSAL Y ESTADO EN TIEMPO REAL ---\n");
        sb.append("Sucursal          : ").append(branchName).append("\n");
        sb.append("Direccion         : ").append(branchAddress).append("\n");
        sb.append("Telefono Contacto : ").append(contactPhone).append("\n");
        sb.append("Horario Atencion  : ").append(String.format("%02d:00 a %02d:00", openingHour, closingHour)).append("\n");
        sb.append("Hora del Sistema  : ").append(currentTime).append("\n");
        sb.append("Estado Actual     : ").append(status).append("\n");
        sb.append("\n--- CALENDARIO DE PROMOCIONES Y NUEVOS LANZAMIENTOS ---\n");
        if (scheduledPromotions.isEmpty()) {
            sb.append("No hay promociones registradas en este momento.\n");
        } else {
            for (int i = 0; i < scheduledPromotions.size(); i++) {
                sb.append("  ").append(i + 1).append(". ").append(scheduledPromotions.get(i)).append("\n");
            }
        }
        return sb.toString();
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public int getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(int openingHour) {
        this.openingHour = openingHour;
    }

    public int getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(int closingHour) {
        this.closingHour = closingHour;
    }

    public List<String> getScheduledPromotions() {
        return scheduledPromotions;
    }

    public void setScheduledPromotions(List<String> scheduledPromotions) {
        this.scheduledPromotions = scheduledPromotions;
    }

    @Override
    public String toString() {
        return "PromotionCalendar{" + "branchName='" + branchName + "', promocionesActivas=" + scheduledPromotions.size() + '}';
    }
}