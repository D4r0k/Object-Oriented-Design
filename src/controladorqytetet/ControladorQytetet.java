/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorqytetet;
import java.util.*;
import modeloqytetet.*;

/**
 *
 * @author DavidRC
 */
public class ControladorQytetet {
    private static ControladorQytetet CONTROLADORQYTETET = new ControladorQytetet();
    private static int MAX_JUGADORES = 4;
    private ArrayList<String> nombreJugadores = new ArrayList();
    private OpcionMenu opcion;
    private Qytetet modelo = Qytetet.getInstance();
    private EstadoJuego estado;
    
    public static ControladorQytetet getInstance(){
       return CONTROLADORQYTETET;
   }
    
    public boolean necesitaElegirCasilla(int opcionMenu){
        boolean necesita = false;
        if(opcionMenu == opcion.HIPOTECARPROPIEDAD.ordinal() || 
                opcionMenu == opcion.CANCELARHIPOTECA.ordinal() ||
                opcionMenu == opcion.EDIFICARCASA.ordinal() ||
                opcionMenu == opcion.EDIFICARHOTEL.ordinal() ||
                opcionMenu == opcion.VENDERPROPIEDAD.ordinal())
            necesita = true;
        
        return necesita;
    }
    
    public ArrayList<Integer> obtenerCasillasValidas(int opcionMenu){
        ArrayList<Integer> casValidas = new ArrayList();
        OpcionMenu opt = OpcionMenu.values()[opcionMenu];
        
        switch(opt){
            case CANCELARHIPOTECA:
                casValidas = modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(true);
                break;
                
            case VENDERPROPIEDAD:
                casValidas = modelo.obtenerPropiedadesJugador();
                break;   
        }
        
        if(opt == OpcionMenu.EDIFICARCASA || opt == OpcionMenu.EDIFICARHOTEL ||
                opt == OpcionMenu.HIPOTECARPROPIEDAD){
            casValidas = modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(false);
        }
        return casValidas;
    }
    
    public ArrayList<Integer> obtenerOperacionesJuegoValidas(){
        ArrayList<Integer> operaciones = new ArrayList();
        estado = modelo.getEstadoJuego();
        
        if(modelo.getJugadores().isEmpty())
            operaciones.add(opcion.INICIARJUEGO.ordinal());
        else{
            switch(estado){
                case JA_PREPARADO:
                    operaciones.add(opcion.JUGAR.ordinal());
                    break;
                
                case JA_PUEDECOMPRAROGESTIONAR: 
                    operaciones.add(opcion.COMPRARTITULOPROPIEDAD.ordinal());
                    operaciones.add(opcion.CONVERTIRME.ordinal());
                    operaciones.add(opcion.HACERMERESPONSABLE.ordinal());
                    
                case JA_PUEDEGESTIONAR:
                    operaciones.add(opcion.VENDERPROPIEDAD.ordinal());
                    operaciones.add(opcion.HIPOTECARPROPIEDAD.ordinal());
                    operaciones.add(opcion.CANCELARHIPOTECA.ordinal());
                    operaciones.add(opcion.EDIFICARCASA.ordinal());
                    operaciones.add(opcion.EDIFICARHOTEL.ordinal());
                    operaciones.add(opcion.PASARTURNO.ordinal());
                    operaciones.add(opcion.CONVERTIRME.ordinal());
                    operaciones.add(opcion.HACERMERESPONSABLE.ordinal());
                    break;
                    
                case JA_ENCARCELADO:
                    operaciones.add(opcion.INTENTARSALIRCARCELPAGANDOLIBERTAD.ordinal());
                    operaciones.add(opcion.INTENTARSALIRCARCELTIRANDODADO.ordinal());
                    operaciones.add(opcion.PASARTURNO.ordinal());
                    break;
                    
                case JA_ENCARCELADOCONOPCIONDELIBERTAD:
                    operaciones.add(opcion.INTENTARSALIRCARCELTIRANDODADO.ordinal());
                    operaciones.add(opcion.INTENTARSALIRCARCELPAGANDOLIBERTAD.ordinal());
                    break;
                    
                case JA_CONSORPRESA:
                    operaciones.add(opcion.APLICARSORPRESA.ordinal());
                    break;
                    
                case ALGUNJUGADORENBANCARROTA:
                    operaciones.add(opcion.OBTENERRANKING.ordinal());
                    break;
            }
            
            operaciones.add(opcion.MOSTRARJUGADORACTUAL.ordinal());
            operaciones.add(opcion.MOSTRARJUGADORES.ordinal());
            operaciones.add(opcion.MOSTRARTABLERO.ordinal());
            operaciones.add(opcion.TERMINARJUEGO.ordinal());
        }
        return operaciones;
    }
    
