package com.covoiturage.controller;

import com.covoiturage.model.TypeUtilisateur;
import com.covoiturage.model.Utilisateur;
import com.covoiturage.service.UtilisateurService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UtilisateursController {

    @FXML private TextField searchField;
    @FXML private TableView<Utilisateur> passagersTable;
    @FXML private TableView<Utilisateur> conducteursTable;
    @FXML private TableColumn<Utilisateur, Long> passagerIdColumn;
    @FXML private TableColumn<Utilisateur, String> passagerNomColumn;
    @FXML private TableColumn<Utilisateur, String> passagerPrenomColumn;
    @FXML private TableColumn<Utilisateur, String> passagerEmailColumn;
    @FXML private TableColumn<Utilisateur, Void> passagerActionsColumn;
    @FXML private TableColumn<Utilisateur, Long> conducteurIdColumn;
    @FXML private TableColumn<Utilisateur, String> conducteurNomColumn;
    @FXML private TableColumn<Utilisateur, String> conducteurPrenomColumn;
    @FXML private TableColumn<Utilisateur, String> conducteurEmailColumn;
    @FXML private TableColumn<Utilisateur, Void> conducteurActionsColumn;

    private UtilisateurService utilisateurService = new UtilisateurService();

    @FXML
    public void initialize() {
        configurePassagersTable();
        configureConducteursTable();
        loadUtilisateurs();
    }

    private void configurePassagersTable() {
        passagerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        passagerNomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        passagerPrenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        passagerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        configureActionColumn(passagerActionsColumn);
    }

    private void configureConducteursTable() {
        conducteurIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        conducteurNomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        conducteurPrenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        conducteurEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        configureActionColumn(conducteurActionsColumn);
    }

    private void configureActionColumn(TableColumn<Utilisateur, Void> column) {
        column.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Modifier");
            private final Button deleteBtn = new Button("Supprimer");

            {
                editBtn.setOnAction(event -> {
                    Utilisateur utilisateur = getTableView().getItems().get(getIndex());
                    modifierUtilisateur(utilisateur);
                });
                deleteBtn.setOnAction(event -> {
                    Utilisateur utilisateur = getTableView().getItems().get(getIndex());
                    supprimerUtilisateur(utilisateur);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, editBtn, deleteBtn));
                }
            }
        });
    }

    private void loadUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.findAll();
        List<Utilisateur> passagers = utilisateurs.stream()
                .filter(u -> u.getType() == TypeUtilisateur.PASSAGER)
                .collect(Collectors.toList());
        List<Utilisateur> conducteurs = utilisateurs.stream()
                .filter(u -> u.getType() == TypeUtilisateur.CONDUCTEUR)
                .collect(Collectors.toList());

        passagersTable.setItems(FXCollections.observableArrayList(passagers));
        conducteursTable.setItems(FXCollections.observableArrayList(conducteurs));
    }

    @FXML
    private void rechercher() {
        String searchTerm = searchField.getText();
        List<Utilisateur> resultats = utilisateurService.rechercherUtilisateurs(searchTerm);
        List<Utilisateur> passagers = resultats.stream()
                .filter(u -> u.getType() == TypeUtilisateur.PASSAGER)
                .collect(Collectors.toList());
        List<Utilisateur> conducteurs = resultats.stream()
                .filter(u -> u.getType() == TypeUtilisateur.CONDUCTEUR)
                .collect(Collectors.toList());

        passagersTable.setItems(FXCollections.observableArrayList(passagers));
        conducteursTable.setItems(FXCollections.observableArrayList(conducteurs));
    }

    @FXML
    private void ajouterUtilisateur() {
        ouvrirFormulaireUtilisateur(null);
        loadUtilisateurs();
    }

    private void modifierUtilisateur(Utilisateur utilisateur) {
        ouvrirFormulaireUtilisateur(utilisateur);
    }

    private void ouvrirFormulaireUtilisateur(Utilisateur utilisateur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/covoiturage/utilisateur-form-view.fxml"));
            Parent root = loader.load();

            UtilisateurFormController controller = loader.getController();
            if (utilisateur != null) {
                controller.setUtilisateur(utilisateur);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(utilisateur == null ? "Ajouter un utilisateur" : "Modifier un utilisateur");
            stage.initModality(Modality.APPLICATION_MODAL);


            stage.setOnHidden(event -> loadUtilisateurs());

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Erreur lors de l'ouverture du formulaire utilisateur");
        }
    }

    private void supprimerUtilisateur(Utilisateur utilisateur) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'utilisateur");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet utilisateur ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                utilisateurService.delete(utilisateur);
                loadUtilisateurs();
            }
        });
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}