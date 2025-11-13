package modelo;

public class Mesa {
    private int numero;
    private int capacidad;
    private boolean ocupada;
    private int comensalesActuales;
    private String estado; // "Libre", "Ocupada", "Reservada"

    public Mesa(int numero, int capacidad) {
        this.numero = numero;
        this.capacidad = capacidad;
        this.ocupada = false;
        this.comensalesActuales = 0;
        this.estado = "Libre";
    }

    // Getters
    public int getNumero() { return numero; }
    public int getCapacidad() { return capacidad; }
    public boolean isOcupada() { return ocupada; }
    public int getComensalesActuales() { return comensalesActuales; }
    public String getEstado() { return estado; }

    // Setters
    public void setOcupada(boolean ocupada) { 
        this.ocupada = ocupada; 
        this.estado = ocupada ? "Ocupada" : "Libre";
    }
    
    public void setComensalesActuales(int comensales) {
        if (comensales <= capacidad) {
            this.comensalesActuales = comensales;
            this.ocupada = (comensales > 0);
            this.estado = this.ocupada ? "Ocupada" : "Libre";
        }
    }

    @Override
    public String toString() {
        return "Mesa " + numero + " (" + comensalesActuales + "/" + capacidad + ") - " + estado;
    }
}