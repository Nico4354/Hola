package modelo;

public class Empleado {
    private String nombre;
    private String usuario;
    private String contraseña;
    private String rol;
    private String telefono;
    private String email;

    public Empleado(String nombre, String usuario, String contraseña, String rol, String telefono, String email) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.rol = rol;
        this.telefono = telefono;
        this.email = email;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getUsuario() { return usuario; }
    public String getContraseña() { return contraseña; }
    public String getRol() { return rol; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
    public void setRol(String rol) { this.rol = rol; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }
}