package stocktrader.model.entity;

import stocktrader.model.repository.FileHandle;
import stocktrader.model.repository.UserListRepository;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private double userMoney;
    private ArrayList<Stock> ownStocks = new ArrayList<>();


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(double userMoney) {
        this.userMoney = userMoney;
    }

    public ArrayList<Stock> getOwnStocks() {
        return ownStocks;
    }

    public void setOwnStocks(ArrayList<Stock> ownStocks) {
        this.ownStocks = ownStocks;
    }

    public void addNewStocks(String name, double price, int quantity) {
        ownStocks.add(new Stock(name, price, quantity));
    }

    @Override
    public String toString() {
        return username;
    }
}
