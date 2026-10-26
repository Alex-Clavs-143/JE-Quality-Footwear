package ec.edu.espe.JEQualityFootwear.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonDataManager {

    private static final String FILE_PATH = "jequality_data.json";
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("dd/MM/yyyy HH:mm:ss")
            .create();

    private static class SystemDatabase {
        int systemId;
        float commission;
        float sellerSalary;
        float cashRegisterBalance;
        int invoiceSequence;
        Date lastAuditDate;
        List<Admin> admins = new ArrayList<>();
        List<Seller> sellers = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        List<Invoice> salesRegistry = new ArrayList<>();
        PromotionCalendar promotionCalendar;
    }

    public static void saveData(Inventory inventory, SalesManagement salesManagement, PromotionCalendar promotionCalendar) {
        SystemDatabase db = new SystemDatabase();
        db.systemId = salesManagement.getSystemId();
        db.commission = salesManagement.getCommission();
        db.sellerSalary = 460.0f;
        db.cashRegisterBalance = salesManagement.getCashRegisterBalance();
        db.invoiceSequence = salesManagement.getSalesRegistry().size() + 1;
        db.lastAuditDate = inventory.getLastAuditDate();

        for (StaffMember staff : salesManagement.getStaffList()) {
            if (staff instanceof Admin) {
                db.admins.add((Admin) staff);
            } else if (staff instanceof Seller) {
                db.sellers.add((Seller) staff);
            }
        }

        db.customers = salesManagement.getCustomerDirectory();
        db.products = inventory.getProducts();
        db.salesRegistry = salesManagement.getSalesRegistry();
        db.promotionCalendar = promotionCalendar;

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(db, writer);
        } catch (IOException e) {
            System.out.println("[ERROR JSON] No se pudo guardar en " + FILE_PATH + ": " + e.getMessage());
        }
    }

    public static boolean loadData(Inventory inventory, SalesManagement salesManagement, PromotionCalendar promotionCalendar) {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return false;
        }

        try (FileReader reader = new FileReader(file)) {
            SystemDatabase db = gson.fromJson(reader, SystemDatabase.class);
            if (db == null) {
                return false;
            }

            inventory.setProducts(db.products != null ? db.products : new ArrayList<>());
            if (db.lastAuditDate != null) {
                inventory.setLastAuditDate(db.lastAuditDate);
            }

            salesManagement.setSystemId(db.systemId);
            salesManagement.setCommission(db.commission);
            salesManagement.setCashRegisterBalance(db.cashRegisterBalance);

            salesManagement.getStaffList().clear();
            if (db.admins != null) {
                salesManagement.getStaffList().addAll(db.admins);
            }
            if (db.sellers != null) {
                salesManagement.getStaffList().addAll(db.sellers);
            }

            salesManagement.getCustomerDirectory().clear();
            if (db.customers != null) {
                for (Customer c : db.customers) {
                    if (c.getPurchaseHistory() == null) {
                        c.setPurchaseHistory(new ArrayList<>());
                    }
                    salesManagement.getCustomerDirectory().add(c);
                }
            }

            salesManagement.getSalesRegistry().clear();
            if (db.salesRegistry != null) {
                for (Invoice inv : db.salesRegistry) {
                    salesManagement.getSalesRegistry().add(inv);
                    if (inv.getCustomer() != null) {
                        Customer registeredCustomer = salesManagement.findCustomerById(inv.getCustomer().getId());
                        if (registeredCustomer != null) {
                            registeredCustomer.getInvoice(inv);
                        }
                    }
                }
            }

            if (db.promotionCalendar != null) {
                promotionCalendar.setBranchName(db.promotionCalendar.getBranchName());
                promotionCalendar.setBranchAddress(db.promotionCalendar.getBranchAddress());
                promotionCalendar.setContactPhone(db.promotionCalendar.getContactPhone());
                promotionCalendar.setOpeningHour(db.promotionCalendar.getOpeningHour());
                promotionCalendar.setClosingHour(db.promotionCalendar.getClosingHour());
                promotionCalendar.setScheduledPromotions(
                        db.promotionCalendar.getScheduledPromotions() != null
                        ? db.promotionCalendar.getScheduledPromotions()
                        : new ArrayList<>()
                );
            }

            return true;
        } catch (IOException e) {
            System.out.println("[ERROR JSON] Fallo al leer el archivo " + FILE_PATH + ": " + e.getMessage());
            return false;
        }
    }
}