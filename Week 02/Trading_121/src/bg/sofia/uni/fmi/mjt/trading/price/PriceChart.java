package bg.sofia.uni.fmi.mjt.trading.price;

import bg.sofia.uni.fmi.mjt.trading.DoublePrecision;

public class PriceChart implements PriceChartAPI {
    double microsoftStockPrice;
    double googleStockPrice;
    double amazonStockPrice;
    public PriceChart(double microsoftStockPrice, double googleStockPrice, double amazonStockPrice){
        this.microsoftStockPrice=microsoftStockPrice;
        this.amazonStockPrice=amazonStockPrice;
        this.googleStockPrice=googleStockPrice;
    }
    @Override
    public double getCurrentPrice(String stockTicker) {
        if(stockTicker==null) return 0.0;
        double price=switch(stockTicker) {
            case "MSFT" ->microsoftStockPrice;
            case "AMZ" ->amazonStockPrice;
            case "GOOG" ->googleStockPrice;
            default ->0.0;
        };
        return DoublePrecision.calculateWithPrecison(price);
    }

    @Override
    public boolean changeStockPrice(String stockTicker, int percentChange){
        boolean isTrue=true;
        if(percentChange<0) return false;
        if(stockTicker==null)return false;
        switch(stockTicker) {
            case "MSFT" ->microsoftStockPrice*=((double)percentChange+100)/100;
            case "AMZ" ->amazonStockPrice*=((double) percentChange+100)/100;
            case "GOOG" ->googleStockPrice*=((double) percentChange+100)/100;
            default ->isTrue=false;
        };
        return isTrue;
    }
}
