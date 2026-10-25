package ec.edu.espe.JEQualityFootwear.view;

import ec.edu.espe.JEQualityFootwear.model.Admin;
import ec.edu.espe.JEQualityFootwear.model.Category;
import ec.edu.espe.JEQualityFootwear.model.Customer;
import ec.edu.espe.JEQualityFootwear.model.Inventory;
import ec.edu.espe.JEQualityFootwear.model.Invoice;
import ec.edu.espe.JEQualityFootwear.model.Payment;
import ec.edu.espe.JEQualityFootwear.model.Person;
import ec.edu.espe.JEQualityFootwear.model.Product;
import ec.edu.espe.JEQualityFootwear.model.PromotionCalendar;
import ec.edu.espe.JEQualityFootwear.model.SalesManagement;
import ec.edu.espe.JEQualityFootwear.model.Seller;
import ec.edu.espe.JEQualityFootwear.model.StaffMember;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JEQualityFootwearSystem {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Inventory inventory = new Inventory();
    private static final SalesManagement salesManagement = new SalesManagement(101, 0.05f, 460.0f);
    private static final PromotionCalendar promotionCalendar = new PromotionCalendar(
            "Matriz Calzado Jorge Enriquez",
            "Av. General Enriquez y Abdon Calderon, Sangolqui - Ecuador",
            "0998765432",
            8, 20
    );

    public static void main(String[] args) {
        seedInitialData();
        boolean exitProgram = false;

        while (!exitProgram) {
            System.out.println("\n======================================================================");
            System.out.println("   SISTEMA DE GESTION DE VENTAS E INVENTARIO - CALZADO JORGE ENRIQUEZ ");
            System.out.println("======================================================================");
            System.out.println("Credenciales de prueba listas para usar:");
            System.out.println("  -> Administrador : usuario = admin    | clave = 1234");
            System.out.println("  -> Vendedor 1    : usuario = henry    | clave = 1234");
            System.out.println("  -> Vendedor 2    : usuario = david    | clave = 1234");
            System.out.println("  -> Cliente       : usuario = cliente1 | clave = 1234");
            System.out.println("----------------------------------------------------------------------");
            System.out.println("1. Iniciar Sesion (Administrador / Vendedor / Cliente)");
            System.out.println("2. Crear Cuenta de Usuario / Cliente Independientemente (US003)");
            System.out.println("3. Ver Calendario de Promociones y Estado de Tienda en Vivo (US008)");
            System.out.println("0. Salir del Sistema");
            System.out.print("Seleccione una opcion: ");

            int option = readInt();
            switch (option) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    registerNewCustomerSelfService();
                    break;
                case 3:
                    System.out.println(promotionCalendar.getStoreStatusReport());
                    break;
                case 0:
                    System.out.println("Cerrando el sistema de Calzado Jorge Enriquez. Hasta pronto!");
                    exitProgram = true;
                    break;
                default:
                    System.out.println("[!] Opcion no valida. Intente nuevamente.");
            }
        }
    }

    // ==================== LOGIN Y ENRUTAMIENTO POR ROL (RF-06) ====================
    private static void handleLogin() {
        System.out.println("\n--- INICIO DE SESION ---");
        System.out.print("Usuario: ");
        String username = scanner.nextLine().trim();
        System.out.print("Contrasena: ");
        String password = scanner.nextLine().trim();

        Person loggedUser = salesManagement.authenticateUser(username, password);
        if (loggedUser == null) {
            System.out.println("[ERROR] Credenciales incorrectas. Verifique su usuario y contrasena.");
            System.out.println("[INFO] Si olvido su contrasena, solicite al Administrador que la restablezca.");
            return;
        }

        System.out.println("\n[BIENVENIDO/A] " + loggedUser.getName());
        if (loggedUser instanceof Admin) {
            showAdminMenu((Admin) loggedUser);
        } else if (loggedUser instanceof Seller) {
            showSellerMenu((Seller) loggedUser);
        } else if (loggedUser instanceof Customer) {
            showCustomerMenu((Customer) loggedUser);
        }
    }

    // ==================== MENU ADMINISTRADOR ====================
    private static void showAdminMenu(Admin admin) {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n================ PANEL DE ADMINISTRADOR (" + admin.getName() + ") ================");
            System.out.println("1.  [Inventario - US001] Ver catalogo completo y buscar producto");
            System.out.println("2.  [Inventario - US001] Agregar nuevo modelo de calzado/accesorio");
            System.out.println("3.  [Inventario - US001] Editar producto existente (precio, nombre, stock)");
            System.out.println("4.  [Inventario - US001] Eliminar producto descontinuado");
            System.out.println("5.  [Auditoria  - RF-03] Realizar auditoria fisica de stock");
            System.out.println("6.  [Personal   - US002] Gestionar empleados (Listar, Agregar, Editar, Eliminar)");
            System.out.println("7.  [Usuarios   - US003] Gestionar usuarios/clientes (Ver cuentas, cambiar contrasenas o datos)");
            System.out.println("8.  [Nomina     - US007] Consultar rol de pagos y comisiones de vendedores");
            System.out.println("9.  [Reportes   - US005] Ver Registro de Ventas, Caja y Productos Mas Vendidos");
            System.out.println("10. [Calendario - US008] Agregar nueva fecha de promocion/oferta");
            System.out.println("0.  Cerrar sesion (US003)");
            System.out.print("Seleccione una opcion: ");

            int option = readInt();
            switch (option) {
                case 1:
                    displayCatalogAndSearch();
                    break;
                case 2:
                    addProductMenu();
                    break;
                case 3:
                    editProductMenu();
                    break;
                case 4:
                    deleteProductMenu();
                    break;
                case 5:
                    auditInventoryMenu();
                    break;
                case 6:
                    manageStaffMenu(admin);
                    break;
                case 7:
                    manageUsersAndPasswordsMenu(admin);
                    break;
                case 8:
                    displayPayrollAndCommissions();
                    break;
                case 9:
                    displaySalesAndBestSellersReport();
                    break;
                case 10:
                    addPromotionMenu();
                    break;
                case 0:
                    System.out.println("Sesion de administrador cerrada correctamente.");
                    logout = true;
                    break;
                default:
                    System.out.println("[!] Opcion invalida.");
            }
        }
    }

    // ==================== MENU VENDEDOR / CAJERO ====================
    private static void showSellerMenu(Seller seller) {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n================ PANEL DE VENDEDOR / CAJA (" + seller.getName() + ") ================");
            System.out.println("1. [Catalogo - US004] Ver catalogo y filtrar por Hombres, Mujeres, Ninos, Accesorios");
            System.out.println("2. [Mostrador - US005/US006/US009] Registrar nueva Venta, Cobro en Efectivo y Factura");
            System.out.println("3. [Desempeno - US007] Ver mis ventas realizadas y mis comisiones acumuladas");
            System.out.println("0. Cerrar sesion (US003)");
            System.out.print("Seleccione una opcion: ");

            int option = readInt();
            switch (option) {
                case 1:
                    filterCatalogMenu();
                    break;
                case 2:
                    processCounterSaleMenu(seller);
                    break;
                case 3:
                    displaySellerPerformance(seller);
                    break;
                case 0:
                    System.out.println("Sesion de vendedor cerrada correctamente.");
                    logout = true;
                    break;
                default:
                    System.out.println("[!] Opcion invalida.");
            }
        }
    }

    // ==================== MENU CLIENTE / COMPRADOR ====================
    private static void showCustomerMenu(Customer customer) {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n================ PANEL DE CLIENTE (" + customer.getName() + ") ================");
            System.out.println("1. [Catalogo   - US004] Explorar todos los zapatos y accesorios disponibles");
            System.out.println("2. [Filtros    - US004] Filtrar catalogo por: Hombres, Mujeres, Ninos, Accesorios");
            System.out.println("3. [Calendario - US008] Ver dias de ofertas especiales y estado de la tienda");
            System.out.println("4. [Facturas   - RF-07] Ver mi historial de compras y facturas emitidas");
            System.out.println("5. [Cuenta     - US003] Eliminar mi cuenta de usuario permanentemente");
            System.out.println("0. Cerrar sesion (US003)");
            System.out.print("Seleccione una opcion: ");

            int option = readInt();
            switch (option) {
                case 1:
                    printProductList(inventory.getProducts());
                    break;
                case 2:
                    filterCatalogMenu();
                    break;
                case 3:
                    System.out.println(promotionCalendar.getStoreStatusReport());
                    break;
                case 4:
                    displayCustomerInvoices(customer);
                    break;
                case 5:
                    System.out.print("Esta seguro de que desea eliminar su cuenta? (S/N): ");
                    String confirm = scanner.nextLine().trim();
                    if (confirm.equalsIgnoreCase("S")) {
                        salesManagement.removeCustomerAccount(customer);
                        System.out.println("[OK] Su cuenta ha sido eliminada exitosamente del sistema.");
                        logout = true;
                    }
                    break;
                case 0:
                    System.out.println("Sesion de cliente cerrada. Gracias por visitar Calzado Jorge Enriquez!");
                    logout = true;
                    break;
                default:
                    System.out.println("[!] Opcion invalida.");
            }
        }
    }

    // ==================== FUNCIONES DE INVENTARIO Y CATALOGO ====================
    private static void printProductList(List<Product> list) {
        System.out.println("\n-------------------------------------------------------------------------------------------------------------");
        System.out.println("                                   CATALOGO DE PRODUCTOS - CALZADO JORGE ENRIQUEZ                            ");
        System.out.println("-------------------------------------------------------------------------------------------------------------");
        if (list.isEmpty()) {
            System.out.println("No se encontraron productos registrados para este criterio.");
        } else {
            for (Product p : list) {
                System.out.println(p.toString());
            }
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------");
    }

    private static void displayCatalogAndSearch() {
        printProductList(inventory.getProducts());
        System.out.print("Desea buscar un producto especifico por su codigo? (Ingrese el codigo o presione Enter para volver): ");
        String code = scanner.nextLine().trim();
        if (!code.isEmpty()) {
            Product found = inventory.findProduct(code);
            if (found != null) {
                System.out.println("[PRODUCTO ENCONTRADO]: " + found);
            } else {
                System.out.println("[!] No existe ningun producto con el codigo: " + code);
            }
        }
    }

    private static void filterCatalogMenu() {
        System.out.println("\n--- FILTRAR CATALOGO POR CATEGORIA (US004 / RF-02) ---");
        System.out.println("1. Hombres");
        System.out.println("2. Mujeres");
        System.out.println("3. Ninos");
        System.out.println("4. Accesorios");
        System.out.println("5. Mostrar todos");
        System.out.print("Seleccione la seccion que desea consultar: ");

        int choice = readInt();
        String section = "";
        switch (choice) {
            case 1: section = "Hombres"; break;
            case 2: section = "Mujeres"; break;
            case 3: section = "Ninos"; break;
            case 4: section = "Accesorios"; break;
            case 5:
                printProductList(inventory.getProducts());
                return;
            default:
                System.out.println("[!] Opcion invalida.");
                return;
        }
        List<Product> filtered = inventory.filterBySection(section);
        printProductList(filtered);
    }

    private static void addProductMenu() {
        System.out.println("\n--- REGISTRAR NUEVO PRODUCTO EN INVENTARIO (US001 / RF-01) ---");
        System.out.print("Codigo del producto (ej. ZAP-007): ");
        String code = scanner.nextLine().trim().toUpperCase();
        if (inventory.findProduct(code) != null) {
            System.out.println("[ERROR] Ya existe un producto con el codigo " + code);
            return;
        }
        System.out.print("Nombre o modelo del calzado/accesorio: ");
        String name = scanner.nextLine().trim();
        System.out.print("Seccion (Hombres / Mujeres / Ninos / Accesorios): ");
        String gender = scanner.nextLine().trim();
        System.out.print("Subcategoria (Formal / Deportivo / Botas / Casual / Cuero): ");
        String subCat = scanner.nextLine().trim();
        System.out.print("Color: ");
        String color = scanner.nextLine().trim();
        System.out.print("Talla (numero entero, ej. 40, o 0 si es accesorio): ");
        int size = readInt();
        System.out.print("Precio unitario ($ USD): ");
        float price = readFloat();
        System.out.print("Stock inicial disponible (pares/unidades): ");
        int stock = readInt();

        Category category = new Category(subCat, gender);
        Product newProduct = new Product(code, name, price, stock, category, color, size);
        inventory.addProduct(newProduct);
        System.out.println("[OK] Producto registrado exitosamente en el catalogo:");
        System.out.println(newProduct);
    }

    private static void editProductMenu() {
        printProductList(inventory.getProducts());
        System.out.print("Ingrese el codigo del producto que desea editar: ");
        String code = scanner.nextLine().trim();
        Product product = inventory.findProduct(code);
        if (product == null) {
            System.out.println("[!] Producto no encontrado.");
            return;
        }
        System.out.println("Editando: " + product);
        System.out.print("Nuevo nombre (Enter para mantener '" + product.getName() + "'): ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            product.setName(newName);
        }
        System.out.print("Nuevo precio unitario en $ (Ingrese -1 para mantener $" + product.getPrice() + "): ");
        float newPrice = readFloat();
        if (newPrice >= 0) {
            product.setPrice(newPrice);
        }
        System.out.print("Nuevo stock en almacen (Ingrese -1 para mantener " + product.getCurrentStock() + "): ");
        int newStock = readInt();
        if (newStock >= 0) {
            product.setCurrentStock(newStock);
        }
        System.out.println("[OK] Producto actualizado correctamente: " + product);
    }

    private static void deleteProductMenu() {
        printProductList(inventory.getProducts());
        System.out.print("Ingrese el codigo del producto que desea eliminar del catalogo: ");
        String code = scanner.nextLine().trim();
        if (inventory.removeProduct(code)) {
            System.out.println("[OK] Producto eliminado del inventario activo.");
        } else {
            System.out.println("[!] No se encontro un producto con ese codigo.");
        }
    }

    private static void auditInventoryMenu() {
        System.out.println("\n--- AUDITORIA FISICA DE INVENTARIO (RF-03) ---");
        printProductList(inventory.getProducts());
        System.out.print("Ingrese el codigo del producto contado fisicamente en percha: ");
        String code = scanner.nextLine().trim();
        Product product = inventory.findProduct(code);
        if (product == null) {
            System.out.println("[!] Producto no encontrado.");
            return;
        }
        System.out.println("Stock actual en sistema: " + product.getCurrentStock() + " unidades.");
        System.out.print("Ingrese la cantidad real verificada fisicamente: ");
        int realStock = readInt();
        if (inventory.performAudit(code, realStock)) {
            System.out.println("[OK] Auditoria registrada en fecha: " + inventory.getLastAuditDate());
            System.out.println("Nuevo balance de stock para " + product.getName() + ": " + product.getCurrentStock());
        } else {
            System.out.println("[ERROR] Cantidad invalida.");
        }
    }

    // ==================== GESTION DE EMPLEADOS (US002 / RF-05) ====================
    private static void manageStaffMenu(Admin admin) {
        System.out.println("\n--- GESTION DE PERSONAL DE LA TIENDA (US002 / RF-05) ---");
        System.out.println("1. Listar todos los empleados");
        System.out.println("2. Agregar nuevo empleado (Vendedor o Administrador)");
        System.out.println("3. Editar sueldo base, telefono o contrasena de un empleado");
        System.out.println("4. Eliminar empleado del sistema");
        System.out.print("Seleccione una opcion: ");

        int option = readInt();
        switch (option) {
            case 1:
                System.out.println("\n--- NOMINA ACTIVA DE EMPLEADOS ---");
                for (StaffMember staff : salesManagement.getStaffList()) {
                    System.out.println("  " + staff + " | Usuario: " + staff.getUsername());
                }
                break;
            case 2:
                System.out.print("Codigo numerico de empleado (ej. 205): ");
                int empCode = readInt();
                if (salesManagement.findStaffByCode(empCode) != null) {
                    System.out.println("[ERROR] Ese codigo de empleado ya esta asignado.");
                    return;
                }
                String id = readValidCedulaOrRuc("Cedula (10 digitos) o RUC (13 digitos) del empleado: ");
                if (salesManagement.findPersonByIdOrUsername(id) != null) {
                    System.out.println("[ERROR] Ya existe una persona registrada con esa Cedula/RUC.");
                    return;
                }
                System.out.print("Nombre completo: ");
                String name = scanner.nextLine().trim();
                String phone = readValidPhoneNumber("Telefono (10 digitos, ej. 0991234567): ");
                System.out.print("Correo electronico: ");
                String email = scanner.nextLine().trim();
                System.out.print("Sueldo mensual base ($ USD): ");
                float baseSalary = readFloat();
                System.out.print("Usuario para login: ");
                String user = scanner.nextLine().trim();
                if (salesManagement.findPersonByIdOrUsername(user) != null) {
                    System.out.println("[ERROR] Ese nombre de usuario ya esta en uso.");
                    return;
                }
                System.out.print("Contrasena: ");
                String pass = scanner.nextLine().trim();
                System.out.print("Rol (1 = Vendedor / 2 = Administrador): ");
                int role = readInt();

                StaffMember newStaff;
                if (role == 2) {
                    newStaff = new Admin(empCode, baseSalary, id, name, phone, email, user, pass);
                } else {
                    newStaff = new Seller(empCode, baseSalary, id, name, phone, email, user, pass);
                }
                salesManagement.getStaffList().add(newStaff);
                admin.manageUser(newStaff);
                System.out.println("[OK] Empleado registrado exitosamente.");
                break;
            case 3:
                System.out.print("Ingrese el codigo del empleado a editar: ");
                int editCode = readInt();
                StaffMember staffToEdit = salesManagement.findStaffByCode(editCode);
                if (staffToEdit == null) {
                    System.out.println("[!] Empleado no encontrado.");
                    return;
                }
                System.out.print("Nuevo sueldo base en $ (Ingrese -1 para mantener $" + staffToEdit.getSalary() + "): ");
                float newSal = readFloat();
                if (newSal > 0) {
                    staffToEdit.setSalary(newSal);
                }
                System.out.print("Desea cambiar el telefono actual (" + staffToEdit.getPhoneNumber() + ")? (S/N): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
                    String newPhone = readValidPhoneNumber("Nuevo telefono (10 digitos): ");
                    staffToEdit.setPhoneNumber(newPhone);
                }
                System.out.print("Desea cambiar la contrasena del empleado? (S/N): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
                    System.out.print("Nueva contrasena: ");
                    String newPass = scanner.nextLine().trim();
                    if (!newPass.isEmpty()) {
                        staffToEdit.setPassword(newPass);
                    }
                }
                admin.manageUser(staffToEdit);
                System.out.println("[OK] Datos del empleado actualizados.");
                break;
            case 4:
                System.out.print("Ingrese el codigo del empleado que desea dar de baja: ");
                int delCode = readInt();
                if (delCode == admin.getEmployeeCode()) {
                    System.out.println("[!] No puede eliminar su propio usuario administrador en sesion.");
                    return;
                }
                if (salesManagement.removeStaffByCode(delCode)) {
                    System.out.println("[OK] Empleado eliminado del sistema.");
                } else {
                    System.out.println("[!] No se encontro un empleado con ese codigo.");
                }
                break;
            default:
                System.out.println("[!] Opcion invalida.");
        }
    }

    // ==================== GESTION DE USUARIOS Y CONTRASENAS POR ADMIN (US003 / RF-06 / RF-07) ====================
    private static void manageUsersAndPasswordsMenu(Admin admin) {
        System.out.println("\n--- GESTION DE CUENTAS DE USUARIOS Y RECUPERACION DE ACCESO ---");
        System.out.println("1. Ver lista completa de Usuarios / Clientes y Empleados (con sus credenciales)");
        System.out.println("2. Restablecer / Cambiar contrasena de un usuario que la olvido");
        System.out.println("3. Editar datos personales de un Cliente / Usuario (Nombre, Cedula/RUC, Telefono, Email)");
        System.out.println("4. Eliminar cuenta de un Cliente / Usuario");
        System.out.print("Seleccione una opcion: ");

        int option = readInt();
        switch (option) {
            case 1:
                System.out.println("\n--- DIRECTORIO DE CLIENTES / USUARIOS REGISTRADOS ---");
                if (salesManagement.getCustomerDirectory().isEmpty()) {
                    System.out.println("  No hay clientes registrados.");
                } else {
                    for (Customer c : salesManagement.getCustomerDirectory()) {
                        System.out.printf("  Cod: #%d | Cedula/RUC: %-13s | Nombre: %-20s | Tel: %-10s | Usuario: %-10s | Clave: %s\n",
                                c.getCustomerCode(), c.getId(), c.getName(), c.getPhoneNumber(), c.getUsername(), c.getPassword());
                    }
                }
                System.out.println("\n--- DIRECTORIO DE PERSONAL (ADMINISTRADORES Y VENDEDORES) ---");
                for (StaffMember s : salesManagement.getStaffList()) {
                    String roleName = (s instanceof Admin) ? "ADMIN" : "VENDEDOR";
                    System.out.printf("  Cod: #%d | Rol: %-8s | Cedula/RUC: %-13s | Nombre: %-20s | Tel: %-10s | Usuario: %-10s | Clave: %s\n",
                            s.getEmployeeCode(), roleName, s.getId(), s.getName(), s.getPhoneNumber(), s.getUsername(), s.getPassword());
                }
                break;

            case 2:
                System.out.print("Ingrese la Cedula/RUC o el Nombre de Usuario (username) de la persona: ");
                String query = scanner.nextLine().trim();
                Person targetPerson = salesManagement.findPersonByIdOrUsername(query);
                if (targetPerson == null) {
                    System.out.println("[!] No se encontro ningun usuario o empleado con ese dato.");
                    return;
                }
                System.out.println("[USUARIO LOCALIZADO] -> " + targetPerson.getInfo() + " | Username: " + targetPerson.getUsername());
                System.out.print("Ingrese la nueva contrasena para '" + targetPerson.getUsername() + "': ");
                String newPassword = scanner.nextLine().trim();
                if (newPassword.isEmpty()) {
                    System.out.println("[ERROR] La contrasena no puede estar vacia.");
                    return;
                }
                targetPerson.setPassword(newPassword);
                admin.manageUser(targetPerson);
                System.out.println("[OK] Contrasena actualizada exitosamente para el usuario: " + targetPerson.getUsername());
                break;

            case 3:
                System.out.print("Ingrese la Cedula/RUC del cliente que desea modificar: ");
                String custId = scanner.nextLine().trim();
                Customer customerToEdit = salesManagement.findCustomerById(custId);
                if (customerToEdit == null) {
                    System.out.println("[!] No se encontro un cliente con esa Cedula/RUC.");
                    return;
                }
                System.out.println("Editando cliente: " + customerToEdit.getInfo() + " | Usuario: " + customerToEdit.getUsername());

                System.out.print("Desea corregir la Cedula/RUC? (S/N): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
                    String newId = readValidCedulaOrRuc("Nueva Cedula (10 digitos) o RUC (13 digitos): ");
                    customerToEdit.setId(newId);
                }

                System.out.print("Nuevo nombre (Enter para mantener '" + customerToEdit.getName() + "'): ");
                String newName = scanner.nextLine().trim();
                if (!newName.isEmpty()) {
                    customerToEdit.setName(newName);
                }

                System.out.print("Desea actualizar el numero telefonico? (S/N): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
                    String newPhone = readValidPhoneNumber("Nuevo telefono (10 digitos): ");
                    customerToEdit.setPhoneNumber(newPhone);
                }

                System.out.print("Nuevo correo electronico (Enter para mantener '" + customerToEdit.getEmail() + "'): ");
                String newEmail = scanner.nextLine().trim();
                if (!newEmail.isEmpty()) {
                    customerToEdit.setEmail(newEmail);
                }

                System.out.print("Nuevo username de acceso (Enter para mantener '" + customerToEdit.getUsername() + "'): ");
                String newUsername = scanner.nextLine().trim();
                if (!newUsername.isEmpty()) {
                    customerToEdit.setUsername(newUsername);
                }

                admin.manageUser(customerToEdit);
                System.out.println("[OK] Datos del cliente actualizados correctamente.");
                break;

            case 4:
                System.out.print("Ingrese la Cedula/RUC del cliente cuya cuenta desea eliminar: ");
                String delCustId = scanner.nextLine().trim();
                Customer custToDelete = salesManagement.findCustomerById(delCustId);
                if (custToDelete == null) {
                    System.out.println("[!] Cliente no encontrado.");
                    return;
                }
                salesManagement.removeCustomerAccount(custToDelete);
                System.out.println("[OK] Cuenta del cliente '" + custToDelete.getName() + "' eliminada del sistema.");
                break;

            default:
                System.out.println("[!] Opcion invalida.");
        }
    }

    // ==================== FLUJO REAL DE VENTA EN MOSTRADOR (US005, US006, US009) ====================
    private static void processCounterSaleMenu(Seller seller) {
        System.out.println("\n================ NUEVA VENTA EN MOSTRADOR (POS) ================");
        System.out.println("Vendedor en turno: " + seller.getName() + " (Codigo: " + seller.getEmployeeCode() + ")");

        // 1. Identificar o registrar al cliente con validacion de Cedula/RUC y Telefono (RF-07)
        String customerId = readValidCedulaOrRuc("Ingrese la Cedula (10 digitos) o RUC (13 digitos) del cliente: ");
        Customer customer = salesManagement.findCustomerById(customerId);

        if (customer == null) {
            System.out.println("[INFO] Cliente nuevo. Ingrese sus datos para la factura:");
            System.out.print("Nombre completo del cliente: ");
            String name = scanner.nextLine().trim();
            String phone = readValidPhoneNumber("Telefono del cliente (10 digitos, ej. 0987654321): ");
            System.out.print("Correo electronico (para envio de factura electronica): ");
            String email = scanner.nextLine().trim();
            int newCode = salesManagement.getCustomerDirectory().size() + 1001;
            customer = new Customer(newCode, customerId, name, phone, email, "cli" + newCode, "1234");
            salesManagement.getCustomerDirectory().add(customer);
            System.out.println("[OK] Cliente registrado con codigo #" + newCode + " (Usuario asignado: cli" + newCode + " | Clave: 1234)");
        } else {
            System.out.println("[CLIENTE IDENTIFICADO]: " + customer.getInfo());
        }

        // 2. Seleccion de calzado y validacion de stock en tiempo real (RF-08)
        List<Product> cart = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        boolean addingItems = true;

        while (addingItems) {
            printProductList(inventory.getProducts());
            System.out.print("Ingrese el codigo del calzado/accesorio solicitado por el cliente (o 'FIN' para cobrar): ");
            String prodCode = scanner.nextLine().trim();
            if (prodCode.equalsIgnoreCase("FIN")) {
                break;
            }

            Product selectedProduct = inventory.findProduct(prodCode);
            if (selectedProduct == null) {
                System.out.println("[!] Codigo de producto no encontrado. Intente nuevamente.");
                continue;
            }

            if (inventory.checkStock(selectedProduct) <= 0) {
                System.out.println("[BLOQUEADO - RF-08] El modelo '" + selectedProduct.getName() +
                                   "' tiene 0 pares en stock. No se puede vender.");
                continue;
            }

            System.out.print("Cantidad de pares/unidades a llevar (Disponible en stock: " +
                             selectedProduct.getCurrentStock() + "): ");
            int qty = readInt();

            if (qty <= 0) {
                System.out.println("[!] La cantidad debe ser mayor a 0.");
                continue;
            }

            if (qty > selectedProduct.getCurrentStock()) {
                System.out.println("[ALERTA DE STOCK - RF-08] Stock insuficiente. Solo existen " +
                                   selectedProduct.getCurrentStock() + " unidades disponibles.");
                continue;
            }

            cart.add(selectedProduct);
            quantities.add(qty);
            System.out.println("[AGREGADO AL CARRITO]: " + qty + "x " + selectedProduct.getName() +
                               " ($" + String.format("%.2f", selectedProduct.getPrice() * qty) + ")");

            System.out.print("El cliente desea llevar otro producto adicional? (S/N): ");
            String more = scanner.nextLine().trim();
            if (!more.equalsIgnoreCase("S")) {
                addingItems = false;
            }
        }

        if (cart.isEmpty()) {
            System.out.println("[INFO] Venta cancelada: No se seleccionaron productos.");
            return;
        }

        // 3. Calculo previo de Subtotal, IVA 15% y Total a Pagar
        float subtotal = 0.0f;
        for (int i = 0; i < cart.size(); i++) {
            subtotal += cart.get(i).getPrice() * quantities.get(i);
        }
        float vat = subtotal * Invoice.VAT_RATE;
        float grandTotal = subtotal + vat;

        System.out.println("\n--- RESUMEN DE LA ORDEN DE VENTA ---");
        System.out.printf("Subtotal sin impuestos : $%.2f USD\n", subtotal);
        System.out.printf("IVA (15%%)              : $%.2f USD\n", vat);
        System.out.printf("TOTAL A PAGAR EN CAJA  : $%.2f USD\n", grandTotal);

        // 4. Cobro en efectivo y calculo automatico del vuelto (US006 / RF-09)
        Payment payment = null;
        while (payment == null || !payment.processPayment()) {
            System.out.print("Ingrese el monto en efectivo entregado por el cliente ($ USD): ");
            float cashReceived = readFloat();
            payment = salesManagement.processCashPayment(cashReceived, grandTotal);
            if (!payment.processPayment()) {
                System.out.printf("[ERROR DE PAGO] El efectivo recibido ($%.2f) es menor al total de la factura ($%.2f).\n",
                        cashReceived, grandTotal);
            }
        }

        System.out.printf("\n>>> [CAJA REGISTRADORA] VUELTO EXACTO A ENTREGAR AL CLIENTE: $%.2f USD <<<\n",
                payment.getChangeGiven());

        // 5. Preferencia de facturacion del cliente (US009 / RF-10)
        System.out.println("\nComo prefiere recibir su factura el cliente? (US009 / RF-10):");
        System.out.println("1. Factura Fisica (Impresa en papel en mostrador)");
        System.out.println("2. Factura Electronica (Archivo digital PDF enviado al correo)");
        System.out.println("3. Ambas (Impresa en mostrador + Electronica PDF al correo)");
        System.out.print("Seleccione la preferencia (1/2/3): ");
        int invOption = readInt();

        String channel;
        switch (invOption) {
            case 1:
                channel = "FISICA (IMPRESA EN CAJA)";
                break;
            case 2:
                channel = "ELECTRONICA (PDF VIA EMAIL)";
                break;
            default:
                channel = "FISICA IMPRESA + ELECTRONICA (PDF VIA EMAIL)";
                break;
        }

        // 6. Cierre de venta, descuento de stock, comision al vendedor y emision de factura
        Invoice generatedInvoice = salesManagement.registerSale(
                customer, seller, cart, quantities, salesManagement.getSystemId(), payment, channel
        );

        System.out.println(salesManagement.generateReceipt(generatedInvoice));
        float earnedCommission = subtotal * salesManagement.getCommission();
        System.out.printf("[COMISION GENERADA - US007] El vendedor %s gano +$%.2f USD de comision en esta venta.\n",
                seller.getName(), earnedCommission);
    }

    // ==================== REPORTES, COMISIONES Y CALENDARIO ====================
    private static void displayPayrollAndCommissions() {
        System.out.println("\n================ ROL DE PAGOS Y COMISIONES MENSUALES (US007 / RF-11) ================");
        System.out.printf("Porcentaje de comision vigente sobre ventas: %.1f%%\n", salesManagement.getCommission() * 100);
        System.out.println("-------------------------------------------------------------------------------------");
        for (StaffMember staff : salesManagement.getStaffList()) {
            if (staff instanceof Seller) {
                Seller s = (Seller) staff;
                System.out.printf("Codigo: %d | Vendedor: %-18s | Ventas: %2d | Base: $%6.2f | Comisiones: $%6.2f | TOTAL A PAGAR: $%7.2f USD\n",
                        s.getEmployeeCode(), s.getName(), s.getCompletedSalesCount(),
                        s.getSalary(), s.getAccumulatedCommission(), s.calculateSalary());
            } else if (staff instanceof Admin) {
                System.out.printf("Codigo: %d | Admin   : %-18s | Sueldo Fijo Mensual: $%7.2f USD\n",
                        staff.getEmployeeCode(), staff.getName(), staff.calculateSalary());
            }
        }
        System.out.println("-------------------------------------------------------------------------------------");
    }

    private static void displaySalesAndBestSellersReport() {
        System.out.println("\n================ REGISTRO HISTORICO DE VENTAS Y CAJA (US005) ================");
        System.out.printf("Saldo Contable Actual en Caja Registradora: $%.2f USD\n",
                salesManagement.getCashRegisterBalance());
        if (salesManagement.getSalesRegistry().isEmpty()) {
            System.out.println("Aun no se han registrado ventas en esta sesion.");
        } else {
            for (Invoice inv : salesManagement.getSalesRegistry()) {
                System.out.println("  -> " + inv.toString());
            }
        }

        System.out.println("\n================ REPORTE DE PRODUCTOS MAS VENDIDOS (RF-04) ================");
        List<Product> ranking = inventory.getBestSellingProducts();
        int pos = 1;
        for (Product p : ranking) {
            System.out.printf("%d. %-26s (Cod: %s) | Unidades Vendidas: %3d | Ingresos Generados: $%7.2f USD | Stock Restante: %3d\n",
                    pos++, p.getName(), p.getProductId(), p.getUnitsSold(),
                    (p.getUnitsSold() * p.getPrice()), p.getCurrentStock());
        }
    }

    private static void displaySellerPerformance(Seller seller) {
        System.out.println("\n--- MI DESEMPENO Y COMPENSACION MENSUAL (US007 / RF-11) ---");
        System.out.println("Vendedor                 : " + seller.getName() + " (ID: " + seller.getEmployeeCode() + ")");
        System.out.println("Ventas Cerradas          : " + seller.getCompletedSalesCount());
        System.out.printf("Sueldo Base Mensual      : $%.2f USD\n", seller.getSalary());
        System.out.printf("Comisiones Acumuladas    : $%.2f USD\n", seller.getAccumulatedCommission());
        System.out.printf("SUELDO NETO A RECIBIR    : $%.2f USD\n", seller.calculateSalary());

        System.out.println("\n--- MIS VENTAS REGISTRADAS (US005) ---");
        boolean hasSales = false;
        for (Invoice inv : salesManagement.getSalesRegistry()) {
            if (inv.getSeller().getEmployeeCode() == seller.getEmployeeCode()) {
                System.out.println("  -> " + inv);
                hasSales = true;
            }
        }
        if (!hasSales) {
            System.out.println("  Aun no has registrado ventas en este turno.");
        }
    }

    private static void displayCustomerInvoices(Customer customer) {
        System.out.println("\n--- MI HISTORIAL DE FACTURAS ---");
        if (customer.getPurchaseHistory().isEmpty()) {
            System.out.println("Aun no registra facturas asociadas a su cuenta.");
        } else {
            for (Invoice inv : customer.getPurchaseHistory()) {
                System.out.println(inv.getDetails());
            }
        }
    }

    private static void addPromotionMenu() {
        System.out.println("\n--- AGREGAR PROMOCION AL CALENDARIO DE LA TIENDA (US008 / RF-12) ---");
        System.out.print("Rango de fechas (ej. 01 al 15 de Noviembre): ");
        String dates = scanner.nextLine().trim();
        System.out.print("Descripcion de la oferta (ej. 20% de descuento en calzado formal): ");
        String desc = scanner.nextLine().trim();
        System.out.print("Modelos participantes: ");
        String models = scanner.nextLine().trim();
        promotionCalendar.addPromotion(dates, desc, models);
        System.out.println("[OK] Promocion agregada exitosamente al calendario.");
    }

    private static void registerNewCustomerSelfService() {
        System.out.println("\n--- REGISTRO INDEPENDIENTE DE CUENTA DE USUARIO / CLIENTE (US003 / RF-07) ---");
        String id = readValidCedulaOrRuc("Cedula (10 digitos) o RUC (13 digitos): ");
        if (salesManagement.findPersonByIdOrUsername(id) != null) {
            System.out.println("[ERROR] Ya existe un usuario registrado con esa Cedula/RUC.");
            return;
        }
        System.out.print("Nombre completo: ");
        String name = scanner.nextLine().trim();
        String phone = readValidPhoneNumber("Telefono (10 digitos, ej. 0987654321): ");
        System.out.print("Correo electronico: ");
        String email = scanner.nextLine().trim();
        System.out.print("Elija un nombre de usuario: ");
        String username = scanner.nextLine().trim();
        if (salesManagement.findPersonByIdOrUsername(username) != null) {
            System.out.println("[ERROR] Ese nombre de usuario ya esta en uso. Intente con otro.");
            return;
        }
        System.out.print("Elija una contrasena: ");
        String password = scanner.nextLine().trim();

        int code = salesManagement.getCustomerDirectory().size() + 1001;
        Customer newCustomer = new Customer(code, id, name, phone, email, username, password);
        salesManagement.getCustomerDirectory().add(newCustomer);
        System.out.println("[OK] Cuenta creada exitosamente. Ahora puede iniciar sesion con el usuario: " + username);
    }

    // ==================== DATOS SEMILLA PARA PRUEBA INMEDIATA ====================
    private static void seedInitialData() {
        Admin admin = new Admin(100, 850.00f, "1710000001", "Angel Bonilla (Admin)", "0991112223",
                "angel.bonilla@espe.edu.ec", "admin", "1234");
        Seller seller1 = new Seller(201, 460.00f, "1720000002", "Henry Chalcualan", "0993334445",
                "henry.chalcualan@espe.edu.ec", "henry", "1234");
        Seller seller2 = new Seller(202, 460.00f, "1730000003", "David Clavon", "0995556667",
                "david.clavon@espe.edu.ec", "david", "1234");

        salesManagement.getStaffList().add(admin);
        salesManagement.getStaffList().add(seller1);
        salesManagement.getStaffList().add(seller2);

        Customer customer1 = new Customer(1001, "1712345678", "Carlos Andrade", "0987654321",
                "carlos.andrade@gmail.com", "cliente1", "1234");
        salesManagement.getCustomerDirectory().add(customer1);

        Category menFormal = new Category("Calzado Formal", "Hombres");
        Category menSport = new Category("Calzado Deportivo", "Hombres");
        Category womenBoots = new Category("Botas de Cuero", "Mujeres");
        Category womenCasual = new Category("Calzado Casual", "Mujeres");
        Category kidsSchool = new Category("Calzado Escolar", "Ninos");
        Category leatherAcc = new Category("Marroquineria", "Accesorios");

        inventory.addProduct(new Product("ZAP-001", "Oxford Clasico Cuero", 65.00f, 12, menFormal, "Negro", 41));
        inventory.addProduct(new Product("ZAP-002", "Runner Pro Ligero", 52.50f, 8, menSport, "Azul", 42));
        inventory.addProduct(new Product("ZAP-003", "Botin Elegante Tacon", 78.00f, 6, womenBoots, "Cafe", 37));
        inventory.addProduct(new Product("ZAP-004", "Mocasin Confort Dama", 48.00f, 10, womenCasual, "Beige", 38));
        inventory.addProduct(new Product("ZAP-005", "Zapato Escolar Reforzado", 34.00f, 15, kidsSchool, "Negro", 33));
        inventory.addProduct(new Product("ACC-001", "Cinturon Cuero Legitimo", 18.50f, 20, leatherAcc, "Cafe", 0));

        promotionCalendar.addPromotion("01 al 10 de cada mes", "15% de descuento en linea escolar", "Zapato Escolar Reforzado");
        promotionCalendar.addPromotion("Fines de semana", "2x1 en correas y marroquineria", "Cinturon Cuero Legitimo");
    }

    // ==================== LECTURA Y VALIDACION DE DATOS POR CONSOLA ====================
    private static String readValidCedulaOrRuc(String promptMessage) {
        while (true) {
            System.out.print(promptMessage);
            String input = scanner.nextLine().trim();
            if (!input.matches("\\d+")) {
                System.out.println("[ERROR DE VALIDACION] La Cedula o RUC solo debe contener digitos numericos (0-9).");
                continue;
            }
            if (input.length() == 10) {
                return input; // Cedula valida de 10 digitos
            } else if (input.length() == 13) {
                if (input.endsWith("001")) {
                    return input; // RUC valido de 13 digitos
                } else {
                    System.out.println("[ERROR DE VALIDACION] Un RUC de 13 digitos debe terminar en '001' (ej. 1712345678001).");
                }
            } else {
                System.out.println("[ERROR DE VALIDACION] Cantidad de digitos incorrecta. Ingrese exactamente 10 digitos (Cedula) o 13 digitos terminados en 001 (RUC).");
            }
        }
    }

    private static String readValidPhoneNumber(String promptMessage) {
        while (true) {
            System.out.print(promptMessage);
            String input = scanner.nextLine().trim();
            if (input.matches("\\d{10}")) {
                return input;
            }
            System.out.println("[ERROR DE VALIDACION] El numero telefonico debe contener exactamente 10 digitos numericos (ej. 0991234567).");
        }
    }

    private static int readInt() {
        while (true) {
            try {
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("[!] Ingrese un numero entero valido: ");
            }
        }
    }

    private static float readFloat() {
        while (true) {
            try {
                String line = scanner.nextLine().trim().replace(",", ".");
                return Float.parseFloat(line);
            } catch (NumberFormatException e) {
                System.out.print("[!] Ingrese un valor numerico valido (ej. 45.50): ");
            }
        }
    }
}