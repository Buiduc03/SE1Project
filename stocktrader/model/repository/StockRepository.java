package stocktrader.model.repository;

import stocktrader.model.entity.Stock;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StockRepository extends FileHandle {
    private final ArrayList<Stock> stocks = new ArrayList<>();

    public StockRepository(String filename) {
        super(filename);
    }

    public ArrayList<Stock> GetStocks() {
        return stocks;
    }

    public boolean StoreStocks() {
        return true;
    }

    @Override
    public boolean loadFromFile(String filename) {
        try {
            BufferedReader readFile = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = readFile.readLine()) != null) {
                String[] stockInfo = line.split(" ");
                String stockName = stockInfo[0];
                double stockPrice = Double.parseDouble(stockInfo[1]);
                int stockQuantity = Integer.parseInt(stockInfo[2]);
                Stock stock = new Stock(stockName, stockPrice,stockQuantity);
                stocks.add(stock);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
