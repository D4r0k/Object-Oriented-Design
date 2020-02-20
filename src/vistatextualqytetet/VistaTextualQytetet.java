/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistatextualqytetet;
import java.util.*;
import controladorqytetet.*;
import java.util.ArrayList;
import modeloqytetet.Qytetet;
import modeloqytetet.EstadoJuego;

/**
 *
 * @author DavidRC
 */
public class VistaTextualQytetet {
    private static final ArrayList<String> VCORRECTOS = 
            new ArrayList<String>(Arrays.asList("2", "3", "4"));
    private static OpcionMenu opcion;
    private static ControladorQytetet controlador = ControladorQytetet.getInstance();
    private static Qytetet modelo = Qytetet.getInstance();
    private static final Scanner IN = new Scanner (System.in);
    
    public int elegirCasilla(int opcionMenu){
        ArrayList<Integer> casillas = controlador.obtenerCasillasValidas(opcionMenu);
        
        if(casillas.isEmpty()) return -1;
        else{
            System.out.println("Lista casillas válidas:");
            for(int cas: casillas){
                System.out.println(modelo.getTablero().obtenerCasillaNumero(cas).getTitulo().getNombre()
                + ": " + cas);
            }
            ArrayList<String> casValidas = new ArrayList();
            
            for(int valor: casillas) casValidas.add(Integer.toString(valor));
             
            String mensaje = "\nElija una casilla: ";
            return Integer.parseInt(leerValorCorrecto(casValidas, mensaje));
        }
        
    }
    
    public int elegirOperacion(){
        ArrayList<Integer> op = controlador.obtenerOperacionesJuegoValidas();
        ArrayList<String> valores = new ArrayList();
        
        for(int valor: op){
            valores.add(Integer.toString(valor));
        }
        
        System.out.print("\nOperaciones disponibles:\n");
        for(String val: valores){
            System.out.print(OpcionMenu.values()[Integer.parseInt(val)] + ": " +
                    val + "\n");
        }
        
        String mensaje = "\nSeleccione una opcion: ";
        return Integer.parseInt(leerValorCorrecto(valores, mensaje));
    }
    
    public ArrayList<String> obtenerNombreJugadores() {
       ArrayList<String> nombres = new ArrayList<>();
       
       String mensaje = "Intoduzca numero de jugadores: ";
       String num = leerValorCorrecto(VCORRECTOS, mensaje);
       int n = Integer.parseInt(num);
       
       for (int i = 0; i < n; i++){
           System.out.print("Nombre de jugador"+ (i+1) + ": ");
           String s = IN.nextLine();
           nombres.add(s);
       }
       return nombres;
    }
    
    public String leerValorCorrecto(ArrayList<String> valoresCorrectos, String mensaje){
        boolean esta = false;
        String s;
        do { 
           System.out.print(mensaje);
           s = IN.nextLine();
           for(String aux: valoresCorrectos){
               if(aux.equals(s))
                   esta = true;
           }
        }while(!esta);
        return s;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        VistaTextualQytetet ui = new VistaTextualQytetet();
        System.out.println("---------------BIENVENIDOS A QYTETET------------------\n");
        controlador.setNombreJugadores(ui.obtenerNombreJugadores());
        int operacionElegida, casillaElegida = 0;
        boolean necesitaElegirCasilla;
        
        do{
            operacionElegida = ui.elegirOperacion();
            necesitaElegirCasilla = controlador.necesitaElegirCasilla(operacionElegida);
            if(necesitaElegirCasilla)
                casillaElegida = ui.elegirCasilla(operacionElegida);
            if(!necesitaElegirCasilla || casillaElegida >= 0)
                System.out.println(controlador.realizarOperacion(operacionElegida, 
                        casillaElegida));
        }while(modelo.getEstadoJuego() != EstadoJuego.FIN);
    }
    
}
