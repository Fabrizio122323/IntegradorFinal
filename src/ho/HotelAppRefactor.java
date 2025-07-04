// Archivo: HotelAppRefactor.java
package ho;





import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// === MODELOS ===
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
class Alojamiento {
    private final String cliente;
    private final Habitacion habitacion;
    private final boolean anticipado;
    private final Date entrada;
    private final Date salida;
    private final String hora;

    public Alojamiento(String cliente, Habitacion habitacion, boolean anticipado, Date entrada, Date salida, String hora) {
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.anticipado = anticipado;
        this.entrada = entrada;
        this.salida = salida;
        this.hora = hora;
    }

    public String getCliente() { return cliente; }
    public Date getFechaEntrada() { return entrada; }
    public Date getFechaSalida() { return salida; }
    public String getHora() { return hora; }
    public boolean isAnticipado() { return anticipado; }
    public Habitacion getHabitacion() { return habitacion; }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return cliente + "|" + habitacion.getNumero() + "|" + anticipado + "|" + sdf.format(entrada) + "|" + sdf.format(salida) + "|" + hora;
    }
}

class Servicio {
    private final String nombre;
    private final double costo;
    public Servicio(String nombre, double costo) {
        this.nombre = nombre;
        this.costo = costo;
    }
    public String getNombre() {
        return nombre;
    }
    public double getCosto() {
        return costo;
    }
    @Override
    public String toString() {
        return nombre + "|" + costo;
    }
}

class Habitacion {
    private final int id;
    private final String numero;
    private String estado;
    private final double precio;
    private final String tipo;

    public Habitacion(int id, String numero, String estado, double precio, String tipo) {
        this.id = id;
        this.numero = numero;
        this.estado = estado;
        this.precio = precio;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getPrecio() {
        return precio;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return numero + " (" + tipo + ")";
    }
}

// === DAO ===
interface DAO<T> {

    void guardar(T t);

    List<T> cargar();
}

class AlojamientoDAO implements DAO<Alojamiento> {

    private final File archivo = new File("alojamientos.txt");

    @Override
    public void guardar(Alojamiento alojamiento) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            Habitacion h = alojamiento.getHabitacion();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            String linea = alojamiento.getCliente() + "|" +
                    h.getNumero() + "|" +
                    h.getTipo() + "|" +
                    h.getPrecio() + "|" +
                    alojamiento.isAnticipado() + "|" +
                    sdf.format(alojamiento.getFechaEntrada()) + "|" +
                    sdf.format(alojamiento.getFechaSalida()) + "|" +
                    alojamiento.getHora();

            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error guardando alojamiento.");
        }
    }
    @Override
    public List<Alojamiento> cargar() {
        List<Alojamiento> lista = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");

                if (partes.length == 8) {
                    String cliente = partes[0];
                    String numHab = partes[1];
                    String tipo = partes[2];
                    double precio = Double.parseDouble(partes[3]);
                    boolean anticipado = Boolean.parseBoolean(partes[4]);
                    Date entrada = sdf.parse(partes[5]);
                    Date salida = sdf.parse(partes[6]);
                    String hora = partes[7];

                    Habitacion h = new Habitacion(0, numHab, "OCUPADO", precio, tipo);
                    lista.add(new Alojamiento(cliente, h, anticipado, entrada, salida, hora));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error cargando alojamientos.");
        }

        return lista;
    }
}

class ServicioDAO implements DAO<Servicio> {
    private final File archivo = new File("servicios.txt");
    public void guardar(Servicio servicio) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(servicio.toString());
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error guardando servicio.");
        }
    }
    public List<Servicio> cargar() {
        List<Servicio> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 2) {
                    String nombre = partes[0];
                    double costo = Double.parseDouble(partes[1]);
                    lista.add(new Servicio(nombre, costo));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error cargando servicios.");
        }
        return lista;
    }
}

