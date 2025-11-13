package servicio;

import modelo.Ingrediente;
import java.util.ArrayList;
import java.util.List;

public class ServicioInventario {
    private List<Ingrediente> ingredientes;
    private int nextId;

    public ServicioInventario() {
        ingredientes = new ArrayList<>();
        nextId = 1;
        inicializarIngredientesEjemplo();
    }

    private void inicializarIngredientesEjemplo() {
        // Ingredientes típicos de cocina peruana
        agregarIngrediente(new Ingrediente(0, "Pescado fresco", "kg", 15.5, 5.0, "Pescados y Mariscos")); // ID 1
        agregarIngrediente(new Ingrediente(0, "Limón", "kg", 8.0, 3.0, "Frutas y Verduras")); // ID 2
        agregarIngrediente(new Ingrediente(0, "Cebolla roja", "kg", 12.0, 4.0, "Frutas y Verduras")); // ID 3
        agregarIngrediente(new Ingrediente(0, "Ají amarillo", "kg", 2.5, 1.0, "Especias y Condimentos")); // ID 4
        agregarIngrediente(new Ingrediente(0, "Cilantro", "atado", 10.0, 3.0, "Hierbas")); // ID 5
        agregarIngrediente(new Ingrediente(0, "Papa amarilla", "kg", 25.0, 10.0, "Tubérculos")); // ID 6
        agregarIngrediente(new Ingrediente(0, "Lomo de res", "kg", 8.0, 3.0, "Carnes")); // ID 7
        agregarIngrediente(new Ingrediente(0, "Pollo", "kg", 12.0, 5.0, "Carnes")); // ID 8
        agregarIngrediente(new Ingrediente(0, "Arroz", "kg", 20.0, 8.0, "Granos")); // ID 9
        agregarIngrediente(new Ingrediente(0, "Maíz morado", "kg", 3.0, 1.0, "Granos")); // ID 10
        agregarIngrediente(new Ingrediente(0, "Leche evaporada", "l", 10.0, 4.0, "Lácteos")); // ID 11
        agregarIngrediente(new Ingrediente(0, "Queso fresco", "kg", 5.0, 2.0, "Lácteos")); // ID 12
    }

    public List<Ingrediente> obtenerTodosIngredientes() {
        return new ArrayList<>(ingredientes);
    }
    
    public Ingrediente buscarIngredientePorId(int id) {
        for (Ingrediente ing : ingredientes) {
            if (ing.getId() == id) {
                return ing;
            }
        }
        return null;
    }
    
    // CAMBIO: Nuevo método para buscar por nombre (para construir recetas)
    public Ingrediente buscarIngredientePorNombre(String nombre) {
        for (Ingrediente ing : ingredientes) {
            if (ing.getNombre().equalsIgnoreCase(nombre)) {
                return ing;
            }
        }
        return null;
    }


    public List<Ingrediente> obtenerIngredientesBajos() {
        List<Ingrediente> ingredientesBajos = new ArrayList<>();
        for (Ingrediente ingrediente : ingredientes) {
            if (ingrediente.necesitaReabastecer()) {
                ingredientesBajos.add(ingrediente);
            }
        }
        return ingredientesBajos;
    }

    public boolean agregarIngrediente(Ingrediente ingrediente) {
        ingrediente.setId(nextId++);
        ingredientes.add(ingrediente);
        return true;
    }

    public boolean actualizarIngrediente(Ingrediente ingredienteActualizado) {
        for (int i = 0; i < ingredientes.size(); i++) {
            Ingrediente ing = ingredientes.get(i);
            if (ing.getId() == ingredienteActualizado.getId()) {
                ingredientes.set(i, ingredienteActualizado);
                return true;
            }
        }
        return false;
    }
    
    // CAMBIO: Nuevo método para consumir stock
    /**
     * Consume una cantidad de stock de un ingrediente.
     * @param idIngrediente El ID del ingrediente.
     * @param cantidadAConsumir La cantidad a restar.
     * @return true si el stock se consumió, false si no había suficiente stock.
     */
    public boolean consumirStock(int idIngrediente, double cantidadAConsumir) {
        Ingrediente ing = buscarIngredientePorId(idIngrediente);
        if (ing == null) {
            System.err.println("Error de inventario: Ingrediente ID " + idIngrediente + " no encontrado.");
            return false;
        }
        
        if (ing.getCantidadDisponible() >= cantidadAConsumir) {
            ing.setCantidadDisponible(ing.getCantidadDisponible() - cantidadAConsumir);
            return true;
        } else {
            // No hay suficiente stock
            return false;
        }
    }
}