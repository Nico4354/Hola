package servicio;

import modelo.Orden;
import modelo.EstadoOrden;
import modelo.ItemOrden;
import modelo.ItemReceta; 
import modelo.Producto;
import modelo.Mesa; 
import modelo.Empleado; 
import modelo.Ingrediente; // <-- ESTA LÍNEA FALTABA
import java.util.ArrayList;
import java.util.List;

public class ServicioOrdenes {
    private List<Orden> ordenes;
    private int nextId;
    
    private ServicioInventario servicioInventario;

    public ServicioOrdenes(ServicioInventario servicioInventario) {
        this.ordenes = new ArrayList<>();
        this.nextId = 1;
        this.servicioInventario = servicioInventario; 
        inicializarOrdenesEjemplo();
    }

    private void inicializarOrdenesEjemplo() {
        ServicioProductos servicioProductos = new ServicioProductos(this.servicioInventario);
        ServicioMesas servicioMesas = new ServicioMesas();
        ServicioEmpleados servicioEmpleados = new ServicioEmpleados();

        List<Producto> productos = servicioProductos.obtenerTodosProductos();
        List<Mesa> mesas = servicioMesas.obtenerTodasLasMesas();
        
        Empleado mesero1 = servicioEmpleados.buscarEmpleadoPorUsuario("mesero");
        Empleado mesero2 = servicioEmpleados.buscarEmpleadoPorUsuario("admin");

        if (productos.size() < 11 || mesas.size() < 3 || mesero1 == null || mesero2 == null) {
            System.err.println("Error: No hay suficientes datos (productos, mesas, empleados) para inicializar órdenes de ejemplo.");
            return;
        }

        Orden orden1 = new Orden(nextId++, mesas.get(0), mesero1); 
        orden1.agregarItem(new ItemOrden(productos.get(0), 2, "Poco picante")); // 2 Ceviches
        orden1.agregarItem(new ItemOrden(productos.get(5), 1, "")); // 1 Causa
        ordenes.add(orden1); 

        Orden orden2 = new Orden(nextId++, mesas.get(2), mesero1); 
        orden2.agregarItem(new ItemOrden(productos.get(1), 1, "Bien cocido")); // 1 Lomo Saltado
        orden2.agregarItem(new ItemOrden(productos.get(2), 1, "")); // 1 Aji de Gallina
        orden2.agregarItem(new ItemOrden(productos.get(10), 2, "")); // 2 Picarones
        orden2.setEstado(EstadoOrden.EN_PREPARACION);
        procesarConsumoOrden(orden2); 
        ordenes.add(orden2);

        Orden orden3 = new Orden(nextId++, mesas.get(1), mesero2); 
        orden3.agregarItem(new ItemOrden(productos.get(3), 1, "")); // 1 Pachamanca
        orden3.agregarItem(new ItemOrden(productos.get(7), 2, "Sin gluten")); // 2 Anticuchos
        orden3.setEstado(EstadoOrden.LISTA);
        ordenes.add(orden3);
    }

    public List<Orden> obtenerTodasLasOrdenes() {
        return new ArrayList<>(ordenes);
    }

    public List<Orden> obtenerOrdenesPorEstado(EstadoOrden estado) {
        List<Orden> resultado = new ArrayList<>();
        for (Orden orden : ordenes) {
            if (orden.getEstado() == estado) {
                resultado.add(orden);
            }
        }
        return resultado;
    }

    public Orden crearOrden(Mesa mesa, Empleado mesero) {
        Orden nuevaOrden = new Orden(nextId++, mesa, mesero);
        ordenes.add(nuevaOrden);
        return nuevaOrden;
    }

    public Orden buscarOrdenPorId(int id) {
        for (Orden orden : ordenes) {
            if (orden.getId() == id) {
                return orden;
            }
        }
        return null;
    }

    public boolean actualizarOrden(Orden ordenActualizada) {
        for (int i = 0; i < ordenes.size(); i++) {
            Orden orden = ordenes.get(i);
            if (orden.getId() == ordenActualizada.getId()) {
                ordenes.set(i, ordenActualizada);
                return true;
            }
        }
        return false;
    }
    
    public boolean procesarConsumoOrden(Orden orden) {
        if (servicioInventario == null) {
            System.err.println("Servicio de Inventario no inicializado en ServicioOrdenes.");
            return false;
        }

        // 1. Verificar si hay stock
        for (ItemOrden item : orden.getItems()) {
            Producto producto = item.getProducto();
            for (ItemReceta itemReceta : producto.getReceta()) {
                Ingrediente ing = itemReceta.getIngrediente(); // Esta línea daba error
                double cantidadTotalNecesaria = itemReceta.getCantidadNecesaria() * item.getCantidad();
                
                Ingrediente ingEnStock = servicioInventario.buscarIngredientePorId(ing.getId()); // Esta línea daba error
                if (ingEnStock.getCantidadDisponible() < cantidadTotalNecesaria) {
                    return false; 
                }
            }
        }

        // 2. Si hay stock de todo, consumir
        for (ItemOrden item : orden.getItems()) {
            Producto producto = item.getProducto();
            for (ItemReceta itemReceta : producto.getReceta()) {
                Ingrediente ing = itemReceta.getIngrediente(); // Esta línea daba error
                double cantidadTotalNecesaria = itemReceta.getCantidadNecesaria() * item.getCantidad();
                
                servicioInventario.consumirStock(ing.getId(), cantidadTotalNecesaria);
            }
        }
        
        return true; 
    }
}