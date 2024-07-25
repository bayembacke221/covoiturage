package com.covoiturage.controller;

import com.covoiturage.model.Vehicule;
import com.covoiturage.service.VehiculeService;
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

public class VehiculesController {

    @FXML private TextField searchField;
    @FXML private TableView<Vehicule> vehiculesTable;
    @FXML private TableColumn<Vehicule, Long> idColumn;
    @FXML private TableColumn<Vehicule, String> marqueColumn;
    @FXML private TableColumn<Vehicule, String> modeleColumn;
    @FXML private TableColumn<Vehicule, String> immatriculationColumn;

    private VehiculeService vehiculeService = new VehiculeService();

    @FXML
    public void initialize() {
        configureTable();
        loadVehicules();
    }

    private void configureTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        marqueColumn.setCellValueFactory(new PropertyValueFactory<>("marque"));
        modeleColumn.setCellValueFactory(new PropertyValueFactory<>("modele"));
        immatriculationColumn.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
    }

    private void loadVehicules() {
        List<Vehicule> vehicules = vehiculeService.findAll();
        vehiculesTable.setItems(FXCollections.observableArrayList(vehicules));
    }

    @FXML
    private void rechercher() {
        String searchTerm = searchField.getText();
        List<Vehicule> resultats = vehiculeService.rechercherVehicules(searchTerm);
        vehiculesTable.setItems(FXCollections.observableArrayList(resultats));
    }

    @FXML
    private void ajouterVehicule() {
        ouvrirFormulaireVehicule(null);
    }

    @FXML
    private void modifierVehicule() {
        Vehicule vehiculeSelectionne = vehiculesTable.getSelectionModel().getSelectedItem();
        if (vehiculeSelectionne != null) {
            ouvrirFormulaireVehicule(vehiculeSelectionne);
        } else {
            showAlert("Aucun véhicule sélectionné", "Veuillez sélectionner un véhicule à modifier.");
        }
    }

    @FXML
    private void supprimerVehicule() {
        Vehicule vehiculeSelectionne = vehiculesTable.getSelectionModel().getSelectedItem();
        if (vehiculeSelectionne != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer le véhicule");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer ce véhicule ?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    vehiculeService.delete(vehiculeSelectionne);
                    loadVehicules();
                }
            });
        } else {
            showAlert("Aucun véhicule sélectionné", "Veuillez sélectionner un véhicule à supprimer.");
        }
    }

    private void ouvrirFormulaireVehicule(Vehicule vehicule) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/covoiturage/vehicule-form-view.fxml"));
            Parent root = loader.load();

            VehiculeFormController controller = loader.getController();
            if (vehicule != null) {
                controller.setVehicule(vehicule);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(vehicule == null ? "Ajouter un véhicule" : "Modifier un véhicule");
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setOnHidden(event -> loadVehicules());

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire véhicule");
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