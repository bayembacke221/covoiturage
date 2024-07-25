package com.covoiturage.controller;

import com.covoiturage.model.Utilisateur;
import com.covoiturage.model.Vehicule;
import com.covoiturage.service.UtilisateurService;
import com.covoiturage.service.VehiculeService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class VehiculeFormController {

    @FXML private TextField marqueField;
    @FXML private TextField modeleField;
    @FXML private TextField immatriculationField;
    @FXML private ComboBox<Utilisateur> proprietaireComboBox;

    private VehiculeService vehiculeService = new VehiculeService();
    private UtilisateurService utilisateurService = new UtilisateurService();
    private Vehicule vehicule;

    @FXML
    public void initialize() {
        loadConducteurs();
    }

    private void loadConducteurs() {
        List<Utilisateur> conducteurs = utilisateurService.findAllConducteurs();
        proprietaireComboBox.getItems().addAll(conducteurs);
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
        if (vehicule != null) {
            marqueField.setText(vehicule.getMarque());
            modeleField.setText(vehicule.getModele());
            immatriculationField.setText(vehicule.getImmatriculation());
            proprietaireComboBox.getSelectionModel().select(
                    proprietaireComboBox.getItems().stream()
                            .filter(u -> u.getId().equals(vehicule.getProprietaireId()))
                            .findFirst()
                            .orElse(null)
            );
        }
    }

    @FXML
    private void enregistrer() {
        if (validateInput()) {
            if (vehicule == null) {
                vehicule = new Vehicule();
            }
            vehicule.setMarque(marqueField.getText());
            vehicule.setModele(modeleField.getText());
            vehicule.setImmatriculation(immatriculationField.getText());

            Utilisateur proprietaire = proprietaireComboBox.getValue();
            if (proprietaire != null) {
                vehicule.setProprietaireId(proprietaire.getId());
            } else {
                showAlert("Erreur", "Veuillez sélectionner un propriétaire.");
                return;
            }

            try {
                if (vehicule.getId() == null) {
                    vehiculeService.save(vehicule);
                } else {
                    vehiculeService.update(vehicule);
                }
                closeStage();
            } catch (Exception e) {
                showAlert("Erreur", "Une erreur est survenue lors de l'enregistrement du véhicule.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void annuler() {
        closeStage();
    }

    private boolean validateInput() {
        if (marqueField.getText().isEmpty() || modeleField.getText().isEmpty() ||
                immatriculationField.getText().isEmpty() || proprietaireComboBox.getValue() == null) {
            showAlert("Champs manquants", "Veuillez remplir tous les champs et sélectionner un propriétaire.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeStage() {
        Stage stage = (Stage) marqueField.getScene().getWindow();
        stage.close();
    }
}