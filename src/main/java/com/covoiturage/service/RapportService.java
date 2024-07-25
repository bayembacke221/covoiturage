package com.covoiturage.service;

import com.covoiturage.dao.TrajetDAO;
import com.covoiturage.model.Trajet;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RapportService {
    private TrajetDAO trajetDAO = new TrajetDAO();

    public Map<String, Long> getNombreTrajetsDuMoisParConducteur(Long conducteurId, int mois, int annee) {
        LocalDate debut = LocalDate.of(annee, mois, 1);
        LocalDate fin = debut.plusMonths(1).minusDays(1);

        List<Trajet> trajets = trajetDAO.findByDateRangeAndConducteur(
                Date.from(debut.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(fin.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                conducteurId
        );

        return trajets.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getVilleDepart() + " - " + t.getVilleArrivee(),
                        Collectors.counting()
                ));
    }

    public Map<String, Double> getMontantGagneParMoisParConducteur(Long conducteurId, int annee) {
        LocalDate debut = LocalDate.of(annee, 1, 1);
        LocalDate fin = LocalDate.of(annee, 12, 31);

        List<Trajet> trajets = trajetDAO.findByDateRangeAndConducteur(
                Date.from(debut.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(fin.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                conducteurId
        );

        return trajets.stream()
                .collect(Collectors.groupingBy(
                        t -> String.valueOf(t.getDateDepart().getMonth() + 1),
                        Collectors.summingDouble(Trajet::getPrix)
                ));
    }
}