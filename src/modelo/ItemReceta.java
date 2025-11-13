package modelo;


public class ItemReceta {
    private Ingrediente ingrediente;
    private double cantidadNecesaria;

    public ItemReceta(Ingrediente ingrediente, double cantidadNecesaria) {
        this.ingrediente = ingrediente;
        this.cantidadNecesaria = cantidadNecesaria;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public double getCantidadNecesaria() {
        return cantidadNecesaria;
    }
}
