package com.backend.api.entity;

import com.backend.api.utility.enums.Status;
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

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private LocalDate distributionDate;

    @Column(nullable = false)
    private String houseHoldNrc;

    @Column(nullable = false)
    private int familyMembers;

    @Column(nullable = true)
    private int underFive;
    @Column(nullable = true)
    private int disabled;

    @Column(nullable = false)
    private String distributedItems;

    @ManyToOne
    @JoinColumn(name = "beneficial_id")
    private Beneficiary beneficiary;

//    @ManyToOne
//    @JoinColumn(name = "stock_id")
//    private StockInfo stock;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}