class HabitacionDAO implements DAO<Habitacion> {
    private final File archivo = new File("habitaciones.txt");
    public void guardar(Habitacion habitacion) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(habitacion.getId() + "|" + habitacion.getNumero() + "|" + habitacion.getEstado() + "|" + habitacion.getPrecio() + "|" + habitacion.getTipo());
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error guardando habitación.");
        }
    }
    public List<Habitacion> cargar() {
        List<Habitacion> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 5) {
                    int id = Integer.parseInt(partes[0]);
                    String numero = partes[1];
                    String estado = partes[2];
                    double precio = Double.parseDouble(partes[3]);
                    String tipo = partes[4];
                    lista.add(new Habitacion(id, numero, estado, precio, tipo));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error cargando habitaciones.");
        }
        return lista;
    }
}

// === FORMULARIO HABITACION ACTUALIZADO ===
class FormularioHabitacion extends JFrame {
    private final JTextField txtNumero, txtPrecio, txtBuscar, txtDias;
    private final JComboBox<String> cboEstado = null, cboTipo, cboFiltroEstado, cboPago, cboServicio;
    private final JTable tabla;
    private final DefaultTableModel modelo;
    private final HabitacionDAO habitacionDAO = new HabitacionDAO();
    private final AlojamientoDAO alojamientoDAO = new AlojamientoDAO();
    private final ArrayList<Habitacion> listaHabitaciones = new ArrayList<>();
    private int contadorId = 1;

    public FormularioHabitacion() {
        setTitle("Gestión de Habitaciones");
        setSize(1000, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        JPanel panelRegistro = new JPanel();
        panelRegistro.setLayout(null);
        panelRegistro.setBounds(10, 10, 300, 430);
        panelRegistro.setBackground(Color.PINK);
        add(panelRegistro);

        panelRegistro.add(new JLabel("Tipo de Habitación:")).setBounds(10, 10, 150, 20);
        cboTipo = new JComboBox<>(new String[]{"INDIVIDUAL", "MATRIMONIAL", "FAMILIAR", "PRESIDENCIAL"});
        cboTipo.setBounds(150, 10, 130, 20);
        panelRegistro.add(cboTipo);

        panelRegistro.add(new JLabel("Nombre Completo:")).setBounds(10, 40, 130, 20);
        txtNumero = new JTextField();
        txtNumero.setBounds(150, 40, 130, 20);
        panelRegistro.add(txtNumero);

        panelRegistro.add(new JLabel("DNI/Carnet:")).setBounds(10, 70, 130, 20);
        txtBuscar = new JTextField();
        txtBuscar.setBounds(150, 70, 130, 20);
        panelRegistro.add(txtBuscar);

        panelRegistro.add(new JLabel("Precio Diario:")).setBounds(10, 100, 130, 20);
        txtPrecio = new JTextField();
        txtPrecio.setBounds(150, 100, 130, 20);
        txtPrecio.setEditable(false);
        panelRegistro.add(txtPrecio);

        panelRegistro.add(new JLabel("Días de Estancia:")).setBounds(10, 130, 130, 20);
        txtDias = new JTextField();
        txtDias.setBounds(150, 130, 130, 20);
        panelRegistro.add(txtDias);

        panelRegistro.add(new JLabel("Servicio:")).setBounds(10, 160, 130, 20);
        cboServicio = new JComboBox<>(new String[]{"NINGUNO", "ALMUERZO", "CENA", "BEBIDAS", "PAQUETE COMPLETO"});
        cboServicio.setBounds(150, 160, 130, 20);
        panelRegistro.add(cboServicio);

        panelRegistro.add(new JLabel("Método de Pago:")).setBounds(10, 190, 130, 20);
        cboPago = new JComboBox<>(new String[]{"EFECTIVO", "TARJETA"});
        cboPago.setBounds(150, 190, 130, 20);
        panelRegistro.add(cboPago);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(90, 230, 120, 25);
        panelRegistro.add(btnRegistrar);

        cboFiltroEstado = new JComboBox<>(new String[]{"DISPONIBLE", "OCUPADO"});
        cboFiltroEstado.setBounds(320, 10, 120, 20);
        add(cboFiltroEstado);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(450, 10, 80, 20);
        add(btnBuscar);

        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new Object[]{"ID", "Número", "Tipo", "Estado", "Precio", "Cliente", "DNI", "Celular", "Días", "Total", "Gestionar"});
        tabla = new JTable(modelo) {
            public boolean isCellEditable(int row, int column) {
                return column == 10;
            }
        };
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(320, 40, 650, 400);
        add(scroll);

        cboTipo.addActionListener(e -> actualizarPrecioPorTipo());

        btnRegistrar.addActionListener(e -> {
            try {
                String tipo = (String) cboTipo.getSelectedItem();
                String cliente = txtNumero.getText();
                String dni = txtBuscar.getText();
                double precio = Double.parseDouble(txtPrecio.getText());
                int dias = Integer.parseInt(txtDias.getText());
                String metodoPago = (String) cboPago.getSelectedItem();
                String servicio = (String) cboServicio.getSelectedItem();
                String numeroHab = "HAB" + String.format("%03d", contadorId);

                double precioServicio = switch (servicio) {
                    case "ALMUERZO" -> 10;
                    case "CENA" -> 12;
                    case "BEBIDAS" -> 14;
                    case "PAQUETE COMPLETO" -> 30;
                    default -> 0;
                };

                double total = (dias * precio) + (dias * precioServicio);
                double igv = total * 0.18;
                double totalConIGV = total + igv;

                Habitacion h = new Habitacion(contadorId++, numeroHab, "OCUPADO", precio, tipo);
                habitacionDAO.guardar(h);

                JButton btnGestionar = new JButton("Gestionar");
                btnGestionar.addActionListener(evt -> mostrarPanelGestion(cliente, dni, dias, tipo, servicio, precio, precioServicio));

                modelo.addRow(new Object[]{h.getId(), h.getNumero(), h.getTipo(), h.getEstado(), h.getPrecio(), cliente, dni, "987654321", dias, totalConIGV, btnGestionar});
                JOptionPane.showMessageDialog(this, "Usuario registrado correctamente. Total con IGV: S/." + totalConIGV);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar. Verifique los datos ingresados.");
            }
        });

        btnBuscar.addActionListener(e -> actualizarTablaHabitaciones((String) cboFiltroEstado.getSelectedItem()));
        cboFiltroEstado.addActionListener(e -> actualizarTablaHabitaciones((String) cboFiltroEstado.getSelectedItem()));
    }

