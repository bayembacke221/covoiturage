package com.covoiturage;

import com.covoiturage.dao.HibernateUtil;
import com.covoiturage.model.TypeUtilisateur;
import com.covoiturage.model.Utilisateur;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class CovoiturageApp extends Application {

    @Override
    public void init() throws Exception {
        super.init();
        createTestUsers();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("connexion-view.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Gestion de Covoiturage");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(400);
            primaryStage.setMinHeight(500);

            // Rendre la scène responsive
            scene.widthProperty().addListener((obs, oldVal, newVal) -> {
                root.prefWidth(newVal.doubleValue());
            });
            scene.heightProperty().addListener((obs, oldVal, newVal) -> {
                root.prefHeight(newVal.doubleValue());
            });

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTestUsers() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();

            // Vérifier si des utilisateurs existent déjà
            long userCount = (long) session.createQuery("SELECT COUNT(u) FROM Utilisateur u").getSingleResult();

            if (userCount == 0) {
                // Créer des utilisateurs de test
                Utilisateur gerant = new Utilisateur();
                gerant.setNom("Dupont");
                gerant.setPrenom("Jean");
                gerant.setEmail("jean.dupont@covoiturage.com");
                gerant.setMotDePasse(BCrypt.hashpw("motdepasse", BCrypt.gensalt()));
                gerant.setType(TypeUtilisateur.GERANT);
                gerant.setPeutSeConnecter(true);

                Utilisateur conducteur = new Utilisateur();
                conducteur.setNom("Martin");
                conducteur.setPrenom("Marie");
                conducteur.setEmail("marie.martin@covoiturage.com");
                conducteur.setMotDePasse(BCrypt.hashpw("motdepasse", BCrypt.gensalt()));
                conducteur.setType(TypeUtilisateur.GERANT);
                conducteur.setPeutSeConnecter(true);

                Utilisateur passager = new Utilisateur();
                passager.setNom("Lefevre");
                passager.setPrenom("Pierre");
                passager.setEmail("pierre.lefevre@covoiturage.com");
                passager.setMotDePasse(BCrypt.hashpw("motdepasse", BCrypt.gensalt()));
                passager.setType(TypeUtilisateur.GERANT);
                passager.setPeutSeConnecter(true);
                createUser(session, gerant.getNom(),
                        gerant.getPrenom(),
                        gerant.getEmail(),
                        gerant.getMotDePasse(),
                        gerant.getType(),
                        gerant.isPeutSeConnecter()
                        );
                createUser(session,
                        conducteur.getNom(),
                        conducteur.getPrenom(),
                        conducteur.getEmail(),
                        conducteur.getMotDePasse(),
                        conducteur.getType(),
                        conducteur.isPeutSeConnecter()
                        );
                createUser(session,
                        passager.getNom(),
                        passager.getPrenom(),
                        passager.getEmail(),
                        passager.getMotDePasse(),
                        passager.getType(),
                        passager.isPeutSeConnecter()
                );

                System.out.println("Utilisateurs de test créés avec succès.");
            } else {
                System.out.println("Des utilisateurs existent déjà dans la base de données.");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("Erreur lors de la création des utilisateurs de test : " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void createUser(Session session, String nom, String prenom, String email, String motDePasse,TypeUtilisateur typeUtilisateur, boolean estConducteur) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setEmail(email);
        utilisateur.setMotDePasse(motDePasse);
        utilisateur.setType(typeUtilisateur);
        utilisateur.setPeutSeConnecter(estConducteur);
        session.save(utilisateur);
    }

    public static void main(String[] args) {
        launch(args);
    }


}