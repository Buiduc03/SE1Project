package stocktrader.server;

import stocktrader.model.entity.Stock;
import stocktrader.model.entity.StockInformation;
import stocktrader.model.entity.User;
import stocktrader.model.repository.StockRepository;
import stocktrader.model.repository.UserListRepository;
import stocktrader.model.repository.UserRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class StockServer {
    private ArrayList<User> users;
    private ArrayList<Stock> stocks;
    private ArrayList<StockInformation> userStocks;
    private ArrayList<StockInformation> userHistory;
    private double userMoney;
    private boolean isLoggedIn;
    public LocalTime currentDate;
    private User currentUser;
    private final UserListRepository userListRepository = new UserListRepository("src/Tutorial9/userlist.txt");
    private final StockRepository stockRepository = new StockRepository("src/Tutorial9/StockInfo.txt");


    public StockServer() {
        stocks = stockRepository.GetStocks();
    }

    public boolean canLoadFile() {
        return userListRepository.loadFromFile("src/Tutorial9/userlist.txt")
                && stockRepository.loadFromFile("src/Tutorial9/StockInfo.txt");
    }



    public boolean login(String username, String password) {
        isLoggedIn = false;
        users = userListRepository.GetUserList();

        for (User user : users) {
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                currentUser = user;
                isLoggedIn = true;
                break;
            }
        }
        if (isLoggedIn) {
            String filename = "src/Tutorial9/" + currentUser.getUsername() + ".txt";
            UserRepository user = new UserRepository(filename);
            if (user.loadFromFile(filename)) {
                currentUser.setUserMoney(user.GetUserMoney());
                currentUser.setOwnStocks(user.getUserStocks());
            }
        }
        return isLoggedIn;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String listAllStocks() {
        StringBuilder printStock = new StringBuilder();
        for (int i = 0; i < stocks.size(); i++) {
            Stock stock = stocks.get(i);
            printStock.append(i + 1).append(" ")
                      .append(stock.getName()).append(" ")
                      .append(stock.getPrice()).append(" ")
                      .append(stock.getQuantity()).append("\n");
        }
        return printStock.toString();
    }

    public boolean purchase(int stockNo, int quantity) {
        if (stockNo < 1 || stockNo > stocks.size()) {
            throw new InputMismatchException("Invalid stock number");
        }

        Stock purchasedStock = stocks.get(stockNo - 1);
        userMoney = currentUser.getUserMoney();
        int currentQuantity = purchasedStock.getQuantity();
        double totalPrice = purchasedStock.getPrice() * quantity;

        if (currentQuantity >= quantity && userMoney >= totalPrice) {
            purchasedStock.setQuantity(currentQuantity - quantity);
            currentUser.setUserMoney(userMoney - totalPrice);
            for (int i = 0; i < currentUser.getOwnStocks().size(); i++) {
                Stock ownStock = currentUser.getOwnStocks().get(i);
                if (ownStock.getName().equals(purchasedStock.getName())) {
                    ownStock.setQuantity(ownStock.getQuantity() + quantity);
                } else {
                    currentUser.addNewStocks(purchasedStock.getName(), purchasedStock.getPrice(), quantity);
                    break;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public String listOwnStocks() {
        ArrayList<Stock> ownedStocks = currentUser.getOwnStocks();

        if (ownedStocks.isEmpty()) {
            return "You do not own any stocks.";
        }

        StringBuilder ownStockList = new StringBuilder();

        for (int i = 0; i < ownedStocks.size(); i++) {
            Stock stock = ownedStocks.get(i);
            ownStockList.append(i + 1).append(" ")
                     .append(stock.getName()).append(" ")
                     .append(stock.getPrice()).append(" ")
                     .append(stock.getQuantity()).append("\n");
        }

        return ownStockList.toString();
    }

    public boolean sellStock(int stockNo, int quantity) {
        if (stockNo < 1 || stockNo > currentUser.getOwnStocks().size()) {
            throw new InputMismatchException("Invalid stock number");
        }

        Stock soldStock = currentUser.getOwnStocks().get(stockNo - 1);
        userMoney = currentUser.getUserMoney();
        double totalPrice = soldStock.getPrice() * quantity;

        if (soldStock.getQuantity() >= quantity) {
            soldStock.setQuantity(soldStock.getQuantity() - quantity);
            currentUser.setUserMoney(userMoney + totalPrice);
            for (Stock stock : stocks) {
                if (stock.getName().equals(soldStock.getName())) {
                    stock.setQuantity(stock.getQuantity() + quantity);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void nextDay() {

    }

    public Double checkBalance() {
        return currentUser.getUserMoney();
    }

    public String getPrice() {
        return "";
    }

    private ArrayList<Stock> getCurrentPrice() {
        return null;
    }

    private boolean storeInformaton(StockInformation info) {
        return true;
    }

}
