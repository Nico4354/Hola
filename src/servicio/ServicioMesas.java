package servicio;

import modelo.Mesa;
import java.util.ArrayList;
import java.util.List;

public class ServicioMesas {
    private List<Mesa> mesas;

    // LA CORRECCIÓN ESTÁ AQUÍ:
    // Asegúrate de que esta línea sea "public", no "private".
    public ServicioMesas() {
        mesas = new ArrayList<>();
        inicializarMesas();
    }

    private void inicializarMesas() {
        // Crear 10 mesas con capacidades variadas
        for (int i = 1; i <= 10; i++) {
            int capacidad = (i % 3 == 0) ? 6 : (i % 2 == 0) ? 4 : 2;
            mesas.add(new Mesa(i, capacidad));
        }
    }

    public List<Mesa> obtenerTodasLasMesas() {
        return new ArrayList<>(mesas);
    }

    public Mesa buscarMesaPorNumero(int numero) {
        for (Mesa mesa : mesas) {
            if (mesa.getNumero() == numero) {
                return mesa;
            }
        }
        return null;
    }

    public boolean ocuparMesa(int numeroMesa, int comensales) {
        Mesa mesa = buscarMesaPorNumero(numeroMesa);
        if (mesa != null && !mesa.isOcupada() && comensales <= mesa.getCapacidad()) {
            mesa.setComensalesActuales(comensales);
            return true;
        }
        return false;
    }

    public boolean liberarMesa(int numeroMesa) {
        Mesa mesa = buscarMesaPorNumero(numeroMesa);
        if (mesa != null && mesa.isOcupada()) {
            mesa.setComensalesActuales(0);
            return true;
        }
        return false;
    }

    public List<Mesa> obtenerMesasOcupadas() {
        List<Mesa> ocupadas = new ArrayList<>();
        for (Mesa mesa : mesas) {
            if (mesa.isOcupada()) {
                ocupadas.add(mesa);
            }
        }
        return ocupadas;
    }

    public List<Mesa> obtenerMesasLibres() {
        List<Mesa> libres = new ArrayList<>();
        for (Mesa mesa : mesas) {
            if (!mesa.isOcupada()) {
                libres.add(mesa);
            }
        }
        return libres;
    }
}