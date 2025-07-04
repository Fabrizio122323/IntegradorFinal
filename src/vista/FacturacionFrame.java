package vista;
import javax.swing.*;
import java.awt.*;

public class FacturacionFrame extends JFrame {
    private JTextField nombreField, cedulaField, habitacionField, serviciosField, totalField;
    private JTextArea facturaArea;

    public FacturacionFrame() {
        setTitle("Facturación");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel de entrada de datos
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        formPanel.add(new JLabel("Nombre del cliente:"));
        nombreField = new JTextField();
        formPanel.add(nombreField);

        formPanel.add(new JLabel("Cédula:"));
        cedulaField = new JTextField();
        formPanel.add(cedulaField);

        formPanel.add(new JLabel("Habitación:"));
        habitacionField = new JTextField();
        formPanel.add(habitacionField);

        formPanel.add(new JLabel("Servicios consumidos:"));
        serviciosField = new JTextField();
        formPanel.add(serviciosField);

        formPanel.add(new JLabel("Total a pagar:"));
        totalField = new JTextField();
        formPanel.add(totalField);

        JButton btnGenerar = new JButton("Generar Factura");
        btnGenerar.addActionListener(e -> generarFactura());
        formPanel.add(btnGenerar);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        formPanel.add(btnCerrar);

        add(formPanel, BorderLayout.NORTH);

        // Área donde se muestra la factura generada
        facturaArea = new JTextArea();
        facturaArea.setEditable(false);
        facturaArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        facturaArea.setBorder(BorderFactory.createTitledBorder("Factura Generada"));
        add(new JScrollPane(facturaArea), BorderLayout.CENTER);

        setVisible(true);
    }

    private void generarFactura() {
        String nombre = nombreField.getText().trim();
        String cedula = cedulaField.getText().trim();
        String habitacion = habitacionField.getText().trim();
        String servicios = serviciosField.getText().trim();
        String total = totalField.getText().trim();

        if (nombre.isEmpty() || cedula.isEmpty() || habitacion.isEmpty() || total.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor completa todos los campos obligatorios.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("===== FACTURA =====\n");
        sb.append("Cliente: ").append(nombre).append("\n");
        sb.append("Cédula: ").append(cedula).append("\n");
        sb.append("Habitación: ").append(habitacion).append("\n");
        sb.append("Servicios: ").append(servicios.isEmpty() ? "Ninguno" : servicios).append("\n");
        sb.append("Total a pagar: $").append(total).append("\n");
        sb.append("====================");

        facturaArea.setText(sb.toString());
    }
}
