package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import modelo.Socio;

public class SocioDAO {
    private EntityManagerFactory emf;

    public SocioDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void insertarSocio(Socio socio) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(socio);
        em.getTransaction().commit();
        em.close();
    }
}