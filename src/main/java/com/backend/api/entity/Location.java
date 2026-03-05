package com.backend.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "location")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String locationName;

    @ManyToOne
    @JoinColumn(name = "projectOfficer_id", nullable = true)
    private User creator;

    @OneToOne
    @JoinColumn(name = "field_staff_id", nullable = true, unique = true)
    private User fieldStaff;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Beneficiary> beneficials;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<AssignDistribution> assignDistributions;
}