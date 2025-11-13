package modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Orden {
    private int id;
    private Mesa mesa; 
    private Empleado mesero; // CAMBIO: de Usuario a Empleado
    private Date fechaHora;
    private EstadoOrden estado;
    private List<ItemOrden> items;
    private String notas;

    // CAMBIO: El constructor ahora recibe Empleado
    public Orden(int id, Mesa mesa, Empleado mesero) {
        this.id = id;
        this.mesa = mesa;
        this.mesero = mesero;
        this.fechaHora = new Date();
        this.estado = EstadoOrden.PENDIENTE;
        this.items = new ArrayList<>();
        this.notas = "";
    }

    // --- Getters ---
    public int getId() { return id; }
    public Date getFechaHora() { return fechaHora; }
    public EstadoOrden getEstado() { return estado; }
    public List<ItemOrden> getItems() { return items; }
    public String getNotas() { return notas; }
    public Mesa getMesa() { return mesa; }

    // CAMBIO: Devuelve un Empleado
    public Empleado getMesero() { return mesero; }

    // --- Setters ---
    public void setEstado(EstadoOrden estado) { this.estado = estado; }
    public void setNotas(String notas) { this.notas = notas; }

    // --- Métodos de conveniencia (para la Vista) ---
    
    public int getNumeroMesa() {
        return (mesa != null) ? mesa.getNumero() : 0;
    }

    public String getNombreMesero() {
        // CAMBIO: usamos getNombre() de Empleado
        return (mesero != null) ? mesero.getNombre() : "N/A";
    }

    // --- Métodos de negocio ---
    
    public void agregarItem(ItemOrden item) {
        items.add(item);
    }

    public double getTotal() {
        double total = 0;
        for (ItemOrden item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public String getEstadoString() {
        switch (estado) {
            case PENDIENTE: return "Pendiente";
            case EN_PREPARACION: return "En Preparación";
            case LISTA: return "Lista";
            case ENTREGADA: return "Entregada";
            case CANCELADA: return "Cancelada";
            default: return "Desconocido";
        }
    }
}