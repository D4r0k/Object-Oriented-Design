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
public class TituloPropiedad {
    private String nombre;
    private boolean hipotecada;
    private int precioCompra;
    private int alquilerBase;
    private float factorRevalorizacion;
    private int hipotecaBase;
    private int precioEdificar;
    private int numHoteles;
    private int numCasas;
    private Jugador propietario;
    
    TituloPropiedad(String nombre, int precioCompra, int alquilerBase, 
            float factorRevalorizacion, int precioEdificar, int hipotecaBase){
        this.nombre = nombre;
        this.precioCompra = precioCompra;
        this.alquilerBase = alquilerBase;
        this.factorRevalorizacion = factorRevalorizacion;
        this.precioEdificar = precioEdificar;
        this.hipotecaBase = hipotecaBase;
        hipotecada = false;
        numHoteles = 0;
        numCasas = 0;
        propietario = null;
    }

    public String getNombre() {
        return nombre;
    }

    boolean isHipotecada() {
        return hipotecada;
    }

    int getPrecioCompra() {
        return precioCompra;
    }

    int getAlquilerBase() {
        return alquilerBase;
    }

    public int getPrecioEdificar() {
        return precioEdificar;
    }

    int getHipotecaBase() {
        return hipotecaBase;
    }

    public int getNumHoteles() {
        return numHoteles;
    }

    public int getNumCasas() {
        return numCasas;
    }

    float getFactorRevalorizacion() {
        return factorRevalorizacion;
    }

    Jugador getPropietario() {
        return propietario;
    }

    void setPropietario(Jugador propietario) {
        this.propietario = propietario;
    }
    
    void setHipotecada(boolean hipotecada) {
        this.hipotecada = hipotecada;
    }
    
    //Metodo usado para sacar en el toString "Si" o "No" en vez de "true" o 
    //"false"
    public String estaHipotecada(){
        if (hipotecada == true)
            return "Si";
        else
            return "No";
    }

    int calcularCosteCancelar(){
        int costeCancelar = (int) (calcularCosteHipotecar() + 
                (int) calcularCosteHipotecar() * 0.1);
        return costeCancelar;
    }
    
    int calcularCosteHipotecar(){
        int costeHipoteca =(int) ((int) hipotecaBase + numCasas * 0.5 * hipotecaBase + 
                numHoteles + hipotecaBase);
        return costeHipoteca;
    }
    
    int calcularImporteAlquiler(){
        int porcentajeCasa = (int) (precioEdificar*0.2);
        int porcentajeHotel = (int) (precioEdificar*0.3);
        int costeAlquiler = this.alquilerBase + numCasas*porcentajeCasa + 
                numHoteles*porcentajeHotel;
        return costeAlquiler;
    }
    
    int calcularPrecioVenta(){
        int precioVenta = (int) (precioCompra + (numCasas + numHoteles) * 
                precioEdificar * factorRevalorizacion);
        return precioVenta;
    }
    
    void cancelarHipoteca(){
        hipotecada = false;
    }
    
    void cobrarAlquiler(int coste){
        throw new UnsupportedOperationException("Sin implementar");
    }
    
    void edificarCasa(){
        numCasas += 1;
    }
    
    void edificarHotel(){
        numCasas = 0;
        numHoteles += 1;
    }
    
    int hipotecar(){
        setHipotecada(true);
        int costeHipoteca = calcularCosteHipotecar();
        return costeHipoteca;
    }
    
    int pagarAlquiler(){
        int costeAlquiler = this.calcularImporteAlquiler();
        propietario.modificarSaldo(-costeAlquiler);
        return costeAlquiler;
    }
    
    boolean propietarioEncarcelado(){
        boolean propietarioExiste = false;
        if(tengoPropietario())
            propietarioExiste = propietario.isEncarcelado();
        
        return propietarioExiste;
    }
    
    boolean tengoPropietario(){
        return propietario != null;
    }
    
    @Override
    public String toString(){
        return "Nombre de la calle: " + getNombre() + "\nPrecio: " +
            getPrecioCompra() + "\nAlquiler Base: " + getAlquilerBase() + 
            "\nFactor de revalorizacion: " + getFactorRevalorizacion() + 
            "\nPrecio de edificar: " + getPrecioEdificar() + "\nHipoteca Base: "
            + getHipotecaBase() + "\n¿Hipotecada?: " + estaHipotecada() + 
            "\nNumero de Hoteles: " + getNumHoteles() + "\nNumero de casas: " 
            + getNumCasas() +  "\n";
    }    
}
