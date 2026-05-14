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
    private EntityManagerFactory emf;

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

    /**
     * Obtiene la lista de socios asociados a un gimnasio específico mediante su ID.
     * Utiliza la navegación de objetos de JPA en lugar de una consulta JPQL explícita.
     */
    public List<Socio> obtenerSociosGimnasioApoyo(int id) {
        // 1. Instanciamos el EntityManager para abrir la conexión con la base de datos (ObjectDB)
        EntityManager em = emf.createEntityManager();

        // 2. Buscamos el objeto Gimnasio en la BD usando su clave primaria (ID)
        // El método .find() devuelve la entidad completa mapeada como objeto Java
        Gimnasio g = em.find(Gimnasio.class, id);

        // 3. Inicializamos la lista a devolver como null por seguridad
        List<Socio> lista = null;

        // 4. Verificamos que el gimnasio exista para evitar un NullPointerException
        if (g != null) {
            // 5. Accedemos a la colección de socios definida en la entidad Gimnasio (@ManyToMany)
            lista = g.getSocios();

            // 6. Verificamos que la lista recuperada no sea nula
            if (lista != null) {
                /**
                 * TRUCO DE CARGA (LAZY LOADING):
                 * En JPA, las colecciones suelen cargarse de forma "perezosa" (Lazy).
                 * Al llamar a .size(), obligamos a ObjectDB a cargar los datos reales
                 * de los socios antes de cerrar el EntityManager. Si no lo hiciéramos,
                 * al intentar leer la lista fuera de este método, el programa fallaría.
                 */
                lista.size();
            }
        }

        // 7. Cerramos el EntityManager para liberar recursos de memoria y la conexión
        em.close();

        // 8. Retornamos la lista (puede ser la lista de socios, una lista vacía o null)
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

    /**
     * Calcula el número de socios por cada gimnasio y devuelve los resultados en un Mapa.
     * La clave (String) es el nombre del gimnasio y el valor (Long) es la cantidad de socios.
     */
    public Map<String, Long> obtenerNumSociosGimnasioApoyo() {
        // 1. Abrimos el EntityManager para interactuar con la persistencia
        EntityManager em = emf.createEntityManager();

        /**
         * 2. Definición de la consulta JPQL:
         * - SELECT g.nombre, COUNT(s): Seleccionamos solo dos columnas específicas.
         * - LEFT JOIN g.socios s: Incluimos gimnasios aunque tengan 0 socios.
         * - GROUP BY g.nombre: Agrupamos los resultados para que el COUNT funcione por gimnasio.
         * - Object[].class: Como devolvemos tipos distintos (String y Long), JPA los empaqueta en un array de objetos.
         */
        TypedQuery<Object[]> query = em.createQuery(
                "SELECT g.nombre, COUNT(s) FROM Gimnasio g LEFT JOIN g.socios s GROUP BY g.nombre",
                Object[].class
        );

        // 3. Ejecutamos la consulta y obtenemos una lista donde cada elemento es un Object[] (una "fila")
        List<Object[]> resultado = query.getResultList();

        // 4. Creamos un HashMap para transformar la lista técnica en algo fácil de usar en el Main
        Map<String, Long> mapa = new HashMap<>();

        // 5. Iteramos sobre cada "fila" del resultado
        for (Object[] fila : resultado) {
            /**
             * 6. Extracción y casting:
             * - fila[0] es el primer elemento del SELECT (g.nombre), por eso casteamos a String.
             * - fila[1] es el segundo elemento (COUNT), que en JPA siempre devuelve un tipo Long.
             */
            mapa.put((String) fila[0], (Long) fila[1]);
        }

        // 7. Cerramos el EntityManager para liberar memoria
        em.close();

        // 8. Devolvemos el mapa procesado
        return mapa;
    }

    public List<Gimnasio> obtenerGimnasiosMenosDe10Socios() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Gimnasio> query = em.createQuery("SELECT g FROM Gimnasio g LEFT JOIN g.socios s GROUP BY g HAVING COUNT(s) < 10", Gimnasio.class);
        List<Gimnasio> lista = query.getResultList();
        em.close();
        return lista;
    }

    /**
     * Recupera una lista de gimnasios que tienen una cantidad de socios inferior a 10.
     * Este método demuestra el uso de filtrado sobre funciones de agregado en JPA.
     */
    public List<Gimnasio> obtenerGimnasiosMenosDe10SociosApoyo() {
        // 1. Iniciamos el EntityManager para gestionar la conexión con ObjectDB
        EntityManager em = emf.createEntityManager();

        /**
         * 2. Construcción de la consulta JPQL avanzada:
         * - SELECT g: Seleccionamos la entidad Gimnasio completa.
         * - LEFT JOIN g.socios s: Realizamos una unión a la izquierda con la colección de socios.
         * Esto es vital para que los gimnasios con 0 socios no sean ignorados por la consulta.
         * - GROUP BY g: Agrupamos los resultados por el objeto Gimnasio (internamente por su ID)
         * para poder realizar operaciones de conteo sobre sus socios.
         * - HAVING COUNT(s) < 10: Aplicamos el filtro de grupo. A diferencia del WHERE,
         * HAVING permite filtrar basándose en el resultado de funciones como COUNT, SUM o AVG.
         */
        TypedQuery<Gimnasio> query = em.createQuery(
                "SELECT g FROM Gimnasio g LEFT JOIN g.socios s GROUP BY g HAVING COUNT(s) < 10",
                Gimnasio.class
        );

        // 3. Ejecutamos la consulta y almacenamos el resultado en una lista de entidades
        List<Gimnasio> lista = query.getResultList();

        // 4. Cerramos el EntityManager para liberar los recursos del sistema
        em.close();

        // 5. Devolvemos la lista de objetos Gimnasio que cumplen la condición
        return lista;
    }

    public List<Gimnasio> obtener5GimnasiosCuotaAlta() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Gimnasio> query = em.createQuery("select g from Gimnasio g order by g.cuotaMensual desc", Gimnasio.class);
        query.setMaxResults(5);
        List<Gimnasio> lista = query.getResultList();
        em.close();
        return lista;
    }

    /**
     * Recupera los 5 gimnasios con la cuota mensual más elevada de la base de datos.
     * Combina la ordenación descendente con la limitación de resultados de JPA.
     */
    public List<Gimnasio> obtener5GimnasiosCuotaAltaApoyo() {
        // 1. Abrimos el EntityManager para realizar la conexión con ObjectDB
        EntityManager em = emf.createEntityManager();

        /**
         * 2. Creación de la consulta JPQL:
         * - SELECT g: Seleccionamos la entidad completa para recuperar objetos Gimnasio.
         * - ORDER BY g.cuotaMensual DESC: Ordenamos los registros basándonos en el
         * atributo cuotaMensual de forma descendente (de mayor a menor).
         */
        TypedQuery<Gimnasio> query = em.createQuery(
                "SELECT g FROM Gimnasio g ORDER BY g.cuotaMensual DESC",
                Gimnasio.class
        );

        /**
         * 3. Limitación de resultados (Paginación):
         * - setMaxResults(5): Esta es la instrucción clave. Le indica al motor de
         * la base de datos que solo nos devuelva los primeros 5 registros de la lista
         * ya ordenada. Es el equivalente al LIMIT 5 de SQL estándar.
         */
        query.setMaxResults(5);

        // 4. Ejecutamos la consulta y volcamos los 5 resultados en la lista
        List<Gimnasio> lista = query.getResultList();

        // 5. Cerramos el EntityManager para liberar los recursos de la sesión
        em.close();

        // 6. Retornamos la lista de los 5 gimnasios más caros
        return lista;
    }

    public Gimnasio obtenerGimnasioMasBaratoCiudadxd(String ciudad) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Gimnasio> query = em.createQuery("SELECT g FROM Gimnasio g WHERE g.ciudad = :ciudad ORDER BY g.cuotaMensual ASC", Gimnasio.class);
        query.setParameter("ciudad", ciudad);
        query.setMaxResults(1);
        List<Gimnasio> resultados = query.getResultList();
        em.close();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    /**
     * Busca el gimnasio con la cuota más económica en una ciudad específica.
     * @param ciudad Nombre de la ciudad para filtrar la búsqueda.
     * @return El objeto Gimnasio más barato o null si no se encuentra ninguno.
     */
    public Gimnasio obtenerGimnasioMasBaratoCiudadxdApoyo(String ciudad) {
        // 1. Abrimos el EntityManager para iniciar la comunicación con ObjectDB
        EntityManager em = emf.createEntityManager();

        /**
         * 2. Definición de la consulta JPQL con parámetros:
         * - WHERE g.ciudad = :ciudad: Filtramos por el parámetro dinámico.
         * - ORDER BY g.cuotaMensual ASC: Ordenamos de forma ascendente (de menor a mayor).
         * Al ser ascendente, el gimnasio más barato siempre será el primero de la lista.
         */
        TypedQuery<Gimnasio> query = em.createQuery(
                "SELECT g FROM Gimnasio g WHERE g.ciudad = :ciudad ORDER BY g.cuotaMensual ASC",
                Gimnasio.class
        );

        // 3. Pasamos el valor del parámetro 'ciudad' a la consulta de forma segura
        query.setParameter("ciudad", ciudad);

        // 4. Limitamos el resultado a 1. Solo nos interesa el primero (el más barato)
        query.setMaxResults(1);

        // 5. Obtenemos el resultado en una lista para evitar excepciones si no hay datos
        List<Gimnasio> resultados = query.getResultList();

        // 6. Cerramos el EntityManager para liberar la conexión
        em.close();

        /**
         * 7. Retorno seguro:
         * - Si la lista está vacía (la ciudad no existe), devolvemos null.
         * - Si hay resultados, devolvemos el objeto en la posición 0.
         */
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    public List<Gimnasio> obtenerTodosGimnasios() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Gimnasio> query = em.createQuery("select g from Gimnasio g", Gimnasio.class);
        List<Gimnasio> lista = query.getResultList();
        em.close();
        return lista;
    }

    //==================================================================================================================
    /**
     * BUSCAR GIMNASIOS POR NOMBRE PARCIAL
     * Filtra los gimnasios cuyo nombre contenga la cadena de texto recibida, sin importar la posición.
     */
    public List<Gimnasio> buscarGimnasiosPorNombre(String nombre) {
        EntityManager em = emf.createEntityManager();

        // El operador LIKE permite buscar patrones. Usamos :nombre como marcador de posición.
        TypedQuery<Gimnasio> query = em.createQuery(
                "SELECT g FROM Gimnasio g WHERE g.nombre LIKE :nombre",
                Gimnasio.class
        );

        // Concatenamos los comodines % antes y después del String para que la búsqueda sea flexible.
        query.setParameter("nombre", "%" + nombre + "%");

        List<Gimnasio> lista = query.getResultList();
        em.close();

        return lista;
    }

    System.out.println("\n--- RESULTADOS DE BÚSQUEDA: 'Iron' ---");

    // Llamamos al método pasando el texto de búsqueda
    List<Gimnasio> encontrados = gymDAO.buscarGimnasiosPorNombre("Iron");

