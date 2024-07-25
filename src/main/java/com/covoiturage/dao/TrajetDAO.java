package com.covoiturage.dao;

import com.covoiturage.model.Trajet;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class TrajetDAO extends GenericDAOImpl<Trajet> {

    public List<Trajet> rechercherTrajets(String villeDepart, String villeArrivee, Date date, Long conducteurId) {
        try (Session session = HibernateUtil.getSession()) {
            StringBuilder queryBuilder = new StringBuilder("FROM Trajet t WHERE 1=1");
            if (villeDepart != null && !villeDepart.isEmpty()) {
                queryBuilder.append(" AND LOWER(t.villeDepart) LIKE LOWER(:villeDepart)");
            }
            if (villeArrivee != null && !villeArrivee.isEmpty()) {
                queryBuilder.append(" AND LOWER(t.villeArrivee) LIKE LOWER(:villeArrivee)");
            }
            if (date != null) {
                queryBuilder.append(" AND DATE(t.dateDepart) = :date");
            }
            if (conducteurId != null) {
                queryBuilder.append(" AND t.conducteurId = :conducteurId");
            }

            Query<Trajet> query = session.createQuery(queryBuilder.toString(), Trajet.class);

            if (villeDepart != null && !villeDepart.isEmpty()) {
                query.setParameter("villeDepart", "%" + villeDepart + "%");
            }
            if (villeArrivee != null && !villeArrivee.isEmpty()) {
                query.setParameter("villeArrivee", "%" + villeArrivee + "%");
            }
            if (date != null) {
                query.setParameter("date", date);
            }
            if (conducteurId != null) {
                query.setParameter("conducteurId", conducteurId);
            }

            return query.list();
        }
    }


    public List<Trajet> findByDateRangeAndConducteur(Date dateDebut, Date dateFin, Long conducteurId) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM Trajet t WHERE t.conducteurId = :conducteurId " +
                    "AND t.dateDepart >= :dateDebut " +
                    "AND t.dateDepart <= :dateFin";

            Query<Trajet> query = session.createQuery(hql, Trajet.class);
            query.setParameter("conducteurId", conducteurId);
            query.setParameter("dateDebut", dateDebut);
            query.setParameter("dateFin", dateFin);

            return query.list();
        }
    }
}