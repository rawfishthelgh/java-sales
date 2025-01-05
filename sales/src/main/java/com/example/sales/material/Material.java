package com.example.sales.material;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * packageName  : com.example.sales.material
 * fileName     : Material
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
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 자재명

    private BigDecimal purchasePrice; //구매단가

    private BigDecimal salesPrice; // 판매단가
}