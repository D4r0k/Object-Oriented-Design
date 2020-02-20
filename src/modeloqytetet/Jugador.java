/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;
import  java.util.ArrayList;

/**
 *
 * @author DavidRC
 */
public class Jugador implements Comparable{
    private boolean encarcelado;
    private String nombre;
    private int saldo;
    private Sorpresa cartaLibertad;
    private ArrayList<TituloPropiedad> propiedades;
    private Casilla casillaActual;
    
    Jugador(String nombre){
        encarcelado = false;
        this.nombre = nombre;
        saldo = 7500;
        propiedades = new ArrayList<>();
    }

    protected Jugador(Jugador otro){
        this.encarcelado = otro.encarcelado;
        this.nombre = otro.nombre;
        this.saldo = otro.saldo;
        this.cartaLibertad = otro.cartaLibertad;
        this.propiedades = otro.propiedades;
        this.casillaActual = otro.casillaActual;
    }
    
    boolean isEncarcelado() {
        return encarcelado;
    }

    public String getNombre() {
        return nombre;
    }

    public int getSaldo() {
        return saldo;
    }

    Sorpresa getCartaLibertad() {
        return cartaLibertad;
    }
    
    public int getCapital(){
        return obtenerCapital();
    }

    ArrayList<TituloPropiedad> getPropiedades() {
        return propiedades;
    }

    Casilla getCasillaActual() {
        return casillaActual;
    }
    
    void setEncarcelado(boolean estado) {
        this.encarcelado = estado;
    }

    void setNombre(String nombre) {
        this.nombre = nombre;
    }

    void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    void setCartaLibertad(Sorpresa cartaLibertad) {
        this.cartaLibertad = cartaLibertad;
    }

    void setCasillaActual(Casilla casillaActual) {
        this.casillaActual = casillaActual;
    }
    
    boolean cancelarHipoteca(TituloPropiedad titulo){
        boolean cancelada = false;
        int costeCancelarHipoteca = titulo.calcularCosteCancelar();
        
        if(saldo > costeCancelarHipoteca){
            modificarSaldo(-costeCancelarHipoteca);
            titulo.cancelarHipoteca();
            cancelada = true;
        }
        return cancelada;
    }
    
    boolean comprarTituloPropiedad(){
        boolean comprado = false;
        int costeCompra = casillaActual.getCoste();
       
        if(tengoSaldo(costeCompra) && !casillaActual.tengoPropietario()){
            TituloPropiedad titulo = ((Calle) casillaActual).asignarPropietario(this);
            comprado = true;
            propiedades.add(titulo);
            modificarSaldo(-costeCompra);
       }
       return comprado;
    }
    
    protected Jugador convertirme(int fianza){
        Jugador yo = new Especulador(this, fianza);
        return yo;
    }
    
    protected Jugador hacermeResponsable(int porcentajeDescuentoAlquiler){
        Jugador yo = new Responsable(this, porcentajeDescuentoAlquiler);
        return yo;
    }
    
    private String estaEncarcelado(){
        if(encarcelado)
            return "Si";
        else
            return "No";
    }
    
    @Override
    public String toString(){
        return "\n\nNombre: " + nombre + "\nSaldo: " + saldo + "\nCapital: " +
                obtenerCapital() + "\nCasilla Actual: " + casillaActual + 
                "\nPropiedades: " + propiedades.toString() + "\n¿Encarcelado?: "
                + estaEncarcelado() + "\n";
    }
    
    int cuantasCasasHotelesTengo() {
        int totCasaHotel = 0;
        for (TituloPropiedad propiedad: propiedades){
            totCasaHotel += propiedad.getNumCasas() + 
                    propiedad.getNumHoteles();
        }
        return totCasaHotel;
    }
    
    protected boolean deboIrACarcel(){
        return !this.tengoCartaLibertad();
    }
    
    boolean deboPagarAlquiler() {
        boolean esDeMiPropiedad = this.esDeMiPropiedad(casillaActual.getTitulo());
        boolean tienePropietario = false;
        boolean estaEncarcelado = true;
        boolean estaHipotecada = true;
        
        if(!esDeMiPropiedad){
            tienePropietario = casillaActual.getTitulo()
                    .tengoPropietario();
            if(tienePropietario){
                estaEncarcelado = ((Calle) casillaActual).propietarioEncarcelado();
                estaHipotecada = casillaActual.getTitulo().isHipotecada();
            }     
        }
        
        return (!esDeMiPropiedad && tienePropietario && !estaEncarcelado && !estaHipotecada);
    }
    
