package com.example.sales.stockTrans;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * packageName  : com.example.sales.stockTrans
 * fileName     : StockTrans
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
public class StockTrans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private StockTransType stockTransType;

    @Enumerated(EnumType.STRING)
    private MoveType moveType;

    private Long plantId;

    private Long targetPlantId;

    private Long materialId;

    private BigDecimal quantity;

    private LocalDateTime stockTransDateTime;


}