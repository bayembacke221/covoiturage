package com.covoiturage.controller;

import com.covoiturage.model.Utilisateur;
import com.covoiturage.service.RapportService;
import com.covoiturage.service.UtilisateurService;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class RapportController {

    @FXML
    private ComboBox<Utilisateur> conducteurComboBox;

    @FXML
    private ComboBox<String> moisComboBox;

    @FXML
    private ComboBox<String> anneeComboBox;

    @FXML
    private BarChart<String, Number> trajetsMoisBarChart;

    @FXML
    private PieChart montantGagnePieChart;

    private RapportService rapportService = new RapportService();
    private UtilisateurService utilisateurService = new UtilisateurService();

    @FXML
    public void initialize() {
        List<Utilisateur> conducteurs = utilisateurService.findAllConducteurs();
        conducteurComboBox.getItems().addAll(conducteurs);

        conducteurComboBox.setConverter(new StringConverter<Utilisateur>() {
            @Override
            public String toString(Utilisateur utilisateur) {
                return utilisateur == null ? "" : utilisateur.getNom() + " " + utilisateur.getPrenom();
            }

            @Override
            public Utilisateur fromString(String string) {
                return null;
            }
        });

        // Initialiser les ComboBox pour les mois et les années
        for (int i = 1; i <= 12; i++) {
            moisComboBox.getItems().add(String.valueOf(i));
        }
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear - 5; i <= currentYear; i++) {
            anneeComboBox.getItems().add(String.valueOf(i));
        }

        conducteurComboBox.setOnAction(e -> updateCharts());
        moisComboBox.setOnAction(e -> updateCharts());
        anneeComboBox.setOnAction(e -> updateCharts());
    }

    private void updateCharts() {
        Utilisateur selectedConducteur = conducteurComboBox.getValue();
        String selectedMois = moisComboBox.getValue();
        String selectedAnnee = anneeComboBox.getValue();

        if (selectedConducteur == null || selectedMois == null || selectedAnnee == null) {
            return;
        }

        Long conducteurId = selectedConducteur.getId();
        int mois = Integer.parseInt(selectedMois);
        int annee = Integer.parseInt(selectedAnnee);

        updateTrajetsMoisBarChart(conducteurId, mois, annee);
        updateMontantGagnePieChart(conducteurId, annee);
    }

    private void updateTrajetsMoisBarChart(Long conducteurId, int mois, int annee) {
        Map<String, Long> data = rapportService.getNombreTrajetsDuMoisParConducteur(conducteurId, mois, annee);

        trajetsMoisBarChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<String, Long> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        trajetsMoisBarChart.getData().add(series);
    }

    private void updateMontantGagnePieChart(Long conducteurId, int annee) {
        Map<String, Double> data = rapportService.getMontantGagneParMoisParConducteur(conducteurId, annee);

        montantGagnePieChart.getData().clear();

        for (Map.Entry<String, Double> entry : data.entrySet()) {
            montantGagnePieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
    }
}