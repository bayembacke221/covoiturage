package com.covoiturage.dao;

import com.covoiturage.model.Vehicule;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class VehiculeDAO extends GenericDAOImpl<Vehicule> {

    public List<Vehicule> rechercherVehicules(String searchTerm) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Vehicule> query = session.createQuery(
                    "FROM Vehicule WHERE marque LIKE :searchTerm OR modele LIKE :searchTerm OR immatriculation LIKE :searchTerm",
                    Vehicule.class
            );
            query.setParameter("searchTerm", "%" + searchTerm + "%");
            return query.list();
        }
    }

    public List<Vehicule> findByProprietaireId(Long proprietaireId) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Vehicule> query = session.createQuery(
                    "FROM Vehicule WHERE proprietaireId = :proprietaireId",
                    Vehicule.class
            );
            query.setParameter("proprietaireId", proprietaireId);
            return query.list();
        }
    }
}