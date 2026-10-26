package ec.edu.espe.JEQualityFootwear.view;

import ec.edu.espe.JEQualityFootwear.model.Admin;
import ec.edu.espe.JEQualityFootwear.model.Category;
import ec.edu.espe.JEQualityFootwear.model.Customer;
import ec.edu.espe.JEQualityFootwear.model.Inventory;
import ec.edu.espe.JEQualityFootwear.model.Invoice;
import ec.edu.espe.JEQualityFootwear.model.JsonDataManager;
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
    private static final PromotionCalendar promotionCalendar = new PromotionCalendar("", "", "", 8, 20);

    public static void main(String[] args) {
        if (JsonDataManager.loadData(inventory, salesManagement, promotionCalendar)) {
            System.out.println("[SISTEMA] Datos cargados desde 'jequality_data.json' correctamente.");
        } else {
            System.out.println("[ERROR] No se encontro 'jequality_data.json' en la carpeta del proyecto.");
        }

        boolean exitProgram = false;

        while (!exitProgram) {
            System.out.println("\n======================================================================");
            System.out.println("   SISTEMA DE GESTION DE VENTAS E INVENTARIO - CALZADO JORGE ENRIQUEZ ");
            System.out.println("======================================================================");
            System.out.println("1. Iniciar Sesion (Administrador / Vendedor / Cliente)");
            System.out.println("2. Crear Cuenta de Usuario / Cliente Independientemente");
            System.out.println("3. Ver Calendario de Promociones y Estado de Tienda en Vivo");
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
                    saveCurrentState();
                    System.out.println("Datos guardados en 'jequality_data.json'. Cerrando el sistema. Hasta pronto!");
                    exitProgram = true;
                    break;
                default:
                    System.out.println("[!] Opcion no valida. Intente nuevamente.");
            }
        }
    }

    private static void saveCurrentState() {
        JsonDataManager.saveData(inventory, salesManagement, promotionCalendar);
    }

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

    private static void showAdminMenu(Admin admin) {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n================ PANEL DE ADMINISTRADOR (" + admin.getName() + ") ================");
            System.out.println("1.  Ver catalogo completo y buscar producto");
            System.out.println("2.  Agregar nuevo modelo de calzado/accesorio");
            System.out.println("3.  Editar producto existente (precio, nombre, stock)");
            System.out.println("4.  Eliminar producto descontinuado");
            System.out.println("5.  Realizar auditoria fisica de stock");
            System.out.println("6.  Gestionar empleados (Listar, Agregar, Editar, Eliminar)");
            System.out.println("7.  Gestionar usuarios/clientes (Ver cuentas, cambiar contrasenas o datos)");
            System.out.println("8.  Consultar rol de pagos y comisiones de vendedores");
            System.out.println("9.  Ver Registro de Ventas, Caja y Productos Mas Vendidos");
            System.out.println("10. Agregar nueva fecha de promocion/oferta");
            System.out.println("0.  Cerrar sesion");
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

    private static void showSellerMenu(Seller seller) {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n================ PANEL DE VENDEDOR / CAJA (" + seller.getName() + ") ================");
            System.out.println("1. Ver catalogo y filtrar por Hombres, Mujeres, Ninos, Accesorios");
            System.out.println("2. Registrar nueva Venta, Cobro en Efectivo y Factura");
            System.out.println("3. Ver mis ventas realizadas y mis comisiones acumuladas");
            System.out.println("0. Cerrar sesion");
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

    private static void showCustomerMenu(Customer customer) {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n================ PANEL DE CLIENTE (" + customer.getName() + ") ================");
            System.out.println("1. Explorar todos los zapatos y accesorios disponibles");
            System.out.println("2. Filtrar catalogo por: Hombres, Mujeres, Ninos, Accesorios");
            System.out.println("3. Ver dias de ofertas especiales y estado de la tienda");
            System.out.println("4. Ver mi historial de compras y facturas emitidas");
            System.out.println("5. Eliminar mi cuenta de usuario permanentemente");
            System.out.println("0. Cerrar sesion");
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
                        saveCurrentState();
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

    private static void printProductList(List<Product> productList) {
        System.out.println("\n-------------------------------------------------------------------------------------------------------------");
        System.out.println("                                   CATALOGO DE PRODUCTOS - CALZADO JORGE ENRIQUEZ                            ");
        System.out.println("-------------------------------------------------------------------------------------------------------------");
        if (productList.isEmpty()) {
            System.out.println("No se encontraron productos registrados para este criterio.");
        } else {
            for (Product product : productList) {
                System.out.println(product.toString());
            }
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------");
    }

    private static void displayCatalogAndSearch() {
        printProductList(inventory.getProducts());
        System.out.print("Desea buscar un producto especifico por su codigo? (Ingrese el codigo o presione Enter para volver): ");
        String code = scanner.nextLine().trim();
        if (!code.isEmpty()) {
            Product foundProduct = inventory.findProduct(code);
            if (foundProduct != null) {
                System.out.println("[PRODUCTO ENCONTRADO]: " + foundProduct.toString());
            } else {
                System.out.println("[!] No existe ningun producto con el codigo: " + code);
            }
        }
    }

    private static void filterCatalogMenu() {
        System.out.println("\n--- FILTRAR CATALOGO POR CATEGORIA ---");
        System.out.println("1. Hombres");
        System.out.println("2. Mujeres");
        System.out.println("3. Ninos");
        System.out.println("4. Accesorios");
        System.out.println("5. Mostrar todos");
        System.out.print("Seleccione la seccion que desea consultar: ");

        int choice = readInt();
        String section = "";
        switch (choice) {
            case 1:
                section = "Hombres";
                break;
            case 2:
                section = "Mujeres";
                break;
            case 3:
                section = "Ninos";
                break;
            case 4:
                section = "Accesorios";
                break;
            case 5:
                printProductList(inventory.getProducts());
                return;
            default:
                System.out.println("[!] Opcion invalida.");
                return;
        }
        List<Product> filteredProducts = inventory.filterBySection(section);
        printProductList(filteredProducts);
    }

    private static void addProductMenu() {
        System.out.println("\n--- REGISTRAR NUEVO PRODUCTO EN INVENTARIO ---");
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
        String subCategory = scanner.nextLine().trim();
        System.out.print("Color: ");
        String color = scanner.nextLine().trim();
        System.out.print("Talla (numero entero, ej. 40, o 0 si es accesorio): ");
        int size = readInt();
        System.out.print("Precio unitario ($ USD): ");
        float price = readFloat();
        System.out.print("Stock inicial disponible (pares/unidades): ");
        int stock = readInt();

        Category category = new Category(subCategory, gender);
        Product newProduct = new Product(code, name, price, stock, category, color, size);
        inventory.addProduct(newProduct);
        saveCurrentState();
        System.out.println("[OK] Producto registrado exitosamente en el catalogo:");
        System.out.println(newProduct.toString());
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
        System.out.println("Editando: " + product.toString());
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
        saveCurrentState();
        System.out.println("[OK] Producto actualizado correctamente: " + product.toString());
    }

    private static void deleteProductMenu() {
        printProductList(inventory.getProducts());
        System.out.print("Ingrese el codigo del producto que desea eliminar del catalogo: ");
        String code = scanner.nextLine().trim();
        if (inventory.removeProduct(code)) {
            saveCurrentState();
            System.out.println("[OK] Producto eliminado del inventario activo.");
        } else {
            System.out.println("[!] No se encontro un producto con ese codigo.");
        }
    }

    private static void auditInventoryMenu() {
        System.out.println("\n--- AUDITORIA FISICA DE INVENTARIO ---");
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
            saveCurrentState();
            System.out.println("[OK] Auditoria registrada en fecha: " + inventory.getLastAuditDate());
            System.out.println("Nuevo balance de stock para " + product.getName() + ": " + product.getCurrentStock());
        } else {
            System.out.println("[ERROR] Cantidad invalida.");
        }
    }

    private static void manageStaffMenu(Admin admin) {
        System.out.println("\n--- GESTION DE PERSONAL DE LA TIENDA ---");
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
                    System.out.println("  " + staff.toString() + " | Usuario: " + staff.getUsername());
                }
                break;
            case 2:
                System.out.print("Codigo numerico de empleado (ej. 205): ");
                int employeeCode = readInt();
                if (salesManagement.findStaffByCode(employeeCode) != null) {
                    System.out.println("[ERROR] Ese codigo de empleado ya esta asignado.");
                    return;
                }
                String id = readValidIdOrTaxId("Cedula (10 digitos) o RUC (13 digitos) del empleado: ");
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
                    newStaff = new Admin(employeeCode, baseSalary, id, name, phone, email, user, pass);
                } else {
                    newStaff = new Seller(employeeCode, baseSalary, id, name, phone, email, user, pass);
                }
                salesManagement.getStaffList().add(newStaff);
                admin.manageUser(newStaff);
                saveCurrentState();
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
                float newSalary = readFloat();
                if (newSalary > 0) {
                    staffToEdit.setSalary(newSalary);
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
                saveCurrentState();
                System.out.println("[OK] Datos del empleado actualizados.");
                break;
            case 4:
                System.out.print("Ingrese el codigo del empleado que desea dar de baja: ");
                int deleteCode = readInt();
                if (deleteCode == admin.getEmployeeCode()) {
                    System.out.println("[!] No puede eliminar su propio usuario administrador en sesion.");
                    return;
                }
                if (salesManagement.removeStaffByCode(deleteCode)) {
                    saveCurrentState();
                    System.out.println("[OK] Empleado eliminado del sistema.");
                } else {
                    System.out.println("[!] No se encontro un empleado con ese codigo.");
                }
                break;
            default:
                System.out.println("[!] Opcion invalida.");
        }
    }

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
                    for (Customer customer : salesManagement.getCustomerDirectory()) {
                        System.out.println("  Cod: #" + customer.getCustomerCode() + " | Cedula/RUC: " + customer.getId() + " | Nombre: " + customer.getName() + " | Tel: " + customer.getPhoneNumber() + " | Usuario: " + customer.getUsername() + " | Clave: " + customer.getPassword());
                    }
                }
                System.out.println("\n--- DIRECTORIO DE PERSONAL (ADMINISTRADORES Y VENDEDORES) ---");
                for (StaffMember staff : salesManagement.getStaffList()) {
                    String roleName = (staff instanceof Admin) ? "ADMIN" : "VENDEDOR";
                    System.out.println("  Cod: #" + staff.getEmployeeCode() + " | Rol: " + roleName + " | Cedula/RUC: " + staff.getId() + " | Nombre: " + staff.getName() + " | Tel: " + staff.getPhoneNumber() + " | Usuario: " + staff.getUsername() + " | Clave: " + staff.getPassword());
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
                saveCurrentState();
                System.out.println("[OK] Contrasena actualizada exitosamente para el usuario: " + targetPerson.getUsername());
                break;

            case 3:
                System.out.print("Ingrese la Cedula/RUC del cliente que desea modificar: ");
                String customerId = scanner.nextLine().trim();
                Customer customerToEdit = salesManagement.findCustomerById(customerId);
                if (customerToEdit == null) {
                    System.out.println("[!] No se encontro un cliente con esa Cedula/RUC.");
                    return;
                }
                System.out.println("Editando cliente: " + customerToEdit.getInfo() + " | Usuario: " + customerToEdit.getUsername());

                System.out.print("Desea corregir la Cedula/RUC? (S/N): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
                    String newId = readValidIdOrTaxId("Nueva Cedula (10 digitos) o RUC (13 digitos): ");
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
                saveCurrentState();
                System.out.println("[OK] Datos del cliente actualizados correctamente.");
                break;

            case 4:
                System.out.print("Ingrese la Cedula/RUC del cliente cuya cuenta desea eliminar: ");
                String deleteCustomerId = scanner.nextLine().trim();
                Customer customerToDelete = salesManagement.findCustomerById(deleteCustomerId);
                if (customerToDelete == null) {
                    System.out.println("[!] Cliente no encontrado.");
                    return;
                }
                salesManagement.removeCustomerAccount(customerToDelete);
                saveCurrentState();
                System.out.println("[OK] Cuenta del cliente '" + customerToDelete.getName() + "' eliminada del sistema.");
                break;

            default:
                System.out.println("[!] Opcion invalida.");
        }
    }

    private static void processCounterSaleMenu(Seller seller) {
        System.out.println("\n================ NUEVA VENTA ================");
        System.out.println("Vendedor en turno: " + seller.getName() + " (Codigo: " + seller.getEmployeeCode() + ")");

        String customerId = readValidIdOrTaxId("Ingrese la Cedula (10 digitos) o RUC (13 digitos) del cliente: ");
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

        List<Product> shoppingCart = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        boolean addingItems = true;

        while (addingItems) {
            printProductList(inventory.getProducts());
            System.out.print("Ingrese el codigo del calzado/accesorio solicitado por el cliente (o 'FIN' para cobrar): ");
            String productCode = scanner.nextLine().trim();
            if (productCode.equalsIgnoreCase("FIN")) {
                break;
            }

            Product selectedProduct = inventory.findProduct(productCode);
            if (selectedProduct == null) {
                System.out.println("[!] Codigo de producto no encontrado. Intente nuevamente.");
                continue;
            }

            if (inventory.checkStock(selectedProduct) <= 0) {
                System.out.println("[BLOQUEADO] El modelo '" + selectedProduct.getName() + "' tiene 0 pares en stock. No se puede vender.");
                continue;
            }

            System.out.print("Cantidad de pares/unidades a llevar (Disponible en stock: " + selectedProduct.getCurrentStock() + "): ");
            int quantity = readInt();

            if (quantity <= 0) {
                System.out.println("[!] La cantidad debe ser mayor a 0.");
                continue;
            }

            if (quantity > selectedProduct.getCurrentStock()) {
                System.out.println("[ALERTA DE STOCK] Stock insuficiente. Solo existen " + selectedProduct.getCurrentStock() + " unidades disponibles.");
                continue;
            }

            shoppingCart.add(selectedProduct);
            quantities.add(quantity);
            System.out.println("[AGREGADO AL CARRITO]: " + quantity + "x " + selectedProduct.getName() + " ($" + (selectedProduct.getPrice() * quantity) + ")");

            System.out.print("El cliente desea llevar otro producto adicional? (S/N): ");
            String moreItems = scanner.nextLine().trim();
            if (!moreItems.equalsIgnoreCase("S")) {
                addingItems = false;
            }
        }

        if (shoppingCart.isEmpty()) {
            System.out.println("[INFO] Venta cancelada: No se seleccionaron productos.");
            return;
        }

        float subtotal = 0.0f;
        for (int i = 0; i < shoppingCart.size(); i++) {
            subtotal += shoppingCart.get(i).getPrice() * quantities.get(i);
        }
        float vat = subtotal * Invoice.VAT_RATE;
        float grandTotal = subtotal + vat;

        System.out.println("\n--- RESUMEN DE LA ORDEN DE VENTA ---");
        System.out.println("Subtotal sin impuestos : $" + subtotal + " USD");
        System.out.println("IVA (15%)              : $" + vat + " USD");
        System.out.println("TOTAL A PAGAR EN CAJA  : $" + grandTotal + " USD");

        Payment payment = null;
        while (payment == null || !payment.processPayment()) {
            System.out.print("Ingrese el monto en efectivo entregado por el cliente ($ USD): ");
            float cashReceived = readFloat();
            payment = salesManagement.processCashPayment(cashReceived, grandTotal);
            if (!payment.processPayment()) {
                System.out.println("[ERROR DE PAGO] El efectivo recibido ($" + cashReceived + ") es menor al total de la factura ($" + grandTotal + ").");
            }
        }

        System.out.println("\n>>> [CAJA REGISTRADORA] VUELTO EXACTO A ENTREGAR AL CLIENTE: $" + payment.getChangeGiven() + " USD <<<");

        System.out.println("\nComo prefiere recibir su factura el cliente?:");
        System.out.println("1. Factura Fisica (Impresa en papel en mostrador)");
        System.out.println("2. Factura Electronica (Archivo digital PDF enviado al correo)");
        System.out.println("3. Ambas (Impresa en mostrador + Electronica PDF al correo)");
        System.out.print("Seleccione la preferencia (1/2/3): ");
        int invoiceOption = readInt();

        String channel;
        switch (invoiceOption) {
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

        Invoice generatedInvoice = salesManagement.registerSale(
                customer, seller, shoppingCart, quantities, salesManagement.getSystemId(), payment, channel
        );

        saveCurrentState();

        System.out.println(salesManagement.generateReceipt(generatedInvoice));
        float earnedCommission = subtotal * salesManagement.getCommission();
        System.out.println("[COMISION GENERADA] El vendedor " + seller.getName() + " gano +$" + earnedCommission + " USD de comision en esta venta.");
    }

    private static void displayPayrollAndCommissions() {
        System.out.println("\n================ ROL DE PAGOS Y COMISIONES MENSUALES ================");
        System.out.println("Porcentaje de comision vigente sobre ventas: " + (salesManagement.getCommission() * 100) + "%");
        System.out.println("-------------------------------------------------------------------------------------");
        for (StaffMember staff : salesManagement.getStaffList()) {
            if (staff instanceof Seller) {
                Seller seller = (Seller) staff;
                System.out.println("Codigo: " + seller.getEmployeeCode() + " | Vendedor: " + seller.getName() + " | Ventas: " + seller.getCompletedSalesCount() + " | Base: $" + seller.getSalary() + " | Comisiones: $" + seller.getAccumulatedCommission() + " | TOTAL A PAGAR: $" + seller.calculateSalary() + " USD");
            } else if (staff instanceof Admin) {
                System.out.println("Codigo: " + staff.getEmployeeCode() + " | Admin   : " + staff.getName() + " | Sueldo Fijo Mensual: $" + staff.calculateSalary() + " USD");
            }
        }
        System.out.println("-------------------------------------------------------------------------------------");
    }

    private static void displaySalesAndBestSellersReport() {
        System.out.println("\n================ REGISTRO HISTORICO DE VENTAS Y CAJA ================");
        System.out.println("Saldo Contable Actual en Caja Registradora: $" + salesManagement.getCashRegisterBalance() + " USD");
        if (salesManagement.getSalesRegistry().isEmpty()) {
            System.out.println("Aun no se han registrado ventas.");
        } else {
            for (Invoice invoice : salesManagement.getSalesRegistry()) {
                System.out.println("  -> " + invoice.toString());
            }
        }

        System.out.println("\n================ REPORTE DE PRODUCTOS MAS VENDIDOS ================");
        List<Product> ranking = inventory.getBestSellingProducts();
        int position = 1;
        for (Product product : ranking) {
            System.out.println(position + ". " + product.getName() + " (Cod: " + product.getProductId() + ") | Unidades Vendidas: " + product.getUnitsSold() + " | Ingresos Generados: $" + (product.getUnitsSold() * product.getPrice()) + " USD | Stock Restante: " + product.getCurrentStock());
            position++;
        }
    }

    private static void displaySellerPerformance(Seller seller) {
        System.out.println("\n--- MI DESEMPENO Y COMPENSACION MENSUAL ---");
        System.out.println("Vendedor                 : " + seller.getName() + " (ID: " + seller.getEmployeeCode() + ")");
        System.out.println("Ventas Cerradas          : " + seller.getCompletedSalesCount());
        System.out.println("Sueldo Base Mensual      : $" + seller.getSalary() + " USD");
        System.out.println("Comisiones Acumuladas    : $" + seller.getAccumulatedCommission() + " USD");
        System.out.println("SUELDO NETO A RECIBIR    : $" + seller.calculateSalary() + " USD");

        System.out.println("\n--- MIS VENTAS REGISTRADAS ---");
        boolean hasSales = false;
        for (Invoice invoice : salesManagement.getSalesRegistry()) {
            if (invoice.getSeller().getEmployeeCode() == seller.getEmployeeCode()) {
                System.out.println("  -> " + invoice.toString());
                hasSales = true;
            }
        }
        if (!hasSales) {
            System.out.println("  Aun no has registrado ventas.");
        }
    }

    private static void displayCustomerInvoices(Customer customer) {
        System.out.println("\n--- MI HISTORIAL DE FACTURAS ---");
        if (customer.getPurchaseHistory().isEmpty()) {
            System.out.println("Aun no registra facturas asociadas a su cuenta.");
        } else {
            for (Invoice invoice : customer.getPurchaseHistory()) {
                System.out.println(invoice.getDetails());
            }
        }
    }

    private static void addPromotionMenu() {
        System.out.println("\n--- AGREGAR PROMOCION AL CALENDARIO DE LA TIENDA ---");
        System.out.print("Rango de fechas (ej. 01 al 15 de Noviembre): ");
        String dates = scanner.nextLine().trim();
        System.out.print("Descripcion de la oferta (ej. 20% de descuento en calzado formal): ");
        String description = scanner.nextLine().trim();
        System.out.print("Modelos participantes: ");
        String models = scanner.nextLine().trim();
        promotionCalendar.addPromotion(dates, description, models);
        saveCurrentState();
        System.out.println("[OK] Promocion agregada exitosamente al calendario.");
    }

    private static void registerNewCustomerSelfService() {
        System.out.println("\n--- REGISTRO INDEPENDIENTE DE CUENTA DE USUARIO / CLIENTE ---");
        String id = readValidIdOrTaxId("Cedula (10 digitos) o RUC (13 digitos): ");
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
        saveCurrentState();
        System.out.println("[OK] Cuenta creada exitosamente. Ahora puede iniciar sesion con el usuario: " + username);
    }

    private static String readValidIdOrTaxId(String promptMessage) {
        while (true) {
            System.out.print(promptMessage);
            String input = scanner.nextLine().trim();
            if (!input.matches("\\d+")) {
                System.out.println("[ERROR DE VALIDACION] La Cedula o RUC solo debe contener digitos numericos (0-9).");
                continue;
            }
            if (input.length() == 10) {
                return input;
            } else if (input.length() == 13) {
                if (input.endsWith("001")) {
                    return input;
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
            } catch (NumberFormatException exception) {
                System.out.print("[!] Ingrese un numero entero valido: ");
            }
        }
    }

    private static float readFloat() {
        while (true) {
            try {
                String line = scanner.nextLine().trim().replace(",", ".");
                return Float.parseFloat(line);
            } catch (NumberFormatException exception) {
                System.out.print("[!] Ingrese un valor numerico valido (ej. 45.50): ");
            }
        }
    }
}