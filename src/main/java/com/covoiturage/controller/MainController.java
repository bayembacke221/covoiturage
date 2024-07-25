    package com.covoiturage.controller;

    import com.covoiturage.model.Utilisateur;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.Alert;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.layout.StackPane;
    import javafx.stage.Stage;

    import java.io.IOException;

    public class MainController {

        @FXML private StackPane contentArea;
        @FXML private Label userNameLabel;
        @FXML private Button btnUtilisateurs;
        @FXML private Button btnVehicules;
        @FXML private Button btnTrajets;
        @FXML private Button btnReservations;

        private Utilisateur utilisateurConnecte;

        @FXML
        public void initialize() {
            updateUI();
        }

        public void setUtilisateurConnecte(Utilisateur utilisateur) {
            this.utilisateurConnecte = utilisateur;
            updateUI();
        }

        private void updateUI() {
            if (utilisateurConnecte != null) {
                userNameLabel.setText("Bienvenue, " + utilisateurConnecte.getPrenom() + " " + utilisateurConnecte.getNom());

                boolean estConducteur = utilisateurConnecte.isPeutSeConnecter();
                btnUtilisateurs.setVisible(estConducteur);
                btnVehicules.setVisible(estConducteur);
                btnTrajets.setVisible(true);
                btnReservations.setVisible(true);
            } else {
                userNameLabel.setText("Utilisateur non connecté");
                btnUtilisateurs.setVisible(false);
                btnVehicules.setVisible(false);
                btnTrajets.setVisible(false);
                btnReservations.setVisible(false);
            }
        }

        @FXML
        private void gererUtilisateurs() {
            if (utilisateurConnecte != null) {
                loadView("utilisateurs-view.fxml");
            } else {
                showAccessDeniedMessage();
            }
        }

        @FXML
        private void gererVehicules() {
            if (utilisateurConnecte != null) {
                loadView("vehicules-view.fxml");
            } else {
                showAccessDeniedMessage();
            }
        }

        @FXML
        private void gererTrajets() {
            if (utilisateurConnecte != null) {
                loadView("trajets-view.fxml");
            } else {
                showAccessDeniedMessage();
            }
        }

        @FXML
        private void gererReservations() {
            if (utilisateurConnecte != null) {
                loadView("reservations-view.fxml");
            } else {
                showAccessDeniedMessage();
            }
        }

        @FXML
        private void afficherStatistiques(){
            if (utilisateurConnecte != null) {
                loadView("statistiques-view.fxml");
            } else {
                showAccessDeniedMessage();
            }
        }

        @FXML
        private void deconnexion() {
            utilisateurConnecte = null;
            updateUI();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/covoiturage/connexion-view.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) userNameLabel.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Connexion - Gestion de Covoiturage");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showErrorMessage("Erreur lors du chargement de la page de connexion");
            }
        }

        private void loadView(String fxmlFile) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/covoiturage/" + fxmlFile));
                Parent view = loader.load();
                contentArea.getChildren().setAll(view);
            } catch (IOException e) {
                e.printStackTrace();
                showErrorMessage("Erreur lors du chargement de la vue");
            }
        }

        private void showAccessDeniedMessage() {
            showErrorMessage("Accès refusé. Vous n'avez pas les permissions nécessaires.");
        }

        private void showErrorMessage(String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }