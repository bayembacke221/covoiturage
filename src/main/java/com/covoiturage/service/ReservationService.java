package com.covoiturage.service;

import com.covoiturage.dao.ReservationDAO;
import com.covoiturage.model.Reservation;
import com.covoiturage.model.StatutReservation;
import com.covoiturage.model.Trajet;
import com.covoiturage.model.Utilisateur;
import com.covoiturage.util.EmailUtil;

import javax.mail.MessagingException;
import java.util.List;

public class ReservationService {
    private ReservationDAO reservationDAO = new ReservationDAO();
    private TrajetService trajetService = new TrajetService();
    private UtilisateurService utilisateurService = new UtilisateurService();

    public List<Reservation> findAll() {
        return reservationDAO.findAll();
    }

    public List<Reservation> findByPassagerId(Long passagerId) {
        return reservationDAO.findByPassagerId(passagerId);
    }

    public List<Reservation> findByTrajetId(Long trajetId) {
        return reservationDAO.findByTrajetId(trajetId);
    }

    public List<Reservation> findByStatut(StatutReservation statut) {
        return reservationDAO.findByStatut(statut);
    }

    public void save(Reservation reservation) {
        reservationDAO.save(reservation);
//        sendReservationConfirmationEmail(reservation);
    }

    private void sendReservationConfirmationEmail(Reservation reservation) {
        Trajet trajet = trajetService.findById(reservation.getTrajetId());
        Utilisateur passager = utilisateurService.findById(reservation.getPassagerId());

        String subject = "Confirmation de réservation - Covoiturage";
        String content = String.format(
                "Bonjour %s,\n\n" +
                        "Votre réservation a été confirmée pour le trajet suivant :\n" +
                        "De : %s\n" +
                        "À : %s\n" +
                        "Date de départ : %s\n" +
                        "Statut de la réservation : %s\n\n" +
                        "Merci d'utiliser notre service de covoiturage !",
                passager.getPrenom(),
                trajet.getVilleDepart(),
                trajet.getVilleArrivee(),
                trajet.getDateDepart(),
                reservation.getStatut()
        );

        try {
            EmailUtil.sendEmail(passager.getEmail(), subject, content);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void update(Reservation reservation) {
        reservationDAO.update(reservation);
    }

    public void delete(Reservation reservation) {
        reservationDAO.delete(reservation);
    }
}