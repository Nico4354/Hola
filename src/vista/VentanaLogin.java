package vista;

// CAMBIO: Importamos Empleado
import modelo.Empleado;
// CAMBIO: Importamos ServicioEmpleados
import servicio.ServicioEmpleados;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaLogin extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoContraseña;
    private JButton botonLogin;
    // CAMBIO: Reemplazamos BaseDatosSimulada por ServicioEmpleados
    private ServicioEmpleados servicioEmpleados;

    public VentanaLogin() {
        // CAMBIO: Inicializamos el servicio de empleados
        servicioEmpleados = new ServicioEmpleados();
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("Sistema de Gestión - Restaurante Peruano 'Sabores del Inca'");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarComponentes() {
        // ... (Todo el código de inicializarComponentes se queda EXACTAMENTE IGUAL) ...
        
        // Panel principal con imagen de fondo
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    ImageIcon imagenFondo = new ImageIcon("recursos/imagenes/fondo_login.jpg");
                    g.drawImage(imagenFondo.getImage(), 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    // Si no hay imagen, usar color de fondo
                    g.setColor(new Color(70, 130, 180));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panelPrincipal.setLayout(new GridBagLayout());
        
        // Panel de login
        JPanel panelLogin = new JPanel();
        panelLogin.setLayout(new GridLayout(4, 1, 10, 10));
        panelLogin.setBackground(new Color(255, 255, 255, 200));
        panelLogin.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelLogin.setPreferredSize(new Dimension(300, 250));

        // Logo
        JLabel etiquetaLogo = new JLabel();
        try {
            ImageIcon logo = new ImageIcon("recursos/iconos/logo_restaurante.png");
            etiquetaLogo.setIcon(logo);
        } catch (Exception e) {
            etiquetaLogo.setText("RESTAURANTE");
            etiquetaLogo.setFont(new Font("Arial", Font.BOLD, 20));
        }
        etiquetaLogo.setHorizontalAlignment(JLabel.CENTER);

        // Campo usuario
        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelUsuario.setBackground(new Color(255, 255, 255, 0));
        
        JLabel iconoUsuario = new JLabel();
        try {
            ImageIcon iconoUser = new ImageIcon("recursos/iconos/icono_usuario.png");
            iconoUsuario.setIcon(iconoUser);
        } catch (Exception e) {
            iconoUsuario.setText("👤");
        }
        
        campoUsuario = new JTextField(15);
        campoUsuario.setBorder(BorderFactory.createTitledBorder("Usuario"));
        panelUsuario.add(iconoUsuario);
        panelUsuario.add(campoUsuario);

        // Campo contraseña
        JPanel panelContraseña = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelContraseña.setBackground(new Color(255, 255, 255, 0));
        
        JLabel iconoContraseña = new JLabel();
        try {
            ImageIcon iconoPass = new ImageIcon("recursos/iconos/icono_contraseña.png");
            iconoContraseña.setIcon(iconoPass);
        } catch (Exception e) {
            iconoContraseña.setText("🔒");
        }
        
        campoContraseña = new JPasswordField(15);
        campoContraseña.setBorder(BorderFactory.createTitledBorder("Contraseña"));
        panelContraseña.add(iconoContraseña);
        panelContraseña.add(campoContraseña);

        // Botón login
        botonLogin = new JButton("INGRESAR AL SISTEMA");
        botonLogin.setBackground(new Color(70, 130, 180));
        botonLogin.setForeground(Color.WHITE);
        botonLogin.setFont(new Font("Arial", Font.BOLD, 14));

        // Agregar componentes
        panelLogin.add(etiquetaLogo);
        panelLogin.add(panelUsuario);
        panelLogin.add(panelContraseña);
        panelLogin.add(botonLogin);

        // Posicionar panel de login
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 50, 50, 50);
        panelPrincipal.add(panelLogin, gbc);

        add(panelPrincipal, BorderLayout.CENTER);

        // Configurar acción del botón
        botonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
    }

    private void realizarLogin() {
        String usuario = campoUsuario.getText();
        String contraseña = new String(campoContraseña.getPassword());
        
        if (usuario.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // CAMBIO: Validamos contra ServicioEmpleados y recibimos un Empleado
        Empleado usuarioValido = servicioEmpleados.validarUsuario(usuario, contraseña);
        
        if (usuarioValido != null) {
            JOptionPane.showMessageDialog(this, 
                // CAMBIO: Usamos getNombre() de Empleado
                "¡Bienvenido " + usuarioValido.getNombre() + "!\nRol: " + usuarioValido.getRol(),
                "Login Exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Abrir menú principal
            abrirMenuPrincipal(usuarioValido);
            this.dispose(); // Cerrar ventana de login
            
        } else {
            JOptionPane.showMessageDialog(this, 
                "Usuario o contraseña incorrectos", 
                "Error de Login", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // CAMBIO: El método ahora recibe un Empleado
    private void abrirMenuPrincipal(Empleado empleado) {
        new VentanaPrincipal(empleado).setVisible(true);
        this.dispose();
    }
}