package com.ecommerce;

import com.ecommerce.exception.InvalidInputException;
import com.ecommerce.logic.CartLogic;
import com.ecommerce.logic.OrderLogic;
import com.ecommerce.logic.ProductLogic;
import com.ecommerce.logic.UserLogic;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.OrderedProduct;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static com.ecommerce.util.InputUtil.*;

public class ECommerceApplication {
    public static void main(String[] args) {
        new ECommerceApplication().initMenu();
    }

    ProductLogic productLogic = new ProductLogic();
    Scanner sc;
    UserLogic userLogic = new UserLogic();
    CartLogic cartLogic=new CartLogic();
    public void initMenu() {
        System.out.println("==============================\n" + " Welcome to E-Commerce App\n" + "==============================");
        showMainMenu();
    }

    public void showMainMenu() {
        loginInUser = null;
        System.out.println("======Main Menu=======");
        System.out.println("1. Register\n" + "2. Login\n" + "3. View Products (Guest)\n" + "4. Exit\n" + "Enter your choice:");
        sc = new Scanner(System.in);
        try {
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    showRegMenu();
                    break;
                case 2:
                    showLoginUI();
                    break;
                case 3:
                    showProducts();
                    break;
                case 4:
                    System.exit(0);
                    break;
                default: {
                    System.out.println("Please enter valid choice");
                    showMainMenu();
                }
            }
        } catch (InputMismatchException mismatch) {
            System.out.println("Please enter your choice number like 2 for login");
            showMainMenu();
        } finally {
            sc.close();
        }
    }

    void showRegMenu() {
        System.out.println("Enter First Name:");
        String firstName = readString(sc, "First Name:");
        System.out.println("Enter Last Name:");
        String lastName = sc.next().trim();// allowed tobe blank
        System.out.println("Enter Username:");
        String userName = readString(sc, "User Name:");
        System.out.println("Enter Password:");
        String password = readString(sc, "Password:");
        System.out.println("Enter City:");
        String city = sc.next().trim(); // allowed tobe blank
        System.out.println("Enter Email:");
        String email = readString(sc, "email:");
        System.out.println("Enter Mobile:");
        String mobile = readString(sc, "Mobile:");
        System.out.println("Enter Role: 1= admin 2 = normal user");
        int role = readInt(sc);
        User user = new User();
        user.setFirst_name(firstName);
        user.setLast_name(lastName);
        user.setUserName(userName);
        user.setPassword(password);
        user.setCity(city);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setRole(role);
        System.out.println("User " + user);
        registerUserOrShowError(user);
    }

    void registerUserOrShowError(User user) {
        try {
            userLogic.register(user);
            System.out.println("User Registered");
            showMainMenu();
        } catch (InvalidInputException e) {
            System.out.println("e.getMessage() " + e.getMessage());
            if ("Username already exists".equals(e.getMessage())) {
                System.out.println("Username already exists Enter new Username :");
                System.out.println("\nOR enter exit to navigate main menu:");
                String userName = readString(sc, "User Name:");
                if ("exit".equals(userName)) {
                    showMainMenu();
                } else {
                    user.setUserName(userName);
                    registerUserOrShowError(user);
                }
            } else {
                System.out.println("Fail to register user please enter details again");
                showRegMenu();
            }
        } catch (Exception e) {
            System.out.println("Fail to register user please enter details again");
            showMainMenu();
        }
    }

    User loginInUser = null;

    void showLoginUI() {
        System.out.println("Enter Username:");
        String userName = readString(sc, "User Name:");
        System.out.println("Enter Password:");
        String password = readString(sc, "Password:");
        try {
            UserLogic userLogic = new UserLogic();
            User user = userLogic.login(userName, password);
            loginInUser = user;
            if (user != null) {
                if (user.getRole() == 1) {
                    //Admin
                    showAdminMenu(user);
                } else {
                    showUserMenu(user);
                }

            } else {
                System.out.println("Failed to login ");
                showLoginUI();
            }

        } catch (InvalidInputException e) {
            System.out.println("Failed to login ");
            showLoginUI();
        }
    }

    private void showAdminMenu(User admin) {

        while (true) {
            System.out.println("\n====== ADMIN MENU ======");
            System.out.println("1. Add Product");
            System.out.println("2. Check Product Quantity");
            System.out.println("3. View Registered Users");
            System.out.println("4. View User Purchase History");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");

            int choice = readInt(sc);

            try {
                switch (choice) {
                    case 1:
                        addProductMenu();
                        break;

                    case 2:
                        showCheckProductQuantity();
                        break;

                    case 3:
                        viewRegisteredUsers();
                        break;
                    case 4:
                        viewUserHistory();
                        break;

                    case 5:
                        System.out.println("Admin logged out.");
                        logout();
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println(" " + e.getMessage());
            }
        }
    }

    private void viewUserHistory() {
        System.out.println("Enter UserName: ");
        String userName = sc.next();
        List<OrderedProduct> orderedProducts = new OrderLogic().getUserOrderHistory(userName);
        if (orderedProducts.isEmpty()) {
            System.out.println("No order placed by user");
            return;
        }
        for (int i = 0; i < orderedProducts.size(); i++) {
            OrderedProduct orderedProduct = orderedProducts.get(i);
            System.out.print((i + 1) + ")");
            System.out.print(" Product id : " + orderedProduct.getProductId());
            System.out.print(" >>Product Description: " + orderedProduct.getDescription());
            System.out.print(" >>Quantity: " + orderedProduct.getQuantity());
            System.out.println("\n");
        }

    }

    private void viewRegisteredUsers() {
        try {
            List<User> users = userLogic.getAllUsers();

            if (users.isEmpty()) {
                System.out.println("No users registered yet.");
                if (loginInUser != null) showAdminMenu(loginInUser);
                return;
            }

            System.out.println("ID |      NAME      | USERNAME | EMAIL | MOBILE | CITY | ROLE");
            System.out.println("--------------------------------------------------");

            for (User user : users) {
                System.out.println(user.getUserId() + " |      " + user.getFirst_name() + " " + user.getLast_name() + "      | " + user.getUserName() + " | " + user.getEmail() + " | " + user.getMobile() + " | " + user.getCity() + " | " + user.getRole());
            }
        } catch (Exception e) {

        }
    }

    private void addProductMenu() {

        try {
            System.out.println("\n====== ADD PRODUCT ======");

            System.out.print("Enter Product Id: ");
            int productId = readInt(sc);

            System.out.print("Enter Product Name: ");
            String name = readString(sc, "Product Name");

            System.out.print("Enter Product Description: ");
            String description = readString(sc, "Product Description");

            System.out.print("Enter Price: ");
            double price = readDouble(sc);

            System.out.print("Enter Quantity: ");
            int quantity = readInt(sc);

            Product product = new Product(productId, name, description, price, quantity);

            productLogic.addProduct(product);

            System.out.println("✅ Product added successfully");

        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
            System.out.println("Please re-enter product details.\n");
            addProductMenu();
        }

    }

    private void showUserMenu(User user) {
        while (true) {
            System.out.println("\n====== USER MENU ======");
            System.out.println("1. View Products");
            System.out.println("2. Buy Product");
            System.out.println("3. View Cart");
            System.out.println("4. Purchase Items");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");

            int choice = readInt(sc);

            try {
                switch (choice) {
                    case 1:
                        showProducts();
                        break;

                    case 2:
                        buyProduct();
                        break;

                    case 3:
                        viewCart();
                        break;

                    case 4:
                        order();
                        break;

                    case 5:
                        System.out.println("Logged out successfully.");
                        logout();
                        break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println(" " + e.getMessage());
                break;
            }
        }
    }

    void logout() {
        loginInUser = null;
        showMainMenu();
    }

    void showProducts() {
        List<Product> products = productLogic.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("No products added yet.");

        } else {
            System.out.println("\nID | NAME | DESCRIPTION | PRICE | QTY");
            System.out.println("-----------------------------------------");

            for (Product p : products) {
                System.out.println(p.getProductId() + " | " + p.getProductName() + " | " + p.getDescription() + " | " + p.getPrice() + " | " + p.getQuantity());
            }
        }
        if (loginInUser != null) {
            if (loginInUser.getRole() == 1) showAdminMenu(loginInUser);
            else {
                showUserMenu(loginInUser);
            }
        } else {
            showMainMenu();
        }

    }

    void showCheckProductQuantity() {
        try {
            System.out.println("\n====== CHECK PRODUCT QUANTITY ======");

            System.out.print("Enter Product Id: ");
            int productId = readInt(sc);
            int quantity = productLogic.getQuantity(productId);
            System.out.println("Available Quantity: " + quantity);

        } catch (InvalidInputException e) {
            System.out.println("" + e.getMessage());
            System.out.println("Please try again.\n");
            showCheckProductQuantity();
        }
    }

    void buyProduct() {
        try {
            if (loginInUser == null) {
                System.out.println("Please login");
                showMainMenu();
            }
            System.out.println("Enter Product ID : ");
            int id = readInt(sc);
            System.out.println("Enter Quantity : ");
            int quantity = readInt(sc);
            cartLogic.addProductToCart(loginInUser.getUserId(), id, quantity);
            System.out.println("Do you want to view cart (Y/N)");
            if ("Y".equalsIgnoreCase(sc.next())) {
                viewCart();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void viewCart() {
        List<CartItem> cartItems = cartLogic.getCartItems(loginInUser.getUserId());
        try {
            System.out.println("\n         NAME          | QTY   ");
            System.out.println("-----------------------------------------");

            for (CartItem ci : cartItems) {
                System.out.println("         " + ci.getProduct().getProductName() + "          | " + ci.getQuantity());
            }
        } catch (Exception e) {

        }
    }

    private void order() {
        try {
            OrderLogic orderLogic = new OrderLogic();
            System.out.println("\n====== CheckOut ======");
            System.out.println("1. Cart");
            System.out.println("2. Single Product");
            System.out.print("Enter choice: ");
            int choice = readInt(sc);
            switch (choice) {
                case 1:
                    orderLogic.orderCart(loginInUser.getUserId());
                    break;
                case 2: {
                    System.out.println("Product Id :");
                    int productId = readInt(sc);
                    System.out.println("Quantity :");
                    int quantity = readInt(sc);
                    orderLogic.orderProduct(loginInUser.getUserId(), productId, quantity);
                }
                break;
            }

        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }

}