    private void actualizarPrecioPorTipo() {
        String tipo = (String) cboTipo.getSelectedItem();
        switch (tipo) {
            case "INDIVIDUAL" -> txtPrecio.setText("80");
            case "MATRIMONIAL" -> txtPrecio.setText("120");
            case "FAMILIAR" -> txtPrecio.setText("150");
            case "PRESIDENCIAL" -> txtPrecio.setText("200");
        }
    }

    private void actualizarTablaHabitaciones(String estado) {
        modelo.setRowCount(0);
        List<Alojamiento> alojamientos = alojamientoDAO.cargar();

        for (Habitacion h : habitacionDAO.cargar()) {
            if (h.getEstado().equalsIgnoreCase(estado)) {
                if (estado.equals("OCUPADO")) {
                    Alojamiento encontrado = alojamientos.stream()
                            .filter(a -> a.getHabitacion().getNumero().equals(h.getNumero()))
                            .findFirst().orElse(null);

                    if (encontrado != null) {
                        long dias = (encontrado.getFechaSalida().getTime() - encontrado.getFechaEntrada().getTime()) / (1000 * 60 * 60 * 24);
                        JButton btnGestionar = new JButton("Gestionar");
                        btnGestionar.addActionListener(evt -> mostrarPanelGestion(encontrado.getCliente(), "DNI123", (int) dias, h.getTipo(), "", h.getPrecio(), 0));
                        double total = h.getPrecio() * dias;
                        double igv = total * 0.18;
                        double totalConIGV = total + igv;

                        modelo.addRow(new Object[]{
                                h.getId(), h.getNumero(), h.getTipo(), h.getEstado(), h.getPrecio(),
                                encontrado.getCliente(), "DNI123", "987654321", dias, totalConIGV, btnGestionar
                        });
                    }
                } else {
                    modelo.addRow(new Object[]{h.getId(), h.getNumero(), h.getTipo(), h.getEstado(), h.getPrecio(), "", "", "", "", "", ""});
                }
            }
        }
    }

