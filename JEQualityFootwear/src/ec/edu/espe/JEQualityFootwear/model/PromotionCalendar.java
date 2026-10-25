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
        String report = "\n--- INFORMACION DE SUCURSAL Y ESTADO EN TIEMPO REAL ---\n"
                + "Sucursal          : " + branchName + "\n"
                + "Direccion         : " + branchAddress + "\n"
                + "Telefono Contacto : " + contactPhone + "\n"
                + "Horario Atencion  : " + openingHour + ":00 a " + closingHour + ":00\n"
                + "Hora del Sistema  : " + currentTime + "\n"
                + "Estado Actual     : " + status + "\n"
                + "\n--- CALENDARIO DE PROMOCIONES Y NUEVOS LANZAMIENTOS ---\n";

        if (scheduledPromotions.isEmpty()) {
            report = report + "No hay promociones registradas en este momento.\n";
        } else {
            for (int i = 0; i < scheduledPromotions.size(); i++) {
                report = report + "  " + (i + 1) + ". " + scheduledPromotions.get(i) + "\n";
            }
        }
        return report;
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
        return "PromotionCalendar{branchName='" + branchName + "', promocionesActivas=" + scheduledPromotions.size() + "}";
    }
}