package com.covoiturage.controller;


import com.covoiturage.model.TypeUtilisateur;
import com.covoiturage.model.Utilisateur;
import com.covoiturage.service.UtilisateurService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

public class UtilisateurFormController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField typeUtilisateurField;

    private UtilisateurService utilisateurService = new UtilisateurService();
    private Utilisateur utilisateur;
    private boolean isNewUser = true;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        isNewUser = false;
        nomField.setText(utilisateur.getNom());
        prenomField.setText(utilisateur.getPrenom());
        emailField.setText(utilisateur.getEmail());
        typeUtilisateurField.setText(utilisateur.getType().name());
    }

    @FXML
    private void enregistrer() {
        if (isNewUser) {
            utilisateur = new Utilisateur();
        }

        utilisateur.setNom(nomField.getText());
        utilisateur.setPrenom(prenomField.getText());
        utilisateur.setEmail(emailField.getText());
        utilisateur.setType(TypeUtilisateur.valueOf(typeUtilisateurField.getText()));

        if (isNewUser) {
            utilisateurService.save(utilisateur);
        } else {
            utilisateurService.update(utilisateur);
        }

        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void annuler() {
        fermerFenetre();
    }


}
