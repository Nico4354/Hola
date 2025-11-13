package modelo;

public class ItemOrden {
    private Producto producto;
    private int cantidad;
    private String notas;
    private boolean listo;

    public ItemOrden(Producto producto, int cantidad, String notas) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.notas = notas;
        this.listo = false;
    }

    // Getters
    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public String getNotas() { return notas; }
    public boolean isListo() { return listo; }

    // Setters
    public void setListo(boolean listo) { this.listo = listo; }

    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }
}