    private void mostrarPanelGestion(String cliente, String dni, int dias, String tipo, String servicio, double precioHab, double precioServ) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Cliente: " + cliente));
        panel.add(new JLabel("DNI: " + dni));
        panel.add(new JLabel("Tipo: " + tipo));
        panel.add(new JLabel("Servicio: " + servicio));
        panel.add(new JLabel("Precio Habitación: S/." + precioHab));
        panel.add(new JLabel("Precio Servicio: S/." + precioServ));
        panel.add(new JLabel("Días: " + dias));

        JButton btnActualizar = new JButton("Actualizar");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        JPanel acciones = new JPanel();
        acciones.add(btnActualizar);
        acciones.add(btnGuardar);
        acciones.add(btnEliminar);
        panel.add(acciones);

        JOptionPane.showMessageDialog(this, panel, "Gestión de Estancia", JOptionPane.PLAIN_MESSAGE);
    }

    private void gestionarEstadia(Alojamiento a) {
        mostrarPanelGestion(a.getCliente(), "DNI123", 3, a.getHabitacion().getTipo(), "", a.getHabitacion().getPrecio(), 0);
    }
}

// === LOGIN ===
class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Login - Sistema Hotelero");
        setSize(325, 175);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lblMensaje = new JLabel("Hola Administrador", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblMensaje.setForeground(Color.BLUE);
        add(lblMensaje, BorderLayout.NORTH);

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnLogin.setBackground(new Color(59, 89, 182));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(e -> mostrarDialogoLogin());

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnLogin);
        add(panelBoton, BorderLayout.CENTER);

        setVisible(true);
    }

    private void mostrarDialogoLogin() {
        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Usuario:"));
        panel.add(user);
        panel.add(new JLabel("Contraseña:"));
        panel.add(pass);

        int result = JOptionPane.showConfirmDialog(this, panel, "Inicio de Sesión", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String u = user.getText().trim();
            String p = new String(pass.getPassword());
            if (u.equals("admin") && p.equals("1234")) {
                dispose();
                new HotelAppRefactor();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
            }
        }
    }
}

// === APP PRINCIPAL ===
public class HotelAppRefactor extends JFrame {

    private final HabitacionDAO habitacionDAO = new HabitacionDAO();
    private final AlojamientoDAO alojamientoDAO = new AlojamientoDAO();
    private final ServicioDAO servicioDAO = new ServicioDAO();
    private ArrayList<Habitacion> habitaciones = generarHabitaciones();

    private ArrayList<Habitacion> generarHabitaciones() {
        ArrayList<Habitacion> lista = new ArrayList<>();
        int id = 1;

        for (int i = 1; i <= 85; i++) {
            lista.add(new Habitacion(id++, "I" + i, "DISPONIBLE", 50.0, "INDIVIDUAL"));
        }
        for (int i = 1; i <= 45; i++) {
            lista.add(new Habitacion(id++, "M" + i, "DISPONIBLE", 80.0, "MATRIMONIAL"));
        }
        for (int i = 1; i <= 65; i++) {
            lista.add(new Habitacion(id++, "F" + i, "DISPONIBLE", 100.0, "FAMILIAR"));
        }
        for (int i = 1; i <= 35; i++) {
            lista.add(new Habitacion(id++, "P" + i, "DISPONIBLE", 150.0, "PRESIDENCIAL"));
        }

        return lista;
    }

