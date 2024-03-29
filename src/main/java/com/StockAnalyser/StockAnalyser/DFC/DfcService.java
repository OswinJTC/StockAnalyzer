package com.StockAnalyser.StockAnalyser.DFC;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DfcService {


    Map<String, Double> stockData = new HashMap<>();

    public Map<String, Double> scrapeStockData(String stockName) {


        String url_1 = "https://stockanalysis.com/stocks/" + stockName + "/"; //marketCap, beta
        String url_2 = "https://stockanalysis.com/stocks/" + stockName + "/financials/balance-sheet/";//totalDebt
        String url_3 = "https://stockanalysis.com/stocks/" + stockName + "/financials/";//Interest Expense, pretaxIncome, incomeTaxText
        String url_4 = "https://www.cnbc.com/quotes/US10Y";//treasuryBondRate
        String url_5 = "https://stockanalysis.com/stocks/" + stockName + "/financials/cash-flow-statement/";//freeCashFlow

        try {
            Document document_1 = Jsoup.connect(url_1).get();
            Document document_2 = Jsoup.connect(url_2).get();
            Document document_3 = Jsoup.connect(url_3).get();
            Document document_4 = Jsoup.connect(url_4).get();
            Document document_5 = Jsoup.connect(url_5).get();


            //1
            Element marketCapElement = document_1.selectFirst("a[href*='/market-cap/']").parent().nextElementSibling();
            String marketCapText = marketCapElement.text();
            double marketCap = parseMath_TBM(marketCapText);

            //2
            Element betaElement = document_1.selectFirst("td:contains(Beta)");
            String betaText = betaElement.nextElementSibling().text();
            double beta = Double.parseDouble(betaText);



            //3
            Element totalDebtElement = document_2.selectFirst("td:contains(Total Debt)");
            String totalDebtText = totalDebtElement.nextElementSibling().text();
            double totalDebt = parseToMillion(totalDebtText);

            //4
            Element interestExpenseElement = document_3.selectFirst("td:contains(Interest Expense / Income)");
            String interestExpenseText = interestExpenseElement.nextElementSibling().text();
            double interestExpense = parseToMillion(interestExpenseText);

            //5
            Element pretaxIncomeElement = document_3.selectFirst("td:contains(Pretax Income)");
            String pretaxIncomeText = pretaxIncomeElement.nextElementSibling().text();
            double pretaxIncome = parseToMillion(pretaxIncomeText);

            //6
            Element incomeTaxElement = document_3.selectFirst("td:contains(Income Tax)");
            String incomeTaxText = incomeTaxElement.nextElementSibling().text();
            double incomeTax = parseToMillion(incomeTaxText);

            //7
            Element treasuryBondRateElement = document_4.selectFirst(".QuoteStrip-lastPrice");
            String treasuryBondRateText = treasuryBondRateElement.text().replace("%", "");
            double treasuryBondRate = parseTreasuryBondRate(treasuryBondRateText);

            //8
            Element freeCashFlowElement = document_5.selectFirst("td:contains(Free Cash Flow)");
            String freeCashFlowText = freeCashFlowElement.nextElementSibling().text().replace(",", "");
            double freeCashFlow = parseToMillion(freeCashFlowText);


            //9
            Element cashEquivalentsElement = document_2.selectFirst("td:contains(Cash & Cash Equivalents)");
            String cashEquivalentsText = cashEquivalentsElement.nextElementSibling().text();
            double cashEquivalents =  parseToMillion(cashEquivalentsText);


            //10
            Element sharesOutElement = document_1.selectFirst("td:contains(Shares Out)");
            String sharesOutText = sharesOutElement.nextElementSibling().text();
            double sharesOut = parseMath_TBM(sharesOutText);

            //put into the hashmap together
            stockData.put("MarketCap", marketCap);
            stockData.put("Beta", beta);
            stockData.put("TotalDebt", totalDebt);
            stockData.put("InterestExpense", interestExpense);
            stockData.put("PretaxIncome", pretaxIncome);
            stockData.put("IncomeTax", incomeTax);
            stockData.put("TreasuryBondRate", treasuryBondRate);
            stockData.put("FreeCashFlow", freeCashFlow);
            stockData.put("CashEquivalents", cashEquivalents);
            stockData.put("SharesOut", sharesOut);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return stockData;
    }

    private static double parseTreasuryBondRate(String treasuryBondRate) {

        double new_treasuryBondRate = Double.parseDouble(treasuryBondRate);

        return new_treasuryBondRate;
    }
    private static double parseToMillion(String totalDebtText) {
        double totalDebt = 0.0;
        try {
            totalDebt = Double.parseDouble(totalDebtText.replace(",", "")) * 1000000;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return totalDebt;
    }

    private static double parseMath_TBM(String marketCapText) {
        double marketCap = 0.0;
        try {
            if (marketCapText.endsWith("T")) {
                marketCap = Double.parseDouble(marketCapText.substring(0, marketCapText.length() - 1)) * 1_000_000_000_000.0;
            } else if (marketCapText.endsWith("B")) {
                marketCap = Double.parseDouble(marketCapText.substring(0, marketCapText.length() - 1)) * 1_000_000_000.0;
            } else if (marketCapText.endsWith("M")) {
                marketCap = Double.parseDouble(marketCapText.substring(0, marketCapText.length() - 1)) * 1_000_000.0;
            } else {
                marketCap = Double.parseDouble(marketCapText);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return marketCap;
    }



    public void calculateWACC(Map<String, Double> stockData) {
        double marketCap = stockData.get("MarketCap");
        double beta = stockData.get("Beta");
        double totalDebt = stockData.get("TotalDebt");
        double interestExpense = stockData.get("InterestExpense");
        double pretaxIncome = stockData.get("PretaxIncome");
        double incomeTax = stockData.get("IncomeTax");
        double treasuryBondRate = stockData.get("TreasuryBondRate");
        double expectedMarketReturn = 0.1;

        double temp_wd = totalDebt / (totalDebt + marketCap);
        double temp_rd = interestExpense / totalDebt;
        double temp_we = marketCap / (marketCap + totalDebt);
        double temp_re = treasuryBondRate + beta * (treasuryBondRate + expectedMarketReturn);
        double temp_t = incomeTax / pretaxIncome;

        double wacc = temp_wd * temp_rd * (1 - temp_t) + temp_we * temp_re;

        stockData.put("WACC", wacc);
    }

    public void calculate_V_value(Map<String, Double> stockData, double rapid_grow_rate, double permanent_grow_rate, double customizedYear) {
        double cashFlow = stockData.get("FreeCashFlow");

        double FCF = cashFlow * Math.pow(1+rapid_grow_rate, customizedYear);
        double wacc = stockData.get("WACC");
        double V_value = FCF * (1 + permanent_grow_rate) / ((wacc/100.0) - permanent_grow_rate);

        stockData.put("V_value", V_value);
    }

    public void calculate_EV(Map<String, Double> stockData,  double rapid_grow_rate, double customizedYear) {
        double ev_value = 0;
        double cashFlow = stockData.get("FreeCashFlow");
        double wacc = stockData.get("WACC");
        double v_value = stockData.get("V_value");

        for(int i=0; i< (int)customizedYear; i++){
            ev_value += (cashFlow*Math.pow(1+rapid_grow_rate,i+1)) / (Math.pow((1+(wacc/100.0)),i+1));

            double temp = (cashFlow*Math.pow(1+rapid_grow_rate,i+1)) / (Math.pow((1+(wacc/100.0)),i+1));
            System.out.println(temp);
        }

        ev_value +=  v_value / Math.pow((1+(wacc/100.0)),customizedYear);

        stockData.put("EV", ev_value);
    }

    public void calculate_E_value(Map<String, Double> stockData) {
        double e_value = 0;
        double ev_value = stockData.get("EV");
        double cashEquivalents = stockData.get("CashEquivalents");
        double totalDebt = stockData.get("TotalDebt");


        e_value = ev_value + cashEquivalents - totalDebt;

        stockData.put("E_value", e_value);
    }

    public void calculate_idealStockPrice(Map<String, Double> stockData) {
        double idealStockPrice = 0;
        double e_value = stockData.get("E_value");
        double sharesOut = stockData.get("SharesOut");


        idealStockPrice = e_value / sharesOut;

        stockData.put("IdealStockPrice", idealStockPrice);
    }
}
