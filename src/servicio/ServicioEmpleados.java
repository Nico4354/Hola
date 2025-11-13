package servicio;

import modelo.Empleado;
import java.util.ArrayList;
import java.util.List;

public class ServicioEmpleados {
    private List<Empleado> empleados;

    public ServicioEmpleados() {
        empleados = new ArrayList<>();
        // Empleados con nombres peruanos
        empleados.add(new Empleado("Manuel", "admin", "admin123", "administrador", "555-0001", "admin@saboresdelinca.com"));
        empleados.add(new Empleado("María ", "chef", "chef123", "chef", "555-0002", "chef@saboresdelinca.com"));
        empleados.add(new Empleado("David Florencio", "mesero", "mesero123", "mesero", "555-0003", "mesero@saboresdelinca.com"));
        // Añadimos al cliente de ejemplo que estaba en BaseDatosSimulada, aunque no sea un "empleado"
        // Opcional: Podrías crear una clase base "Persona" y que Empleado y Cliente hereden de ella.
        empleados.add(new Empleado("Cliente Ejemplo", "cliente", "cliente123", "cliente", "N/A", "N/A"));
    }

    /**
     * Valida las credenciales de un usuario.
     * Reemplaza la lógica de BaseDatosSimulada.
     * @param usuario El nombre de usuario.
     * @param contraseña La contraseña.
     * @return El objeto Empleado si es válido, o null si no lo es.
     */
    public Empleado validarUsuario(String usuario, String contraseña) {
        for (Empleado emp : empleados) {
            if (emp.getUsuario().equals(usuario) && emp.getContraseña().equals(contraseña)) {
                return emp;
            }
        }
        return null;
    }

    public List<Empleado> obtenerTodosEmpleados() {
        return new ArrayList<>(empleados);
    }

    public boolean agregarEmpleado(Empleado empleado) {
        // Verificar si el usuario ya existe
        for (Empleado emp : empleados) {
            if (emp.getUsuario().equals(empleado.getUsuario())) {
                return false;
            }
        }
        empleados.add(empleado);
        return true;
    }

    public boolean eliminarEmpleado(String usuario) {
        // Evitar que se eliminen los usuarios base
        if (usuario.equals("admin") || usuario.equals("chef") || usuario.equals("mesero")) {
            return false;
        }
        return empleados.removeIf(emp -> emp.getUsuario().equals(usuario));
    }

    public boolean actualizarEmpleado(Empleado empleadoActualizado) {
        for (int i = 0; i < empleados.size(); i++) {
            Empleado emp = empleados.get(i);
            if (emp.getUsuario().equals(empleadoActualizado.getUsuario())) {
                empleados.set(i, empleadoActualizado);
                return true;
            }
        }
        return false;
    }

    public Empleado buscarEmpleadoPorUsuario(String usuario) {
        for (Empleado emp : empleados) {
            if (emp.getUsuario().equals(usuario)) {
                return emp;
            }
        }
        return null;
    }
}