package com.covoiturage.dao;

import com.covoiturage.model.Reservation;
import com.covoiturage.model.StatutReservation;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ReservationDAO extends GenericDAOImpl<Reservation> {

    public List<Reservation> findByPassagerId(Long passagerId) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Reservation> query = session.createQuery(
                    "FROM Reservation WHERE passagerId = :passagerId",
                    Reservation.class
            );
            query.setParameter("passagerId", passagerId);
            return query.list();
        }
    }

    public List<Reservation> findByTrajetId(Long trajetId) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Reservation> query = session.createQuery(
                    "FROM Reservation WHERE trajetId = :trajetId",
                    Reservation.class
            );
            query.setParameter("trajetId", trajetId);
            return query.list();
        }
    }

    public List<Reservation> findByStatut(StatutReservation statut) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Reservation> query = session.createQuery(
                    "FROM Reservation WHERE statut = :statut",
                    Reservation.class
            );
            query.setParameter("statut", statut);
            return query.list();
        }
    }
}