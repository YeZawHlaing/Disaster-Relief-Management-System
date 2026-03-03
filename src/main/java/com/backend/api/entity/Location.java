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

    @ManyToOne(optional = false)
    @JoinColumn(name = "admin_id", nullable = false)
    private User creator;

    @OneToOne(optional = false)
    @JoinColumn(name = "field_staff_id", nullable = false, unique = true)
    private User fieldStaff;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Beneficial> beneficials;
}