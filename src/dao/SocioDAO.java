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

    /**
     * Obtiene la lista de centros (Gimnasios) a los que pertenece un socio concreto.
     * Se basa en la relación definida en la clase Socio en lugar de hacer una Query.
     */
    public List<Gimnasio> obtenerGimnasiosSocioApoyo(int id) {
        // 1. Abrimos el EntityManager para conectar con el archivo .odb
        EntityManager em = emf.createEntityManager();

        // 2. Recuperamos el objeto Socio de la base de datos usando su ID
        // Es una búsqueda directa por clave primaria, la forma más eficiente posible.
        Socio s = em.find(Socio.class, id);

        // 3. Inicializamos la lista de retorno
        List<Gimnasio> lista = null;

        // 4. Verificación de seguridad: Comprobamos si el socio existe antes de operar
        if (s != null) {
            // 5. Obtenemos la colección de gimnasios navegando por el getter del objeto
            // JPA ya sabe cómo cruzar las tablas gracias a la anotación @ManyToMany
            lista = s.getGimnasios();

            // 6. Control de la "Carga Perezosa" (Lazy Loading)
            if (lista != null) {
                /**
                 * TRUCO TÉCNICO:
                 * Al ser una relación ManyToMany, JPA no trae los datos de los gimnasios
                 * por defecto para ahorrar memoria. Al llamar a .size(), "despertamos"
                 * a la colección y obligamos a la base de datos a cargar los objetos
                 * reales antes de cerrar la conexión (EntityManager).
                 */
                lista.size();
            }
        }

        // 7. Cerramos la sesión para liberar recursos
        em.close();

        // 8. Devolvemos la lista cargada (o null si el socio no existía)
        return lista;
    }

    public double obtenerMediaEdadSocios() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Double> query = em.createQuery("select avg(s.edad) from Socio s", Double.class);
        Double media = query.getSingleResult();
        em.close();
        return media;
    }

    /**
     * Calcula la edad promedio de todos los socios registrados en la base de datos.
     * Utiliza una función de agregado para optimizar el rendimiento.
     */
    public double obtenerMediaEdadSociosApoyo() {
        // 1. Abrimos el EntityManager para conectar con la persistencia
        EntityManager em = emf.createEntityManager();

        /**
         * 2. Definición de la consulta JPQL con función de agregado:
         * - select avg(s.edad): La función AVG() calcula automáticamente el promedio.
         * - Double.class: Las funciones de agregado en JPA devuelven objetos envoltorio (wrappers).
         * Usamos Double (con mayúscula) porque si la tabla está vacía, el resultado será null.
         */
        TypedQuery<Double> query = em.createQuery(
                "SELECT AVG(s.edad) FROM Socio s",
                Double.class
        );

        /**
         * 3. Ejecución de la consulta:
         * - getSingleResult(): Como la media es un único valor numérico, no necesitamos una lista.
         * Este método devuelve directamente el objeto Double con el resultado.
         */
        Double media = query.getSingleResult();

        // 4. Cerramos el EntityManager para liberar los recursos de la conexión
        em.close();

        /**
         * 5. Retorno del valor:
         * - Aquí ocurre un "unboxing" automático de Double (objeto) a double (primitivo).
         * - Nota técnica: Si la tabla estuviera vacía, 'media' sería null y esto podría
         * dar un error. En un entorno real se controlaría con un (media != null ? media : 0.0).
         */
        return media;
    }

    public List<Socio> obtenerSociosSinGimnasio() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Socio> query = em.createQuery("select s from Socio s where s.gimnasios is empty", Socio.class);
        List<Socio> lista = query.getResultList();
        em.close();
        return lista;
    }

    /**
     * Recupera la lista de socios que no están inscritos en ningún gimnasio.
     * Utiliza el operador 'IS EMPTY' para filtrar colecciones sin elementos.
     */
    public List<Socio> obtenerSociosSinGimnasioApoyo() {
        // 1. Instanciamos el EntityManager para abrir la comunicación con ObjectDB
        EntityManager em = emf.createEntityManager();

        /**
         * 2. Construcción de la consulta JPQL:
         * - SELECT s FROM Socio s: Seleccionamos la entidad Socio completa.
         * - WHERE s.gimnasios IS EMPTY: Este es el operador clave.
         * Comprueba si la colección 'gimnasios' (definida como @ManyToMany en la clase Socio)
         * no tiene ningún elemento asociado.
         */
        TypedQuery<Socio> query = em.createQuery(
                "SELECT s FROM Socio s WHERE s.gimnasios IS EMPTY",
                Socio.class
        );

        // 3. Ejecutamos la consulta y almacenamos los resultados en la lista
        List<Socio> lista = query.getResultList();

        // 4. Cerramos el EntityManager para liberar los recursos de la conexión
        em.close();

        // 5. Retornamos la lista de socios "libres"
        return lista;
    }

    public List<Socio> obtenerTodosSocios() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Socio> query = em.createQuery("select s from Socio s", Socio.class);
        List<Socio> lista = query.getResultList();
        em.close();
        return lista;
    }


    //==================================================================================================================

    /**
     * BUSCAR SOCIOS MULTICENTRO
     * Recupera los socios que están inscritos en más de un número determinado de gimnasios.
     */
    public List<Socio> obtenerSociosEnMasDeXGimnasios(int cantidad) {
        EntityManager em = emf.createEntityManager();

        // SIZE() cuenta los elementos de la colección 'gimnasios' dentro de cada Socio.
        TypedQuery<Socio> query = em.createQuery(
                "SELECT s FROM Socio s WHERE SIZE(s.gimnasios) > :cantidad",
                Socio.class
        );

        query.setParameter("cantidad", cantidad);

        List<Socio> lista = query.getResultList();
        em.close();
        return lista;
    }

    System.out.println("\n--- SOCIOS APUNTADOS A MÁS DE 1 GIMNASIO ---");
    List<Socio> activos = socioDAO.obtenerSociosEnMasDeXGimnasios(1);
