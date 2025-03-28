package pj;

import java.util.List;
import java.util.ArrayList;

public class FichaDomino {
    private int ladoIzquierdo;
    private int ladoDerecho;

    public FichaDomino(int ladoIzquierdo, int ladoDerecho) {
        this.ladoIzquierdo = ladoIzquierdo;
        this.ladoDerecho = ladoDerecho;
    }

    public int getLadoIzquierdo() {
        return ladoIzquierdo;
    }

    public int getLadoDerecho() {
        return ladoDerecho;
    }

    public static List<FichaDomino> generarTodasLasFichas() {
        List<FichaDomino> fichas = new ArrayList<>();
        for (int i = 9; i >= 0; i--) {
            for (int j = i; j >= 0; j--) {
                fichas.add(new FichaDomino(i, j));
            }
        }
        return fichas;
    }

    public void imprimirFicha() {
        System.out.println("[" + ladoIzquierdo + "|" + ladoDerecho + "]");
    }
}