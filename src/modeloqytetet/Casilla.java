/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

/**
 *
 * @author DavidRC
 */
public abstract class Casilla {
    private int numeroCasilla;
    private int coste;
    
    protected Casilla(int numeroCasilla){
        this.numeroCasilla = numeroCasilla;
    }
    
    /*protected Casilla(int numeroCasilla, TituloPropiedad titulo){
        this.numeroCasilla = numeroCasilla;
        setTitulo(titulo);
    }*/

    int getNumeroCasilla() {
        return numeroCasilla;
    }

    public int getCoste() {
        return coste;
    }

    abstract protected TipoCasilla getTipo();
    
    abstract public TituloPropiedad getTitulo();
    
    abstract protected boolean soyEdificable();
    
    abstract public boolean tengoPropietario();

    public void setCoste(int coste){
        this.coste = coste;
    }
}
