package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import modelo.Gimnasio;
import modelo.Socio;

import java.util.List;

public class GimnasioDAO {
    EntityManagerFactory emf;

    public GimnasioDAO(EntityManagerFactory emf) {
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
        em.close();
    }

    public void borrarGimnasio(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Gimnasio g = em.find(Gimnasio.class, id);
        if (g != null) {
            em.remove(g);
        }
        em.getTransaction().commit();
        em.close();
    }

    public List<Socio> obtenerSociosGimnasio(int id) {
        EntityManager em = emf.createEntityManager();
        Gimnasio g = em.find(Gimnasio.class, id);
        List<Socio> lista = null;
        if (g != null) {
            lista = g.getSocios();
            if (lista != null) {
                lista.size();
            }
        }
        em.close();
        return lista;
    }

    public List<Object[]> obtenerNumSociosGimnasio() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Object[]> query = em.createQuery("select g.nombre, count(s) from Gimnasio g left join g.socios s group by g.nombre", Object[].class);
        List<Object[]> resultado = query.getResultList();
        em.close();
        return resultado;
    }

    public List<Gimnasio> obtenerGimnasiosMenosDe10Socios() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Gimnasio> query = em.createQuery("select g from Gimnasio g where size(g.socios) < 10", Gimnasio.class);
        List<Gimnasio> lista = query.getResultList();
        em.close();
        return lista;
    }

    public List<Gimnasio> obtener5GimnasiosCuotaAlta() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Gimnasio> query = em.createQuery("select g from Gimnasio g order by g.cuotaMensual desc", Gimnasio.class);
        List<Gimnasio> lista = query.getResultList();
        em.close();
        return lista;
    }
}