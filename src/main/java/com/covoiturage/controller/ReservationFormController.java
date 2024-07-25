package com.covoiturage.controller;

import com.covoiturage.model.Reservation;
import com.covoiturage.model.StatutReservation;
import com.covoiturage.model.Trajet;
import com.covoiturage.model.Utilisateur;
import com.covoiturage.service.ReservationService;
import com.covoiturage.service.TrajetService;
import com.covoiturage.service.UtilisateurService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Date;

public class ReservationFormController {

    @FXML private ComboBox<Trajet> trajetComboBox;
    @FXML private ComboBox<Utilisateur> passagerComboBox;
    @FXML private DatePicker dateReservationPicker;
    @FXML private ComboBox<StatutReservation> statutComboBox;

    private ReservationService reservationService = new ReservationService();
    private TrajetService trajetService = new TrajetService();
    private UtilisateurService utilisateurService = new UtilisateurService();
    private Reservation reservation;

    @FXML
    public void initialize() {
        loadTrajets();
        loadPassagers();
        loadStatuts();
    }

    private void loadTrajets() {
        trajetComboBox.getItems().addAll(trajetService.findAll());
    }

    private void loadPassagers() {
        passagerComboBox.getItems().addAll(utilisateurService.findAllPassagers());
    }

    private void loadStatuts() {
        statutComboBox.getItems().addAll(StatutReservation.values());
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        if (reservation != null) {
            trajetComboBox.setValue(trajetService.findById(reservation.getTrajetId()));
            passagerComboBox.setValue(utilisateurService.findById(reservation.getPassagerId()));
            dateReservationPicker.setValue(reservation.getDateReservation().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
            statutComboBox.setValue(reservation.getStatut());
        }
    }

    @FXML
    private void enregistrer() {
        if (validateInput()) {
            if (reservation == null) {
                reservation = new Reservation();
            }
            reservation.setTrajetId(trajetComboBox.getValue().getId());
            reservation.setPassagerId(passagerComboBox.getValue().getId());
            reservation.setDateReservation(java.sql.Date.valueOf(dateReservationPicker.getValue()));
            reservation.setStatut(statutComboBox.getValue());

            try {
                if (reservation.getId() == null) {
                    reservationService.save(reservation);
                    showAlert("Succès", "La réservation a été enregistrée et un email de confirmation a été envoyé.", Alert.AlertType.INFORMATION);
                } else {
                    reservationService.update(reservation);
                    showAlert("Succès", "La réservation a été mise à jour.", Alert.AlertType.INFORMATION);
                }
                closeStage();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur est survenue lors de l'enregistrement de la réservation ou de l'envoi de l'email de confirmation.", Alert.AlertType.ERROR);
            }
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void annuler() {
        closeStage();
    }

    private boolean validateInput() {
        if (trajetComboBox.getValue() == null || passagerComboBox.getValue() == null ||
                dateReservationPicker.getValue() == null || statutComboBox.getValue() == null) {
            showAlert("Champs manquants", "Veuillez remplir tous les champs.");
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
        Stage stage = (Stage) trajetComboBox.getScene().getWindow();
        stage.close();
    }
}