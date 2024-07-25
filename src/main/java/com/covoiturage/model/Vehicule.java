package com.covoiturage.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "vehicules")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String marque;

    @Column(nullable = false)
    private String modele;

    @Column(nullable = false, unique = true)
    private String immatriculation;
    @Column(name = "proprietaire_id", nullable = false)
    private Long proprietaireId;
}