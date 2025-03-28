package pj.model;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Modelo de datos para la lista de fichas en pirámide
 */
public class PyramidListModel extends AbstractListModel<List<FichaDomino>> {
    private final List<List<FichaDomino>> niveles;

    public PyramidListModel(List<FichaDomino> fichas) {
        this.niveles = organizarEnNiveles(fichas);
    }

    @Override
    public int getSize() {
        return niveles.size();
    }

    @Override
    public List<FichaDomino> getElementAt(int index) {
        return niveles.get(index);
    }

    private List<List<FichaDomino>> organizarEnNiveles(List<FichaDomino> fichas) {
        List<List<FichaDomino>> niveles = new ArrayList<>();
        int indice = 0;
        
        for (int nivel = 9; nivel >= 0 && indice < fichas.size(); nivel--) {
            int fichasEnNivel = nivel + 1;
            int endIndex = Math.min(indice + fichasEnNivel, fichas.size());
            niveles.add(fichas.subList(indice, endIndex));
            indice = endIndex;
        }
        
        return niveles;
    }
}