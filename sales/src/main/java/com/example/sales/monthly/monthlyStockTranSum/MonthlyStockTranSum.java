package com.example.sales.monthly.monthlyStockTranSum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import com.example.sales.plant.Plant;

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
@AllArgsConstructor
@Builder
@Getter
public class MonthlyStockTranSum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate yearMonth; // 수불년월

    private BigDecimal beginInventoryAmount; // 기초재고액

    private BigDecimal endInventoryAmount; // 기말재고액

    private BigDecimal monthlyPurchaseAmount; // 당기매입액

    private BigDecimal costOfGoodsSold; // 매출원가

    @ManyToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;
}