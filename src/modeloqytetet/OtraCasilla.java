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
public class OtraCasilla extends Casilla {
    private TipoCasilla tipo;
    
    protected OtraCasilla(int numCasilla, TipoCasilla tipo){
        super(numCasilla);
        this.tipo = tipo;
        if(tipo == TipoCasilla.IMPUESTO){
            super.setCoste(300);
        }
    }
    
    @Override
    protected TipoCasilla getTipo() {
        return tipo;
    }
    
    @Override
    public TituloPropiedad getTitulo(){
        return null;
    }
    
    @Override
    protected boolean soyEdificable(){
        return false;
    }
    
    @Override
    public boolean tengoPropietario(){
        return false;
    }
    
    @Override
    public String toString(){
        return "Numero de Casilla: " + super.getNumeroCasilla() + 
            "\nTipo de Casilla: " + tipo;
    }
}
