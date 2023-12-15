package stocktrader.model.entity;

import java.time.LocalTime;

public class StockInformation {
    private int commandType;
    private Stock stock;
    private LocalTime purchaseDate;

    public int getCommandType() {
        return commandType;
    }

    public void setCommandType(int commandType) {
        this.commandType = commandType;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public LocalTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
