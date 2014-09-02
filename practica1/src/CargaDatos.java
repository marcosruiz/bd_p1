import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Properties;

import au.com.bytecode.opencsv.CSVReader;

public class CargaDatos {

	private OracleTemplate ot;
	public CargaDatos(OracleTemplate ot) {
		this.ot = ot;
	}

	public void leeArbitros(String archivo) throws IOException {
		CSVReader reader = new CSVReader(new InputStreamReader(
				new FileInputStream(archivo), "UTF-16"), '\t');
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			String edad = nextLine[16].trim();
			if (edad.length() == 0) {
				// En Eurocopa2012.txt los arbitros no tienen "edad"
				String nombre = nextLine[11].trim();
				String tipoArbitro = nextLine[19].trim();
				insertarArbitro(nombre, tipoArbitro);
			}
		}
		reader.close();
	}


	public static void main(String args[]) {
		OracleTemplate q = null;
		Properties properties = new Properties();
		try {
			properties.load(OracleTemplate.class
					.getResourceAsStream("practica1.properties"));
			q = new OracleTemplate(properties.getProperty("database.host"),
					properties.getProperty("database.port"),
					properties.getProperty("database.sid"),
					properties.getProperty("database.user"),
					properties.getProperty("database.password"));
			q.connect();
			System.out.println("Conectado a " + q);
			String fuente = "Eurocopa2012.txt";

			EjemploCargaDatos ecd = new EjemploCargaDatos(q);
			ecd.procesarArbitros(fuente);
			
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (InstantiationException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (IllegalAccessException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (q != null) {
				q.disconnect();
			}
		}
	}

	private void procesarArbitros(String fuente) throws IOException {
		creaTablaArbitros();
		leeArbitros(fuente);
		listarArbitros();
	}

	private void listarArbitros() {
		ot.executeQuery("SELECT * FROM ARBITROS_EJEMPLO");
	}

	private void creaTablaArbitros() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ARBITROS_EJEMPLO(");
		sb.append("NOMBRE VARCHAR(30) PRIMARY KEY,");
		sb.append("TIPO VARCHAR(30)");
		sb.append(")");
		ot.executeSentence(sb.toString());
	}

	private void insertarArbitro(String nombre, String tipoArbitro) {
		System.out.println("Insertando "+nombre+" - "+tipoArbitro);
		ot.executeSentence("INSERT INTO ARBITROS_EJEMPLO(NOMBRE,TIPO) VALUES (?,?)", nombre, tipoArbitro);
	}
	
	
}
