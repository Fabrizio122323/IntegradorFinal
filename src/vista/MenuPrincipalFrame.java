package vista;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalFrame extends JFrame {
    public MenuPrincipalFrame() {
        setTitle("Menú Principal");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel principal con borde y padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Botones
        JButton btnAlojamiento = new JButton("Gestión de Alojamiento");
        btnAlojamiento.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAlojamiento.addActionListener(e -> new GestionAlojamientoFrame());
        mainPanel.add(btnAlojamiento);
        mainPanel.add(Box.createVerticalStrut(15));

        JButton btnServicios = new JButton("Gestión de Servicios");
        btnServicios.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnServicios.addActionListener(e -> new GestionServicioFrame());
        mainPanel.add(btnServicios);
        mainPanel.add(Box.createVerticalStrut(15));

        JButton btnFacturacion = new JButton("Facturación");
        btnFacturacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFacturacion.addActionListener(e -> new FacturacionFrame());
        mainPanel.add(btnFacturacion);
        mainPanel.add(Box.createVerticalStrut(15));

        JButton btnSalir = new JButton("Salir");
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.addActionListener(e -> System.exit(0));
        mainPanel.add(btnSalir);

        add(mainPanel);
        setVisible(true);
    }
}