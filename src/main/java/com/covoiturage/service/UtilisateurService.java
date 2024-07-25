package com.covoiturage.service;

import com.covoiturage.dao.HibernateUtil;
import com.covoiturage.dao.UtilisateurDAO;
import com.covoiturage.model.TypeUtilisateur;
import com.covoiturage.model.Utilisateur;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import javax.naming.AuthenticationException;
import java.util.List;

public class UtilisateurService {
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public Utilisateur connecter(String email, String motDePasse) throws AuthenticationException {
        Utilisateur utilisateur = utilisateurDAO.findByEmail(email);
        if (utilisateur != null && BCrypt.checkpw(motDePasse, utilisateur.getMotDePasse())) {
            if (utilisateur.isPeutSeConnecter()) {
                return utilisateur;
            } else {
                throw new AuthenticationException("Cet utilisateur n'a pas le droit de se connecter.");
            }
        }
        return null;
    }

    public List<Utilisateur> findAll() {
        return utilisateurDAO.findAll();
    }

    public List<Utilisateur> rechercherUtilisateurs(String searchTerm) {
        return utilisateurDAO.rechercherUtilisateurs(searchTerm);
    }

    public void save(Utilisateur utilisateur) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(utilisateur);
            transaction.commit();
            session.refresh(utilisateur);
        }
    }
    public List<Utilisateur> findAllConducteurs() {
        return utilisateurDAO.findByType(TypeUtilisateur.CONDUCTEUR);
    }
    public void update(Utilisateur utilisateur) {
        utilisateurDAO.update(utilisateur);
    }

    public void delete(Utilisateur utilisateur) {
        utilisateurDAO.delete(utilisateur);
    }

    public List<Utilisateur> findAllPassagers() {
        return utilisateurDAO.findByType(TypeUtilisateur.PASSAGER);
    }

    public Utilisateur findById(Long passagerId) {
        return utilisateurDAO.findById(passagerId);
    }
}