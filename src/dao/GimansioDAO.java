package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import modelo.Gimnasio;

public class GimansioDAO {
    EntityManagerFactory emf;

    public GimansioDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void insertarGimnasio(Gimnasio gimnasio) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(gimnasio);
        em.getTransaction().commit();
        em.close();
    }

    public void actualizarGimnasio(int id, String nombre, String ciudad, double cuota) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Gimnasio g = em.find(Gimnasio.class, id);
        if (g != null) {
            g.setNombre(nombre);
            g.setCiudad(ciudad);
            g.setCuotaMensual(cuota);
        }
        em.getTransaction().commit();
        em.close;
    }
}
