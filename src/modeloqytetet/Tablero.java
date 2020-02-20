/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;
import java.util.ArrayList;


/**
 *
 * @author DavidRC
 */
public class Tablero {
    private ArrayList<Casilla> casillas;
    private Casilla carcel;
    
    Tablero(){
        Inicializar();
    }
    
    boolean esCasillaCarcel(int numeroCasilla){
        return numeroCasilla == carcel.getNumeroCasilla();
    }

    public ArrayList<Casilla> getCasillas() {
        return casillas;
    }

    Casilla getCarcel() {
        return carcel;
    }
    
    private void Inicializar(){
        casillas = new ArrayList<>();
        carcel = new OtraCasilla(5, TipoCasilla.CARCEL);
        
        //Declaracion de los titulos a usar
        TituloPropiedad mordor;
        TituloPropiedad urss;
        TituloPropiedad pedro_antonio;
        TituloPropiedad valle_caidos;
        TituloPropiedad casa_pepe;
        TituloPropiedad genova;
        TituloPropiedad invernalia;
        TituloPropiedad corea_norte;
        TituloPropiedad belgica;
        TituloPropiedad stalingrado;
        TituloPropiedad vaticano;
        TituloPropiedad jerusalem;
           
        //Inicializacion de dichos titulos (hecho por separado porque me gusta
        //como queda en 3 bloques)
        pedro_antonio = new TituloPropiedad("Pedro Antonio", 500, 50, 0.0f, 
                250, 150);
        urss = new TituloPropiedad("URSS", 550, 52, -0.1f, 260, 175);
        invernalia = new TituloPropiedad("Invernalia", 600, 65, 0.05f, 
                375, 500);
        corea_norte = new TituloPropiedad("Corea del Norte", 650, 50, -0.15f, 
                300, 450);
        casa_pepe = new TituloPropiedad("Casa Pepe", 700, 70, -0.05f, 300, 225);
        belgica = new TituloPropiedad("Bélgica", 733, 75, -0.05f, 400, 600);
        stalingrado = new TituloPropiedad("Stalingrado", 766, 82, 0.10f, 
                500, 750);
        genova = new TituloPropiedad("Génova", 800, 80, 0.2f, 550, 800);
        vaticano = new TituloPropiedad("El Vaticano", 850, 85, 0.20f, 610, 800);
        valle_caidos = new TituloPropiedad("Valle de los caidos", 900, 90, 0.1f, 
            650, 900);
        mordor = new TituloPropiedad("Mordor", 950, 95, 0.12f, 700, 950);
        jerusalem = new TituloPropiedad("Jerusalem", 1000, 100, 0.20f, 750,
                1000);
        
        //Inclusión de las distintas casillas en el array
        casillas.add(new OtraCasilla(0, TipoCasilla.SALIDA));
        casillas.add(new Calle(1, pedro_antonio));
        casillas.add(new Calle(2, urss));
        casillas.add(new OtraCasilla(3, TipoCasilla.SORPRESA));
        casillas.add(new Calle(4,invernalia));
        casillas.add(carcel);
        casillas.add(new Calle(6, corea_norte));
        casillas.add(new Calle(7, casa_pepe));
        casillas.add(new OtraCasilla(8, TipoCasilla.SORPRESA));
        casillas.add(new Calle(9, belgica));
        casillas.add(new OtraCasilla(10, TipoCasilla.PARKING));
        casillas.add(new Calle(11, stalingrado));
        casillas.add(new OtraCasilla(12, TipoCasilla.SORPRESA));
        casillas.add(new Calle(13, genova));
        casillas.add(new Calle(14, vaticano));
        casillas.add(new OtraCasilla(15, TipoCasilla.JUEZ));
        casillas.add(new Calle(16, valle_caidos));
        casillas.add(new Calle(17, mordor));
        casillas.add(new Calle(18, jerusalem));
        casillas.add(new OtraCasilla(19, TipoCasilla.IMPUESTO));
    }
    
    Casilla obtenerCasillaFinal(Casilla casilla, int desplazamiento){
        return casillas.get((casilla.getNumeroCasilla() + desplazamiento)
                %casillas.size());
    }
    
    public Casilla obtenerCasillaNumero(int numeroCasilla){
        return casillas.get(numeroCasilla);
    }
    
    @Override
    public String toString(){
        return casillas+ "";
    }
}
