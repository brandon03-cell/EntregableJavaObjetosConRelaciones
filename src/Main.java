import dao.GimnasioDAO;
import dao.SocioDAO;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.Gimnasio;
import modelo.Socio;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Gimnasio.odb");
        GimnasioDAO gymDAO = new GimnasioDAO(emf);
        SocioDAO socioDAO = new SocioDAO(emf);

        /*
        socioDAO.asignarSocio(1, 1);
        socioDAO.asignarSocio(1, 3);
        socioDAO.asignarSocio(1, 5);
        socioDAO.asignarSocio(1, 8);
        socioDAO.asignarSocio(1, 10);
        socioDAO.asignarSocio(2, 2);
        socioDAO.asignarSocio(2, 4);
        socioDAO.asignarSocio(2, 9);
        socioDAO.asignarSocio(3, 2);
        socioDAO.asignarSocio(3, 4);
        socioDAO.asignarSocio(3, 10);
        socioDAO.asignarSocio(4, 1);
        socioDAO.asignarSocio(4, 2);
        socioDAO.asignarSocio(4, 3);
        socioDAO.asignarSocio(4, 4);
        socioDAO.asignarSocio(4, 5);
        socioDAO.asignarSocio(5, 6);
        socioDAO.asignarSocio(5, 7);
        socioDAO.asignarSocio(5, 8);
        socioDAO.asignarSocio(6, 1);
        socioDAO.asignarSocio(6, 6);
        socioDAO.asignarSocio(6, 9);
        socioDAO.asignarSocio(7, 2);
        socioDAO.asignarSocio(7, 4);
        socioDAO.asignarSocio(7, 7);
        socioDAO.asignarSocio(8, 1);
        socioDAO.asignarSocio(8, 3);
        socioDAO.asignarSocio(8, 5);
        socioDAO.asignarSocio(9, 6);
        socioDAO.asignarSocio(9, 10);
        socioDAO.asignarSocio(10, 5);
        socioDAO.asignarSocio(10, 8);
         */

        Gimnasio gymProMax4Kxd = new Gimnasio("Gimnasio Franquista", "CEI", 67.69);
        Socio socioProMax = new Socio("Abraham Lincoln", 56, true);
        gymDAO.insertarGimnasio(gymProMax4Kxd);
        socioDAO.insertarSocio(socioProMax);
        int gId = gymProMax4Kxd.getId();
        int sId = socioProMax.getId();
        System.out.println("\n===================================================================================================");
        System.out.println("    Estos son el Gym y Socio de prueba para que los métodos de insercción y borrado sean visibles");
        System.out.println("===================================================================================================\n");
        System.out.println("Nuevo gym con id: " + gId + ". Y nuevo socio con id: " + sId);
        System.out.println("Asignando socio al gym pa q baje de peso aunque esté en el más allá");
        socioDAO.asignarSocio(sId, gId);
        System.out.println("El socio " + socioProMax.getNombreCompleto() + " está en: " + gymProMax4Kxd.getNombre());
        socioDAO.borrarSocioDeGimnasio(sId, gId);
        socioDAO.borrarSocio(sId);
        gymDAO.borrarGimnasio(gId);

        System.out.println("\n===================================================================================================");
        System.out.println("                                     Métodos de GimnasioDAO");
        System.out.println("===================================================================================================\n");
        System.out.println("Método para obtener los socios de un gimnasio concreto");
        List<Socio> obtenerSociosGimnasio = gymDAO.obtenerSociosGimnasio(3);
        System.out.println("Los socios de ese gimnasio son: ");
        for (Socio socio : obtenerSociosGimnasio) {
            System.out.println(socio.getNombreCompleto());
        }
        System.out.println("\nMétodo para obtener el número de socios inscritos a cada gimnasio");
        Map<String, Long> estadisticas = gymDAO.obtenerNumSociosGimnasio();
        estadisticas.forEach((nombre, total) -> {
            System.out.println("Gimnasio: " + nombre + " | Socios: " + total);
        });


        System.out.println("\n===================================================================================================");
        System.out.println("         Esto es simplemente para ver que están todos los datos de la base de datos");
        System.out.println("===================================================================================================\n");
        List<Gimnasio> todosLosGimnasios = gymDAO.obtenerTodosGimnasios();
        for (Gimnasio g : todosLosGimnasios) {
            System.out.println(g);
        }
        System.out.println("\n===================================================================================================");
        System.out.println("                                    Estos son los Socios");
        System.out.println("===================================================================================================\n");
        List<Socio> todosLosSocios = socioDAO.obtenerTodosSocios();
        for (Socio socio : todosLosSocios) {
            System.out.println(socio);
        }
        /*
        gymDAO.insertarGimnasio(new Gimnasio("Iron Temple", "Madrid", 45.99));
        gymDAO.insertarGimnasio(new Gimnasio("Sparta Fitness", "Barcelona", 29.90));
        gymDAO.insertarGimnasio(new Gimnasio("Yoga & Flow", "Valencia", 60.00));
        gymDAO.insertarGimnasio(new Gimnasio("LowCost Gym", "Sevilla", 19.95));
        gymDAO.insertarGimnasio(new Gimnasio("Elite Performance", "Madrid", 85.00));
        gymDAO.insertarGimnasio(new Gimnasio("CrossFit Box 33", "Bilbao", 70.00));
        gymDAO.insertarGimnasio(new Gimnasio("Padel & Gym", "Málaga", 35.50));
        gymDAO.insertarGimnasio(new Gimnasio("Wellness Center", "Zaragoza", 55.00));
        gymDAO.insertarGimnasio(new Gimnasio("Heavy Metal Lifting", "Vigo", 25.00));
        gymDAO.insertarGimnasio(new Gimnasio("Zumba Party", "Alicante", 32.00));
        socioDAO.insertarSocio(new Socio("Ana García", 28, true));
        socioDAO.insertarSocio(new Socio("Carlos Pérez", 45, false));
        socioDAO.insertarSocio(new Socio("Lucía Fernández", 19, false));
        socioDAO.insertarSocio(new Socio("Marcos Ruiz", 34, true));
        socioDAO.insertarSocio(new Socio("Elena Sanz", 52, false));
        socioDAO.insertarSocio(new Socio("David León", 23, true));
        socioDAO.insertarSocio(new Socio("Sara Cano", 31, false));
        socioDAO.insertarSocio(new Socio("Roberto Gómez", 60, true));
        socioDAO.insertarSocio(new Socio("Irene Molina", 26, false));
        socioDAO.insertarSocio(new Socio("Javier Ortiz", 40, false));
         */
    }
}