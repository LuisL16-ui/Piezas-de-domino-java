package pj;

import pj.model.FichaDomino;
import pj.service.FichaDominoService;
import pj.view.FichaDominoGrafica;
import pj.view.PyramidCellRenderer;
import pj.model.PyramidListModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

public class app {
    // Constantes de diseño
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
    private static final Color PANEL_COLOR = new Color(220, 220, 220);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color TEST_BUTTON_COLOR = new Color(100, 150, 200);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final int BUTTON_PADDING = 8;
    
    // Componentes principales
    private final JFrame frame = new JFrame("Piezas de dominó doble 9");
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final JTextField txtX = new JTextField("4", 3);
    private final JTextField txtY = new JTextField("2", 3);
    private FichaDominoGrafica fichaActual;
    private final JPanel displayPanel = new JPanel(new GridBagLayout());
    private final FichaDominoService dominoService = new FichaDominoService();
    private JScrollPane pyramidScrollPane;
    
    // Listeners reutilizables
    private final ActionListener rotate90Action = e -> rotarFicha(90);
    private final ActionListener rotate180Action = e -> rotarFicha(180);
    private final ActionListener flipAction = this::voltearFicha;
    private final ActionListener showFichaAction = e -> mostrarPanel("FICHA");
    private final ActionListener showPiramideAction = e -> mostrarPanel("PIRAMIDE");
    private final ActionListener createFichaAction = this::crearFicha;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new app().iniciar());
    }

    public void iniciar() {
        configurarVentanaPrincipal();
        crearInterfaz();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void configurarVentanaPrincipal() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(crearPanelMenu(), BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        
        frame.add(mainPanel);
    }

    private JPanel crearPanelMenu() {
        JPanel menuPanel = crearPanelBasico(10);
        
        menuPanel.add(crearBoton("Mostrar Ficha", showFichaAction));
        menuPanel.add(Box.createHorizontalStrut(10));
        menuPanel.add(crearBoton("Mostrar Pirámide", showPiramideAction));
        
        return menuPanel;
    }

    private void crearInterfaz() {
        cardPanel.add(crearPanelFichaIndividual(), "FICHA");
        cardPanel.add(crearPanelPiramide(), "PIRAMIDE");
    }

    private JPanel crearPanelFichaIndividual() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(crearPanelControles(), BorderLayout.NORTH);
        panel.add(configurarDisplayPanel(), BorderLayout.CENTER);
        panel.add(crearPanelAcciones(), BorderLayout.SOUTH);
        
        crearFichaInicial();
        return panel;
    }

    private JPanel crearPanelControles() {
        JPanel panel = crearPanelBasico(10);
        
        panel.add(new JLabel("Valor X (0-9):"));
        panel.add(txtX);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel("Valor Y (0-9):"));
        panel.add(txtY);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(crearBoton("Crear Ficha", createFichaAction));
        
        return panel;
    }

    private JPanel configurarDisplayPanel() {
        displayPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        displayPanel.setBackground(BACKGROUND_COLOR);
        return displayPanel;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = crearPanelBasico(10);
        
        panel.add(crearBoton("Girar 90°", rotate90Action));
        panel.add(Box.createHorizontalStrut(10));
        panel.add(crearBoton("Girar 180°", rotate180Action));
        panel.add(Box.createHorizontalStrut(10));
        panel.add(crearBoton("Voltear", flipAction));
        panel.add(Box.createHorizontalStrut(10));
        panel.add(crearBotonPruebaRendimiento());
        
        return panel;
    }

    private JButton crearBotonPruebaRendimiento() {
        JButton btn = new JButton("Prueba Rendimiento");
        btn.setFont(BUTTON_FONT);
        btn.setBackground(TEST_BUTTON_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(BUTTON_PADDING, 15, BUTTON_PADDING, 15)
        ));
        btn.addActionListener(this::ejecutarPruebaRendimiento);
        return btn;
    }

    private void ejecutarPruebaRendimiento(ActionEvent e) {
        if (fichaActual == null) {
            JOptionPane.showMessageDialog(frame, 
                "Crea una ficha primero", 
                "Prueba de Rendimiento", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        setEnableAllButtons(false);
        
        new Thread(() -> {
            try {
                int iterations = 100;
                java.util.concurrent.atomic.AtomicLong totalRotacion = new java.util.concurrent.atomic.AtomicLong(0);
                java.util.concurrent.atomic.AtomicLong totalRender = new java.util.concurrent.atomic.AtomicLong(0);
                
                // Warmup
                for(int i=0; i<10; i++) {
                    fichaActual.rotar90Grados();
                    fichaActual.repaint();
                }
                
                // Prueba real
                long startTotal = System.nanoTime();
                
                for(int i=0; i<iterations; i++) {
                    long startRot = System.nanoTime();
                    fichaActual.rotar90Grados();
                    totalRotacion.addAndGet(System.nanoTime() - startRot);
                    
                    long startRen = System.nanoTime();
                    fichaActual.repaint();
                    Toolkit.getDefaultToolkit().sync();
                    totalRender.addAndGet(System.nanoTime() - startRen);
                }
                
                long totalTime = System.nanoTime() - startTotal;
                
                SwingUtilities.invokeLater(() -> {
                    String mensaje = String.format(
                        "<html><b>Resultados de la prueba (100 iteraciones):</b><br><br>" +
                        "• Tiempo total: %.2f ms<br>" +
                        "• Rotación promedio: %.3f ms<br>" +
                        "• Renderizado promedio: %.3f ms<br>" +
                        "• Total por operación: %.3f ms</html>",
                        totalTime / 1_000_000.0,
                        totalRotacion.get() / (iterations * 1_000_000.0),
                        totalRender.get() / (iterations * 1_000_000.0),
                        (totalRotacion.get() + totalRender.get()) / (iterations * 1_000_000.0));
                    
                    JOptionPane.showMessageDialog(frame, 
                        mensaje, 
                        "Resultados de Rendimiento", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    setEnableAllButtons(true);
                });
                
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(frame, 
                        "Error durante la prueba: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    setEnableAllButtons(true);
                });
            }
        }).start();
    }

    private void setEnableAllButtons(boolean enabled) {
        Component[] components = frame.getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                setButtonsEnabled((JPanel) comp, enabled);
            }
        }
    }

    private void setButtonsEnabled(JPanel panel, boolean enabled) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                comp.setEnabled(enabled);
            } else if (comp instanceof JPanel) {
                setButtonsEnabled((JPanel) comp, enabled);
            }
        }
    }

    private JScrollPane crearPanelPiramide() {
        if (pyramidScrollPane == null) {
            PyramidListModel model = new PyramidListModel(dominoService.generarTodasLasFichas());
            JList<List<FichaDomino>> list = new JList<>(model);
            list.setCellRenderer(new PyramidCellRenderer(PANEL_COLOR));
            list.setLayoutOrientation(JList.VERTICAL);
            list.setVisibleRowCount(-1);
            list.setBackground(BACKGROUND_COLOR);
            
            pyramidScrollPane = new JScrollPane(list);
            pyramidScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        }
        return pyramidScrollPane;
    }

    private JPanel crearPanelBasico(int padding) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        panel.setBackground(PANEL_COLOR);
        return panel;
    }

    private JButton crearBoton(String texto, ActionListener accion) {
        JButton btn = new JButton(texto);
        btn.setFont(BUTTON_FONT);
        btn.setBackground(BUTTON_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(BUTTON_PADDING, 15, BUTTON_PADDING, 15)
        ));
        btn.addActionListener(accion);
        return btn;
    }

    private void mostrarPanel(String nombrePanel) {
        cardLayout.show(cardPanel, nombrePanel);
    }

    private void crearFichaInicial() {
        crearFicha(new ActionEvent(txtX, ActionEvent.ACTION_PERFORMED, ""));
    }

    private void crearFicha(ActionEvent e) {
        String xText = txtX.getText().trim();
        String yText = txtY.getText().trim();
        
        if (xText.isEmpty() || yText.isEmpty()) {
            mostrarError("Los valores no pueden estar vacíos");
            return;
        }
        
        try {
            int x = parseInt(xText);
            int y = parseInt(yText);
            
            if (!esValido(x) || !esValido(y)) {
                mostrarError("Los valores deben estar entre 0 y 9");
                resetearCampos();
                return;
            }
            
            actualizarFicha(dominoService.crearFicha(x, y));
            
        } catch (NumberFormatException ex) {
            mostrarError("Ingrese valores numéricos válidos");
            resetearCampos();
        }
    }

    private int parseInt(String text) throws NumberFormatException {
        return Integer.parseInt(Objects.requireNonNull(text).trim());
    }

    private boolean esValido(int valor) {
        return valor >= 0 && valor <= 9;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void resetearCampos() {
        txtX.setText("4");
        txtY.setText("2");
    }

    private void actualizarFicha(FichaDomino ficha) {
        System.out.println("Creando ficha: " + ficha);
        fichaActual = new FichaDominoGrafica(ficha);
        
        displayPanel.removeAll();
        displayPanel.add(fichaActual);
        displayPanel.revalidate();
        displayPanel.repaint();
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

    private void voltearFicha(ActionEvent e) {
        if (fichaActual != null) {
            fichaActual.voltear();
        }
    }
}