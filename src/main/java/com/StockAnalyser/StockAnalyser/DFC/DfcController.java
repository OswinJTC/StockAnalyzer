package com.StockAnalyser.StockAnalyser.DFC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RequestMapping("/dfcApi")
@RestController
@CrossOrigin(origins = "https://main.d2vaujsrihdmnl.amplifyapp.com/")
public class DfcController {

    @Autowired
    private DfcService dfcService;

    @GetMapping("/stockInfo/{stockName}")
    public ResponseEntity<?> fetchStockData(@PathVariable String stockName) {
        try {
            Map<String, Double> stockData = dfcService.scrapeStockData(stockName);
            if (stockData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stock data not found");
            }
            return ResponseEntity.ok(stockData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching stock data");
        }
    }

    @GetMapping("/wacc/{stockName}")
    public ResponseEntity<?> getWACC(@PathVariable String stockName) {
        Map<String, Double> stockData = dfcService.scrapeStockData(stockName);
        dfcService.calculateWACC(stockData);
        return ResponseEntity.ok(stockData);
    }

    @PostMapping("/v_value/{stockName}")
    public ResponseEntity<?> getV_Value(@PathVariable String stockName, @RequestBody Map<String, Double> payload) {
        Map<String, Double> stockData = dfcService.scrapeStockData(stockName);
        dfcService.calculateWACC(stockData); // Ensure WACC is calculated before V_value
        double rapidGrowRate = payload.get("rapidGrowRate");
        double permanentGrowRate = payload.get("permanentGrowRate");
        double customizedYear = payload.get("customizedYear");
        dfcService.calculate_V_value(stockData, rapidGrowRate, permanentGrowRate, customizedYear);
        return ResponseEntity.ok(stockData);
    }

    @PostMapping("/ev_value/{stockName}")
    public ResponseEntity<?> getEV_Value(@PathVariable String stockName, @RequestBody Map<String, Double> payload) {
        Map<String, Double> stockData = dfcService.scrapeStockData(stockName);
        dfcService.calculateWACC(stockData); // Ensure WACC is calculated before V_value
        double rapidGrowRate = payload.get("rapidGrowRate");
        double permanentGrowRate = payload.get("permanentGrowRate");
        double customizedYear = payload.get("customizedYear");
        dfcService.calculate_V_value(stockData, rapidGrowRate, permanentGrowRate, customizedYear);
        dfcService.calculate_EV(stockData, rapidGrowRate, customizedYear);
        return ResponseEntity.ok(stockData);
    }

    @PostMapping("/e_value/{stockName}")
    public ResponseEntity<?> getE_Value(@PathVariable String stockName, @RequestBody Map<String, Double> payload) {
        Map<String, Double> stockData = dfcService.scrapeStockData(stockName);
        dfcService.calculateWACC(stockData); // Ensure WACC is calculated before V_value
        double rapidGrowRate = payload.get("rapidGrowRate");
        double permanentGrowRate = payload.get("permanentGrowRate");
        double customizedYear = payload.get("customizedYear");
        dfcService.calculate_V_value(stockData, rapidGrowRate, permanentGrowRate, customizedYear);
        dfcService.calculate_EV(stockData, rapidGrowRate, customizedYear);
        dfcService.calculate_E_value(stockData);
        return ResponseEntity.ok(stockData);
    }

    @PostMapping("/idealStockPrice/{stockName}")
    public ResponseEntity<?> get_IdealStockPrice(@PathVariable String stockName, @RequestBody Map<String, Double> payload) {
        Map<String, Double> stockData = dfcService.scrapeStockData(stockName);
        dfcService.calculateWACC(stockData); // Ensure WACC is calculated before V_value
        double rapidGrowRate = payload.get("rapidGrowRate");
        double permanentGrowRate = payload.get("permanentGrowRate");
        double customizedYear = payload.get("customizedYear");
        dfcService.calculate_V_value(stockData, rapidGrowRate, permanentGrowRate, customizedYear);
        dfcService.calculate_EV(stockData, rapidGrowRate, customizedYear);
        dfcService.calculate_E_value(stockData);
        dfcService.calculate_idealStockPrice(stockData);
        return ResponseEntity.ok(stockData);
    }


}
