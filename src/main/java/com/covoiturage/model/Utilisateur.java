package com.covoiturage.model;


import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "utilisateurs")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeUtilisateur type;

    @Column(nullable = false)
    private boolean peutSeConnecter;

}

