package com.covoiturage.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reservations")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trajet_id", nullable = false)
    private Long trajetId;

    @Column(name = "passager_id", nullable = false)
    private Long passagerId;

    @Column(name = "date_reservation", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReservation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutReservation statut;
}