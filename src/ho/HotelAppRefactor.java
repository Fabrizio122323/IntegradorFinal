package ho;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.toedter.calendar.JDateChooser;
import javax.swing.Timer;

// === MODELOS ===
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.swing.table.TableCellRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import javax.swing.table.DefaultTableCellRenderer;

class Alojamiento {

    private String cliente;
    private final Habitacion habitacion;
    private final boolean anticipado;
    private final Date entrada;
    private final Date salida;
    private final String hora;
    private String dni;
    private String celular;
    private String servicio;
    private int diasEstadia;

    public Alojamiento(String cliente, Habitacion habitacion, boolean anticipado, Date entrada, Date salida, String hora) {
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.anticipado = anticipado;
        this.entrada = entrada;
        this.salida = salida;
        this.hora = hora;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public int getDiasEstadia() {
        return diasEstadia;
    }

    public void setDiasEstadia(int dias) {
        this.diasEstadia = dias;
    }

    public Date getFechaEntrada() {
        return entrada;
    }

    public Date getFechaSalida() {
        return salida;
    }

    public String getHora() {
        return hora;
    }

    public boolean isAnticipado() {
        return anticipado;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return cliente + "|" + habitacion.getNumero() + "|" + anticipado + "|"
                + sdf.format(entrada) + "|" + sdf.format(salida) + "|" + hora + "|"
                + dni + "|" + celular + "|" + servicio + "|" + diasEstadia;
    }
}

class Servicio {

    private int id;
    private String nombre;
    private double precio;

    // Constructor completo (usado al cargar desde archivo)
    public Servicio(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    // Constructor corto (usado cuando se genera ID después)
    public Servicio(String nombre, double precio) {
        this.id = 0; // o puedes implementar lógica para generar ID único
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return nombre + " ($" + precio + ")";
    }
}

class Habitacion {

    private int id;
    private String numero;
    private String estado;
    private double precio;
    private String tipo;

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

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return numero + " (" + tipo + ")";
    }
}

interface DAO<T> {

    void guardar(T obj);

    List<T> cargar();

    void actualizar(T obj);

    void eliminar(T obj);
}

class AlojamientoDAO {

    private final File archivo = new File("alojamientos.txt");

