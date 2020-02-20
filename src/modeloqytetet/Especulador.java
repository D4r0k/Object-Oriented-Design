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
public class Especulador extends Jugador{
    private int fianza;
    
    protected Especulador(Jugador jugador, int fianza){
        super(jugador);
        this.fianza = fianza;
    }
    
    @Override
    protected Jugador hacermeResponsable(int porcentajeDescuentoAlquiler){
        return this;
    }
    
    @Override
    protected Especulador convertirme(int fianza){
        return this;
    }
    
    @Override
    protected boolean deboIrACarcel(){
        boolean hayCartaLibertad = super.tengoCartaLibertad();
        boolean puedoPagar = this.pagarFianza();
        
        return hayCartaLibertad && !puedoPagar;
    }
    
    @Override
    protected void pagarImpuesto(){
        int impuesto = super.getCasillaActual().getCoste()/2;
        super.modificarSaldo(-impuesto);
        System.out.print("\nSe han pagado " + impuesto +
                "euros en impuestos, su saldo actual es de " + this.getSaldo() +
                "euros.\n");
    }
    
    protected boolean pagarFianza(){
        boolean pagado = false;
        if(super.getSaldo() > fianza){
            super.modificarSaldo(-fianza);
            pagado = true;
        }
        return pagado;
    }
    
    @Override
    protected boolean puedoEdificarCasa(TituloPropiedad titulo){
        boolean puedo = (titulo.getNumCasas() < 8);
        
        if(puedo){
            int costeEdificarCasa = titulo.getPrecioEdificar();
            puedo = super.tengoSaldo(costeEdificarCasa);
        }
        return puedo;
    }
    
    @Override
    protected boolean puedoEdificarHotel(TituloPropiedad titulo){
        boolean puedo = (titulo.getNumCasas() <= 4 && titulo.getNumHoteles() < 8);
        
        if(puedo){
            int costeEdificarHotel = titulo.getPrecioEdificar();
            puedo = super.tengoSaldo(costeEdificarHotel);
        }
        return puedo;
    }
    
    @Override
    public String toString(){
        return super.toString();
    }
}

