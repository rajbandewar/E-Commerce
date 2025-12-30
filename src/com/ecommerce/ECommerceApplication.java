package com.ecommerce;

import com.ecommerce.exception.InvalidInputException;
import com.ecommerce.logic.ProductLogic;
import com.ecommerce.logic.UserLogic;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ECommerceApplication {
    public static void main(String[] args) {
        new ECommerceApplication().initMenu();
    }

    ProductLogic productLogic = new ProductLogic();
    public void initMenu() {
        System.out.println("==============================\n" +
                " Welcome to E-Commerce App\n" +
                "==============================");
        showMainMenu();
    }

    Scanner sc;

    public void showMainMenu() {
        System.out.println("1. Register\n" +
                "2. Login\n" +
                "3. View Products (Guest)\n" +
                "4. Exit\n" +
                "Enter your choice:");
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
        String firstName = readString("First Name:");
        System.out.println("Enter Last Name:");
        String lastName = sc.next().trim();// allowed tobe blank
        System.out.println("Enter Username:");
        String userName = readString("User Name:");
        System.out.println("Enter Password:");
        String password = readString("Password:");
        System.out.println("Enter City:");
        String city = sc.next().trim(); // allowed tobe blank
        System.out.println("Enter Email:");
        String email = readString("email:");
        System.out.println("Enter Mobile:");
        String mobile = readString("Mobile:");
        User user = new User();
        user.setFirst_name(firstName);
        user.setLast_name(lastName);
        user.setUserName(userName);
        user.setPassword(password);
        user.setCity(city);
        user.setEmail(email);
        user.setMobile(mobile);
        System.out.println("User " + user);
        registerUserOrShowError(user);
    }

    UserLogic userLogic = new UserLogic();

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
                String userName = readString("User Name:");
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
            showRegMenu();
        }
    }

    User loginInUser = null;

    void showLoginUI() {
        System.out.println("Enter Username:");
        String userName = readString("User Name:");
        System.out.println("Enter Password:");
        String password = readString("Password:");
        try {
            UserLogic userLogic = new UserLogic();
            User user = userLogic.login(userName, password);
            if (user != null) {
                if (user.getRole() == 1) {
                    //Admin
                    showAdminMenu(user);
                } else {
                    showUserMenu(user);
                }
                loginInUser = user;
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

            int choice = readInt();

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
                        //viewUserHistoryMenu();
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

    private void viewRegisteredUsers() {
        try {
            List<User> users = userLogic.getAllUsers();

            if (users.isEmpty()) {
                System.out.println("No users registered yet.");
                if (loginInUser != null)
                    showAdminMenu(loginInUser);
                return;
            }

            System.out.println("ID |      NAME      | USERNAME | EMAIL | MOBILE | CITY | ROLE");
            System.out.println("--------------------------------------------------");

            for (User user : users) {
                System.out.println(
                        user.getUserId() + " | " +
                                user.getFirst_name() +
                                user.getLast_name() + " | " +
                                user.getUserName() + " | " +
                                user.getEmail() + " | " +
                                user.getMobile() + " | " +
                                user.getCity() + " | " +
                                user.getRole()
                );
            }
        } catch (Exception e) {

        }
    }

    private void addProductMenu() {

        try {
            System.out.println("\n====== ADD PRODUCT ======");

            System.out.print("Enter Product Id: ");
            int productId = readInt();

            System.out.print("Enter Product Name: ");
            String name = readString("Product Name");

            System.out.print("Enter Product Description: ");
            String description = readString("Product Description");

            System.out.print("Enter Price: ");
            double price = readDouble();

            System.out.print("Enter Quantity: ");
            int quantity = readInt();

            Product product = new Product(
                    productId,
                    name,
                    description,
                    price,
                    quantity
            );

            productLogic.addProduct(product);

            System.out.println("✅ Product added successfully");

        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
            System.out.println("Please re-enter product details.\n");
            addProductMenu();
        }

    }

    private void showUserMenu(User user) {

        System.out.println("\n====== USER MENU ======");
        System.out.println("1. View Products");
        System.out.println("2. Buy Product");
        System.out.println("3. View Cart");
        System.out.println("4. Purchase Items");
        System.out.println("5. Logout");
        System.out.print("Enter choice: ");

        int choice = readInt();

        try {
            switch (choice) {
                case 1:
                    showProducts();
                    break;

                case 2:
                    //buyProductMenu(user);
                    break;

                case 3:
                    //  viewCartMenu(user);
                    break;

                case 4:
                    // purchaseMenu(user);
                    break;

                case 5:
                    System.out.println("Logged out successfully.");
                    logout();
                    break; // back to main menu

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } catch (Exception e) {
            System.out.println(" " + e.getMessage());
        }

    }

    void logout() {
        loginInUser = null;
        showLoginUI();
    }

    void showProducts() {
        List<Product> products = productLogic.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("No products added yet.");
            if (loginInUser != null){
                if (loginInUser.getRole() == 1)
                    showAdminMenu(loginInUser);
                else {
                    showUserMenu(loginInUser);
                }
            }
            return;
        }

        System.out.println("\nID | NAME | DESCRIPTION | PRICE | QTY");
        System.out.println("-----------------------------------------");

        for (Product p : products) {
            System.out.println(
                    p.getProductId() + " | " +
                            p.getProductName() + " | " +
                            p.getDescription() + " | " +
                            p.getPrice() + " | " +
                            p.getQuantity()
            );
        }
    }

    void showCheckProductQuantity() {
        try {
            System.out.println("\n====== CHECK PRODUCT QUANTITY ======");

            System.out.print("Enter Product Id: ");
            int productId = readInt();
            int quantity = productLogic.getQuantity(productId);
            System.out.println("Available Quantity: " + quantity);

        } catch (InvalidInputException e) {
            System.out.println("" + e.getMessage());
            System.out.println("Please try again.\n");
            showCheckProductQuantity();
        }
    }


    private int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        return sc.nextInt();
    }

    private String readString(String fieldName) {
        String input = sc.next().trim();

        while (input.isEmpty()) {
            System.out.print(fieldName + "cannot be empty. Please enter again: ");
            input = sc.nextLine().trim();
        }
        return input;
    }

    private double readDouble() {
        while (!sc.hasNextDouble()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        return sc.nextDouble();
    }
}