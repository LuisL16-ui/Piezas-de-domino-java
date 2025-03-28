package pj.view;

import pj.model.FichaDomino;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import static java.util.Map.entry;

public class FichaDominoGrafica extends JPanel {
    private final FichaDomino ficha;
    private int rotacion = 0;
    private boolean volteada = false;
    private BufferedImage cachedImage;
    private boolean needsRedraw = true;
    
    // Dimensiones
    private static final int ANCHO_BASE = 100;
    private static final int ALTO_BASE = 200;
    private static final int PANEL_SIZE = (int)(Math.hypot(ANCHO_BASE, ALTO_BASE)) + 20;
    
    // Constantes de diseño
    private static final int DOT_SIZE = 10;
    private static final int DOT_SPACING = 10;
    private static final int BORDER_RADIUS = 15;
    
    // Cache de gráficos
    private static final Shape DOT_SHAPE = new Ellipse2D.Double(-DOT_SIZE/2, -DOT_SIZE/2, DOT_SIZE, DOT_SIZE);
    private static final Map<Integer, Color> COLORES_PUNTOS = Map.ofEntries(
        entry(1, Color.BLUE), entry(2, Color.RED), entry(3, Color.GREEN),
        entry(4, Color.YELLOW), entry(5, Color.ORANGE), entry(6, Color.PINK),
        entry(7, Color.MAGENTA), entry(8, Color.CYAN), entry(9, new Color(128, 0, 128))
    );
    
    private static final Map<Integer, int[][]> PUNTOS_POR_VALOR = Map.ofEntries(
        entry(1, new int[][]{{0, 0}}),
        entry(2, new int[][]{{-1, -1}, {1, 1}}),
        entry(3, new int[][]{{0, 0}, {-1, -1}, {1, 1}}),
        entry(4, new int[][]{{-1, -1}, {1, -1}, {-1, 1}, {1, 1}}),
        entry(5, new int[][]{{0, 0}, {-1, -1}, {1, -1}, {-1, 1}, {1, 1}}),
        entry(6, new int[][]{{-1, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {1, 1}}),
        entry(7, new int[][]{{0, 0}, {-1, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {1, 1}}),
        entry(8, new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}}),
        entry(9, new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}})
    );

    public FichaDominoGrafica(FichaDomino ficha) {
        this.ficha = ficha;
        setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        setOpaque(false);
        setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Limpiar el área de dibujo completamente
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        
        if (needsRedraw || cachedImage == null) {
            renderToCache();
        }
        
        // Dibujar la imagen cacheada centrada
        int x = (getWidth() - PANEL_SIZE) / 2;
        int y = (getHeight() - PANEL_SIZE) / 2;
        g.drawImage(cachedImage, x, y, null);
    }
    
    private void renderToCache() {
        cachedImage = new BufferedImage(PANEL_SIZE, PANEL_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = cachedImage.createGraphics();
        
        // Configuración de calidad
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Fondo transparente
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, PANEL_SIZE, PANEL_SIZE);
        g2d.setComposite(AlphaComposite.SrcOver);
        
        // Transformación
        AffineTransform transform = getCurrentTransform();
        g2d.setTransform(transform);
        
        dibujarFicha(g2d);
        
        g2d.dispose();
        needsRedraw = false;
    }
    
    private AffineTransform getCurrentTransform() {
        AffineTransform transform = new AffineTransform();
        transform.translate(PANEL_SIZE/2, PANEL_SIZE/2);
        transform.rotate(Math.toRadians(rotacion));
        transform.translate(-ANCHO_BASE/2, -ALTO_BASE/2);
        return transform;
    }

    private void dibujarFicha(Graphics2D g) {
        // Fondo blanco
        g.setColor(Color.WHITE);
        g.fillRoundRect(0, 0, ANCHO_BASE, ALTO_BASE, BORDER_RADIUS, BORDER_RADIUS);
        
        // Borde
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.8f));
        g.drawRoundRect(0, 0, ANCHO_BASE, ALTO_BASE, BORDER_RADIUS, BORDER_RADIUS);

        if (!volteada) {
            // Línea divisoria
            g.setStroke(new BasicStroke(1.5f));
            g.drawLine(0, ALTO_BASE/2, ANCHO_BASE, ALTO_BASE/2);

            dibujarMitadPuntos(g, ficha.getLadoIzquierdo(), ANCHO_BASE/2, ALTO_BASE/4);
            dibujarMitadPuntos(g, ficha.getLadoDerecho(), ANCHO_BASE/2, 3*ALTO_BASE/4);
        }
    }

    private void dibujarMitadPuntos(Graphics2D g, int valor, int centerX, int centerY) {
        if (valor == 0 || !PUNTOS_POR_VALOR.containsKey(valor)) return;
        
        g.setColor(COLORES_PUNTOS.getOrDefault(valor, Color.BLACK));
        int spacing = (rotacion % 180 == 0) ? DOT_SPACING : DOT_SPACING - 2;
        
        AffineTransform oldTransform = g.getTransform();
        g.translate(centerX, centerY);
        
        for (int[] punto : PUNTOS_POR_VALOR.get(valor)) {
            g.translate(punto[0] * spacing, punto[1] * spacing);
            g.fill(DOT_SHAPE);
            g.translate(-punto[0] * spacing, -punto[1] * spacing);
        }
        
        g.setTransform(oldTransform);
    }

    public void rotar90Grados() {
        rotacion = (rotacion + 90) % 360;
        invalidateCache();
        repaint();
    }

    public void rotar180Grados() {
        rotacion = (rotacion + 180) % 360;
        invalidateCache();
        repaint();
    }

    public void voltear() {
        volteada = !volteada;
        invalidateCache();
        repaint();
    }

    private void invalidateCache() {
        needsRedraw = true;
        cachedImage = null;
    }
}