    public HotelAppRefactor() {
        habitaciones = generarHabitaciones();
        setTitle("Sistema de Gestión Hotelera");
        setSize(950, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setJMenuBar(crearMenuBar());
        JLabel imagen = new JLabel();
        imagen.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("hotel.jpg"));
            Image img = icon.getImage().getScaledInstance(1000, 1000, Image.SCALE_SMOOTH);
            imagen.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imagen.setText("Bienvenido al Sistema Hotelero");
            imagen.setFont(new Font("Segoe UI", Font.BOLD, 28));
            imagen.setForeground(new Color(45, 45, 45));
        }
        add(imagen, BorderLayout.CENTER);
        setVisible(true);
    }

    private JMenuBar crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuGestion = new JMenu("Gestión");
        menuGestion.add(crearItem("Registrar Reservación", e -> vistaAlojamiento()));
        menuGestion.add(crearItem("Registrar Servicio", e -> vistaServicio()));
        menuGestion.add(crearItem("Registrar Habitaciones", e -> vistaHabitaciones()));

        JMenu menuRegistro = new JMenu("Facturación");
        menuRegistro.add(crearItem("Generar Factura", e -> vistaFacturacion()));

        JMenu menuReportes = new JMenu("Reportes");
        menuReportes.add(crearItem("Resumen General", e -> vistaResumen()));

        JMenu menuHerramientas = new JMenu("Herramientas");
        menuHerramientas.add(crearItem("Operaciones Básicas", e -> vistaOperaciones()));

        JMenu menuAyuda = new JMenu("Ayuda");
        menuAyuda.add(crearItem("Acerca del sistema", e -> JOptionPane.showMessageDialog(this,
                "Sistema de Gestión Hotelera\nVersión 1.0\nDesarrollado por sebastian casas")));
        menuAyuda.addSeparator();
        menuAyuda.add(crearItem("Salir", e -> System.exit(0)));

        menuBar.add(menuGestion);
        menuBar.add(menuRegistro);
        menuBar.add(menuReportes);
        menuBar.add(menuHerramientas);
        menuBar.add(menuAyuda);
        return menuBar;
    }

    private JMenuItem crearItem(String texto, ActionListener action) {
        JMenuItem item = new JMenuItem(texto);
        item.addActionListener(action);
        return item;
    }

    private void abrirFormularioServicio() {
        JFrame frame = new JFrame("Registro de Servicio");
        frame.setSize(400, 350);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        JLabel lblServicio = new JLabel("Servicio:");
        JCheckBox cbAlmuerzo = new JCheckBox("Almuerzo");
        JCheckBox cbBebida = new JCheckBox("Bebida");
        JCheckBox cbCompleto = new JCheckBox("Servicio Completo");

        JLabel lblCosto = new JLabel("Costo:");
        JTextField txtCosto = new JTextField();

        JButton btnGuardar = new JButton("Guardar");

        lblServicio.setBounds(20, 20, 150, 25);
        cbAlmuerzo.setBounds(40, 50, 150, 25);
        cbBebida.setBounds(40, 80, 150, 25);
        cbCompleto.setBounds(40, 110, 150, 25);
        lblCosto.setBounds(20, 150, 150, 25);
        txtCosto.setBounds(80, 150, 150, 25);
        btnGuardar.setBounds(120, 200, 120, 30);

        frame.add(lblServicio);
        frame.add(cbAlmuerzo);
        frame.add(cbBebida);
        frame.add(cbCompleto);
        frame.add(lblCosto);
        frame.add(txtCosto);
        frame.add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            double costo;
            try {
                costo = Double.parseDouble(txtCosto.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Costo inválido.");
                return;
            }

            ServicioDAO dao = new ServicioDAO();

            if (cbAlmuerzo.isSelected()) {
                dao.guardar(new Servicio("Almuerzo", costo));
            }
            if (cbBebida.isSelected()) {
                dao.guardar(new Servicio("Bebida", costo));
            }
            if (cbCompleto.isSelected()) {
                dao.guardar(new Servicio("Completo", costo));
            }

            JOptionPane.showMessageDialog(frame, "Servicios registrados correctamente.");
            frame.dispose();
        });

        frame.setVisible(true);
    }

    private void vistaAlojamiento() {
        JDialog dialog = new JDialog(this, "Registro Reservación", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componentes
        JTextField txtCliente = new JTextField();
        JComboBox<String> cboTipo = new JComboBox<>(new String[]{"INDIVIDUAL", "MATRIMONIAL", "FAMILIAR", "PRESIDENCIAL"});
        JComboBox<String> cboHabitacion = new JComboBox<>();
        JCheckBox chkAnticipado = new JCheckBox("Reservado con anticipación");

        JSpinner spnEntrada = new JSpinner(new SpinnerDateModel());
        spnEntrada.setEditor(new JSpinner.DateEditor(spnEntrada, "dd/MM/yyyy"));

        JSpinner spnSalida = new JSpinner(new SpinnerDateModel());
        spnSalida.setEditor(new JSpinner.DateEditor(spnSalida, "dd/MM/yyyy"));

        JSpinner spnHora = new JSpinner(new SpinnerDateModel());
        spnHora.setEditor(new JSpinner.DateEditor(spnHora, "HH:mm"));

        JTextField txtDisponibles = new JTextField("Disponibles: 0");
        txtDisponibles.setEditable(false);

        JTextField txtPrecioDia = new JTextField();
        txtPrecioDia.setEditable(false);

        JTextField txtPrecioTotal = new JTextField();
        txtPrecioTotal.setEditable(false);

        JComboBox<String> cboPago = new JComboBox<>(new String[]{"Tarjeta", "Efectivo"});

        JButton btnGuardar = new JButton("Guardar");

        // Layout
        int y = 0;
        gbc.gridx = 0;
        gbc.gridy = y;
        dialog.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtCliente, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Tipo de Habitación:"), gbc);
        gbc.gridx = 1;
        dialog.add(cboTipo, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Habitación disponible:"), gbc);
        gbc.gridx = 1;
        dialog.add(cboHabitacion, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(chkAnticipado, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Fecha Entrada:"), gbc);
        gbc.gridx = 1;
        dialog.add(spnEntrada, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Fecha Salida:"), gbc);
        gbc.gridx = 1;
        dialog.add(spnSalida, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Hora de reserva:"), gbc);
        gbc.gridx = 1;
        dialog.add(spnHora, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Habitaciones disponibles:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtDisponibles, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Precio por Día:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtPrecioDia, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Precio Total (con IGV):"), gbc);
        gbc.gridx = 1;
        dialog.add(txtPrecioTotal, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Método de Pago:"), gbc);
        gbc.gridx = 1;
        dialog.add(cboPago, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(btnGuardar, gbc);

        // Eventos
        cboTipo.addActionListener(e -> {
            String tipoSeleccionado = (String) cboTipo.getSelectedItem();
            cboHabitacion.removeAllItems();
            int disponibles = 0;
            for (Habitacion h : habitaciones) {
                if (h.getTipo().equalsIgnoreCase(tipoSeleccionado) && h.getEstado().equalsIgnoreCase("DISPONIBLE")) {
                    cboHabitacion.addItem(h.getNumero());
                    disponibles++;
                }
            }
            txtDisponibles.setText("Disponibles: " + disponibles);
            actualizarPrecios(cboHabitacion, spnEntrada, spnSalida, txtPrecioDia, txtPrecioTotal);
        });

        cboHabitacion.addActionListener(e -> actualizarPrecios(cboHabitacion, spnEntrada, spnSalida, txtPrecioDia, txtPrecioTotal));
        spnEntrada.addChangeListener(e -> actualizarPrecios(cboHabitacion, spnEntrada, spnSalida, txtPrecioDia, txtPrecioTotal));
        spnSalida.addChangeListener(e -> actualizarPrecios(cboHabitacion, spnEntrada, spnSalida, txtPrecioDia, txtPrecioTotal));

        btnGuardar.addActionListener(e -> {
            String cliente = txtCliente.getText().trim();
            String tipo = (String) cboTipo.getSelectedItem();
            String numeroHab = (String) cboHabitacion.getSelectedItem();

            if (cliente.isEmpty() || numeroHab == null) {
                JOptionPane.showMessageDialog(dialog, "Debe completar todos los campos.");
                return;
            }

            Habitacion habSeleccionada = null;
            for (Habitacion h : habitaciones) {
                if (h.getNumero().equals(numeroHab)) {
                    habSeleccionada = h;
                    break;
                }
            }

            if (habSeleccionada == null) {
                JOptionPane.showMessageDialog(dialog, "Habitación no encontrada.");
                return;
            }

            Date entrada = (Date) spnEntrada.getValue();
            Date salida = (Date) spnSalida.getValue();
            String hora = new SimpleDateFormat("HH:mm").format((Date) spnHora.getValue());

            Alojamiento alojamiento = new Alojamiento(cliente, habSeleccionada, chkAnticipado.isSelected(), entrada, salida, hora);
            alojamientoDAO.guardar(alojamiento);
            habSeleccionada.setEstado("OCUPADO");

            JOptionPane.showMessageDialog(dialog, "Reservación Realizada \nMétodo de pago: " + cboPago.getSelectedItem());
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    private void actualizarPrecios(JComboBox<String> cboHabitacion, JSpinner spnEntrada, JSpinner spnSalida, JTextField txtPrecioDia, JTextField txtPrecioTotal) {
        String numeroHab = (String) cboHabitacion.getSelectedItem();
        if (numeroHab == null) {
            return;
        }

        Habitacion habSeleccionada = null;
        for (Habitacion h : habitaciones) {
            if (h.getNumero().equals(numeroHab)) {
                habSeleccionada = h;
                break;
            }
        }
        if (habSeleccionada == null) {
            return;
        }

        Date entrada = (Date) spnEntrada.getValue();
        Date salida = (Date) spnSalida.getValue();
        long dias = (salida.getTime() - entrada.getTime()) / (1000 * 60 * 60 * 24);
        if (dias <= 0) {
            dias = 1;
        }

        double precioDia = habSeleccionada.getPrecio();
        double subtotal = precioDia * dias;
        double totalConIGV = subtotal * 1.18;

        txtPrecioDia.setText(String.format("%.2f", precioDia));
        txtPrecioTotal.setText(String.format("%.2f", totalConIGV));
    }

    private void vistaServicio() {
        JTextField nombre = new JTextField();
        JTextField costo = new JTextField();
        JCheckBox chkAlmuerzo = new JCheckBox("Almuerzo");
        JCheckBox chkBebida = new JCheckBox("Bebidas");
        JCheckBox chkTodo = new JCheckBox("Todo completo");

        JButton guardar = new JButton("Guardar");
        guardar.addActionListener(e -> {
            try {
                double c = Double.parseDouble(costo.getText());
                StringBuilder desc = new StringBuilder(nombre.getText());
                if (chkAlmuerzo.isSelected()) {
                    desc.append(" + Almuerzo");
                }
                if (chkBebida.isSelected()) {
                    desc.append(" + Bebidas");
                }
                if (chkTodo.isSelected()) {
                    desc.append(" + Todo Completo");
                }
                servicioDAO.guardar(new Servicio(desc.toString(), c));
                JOptionPane.showMessageDialog(this, "Servicio guardado.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Costo inválido.");
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Nombre del servicio:"));
        panel.add(nombre);
        panel.add(new JLabel("Costo ($):"));
        panel.add(costo);
        panel.add(chkAlmuerzo);
        panel.add(chkBebida);
        panel.add(chkTodo);
        panel.add(guardar);

        JOptionPane.showMessageDialog(this, panel, "Registro de Servicio", JOptionPane.PLAIN_MESSAGE);
    }

    private void vistaFacturacion() {
        JDialog dialog = new JDialog(this, "Factura", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        StringBuilder sb = new StringBuilder();
        sb.append("===== FACTURA =====\n\n");

        List<Alojamiento> alojamientos = alojamientoDAO.cargar();

        for (Alojamiento a : alojamientos) {
            Habitacion hab = a.getHabitacion();
            String tipo = hab.getTipo();
            double precioPorDia = hab.getPrecio();

            long dias = (a.getFechaSalida().getTime() - a.getFechaEntrada().getTime()) / (1000 * 60 * 60 * 24);
            if (dias <= 0) {
                dias = 1;
            }

            double subtotal = precioPorDia * dias;
            double totalConIGV = subtotal * 1.18;

            sb.append("Cliente: ").append(a.getCliente()).append("\n");
            sb.append("Tipo Habitación: ").append(tipo).append("\n");
            sb.append("Número Habitación: ").append(hab.getNumero()).append("\n");
            sb.append("Fecha Entrada: ").append(new SimpleDateFormat("dd/MM/yyyy").format(a.getFechaEntrada())).append("\n");
            sb.append("Fecha Salida: ").append(new SimpleDateFormat("dd/MM/yyyy").format(a.getFechaSalida())).append("\n");
            sb.append("Hora Reserva: ").append(a.getHora()).append("\n");
            sb.append("Reservado anticipadamente: ").append(a.isAnticipado() ? "Sí" : "No").append("\n");
            sb.append("Precio por noche: $").append(String.format("%.2f", precioPorDia)).append("\n");
            sb.append("Cantidad de noches: ").append(dias).append("\n");
            sb.append("Subtotal: $").append(String.format("%.2f", subtotal)).append("\n");
            sb.append("Total con IGV (18%): $").append(String.format("%.2f", totalConIGV)).append("\n");
            sb.append("------------------------------\n");
        }

        area.setText(sb.toString());
        dialog.add(new JScrollPane(area), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void vistaOperaciones() {
        JTextField a = new JTextField();
        JTextField b = new JTextField();
        JButton calcular = new JButton("Calcular");

        calcular.addActionListener(e -> {
            try {
                double x = Double.parseDouble(a.getText());
                double y = Double.parseDouble(b.getText());
                String result = String.format("""
                        Suma: %.2f
                        Resta: %.2f
                        Multiplicación: %.2f
                        División: %s
                    """, x + y, x - y, x * y, y != 0 ? String.format("%.2f", x / y) : "Indefinido");
                JOptionPane.showMessageDialog(this, result);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese números válidos.");
            }
        });

        mostrarFormulario("Cálculos Básicos", new JComponent[]{
            new JLabel("Número A:"), a,
            new JLabel("Número B:"), b
        }, calcular);
    }

    private void vistaResumen() {
        JTextArea area = new JTextArea(20, 60);
        area.setEditable(false);
        area.append("=== ALOJAMIENTOS ===\n");
        alojamientoDAO.cargar().forEach(a -> area.append(a.toString() + "\n"));
        area.append("\n=== SERVICIOS ===\n");
        servicioDAO.cargar().forEach(s -> area.append(s.toString() + "\n"));

        JScrollPane scroll = new JScrollPane(area);
        JOptionPane.showMessageDialog(this, scroll, "Resumen General", JOptionPane.INFORMATION_MESSAGE);
    }

    private void vistaHabitaciones() {
        FormularioHabitacion form = new FormularioHabitacion();
        form.setVisible(true);
    }

    private void mostrarFormulario(String titulo, JComponent[] campos, JButton boton) {
        JPanel panel = new JPanel(new GridLayout(campos.length / 2 + 2, 2, 10, 10));
        for (JComponent comp : campos) {
            panel.add(comp);
        }
        panel.add(new JLabel());
        panel.add(boton);
        JOptionPane.showMessageDialog(this, panel, titulo, JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}