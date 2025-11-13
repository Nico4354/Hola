package vista;

import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import modelo.Mesa;
import servicio.ServicioMesas;
import java.util.List;
import modelo.Empleado;
import modelo.Producto;
import modelo.Orden;
import modelo.ItemOrden;
import modelo.EstadoOrden;
import modelo.Ingrediente;
import modelo.ItemReceta; // <-- ESTA LÍNEA FALTABA
import servicio.ServicioEmpleados;
import servicio.ServicioProductos;
import servicio.ServicioOrdenes; 
import servicio.ServicioInventario; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {
    private Empleado usuario;
    private JPanel panelContenido;
    private CardLayout cardLayout;

    private ServicioInventario servicioInventario;
    private ServicioProductos servicioProductos;
    private ServicioOrdenes servicioOrdenes;
    private ServicioMesas servicioMesas;
    private ServicioEmpleados servicioEmpleados;

    public VentanaPrincipal(Empleado usuario) {
        this.usuario = usuario;
        
        this.servicioInventario = new ServicioInventario();
        this.servicioProductos = new ServicioProductos(servicioInventario);
        this.servicioOrdenes = new ServicioOrdenes(servicioInventario); 
        this.servicioMesas = new ServicioMesas();
        this.servicioEmpleados = new ServicioEmpleados();

        configurarVentana();
        inicializarComponentes();
        mostrarPanelSegunRol();
    }

    // ... (El resto del código es idéntico) ...
    // ... (No es necesario volver a pegar todo, solo asegúrate de que el 'import modelo.ItemReceta;' esté añadido al inicio de tu archivo) ...
    
    // (Pego el resto del archivo por si acaso, para que puedas reemplazarlo todo y asegurarte)
    
    private void configurarVentana() {
        setTitle("Sistema de Gestión - Restaurante Peruano 'Sabores del Inca' - Usuario: " + usuario.getUsuario());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        JPanel panelCabecera = crearPanelCabecera();
        add(panelCabecera, BorderLayout.NORTH);
        JPanel panelNavegacion = crearPanelNavegacion();
        add(panelNavegacion, BorderLayout.WEST);
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        add(panelContenido, BorderLayout.CENTER);
        inicializarPaneles();
    }

    private JPanel crearPanelCabecera() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(193, 39, 45));
        panel.setPreferredSize(new Dimension(getWidth(), 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel etiquetaTitulo = new JLabel("SABORES DEL INCA - RESTAURANTE PERUANO");
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        etiquetaTitulo.setForeground(Color.WHITE);
        JLabel etiquetaUsuario = new JLabel("Usuario: " + usuario.getUsuario() + " | Rol: " + usuario.getRol());
        etiquetaUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        etiquetaUsuario.setForeground(Color.WHITE);
        panel.add(etiquetaTitulo, BorderLayout.WEST);
        panel.add(etiquetaUsuario, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearPanelNavegacion() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 240, 240));
        panel.setPreferredSize(new Dimension(200, getHeight()));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        String rol = usuario.getRol();
        switch (rol) {
            case "administrador":
                agregarBotonNavegacion(panel, "Dashboard", "dashboard");
                agregarBotonNavegacion(panel, "Gestión de Usuarios", "gestion_usuarios");
                agregarBotonNavegacion(panel, "Gestión de Menú", "gestion_menu");
                agregarBotonNavegacion(panel, "Reportes", "reportes");
                agregarBotonNavegacion(panel, "Configuración", "configuracion");
                break;
            case "chef":
                agregarBotonNavegacion(panel, "Órdenes Pendientes", "ordenes_pendientes");
                agregarBotonNavegacion(panel, "Historial de Órdenes", "historial_ordenes");
                agregarBotonNavegacion(panel, "Inventario Cocina", "inventario_cocina");
                break;
            case "mesero":
                agregarBotonNavegacion(panel, "Tomar Pedido", "tomar_pedido");
                agregarBotonNavegacion(panel, "Mesas", "gestion_mesas");
                agregarBotonNavegacion(panel, "Órdenes Activas", "ordenes_activas");
                break;
            case "cliente":
                agregarBotonNavegacion(panel, "Ver Menú", "ver_menu");
                agregarBotonNavegacion(panel, "Realizar Pedido", "realizar_pedido");
                agregarBotonNavegacion(panel, "Estado Pedido", "estado_pedido");
                break;
        }
        panel.add(Box.createVerticalGlue());
        JButton botonSalir = new JButton("Cerrar Sesión");
        botonSalir.setBackground(new Color(220, 53, 69));
        botonSalir.setForeground(Color.WHITE);
        botonSalir.setMaximumSize(new Dimension(180, 40));
        botonSalir.addActionListener(e -> cerrarSesion());
        panel.add(botonSalir);
        return panel;
    }

    private void agregarBotonNavegacion(JPanel panel, String texto, String comando) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(180, 45));
        boton.setBackground(new Color(200, 200, 200));
        boton.setFocusPainted(false);
        boton.setMargin(new Insets(10, 10, 10, 10));
        boton.addActionListener(e -> cardLayout.show(panelContenido, comando));
        panel.add(boton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void inicializarPaneles() {
        panelContenido.add(crearPanelBienvenida(), "dashboard");
        String rol = usuario.getRol();
        if (rol.equals("administrador")) {
            panelContenido.add(crearPanelGestionUsuarios(servicioEmpleados), "gestion_usuarios");
            panelContenido.add(crearPanelGestionMenu(servicioProductos), "gestion_menu");
            panelContenido.add(crearPanelReportes(), "reportes");
            panelContenido.add(crearPanelConfiguracion(), "configuracion");
        } else if (rol.equals("chef")) {
            panelContenido.add(crearPanelOrdenesPendientes(servicioOrdenes), "ordenes_pendientes");
            panelContenido.add(crearPanelHistorialOrdenes(servicioOrdenes), "historial_ordenes");
            panelContenido.add(crearPanelInventarioCocina(servicioInventario), "inventario_cocina");
        } else if (rol.equals("mesero")) {
            panelContenido.add(crearPanelTomarPedido(servicioMesas, servicioProductos, servicioOrdenes), "tomar_pedido");
            panelContenido.add(crearPanelGestionMesas(servicioMesas), "gestion_mesas");
            panelContenido.add(crearPanelOrdenesActivas(servicioOrdenes), "ordenes_activas");
        } else if (rol.equals("cliente")) {
            panelContenido.add(crearPanelVerMenu(), "ver_menu");
            panelContenido.add(crearPanelRealizarPedido(), "realizar_pedido");
            panelContenido.add(crearPanelEstadoPedido(), "estado_pedido");
        }
    }

    private void mostrarPanelSegunRol() {
        cardLayout.show(panelContenido, "dashboard");
    }

    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel etiqueta = new JLabel("¡Bienvenido al Sistema de Gestión!", JLabel.CENTER);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 28));
        etiqueta.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        JLabel etiquetaRol = new JLabel("Rol: " + usuario.getRol(), JLabel.CENTER);
        etiquetaRol.setFont(new Font("Arial", Font.PLAIN, 18));
        etiquetaRol.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        panel.add(etiqueta, BorderLayout.NORTH);
        panel.add(etiquetaRol, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelGestionUsuarios(ServicioEmpleados servicioEmpleados) {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        String[] columnas = {"Nombre", "Usuario", "Rol", "Teléfono", "Email"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaEmpleados = new JTable(modeloTabla);
        actualizarTablaEmpleados(modeloTabla, servicioEmpleados);
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton botonAgregar = new JButton("Agregar Empleado");
        JButton botonEditar = new JButton("Editar Empleado");
        JButton botonEliminar = new JButton("Eliminar Empleado");
        panelBotones.add(botonAgregar);
        panelBotones.add(botonEditar);
        panelBotones.add(botonEliminar);
        panelPrincipal.add(new JScrollPane(tablaEmpleados), BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        botonAgregar.addActionListener(e -> mostrarDialogoAgregarEmpleado(servicioEmpleados, modeloTabla));
        botonEditar.addActionListener(e -> {
            int filaSeleccionada = tablaEmpleados.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(panelPrincipal, "Por favor seleccione un empleado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String usuario = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
            Empleado empleado = servicioEmpleados.buscarEmpleadoPorUsuario(usuario);
            if (empleado != null) {
                mostrarDialogoEditarEmpleado(empleado, servicioEmpleados, modeloTabla);
            }
        });
        botonEliminar.addActionListener(e -> {
            int filaSeleccionada = tablaEmpleados.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(panelPrincipal, "Por favor seleccione un empleado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String usuario = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
            String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
            int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal, "¿Está seguro de eliminar al empleado: " + nombre + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (servicioEmpleados.eliminarEmpleado(usuario)) {
                    actualizarTablaEmpleados(modeloTabla, servicioEmpleados);
                    JOptionPane.showMessageDialog(panelPrincipal, "Empleado eliminado exitosamente");
                } else {
                    JOptionPane.showMessageDialog(panelPrincipal, "No se puede eliminar a este usuario", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return panelPrincipal;
    }

    private void actualizarTablaEmpleados(DefaultTableModel modeloTabla, ServicioEmpleados servicioEmpleados) {
        modeloTabla.setRowCount(0);
        for (Empleado empleado : servicioEmpleados.obtenerTodosEmpleados()) {
            Object[] fila = { empleado.getNombre(), empleado.getUsuario(), empleado.getRol(), empleado.getTelefono(), empleado.getEmail() };
            modeloTabla.addRow(fila);
        }
    }

    private void mostrarDialogoAgregarEmpleado(ServicioEmpleados servicioEmpleados, DefaultTableModel modeloTabla) {
        JDialog dialogo = new JDialog(this, "Agregar Nuevo Empleado", true);
        dialogo.setLayout(new GridLayout(7, 2, 10, 10));
        dialogo.setSize(400, 350);
        dialogo.setLocationRelativeTo(this);
        JTextField campoNombre = new JTextField();
        JTextField campoUsuario = new JTextField();
        JPasswordField campoContraseña = new JPasswordField();
        JComboBox<String> comboRol = new JComboBox<>(new String[]{"administrador", "chef", "mesero", "cliente"});
        JTextField campoTelefono = new JTextField();
        JTextField campoEmail = new JTextField();
        dialogo.add(new JLabel("Nombre Completo:"));
        dialogo.add(campoNombre);
        dialogo.add(new JLabel("Usuario:"));
        dialogo.add(campoUsuario);
        dialogo.add(new JLabel("Contraseña:"));
        dialogo.add(campoContraseña);
        dialogo.add(new JLabel("Rol:"));
        dialogo.add(comboRol);
        dialogo.add(new JLabel("Teléfono:"));
        dialogo.add(campoTelefono);
        dialogo.add(new JLabel("Email:"));
        dialogo.add(campoEmail);
        JButton botonGuardar = new JButton("Guardar");
        JButton botonCancelar = new JButton("Cancelar");
        botonGuardar.addActionListener(ev -> {
            String nombre = campoNombre.getText();
            String usuario = campoUsuario.getText();
            String contraseña = new String(campoContraseña.getPassword());
            String rol = (String) comboRol.getSelectedItem();
            String telefono = campoTelefono.getText();
            String email = campoEmail.getText();
            if (nombre.isEmpty() || usuario.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Por favor complete los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Empleado nuevoEmpleado = new Empleado(nombre, usuario, contraseña, rol, telefono, email);
            if (servicioEmpleados.agregarEmpleado(nuevoEmpleado)) {
                actualizarTablaEmpleados(modeloTabla, servicioEmpleados);
                dialogo.dispose();
                JOptionPane.showMessageDialog(this, "Empleado agregado exitosamente");
            } else {
                JOptionPane.showMessageDialog(dialogo, "El usuario ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        botonCancelar.addActionListener(ev -> dialogo.dispose());
        dialogo.add(botonGuardar);
        dialogo.add(botonCancelar);
        dialogo.setVisible(true);
    }

    private void mostrarDialogoEditarEmpleado(Empleado empleado, ServicioEmpleados servicioEmpleados, DefaultTableModel modeloTabla) {
        JDialog dialogo = new JDialog(this, "Editar Empleado", true);
        dialogo.setLayout(new GridLayout(7, 2, 10, 10));
        dialogo.setSize(400, 350);
        dialogo.setLocationRelativeTo(this);
        JTextField campoNombre = new JTextField(empleado.getNombre());
        JTextField campoUsuario = new JTextField(empleado.getUsuario());
        campoUsuario.setEditable(false);
        JPasswordField campoContraseña = new JPasswordField(empleado.getContraseña());
        JComboBox<String> comboRol = new JComboBox<>(new String[]{"administrador", "chef", "mesero", "cliente"});
        comboRol.setSelectedItem(empleado.getRol());
        JTextField campoTelefono = new JTextField(empleado.getTelefono());
        JTextField campoEmail = new JTextField(empleado.getEmail());
        dialogo.add(new JLabel("Nombre Completo:"));
        dialogo.add(campoNombre);
        dialogo.add(new JLabel("Usuario:"));
        dialogo.add(campoUsuario);
        dialogo.add(new JLabel("Contraseña:"));
        dialogo.add(campoContraseña);
        dialogo.add(new JLabel("Rol:"));
        dialogo.add(comboRol);
        dialogo.add(new JLabel("Teléfono:"));
        dialogo.add(campoTelefono);
        dialogo.add(new JLabel("Email:"));
        dialogo.add(campoEmail);
        JButton botonGuardar = new JButton("Guardar Cambios");
        JButton botonCancelar = new JButton("Cancelar");
        botonGuardar.addActionListener(ev -> {
            String nombre = campoNombre.getText();
            String contraseña = new String(campoContraseña.getPassword());
            String rol = (String) comboRol.getSelectedItem();
            String telefono = campoTelefono.getText();
            String email = campoEmail.getText();
            if (nombre.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Por favor complete los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            empleado.setNombre(nombre);
            empleado.setContraseña(contraseña);
            empleado.setRol(rol);
            empleado.setTelefono(telefono);
            empleado.setEmail(email);
            if (servicioEmpleados.actualizarEmpleado(empleado)) {
                actualizarTablaEmpleados(modeloTabla, servicioEmpleados);
                dialogo.dispose();
                JOptionPane.showMessageDialog(this, "Empleado actualizado exitosamente");
            } else {
                JOptionPane.showMessageDialog(dialogo, "Error al actualizar el empleado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        botonCancelar.addActionListener(ev -> dialogo.dispose());
        dialogo.add(botonGuardar);
        dialogo.add(botonCancelar);
        dialogo.setVisible(true);
    }

    private JPanel crearPanelGestionMenu(ServicioProductos servicioProductos) {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        String[] columnas = {"ID", "Nombre", "Descripción", "Precio (S/)", "Categoría", "Disponible"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaProductos = new JTable(modeloTabla);
        actualizarTablaProductos(modeloTabla, servicioProductos);
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton botonAgregar = new JButton("Agregar Producto");
        JButton botonEditar = new JButton("Editar Producto");
        JButton botonEliminar = new JButton("Eliminar Producto");
        panelBotones.add(botonAgregar);
        panelBotones.add(botonEditar);
        panelBotones.add(botonEliminar);
        panelPrincipal.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        botonAgregar.addActionListener(e -> mostrarDialogoAgregarProducto(servicioProductos, modeloTabla));
        botonEditar.addActionListener(e -> {
            int filaSeleccionada = tablaProductos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(panelPrincipal, "Por favor seleccione un producto", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            Producto producto = servicioProductos.buscarProductoPorId(id);
            if (producto != null) {
                mostrarDialogoEditarProducto(producto, servicioProductos, modeloTabla);
            }
        });
        botonEliminar.addActionListener(e -> {
            int filaSeleccionada = tablaProductos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(panelPrincipal, "Por favor seleccione un producto", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
            int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal, "¿Está seguro de eliminar el producto: " + nombre + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (servicioProductos.eliminarProducto(id)) {
                    actualizarTablaProductos(modeloTabla, servicioProductos);
                    JOptionPane.showMessageDialog(panelPrincipal, "Producto eliminado exitosamente");
                } else {
                    JOptionPane.showMessageDialog(panelPrincipal, "Error al eliminar el producto", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return panelPrincipal;
    }

    private void actualizarTablaProductos(DefaultTableModel modeloTabla, ServicioProductos servicioProductos) {
        modeloTabla.setRowCount(0);
        for (Producto producto : servicioProductos.obtenerTodosProductos()) {
            Object[] fila = { producto.getId(), producto.getNombre(), producto.getDescripcion(), producto.getPrecio(), producto.getCategoria(), producto.isDisponible() ? "Sí" : "No" };
            modeloTabla.addRow(fila);
        }
    }

    private void mostrarDialogoAgregarProducto(ServicioProductos servicioProductos, DefaultTableModel modeloTabla) {
        JDialog dialogo = new JDialog(this, "Agregar Nuevo Producto", true);
        dialogo.setLayout(new GridLayout(7, 2, 10, 10));
        dialogo.setSize(400, 350);
        dialogo.setLocationRelativeTo(this);
        JTextField campoNombre = new JTextField();
        JTextField campoDescripcion = new JTextField();
        JTextField campoPrecio = new JTextField();
        JComboBox<String> comboCategoria = new JComboBox<>(new String[]{"Plato Principal", "Entrada", "Postre", "Bebida", "Sopa", "Especialidad de la Casa"});
        JCheckBox checkDisponible = new JCheckBox("Disponible", true);
        dialogo.add(new JLabel("Nombre:"));
        dialogo.add(campoNombre);
        dialogo.add(new JLabel("Descripción:"));
        dialogo.add(campoDescripcion);
        dialogo.add(new JLabel("Precio (S/):"));
        dialogo.add(campoPrecio);
        dialogo.add(new JLabel("Categoría:"));
        dialogo.add(comboCategoria);
        dialogo.add(new JLabel("Disponible:"));
        dialogo.add(checkDisponible);
        JButton botonGuardar = new JButton("Guardar");
        JButton botonCancelar = new JButton("Cancelar");
        botonGuardar.addActionListener(ev -> {
            String nombre = campoNombre.getText();
            String descripcion = campoDescripcion.getText();
            String precioTexto = campoPrecio.getText();
            String categoria = (String) comboCategoria.getSelectedItem();
            boolean disponible = checkDisponible.isSelected();
            if (nombre.isEmpty() || descripcion.isEmpty() || precioTexto.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Por favor complete los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double precio;
            try {
                precio = Double.parseDouble(precioTexto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "El precio debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Producto nuevoProducto = new Producto(0, nombre, descripcion, precio, categoria, disponible);
            if (servicioProductos.agregarProducto(nuevoProducto)) {
                actualizarTablaProductos(modeloTabla, servicioProductos);
                dialogo.dispose();
                JOptionPane.showMessageDialog(this, "Producto agregado exitosamente");
            } else {
                JOptionPane.showMessageDialog(dialogo, "Error al agregar el producto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        botonCancelar.addActionListener(ev -> dialogo.dispose());
        dialogo.add(botonGuardar);
        dialogo.add(botonCancelar);
        dialogo.setVisible(true);
    }

    private void mostrarDialogoEditarProducto(Producto producto, ServicioProductos servicioProductos, DefaultTableModel modeloTabla) {
        JDialog dialogo = new JDialog(this, "Editar Producto", true);
        dialogo.setLayout(new GridLayout(7, 2, 10, 10));
        dialogo.setSize(400, 350);
        dialogo.setLocationRelativeTo(this);
        JTextField campoNombre = new JTextField(producto.getNombre());
        JTextField campoDescripcion = new JTextField(producto.getDescripcion());
        JTextField campoPrecio = new JTextField(String.valueOf(producto.getPrecio()));
        JComboBox<String> comboCategoria = new JComboBox<>(new String[]{"Plato Principal", "Entrada", "Postre", "Bebida", "Sopa", "Especialidad de la Casa"});
        comboCategoria.setSelectedItem(producto.getCategoria());
        JCheckBox checkDisponible = new JCheckBox("Disponible", producto.isDisponible());
        dialogo.add(new JLabel("Nombre:"));
        dialogo.add(campoNombre);
        dialogo.add(new JLabel("Descripción:"));
        dialogo.add(campoDescripcion);
        dialogo.add(new JLabel("Precio (S/):"));
        dialogo.add(campoPrecio);
        dialogo.add(new JLabel("Categoría:"));
        dialogo.add(comboCategoria);
        dialogo.add(new JLabel("Disponible:"));
        dialogo.add(checkDisponible);
        JButton botonGuardar = new JButton("Guardar Cambios");
        JButton botonCancelar = new JButton("Cancelar");
        botonGuardar.addActionListener(ev -> {
            String nombre = campoNombre.getText();
            String descripcion = campoDescripcion.getText();
            String precioTexto = campoPrecio.getText();
            String categoria = (String) comboCategoria.getSelectedItem();
            boolean disponible = checkDisponible.isSelected();
            if (nombre.isEmpty() || descripcion.isEmpty() || precioTexto.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Por favor complete los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double precio;
            try {
                precio = Double.parseDouble(precioTexto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "El precio debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setCategoria(categoria);
            producto.setDisponible(disponible);
            if (servicioProductos.actualizarProducto(producto)) {
                actualizarTablaProductos(modeloTabla, servicioProductos);
                dialogo.dispose();
                JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente");
            } else {
                JOptionPane.showMessageDialog(dialogo, "Error al actualizar el producto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        botonCancelar.addActionListener(ev -> dialogo.dispose());
        dialogo.add(botonGuardar);
        dialogo.add(botonCancelar);
        dialogo.setVisible(true);
    }

    private JPanel crearPanelOrdenesPendientes(ServicioOrdenes servicioOrdenes) {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        String[] columnas = {"ID", "Mesa", "Mesero", "Estado", "Total", "Hora"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaOrdenes = new JTable(modeloTabla);
        actualizarTablaOrdenesChef(modeloTabla, servicioOrdenes);
        JPanel panelDetalles = new JPanel(new BorderLayout());
        panelDetalles.setBorder(BorderFactory.createTitledBorder("Detalles de la Orden"));
        panelDetalles.setPreferredSize(new Dimension(300, 0));
        JTextArea areaDetalles = new JTextArea();
        areaDetalles.setEditable(false);
        areaDetalles.setFont(new Font("Arial", Font.PLAIN, 12));
        panelDetalles.add(new JScrollPane(areaDetalles), BorderLayout.CENTER);
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton botonPreparar = new JButton("Comenzar Preparación");
        JButton botonLista = new JButton("Marcar como Lista");
        JButton botonActualizar = new JButton("Actualizar");
        botonPreparar.setBackground(new Color(255, 193, 7));
        botonLista.setBackground(new Color(40, 167, 69));
        botonLista.setForeground(Color.WHITE);
        panelBotones.add(botonPreparar);
        panelBotones.add(botonLista);
        panelBotones.add(botonActualizar);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tablaOrdenes), panelDetalles);
        splitPane.setDividerLocation(600);
        panelPrincipal.add(splitPane, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        tablaOrdenes.getSelectionModel().addListSelectionListener(e -> {
            int filaSeleccionada = tablaOrdenes.getSelectedRow();
            if (filaSeleccionada != -1) {
                int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
                Orden orden = servicioOrdenes.buscarOrdenPorId(id);
                if (orden != null) {
                    mostrarDetallesOrden(orden, areaDetalles);
                }
            }
        });

        botonPreparar.addActionListener(e -> {
            int filaSeleccionada = tablaOrdenes.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(panelPrincipal, "Por favor seleccione una orden", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            Orden orden = servicioOrdenes.buscarOrdenPorId(id);
            
            if (orden != null && orden.getEstado() == EstadoOrden.PENDIENTE) {
                boolean stockConsumido = servicioOrdenes.procesarConsumoOrden(orden);
                if (stockConsumido) {
                    orden.setEstado(EstadoOrden.EN_PREPARACION);
                    servicioOrdenes.actualizarOrden(orden);
                    actualizarTablaOrdenesChef(modeloTabla, servicioOrdenes);
                    mostrarDetallesOrden(orden, areaDetalles);
                    JOptionPane.showMessageDialog(panelPrincipal, "Orden en preparación. Stock descontado.");
                } else {
                    JOptionPane.showMessageDialog(panelPrincipal, 
                        "Error: Stock insuficiente para preparar la orden #" + orden.getId() + ".\n" +
                        "Revise el inventario.", 
                        "Error de Stock", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        botonLista.addActionListener(e -> {
            int filaSeleccionada = tablaOrdenes.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(panelPrincipal, "Por favor seleccione una orden", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            Orden orden = servicioOrdenes.buscarOrdenPorId(id);
            if (orden != null && orden.getEstado() == EstadoOrden.EN_PREPARACION) {
                orden.setEstado(EstadoOrden.LISTA);
                servicioOrdenes.actualizarOrden(orden);
                actualizarTablaOrdenesChef(modeloTabla, servicioOrdenes);
                mostrarDetallesOrden(orden, areaDetalles);
                JOptionPane.showMessageDialog(panelPrincipal, "Orden marcada como lista");
            }
        });
        
        botonActualizar.addActionListener(e -> actualizarTablaOrdenesChef(modeloTabla, servicioOrdenes));
        return panelPrincipal;
    }

    private void actualizarTablaOrdenesChef(DefaultTableModel modeloTabla, ServicioOrdenes servicioOrdenes) {
        modeloTabla.setRowCount(0);
        for (Orden orden : servicioOrdenes.obtenerTodasLasOrdenes()) {
            if (orden.getEstado() == EstadoOrden.PENDIENTE || orden.getEstado() == EstadoOrden.EN_PREPARACION) {
                Object[] fila = { orden.getId(), orden.getNumeroMesa(), orden.getNombreMesero(), orden.getEstadoString(), "S/" + orden.getTotal(), orden.getFechaHora() };
                modeloTabla.addRow(fila);
            }
        }
    }

    private void mostrarDetallesOrden(Orden orden, JTextArea areaDetalles) {
        StringBuilder detalles = new StringBuilder();
        detalles.append("Orden #").append(orden.getId()).append("\n");
        detalles.append("Mesa: ").append(orden.getNumeroMesa()).append("\n");
        detalles.append("Mesero: ").append(orden.getNombreMesero()).append("\n");
        detalles.append("Estado: ").append(orden.getEstadoString()).append("\n");
        detalles.append("Hora: ").append(orden.getFechaHora()).append("\n\n");
        detalles.append("ITEMS:\n");
        for (ItemOrden item : orden.getItems()) {
            detalles.append("• ").append(item.getCantidad()).append("x ").append(item.getProducto().getNombre()).append(" - S/").append(item.getSubtotal()).append("\n");
            if (!item.getNotas().isEmpty()) {
                detalles.append("  Notas: ").append(item.getNotas()).append("\n");
            }
            if (!item.getProducto().getReceta().isEmpty()) {
                detalles.append("    Receta:\n");
                // Esta es la línea (661 en tu error) que necesitaba el import
                for (ItemReceta ir : item.getProducto().getReceta()) {
                    detalles.append("    - ").append(ir.getIngrediente().getNombre())
                            .append(": ").append(ir.getCantidadNecesaria() * item.getCantidad())
                            .append(" ").append(ir.getIngrediente().getUnidadMedida()).append("\n");
                }
            }
        }
        detalles.append("\nTOTAL: S/").append(orden.getTotal());
        areaDetalles.setText(detalles.toString());
    }

    private JPanel crearPanelHistorialOrdenes(ServicioOrdenes servicioOrdenes) {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        String[] columnas = {"ID", "Mesa", "Mesero", "Estado", "Total", "Hora"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaHistorial = new JTable(modeloTabla);
        actualizarTablaHistorial(modeloTabla, servicioOrdenes);
        JPanel panelFiltros = new JPanel(new FlowLayout());
        JComboBox<String> comboFiltro = new JComboBox<>();
        comboFiltro.addItem("Todas");
        for (EstadoOrden estado : EstadoOrden.values()) {
            comboFiltro.addItem(estado.toString()); 
        }
        JButton botonFiltrar = new JButton("Filtrar");
        panelFiltros.add(new JLabel("Filtrar por estado:"));
        panelFiltros.add(comboFiltro);
        panelFiltros.add(botonFiltrar);
        panelPrincipal.add(panelFiltros, BorderLayout.NORTH);
        panelPrincipal.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);
        botonFiltrar.addActionListener(e -> {
            String filtro = (String) comboFiltro.getSelectedItem();
            actualizarTablaHistorialFiltrado(modeloTabla, servicioOrdenes, filtro);
        });
        return panelPrincipal;
    }

    private void actualizarTablaHistorial(DefaultTableModel modeloTabla, ServicioOrdenes servicioOrdenes) {
        modeloTabla.setRowCount(0);
        for (Orden orden : servicioOrdenes.obtenerTodasLasOrdenes()) {
            Object[] fila = { orden.getId(), orden.getNumeroMesa(), orden.getNombreMesero(), orden.getEstadoString(), "S/" + orden.getTotal(), orden.getFechaHora() };
            modeloTabla.addRow(fila);
        }
    }

    private void actualizarTablaHistorialFiltrado(DefaultTableModel modeloTabla, ServicioOrdenes servicioOrdenes, String filtro) {
        modeloTabla.setRowCount(0);
        for (Orden orden : servicioOrdenes.obtenerTodasLasOrdenes()) {
            if (filtro.equals("Todas") || orden.getEstado().toString().equals(filtro)) {
                Object[] fila = { orden.getId(), orden.getNumeroMesa(), orden.getNombreMesero(), orden.getEstadoString(), "S/" + orden.getTotal(), orden.getFechaHora() };
                modeloTabla.addRow(fila);
            }
        }
    }

    private JPanel crearPanelInventarioCocina(ServicioInventario servicioInventario) {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        String[] columnas = {"ID", "Ingrediente", "Categoría", "Disponible", "Mínimo", "Unidad", "Estado"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaInventario = new JTable(modeloTabla);
        actualizarTablaInventario(modeloTabla, servicioInventario);
        JPanel panelAlertas = new JPanel(new BorderLayout());
        panelAlertas.setBorder(BorderFactory.createTitledBorder("Alertas de Inventario Bajo"));
        panelAlertas.setPreferredSize(new Dimension(300, 0));
        JTextArea areaAlertas = new JTextArea();
        areaAlertas.setEditable(false);
        areaAlertas.setFont(new Font("Arial", Font.PLAIN, 12));
        areaAlertas.setForeground(Color.RED);
        actualizarAlertasInventario(areaAlertas, servicioInventario);
        panelAlertas.add(new JScrollPane(areaAlertas), BorderLayout.CENTER);
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton botonActualizar = new JButton("Actualizar Inventario");
        JButton botonReabastecer = new JButton("Solicitar Reabastecimiento");
        panelBotones.add(botonActualizar);
        panelBotones.add(botonReabastecer);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tablaInventario), panelAlertas);
        splitPane.setDividerLocation(600);
        panelPrincipal.add(splitPane, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        botonActualizar.addActionListener(e -> {
            actualizarTablaInventario(modeloTabla, servicioInventario);
            actualizarAlertasInventario(areaAlertas, servicioInventario);
        });
        botonReabastecer.addActionListener(e -> {
            List<Ingrediente> ingredientesBajos = servicioInventario.obtenerIngredientesBajos();
            if (ingredientesBajos.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal, "No hay ingredientes que necesiten reabastecimiento urgente");
            } else {
                StringBuilder mensaje = new StringBuilder("Solicitud de reabastecimiento:\n\n");
                for (Ingrediente ing : ingredientesBajos) {
                    mensaje.append("• ").append(ing.getNombre()).append(" - ").append(ing.getCantidadDisponible()).append("/").append(ing.getCantidadMinima()).append(" ").append(ing.getUnidadMedida()).append("\n");
                }
                JOptionPane.showMessageDialog(panelPrincipal, mensaje.toString(), "Solicitud de Reabastecimiento", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return panelPrincipal;
    }

    private void actualizarTablaInventario(DefaultTableModel modeloTabla, ServicioInventario servicioInventario) {
        modeloTabla.setRowCount(0);
        for (Ingrediente ingrediente : servicioInventario.obtenerTodosIngredientes()) {
            String estado = ingrediente.necesitaReabastecer() ? "BAJO" : "OK";
            Object[] fila = { ingrediente.getId(), ingrediente.getNombre(), ingrediente.getCategoria(), ingrediente.getCantidadDisponible(), ingrediente.getCantidadMinima(), ingrediente.getUnidadMedida(), estado };
            modeloTabla.addRow(fila);
        }
    }

    private void actualizarAlertasInventario(JTextArea areaAlertas, ServicioInventario servicioInventario) {
        List<Ingrediente> ingredientesBajos = servicioInventario.obtenerIngredientesBajos();
        if (ingredientesBajos.isEmpty()) {
            areaAlertas.setText("No hay alertas de inventario.\nTodo está en orden.");
            areaAlertas.setForeground(new Color(0, 128, 0));
        } else {
            StringBuilder alertas = new StringBuilder("INGREDIENTES BAJOS:\n\n");
            for (Ingrediente ing : ingredientesBajos) {
                alertas.append("⚠ ").append(ing.getNombre()).append("\n");
                alertas.append("   Disponible: ").append(ing.getCantidadDisponible()).append(" ").append(ing.getUnidadMedida()).append("\n");
                alertas.append("   Mínimo: ").append(ing.getCantidadMinima()).append(" ").append(ing.getUnidadMedida()).append("\n\n");
            }
            areaAlertas.setText(alertas.toString());
            areaAlertas.setForeground(Color.RED);
        }
    }

    private JPanel crearPanelReportes() {
        return crearPanelPlaceholder("Reportes");
    }

    private JPanel crearPanelConfiguracion() {
        return crearPanelPlaceholder("Configuración");
    }

    private JPanel crearPanelTomarPedido(ServicioMesas servicioMesas, ServicioProductos servicioProductos, ServicioOrdenes servicioOrdenes) {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        final Orden[] ordenActual = {null};
        DefaultTableModel modeloTablaPedido = new DefaultTableModel(new String[]{"Producto", "Cantidad", "Precio", "Subtotal"}, 0);
        JPanel panelSuperior = new JPanel(new FlowLayout());
        panelSuperior.setBackground(Color.WHITE);
        JLabel lblMesa = new JLabel("Seleccionar Mesa:");
        JComboBox<Integer> comboMesas = new JComboBox<>();
        for (Mesa mesa : servicioMesas.obtenerMesasOcupadas()) {
            comboMesas.addItem(mesa.getNumero());
        }
        JButton btnCrearPedido = new JButton("Crear Orden para Mesa");
        panelSuperior.add(lblMesa);
        panelSuperior.add(comboMesas);
        panelSuperior.add(btnCrearPedido);
        JPanel panelMenu = new JPanel(new BorderLayout());
        panelMenu.setPreferredSize(new Dimension(400, 0));
        panelMenu.setBorder(BorderFactory.createTitledBorder("Menú"));
        JComboBox<String> comboCategorias = new JComboBox<>();
        for (String categoria : servicioProductos.obtenerCategorias()) {
            comboCategorias.addItem(categoria);
        }
        DefaultListModel<String> modeloProductos = new DefaultListModel<>();
        JList<String> listaProductos = new JList<>(modeloProductos);
        comboCategorias.addActionListener(e -> {
            modeloProductos.clear();
            String categoria = (String) comboCategorias.getSelectedItem();
            for (modelo.Producto producto : servicioProductos.obtenerProductosPorCategoria(categoria)) {
                modeloProductos.addElement(producto.getNombre() + " - S/" + producto.getPrecio());
            }
        });
        if (comboCategorias.getItemCount() > 0) {
            comboCategorias.setSelectedIndex(0);
        }
        panelMenu.add(comboCategorias, BorderLayout.NORTH);
        panelMenu.add(new JScrollPane(listaProductos), BorderLayout.CENTER);
        JPanel panelAgregar = new JPanel(new GridLayout(4, 2, 10, 10));
        panelAgregar.setBorder(BorderFactory.createTitledBorder("Agregar a la Orden"));
        panelAgregar.setPreferredSize(new Dimension(300, 0));
        JLabel lblCantidad = new JLabel("Cantidad:");
        JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        JLabel lblNotas = new JLabel("Notas:");
        JTextField txtNotas = new JTextField();
        JButton btnAgregar = new JButton("Agregar Producto");
        panelAgregar.add(lblCantidad);
        panelAgregar.add(spinnerCantidad);
        panelAgregar.add(lblNotas);
        panelAgregar.add(txtNotas);
        panelAgregar.add(new JLabel());
        panelAgregar.add(btnAgregar);
        JPanel panelPedido = new JPanel(new BorderLayout());
        panelPedido.setBorder(BorderFactory.createTitledBorder("Orden Actual"));
        JTable tablaPedido = new JTable(modeloTablaPedido);
        JLabel lblTotal = new JLabel("Total: S/0.00", JLabel.CENTER);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        JButton btnEnviarCocina = new JButton("Enviar a Cocina");
        btnEnviarCocina.setBackground(new Color(40, 167, 69));
        btnEnviarCocina.setForeground(Color.WHITE);
        panelPedido.add(new JScrollPane(tablaPedido), BorderLayout.CENTER);
        panelPedido.add(lblTotal, BorderLayout.SOUTH);
        JPanel panelInferior = new JPanel(new FlowLayout());
        panelInferior.add(btnEnviarCocina);
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(panelMenu, BorderLayout.WEST);
        panelCentral.add(panelAgregar, BorderLayout.CENTER);
        panelCentral.add(panelPedido, BorderLayout.EAST);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        btnCrearPedido.addActionListener(e -> {
            if (comboMesas.getItemCount() == 0) {
                JOptionPane.showMessageDialog(panelPrincipal, "No hay mesas ocupadas");
                return;
            }
            int numeroMesa = (Integer) comboMesas.getSelectedItem();
            Mesa mesaSeleccionada = servicioMesas.buscarMesaPorNumero(numeroMesa);
            Empleado meseroLogueado = VentanaPrincipal.this.usuario; 
            if (mesaSeleccionada == null) {
                 JOptionPane.showMessageDialog(panelPrincipal, "Error: No se encontró la mesa");
                 return;
            }
            ordenActual[0] = servicioOrdenes.crearOrden(mesaSeleccionada, meseroLogueado);
            modeloTablaPedido.setRowCount(0);
            lblTotal.setText("Total: S/0.00");
            JOptionPane.showMessageDialog(panelPrincipal, "Nueva orden creada para Mesa " + numeroMesa);
        });
        btnAgregar.addActionListener(e -> {
            if (ordenActual[0] == null) {
                JOptionPane.showMessageDialog(panelPrincipal, "Primero debe crear una orden");
                return;
            }
            int selectedIndex = listaProductos.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(panelPrincipal, "Seleccione un producto del menú");
                return;
            }
            String categoria = (String) comboCategorias.getSelectedItem();
            java.util.List<modelo.Producto> productos = servicioProductos.obtenerProductosPorCategoria(categoria);
            modelo.Producto productoSeleccionado = productos.get(selectedIndex);
            int cantidad = (Integer) spinnerCantidad.getValue();
            String notas = txtNotas.getText();
            modelo.ItemOrden item = new modelo.ItemOrden(productoSeleccionado, cantidad, notas);
            ordenActual[0].agregarItem(item);
            modeloTablaPedido.addRow(new Object[]{ productoSeleccionado.getNombre(), cantidad, "S/" + productoSeleccionado.getPrecio(), "S/" + item.getSubtotal() });
            lblTotal.setText("Total: S/" + ordenActual[0].getTotal());
            txtNotas.setText("");
            spinnerCantidad.setValue(1);
        });
        btnEnviarCocina.addActionListener(e -> {
            if (ordenActual[0] == null || ordenActual[0].getItems().isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal, "No hay orden para enviar");
                return;
            }
            ordenActual[0].setEstado(EstadoOrden.PENDIENTE);
            servicioOrdenes.actualizarOrden(ordenActual[0]); 
            JOptionPane.showMessageDialog(panelPrincipal, "Orden #" + ordenActual[0].getId() + " enviada a cocina\n" + "Mesa: " + ordenActual[0].getNumeroMesa() + "\n" + "Total: S/" + ordenActual[0].getTotal());
            ordenActual[0] = null;
            modeloTablaPedido.setRowCount(0);
            lblTotal.setText("Total: S/0.00");
        });
        return panelPrincipal;
    }

    private JPanel crearPanelGestionMesas(ServicioMesas servicioMesas) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel titulo = new JLabel("GESTIÓN DE MESAS", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        JPanel gridMesas = new JPanel(new GridLayout(2, 5, 10, 10));
        gridMesas.setBackground(Color.WHITE);
        gridMesas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (Mesa mesa : servicioMesas.obtenerTodasLasMesas()) {
            JButton btnMesa = new JButton("Mesa " + mesa.getNumero());
            btnMesa.setBackground(mesa.isOcupada() ? new Color(220, 53, 69) : new Color(40, 167, 69));
            btnMesa.setForeground(Color.WHITE);
            btnMesa.setFont(new Font("Arial", Font.BOLD, 14));
            btnMesa.addActionListener(e -> {
                if (mesa.isOcupada()) {
                    int respuesta = JOptionPane.showConfirmDialog(panel, "¿Liberar Mesa " + mesa.getNumero() + "?", "Liberar Mesa", JOptionPane.YES_NO_OPTION);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        servicioMesas.liberarMesa(mesa.getNumero());
                        btnMesa.setBackground(new Color(40, 167, 69));
                    }
                } else {
                    String comensalesStr = JOptionPane.showInputDialog(panel, "Número de comensales para Mesa " + mesa.getNumero() + ":");
                    try {
                        int comensales = Integer.parseInt(comensalesStr);
                        if (servicioMesas.ocuparMesa(mesa.getNumero(), comensales)) {
                            btnMesa.setBackground(new Color(220, 53, 69));
                        } else {
                            JOptionPane.showMessageDialog(panel, "No se puede ocupar la mesa (capacidad excedida o inválida)");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(panel, "Número inválido");
                    }
                }
            });
            gridMesas.add(btnMesa);
        }
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(gridMesas, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelOrdenesActivas(ServicioOrdenes servicioOrdenes) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel titulo = new JLabel("ÓRDENES LISTAS - ENTREGAR A CLIENTES", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        String[] columnas = {"ID", "Mesa", "Estado", "Total", "Hora"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tablaOrdenes = new JTable(modeloTabla);
        for (Orden orden : servicioOrdenes.obtenerOrdenesPorEstado(EstadoOrden.LISTA)) {
            Object[] fila = { orden.getId(), orden.getNumeroMesa(), orden.getEstadoString(), "S/" + orden.getTotal(), orden.getFechaHora() };
            modeloTabla.addRow(fila);
        }
        JButton btnEntregar = new JButton("Marcar como Entregado");
        btnEntregar.setBackground(new Color(40, 167, 69));
        btnEntregar.setForeground(Color.WHITE);
        btnEntregar.addActionListener(e -> {
            int fila = tablaOrdenes.getSelectedRow();
            if (fila != -1) {
                int idOrden = (int) modeloTabla.getValueAt(fila, 0);
                Orden orden = servicioOrdenes.buscarOrdenPorId(idOrden);
                if (orden != null) {
                    orden.setEstado(EstadoOrden.ENTREGADA);
                    servicioOrdenes.actualizarOrden(orden);
                    modeloTabla.removeRow(fila);
                    JOptionPane.showMessageDialog(panel, "Orden entregada al cliente");
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Seleccione una orden de la lista");
            }
        });
        JPanel panelSur = new JPanel();
        panelSur.add(btnEntregar);
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaOrdenes), BorderLayout.CENTER);
        panel.add(panelSur, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelVerMenu() {
        return crearPanelPlaceholder("Ver Menú");
    }
    private JPanel crearPanelRealizarPedido() {
        return crearPanelPlaceholder("Realizar Pedido");
    }
    private JPanel crearPanelEstadoPedido() {
        return crearPanelPlaceholder("Estado Pedido");
    }

    private JPanel crearPanelPlaceholder(String nombrePanel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel etiqueta = new JLabel(nombrePanel, JLabel.CENTER);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 24));
        etiqueta.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        panel.add(etiqueta, BorderLayout.CENTER);
        return panel;
    }

    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            new VentanaLogin().setVisible(true);
            this.dispose();
        }
    }
}