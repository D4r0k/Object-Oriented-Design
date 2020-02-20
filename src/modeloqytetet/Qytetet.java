/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;
import  java.util.*;

/**
 *
 * @author DavidRC
 */
public class Qytetet{
   private static final Qytetet QYTETET = new Qytetet();
   public static final int MAX_JUGADORES = 4;
   static final int NUM_SORPRESAS = 10;
   public static final int NUM_CASILLAS = 20;
   static final int PRECIO_LIBERTAD = 200;
   static final int SALDO_SALIDA = 1000;
   private ArrayList <Sorpresa> mazo; 
   private Tablero tablero;
   private Sorpresa cartaActual;
   private ArrayList <Jugador> jugadores;
   private Jugador jugadorActual;
   private Dado dado = Dado.getInstance();
   private EstadoJuego estadoJuego;
   
   private Qytetet(){
       mazo = new ArrayList<>();
       jugadores = new ArrayList<>();
   }
   
   public static Qytetet getInstance(){
       return QYTETET;
   }
   
   void actuarSiEnCasillaEdificable(){
       boolean deboPagar = jugadorActual.deboPagarAlquiler();
       if (deboPagar)
           jugadorActual.pagarAlquiler();
       
       Casilla casilla = obtenerCasillaJugadorActual();
       boolean tengoPropietario = casilla.tengoPropietario();
       
       if(tengoPropietario)
           setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
       else
           setEstadoJuego(EstadoJuego.JA_PUEDECOMPRAROGESTIONAR);
   }
   
   void actuarSiEnCasillaNoEdificable(){
       setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
       Casilla casillaActual = jugadorActual.getCasillaActual();
       
       if(casillaActual.getTipo() == TipoCasilla.IMPUESTO)
           jugadorActual.pagarImpuesto();
       else if(casillaActual.getTipo() == TipoCasilla.JUEZ)
           encarcelarJugador();
       else if(casillaActual.getTipo() == TipoCasilla.SORPRESA){ 
           cartaActual = mazo.remove(0);
           setEstadoJuego(EstadoJuego.JA_CONSORPRESA);
       }
   }
   
   //Preguntar por este metodo
   public void aplicarSorpresa(){
       setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
       if(cartaActual.GetTipo() == TipoSorpresa.SALIRCARCEL)
           jugadorActual.setCartaLibertad(cartaActual);
       else{
           mazo.add(cartaActual);
           if(cartaActual.GetTipo() == TipoSorpresa.PAGARCOBRAR){
                jugadorActual.modificarSaldo(cartaActual.GetValor());
                if(jugadorActual.getSaldo()<0)
                    setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
           }
           if(cartaActual.GetTipo() == TipoSorpresa.IRACASILLA){
                int valor = cartaActual.GetValor();
                boolean casillaCarcel = tablero.esCasillaCarcel(valor);
           
                if(casillaCarcel)
                    encarcelarJugador();
                else
                    mover(valor);
           }
           else if(cartaActual.GetTipo() == TipoSorpresa.PORCASAHOTEL){
                int cantidad = cartaActual.GetValor();
                int numTotal = jugadorActual.cuantasCasasHotelesTengo();
                jugadorActual.modificarSaldo(cantidad*numTotal);
           
                if(jugadorActual.getSaldo()<0)
                    setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
           }
           else if(cartaActual.GetTipo() == TipoSorpresa.PORJUGADOR){
                for(Jugador jugador: jugadores){
                    if(jugador != jugadorActual){
                        jugador.modificarSaldo(cartaActual.GetValor());
                        jugadorActual.modificarSaldo(-cartaActual.GetValor());
                    }
                }
            }
           else if(cartaActual.GetTipo() == TipoSorpresa.CONVERTIRME){
               convertirJugadorActual();
           }
           else if(cartaActual.GetTipo() == TipoSorpresa.HACERMERESPONSABLE){
               hacerResponsableJugadorActual();
           }
       }      
   }
   
   public boolean cancelarHipoteca(int numCasilla){
       boolean cancelada = false;
       Casilla casilla = tablero.obtenerCasillaNumero(numCasilla);
       TituloPropiedad titulo = casilla.getTitulo();
       
       if(titulo.getPropietario() == jugadorActual && titulo.isHipotecada())
           cancelada = jugadorActual.cancelarHipoteca(titulo);
       
       if(cancelada)
           setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
       
       return cancelada;
   }
   
   public boolean comprarTituloPropiedad(){
       boolean comprado = jugadorActual.comprarTituloPropiedad();
       
       if(comprado)
           setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
       
       return comprado;
   }
   
