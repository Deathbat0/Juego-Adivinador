//DeathbatO

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Scanner;

/**
 * Programa Adivinador utilizando enumerados, registros y rule switch
 * @author DeathbatO
 */
public class Adivinador{

	public static int MaxIntentosFacil = 15; //Cantidad maxima de intentos en dificultad facil
	public static int MaxIntentosDificil = 10; //Cantidad maxima de intentos en dificultad dificil
	public static int NumeroMaximo = 100; //Numero maximo a adivinar
	
	public static enum Dificultad{FACIL, DIFICIL} //Dificultades disponibles
	public static enum Estado{JUGANDO, GANO, PERDIO} //Los estados posibles del juego
	public static enum Resultado{ES_MAYOR, ES_IGUAL, ES_MENOR}//Posibles resultados al jugar
	
	/**
	 * Modelo de juego: <br>
	 * <ul><li>Dificultad: Arreglo de 1 celda conteniendo la dificultad del juego.</li>
	 * <li>MaxIntentos: Cantidad maxima de intentos con que se inicio el juego segun dificultad</li>
	 * <li>Estado: Arreglo de 1 celda conteniendo el estado actual del juego.</li>
	 * <li>NumeroSecreto: El numero secreto generado aleatoriamente entre 1 y NumeroMaximo</li>
	 * <li>IntentoActual: El intento actual del jugador</li>
	 * </ul>
	 */
	public static record Juego(
		Dificultad[] dificultad,
		AtomicInteger maxIntentos,
		Estado[] estado,
		AtomicInteger numeroSecreto,
		AtomicInteger intentoActual
	) {}
	/**
	 * Inicializa un Juego con los valores correctos acorde a la dificultad asignada
	 * El estado inicial sera Jugando y el intento actual sera 0. Tambien se generara
	 * un numero aleatorio entre 1 y NumeroMaximo.
	 * @param d - La dificultad con que se iniciara el juego
	 * @return Un Juego listo para comenzar
	 * @see Dificultad
	 * @see Juego
	 */
	public static Juego iniciarJuego(Dificultad d){
		Juego j = new Juego(new Dificultad[1], new AtomicInteger(),new Estado[1], new AtomicInteger(), new AtomicInteger());
		j.dificultad[0] = d;
		if(d == Dificultad.FACIL){
			j.maxIntentos.set(MaxIntentosFacil);
		}else{
			j.maxIntentos.set(MaxIntentosDificil);
		}
		
		j.estado[0] = Estado.JUGANDO;
		j.numeroSecreto.set(new Random().nextInt(NumeroMaximo)+1);
		return j;
	}
	/**
	 * Si n es igual que el numero secreto, se retorna ES_IGUAL, si el numero 
	 * secreto es menor que n se retorna ES_MENOR, y si no, ES_MAYOR. Ademas se
	 * aumenta el numero de intentos y se cambia el estado del juego por GANO si 
	 * el usuario adivino el numero, PERDIO si agototo los intentos. El parametro
	 * mensaje obtendra un mensaje adecuado en funcion de lo que ocurra.
	 * @param n - El numero ingresado por el usuario.
	 * @param j - El Juego 
	 * @param mens - El mansaje se obtendra aqui.
	 * @return ES_IGUAL, ES_MENOR, ES_MAYOR en funcion del numero secreto comparado con n.
	 */
	public static Resultado verificarNumero(int n, Juego j, StringBuilder mens) {
		j.intentoActual.set(j.intentoActual.get()+1);
		mens.delete(0, mens.length());
		Resultado res;
		
		if(n == j.numeroSecreto.get()){
			j.estado[0] = Estado.GANO;
			mens.append("GANASTE !!!");
			res = Resultado.ES_IGUAL;
		}else if(j.numeroSecreto.get() < n){
			mens.append("El numero a adivinar es menor ");
			res = Resultado.ES_MENOR;
		}else{
			mens.append("El numero a adivinar es mayor.");
			res = Resultado.ES_MAYOR;
		}
		
		if(j.intentoActual.get() == j.maxIntentos.get()){
			mens.delete(0, mens.length());
			mens.append("PERDISTE !!! El numero era ").append(j.numeroSecreto).append(".");
			j.estado[0] = Estado.PERDIO;
		}
		
		return res;
	}
	
	/**
	 * Logica del juego
	 * @param args - Argumentos de la linea de comandos (No se usa)
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dif;
		var sc = new Scanner(System.in);
		int numeroUsuario;
		Juego juego;
		Resultado resultado;
		StringBuilder mens = new StringBuilder();
		
		System.out.print("Elige una dificultad [F]Facil/[D]Dificil >> ");
		dif = sc.nextLine().toUpperCase();
		
		while((dif.length() != 1) || (dif.charAt(0) != 'F' && dif.charAt(0) != 'D')){
			System.out.print("ERROR: Ingresa F o D >> ");
			dif = sc.nextLine().toUpperCase();
		}
		
		juego = switch(dif){
			case "F" -> iniciarJuego(Dificultad.FACIL);
			case "D" -> iniciarJuego(Dificultad.DIFICIL);
			default -> iniciarJuego(Dificultad.FACIL);
		};
		
		System.out.print("\nDispones de "+juego.maxIntentos+" intentos para adivinar.\n");
		
		do{
			System.out.print("\nIntento "+(juego.intentoActual.get()+1)+" >> ");
			numeroUsuario = sc.nextInt(); sc.nextLine();
			resultado = verificarNumero(numeroUsuario, juego, mens);
			System.out.print(mens);
		}while(juego.estado[0] == Estado.JUGANDO);
		
		sc.close();
	}

}