    public void guardar(Alojamiento alojamiento) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            Habitacion h = alojamiento.getHabitacion();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            String linea = alojamiento.getCliente() + "|"
                    + h.getId() + "|"
                    + h.getNumero() + "|"
                    + h.getTipo() + "|"
                    + h.getPrecio() + "|"
                    + alojamiento.isAnticipado() + "|"
                    + sdf.format(alojamiento.getFechaEntrada()) + "|"
                    + sdf.format(alojamiento.getFechaSalida()) + "|"
                    + alojamiento.getHora() + "|"
                    + alojamiento.getDni() + "|"
                    + alojamiento.getCelular() + "|"
                    + alojamiento.getDiasEstadia() + "|"
                    + alojamiento.getServicio();

            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error guardando alojamiento.");
        }
    }

    public List<Alojamiento> cargar() {
        List<Alojamiento> lista = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");

                if (partes.length >= 13) {
                    String cliente = partes[0];
                    int id = Integer.parseInt(partes[1]);
                    String numHab = partes[2];
                    String tipo = partes[3];
                    double precio = Double.parseDouble(partes[4]);
                    boolean anticipado = Boolean.parseBoolean(partes[5]);
                    Date entrada = sdf.parse(partes[6]);
                    Date salida = sdf.parse(partes[7]);
                    String hora = partes[8];
                    String dni = partes[9];
                    String celular = partes[10];
                    int diasEstadia = Integer.parseInt(partes[11]);
                    String servicio = partes[12];

                    Habitacion h = new Habitacion(id, numHab, "OCUPADO", precio, tipo);
                    Alojamiento a = new Alojamiento(cliente, h, anticipado, entrada, salida, hora);
                    a.setDni(dni);
                    a.setCelular(celular);
                    a.setDiasEstadia(diasEstadia);
                    a.setServicio(servicio);
                    lista.add(a);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error cargando alojamientos.");
        }

        return lista;
    }

    public void actualizar(Alojamiento a) {
        List<Alojamiento> lista = cargar();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (Alojamiento alo : lista) {
                if (alo.getHabitacion().getId() == a.getHabitacion().getId()) {
                    alo = a;
                }
                Habitacion h = alo.getHabitacion();
                String linea = alo.getCliente() + "|"
                        + h.getId() + "|"
                        + h.getNumero() + "|"
                        + h.getTipo() + "|"
                        + h.getPrecio() + "|"
                        + alo.isAnticipado() + "|"
                        + sdf.format(alo.getFechaEntrada()) + "|"
                        + sdf.format(alo.getFechaSalida()) + "|"
                        + alo.getHora() + "|"
                        + alo.getDni() + "|"
                        + alo.getCelular() + "|"
                        + alo.getDiasEstadia() + "|"
                        + alo.getServicio();

                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error actualizando alojamiento.");
        }
    }

    public void eliminar(Alojamiento a) {
        List<Alojamiento> lista = cargar();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (Alojamiento alo : lista) {
                if (alo.getHabitacion().getId() != a.getHabitacion().getId()) {
                    Habitacion h = alo.getHabitacion();
                    String linea = alo.getCliente() + "|"
                            + h.getId() + "|"
                            + h.getNumero() + "|"
                            + h.getTipo() + "|"
                            + h.getPrecio() + "|"
                            + alo.isAnticipado() + "|"
                            + sdf.format(alo.getFechaEntrada()) + "|"
                            + sdf.format(alo.getFechaSalida()) + "|"
                            + alo.getHora() + "|"
                            + alo.getDni() + "|"
                            + alo.getCelular() + "|"
                            + alo.getDiasEstadia() + "|"
                            + alo.getServicio();

                    bw.write(linea);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error eliminando alojamiento.");
        }
    }

    public Alojamiento obtenerPorHabitacionId(int id) {
        for (Alojamiento a : cargar()) {
            if (a.getHabitacion().getId() == id) {
                return a;
            }
        }
        return null;
    }
}

class ServicioDAO implements DAO<Servicio> {

    private final File archivo = new File("servicios.txt");

    @Override
    public void guardar(Servicio servicio) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(servicio.getId() + "|" + servicio.getNombre() + "|" + servicio.getPrecio());
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error guardando servicio.");
        }
    }

    @Override
    public List<Servicio> cargar() {
        List<Servicio> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 3) {
                    int id = Integer.parseInt(partes[0]);
                    String nombre = partes[1];
                    double precio = Double.parseDouble(partes[2]);
                    lista.add(new Servicio(id, nombre, precio));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error cargando servicios.");
        }
        return lista;
    }

    @Override
    public void eliminar(Servicio servicio) {
        List<Servicio> lista = cargar();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Servicio s : lista) {
                if (s.getId() != servicio.getId()) {
                    bw.write(s.getId() + "|" + s.getNombre() + "|" + s.getPrecio());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error eliminando servicio.");
        }
    }

    @Override
    public void actualizar(Servicio servicioActualizado) {
        List<Servicio> servicios = cargar();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Servicio s : servicios) {
                if (s.getId() == servicioActualizado.getId()) {
                    s = servicioActualizado;
                }
                bw.write(s.getId() + "|" + s.getNombre() + "|" + s.getPrecio());
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error actualizando servicio.");
        }
    }
}

class HabitacionDAO implements DAO<Habitacion> {

    private final File archivo = new File("habitaciones.txt");

    @Override
    public void guardar(Habitacion habitacion) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(habitacion.getId() + "|" + habitacion.getNumero() + "|" + habitacion.getEstado() + "|" + habitacion.getPrecio() + "|" + habitacion.getTipo());
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error guardando habitación.");
        }
    }

