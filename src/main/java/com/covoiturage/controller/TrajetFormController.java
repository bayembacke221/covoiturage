package com.covoiturage.controller;

import com.covoiturage.model.Trajet;
import com.covoiturage.model.Utilisateur;
import com.covoiturage.model.Vehicule;
import com.covoiturage.service.TrajetService;
import com.covoiturage.service.UtilisateurService;
import com.covoiturage.service.VehiculeService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TrajetFormController {

    @FXML private TextField villeDepartField;
    @FXML private TextField villeArriveeField;
    @FXML private DatePicker datePicker;
    @FXML private Spinner<Integer> heureSpinner;
    @FXML private Spinner<Integer> minuteSpinner;
    @FXML private Spinner<Integer> placesDisponiblesSpinner;
    @FXML private TextField prixField;
    @FXML private ComboBox<Utilisateur> conducteurComboBox;
    @FXML private ComboBox<Vehicule> vehiculeComboBox;

    private TrajetService trajetService = new TrajetService();
    private UtilisateurService utilisateurService = new UtilisateurService();
    private VehiculeService vehiculeService = new VehiculeService();
    private Trajet trajet;

    @FXML
    public void initialize() {
        heureSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        placesDisponiblesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));

        loadConducteurs();
        loadVehicules();
    }

    private void loadConducteurs() {
        conducteurComboBox.getItems().addAll(utilisateurService.findAllConducteurs());
    }

    private void loadVehicules() {
        vehiculeComboBox.getItems().addAll(vehiculeService.findAll());
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
        if (trajet != null) {
            villeDepartField.setText(trajet.getVilleDepart());
            villeArriveeField.setText(trajet.getVilleArrivee());
            LocalDateTime dateDepart = LocalDateTime.ofInstant(trajet.getDateDepart().toInstant(), ZoneId.systemDefault());
            datePicker.setValue(dateDepart.toLocalDate());
            heureSpinner.getValueFactory().setValue(dateDepart.getHour());
            minuteSpinner.getValueFactory().setValue(dateDepart.getMinute());
            placesDisponiblesSpinner.getValueFactory().setValue(trajet.getPlacesDisponibles());
            prixField.setText(String.valueOf(trajet.getPrix()));

            conducteurComboBox.getSelectionModel().select(
                    conducteurComboBox.getItems().stream()
                            .filter(u -> u.getId().equals(trajet.getConducteurId()))
                            .findFirst()
                            .orElse(null)
            );

            vehiculeComboBox.getSelectionModel().select(
                    vehiculeComboBox.getItems().stream()
                            .filter(v -> v.getId().equals(trajet.getVehiculeId()))
                            .findFirst()
                            .orElse(null)
            );
        }
    }

    @FXML
    private void enregistrer() {
        if (validateInput()) {
            if (trajet == null) {
                trajet = new Trajet();
            }
            trajet.setVilleDepart(villeDepartField.getText());
            trajet.setVilleArrivee(villeArriveeField.getText());

            LocalDateTime dateTime = LocalDateTime.of(
                    datePicker.getValue().getYear(),
                    datePicker.getValue().getMonthValue(),
                    datePicker.getValue().getDayOfMonth(),
                    heureSpinner.getValue(),
                    minuteSpinner.getValue()
            );
            trajet.setDateDepart(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));

            trajet.setPlacesDisponibles(placesDisponiblesSpinner.getValue());
            trajet.setPrix(Double.parseDouble(prixField.getText()));

            Utilisateur conducteur = conducteurComboBox.getValue();
            if (conducteur != null) {
                trajet.setConducteurId(conducteur.getId());
            }

            Vehicule vehicule = vehiculeComboBox.getValue();
            if (vehicule != null) {
                trajet.setVehiculeId(vehicule.getId());
            }

            try {
                if (trajet.getId() == null) {
                    trajetService.save(trajet);
                } else {
                    trajetService.update(trajet);
                }
                closeStage();
            } catch (Exception e) {
                showAlert("Erreur", "Une erreur est survenue lors de l'enregistrement du trajet.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void annuler() {
        closeStage();
    }

    private boolean validateInput() {
        if (villeDepartField.getText().isEmpty() || villeArriveeField.getText().isEmpty() ||
                datePicker.getValue() == null || prixField.getText().isEmpty() ||
                conducteurComboBox.getValue() == null || vehiculeComboBox.getValue() == null) {
            showAlert("Champs manquants", "Veuillez remplir tous les champs.");
            return false;
        }
        try {
            Double.parseDouble(prixField.getText());
        } catch (NumberFormatException e) {
            showAlert("Format incorrect", "Le prix doit être un nombre valide.");
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
        Stage stage = (Stage) villeDepartField.getScene().getWindow();
        stage.close();
    }
}