    Sorpresa devolverCartaLibertad() {
        Sorpresa cartaLibertadDevuelta = cartaLibertad;
        cartaLibertad = null;
        return cartaLibertadDevuelta;
    }
    
     boolean edificarCasa(TituloPropiedad titulo) {
        boolean edificada = false;
        
        if(puedoEdificarCasa(titulo)){
                titulo.edificarCasa();
                modificarSaldo(-titulo.getPrecioEdificar());
                edificada = true;
        }        
        
        return edificada;
    }
    
    boolean edificarHotel(TituloPropiedad titulo) {
        boolean edificada = false;

        if(puedoEdificarHotel(titulo)){
                titulo.edificarHotel();
                modificarSaldo(-titulo.getPrecioEdificar());
                edificada = true;
        }
        return edificada;
    }
    
    private void eliminarDeMisPropiedades(TituloPropiedad titulo) {
        propiedades.remove(titulo);
        titulo.setPropietario(null);
    }
    
    private boolean esDeMiPropiedad(TituloPropiedad titulo) {
        boolean encontrado = false;
        for(TituloPropiedad propiedad: propiedades){
            if(propiedad == titulo){
                encontrado = true;
                break;
            }
        }
        return encontrado;
    }
    
    boolean estoyEnCalleLibre() {
        throw new UnsupportedOperationException("Sin implementar");
    }
    
    boolean hipotecarPropiedad(TituloPropiedad titulo) {
        int costeHipoteca = titulo.hipotecar();
        modificarSaldo(costeHipoteca);
        return titulo.isHipotecada();
    }
    
    void irACarcel(Casilla casilla) {
        setCasillaActual(casilla);
        setEncarcelado(true);
    }
    
    int modificarSaldo(int cantidad) {
        return saldo += cantidad;
    }
    
    int obtenerCapital() {
        int capital = saldo;
        for(TituloPropiedad propiedad: propiedades){
            capital += propiedad.getPrecioCompra() + (propiedad.getNumCasas()
                    + propiedad.getNumHoteles())* propiedad.getPrecioEdificar();
            
            if(propiedad.isHipotecada()){
                capital = capital - propiedad.getAlquilerBase();
            }
        }
        return capital;
    }
    
    ArrayList<TituloPropiedad> obtenerPropiedades(boolean hipotecada) {
        ArrayList<TituloPropiedad> new_propiedades = new ArrayList<>();
        for(TituloPropiedad propiedad: propiedades){
            if(propiedad.isHipotecada() == hipotecada)
                new_propiedades.add(propiedad);
        }
        return new_propiedades;
    }
    
    void pagarAlquiler() {
        int costeAlquiler = ((Calle) casillaActual).pagarAlquiler();
        this.modificarSaldo(-costeAlquiler);
    }
    
    protected void pagarImpuesto() {
        saldo -= casillaActual.getCoste();
        System.out.print("\nSe han pagado " + casillaActual.getCoste() +
                " euros en impuestos, su saldo actual es de " + saldo +
                " euros.\n");
    }
    
    void pagarLibertad(int cantidad) {
        boolean tengoSaldo = tengoSaldo(cantidad);
        if(tengoSaldo){
            setEncarcelado(false);
            modificarSaldo(-cantidad);
        }
    }
    
    protected boolean puedoEdificarCasa(TituloPropiedad titulo){
        boolean puedo = (titulo.getNumCasas() < 4);
        
        if(puedo){
            int costeEdificarCasa = titulo.getPrecioEdificar();
            puedo = tengoSaldo(costeEdificarCasa);
        }
        return puedo;
    }
    
    protected boolean puedoEdificarHotel(TituloPropiedad titulo){
        boolean puedo = (titulo.getNumCasas() == 4 && titulo.getNumHoteles() < 4);
        if(puedo){
            int costeEdificarHotel = titulo.getPrecioEdificar();
            puedo = tengoSaldo(costeEdificarHotel);
        }
        return puedo;
    }
    
    boolean tengoCartaLibertad() {
        return cartaLibertad != null;
    }
    
    protected boolean tengoSaldo(int cantidad) {
        return saldo > cantidad;
    }
    
    void venderPropiedad(Casilla casilla) {
        TituloPropiedad titulo = casilla.getTitulo();
        eliminarDeMisPropiedades(titulo);
        int precioVenta = titulo.calcularPrecioVenta();
        modificarSaldo(precioVenta);
    }
    
    @Override
    public int compareTo(Object otroJugador){
        int otroCapital = ((Jugador) otroJugador).obtenerCapital();
        return otroCapital - obtenerCapital();
    }
}
