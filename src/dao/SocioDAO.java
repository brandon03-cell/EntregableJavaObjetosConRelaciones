package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import modelo.Gimnasio;
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

    public void actualizarSocio(int id, String nombre, int edad, boolean vip) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Socio s = em.find(Socio.class, id);
        if (s != null) {
            s.setNombreCompleto(nombre);
            s.setEdad(edad);
            s.setVip(vip);
        }
        em.getTransaction().commit();
        em.close();
    }

    public void borrarSocio(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Socio s = em.find(Socio.class, id);
        if (s != null) {
            em.remove(s);
        }
        em.getTransaction().commit();
        em.close();
    }

    public void asignarSocio(int socioId, int gimnasioId) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Socio s = em.find(Socio.class, socioId);
        Gimnasio g = em.find(Gimnasio.class, gimnasioId);
        if (s != null &&  g != null) {
            s.getGimnasios().add(g);
            g.getSocios().add(s);
        }
        em.getTransaction().commit();
        em.close();
    }
}