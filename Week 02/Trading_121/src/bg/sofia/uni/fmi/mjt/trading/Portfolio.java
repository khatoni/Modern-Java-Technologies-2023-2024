package bg.sofia.uni.fmi.mjt.trading;

import bg.sofia.uni.fmi.mjt.trading.price.PriceChart;
import bg.sofia.uni.fmi.mjt.trading.stock.AmazonStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.GoogleStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.MicrosoftStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.StockPurchase;
import bg.sofia.uni.fmi.mjt.trading.price.PriceChartAPI;

import java.time.LocalDateTime;

public class Portfolio implements PortfolioAPI {
    String owner;
    PriceChartAPI priceChart;
    double budget;
    int maxSize;
    StockPurchase[] stockPurchases;

    int currentSize;

    public Portfolio(String owner, PriceChartAPI priceChart, double budget, int maxSize) {
        this.owner = owner;
        this.priceChart = priceChart;
        this.budget = budget;
        this.maxSize = maxSize;
        this.currentSize = 0;
        this.stockPurchases = new StockPurchase[maxSize];
    }

    public Portfolio(String owner, PriceChartAPI priceChart, StockPurchase[] stockPurchases, double budget, int maxSize) {
        this.owner = owner;
        this.priceChart = priceChart;
        this.budget = budget;
        this.maxSize = maxSize;
        this.currentSize = 0;
        this.stockPurchases = stockPurchases;
    }

    @Override
    public StockPurchase buyStock(String stockTicker, int quantity) {
        if (quantity < 0 || stockTicker == null) return null;
        if (currentSize >= maxSize) return null;
        StockPurchase result;
        switch (stockTicker) {
            case "MSFT" -> {
                double temporaryPrice = priceChart.getCurrentPrice("MSFT");
                if (budget < temporaryPrice * quantity) {
                    return null;
                }
                result = new MicrosoftStockPurchase(quantity, LocalDateTime.now(), priceChart.getCurrentPrice("MSFT"));
                budget -= temporaryPrice * quantity;
                priceChart.changeStockPrice("MSFT", 5);
            }
            case "AMZ" -> {
                double temporaryPrice = priceChart.getCurrentPrice("AMZ");
                if (budget < temporaryPrice * quantity) {
                    return null;
                }
                result = new AmazonStockPurchase(quantity, LocalDateTime.now(), priceChart.getCurrentPrice("AMZ"));
                budget -= temporaryPrice * quantity;
                priceChart.changeStockPrice("AMZ", 5);
            }
            case "GOOG" -> {
                double temporaryPrice = priceChart.getCurrentPrice("GOOG");
                if (budget < temporaryPrice * quantity) {
                    return null;
                }
                result = new GoogleStockPurchase(quantity, LocalDateTime.now(), priceChart.getCurrentPrice("GOOG"));
                budget -= temporaryPrice * quantity;
                priceChart.changeStockPrice("GOOG", 5);
            }
            default -> result = null;
        }

        if(result!=null) {
            if(currentSize==0) {
                stockPurchases=new StockPurchase[maxSize];
            }
            stockPurchases[currentSize++]=result;
        }
        return result;
    }

    @Override
    public StockPurchase[] getAllPurchases() {
        return stockPurchases;
    }

    @Override
    public StockPurchase[] getAllPurchases(LocalDateTime startTimestamp, LocalDateTime endTimestamp) {
        int counter=0;
        for (int i = 0; i < stockPurchases.length; i++) {
            if (stockPurchases[i].getPurchaseTimestamp().isAfter(startTimestamp) && stockPurchases[i].getPurchaseTimestamp().isBefore(endTimestamp)) {
                counter++;
            }
        }
        StockPurchase[] result = new StockPurchase[counter];
        int index=0;
        for (int i = 0; i < stockPurchases.length; i++) {
            if (stockPurchases[i].getPurchaseTimestamp().isAfter(startTimestamp) && stockPurchases[i].getPurchaseTimestamp().isBefore(endTimestamp)) {
                result[index++]=stockPurchases[i];
            }
        }
        return result;
    }

    @Override
    public double getNetWorth() {
        double netWorth = 0.0;
        for (int i = 0; i < currentSize; i++) {
            netWorth += priceChart.getCurrentPrice(stockPurchases[i].getStockTicker()) * stockPurchases[i].getQuantity();
        }
        return DoublePrecision.calculateWithPrecison(netWorth);
    }

    @Override
    public double getRemainingBudget() {
        return DoublePrecision.calculateWithPrecison(budget);
    }

    @Override
    public String getOwner() {
        return owner;
    }

}
