package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import modelo.Gimnasio;
import modelo.Socio;

import java.util.List;

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

    public void borrarSocioDeGimnasio(int socioId, int gimnasioId) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Socio s = em.find(Socio.class, socioId);
        Gimnasio g = em.find(Gimnasio.class, gimnasioId);
        if (s != null && g != null) {
            s.getGimnasios().remove(g);
            g.getSocios().remove(s);
        }
        em.getTransaction().commit();
        em.close();
    }

    public List<Gimnasio> obtenerGimnasiosSocio(int id) {
        EntityManager em = emf.createEntityManager();
        Socio s = em.find(Socio.class, id);
        List<Gimnasio> lista = null;
        if (s != null) {
            lista = s.getGimnasios();
            if (lista != null) {
                lista.size();
            }
        }
        em.close();
        return lista;
    }

    public double obtenerMediaEdadSocios() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Double> query = em.createQuery("select avg(s.edad) from Socio s", Double.class);
        Double media = query.getSingleResult();
        em.close();
        return media;
    }

    public List<Socio> obtenerSociosSinGimnasio() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Socio> query = em.createQuery("select s from Socio s where s.gimnasios is empty", Socio.class);
        List<Socio> lista = query.getResultList();
        em.close();
        return lista;
    }
}