if (encontrados.isEmpty()) {
        System.out.println("No se han encontrado gimnasios que coincidan con la búsqueda.");
    } else {
        for (Gimnasio g : encontrados) {
            // Imprimimos el nombre para verificar que el filtro LIKE ha funcionado
            System.out.println("Gimnasio localizado: " + g.getNombre());
        }
    }

    //==================================================================================================================

    /**
     * BUSCAR GIMNASIOS POR RANGO DE CUOTA
     * Devuelve los gimnasios cuya cuota mensual se encuentra entre un valor mínimo y uno máximo.
     */
    public List<Gimnasio> obtenerGimnasiosPorRangoCuota(double min, double max) {
        EntityManager em = emf.createEntityManager();

        // El operador BETWEEN es inclusivo (incluye los valores min y max).
        TypedQuery<Gimnasio> query = em.createQuery(
                "SELECT g FROM Gimnasio g WHERE g.cuotaMensual BETWEEN :min AND :max",
                Gimnasio.class
        );

        // Seteamos ambos parámetros necesarios para la consulta.
        query.setParameter("min", min);
        query.setParameter("max", max);

        List<Gimnasio> lista = query.getResultList();
        em.close();

        return lista;
    }

    System.out.println("\n--- GIMNASIOS CON CUOTA ENTRE 20€ Y 50€ ---");

    // Buscamos gimnasios con cuotas en el rango de 20 a 50 euros
    List<Gimnasio> rangoGyms = gymDAO.obtenerGimnasiosPorRangoCuota(20.0, 50.0);

