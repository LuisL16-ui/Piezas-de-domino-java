package pj;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;

public class FichaDominoGrafica extends JPanel {
    private final FichaDomino ficha;
    private int rotacion = 0;
    private boolean volteada = false;
    
    // Dimensiones base de la ficha (sin rotación)
    private static final int ANCHO_BASE = 50;
    private static final int ALTO_BASE = 100;
    
    // Tamaño del panel (diagonal + margen de seguridad)
    private static final int PANEL_SIZE = (int)(Math.sqrt(ANCHO_BASE*ANCHO_BASE + ALTO_BASE*ALTO_BASE)) + 20;
    
    // Constantes de diseño
    private static final int DOT_SIZE = 8;
    private static final int DOT_SPACING = 10;
    private static final int BORDER_RADIUS = 15;
    private static final Color COLOR_FONDO = new Color(240, 240, 240);
    private static final Color COLOR_BORDE = new Color(50, 50, 50);
    
    // Colores para los puntos según la cantidad
    private static final Color COLOR_1_PUNTO = Color.BLUE;
    private static final Color COLOR_2_PUNTOS = Color.RED;
    private static final Color COLOR_3_PUNTOS = Color.GREEN;
    private static final Color COLOR_4_PUNTOS = Color.YELLOW;
    private static final Color COLOR_5_PUNTOS = Color.ORANGE;
    private static final Color COLOR_6_PUNTOS = Color.PINK;
    private static final Color COLOR_7_PUNTOS = Color.MAGENTA;
    private static final Color COLOR_8_PUNTOS = Color.CYAN;
    private static final Color COLOR_9_PUNTOS = new Color(128, 0, 128); // Púrpura oscuro

    public FichaDominoGrafica(FichaDomino ficha) {
        this.ficha = ficha;
        setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        setBackground(COLOR_FONDO);
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Configuración de calidad
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Transformación para rotación centrada
        AffineTransform oldTransform = g2d.getTransform();
        g2d.translate(PANEL_SIZE/2, PANEL_SIZE/2);  // Mover al centro
        g2d.rotate(Math.toRadians(rotacion));       // Aplicar rotación
        g2d.translate(-ANCHO_BASE/2, -ALTO_BASE/2); // Ajustar posición

        // Dibujar ficha
        dibujarFicha(g2d);
        
        // Restaurar transformación
        g2d.setTransform(oldTransform);
    }

    private void dibujarFicha(Graphics2D g) {
        // Fondo con efecto de elevación
        g.setColor(Color.WHITE);
        g.fillRoundRect(0, 0, ANCHO_BASE, ALTO_BASE, BORDER_RADIUS, BORDER_RADIUS);
        
        // Borde con grosor variable
        g.setColor(COLOR_BORDE);
        g.setStroke(new BasicStroke(1.8f));
        g.drawRoundRect(0, 0, ANCHO_BASE, ALTO_BASE, BORDER_RADIUS, BORDER_RADIUS);

        // Solo dibujar línea divisoria y puntos si no está volteada
        if (!volteada) {
            // Línea divisoria dinámica
            g.setStroke(new BasicStroke(1.5f));
            g.drawLine(0, ALTO_BASE/2, ANCHO_BASE, ALTO_BASE/2);

            // Dibujar puntos (adaptados a la rotación)
            dibujarMitadPuntos(g, ficha.getLadoIzquierdo(), ANCHO_BASE/2, ALTO_BASE/4);
            dibujarMitadPuntos(g, ficha.getLadoDerecho(), ANCHO_BASE/2, 3*ALTO_BASE/4);
        }
    }

