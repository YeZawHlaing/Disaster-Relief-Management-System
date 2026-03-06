package com.backend.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "distribution_record")
public class DistributionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    private LocalDate distributionDate;

    private Double quantityGiven;

    private String unitOfMeasure;

    private LocalDate reportCreatedDate;

    private LocalDate emergencyDate;

    private LocalDate distributedDate;

    @ManyToOne
    @JoinColumn(name = "beneficial_id")
    private Beneficiary beneficiary;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private StockInfo stock;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}