

package vista;
import javax.swing.*;
import java.awt.*;

public class GestionAlojamientoFrame extends JFrame {
    public GestionAlojamientoFrame() {
        setTitle("Gestión de Alojamiento");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Nombre del Cliente:"));
        JTextField nombreField = new JTextField();
        panel.add(nombreField);

        panel.add(new JLabel("Tipo de Habitación:"));
        JTextField habitacionField = new JTextField();
        panel.add(habitacionField);

        JButton btnRegistrar = new JButton("Registrar Alojamiento");
        btnRegistrar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Alojamiento registrado"));
        panel.add(btnRegistrar);

        JButton btnSalir = new JButton("Cerrar");
        btnSalir.addActionListener(e -> dispose());
        panel.add(btnSalir);

        add(panel);
        setVisible(true);
    }
}