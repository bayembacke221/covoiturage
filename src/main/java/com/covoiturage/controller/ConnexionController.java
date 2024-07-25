package com.covoiturage.controller;

import com.covoiturage.model.Utilisateur;
import com.covoiturage.service.UtilisateurService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.naming.AuthenticationException;
import java.io.IOException;

public class ConnexionController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private UtilisateurService utilisateurService = new UtilisateurService();

    @FXML
    private void connexion() throws AuthenticationException {
        String email = emailField.getText();
        String motDePasse = passwordField.getText();

        Utilisateur utilisateur = utilisateurService.connecter(email, motDePasse);
        if (utilisateur != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/covoiturage/main-view.fxml"));
                Parent root = loader.load();

                MainController mainController = loader.getController();
                mainController.setUtilisateurConnecte(utilisateur);

                Scene scene = new Scene(root);
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Accueil - Gestion de Covoiturage");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                messageLabel.setText("Erreur lors du chargement de la page d'accueil.");
            }
        } else {
            messageLabel.setText("Email ou mot de passe incorrect.");
        }
    }
}