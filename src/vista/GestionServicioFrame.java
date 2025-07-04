package vista;
import javax.swing.*;
import java.awt.*;

public class GestionServicioFrame extends JFrame {
    public GestionServicioFrame() {
        setTitle("Gestión de Servicios");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Nombre del Servicio:"));
        JTextField servicioField = new JTextField();
        panel.add(servicioField);

        panel.add(new JLabel("Costo:"));
        JTextField costoField = new JTextField();
        panel.add(costoField);

        JButton btnRegistrar = new JButton("Registrar Servicio");
        btnRegistrar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Servicio registrado"));
        panel.add(btnRegistrar);

        JButton btnSalir = new JButton("Cerrar");
        btnSalir.addActionListener(e -> dispose());
        panel.add(btnSalir);

        add(panel);
        setVisible(true);
    }
}