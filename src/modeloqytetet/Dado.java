/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;
import java.util.*;

/**
 *
 * @author DavidRC
 */
class Dado {
    int valor;
    private Random aleatorio = new Random(System.currentTimeMillis());
    private static final Dado DADO = new Dado();
    
    private Dado() {
    }
    
    public static Dado getInstance() {
        return DADO;
    }
    
    private static class DadoHolder {

        private static final Dado INSTANCE = new Dado();
    }
    
    int tirar(){
        aleatorio.setSeed(System.currentTimeMillis());
        //Generar valor aleatorio entre 1 y 6
        valor = 1 + aleatorio.nextInt(6);
        //Refrescar valores aleatorios para que los resultados 
        //no sean reproducibles
        
        return valor;
    }
    
    public int getValor(){
        return valor;
    }
}