if (rangoGyms.isEmpty()) {
        System.out.println("No hay gimnasios en este rango de precio.");
    } else {
        for (Gimnasio g : rangoGyms) {
            System.out.println("Gimnasio: " + g.getNombre() + " | Cuota: " + g.getCuotaMensual() + "€");
        }
    }

    //=================================================================================================================

    /**
     * CALCULAR INGRESOS MENSUALES TOTALES
     * Suma todas las cuotas mensuales de los gimnasios para obtener la facturación total.
     */
    public double obtenerIngresosTotales() {
        EntityManager em = emf.createEntityManager();

        // La función SUM suma los valores de la columna indicada.
        TypedQuery<Double> query = em.createQuery(
                "SELECT SUM(g.cuotaMensual) FROM Gimnasio g",
                Double.class
        );

        // Al ser una función de agregado, devuelve un único resultado (Double).
        Double total = query.getSingleResult();
        em.close();

        // Si no hay gimnasios, SUM devuelve null, por lo que controlamos el retorno.
        return (total != null) ? total : 0.0;
    }

    System.out.println("\n--- FACTURACIÓN TOTAL DEL MES ---");

    // Obtenemos el sumatorio de todas las cuotas
    double ingresos = gymDAO.obtenerIngresosTotales();

System.out.println("La facturación total prevista es de: " + ingresos + "€");

    //==================================================================================================================

    /**
     * BUSCAR GIMNASIOS POR MÍNIMO DE SOCIOS
     * Recupera los gimnasios que tienen un número de socios igual o mayor al indicado.
     */
    public List<Gimnasio> obtenerGimnasiosConMinimoSocios(int minSocios) {
        EntityManager em = emf.createEntityManager();

        // SIZE() es una función de JPQL que devuelve el número de elementos de una colección.
        TypedQuery<Gimnasio> query = em.createQuery(
                "SELECT g FROM Gimnasio g WHERE SIZE(g.socios) >= :min",
                Gimnasio.class
        );

        // Seteamos el valor mínimo para filtrar la cantidad de socios.
        query.setParameter("min", minSocios);

        List<Gimnasio> lista = query.getResultList();
        em.close();

        return lista;
    }

    System.out.println("\n--- GIMNASIOS CON 2 O MÁS SOCIOS ---");

    // Buscamos centros que tengan al menos 2 personas inscritas
    List<Gimnasio> populares = gymDAO.obtenerGimnasiosConMinimoSocios(2);