@Override
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

        // ⚠️ Solo genera las habitaciones si el archivo está completamente vacío
        if (lista.isEmpty()) {
            List<Habitacion> nuevas = new ArrayList<>();
            int id = 1;

            // 85 individuales
            for (int i = 1; i <= 85; i++) {
                nuevas.add(new Habitacion(id++, "IND" + String.format("%03d", i), "DISPONIBLE", 80, "INDIVIDUAL"));
            }

            // 45 matrimoniales
            for (int i = 1; i <= 45; i++) {
                nuevas.add(new Habitacion(id++, "MAT" + String.format("%03d", i), "DISPONIBLE", 120, "MATRIMONIAL"));
            }

            // 65 familiares
            for (int i = 1; i <= 65; i++) {
                nuevas.add(new Habitacion(id++, "FAM" + String.format("%03d", i), "DISPONIBLE", 150, "FAMILIAR"));
            }

            // 35 presidenciales
            for (int i = 1; i <= 35; i++) {
                nuevas.add(new Habitacion(id++, "PRE" + String.format("%03d", i), "DISPONIBLE", 200, "PRESIDENCIAL"));
            }

            for (Habitacion h : nuevas) {
                guardar(h);
            }

            return nuevas;
        }

        return lista;
    }

    @Override
    public void eliminar(Habitacion habitacion) {
        List<Habitacion> habitaciones = cargar();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Habitacion h : habitaciones) {
                if (h.getId() != habitacion.getId()) {
                    bw.write(h.getId() + "|" + h.getNumero() + "|" + h.getEstado() + "|" + h.getPrecio() + "|" + h.getTipo());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error eliminando habitación.");
        }
    }

    public void actualizar(Habitacion habitacionActualizada) {
        List<Habitacion> habitaciones = cargar();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Habitacion h : habitaciones) {
                if (h.getId() == habitacionActualizada.getId()) {
                    h = habitacionActualizada;
                }
                bw.write(h.getId() + "|" + h.getNumero() + "|" + h.getEstado() + "|" + h.getPrecio() + "|" + h.getTipo());
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error actualizando habitación.");
        }
    }
}
    
// === FORMULARIO HABITACION ACTUALIZADO ===
class FormularioHabitacion extends JFrame {
    private JPanel panelHabitaciones;
    private JComboBox<String> filtroTipo;
    private final HabitacionDAO habitacionDAO = new HabitacionDAO();
    private final AlojamientoDAO alojamientoDAO = new AlojamientoDAO();

