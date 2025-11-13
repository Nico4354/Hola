package modelo;

import java.util.ArrayList;
import java.util.List;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria;
    private boolean disponible;
    
    // CAMBIO: Cada producto ahora tiene una receta
    private List<ItemReceta> receta;

    public Producto(int id, String nombre, String descripcion, double precio, String categoria, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.disponible = disponible;
        // CAMBIO: Inicializamos la lista de receta
        this.receta = new ArrayList<>();
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }
    public boolean isDisponible() { return disponible; }
    
    // CAMBIO: Getter para la receta
    public List<ItemReceta> getReceta() {
        return receta;
    }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    // CAMBIO: Método para construir la receta
    public void agregarIngredienteReceta(Ingrediente ingrediente, double cantidad) {
        this.receta.add(new ItemReceta(ingrediente, cantidad));
    }

    @Override
    public String toString() {
        return nombre + " - S/" + precio;
    }
}