if (populares.isEmpty()) {
        System.out.println("No hay gimnasios con esa cantidad de socios.");
    } else {
        for (Gimnasio g : populares) {
            System.out.println("Gimnasio: " + g.getNombre() + " | Socios actuales: " + g.getSocios().size());
        }
    }

    //==================================================================================================================

    /**
     * BUSCAR SOCIOS VIP POR RANGO DE EDAD
     * Filtra los socios que tienen activado el estado VIP y cuya edad está entre los límites indicados.
     */
    public List<Socio> obtenerSociosVipPorEdad(int min, int max) {
        EntityManager em = emf.createEntityManager();

        // Combinamos una condición booleana con un rango numérico usando AND.
        TypedQuery<Socio> query = em.createQuery(
                "SELECT s FROM Socio s WHERE s.vip = true AND s.edad BETWEEN :min AND :max",
                Socio.class
        );

        query.setParameter("min", min);
        query.setParameter("max", max);

        List<Socio> lista = query.getResultList();
        em.close();

        return lista;
    }

    System.out.println("\n--- SOCIOS VIP (ENTRE 20 Y 40 AÑOS) ---");

    // Buscamos socios que sean VIP y tengan entre 20 y 40 años
    List<Socio> vips = socioDAO.obtenerSociosVipPorEdad(20, 40);

