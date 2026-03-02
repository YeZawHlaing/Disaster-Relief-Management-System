package com.backend.api.entity;

import com.backend.api.utility.enums.EventType;
import com.backend.api.utility.enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(nullable = false, length = 50)
    private String itemName;

    @Column(nullable = false, length = 255)
    private String itemDescription;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false, length = 50)
    private String unitOfMeasure;

    @Column(nullable = false, length = 50)
    private String storageLocation;

    @Column(nullable = false)
    private LocalDate manufacturedDate;

    @Column(nullable = false)
    private LocalDate expiriedDate;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}