    private void dibujarMitadPuntos(Graphics2D g, int valor, int centerX, int centerY) {
        if (valor == 0) return;
        
        // Establecer color según la cantidad de puntos
        switch (valor) {
            case 1: g.setColor(COLOR_1_PUNTO); break;
            case 2: g.setColor(COLOR_2_PUNTOS); break;
            case 3: g.setColor(COLOR_3_PUNTOS); break;
            case 4: g.setColor(COLOR_4_PUNTOS); break;
            case 5: g.setColor(COLOR_5_PUNTOS); break;
            case 6: g.setColor(COLOR_6_PUNTOS); break;
            case 7: g.setColor(COLOR_7_PUNTOS); break;
            case 8: g.setColor(COLOR_8_PUNTOS); break;
            case 9: g.setColor(COLOR_9_PUNTOS); break;
        }
        
        int spacing = (rotacion % 180 == 0) ? DOT_SPACING : DOT_SPACING - 2;

        switch (valor) {
            case 1:
                dibujarPunto(g, centerX, centerY);
                break;
            case 2:
                dibujarPunto(g, centerX - spacing, centerY - spacing);
                dibujarPunto(g, centerX + spacing, centerY + spacing);
                break;
            case 3:
                dibujarPunto(g, centerX, centerY);
                dibujarPunto(g, centerX - spacing, centerY - spacing);
                dibujarPunto(g, centerX + spacing, centerY + spacing);
                break;
            case 4:
                dibujarPunto(g, centerX - spacing, centerY - spacing);
                dibujarPunto(g, centerX + spacing, centerY - spacing);
                dibujarPunto(g, centerX - spacing, centerY + spacing);
                dibujarPunto(g, centerX + spacing, centerY + spacing);
                break;
            case 5:
                dibujarPunto(g, centerX, centerY);
                dibujarPunto(g, centerX - spacing, centerY - spacing);
                dibujarPunto(g, centerX + spacing, centerY - spacing);
                dibujarPunto(g, centerX - spacing, centerY + spacing);
                dibujarPunto(g, centerX + spacing, centerY + spacing);
                break;
            case 6:
                dibujarPunto(g, centerX - spacing, centerY - spacing);
                dibujarPunto(g, centerX + spacing, centerY - spacing);
                dibujarPunto(g, centerX - spacing, centerY);
                dibujarPunto(g, centerX + spacing, centerY);
                dibujarPunto(g, centerX - spacing, centerY + spacing);
                dibujarPunto(g, centerX + spacing, centerY + spacing);
                break;
            case 7:
                dibujarPunto(g, centerX, centerY);
                dibujarPunto(g, centerX - spacing, centerY - spacing);
                dibujarPunto(g, centerX + spacing, centerY - spacing);
                dibujarPunto(g, centerX - spacing, centerY);
                dibujarPunto(g, centerX + spacing, centerY);
                dibujarPunto(g, centerX - spacing, centerY + spacing);
                dibujarPunto(g, centerX + spacing, centerY + spacing);
                break;
            case 8:
                dibujarPunto(g, centerX - spacing, centerY - spacing);
                dibujarPunto(g, centerX, centerY - spacing);
                dibujarPunto(g, centerX + spacing, centerY - spacing);
                dibujarPunto(g, centerX - spacing, centerY);
                dibujarPunto(g, centerX + spacing, centerY);
                dibujarPunto(g, centerX - spacing, centerY + spacing);
                dibujarPunto(g, centerX, centerY + spacing);
                dibujarPunto(g, centerX + spacing, centerY + spacing);
                break;
            case 9:
                dibujarPunto(g, centerX - spacing, centerY - spacing);
                dibujarPunto(g, centerX, centerY - spacing);
                dibujarPunto(g, centerX + spacing, centerY - spacing);
                dibujarPunto(g, centerX - spacing, centerY);
                dibujarPunto(g, centerX, centerY);
                dibujarPunto(g, centerX + spacing, centerY);
                dibujarPunto(g, centerX - spacing, centerY + spacing);
                dibujarPunto(g, centerX, centerY + spacing);
                dibujarPunto(g, centerX + spacing, centerY + spacing);
                break;
        }
    }

    private void dibujarPunto(Graphics2D g, int x, int y) {
        g.fill(new Ellipse2D.Double(x - DOT_SIZE/2, y - DOT_SIZE/2, DOT_SIZE, DOT_SIZE));
    }

    // Métodos de control de rotación
    public void rotar90Grados() {
        rotacion = (rotacion + 90) % 360;
        repaint();
    }

    public void rotar180Grados() {
        rotacion = (rotacion + 180) % 360;
        repaint();
    }

    // Método para voltear la ficha
    public void voltear() {
        volteada = !volteada;
        repaint();
    }
}