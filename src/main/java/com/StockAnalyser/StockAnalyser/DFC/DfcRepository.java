package com.StockAnalyser.StockAnalyser.DFC;

import com.StockAnalyser.StockAnalyser.DFC.DFC;
import com.StockAnalyser.StockAnalyser.Trade.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DfcRepository extends JpaRepository<DFC, Long> {



}