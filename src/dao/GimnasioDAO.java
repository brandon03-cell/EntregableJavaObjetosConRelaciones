package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import modelo.Gimnasio;
import modelo.Socio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Long> obtenerNumSociosGimnasio() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Object[]> query = em.createQuery("select g.nombre, count(s) from Gimnasio g left join g.socios s group by g.nombre", Object[].class);
        List<Object[]> resultado = query.getResultList();
        Map<String, Long> mapa = new HashMap<>();
        for (Object[] fila : resultado) {
            mapa.put((String) fila[0], (Long) fila[1]);
        }
        em.close();
        return mapa;
    }

    public List<Gimnasio> obtenerGimnasiosMenosDe10Socios() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Gimnasio> query = em.createQuery("SELECT g FROM Gimnasio g LEFT JOIN g.socios s GROUP BY g HAVING COUNT(s) < 10", Gimnasio.class);
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

    public Gimnasio obtenerGimnasioMasBaratoCiudadxd(String ciudad) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Gimnasio> query = em.createQuery("select g from Gimnasio g where g.ciudad = :ciudad order by g.cuotaMensual asc", Gimnasio.class);
        query.setParameter("ciudad", ciudad);
        query.setMaxResults(1);
        Gimnasio g = query.getSingleResult();
        em.close();
        return g;
    }

    public List<Gimnasio> obtenerTodosGimnasios() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Gimnasio> query = em.createQuery("select g from Gimnasio g", Gimnasio.class);
        List<Gimnasio> lista = query.getResultList();
        em.close();
        return lista;
    }
}