package com.StockAnalyser.StockAnalyser.DFC;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;


@Entity
@Table(name = "DFC")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DFC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "MARKET_CAP")
    private double marketCap;

    @Column(name = "BETA")
    private double beta;

    @Column(name = "TOTAL_DEBT")
    private double totalDebt;

    @Column(name = "INTEREST_EXPENSE")
    private double interestExpense;

    @Column(name = "PRETAX_INCOME")
    private double pretaxIncome;

    @Column(name = "TAX_PROVISION")
    private double taxProvision;

    @Column(name = "TREASURY_BOND_RATE")
    private double treasuryBondRate;

    @Column(name = "EXPECTED_MARKET_RETURN")
    private double expectedMarketReturn;

    //USER inputs
    @Column(name = "CUSTOMIZED_YEAR")
    private double customizedYear;

    @Column(name = "RAPID_GROW_RATE")
    private double rapid_grow_rate;

    @Column(name = "PERMANENT_GROW_RATE")
    private double permanent_grow_rate;

    @Column(name = "WACC")
    private double WACC;

}
