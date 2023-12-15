package stocktrader.model.repository;

import stocktrader.model.entity.Stock;
import stocktrader.model.entity.StockInformation;
import stocktrader.model.entity.User;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class UserRepository extends FileHandle {
//    private ArrayList<StockInformation> userStocks;
    private double userMoney;
    private final ArrayList<Stock> userStocks = new ArrayList<>();

    public UserRepository(String filename) {
        super(filename);
    }

    public ArrayList<StockInformation> GetUserInfo() {
        return null;
    }

    public boolean StoreUserInfo() {
        return true;
    }

    public double GetUserMoney() {
        return userMoney;
    }
    public ArrayList<Stock> getUserStocks() {
        return userStocks;
    }

    @Override
    public boolean loadFromFile(String filename) {
        try {
            BufferedReader readFile = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = readFile.readLine()) != null) {
                if (line.contains("Money")) {
                    userMoney = Double.parseDouble(line.substring(6));
                } else {
                    String[] ownStock = line.split(" ");
                    String userStockName = ownStock[0];
                    double userStockPrice = Double.parseDouble(ownStock[1]);
                    int userStockQuantity = Integer.parseInt(ownStock[2]);
                    Stock stock = new Stock(userStockName, userStockPrice,userStockQuantity);
                    userStocks.add(stock);
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
