package bg.sofia.uni.fmi.mjt.trading.stock;

import bg.sofia.uni.fmi.mjt.trading.DoublePrecision;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AmazonStockPurchase implements StockPurchase {
    String ticker;
    LocalDateTime purchaseTime;
    int quantity;
    double purchasePricePerUnit;
    double totalPurchasePrice;


    public AmazonStockPurchase(int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit){
        this.quantity=quantity;
        this.purchaseTime=purchaseTimestamp;
        this.purchasePricePerUnit=purchasePricePerUnit;
        this.totalPurchasePrice=quantity*purchasePricePerUnit;
        this.ticker="AMZ";
    }

    @Override
    public int getQuantity(){
        return quantity;
    }
    @Override
    public LocalDateTime getPurchaseTimestamp(){
        return purchaseTime;
    }
    @Override
    public double getPurchasePricePerUnit(){
        return DoublePrecision.calculateWithPrecison(purchasePricePerUnit);
    }
    @Override
    public double getTotalPurchasePrice(){
        return DoublePrecision.calculateWithPrecison(totalPurchasePrice);
    }
    @Override
    public String getStockTicker(){
        return ticker;
    }

}