    public FormularioHabitacion() {
        setTitle("Gestión Visual de Habitaciones");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        panelSuperior.add(new JLabel("Filtrar por tipo:"));
        filtroTipo = new JComboBox<>(new String[]{"TODOS", "INDIVIDUAL", "MATRIMONIAL", "FAMILIAR", "PRESIDENCIAL"});
        filtroTipo.addActionListener(e -> cargarBotonesHabitacion());
        panelSuperior.add(filtroTipo);
        add(panelSuperior, BorderLayout.NORTH);

        panelHabitaciones = new JPanel(new GridLayout(0, 5, 15, 15));
        panelHabitaciones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inicializarHabitacionesSiNoExisten();
        cargarBotonesHabitacion();

        JScrollPane scroll = new JScrollPane(panelHabitaciones);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private void cargarBotonesHabitacion() {
        panelHabitaciones.removeAll();
        List<Habitacion> habitaciones = habitacionDAO.cargar();
        String filtro = (String) filtroTipo.getSelectedItem();

        for (Habitacion h : habitaciones) {
            if (!"TODOS".equals(filtro) && !h.getTipo().equalsIgnoreCase(filtro)) {
                continue;
            }

            String label = String.format("<html><center><b>Habitación %s</b><br><span style='font-size:16pt;'>%s</span><br><span style='color:gray;'>Precio x día:</span><br><b>S/. %.2f</b></center></html>",
                    h.getTipo().substring(0, 1).toUpperCase() + h.getTipo().substring(1).toLowerCase(),
                    h.getNumero().replace("HAB", ""),
                    h.getPrecio());

            JButton btn = new JButton(label);
            btn.setPreferredSize(new Dimension(160, 120));
            btn.setFont(new Font("Arial", Font.PLAIN, 12));

            switch (h.getEstado()) {
                case "DISPONIBLE" ->
                    btn.setBackground(new Color(76, 175, 80));
                case "OCUPADO" ->
                    btn.setBackground(new Color(244, 67, 54));
                default ->
                    btn.setBackground(Color.LIGHT_GRAY);
            }

            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setOpaque(true);

            btn.addActionListener(e -> {
                if (h.getEstado().equals("DISPONIBLE")) {
                    mostrarFormularioRegistro(h);
                } else if (h.getEstado().equals("OCUPADO")) {
                    Alojamiento a = alojamientoDAO.obtenerPorHabitacionId(h.getId());
                    if (a != null) {
                        mostrarPanelGestion(a);
                    }
                }
            });

            panelHabitaciones.add(btn);
        }

        panelHabitaciones.revalidate();
        panelHabitaciones.repaint();
    }

    private void inicializarHabitacionesSiNoExisten() {
        List<Habitacion> existentes = habitacionDAO.cargar();

        Map<String, Integer> conteoPorTipo = new HashMap<>();
        for (Habitacion h : existentes) {
            conteoPorTipo.put(h.getTipo(), conteoPorTipo.getOrDefault(h.getTipo(), 0) + 1);
        }

        String[] tipos = {"INDIVIDUAL", "MATRIMONIAL", "FAMILIAR", "PRESIDENCIAL"};
        double[] precios = {80, 120, 150, 200};
        int[] pisos = {1, 2, 3, 4};

        int id = existentes.stream().mapToInt(Habitacion::getId).max().orElse(0) + 1;

        for (int i = 0; i < tipos.length; i++) {
            String tipo = tipos[i];
            double precio = precios[i];
            int piso = pisos[i];

            int existentesPorTipo = conteoPorTipo.getOrDefault(tipo, 0);

            for (int j = existentesPorTipo + 1; j <= 50; j++) {
                String numero = String.format("%d%02d", piso, j); // Ej: 101, 102...
                Habitacion h = new Habitacion(id++, "HAB" + numero, "DISPONIBLE", precio, tipo);
                habitacionDAO.guardar(h);
            }
        }
    }

    private void mostrarFormularioRegistro(Habitacion habitacion) {
        JTextField txtCliente = new JTextField();
        JTextField txtDni = new JTextField();
        JTextField txtDias = new JTextField();
        JComboBox<String> cboServicio = new JComboBox<>(new String[]{"NINGUNO", "ALMUERZO", "CENA", "BEBIDAS", "PAQUETE COMPLETO"});
        JComboBox<String> cboPago = new JComboBox<>(new String[]{"EFECTIVO", "TARJETA"});
        JLabel lblTotal = new JLabel("Total con IGV: S/. 0.00");

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Cliente:"));
        panel.add(txtCliente);
        panel.add(new JLabel("DNI:"));
        panel.add(txtDni);
        panel.add(new JLabel("Días:"));
        panel.add(txtDias);
        panel.add(new JLabel("Servicio:"));
        panel.add(cboServicio);
        panel.add(new JLabel("Pago:"));
        panel.add(cboPago);
        panel.add(new JLabel(""));
        panel.add(lblTotal);

        txtDias.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                actualizarTotal();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                actualizarTotal();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                actualizarTotal();
            }

            private void actualizarTotal() {
                try {
                    int dias = Integer.parseInt(txtDias.getText());
                    double precioBase = habitacion.getPrecio();
                    double precioServicio = switch ((String) cboServicio.getSelectedItem()) {
                        case "ALMUERZO" ->
                            10;
                        case "CENA" ->
                            12;
                        case "BEBIDAS" ->
                            14;
                        case "PAQUETE COMPLETO" ->
                            30;
                        default ->
                            0;
                    };
                    double total = (precioBase + precioServicio) * dias * 1.18;
                    lblTotal.setText("Total con IGV: S/. " + String.format("%.2f", total));
                } catch (NumberFormatException ex) {
                    lblTotal.setText("Total con IGV: S/. 0.00");
                }
            }
        });

        int option = JOptionPane.showConfirmDialog(this, panel, "Registrar Alojamiento", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int dias = Integer.parseInt(txtDias.getText());
                double precioBase = habitacion.getPrecio();
                double precioServicio = switch ((String) cboServicio.getSelectedItem()) {
                    case "ALMUERZO" ->
                        10;
                    case "CENA" ->
                        12;
                    case "BEBIDAS" ->
                        14;
                    case "PAQUETE COMPLETO" ->
                        30;
                    default ->
                        0;
                };
                double total = (precioBase + precioServicio) * dias;
                double totalIGV = total * 1.18;

                Alojamiento a = new Alojamiento(txtCliente.getText(), habitacion, true, new Date(), new Date(), "00:00");
                a.setDni(txtDni.getText());
                a.setCelular("987654321");
                a.setServicio((String) cboServicio.getSelectedItem());
                a.setDiasEstadia(dias);

                habitacion.setEstado("OCUPADO");
                alojamientoDAO.guardar(a);
                habitacionDAO.actualizar(habitacion);
                cargarBotonesHabitacion();

                JOptionPane.showMessageDialog(this, "Registro exitoso.\nTotal con IGV: S/. " + String.format("%.2f", totalIGV));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage());
            }
        }
    }

    private void mostrarPanelGestion(Alojamiento a) {
        Habitacion habitacion = a.getHabitacion();
        JTextField txtCliente = new JTextField(a.getCliente());
        JTextField txtDni = new JTextField(a.getDni());
        JTextField txtDias = new JTextField(String.valueOf(a.getDiasEstadia()));
        JComboBox<String> cboServicio = new JComboBox<>(new String[]{"NINGUNO", "ALMUERZO", "CENA", "BEBIDAS", "PAQUETE COMPLETO"});
        cboServicio.setSelectedItem(a.getServicio());

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Cliente:"));
        panel.add(txtCliente);
        panel.add(new JLabel("DNI:"));
        panel.add(txtDni);
        panel.add(new JLabel("Días:"));
        panel.add(txtDias);
        panel.add(new JLabel("Servicio:"));
        panel.add(cboServicio);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        panel.add(btnGuardar);
        panel.add(btnEliminar);

        JDialog dialog = new JDialog(this, "Gestión", true);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);

        btnGuardar.addActionListener(e -> {
            try {
                a.setCliente(txtCliente.getText());
                a.setDni(txtDni.getText());
                a.setServicio((String) cboServicio.getSelectedItem());
                a.setDiasEstadia(Integer.parseInt(txtDias.getText()));
                alojamientoDAO.actualizar(a);
                dialog.dispose();
                cargarBotonesHabitacion();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        btnEliminar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog, "¿Eliminar alojamiento?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                alojamientoDAO.eliminar(a);
                habitacion.setEstado("DISPONIBLE");
                habitacionDAO.actualizar(habitacion);
                dialog.dispose();
                cargarBotonesHabitacion();
            }
        });

        dialog.setVisible(true);
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
    public static Runnable vistaAlojamientoUpdater;
    private FormularioHabitacion formularioHabitacion;  // referencia global
    private final HabitacionDAO habitacionDAO = new HabitacionDAO();
    private final AlojamientoDAO alojamientoDAO = new AlojamientoDAO();
    private final ServicioDAO servicioDAO = new ServicioDAO();
    private ArrayList<Habitacion> habitaciones = generarHabitaciones();
    private Map<String, Habitacion> mapaHabitaciones = new HashMap<>();
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
    //Index//

    public HotelAppRefactor() {
        habitaciones = generarHabitaciones();
        setTitle("Sistema de Gestión Hotelera");
        setSize(950, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setJMenuBar(crearMenuBar());

        JLabel imagen = new JLabel();
        imagen.setHorizontalAlignment(SwingConstants.CENTER);
        add(imagen, BorderLayout.CENTER);

        // Escala la imagen al iniciar
        escalarImagenALabel(imagen, "hotel.jpg");

        // Escala automáticamente cuando se redimensiona con mejor rendimiento
        imagen.addComponentListener(new ComponentAdapter() {
            private Timer resizeTimer;

            @Override
            public void componentResized(ComponentEvent e) {
                if (resizeTimer != null && resizeTimer.isRunning()) {
                    resizeTimer.restart();
                } else {
                    resizeTimer = new Timer(50, ev -> {
                        escalarImagenALabel(imagen, "hotel.jpg");
                        resizeTimer.stop();
                    });
                    resizeTimer.setRepeats(false);
                    resizeTimer.start();
                }
            }
        });

        setVisible(true);
    }

    private void escalarImagenALabel(JLabel label, String rutaImagen) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(rutaImagen));
            Image img = icon.getImage();
            Image imgEscalada = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_FAST);
            label.setIcon(new ImageIcon(imgEscalada));
            label.setText(""); // Por si antes mostraba texto de error
        } catch (Exception e) {
            label.setText("Imagen no encontrada");
            label.setFont(new Font("Segoe UI", Font.BOLD, 28));
            label.setForeground(new Color(45, 45, 45));
        }
    }

    private JMenuBar crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuGestion = new JMenu("Gestión");
        menuGestion.add(crearItem("Registrar Reservación", e -> vistaAlojamiento()));
        menuGestion.add(crearItem("Registrar Servicio", e -> vistaServicio()));
        menuGestion.add(crearItem("Registrar Habitaciones", e -> vistaHabitaciones()));

        JMenu menuRegistro = new JMenu("Facturación");
        menuRegistro.add(crearItem("Generar Factura", e -> vistaFacturacion()));

        JMenu menuAyuda = new JMenu("Ayuda");
        menuAyuda.add(crearItem("Acerca del sistema", e -> JOptionPane.showMessageDialog(this,
                "Sistema de Gestión Hotelera\nVersión 1.0\nDesarrollado por sebastian casas")));
        menuAyuda.addSeparator();
        menuAyuda.add(crearItem("Salir", e -> System.exit(0)));

        menuBar.add(menuGestion);
        menuBar.add(menuRegistro);
        menuBar.add(menuAyuda);
        return menuBar;
    }

    private JMenuItem crearItem(String texto, ActionListener action) {
        JMenuItem item = new JMenuItem(texto);
        item.addActionListener(action);
        return item;
    }
    
    public static void actualizarVistaAlojamiento() {
    if (vistaAlojamientoUpdater != null) {
        vistaAlojamientoUpdater.run();
    }
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

        Date hoy = new Date();

        JDateChooser dateEntrada = new JDateChooser();
        dateEntrada.setDateFormatString("dd/MM/yyyy");
        dateEntrada.setMinSelectableDate(hoy);

        JDateChooser dateSalida = new JDateChooser();
        dateSalida.setDateFormatString("dd/MM/yyyy");
        dateSalida.setMinSelectableDate(hoy);

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
        dialog.add(dateEntrada, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Fecha Salida:"), gbc);
        gbc.gridx = 1;
        dialog.add(dateSalida, gbc);

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
            mapaHabitaciones.clear(); // 

            int disponibles = 0;
            List<Habitacion> habitaciones = habitacionDAO.cargar();
            for (Habitacion h : habitaciones) {
                if (h.getTipo().equalsIgnoreCase(tipoSeleccionado) && h.getEstado().equalsIgnoreCase("DISPONIBLE")) {
                    String label = "Habitación " + h.getTipo().substring(0, 1).toUpperCase()
                            + h.getTipo().substring(1).toLowerCase()
                            + " " + h.getNumero().replace("HAB", "");
                    cboHabitacion.addItem(label);
                    mapaHabitaciones.put(label, h);     
                    disponibles++;
                }
            }
            txtDisponibles.setText("Disponibles: " + disponibles);
            actualizarPrecios(cboHabitacion, dateEntrada, dateSalida, txtPrecioDia, txtPrecioTotal);
        });

        btnGuardar.addActionListener(e -> {
            String cliente = txtCliente.getText().trim();
            String seleccion = (String) cboHabitacion.getSelectedItem();

            if (cliente.isEmpty() || seleccion == null || dateEntrada.getDate() == null || dateSalida.getDate() == null) {
                JOptionPane.showMessageDialog(dialog, "Debe completar todos los campos.");
                return;
            }

            Habitacion habSeleccionada = mapaHabitaciones.get(seleccion);

            if (habSeleccionada == null) {
                JOptionPane.showMessageDialog(dialog, "Habitación no encontrada.");
                return;
            }

            Date entrada = dateEntrada.getDate();
            Date salida = dateSalida.getDate();
            String hora = new SimpleDateFormat("HH:mm").format((Date) spnHora.getValue());

            Alojamiento alojamiento = new Alojamiento(cliente, habSeleccionada, chkAnticipado.isSelected(), entrada, salida, hora);
            alojamientoDAO.guardar(alojamiento);
            habSeleccionada.setEstado("OCUPADO");
            habitacionDAO.actualizar(habSeleccionada);
            JOptionPane.showMessageDialog(dialog, "Reservación Realizada\nMétodo de pago: " + cboPago.getSelectedItem());
            dialog.dispose();
        });
        // Actualizar precio cuando se selecciona una habitación
        cboHabitacion.addActionListener(e -> {
            actualizarPrecios(cboHabitacion, dateEntrada, dateSalida, txtPrecioDia, txtPrecioTotal);
        });

        // Actualizar precio cuando cambia la fecha de entrada
        dateEntrada.getDateEditor().addPropertyChangeListener("date", e -> {
            actualizarPrecios(cboHabitacion, dateEntrada, dateSalida, txtPrecioDia, txtPrecioTotal);
        });

        // Actualizar precio cuando cambia la fecha de salida
        dateSalida.getDateEditor().addPropertyChangeListener("date", e -> {
            actualizarPrecios(cboHabitacion, dateEntrada, dateSalida, txtPrecioDia, txtPrecioTotal);
        });

        dialog.setVisible(true);
    }

    private void actualizarPrecios(JComboBox<String> cboHabitacion, JDateChooser entrada, JDateChooser salida,
                               JTextField txtPrecioDia, JTextField txtPrecioTotal) {
    try {
        String seleccion = (String) cboHabitacion.getSelectedItem();
        if (seleccion == null || seleccion.trim().isEmpty()) {
            txtPrecioDia.setText("");
            txtPrecioTotal.setText("");
            return;
        }

        // Usar el mapaHabitaciones directamente
        Habitacion hab = mapaHabitaciones.get(seleccion);
        if (hab == null) {
            txtPrecioDia.setText("");
            txtPrecioTotal.setText("");
            return;
        }

        Date fEntrada = entrada.getDate();
        Date fSalida = salida.getDate();
        if (fEntrada == null || fSalida == null || fSalida.before(fEntrada)) {
            txtPrecioDia.setText(String.format("S/. %.2f", hab.getPrecio()));
            txtPrecioTotal.setText("");
            return;
        }

        // Calcular días entre fechas
        LocalDate fechaEntrada = fEntrada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaSalida = fSalida.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long dias = ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
        if (dias < 1) dias = 1;

        double precioDia = hab.getPrecio();
        double subtotal = precioDia * dias;
        double igv = subtotal * 0.18;
        double totalConIGV = subtotal + igv;

        txtPrecioDia.setText(String.format("S/. %.2f", precioDia));
        txtPrecioTotal.setText(String.format("S/. %.2f", totalConIGV));
    } catch (Exception e) {
        e.printStackTrace();
    }
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
