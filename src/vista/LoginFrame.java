



package vista;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField usuarioField;
    private final JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Inicio de Sesión");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel principal con borde y padding
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Usuario:"), gbc);

        // Campo Usuario
        gbc.gridx = 1;
        usuarioField = new JTextField(15);
        panel.add(usuarioField, gbc);

        // Etiqueta Contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Contraseña:"), gbc);

        // Campo Contraseña
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Botón de Login
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginBtn = new JButton("Iniciar Sesión");
        loginBtn.addActionListener(e -> validarCredenciales());
        panel.add(loginBtn, gbc);

        // Agregar panel al frame
        add(panel);
        setVisible(true);
    }

    private void validarCredenciales() {
        String usuario = usuarioField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete ambos campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (usuario.equals("admin") && password.equals("1234")) {
            new MenuPrincipalFrame();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