    public String realizarOperacion(int opcionElegida, int casillaElegida){
        OpcionMenu opt = OpcionMenu.values()[opcionElegida];
        String mensaje = null;
        
        switch(opt){
            case INICIARJUEGO:
                modelo.inicializarJuego(nombreJugadores);
                mensaje = "\nComienza el juego, empieza " + 
                        modelo.getJugadorActual().getNombre();
                break;
            
            case JUGAR:
                modelo.jugar();
                mensaje = "\nHa salido un " + modelo.getValorDado() + 
                        ", has caido en la casilla:\n" + 
                        modelo.obtenerCasillaJugadorActual().toString();
                break;
                
            case APLICARSORPRESA:
                mensaje = "\nHa salido la carta " + modelo.getCartaActual();
                modelo.aplicarSorpresa();
                break;
                
            case INTENTARSALIRCARCELPAGANDOLIBERTAD:
                boolean hasSalido = 
                        modelo.intentarSalirCarcel(MetodoSalirCarcel.PAGANDOLIBERTAD);
                if(hasSalido == true) mensaje = "\nHas sobornado a los guardias y has salido de la carcel\n" + "Saldo actual " + modelo.getJugadorActual().getSaldo() + "\n";
                else mensaje = "\nParece que tus riquezas no impresionan a nadie, sigues encarcelado";
                break;
                
            case INTENTARSALIRCARCELTIRANDODADO:
                boolean hasSalido1 = 
                        modelo.intentarSalirCarcel(MetodoSalirCarcel.TIRANDODADO);
                if(hasSalido1 == true) mensaje = "\nLa suerte te sonríe querido Padawan, sales de la carcel";
                else mensaje = "\nParece que vas a seguir encerrado por un tiempo, así aprenderás a no jugar con la justicia";
                break;
                
            case COMPRARTITULOPROPIEDAD:
                modelo.comprarTituloPropiedad();
                mensaje = "\nHas comprado el título:\n" + 
                        modelo.obtenerCasillaJugadorActual().toString()
                        + "\nSaldo actual " + modelo.getJugadorActual().getSaldo();
                break;
                
            case HIPOTECARPROPIEDAD:
                modelo.hipotecarPropiedad(casillaElegida);
                mensaje = "\nHas hipotecado la propiedad " + casillaElegida 
                        + ", saldo actual " + modelo.getJugadorActual().getSaldo();
                break;
                
            case CANCELARHIPOTECA:
                modelo.cancelarHipoteca(casillaElegida);
                mensaje = "\nHas cancelado la hipoteca de la propiedad " + 
                        casillaElegida;
                break;
                
            case EDIFICARCASA:
                boolean casa = modelo.edificarCasa(casillaElegida);
                if (casa == true) mensaje = "\nHas construido " + 
                        modelo.getTablero().obtenerCasillaNumero(casillaElegida).getTitulo().getNumCasas() +
                        "/4 casas en " +
                        modelo.getTablero().obtenerCasillaNumero(casillaElegida).getTitulo().getNombre()
                        + ", saldo actual " + modelo.getJugadorActual().getSaldo();
                else mensaje = "\nNo se ha podido construir una casa";
                break;
                
            case EDIFICARHOTEL:
                //System.out.println("He llegado hasta aquí");
                boolean hotel = modelo.edificarHotel(casillaElegida);
                //System.out.println("Y hasta aquí");
                if(hotel == true) mensaje = "\nHas construido " + 
                        modelo.getTablero().obtenerCasillaNumero(casillaElegida).getTitulo().getNumHoteles() + 
                        "/4 hoteles en " + 
                        modelo.getTablero().obtenerCasillaNumero(casillaElegida).getTitulo().getNombre()
                        + ", saldo actual " + modelo.getJugadorActual().getSaldo();
                else mensaje = "\nNo se ha podido construir un hotel";
                break;
                
            case VENDERPROPIEDAD:
                modelo.venderPropiedad(casillaElegida);
                mensaje = "\nHas vendido la propiedad " + casillaElegida 
                        + ", saldo actual " + modelo.getJugadorActual().getSaldo();
                break;
                
            case PASARTURNO:
                modelo.siguienteJugador();
                mensaje = "\nPasas el turno a " + modelo.getJugadorActual().getNombre();
                break;
                
            case OBTENERRANKING:
                modelo.obtenerRanking();
                int i = 0;
                int capitalAnterior = 0;
                mensaje = "\nRanking:";
                for (Jugador j: modelo.getJugadores()){
                    if(capitalAnterior != j.getCapital()) i++;
                    mensaje = mensaje + "\n" + i + "º  " + j.getNombre() + 
                            " con capital " + j.getCapital();
                    capitalAnterior = j.getCapital();
                }
                modelo.setEstadoJuego(EstadoJuego.FIN);
                break;
                
            case TERMINARJUEGO:
                modelo.setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
                mensaje = "\nEl juego ha terminado, la opcion OBTENERRANKING terminará la ejecución del juego,\n"
                        + "además de mostrar el ranking, como su propio nombre indica.";
                break;
                
            case MOSTRARJUGADORACTUAL:
                mensaje = modelo.getJugadorActual().toString();
                break;
               
            case MOSTRARJUGADORES:
                mensaje = modelo.getJugadores().toString();
                break;
                
            case MOSTRARTABLERO:
                mensaje = modelo.getTablero().toString();
                break;
            case CONVERTIRME:
                modelo.convertirJugadorActual();
                break;
            case HACERMERESPONSABLE:
                modelo.hacerResponsableJugadorActual();
                break;
        }
        return mensaje;
    }
    
    public void setNombreJugadores(ArrayList<String> nombreJugadores){
        this.nombreJugadores = nombreJugadores;
    }
}
