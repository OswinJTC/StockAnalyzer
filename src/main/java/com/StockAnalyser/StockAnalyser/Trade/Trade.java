package com.StockAnalyser.StockAnalyser.Trade;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRADE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "REFERENCE_NUMBER")
    private int referenceNumber;

    @Column(name = "OWNER")
    private String owner;

    @Column(name = "TRANSACTION_TYPE")
    private String transactionType;

    @Column(name = "STOCK_NAME")
    private String stockName;

    @Column(name = "BUY_PRICE")
    private double buyPrice;

    @Column(name = "SALE_PRICE")
    private double salePrice;

    @Column(name = "QUANTITY")
    private double quantity;

    @Column(name = "BUY_DATE")
    private LocalDateTime buyDate;

    @Column(name = "SALE_DATE")
    private LocalDateTime saleDate;

    @Column(name = "TOTAL_BUY_AMOUNT")
    private double totalBuyAmount;

    @Column(name = "TOTAL_SALE_AMOUNT")
    private double totalSaleAmount;

    @Column(name = "PERCENT_AMOUNT")
    private double percentAmount;

    @Column(name = "PROFIT")
    private double profit;

    public void setQuantity(double quantity) {
        this.quantity = quantity;
        calculateTotalBuyAmount();
        calculateTotalSaleAmount();
        calculateProfit();
        calculatePercentBuyAmount();
    }

    private void calculateTotalBuyAmount() {
        this.totalBuyAmount = quantity * buyPrice;
    }

    private void calculatePercentBuyAmount() {
        double temp = ((salePrice - buyPrice) / buyPrice) * 100;
        this.percentAmount = temp;
    }

    private void calculateTotalSaleAmount() {
        this.totalSaleAmount = quantity * salePrice;
    }

    private void calculateProfit() {
        this.profit = (salePrice - buyPrice) * quantity;
    }

    public void setBuyDate(String buyDate) {
        // Assuming buyDate is in the format "yyyy-MM-dd"
        this.buyDate = LocalDateTime.parse(buyDate + "T00:00:00");
    }

    public void setSaleDate(String saleDate) {
        // Assuming saleDate is in the format "yyyy-MM-dd"
        this.saleDate = LocalDateTime.parse(saleDate + "T00:00:00");
    }
}
