import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class Crear {
	public static void main(String[] args){
		String fuente = "PARTIDOS.txt";
		String destino = "poblar_partidos.sql";
		try {
			Scanner or = new Scanner(new File(fuente));
			PrintStream des = new PrintStream(new File(destino));
			String tempAux;
			String temp;
			String div;
			String jor;
			String eq_local;
			String eq_vis;
			String resultado;
			char gol_loc;
			char gol_vis;
			or.useDelimiter("[\t\n]");
			int i = 0;
			while(or.hasNextLine()){
				or.nextLine();
				tempAux = or.next();
				temp = tempAux.substring(0, 4);
				div = or.next().charAt(0) + "";
				if(div.compareTo("P") == 0 || div.compareTo("D") == 0){
					jor = "null";
				}
				else{
					jor = or.next();
				}
				eq_local = or.next();
				eq_vis = or.next();
				resultado = or.next();
				gol_loc = resultado.charAt(0);
				gol_vis = resultado.charAt(2);
				
				//Escribimos
				des.println("INSERT INTO Partidos(goles_loc, goles_vis, temporada, division, jornada, id_partido, eq_loc, eq_vis) VALUES (" + gol_loc + ", " + gol_vis + ", " + temp +", '" +  div +"', " + jor +", " +  i +", '" +  eq_local +"', '" +  eq_vis + "');");
				i++;
			}
			des.close();
			or.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
