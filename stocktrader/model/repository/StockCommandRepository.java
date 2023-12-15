package stocktrader.model.repository;

import stocktrader.model.entity.StockInformation;

import java.util.ArrayList;

public class StockCommandRepository extends FileHandle{
    private ArrayList<StockInformation> userHistory;

    public StockCommandRepository(String filename) {
        super(filename);
    }

    public ArrayList<StockInformation> GetStockCommands() {
        return null;
    }

    public boolean StoreStockCommands() {
        return true;
    }

    public boolean AppendLastStockCommand() {
        return true;
    }
}
