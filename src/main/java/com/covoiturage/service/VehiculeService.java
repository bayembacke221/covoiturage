package com.covoiturage.service;

import com.covoiturage.dao.VehiculeDAO;
import com.covoiturage.model.Vehicule;

import java.util.List;

public class VehiculeService {
    private VehiculeDAO vehiculeDAO = new VehiculeDAO();

    public List<Vehicule> findAll() {
        return vehiculeDAO.findAll();
    }

    public List<Vehicule> rechercherVehicules(String searchTerm) {
        return vehiculeDAO.rechercherVehicules(searchTerm);
    }

    public List<Vehicule> findByProprietaireId(Long proprietaireId) {
        return vehiculeDAO.findByProprietaireId(proprietaireId);
    }

    public void save(Vehicule vehicule) {
        vehiculeDAO.save(vehicule);
    }

    public void update(Vehicule vehicule) {
        vehiculeDAO.update(vehicule);
    }

    public void delete(Vehicule vehicule) {
        vehiculeDAO.delete(vehicule);
    }
}