import dao.GimnasioDAO;
import dao.SocioDAO;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.Gimnasio;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Gimnasio.odb");
        GimnasioDAO gymDAO = new GimnasioDAO(emf);
        Gimnasio gymProMax4Kxd = new Gimnasio("Gimnasio Franquista", "CEI", 67.67);
    }
}