if (vips.isEmpty()) {
        System.out.println("No se han encontrado socios VIP en ese rango de edad.");
    } else {
        for (Socio s : vips) {
            System.out.println("Socio: " + s.getNombreCompleto() + " | Edad: " + s.getEdad() + " | VIP: " + s.isVip());
        }
    }

    //==================================================================================================================

    /**
     * BUSCAR GIMNASIOS EN VARIAS LOCALIDADES
     * Recupera los gimnasios que se encuentren en cualquiera de las ciudades de la lista.
     */
    public List<Gimnasio> obtenerGimnasiosEnLocalidades(List<String> ciudades) {
        EntityManager em = emf.createEntityManager();

        // El operador IN permite comparar un campo contra una lista de valores.
        TypedQuery<Gimnasio> query = em.createQuery(
                "SELECT g FROM Gimnasio g WHERE g.ciudad IN :listaCiudades",
                Gimnasio.class
        );

        // JPA permite pasar una Colección de Java directamente como parámetro.
        query.setParameter("listaCiudades", ciudades);

        List<Gimnasio> lista = query.getResultList();
        em.close();

        return lista;
    }

    System.out.println("\n--- BUSCANDO GIMNASIOS EN SEVILLA Y ALREDEDORES ---");

    // Creamos la lista de ciudades que queremos filtrar
    List<String> misCiudades = Arrays.asList("Sevilla", "La Rinconada", "Villanueva del Ariscal");

    List<Gimnasio> locales = gymDAO.obtenerGimnasiosEnLocalidades(misCiudades);

if (locales.isEmpty()) {
        System.out.println("No se han encontrado gimnasios en esas localidades.");
    } else {
        for (Gimnasio g : locales) {
            System.out.println("Gimnasio: " + g.getNombre() + " | Localidad: " + g.getCiudad());
        }
    }

    //=================================================================================================================

    /**
     * OBTENER GIMNASIOS ORDENADOS POR CIUDAD Y CUOTA
     * Lista todos los centros ordenados alfabéticamente por ciudad y, en caso de empate, por precio.
     */
    public List<Gimnasio> obtenerGimnasiosOrdenadosDoble() {
        EntityManager em = emf.createEntityManager();

        // Se pueden añadir tantos criterios de ordenación como necesites separados por coma.
        TypedQuery<Gimnasio> query = em.createQuery(
                "SELECT g FROM Gimnasio g ORDER BY g.ciudad ASC, g.cuotaMensual DESC",
                Gimnasio.class
        );

        List<Gimnasio> lista = query.getResultList();
        em.close();
        return lista;
    }

    System.out.println("\n--- LISTADO DE GIMNASIOS (POR CIUDAD Y PRECIO) ---");

    // Llamamos al método que ordena por dos criterios
    List<Gimnasio> ordenados = gymDAO.obtenerGimnasiosOrdenadosDoble();

