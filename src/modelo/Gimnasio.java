package modelo;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Gimnasio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String ciudad;
    private double cuotaMensual;
    @ManyToMany(mappedBy = "gimnasios")
    private List<Socio> socios;

    public Gimnasio(String nombre, String ciudad, double cuotaMensual) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.cuotaMensual = cuotaMensual;
    }

    public Gimnasio() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public double getCuotaMensual() {
        return cuotaMensual;
    }

    public void setCuotaMensual(double cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }

    public List<Socio> getSocios() {
        return socios;
    }

    public void setSocios(List<Socio> socios) {
        this.socios = socios;
    }

    @Override
    public String toString() {
        return "El gimnasio " + this.getNombre() + " está ubicado en " + this.getCiudad() + " y tiene una cuota mensual de: "
                + this.getCuotaMensual() + "€.";
    }
}