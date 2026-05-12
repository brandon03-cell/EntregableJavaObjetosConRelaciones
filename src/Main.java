import dao.GimnasioDAO;
import dao.SocioDAO;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.Gimnasio;
import modelo.Socio;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Gimnasio.odb");
        GimnasioDAO gymDAO = new GimnasioDAO(emf);
        SocioDAO socioDAO = new SocioDAO(emf);
        /*
        socioDAO.asignarSocio(11, 1);
        socioDAO.asignarSocio(11, 3);
        socioDAO.asignarSocio(11, 5);
        socioDAO.asignarSocio(11, 8);
        socioDAO.asignarSocio(11, 10);
        socioDAO.asignarSocio(12, 2);
        socioDAO.asignarSocio(12, 4);
        socioDAO.asignarSocio(12, 9);
        socioDAO.asignarSocio(13, 2);
        socioDAO.asignarSocio(13, 4);
        socioDAO.asignarSocio(13, 10);
        socioDAO.asignarSocio(14, 1);
        socioDAO.asignarSocio(14, 2);
        socioDAO.asignarSocio(14, 3);
        socioDAO.asignarSocio(14, 4);
        socioDAO.asignarSocio(14, 5);
        socioDAO.asignarSocio(15, 6);
        socioDAO.asignarSocio(15, 7);
        socioDAO.asignarSocio(15, 8);
        socioDAO.asignarSocio(16, 1);
        socioDAO.asignarSocio(16, 6);
        socioDAO.asignarSocio(16, 9);
        socioDAO.asignarSocio(17, 2);
        socioDAO.asignarSocio(17, 4);
        socioDAO.asignarSocio(17, 7);
        socioDAO.asignarSocio(18, 1);
        socioDAO.asignarSocio(18, 3);
        socioDAO.asignarSocio(18, 5);
        socioDAO.asignarSocio(19, 6);
        socioDAO.asignarSocio(19, 10);

        */
        Gimnasio gymProMax4Kxd = new Gimnasio("Gimnasio Franquista", "CEI", 67.69);
        Socio socioProMax = new Socio("Abraham Lincoln", 56, true);
        gymDAO.insertarGimnasio(gymProMax4Kxd);
        socioDAO.insertarSocio(socioProMax);
        int gId = gymProMax4Kxd.getId();
        int sId = socioProMax.getId();
        System.out.println("Nuevo gym con id: " + gId + ". Y nuevo socio con id: " + sId);
        System.out.println("Asignando socio al gym pa q haga baje de peso aunque esté en el más allá");
        socioDAO.asignarSocio(sId, gId);
        List<Gimnasio> susGyms = socioDAO.obtenerGimnasiosSocio(sId);
        System.out.println("El socio " + socioProMax.getNombreCompleto() + " está en: " + susGyms);
        socioDAO.borrarSocioDeGimnasio(sId, gId);
        socioDAO.borrarSocio(sId);
        gymDAO.borrarGimnasio(gId);

        System.out.println("\nEsto es simplemente para ver que están todos los datos de la base de datos");
        List<Gimnasio> todosLosGimnasios = gymDAO.obtenerTodosGimnasios();
        for (Gimnasio g : todosLosGimnasios) {
            System.out.println(g);
        }
        System.out.println("\nEstos son los Socios");
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