if (ordenados.isEmpty()) {
        System.out.println("No hay gimnasios registrados.");
    } else {
        for (Gimnasio g : ordenados) {
            // Imprimimos ciudad primero para que se vea el orden principal (A-Z)
            // y luego la cuota para ver el orden secundario (Caro-Barato)
            System.out.println("Ciudad: " + g.getCiudad() + " | Gimnasio: " + g.getNombre() + " | Cuota: " + g.getCuotaMensual() + "€");
        }
    }

    //=================================================================================================================

    /**
     * LISTAR CIUDADES CON GIMNASIOS
     * Devuelve los nombres de las ciudades donde hay al menos un gimnasio, sin repetir nombres.
     */
    public List<String> obtenerCiudadesUnicas() {
        EntityManager em = emf.createEntityManager();

        // DISTINCT elimina los resultados duplicados de la consulta.
        TypedQuery<String> query = em.createQuery(
                "SELECT DISTINCT g.ciudad FROM Gimnasio g",
                String.class
        );

        List<String> ciudades = query.getResultList();
        em.close();
        return ciudades;
    }

    System.out.println("\n--- CIUDADES DONDE OPERAMOS ---");
    List<String> ciudades = gymDAO.obtenerCiudadesUnicas();
for (String c : ciudades) {
        System.out.println("- " + c);
    }

    //==================================================================================================================

    /**
     * BUSCAR POR CIUDAD (IGNORANDO MAYÚSCULAS)
     * Filtra gimnasios por ciudad sin importar cómo esté escrito (Mayus/Minus).
     */
    public List<Gimnasio> buscarCiudadIgnoreCase(String ciudad) {
        EntityManager em = emf.createEntityManager();

        // Convertimos tanto el campo de la BD como el parámetro a mayúsculas.
        TypedQuery<Gimnasio> query = em.createQuery(
                "SELECT g FROM Gimnasio g WHERE UPPER(g.ciudad) = UPPER(:ciudad)",
                Gimnasio.class
        );

        query.setParameter("ciudad", ciudad);

        List<Gimnasio> lista = query.getResultList();
        em.close();
        return lista;
    }

    // Esto encontrará gimnasios en "Sevilla" aunque lo escribas en minúsculas
    List<Gimnasio> resultados = gymDAO.buscarCiudadIgnoreCase("sevilla");
System.out.println("Encontrados: " + resultados.size() + " gimnasios.");

    //==================================================================================================================

    /**
     * OBTENER SOCIOS POR ENCIMA DE LA MEDIA
     * Recupera los socios cuya edad es mayor que el promedio de edad de toda la base de datos.
     */
    public List<Socio> obtenerSociosMayoresQueLaMedia() {
        EntityManager em = emf.createEntityManager();

        // Usamos una subconsulta (SELECT AVG...) dentro del WHERE.
        TypedQuery<Socio> query = em.createQuery(
                "SELECT s FROM Socio s WHERE s.edad > (SELECT AVG(s2.edad) FROM Socio s2)",
                Socio.class
        );

        List<Socio> lista = query.getResultList();
        em.close();
        return lista;
    }

    System.out.println("\n--- SOCIOS MÁS VIEJOS QUE EL PROMEDIO ---");
    List<Socio> veteranos = socioDAO.obtenerSociosMayoresQueLaMedia();
for (Socio s : veteranos) {
        System.out.println(s.getNombreCompleto() + " (" + s.getEdad() + " años)");
    }

    //==================================================================================================================

    /**
     * CONTAR SOCIOS POR CIUDAD
     * Devuelve el número total de socios que están inscritos en gimnasios de una ciudad concreta.
     */
    public long contarSociosEnCiudad(String ciudad) {
        EntityManager em = emf.createEntityManager();

        // Hacemos un JOIN para conectar socios con sus gimnasios y filtrar por ciudad.
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(DISTINCT s) FROM Socio s JOIN s.gimnasios g WHERE g.ciudad = :ciudad",
                Long.class
        );

        query.setParameter("ciudad", ciudad);
        Long total = query.getSingleResult();
        em.close();
        return (total != null) ? total : 0;
    }

    String city = "Sevilla";
    long num = socioDAO.contarSociosEnCiudad(city);
System.out.println("En " + city + " tenemos un total de " + num + " socios inscritos.");

    //==================================================================================================================
}