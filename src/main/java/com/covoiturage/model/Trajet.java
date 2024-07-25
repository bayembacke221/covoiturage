package com.covoiturage.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "trajets")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Trajet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ville_depart", nullable = false)
    private String villeDepart;

    @Column(name = "ville_arrivee", nullable = false)
    private String villeArrivee;

    @Column(name = "date_depart", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDepart;

    @Column(name = "places_disponibles", nullable = false)
    private int placesDisponibles;

    @Column(nullable = false)
    private double prix;

    @Column(name = "conducteur_id", nullable = false)
    private Long conducteurId;

    @Column(name = "vehicule_id", nullable = false)
    private Long vehiculeId;


}