package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map.Entry;

/**
 * Clase de Acceso a Base de datos, abstracta, que permite hacer de forma simple
 * y sin preocuparse de la sintaxis SQL, las operaciones CRUD sobre una bd.
 * 
 * @author Miguel Páramos
 *
 */
public abstract class DAO {

	/**
	 * objeto conexión, desde el que se va a referenciar a la BD. la operativa será
	 * conectar, usar, y desconectar lo antes posible.
	 */
	private static Connection connection;

	/**
	 * Función privada que abre una conexión con un servidor de base de datos. Las
	 * propiedades de la base de datos deben estar definidas en un fichero
	 * ./bdconfig.ini con el siguiente formato: 1º Linea: ip o dns del servidor 2º
	 * Linea: puerto 3º Linea: nombre bd 4º Linea: usuario BD 5º Linea: contraseña
	 * BD
	 * 
	 * @return statement listo para hacer la consulta que necesitemos
	 */
	private static Statement connect() {
		try {
			BufferedReader lector = new BufferedReader(new FileReader("./bdconfig.ini"));
			String ip = lector.readLine();
			int puerto = Integer.parseInt(lector.readLine());
			String nombreBD = lector.readLine();
			String usuario = lector.readLine();
			String password = lector.readLine();
			lector.close();
			connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + puerto + "/" + nombreBD, usuario,
					password);
			return connection.createStatement();
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static int insert(String table, HashMap<String, Object> campos) throws SQLException {
		Statement querier = connect();
		String query = "insert into " + table + " (";
		Iterator it = campos.keySet().iterator();
		while (it.hasNext()) {
			String clave = (String) it.next();
			query += clave + ",";
		}

		query = query.substring(0, query.length() - 1) + ") values (";

		Iterator itv = campos.values().iterator();
		while (itv.hasNext()) {
			Object elemento = itv.next();
			if (elemento.getClass() != String.class && elemento.getClass() != Character.class) {
				query += elemento + ",";
			} else {
				query += "'" + elemento + "',";
			}
		}

		query = query.substring(0, query.length() - 1) + ")";

		if (Config.verboseMode) {
			System.out.println(query);
		}
		int ret = querier.executeUpdate(query);
		disconnect(querier);
		return ret;
	}

	public static int delete(String table, HashMap<String, Object> campos) throws SQLException {
		Statement querier = connect();

		String query = "delete from " + table + " where ";
		Iterator it = campos.entrySet().iterator();
		while (it.hasNext()) {
			Entry actual = (Entry) it.next();
			if (actual.getValue().getClass() != String.class && actual.getValue().getClass() != Character.class) {
				query += actual.getKey() + " = " + actual.getValue() + " and ";
			} else {
				query += actual.getKey() + " = '" + actual.getValue() + "' and ";
			}
		}

		query = query.substring(0, query.length() - 5);

		if (Config.verboseMode) {
			System.out.println(query);
		}
		int ret = querier.executeUpdate(query);
		disconnect(querier);
		return ret;
	}

	/**
	 * función privada que cierra en su interior tanto el statement pasado por
	 * argumentos como la conexión, para dejar libre la entrada a más programas que
	 * quieran conectar a la misma bd
	 * 
	 * @param smt Statement que se va a liberar (cerrar).
	 */
	private static void disconnect(Statement smt) {
		try {
			smt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<Object> consultar(String tabla, LinkedHashSet<String> columnasSacar,
			HashMap<String, Object> restricciones) throws SQLException {
		Statement smt = connect();
		String query = "select ";
		Iterator ith = columnasSacar.iterator();

		while (ith.hasNext()) {
			query += (String) ith.next() + ",";
		}
		query = query.substring(0, query.length() - 1) + " from " + tabla + (restricciones.size() > 0 ? " where " : "");
		// Select email, nombre, pass from cliente where
		Iterator itm = restricciones.entrySet().iterator();

		while (itm.hasNext()) {
			Entry actual = (Entry) itm.next();
			if (actual.getValue().getClass() != String.class && actual.getValue().getClass() != Character.class) {
				query += (String) actual.getKey() + " = " + actual.getValue() + " and ";
			} else {
				query += (String) actual.getKey() + " = '" + actual.getValue() + "' and ";
			}
		}
		if (restricciones.size() > 0) {
			query = query.substring(0, query.length() - 5);
		}
		if (Config.verboseMode) {
			System.out.println(query);
		}
		ResultSet cursor = smt.executeQuery(query);
		ArrayList<Object> fila = new ArrayList<Object>();
		while (cursor.next()) {
			Iterator hsCols = columnasSacar.iterator();
			while (hsCols.hasNext()) {
				String nombreCol = (String) hsCols.next();
				try {
					fila.add(cursor.getInt(cursor.findColumn(nombreCol)));
				} catch (NumberFormatException | SQLException e) {
					fila.add(cursor.getString(cursor.findColumn(nombreCol)));
				}
			}

		}
		disconnect(smt);
		return fila;
	}

	public static int actualizar(String tabla, HashMap<String, Object> datosAModificar,
			HashMap<String, Object> restricciones) throws SQLException {
		String query = "update " + tabla + " set ";
		Iterator itm = datosAModificar.entrySet().iterator();
		while (itm.hasNext()) {
			Entry actual = (Entry) itm.next();
			if (actual.getValue().getClass() != String.class && actual.getValue().getClass() != Character.class) {
				query += actual.getKey() + " = " + actual.getValue() + ",";
			} else {
				query += actual.getKey() + " = '" + actual.getValue() + "',";
			}
		}
		query = query.substring(0, query.length() - 1) + " where ";
		Iterator itr = restricciones.entrySet().iterator();
		while (itr.hasNext()) {
			Entry actual = (Entry) itr.next();
			if (actual.getValue().getClass() != String.class && actual.getValue().getClass() != Character.class) {
				query += actual.getKey() + " = " + actual.getValue() + " and ";
			} else {
				query += actual.getKey() + " = '" + actual.getValue() + "' and ";
			}
		}
		query = query.substring(0, query.length() - 5);

		if (Config.verboseMode) {
			System.out.println(query);
		}
		
		Statement smt = connect();
		int ret = smt.executeUpdate(query);
		disconnect(smt);

		return ret;
	}
}
