package com.covoiturage.dao;

import com.covoiturage.model.TypeUtilisateur;
import com.covoiturage.model.Utilisateur;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class UtilisateurDAO extends GenericDAOImpl<Utilisateur> {
    public List<Utilisateur> rechercherParNom(String nom) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Utilisateur> query = session.createQuery("FROM Utilisateur WHERE nom LIKE :nom", Utilisateur.class);
            query.setParameter("nom", "%" + nom + "%");
            return query.list();
        }
    }

    public Utilisateur findByEmail(String email) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Utilisateur> query = session.createQuery("FROM Utilisateur WHERE email = :email", Utilisateur.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        }
    }
    public List<Utilisateur> findByType(TypeUtilisateur type) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Utilisateur> query = session.createQuery(
                    "FROM Utilisateur WHERE type = :type", Utilisateur.class);
            query.setParameter("type", type);
            return query.list();
        }
    }
    public List<Utilisateur> rechercherUtilisateurs(String searchTerm) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Utilisateur> query = session.createQuery(
                    "FROM Utilisateur WHERE nom LIKE :searchTerm OR prenom LIKE :searchTerm OR email LIKE :searchTerm",
                    Utilisateur.class
            );
            query.setParameter("searchTerm", "%" + searchTerm + "%");
            return query.list();
        }
    }
}