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
public class Sorpresa {
   private String texto;
   private TipoSorpresa tipo;
   private int valor;
   
   Sorpresa(String texto, int valor, TipoSorpresa tipo){
      this.texto = texto;
      this.tipo = tipo;
      this.valor = valor;
   }
   
   String GetTexto() {
       return texto;
   }
   
   TipoSorpresa GetTipo() {
       return tipo;
   }
   
   int GetValor() {
       return valor;
   }
   
   @Override
   public String toString() {
      return "Sorpresa:\n{" + "texto = " + texto + "\n valor=" + 
             //Integer.toString(valor) + ", tipo=" + tipo + "}";
             valor + "\n tipo=" + tipo + "}";
             //Aquí arriba probé ambas formas de imprimir la variable valor,
             //al ver que ambas funcionaban igual, me quedé con la más simple
   } 
}
