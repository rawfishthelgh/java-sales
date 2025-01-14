package com.example.sales.monthly.monthlySalesResult;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import com.example.sales.plant.Plant;

/**
 * packageName  : com.example.sales.stockTrans
 * fileName     : MonthlySalesResult
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
public class MonthlySalesResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate yearMonth; // 수불년월

    private BigDecimal revenue; // 매출액

    private BigDecimal costOfGoodsSold; // 매출원가

    private BigDecimal grossProfit; //매출총이익

    private BigDecimal sellingGeneralAndAdministrativeExpenses; //판관비

    private BigDecimal operatingProfit; //영업이익

    @ManyToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;

}