package pj;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class app {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextField txtX, txtY;
    private FichaDominoGrafica fichaActual;
    private JPanel displayPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new app().iniciar());
    }

    public void iniciar() {
        crearVentanaPrincipal();
        crearMenu();
        crearPanelFichaIndividual();
        crearPanelPiramide();
        mostrarVentana();
    }

    private void crearVentanaPrincipal() {
        frame = new JFrame("Piezas de dominó doble 9");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        
        mainPanel = new JPanel(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        frame.add(mainPanel, BorderLayout.CENTER);
    }

    private void crearMenu() {
        JPanel menuPanel = new JPanel();
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        menuPanel.setBackground(new Color(220, 220, 220));
        
        JButton btnMostrarFicha = crearBoton("Mostrar Ficha", e -> mostrarPanel("FICHA"));
        JButton btnMostrarPiramide = crearBoton("Mostrar Pirámide", e -> mostrarPanel("PIRAMIDE"));
        
        menuPanel.add(btnMostrarFicha);
        menuPanel.add(Box.createHorizontalStrut(10));
        menuPanel.add(btnMostrarPiramide);
        
        mainPanel.add(menuPanel, BorderLayout.NORTH);
    }

    private void crearPanelFichaIndividual() {
        JPanel fichaPanel = new JPanel(new BorderLayout());
        fichaPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de controles para seleccionar ficha
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblX = new JLabel("Valor X (0-9):");
        txtX = new JTextField("4", 3);
        JLabel lblY = new JLabel("Valor Y (0-9):");
        txtY = new JTextField("2", 3);
        JButton btnCrearFicha = crearBoton("Crear Ficha", e -> crearFicha());
        
        controlPanel.add(lblX);
        controlPanel.add(txtX);
        controlPanel.add(Box.createHorizontalStrut(10));
        controlPanel.add(lblY);
        controlPanel.add(txtY);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(btnCrearFicha);
        
        // Panel para mostrar la ficha
        displayPanel = new JPanel(new GridBagLayout());
        displayPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de controles de rotación/volteo
        JPanel actionPanel = new JPanel();
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnRotar90 = crearBoton("Girar 90°", e -> rotarFicha(90));
        JButton btnRotar180 = crearBoton("Girar 180°", e -> rotarFicha(180));
        JButton btnVoltear = crearBoton("Voltear", e -> voltearFicha());
        
        actionPanel.add(btnRotar90);
        actionPanel.add(Box.createHorizontalStrut(10));
        actionPanel.add(btnRotar180);
        actionPanel.add(Box.createHorizontalStrut(10));
        actionPanel.add(btnVoltear);
        
        // Crear ficha inicial
        crearFicha();
        
        fichaPanel.add(controlPanel, BorderLayout.NORTH);
        fichaPanel.add(displayPanel, BorderLayout.CENTER);
        fichaPanel.add(actionPanel, BorderLayout.SOUTH);
        
        cardPanel.add(fichaPanel, "FICHA");
        mainPanel.add(cardPanel, BorderLayout.CENTER);
    }

    private void crearPanelPiramide() {
        JScrollPane scrollPane = new JScrollPane();
        JPanel pyramidPanel = new JPanel();
        pyramidPanel.setLayout(new BoxLayout(pyramidPanel, BoxLayout.Y_AXIS));
        pyramidPanel.setBackground(new Color(240, 240, 240));
        
        List<FichaDomino> todasLasFichas = GenerarFichas();
        mostrarPiramideInvertida(pyramidPanel, todasLasFichas);
        
        scrollPane.setViewportView(pyramidPanel);
        cardPanel.add(scrollPane, "PIRAMIDE");
    }

    private static List<FichaDomino> GenerarFichas(){
        List<FichaDomino> todasLasFichas = FichaDomino.generarTodasLasFichas();
        for (FichaDomino ficha : todasLasFichas) {
            ficha.imprimirFicha();
        }
        return todasLasFichas;
    }

    private void mostrarPiramideInvertida(JPanel panel, List<FichaDomino> fichas) {
        int nivel = 9;
        int indice = 0;
        
        while (nivel >= 0 && indice < fichas.size()) {
            JPanel nivelPanel = new JPanel();
            nivelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            nivelPanel.setBackground(new Color(220, 220, 220));
            nivelPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
            
            for (int i = 0; i <= nivel && indice < fichas.size(); i++) {
                FichaDomino ficha = fichas.get(indice);
                FichaDominoGrafica fichaGrafica = new FichaDominoGrafica(ficha);
                nivelPanel.add(fichaGrafica);
                indice++;
            }
            
            panel.add(nivelPanel);
            nivel--;
        }
    }

    private void crearFicha() {
        try {
            int x = Integer.parseInt(txtX.getText());
            int y = Integer.parseInt(txtY.getText());
            
            if (x < 0 || x > 9 || y < 0 || y > 9) {
                JOptionPane.showMessageDialog(frame, "Los valores deben estar entre 0 y 9", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            FichaDomino nuevaFicha = new FichaDomino(x, y);
            fichaActual = new FichaDominoGrafica(nuevaFicha);
            
            // Actualizar la visualización
            displayPanel.removeAll();
            displayPanel.add(fichaActual);
            displayPanel.revalidate();
            displayPanel.repaint();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Ingrese valores numéricos válidos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rotarFicha(int grados) {
        if (fichaActual != null) {
            if (grados == 90) {
                fichaActual.rotar90Grados();
            } else {
                fichaActual.rotar180Grados();
            }
        }
    }

    private void voltearFicha() {
        if (fichaActual != null) {
            fichaActual.voltear();
        }
    }

    private void mostrarPanel(String nombrePanel) {
        cardLayout.show(cardPanel, nombrePanel);
    }

    private void mostrarVentana() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JButton crearBoton(String texto, java.awt.event.ActionListener accion) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btn.addActionListener(accion);
        return btn;
    }
}