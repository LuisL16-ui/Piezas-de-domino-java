package pj.service;

import pj.model.FichaDomino;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 * Servicio para gestión de fichas de dominó
 */
public class FichaDominoService {
    private static final List<FichaDomino> ALL_TILES = generateAllTiles();

    private static List<FichaDomino> generateAllTiles() {
        List<FichaDomino> tiles = new ArrayList<>(55);
        for (int i = 9; i >= 0; i--) {
            for (int j = i; j >= 0; j--) {
                tiles.add(new FichaDomino(i, j));
            }
        }
        return Collections.unmodifiableList(tiles);
    }

    public List<FichaDomino> generarTodasLasFichas() {
        return ALL_TILES;
    }

    public FichaDomino crearFicha(int ladoA, int ladoB) {
        return new FichaDomino(ladoA, ladoB);
    }
}