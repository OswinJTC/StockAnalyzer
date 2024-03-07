package com.StockAnalyser.StockAnalyser.Trade;

import com.StockAnalyser.StockAnalyser.Trade.Trade;
import com.StockAnalyser.StockAnalyser.Trade.TradeRepository;
import com.StockAnalyser.StockAnalyser.User.User;
import com.StockAnalyser.StockAnalyser.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private UserRepository userRepository;

    private int nextReferenceNumber = 1; // Starting reference number

    public Trade createTrade(Trade trade) {
        trade.setReferenceNumber(generateNextReferenceNumber()); // Set the reference number
        Trade savedTrade = tradeRepository.save(trade);

        // Update the corresponding user with the new trade
        String owner = trade.getOwner();
        User user = userRepository.findByUsername(owner).orElseThrow(() -> new IllegalArgumentException("User not found"));
        //user.getUser_trades().add(savedTrade);
        userRepository.save(user);

        return savedTrade;
    }

    private int generateNextReferenceNumber() {
        return nextReferenceNumber++;
    }

//    public void deleteTrade(int tradeReference, String owner) {
//        Optional<Trade> optionalTrade = tradeRepository.findByReferenceNumber(tradeReference);
//
//        if (optionalTrade.isPresent()) {
//            Trade trade = optionalTrade.get();
//            tradeRepository.delete(trade);
//
//            User user = userRepository.findByUsername(owner).orElseThrow(() -> new IllegalArgumentException("User not found"));
//            user.getUser_trades().removeIf(t -> t.getReferenceNumber() == tradeReference);
//            userRepository.save(user);
//        } else {
//            // Handle case where trade does not exist
//        }
//    }

    public List<Trade> allTrades() {
        return tradeRepository.findAll();
    }
}