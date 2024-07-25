package com.covoiturage.controller;

import com.covoiturage.model.Trajet;
import com.covoiturage.service.TrajetService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class TrajetsController {

    @FXML private TextField villeDepartField;
    @FXML private TextField villeArriveeField;
    @FXML private DatePicker datePicker;
    @FXML private TableView<Trajet> trajetsTable;
    @FXML private TableColumn<Trajet, Long> idColumn;
    @FXML private TableColumn<Trajet, String> villeDepartColumn;
    @FXML private TableColumn<Trajet, String> villeArriveeColumn;
    @FXML private TableColumn<Trajet, Date> dateDepartColumn;
    @FXML private TableColumn<Trajet, Integer> placesDisponiblesColumn;
    @FXML private TableColumn<Trajet, Double> prixColumn;

    private TrajetService trajetService = new TrajetService();

    @FXML
    public void initialize() {
        configureTable();
        loadTrajets();
    }

    private void configureTable() {
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        villeDepartColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVilleDepart()));
        villeArriveeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVilleArrivee()));
        dateDepartColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateDepart()));
        placesDisponiblesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPlacesDisponibles()).asObject());
        prixColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrix()).asObject());
    }

    private void loadTrajets() {
        List<Trajet> trajets = trajetService.findAll();
        trajetsTable.setItems(FXCollections.observableArrayList(trajets));
    }

    @FXML
    private void rechercher() {
        String villeDepart = villeDepartField.getText();
        String villeArrivee = villeArriveeField.getText();
        Date date = null;
        if (datePicker.getValue() != null) {
            date = Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        List<Trajet> resultats = trajetService.rechercherTrajets(villeDepart, villeArrivee, date, null);
        trajetsTable.setItems(FXCollections.observableArrayList(resultats));
    }

    @FXML
    private void ajouterTrajet() {
        ouvrirFormulaireTrajet(null);
    }

    @FXML
    private void modifierTrajet() {
        Trajet trajetSelectionne = trajetsTable.getSelectionModel().getSelectedItem();
        if (trajetSelectionne != null) {
            ouvrirFormulaireTrajet(trajetSelectionne);
        } else {
            showAlert("Aucun trajet sélectionné", "Veuillez sélectionner un trajet à modifier.");
        }
    }

    @FXML
    private void supprimerTrajet() {
        Trajet trajetSelectionne = trajetsTable.getSelectionModel().getSelectedItem();
        if (trajetSelectionne != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer le trajet");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer ce trajet ?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    trajetService.delete(trajetSelectionne);
                    loadTrajets();
                }
            });
        } else {
            showAlert("Aucun trajet sélectionné", "Veuillez sélectionner un trajet à supprimer.");
        }
    }

    private void ouvrirFormulaireTrajet(Trajet trajet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/covoiturage/trajet-form-view.fxml"));
            Parent root = loader.load();

            TrajetFormController controller = loader.getController();
            if (trajet != null) {
                controller.setTrajet(trajet);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(trajet == null ? "Ajouter un trajet" : "Modifier un trajet");
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setOnHidden(event -> loadTrajets());

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire trajet");
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