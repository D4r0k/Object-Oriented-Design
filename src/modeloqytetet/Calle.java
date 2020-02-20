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
public class Calle extends Casilla {
    static private final TipoCasilla TIPO = TipoCasilla.CALLE;
    private TituloPropiedad titulo;
    
    protected Calle(int numCasilla, TituloPropiedad titulo){
        super(numCasilla);
        setTitulo(titulo);
    }
    
    public TituloPropiedad asignarPropietario(Jugador jugador){
        titulo.setPropietario(jugador);
        return titulo;
    }
    
    @Override
    protected TipoCasilla getTipo() {
        return TIPO;
    }
    
    @Override
    public TituloPropiedad getTitulo() {
        return titulo;
    }
    
    public int pagarAlquiler(){
        return titulo.pagarAlquiler();
    }
    
    boolean propietarioEncarcelado(){
        boolean enCarcel = false;
        if(tengoPropietario())
           enCarcel = titulo.propietarioEncarcelado();
        
        return enCarcel;
    }
    
    //Inicializaamos el coste en el mismo setTitulo, ya que este viene dado por
    //el titulo en cuestion
    private void setTitulo(TituloPropiedad titulo) {
        this.titulo = titulo;
        super.setCoste(titulo.getPrecioCompra());
    }
    
    @Override
    protected boolean soyEdificable(){
        return true;
    }
    
    @Override
    public boolean tengoPropietario(){
        boolean propietarioExiste = false;
        if(soyEdificable())
            propietarioExiste = titulo.tengoPropietario();
        
        return propietarioExiste;
    }
    
    @Override
    public String toString(){
        return "Numero de Casilla: " + super.getNumeroCasilla() + 
                "\nTipo de Casilla: " + TIPO + "\n" + titulo.toString();
    }
    
}
