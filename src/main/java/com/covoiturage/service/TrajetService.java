package com.covoiturage.service;

import com.covoiturage.dao.TrajetDAO;
import com.covoiturage.model.Trajet;

import java.util.Date;
import java.util.List;

public class TrajetService {
    private TrajetDAO trajetDAO = new TrajetDAO();

    public List<Trajet> findAll() {
        return trajetDAO.findAll();
    }

    public List<Trajet> rechercherTrajets(String villeDepart, String villeArrivee, Date date, Long conducteurId) {
        return trajetDAO.rechercherTrajets(villeDepart, villeArrivee, date, conducteurId);
    }

    public void save(Trajet trajet) {
        trajetDAO.save(trajet);
    }

    public void update(Trajet trajet) {
        trajetDAO.update(trajet);
    }

    public void delete(Trajet trajet) {
        trajetDAO.delete(trajet);
    }

    public Trajet findById(Long trajetId) {
        return trajetDAO.findById(trajetId);
    }
}