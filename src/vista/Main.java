tyytyty
        t
yt
yt


package vista;

import javax.swing.*;
// Importar FlatLaf si lo vas a usar
public class Main {
    public static void main(String[] args) {
        // Establecer tema visual moderno
        try {
            // Usa Look & Feel moderno nativo del sistema
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // O usa FlatLaf (requiere agregar dependencia)
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el Look and Feel.");
        }

        // Lanzar la interfaz en el hilo de eventos
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }

    private static class FlatLightLaf {

        private static void setup() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        public FlatLightLaf() {
        }
    }
}

