package modelo;

public class Ingrediente {
    private int id;
    private String nombre;
    private String unidadMedida;
    private double cantidadDisponible;
    private double cantidadMinima;
    private String categoria;

    public Ingrediente(int id, String nombre, String unidadMedida, double cantidadDisponible, double cantidadMinima, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.cantidadDisponible = cantidadDisponible;
        this.cantidadMinima = cantidadMinima;
        this.categoria = categoria;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUnidadMedida() { return unidadMedida; }
    public double getCantidadDisponible() { return cantidadDisponible; }
    public double getCantidadMinima() { return cantidadMinima; }
    public String getCategoria() { return categoria; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setCantidadDisponible(double cantidadDisponible) { 
        this.cantidadDisponible = cantidadDisponible; 
    }

    // MÉTODO QUE FALTA - AGREGAR ESTE
    public boolean necesitaReabastecer() {
        return cantidadDisponible <= cantidadMinima;
    }
}