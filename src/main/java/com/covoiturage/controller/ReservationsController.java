package com.covoiturage.controller;

import com.covoiturage.model.Reservation;
import com.covoiturage.model.StatutReservation;
import com.covoiturage.service.ReservationService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ReservationsController {

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, Long> idColumn;
    @FXML private TableColumn<Reservation, Long> trajetIdColumn;
    @FXML private TableColumn<Reservation, Long> passagerIdColumn;
    @FXML private TableColumn<Reservation, String> dateReservationColumn;
    @FXML private TableColumn<Reservation, StatutReservation> statutColumn;
    @FXML private ComboBox<StatutReservation> statutFilterComboBox;

    private ReservationService reservationService = new ReservationService();

    @FXML
    public void initialize() {
        configureTable();
        configureStatutFilter();
        loadReservations();
    }

    private void configureTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        trajetIdColumn.setCellValueFactory(new PropertyValueFactory<>("trajetId"));
        passagerIdColumn.setCellValueFactory(new PropertyValueFactory<>("passagerId"));
        dateReservationColumn.setCellValueFactory(new PropertyValueFactory<>("dateReservation"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    private void configureStatutFilter() {
        statutFilterComboBox.getItems().addAll(StatutReservation.values());
        statutFilterComboBox.getItems().add(0, null);
        statutFilterComboBox.setPromptText("Tous les statuts");
        statutFilterComboBox.setOnAction(event -> filterReservations());
    }

    private void loadReservations() {
        List<Reservation> reservations = reservationService.findAll();
        reservationsTable.setItems(FXCollections.observableArrayList(reservations));
    }

    private void filterReservations() {
        StatutReservation selectedStatut = statutFilterComboBox.getValue();
        List<Reservation> filteredReservations;
        if (selectedStatut == null) {
            filteredReservations = reservationService.findAll();
        } else {
            filteredReservations = reservationService.findByStatut(selectedStatut);
        }
        reservationsTable.setItems(FXCollections.observableArrayList(filteredReservations));
    }

    @FXML
    private void ajouterReservation() {
        ouvrirFormulaireReservation(null);
    }

    @FXML
    private void modifierReservation() {
        Reservation reservationSelectionnee = reservationsTable.getSelectionModel().getSelectedItem();
        if (reservationSelectionnee != null) {
            ouvrirFormulaireReservation(reservationSelectionnee);
        } else {
            showAlert("Aucune réservation sélectionnée", "Veuillez sélectionner une réservation à modifier.");
        }
    }

    @FXML
    private void supprimerReservation() {
        Reservation reservationSelectionnee = reservationsTable.getSelectionModel().getSelectedItem();
        if (reservationSelectionnee != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer la réservation");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réservation ?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    reservationService.delete(reservationSelectionnee);
                    loadReservations();
                }
            });
        } else {
            showAlert("Aucune réservation sélectionnée", "Veuillez sélectionner une réservation à supprimer.");
        }
    }

    private void ouvrirFormulaireReservation(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/covoiturage/reservation-form-view.fxml"));
            Parent root = loader.load();

            ReservationFormController controller = loader.getController();
            if (reservation != null) {
                controller.setReservation(reservation);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(reservation == null ? "Ajouter une réservation" : "Modifier une réservation");
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setOnHidden(event -> loadReservations());

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire de réservation");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}