   public boolean edificarCasa(int numCasilla){
       boolean edificada = false;
       Casilla casilla = tablero.obtenerCasillaNumero(numCasilla);
       TituloPropiedad titulo = casilla.getTitulo();
       
       if(titulo.getPropietario() == jugadorActual)
           edificada = jugadorActual.edificarCasa(titulo);
       
       
       if(edificada)
           setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
       
       return edificada;       
   }
   
   public boolean edificarHotel(int numCasilla){
       boolean edificada = false;
       Casilla casilla = tablero.obtenerCasillaNumero(numCasilla);
       TituloPropiedad titulo = casilla.getTitulo();
       
       if(titulo.getPropietario() == jugadorActual)
           edificada = jugadorActual.edificarHotel(titulo);
       
       
       if(edificada)
           setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
       
       return edificada;
   }
   
   private void encarcelarJugador(){
       if(jugadorActual.deboIrACarcel()){
           Casilla casillaCarcel = tablero.getCarcel();
           jugadorActual.irACarcel(casillaCarcel);
           this.setEstadoJuego(EstadoJuego.JA_ENCARCELADO);
       }
       else{
           if(jugadorActual.tengoCartaLibertad()){
               System.out.println("Tu carta de libertad te salva de ir a la carcel");
               Sorpresa carta = jugadorActual.devolverCartaLibertad();
               this.mazo.add(carta);
           }
           this.setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
       }
   }
   
   public Sorpresa getCartaActual(){
       return cartaActual;
   }

   public ArrayList<Jugador> getJugadores() {
       return jugadores;
   }

   public Jugador getJugadorActual() {
       return jugadorActual;
   }

   Dado getDado() {
       return dado;
   }
   
   public int getValorDado(){
       return dado.getValor();
   }
   
   public Tablero getTablero() {
       return tablero;
   }
   
   ArrayList<Sorpresa> GetMazo() {
       return mazo;
   }
   
   public EstadoJuego getEstadoJuego(){
       return estadoJuego;
   }
   
   public void hipotecarPropiedad(int numCasilla){
       Casilla casilla = tablero.obtenerCasillaNumero(numCasilla);
       TituloPropiedad titulo = casilla.getTitulo();
       jugadorActual.hipotecarPropiedad(titulo);
   }

