package pj.model;

/**
 * Modelo de ficha de dominó
 */
public class FichaDomino {
    private final int ladoIzquierdo;
    private final int ladoDerecho;

    public FichaDomino(int ladoA, int ladoB) {
        this.ladoIzquierdo = Math.max(ladoA, ladoB);
        this.ladoDerecho = Math.min(ladoA, ladoB);
    }

    // Getters
    public int getLadoIzquierdo() { return ladoIzquierdo; }
    public int getLadoDerecho() { return ladoDerecho; }

    @Override
    public String toString() {
        return "[" + ladoIzquierdo + "|" + ladoDerecho + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FichaDomino)) return false;
        FichaDomino that = (FichaDomino) o;
        return ladoIzquierdo == that.ladoIzquierdo && 
               ladoDerecho == that.ladoDerecho;
    }

    @Override
    public int hashCode() {
        return 31 * ladoIzquierdo + ladoDerecho;
    }
}