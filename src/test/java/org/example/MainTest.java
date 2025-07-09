package org.example;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.example.Main.*;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    HashMap<String, Integer> productsList;
    private final ByteArrayOutputStream testOut = new ByteArrayOutputStream();
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setup() {
        productsList = new HashMap<>();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
        testOut.reset();
    }

    @Test
    void testViewInventoryEmpty() {
        viewInventory(productsList);
        String output = testOut.toString();

        assertTrue(output.contains("There are currently no products in the inventory."));
    }

    @Test
    void testViewInventoryNotEmpty() {
        addProduct(productsList, "Cheese", 10);
        viewInventory(productsList);
        String output = testOut.toString();

        assertEquals(10, productsList.get("Cheese"));
        assertTrue(output.contains("Current Inventory:"));
        assertTrue(output.contains("Cheese - 10 pcs"));
    }

    @Test
    void addProductValid() {
        System.out.println(addProduct(productsList, "Banana", 30)); // Still need to print the return value
        String output = testOut.toString();

        assertTrue(productsList.containsKey("Banana"));
        assertEquals(30, productsList.get("Banana"));
        assertTrue(output.contains("Product added!"));
    }

    @Test
    void addProductInvalidZeroQty() {
        System.out.println(addProduct(productsList, "Mango", 0));
        String output = testOut.toString();

        assertTrue(productsList.isEmpty());
        assertTrue(output.contains("Quantity should be a positive number."));
    }

    @Test
    void addProductInvalidNegativeQty() {
        System.out.println(addProduct(productsList, "Mango", -1));
        String output = testOut.toString();

        assertTrue(productsList.isEmpty());
        assertTrue(output.contains("Quantity should be a positive number."));
    }

    @Test
    void addProductInvalidAlreadyExisting() {
        System.out.println(addProduct(productsList, "Milk", 20));
        System.out.println(addProduct(productsList, "Milk", 30));

        String output = testOut.toString();
        assertEquals(20, productsList.get("Milk"), "Milk should still have a stock of 20");
        assertTrue(output.contains("Milk is already in the inventory."));
    }

    @Test
    void checkProductExisting() {
        addProduct(productsList, "Milk", 20);
        System.out.println(checkProduct(productsList, "Milk"));
        String output = testOut.toString();

        assertEquals(20, productsList.get("Milk"));
        assertTrue(output.contains("Milk is in stock: 20"));
    }

    @Test
    void checkProductNotExisting() {
        System.out.println(checkProduct(productsList, "Ice Cream"));
        String output = testOut.toString();

        assertTrue(productsList.isEmpty());
        assertTrue(output.contains("Ice Cream is currently not in the inventory."));
    }

    @Test
    void updateStockValid() {
        addProduct(productsList, "Bread", 10);
        System.out.println(updateStock(productsList, "Bread", 25));
        String output = testOut.toString();

        assertEquals(25, productsList.get("Bread"));
        assertTrue(output.contains("Stock updated!"));
    }

    @Test
    void updateStockInvalidNotExisting() {
        System.out.println(updateStock(productsList, "Tofu", 10));
        String output = testOut.toString();

        assertTrue(productsList.isEmpty());
        assertTrue(output.contains("Tofu is currently not in the inventory."));
    }

    @Test
    void updateStockInvalidZeroQty() {
        addProduct(productsList, "Cheese", 10);
        System.out.println(updateStock(productsList, "Cheese", 0));
        String output = testOut.toString();

        assertEquals(10, productsList.get("Cheese"));
        assertTrue(output.contains("Quantity should be a positive number."));
    }

    @Test
    void updateStockInvalidNegativeQty() {
        addProduct(productsList, "Cheese", 10);
        System.out.println(updateStock(productsList, "Cheese", -5));
        String output = testOut.toString();

        assertEquals(10, productsList.get("Cheese"));
        assertTrue(output.contains("Quantity should be a positive number."));
    }

    @Test
    void removeProductExisting() {
        addProduct(productsList, "Eggs", 10);

        assertEquals(10, productsList.get("Eggs"), "Eggs should have a quantity of 10 prior to removing.");

        System.out.println(removeProduct(productsList, "Eggs"));
        String output = testOut.toString();

        assertFalse(productsList.containsKey("Egg"));
        assertTrue(productsList.isEmpty());
        assertTrue(output.contains("Product removed."));
    }

    @Test
    void removeProductNotExisting() {
        System.out.println(removeProduct(productsList, "Pizza"));
        String output = testOut.toString();

        assertTrue(productsList.isEmpty());
        assertTrue(output.contains("Pizza is currently not in the inventory"));
    }

    // Testing with actual user input
    // Helper to simulate input and capture output
    private String runWithInput(String input) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
        main(new String[]{});

        return testOut.toString();
    }

    @Test
    void viewInventoryThenExit() {
        String input = "1\n6\n";
        String output = runWithInput(input);

        assertTrue(productsList.isEmpty());
        assertTrue(output.contains("There are currently no products in the inventory."));
        assertTrue(output.contains("Exiting system..."));
    }

    @Test
    void addStockThenViewInventoryExit() {
        String input = "2\nCheese\n10\n1\n6\n";
        String output = runWithInput(input);

        assertTrue(output.contains("Product added!"));
        assertTrue(output.contains("Cheese - 10 pcs"));
        assertTrue(output.contains("Exiting system..."));
    }

    @Test
    void addStockExistingThenViewInventoryExit() {
        String input = "2\nCheese\n10\n2\nCheese\n30\n1\n6\n";
        String output = runWithInput(input);

        assertTrue(output.contains("Product added!"));
        assertTrue(output.contains("Cheese - 10 pcs"));
        assertTrue(output.contains("Cheese is already in the inventory."));
        assertTrue(output.contains("Exiting system..."));
    }

    @Test
    void checkExistingProductThenExit() {
        String input = "2\nCheese\n10\n3\nCheese\n1\n6\n";
        String output = runWithInput(input);

        assertTrue(output.contains("Product added!"));
        assertTrue(output.contains("Cheese is in stock: 10"));
        assertTrue(output.contains("Cheese - 10 pcs"));
        assertTrue(output.contains("Exiting system..."));
    }

    @Test
    void checkNonExistingProductThenExit() {
        String input = "3\nCheese\n6\n";
        String output = runWithInput(input);

        assertTrue(output.contains("Cheese is currently not in the inventory."));
        assertTrue(output.contains("Exiting system..."));
    }

    @Test
    void updateStockExistingThenExit() {
        String input = "2\nCheese\n10\n4\nCheese\n30\n6\n";
        String output = runWithInput(input);

        assertTrue(output.contains("Product added!"));
        assertTrue(output.contains("Stock updated!"));
        assertTrue(output.contains("Exiting system..."));
    }

    @Test
    void updateStockNotExistingThenExit() {
        String input = "4\nCheese\n30\n6\n";
        String output = runWithInput(input);

        assertTrue(output.contains("Cheese is currently not in the inventory."));
        assertTrue(output.contains("Exiting system..."));
    }

    @Test
    void removeExistingProductThenExit() {
        String input = "2\nCheese\n10\n5\nCheese\n6\n";
        String output = runWithInput(input);

        assertTrue(output.contains("Product added!"));
        assertTrue(output.contains("Product removed."));
        assertTrue(output.contains("Exiting system..."));
    }

    @Test
    void removeNonExistingProductThenExit() {
        String input = "5\nCheese\n6\n";
        String output = runWithInput(input);

        assertTrue(output.contains("Cheese is currently not in the inventory."));
        assertTrue(output.contains("Exiting system..."));
    }

    @Test
    void numberOtherThanOneToSixShouldExit() {
        String input = "420\n";
        String output = runWithInput(input);

        assertTrue(output.contains("Exiting system..."));
    }
}