package stocktrader.client;

import stocktrader.server.StockServer;

import java.util.Scanner;

public class StockClient {
    public static void main(String[] args) {
        StockServer server = new StockServer();
        Scanner input = new Scanner(System.in);

        boolean isLoggedIn = false;
        String title = "             STOCK TRADER             ";
        String line =  "--------------------------------------";


        System.out.println(title);
        System.out.println(line);
        if (!server.canLoadFile()) {
            System.out.println("Can not load from file!");
        }

        while (!isLoggedIn) {
            System.out.print("Username: ");
            String username = input.nextLine();

            System.out.print("Password: ");
            String password = input.nextLine();
            if (server.login(username, password)) {
                isLoggedIn = true;
                System.out.println("Welcome " + server.getCurrentUser().toString());
            } else {
                System.out.println("Login failed.");
            }
        }

        System.out.println(line);
        menu();
        System.out.println(line);

        while (isLoggedIn) {
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    System.out.println(server.listAllStocks());
                    break;
                case 2:
                    System.out.print("Enter stock number: ");
                    int purchasedStockNo = input.nextInt();
                    System.out.print("Enter quantity of stock: ");
                    int purchasedQuantity = input.nextInt();
                    if (server.purchase(purchasedStockNo, purchasedQuantity)) {
                        System.out.println("Purchase successfully!");
                    } else {
                        System.out.println("Do not have enough stock or money to purchase!");
                    }
                    break;
                case 3:
                    System.out.println(server.listOwnStocks());
                    break;
                case 4:
                    System.out.print("Enter stock number: ");
                    int soldStockNo = input.nextInt();
                    System.out.print("Enter quantity of stock: ");
                    int soldQuantity = input.nextInt();
                    if (server.sellStock(soldStockNo, soldQuantity)) {
                        System.out.println("Sell successfully!");
                    } else {
                        System.out.println("Do not have enough stock to sell!");
                    }
                    break;
                case 5:
                    System.out.printf("Your balance: %.0f\n",server.checkBalance());
                    break;
                case 6:
                    System.out.println("Quitting...");
                    isLoggedIn = false;
            }
        }
    }

    public static void menu() {
        System.out.println("1. List all stocks");
        System.out.println("2. Purchase stocks");
        System.out.println("3. List own stocks");
        System.out.println("4. Sell stocks");
        System.out.println("5. Check balance");
        System.out.println("6. Exit");
    }
}
