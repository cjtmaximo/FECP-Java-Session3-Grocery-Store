package org.example;

import java.util.*;

public class Main {
    public static void viewInventory(HashMap<String, Integer> products) {
        // Iterate over products in the inventory
        System.out.println("\nCurrent Inventory:");

        if (products.isEmpty()) {
            System.out.println("There are currently no products in the inventory.\n");
            return;
        }

        for (HashMap.Entry<String, Integer> product: products.entrySet()) {
            System.out.println(product.getKey() + " - " + product.getValue() + " pcs");
        }

        System.out.println(); // Print new line
    }

    public static String addProduct(HashMap<String, Integer> products, String productName, int productQty) {
        if (products.containsKey(productName)) {
            return productName + " is already in the inventory.\n";
        }

        if (productQty <= 0) {
            return "Quantity should be a positive number.\n";
        }

        products.put(productName, productQty);
        return "Product added!\n";
    }

    public static String checkProduct(HashMap<String, Integer> products, String productName) {
        if (!products.containsKey(productName)) {
            return productName + " is currently not in the inventory.\n";
        }

        return productName + " is in stock: " + products.get(productName) + "\n";
    }

    public static String updateStock(HashMap<String, Integer> products, String productName, int productQty) {
        if (!products.containsKey(productName)) {
            return productName + " is currently not in the inventory.\n";
        }

        if (productQty <= 0) {
            return "Quantity should be a positive number.\n";
        }

        products.put(productName, productQty);
        return "Stock updated!\n";
    }

    public static String removeProduct(HashMap<String, Integer> products, String productName) {
        if (!products.containsKey(productName)) {
            return productName + " is currently not in the inventory.\n";
        }

        products.remove(productName);
        return "Product removed.\n";
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isExit = false;

        // Initialize HashMap for products
        HashMap<String, Integer> productsList = new HashMap<>();

        do {
            System.out.println("--- Grocery Inventory Menu ---");
            System.out.println("1. View Inventory");
            System.out.println("2. Add Product");
            System.out.println("3. Check Product");
            System.out.println("4. Update Stock");
            System.out.println("5. Remove Product");
            System.out.println("6. Exit");

            System.out.print("Choose an option: ");
            int userChoice = scanner.nextInt();
            scanner.nextLine(); // Consume new line

            switch (userChoice) {
                case 1:
                    // View Inventory
                    viewInventory(productsList);
                    break;
                case 2:
                    // Add Product
                    System.out.print("Enter product name: ");
                    String productName = scanner.nextLine();

                    System.out.print("Enter quantity: ");
                    int productQty = scanner.nextInt();
                    scanner.nextLine(); // Consume new line

                    String addProductResult = addProduct(productsList, productName, productQty);
                    System.out.println(addProductResult);
                    break;
                case 3:
                    // Check Product
                    System.out.print("Enter product name: ");
                    String productNameToCheck = scanner.nextLine();

                    String checkProductResult = checkProduct(productsList, productNameToCheck);
                    System.out.println(checkProductResult);
                    break;
                case 4:
                    // Update Stock
                    System.out.print("Enter product name to update: ");
                    String productNameToUpdate = scanner.nextLine();

                    System.out.print("Enter new stock quantity: ");
                    int newStockQuantity = scanner.nextInt();
                    scanner.nextLine(); // Consume new line

                    String updateStockResult = updateStock(productsList, productNameToUpdate, newStockQuantity);
                    System.out.println(updateStockResult);
                    break;
                case 5:
                    // Remove Product
                    System.out.print("Enter product name to update: ");
                    String productNameToRemove = scanner.nextLine();

                    String removeProductResult = removeProduct(productsList, productNameToRemove);
                    System.out.println(removeProductResult);
                    break;
                default:
                    isExit = true;
                    System.out.println("Exiting system...");
                    break;
            }
        } while (!isExit);
    }
}