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
public class Responsable extends Jugador{
    public static final int PORCENTAJEINCREMENTOIMPUESTO = 10;
    public int porcentajeDescuentoAlquiler;
    
    protected Responsable(Jugador jugador, int porcentajeincrementoimpuesto){
        super(jugador);
        this.porcentajeDescuentoAlquiler = porcentajeincrementoimpuesto;
    }
    
    @Override
    protected void pagarImpuesto(){
        int impuesto = super.getCasillaActual().getCoste() + (super.getCasillaActual().getCoste()*PORCENTAJEINCREMENTOIMPUESTO / 100);
        super.modificarSaldo(-impuesto);
        System.out.print("\nSe han pagado " + impuesto +
                "euros en impuestos, su saldo actual es de " + this.getSaldo() +
                "euros.\n");
    }
    
    @Override
    protected Jugador convertirme(int fianza){
        Jugador yo = new Especulador(this, fianza);
        return yo;
    }
    
    
    @Override
        void pagarAlquiler() {
        int costeAlquiler = ((Calle) super.getCasillaActual()).pagarAlquiler();
        int descuento = porcentajeDescuentoAlquiler/100;
        int costeAlquilerFinal = costeAlquiler*descuento;
        this.modificarSaldo(-costeAlquilerFinal);
    }
        
    @Override
    public String toString(){
        return super.toString();
    }
}
