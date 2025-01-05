package com.example.sales.monthly.monthlyStockTranSum;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

/**
 * packageName  : com.example.sales.stockTrans
 * fileName     : MonthlyStockTranSum
 * author       : LEE_GEONHOE01
 * date         : 2024-12-30
 * description:
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-30       LEE_GEONHOE01      최초 생성
 */

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MonthlyStockTranSum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private YearMonth yearMonth; // 수불년월

    private BigDecimal beginInventoryAmount; // 기초재고액

    private BigDecimal endInventoryAmount; // 기말재고액

    private BigDecimal MonthlyPurchaseAmount; // 당기매입액

    private BigDecimal costOfGoodsSold; // 매출원가
}