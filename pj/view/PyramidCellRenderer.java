package pj.view;

import pj.model.FichaDomino;
import javax.swing.*;
import java.awt.*;
import java.util.List;
// import pj.view.FichaDominoGrafica; // Removed as it is not used

/**
 * Renderizador personalizado para la lista de fichas
 */
public class PyramidCellRenderer implements ListCellRenderer<List<FichaDomino>> {
    private final JPanel panel;
    private final Color backgroundColor;

    public PyramidCellRenderer(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends List<FichaDomino>> list,
            List<FichaDomino> fichasNivel, int index, boolean isSelected, boolean cellHasFocus) {
        panel.removeAll();
        panel.setBackground(isSelected ? backgroundColor.darker() : backgroundColor);
        fichasNivel.forEach(ficha -> panel.add(new FichaDominoGrafica(ficha)));
        return panel;
    }
}