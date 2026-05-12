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
        Gimnasio gymProMax4Kxd = new Gimnasio("Gimnasio Franquista", "CEI", 67.69);
        gymDAO.insertarGimnasio(gymProMax4Kxd);
        int idDinamico = gymProMax4Kxd.getId();
        gymDAO.borrarGimnasio(idDinamico);
         */

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