   private void inicializarCartasSorpresa() {
        mazo.add(new Sorpresa("Dime, ¿has oido hablar de la historia de Darth Sevilla\n"
                + " el Especulador? Lo suponía... Un Agente Inmobiliario nunca te\n"
                + " la contaria. Es una leyenda Especuladora. Darth Sevilla era un\n"
                + " Lord Tenebroso de la Especulacion. Era tan poderoso y tan sabio,\n"
                + " que podía utilizar la Fuerza Inmobiliaria para influir en los precios\n"
                + " y crear... inflación. Era tal su conocimiento del Lado Oscuro,\n"
                + " que incluso podía llegar a evitar que los seres que le importaban\n"
                + " pagasen precios abusivos. El Lado Oscuro de la Inmobiliaria es un\n"
                + " camino que puede aportar facultades y dones que muchos no dudan\n"
                + " en calificar de antinaturales. Darth Sevilla llegó a ser un\n"
                + " hombre tan poderoso, que su único incesante temor era perder\n"
                + " el poder que, por supuesto, perdió. Cometió el error de transmitir\n"
                + " a su aprendiz todos sus conocimientos. Un día su aprendiz le\n"
                + " cobró precios abusivos por un pisucho. Es irónico. Era capaz\n"
                + " de salvar de la inflación a cualquiera menos a si mismo. \n"
                + " ¿Que si es posible aprender ese poder? Lo es, pero no de un\n"
                + " Agente Inmobiliario...", 5000, TipoSorpresa.CONVERTIRME));
        mazo.add(new Sorpresa ("¡Tú eras el elegido! ¡El que destruiría a los especuladores,\n"
                + " no el que se uniría a ellos! ¡El que vendría a traer el equilibrio\n"
                + " al mercado inmobiliario, no a hundirlo en la burbuja! Te has convertido\n"
                + " en lo que juraste destruir.", 3000, TipoSorpresa.CONVERTIRME));
        mazo.add(new Sorpresa ("¡Enhorabuena, has aprobado todas las asignaturas\n"
               + " de este curso! Para celebrarlo te gastas 500 euros \n"
               + " en fiestas", -500, TipoSorpresa.PAGARCOBRAR));
        mazo.add(new Sorpresa ("¡Uy! Parece que te han metido en la cárcel, \n"
               + " ¿ya has vuelto a intentar financiar un partido de forma\n"
               + " ilegal?, lo que la gente no sabe es que eres rico, por lo que\n"
               + " solo te caen un par de azotes en el culete",0 ,
               TipoSorpresa.SALIRCARCEL));
        mazo.add(new Sorpresa ("¡Vaya! Parece que te han mandado a sacar los\n"
               + " restos de Franco del Valle de los Caidos, ve a la casilla 16",
               16, TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa ("Acabas de pasar despeñaperros y por alguna \n"
               + " extraña razón, decides pasar por Casa Pepe. Resulta que están\n"
               + " pidiendo donativos para realizar una manifestación en contra\n"
               + " de sacar a Franco del Valle, te toca pagar o acabar en alguna \n"
               + " cuneta. Esto no es Twitter, te toca pagar", -100,
               TipoSorpresa.PORCASAHOTEL));
        mazo.add(new Sorpresa ("¡Wow! Parece que esa idea descabellada de cobrar\n"
               + " un impuesto a la gente por respirar a resultado efectiva,\n"
               + " como resultado, tu partido es más rico y tu te llevas una \n"
               + " conveniente comision de esos impuestos por tus esfuerzos",
               20, TipoSorpresa.PORJUGADOR));
        mazo.add(new Sorpresa("¡Te has pasado y te imputan por injurias a la \n"
                + " corona! Intentas huir a Bélgica. Sale mal.", 
                tablero.getCarcel().getNumeroCasilla(), 
                TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa("¡Ha estallado la revolución! La colectivización \n"
                + " forzosa te obliga a repartir tus riquezas con el \n"
                + " proletariado. Porque no eres un contrarevolucionario, \n"
                + " ¿verdad?", -100, TipoSorpresa.PORJUGADOR));
        mazo.add(new Sorpresa("Tu hobby como cazarrecompensas a media jornada \n"
                + " ha empezado a dar sus frutos. Un pueblo agradecido te \n"
                + " recompensa por dar caza a aquel Ballrog de plutonio de \n"
                + " nivel 25.", 400, TipoSorpresa.PAGARCOBRAR));
        mazo.add(new Sorpresa("¡Rápido, no hay testigos! Si tiras el cadáver al\n"
                + " pantano y lavas la escena del crimen a conciencia nadie \n"
                + " tiene por que saber lo que le hiciste a aquella prostituta.\n"
                + " ¡Huye rápido a esta casilla para tener coartada!", 9, 
                TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa("Tu alianza con la mafia italoamericana no podría\n"
                + " ir mejor. Tu idea de cederles algunos locales en tus \n"
                + " propiedades para que controlen el mercado local mediante el\n"
                + " uso desproporcionado de herramientas disuasorias te \n"
                + " proporciona grandes beneficios.", 70, 
                TipoSorpresa.PORCASAHOTEL));
        mazo.add(new Sorpresa("Te haces responsable mensaje1.", 30, TipoSorpresa.HACERMERESPONSABLE));
        mazo.add(new Sorpresa("Te haces responsable mensaje2.", 50, TipoSorpresa.HACERMERESPONSABLE));
   }

    public void inicializarJuego(ArrayList<String> nombres){ 
       inicializarJugadores(nombres);
       inicializarTablero();       
       inicializarCartasSorpresa();
       Random shuf = new Random(System.currentTimeMillis());
       Collections.shuffle(mazo, shuf);
       salidaJugadores();
   }
    
   private void inicializarJugadores(ArrayList<String> nombres){
       for (String jugador: nombres){
           jugadores.add(new Jugador(jugador));
       }
   }
   
   private void inicializarTablero() {
       tablero = new Tablero();
   }
   
   public boolean intentarSalirCarcel(MetodoSalirCarcel metodo){
       if(metodo == MetodoSalirCarcel.TIRANDODADO){
           int resultado = dado.tirar();
           if(resultado >= 5)
               jugadorActual.setEncarcelado(false);
       } 
       else if(metodo == MetodoSalirCarcel.PAGANDOLIBERTAD)
           jugadorActual.pagarLibertad(PRECIO_LIBERTAD);
       
       boolean encarcelado = jugadorActual.isEncarcelado();
       
       if(encarcelado)
           this.setEstadoJuego(EstadoJuego.JA_ENCARCELADO);
       else
           this.setEstadoJuego(EstadoJuego.JA_PREPARADO);
       
       return encarcelado;
   }
   
   public void jugar(){
       int tirada = tirarDado();
       int casillaFinal = tablero.obtenerCasillaFinal
                  (jugadorActual.getCasillaActual(), tirada).getNumeroCasilla();
       this.mover(casillaFinal);
   }
   
   void mover(int numCasillaDestino){
       Casilla casillaInicial = jugadorActual.getCasillaActual();
       Casilla casillaFinal = tablero.obtenerCasillaNumero(numCasillaDestino);
       jugadorActual.setCasillaActual(casillaFinal);
       
       if(numCasillaDestino < casillaInicial.getNumeroCasilla())
           jugadorActual.modificarSaldo(SALDO_SALIDA);
       
       if(casillaFinal.soyEdificable())
           this.actuarSiEnCasillaEdificable();
       else
           this.actuarSiEnCasillaNoEdificable();
   }
   
   public Casilla obtenerCasillaJugadorActual(){
       Casilla casilla = jugadorActual.getCasillaActual();
       return casilla;
   }
   
   public ArrayList<Casilla> obtenerCasillasTablero(){
       throw new UnsupportedOperationException("Sin implementar");
   }
   
   public ArrayList<Integer> obtenerPropiedadesJugador(){
       ArrayList<Integer> casEnPropiedad = new ArrayList<>();
       if(jugadorActual.getPropiedades() != null){
           int i = 0;
       
           for(TituloPropiedad propiedad: jugadorActual.getPropiedades()){
               for(Casilla casilla: tablero.getCasillas()){
                   if(casilla.getTitulo() == propiedad){
                      casEnPropiedad.add(casilla.getNumeroCasilla());
                      i++;
                   }
               }
           }
       }
       return casEnPropiedad;
           
   }
   
   public ArrayList<Integer> obtenerPropiedadesJugadorSegunEstadoHipoteca
   (boolean estadoHipoteca){
       ArrayList<Integer> casEnPropiedad = new ArrayList<>();
       
       if(jugadorActual.getPropiedades() != null){
           int i = 0;
       
           for(TituloPropiedad propiedad: 
                   jugadorActual.obtenerPropiedades(estadoHipoteca)){
               for(Casilla casilla: tablero.getCasillas()){
                   if(casilla.getTitulo() == propiedad){
                      casEnPropiedad.add(casilla.getNumeroCasilla());
                      i++;
                   }
               }
           }
       }
       
       return casEnPropiedad;
   }
   
   public void obtenerRanking(){       
       //Preguntar si está correcto
       Collections.sort(jugadores); 
   }
   
   public int obtenerSaldoJugadorActual(){
       return jugadorActual.getSaldo();
   }
   
   private void salidaJugadores(){
       Random primerTurno = new Random(System.currentTimeMillis());
       for(Jugador jugador: jugadores){
           jugador.setCasillaActual(tablero.obtenerCasillaNumero(0));
       }
       jugadorActual = jugadores.get(primerTurno.nextInt(jugadores.size()));
       estadoJuego = EstadoJuego.JA_PREPARADO;
   }
   
   private void setCartaActual(Sorpresa cartaActual){
       this.cartaActual = cartaActual;
   }

   public void setEstadoJuego(EstadoJuego estadoJuego) {
       this.estadoJuego = estadoJuego;
   }
   
   public void siguienteJugador(){
       jugadorActual = jugadores.get((jugadores.indexOf(jugadorActual)+1)
               %jugadores.size());
       
       if(jugadorActual.isEncarcelado())
           estadoJuego = EstadoJuego.JA_ENCARCELADO;
       else
           estadoJuego = EstadoJuego.JA_PREPARADO;
   }
   
   int tirarDado(){
       return dado.tirar();
   }
   
   public void venderPropiedad(int numCasilla){
       Casilla casilla = tablero.obtenerCasillaNumero(numCasilla);
       jugadorActual.venderPropiedad(casilla);
   }
   
   //¿Protected, private o public?
   public boolean jugadorActualEnCalleLibre(){
       return jugadorActual.getCasillaActual().soyEdificable() &&
               jugadorActual.getCasillaActual().tengoPropietario();
   }
   
   //¿Protected, private, public?
   public boolean jugadorActualEncarcelado(){
       return jugadorActual.isEncarcelado();
   }  
   
   private boolean sustituirJugadorActual(Jugador otroJugador){
       int posicionActual = jugadores.indexOf(jugadorActual);
       if (jugadorActual == otroJugador){
           return false;
       }else{
           jugadores.set(posicionActual, otroJugador);
           jugadorActual = otroJugador;
           return true;                   
       }
   }
   
   public boolean convertirJugadorActual(){
       Jugador nuevo = jugadorActual.convertirme(4000);
       if (sustituirJugadorActual(nuevo)){
           return true;
       }else{
           return false;
       }
   }
   
      public boolean hacerResponsableJugadorActual(){
       Jugador nuevo = jugadorActual.hacermeResponsable(20);
       if (sustituirJugadorActual(nuevo)){
           return true;
       }else{
           return false;
       }
   }
   
}