for (Socio s : activos) {
        System.out.println(s.getNombreCompleto() + " está en " + s.getGimnasios().size() + " centros.");
    }

    //=================================================================================================================

    /**
     * OBTENER SOCIO MÁS JOVEN
     * Busca al socio con la edad mínima registrada en la base de datos.
     */
    public Socio obtenerSocioMasJoven() {
        EntityManager em = emf.createEntityManager();

        // Buscamos el socio cuya edad sea igual al valor mínimo de todas las edades.
        TypedQuery<Socio> query = em.createQuery(
                "SELECT s FROM Socio s WHERE s.edad = (SELECT MIN(s2.edad) FROM Socio s2)",
                Socio.class
        );

        // Usamos una lista por si hay varios socios con la misma edad mínima.
        List<Socio> resultados = query.getResultList();
        em.close();

        return resultados.isEmpty() ? null : resultados.get(0);
    }

    Socio joven = socioDAO.obtenerSocioMasJoven();
if (joven != null) {
        System.out.println("El socio más joven es: " + joven.getNombreCompleto() + " con " + joven.getEdad() + " años.");
    }

    //==================================================================================================================

    /**
     * LISTAR SOCIOS ESTÁNDAR
     * Obtiene los socios que no son VIP, ordenados de mayor a menor edad.
     */
    public List<Socio> obtenerSociosNoVipOrdenados() {
        EntityManager em = emf.createEntityManager();

        TypedQuery<Socio> query = em.createQuery(
                "SELECT s FROM Socio s WHERE s.vip = false ORDER BY s.edad DESC",
                Socio.class
        );

        List<Socio> lista = query.getResultList();
        em.close();
        return lista;
    }

    System.out.println("\n--- SOCIOS NO VIP (ORDENADOS POR EDAD) ---");
    List<Socio> listaNoVip = socioDAO.obtenerSociosNoVipOrdenados();
for (Socio s : listaNoVip) {
        System.out.println(s.getNombreCompleto() + " - " + s.getEdad() + " años");
    }

    //==================================================================================================================

    /**
     * OBTENER SOCIOS VIP CON GIMNASIOS ASIGNADOS
     * Filtra socios que tienen el estado VIP activo y que además están inscritos en al menos un gimnasio.
     */
    public List<Socio> obtenerSociosVipConGimnasios() {
        EntityManager em = emf.createEntityManager();

        // Combinamos un atributo booleano con la función SIZE para verificar que la lista no esté vacía.
        TypedQuery<Socio> query = em.createQuery(
                "SELECT s FROM Socio s WHERE s.vip = true AND SIZE(s.gimnasios) > 0",
                Socio.class
        );

        List<Socio> lista = query.getResultList();
        em.close();
        return lista;
    }

    /**
     * BUSCAR SOCIOS POR NÚMERO EXACTO DE GIMNASIOS
     * Recupera los socios que están inscritos en una cantidad específica de centros (ej. exactamente 2).
     */
    public List<Socio> obtenerSociosPorCantidadGimnasios(int cantidad) {
        EntityManager em = emf.createEntityManager();

        // SIZE permite realizar comparaciones exactas (=) sobre el tamaño de la colección.
        TypedQuery<Socio> query = em.createQuery(
                "SELECT s FROM Socio s WHERE SIZE(s.gimnasios) = :cantidad",
                Socio.class
        );

        query.setParameter("cantidad", cantidad);

        List<Socio> lista = query.getResultList();
        em.close();
        return lista;
    }

    // Prueba del método VIP con gimnasios
System.out.println("\n--- SOCIOS VIP ACTIVOS (CON AL MENOS 1 GYM) ---");
    List<Socio> vipsActivos = socioDAO.obtenerSociosVipConGimnasios();
for (Socio s : vipsActivos) {
        System.out.println("Socio: " + s.getNombreCompleto() + " | Gimnasios: " + s.getGimnasios().size());
    }

    // Prueba del método por cantidad exacta (ejemplo: socios en 2 gimnasios)
    int numBusqueda = 2;
System.out.println("\n--- SOCIOS INSCRITOS EN EXACTAMENTE " + numBusqueda + " GIMNASIOS ---");
    List<Socio> exactos = socioDAO.obtenerSociosPorCantidadGimnasios(numBusqueda);

if (exactos.isEmpty()) {
        System.out.println("No hay socios con exactamente " + numBusqueda + " gimnasios.");
    } else {
        for (Socio s : exactos) {
            System.out.println("- " + s.getNombreCompleto());
        }
    }

    //================================================================================================================
}