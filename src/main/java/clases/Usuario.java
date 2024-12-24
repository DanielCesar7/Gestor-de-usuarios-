package clases;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

import excepciones.ContraseñaInvalidaException;
import excepciones.UsuarioNoExiste;
import utils.DAO;

public class Usuario {

	private String email;
	private String nick;

	// Primer constructor
	public Usuario(String email, String pass) throws SQLException, UsuarioNoExiste, ContraseñaInvalidaException {
		super();

		LinkedHashSet<String> columnasSacar = new LinkedHashSet<String>();
		columnasSacar.add("email");
		columnasSacar.add("pass");
		columnasSacar.add("nick");
		
		HashMap<String, Object> restricciones = new HashMap<String, Object>();
		restricciones.put("email", email);
		//restricciones.put("pass", pass);

		ArrayList<Object> fila = DAO.consultar("user", columnasSacar, restricciones);

		if (fila.size() < 1) {
			throw new UsuarioNoExiste("No existe usuario");
		} else {
				String passString =fila.get(1) +"";

				if (passString.equals(pass)) {

					this.email = (String) fila.get(0)+"";
					this.nick = (String) fila.get(2)+"";

				} else {
					throw new ContraseñaInvalidaException("La contraseña invalida");
				}
			

		}

	}

	// Segundo constructor
	public Usuario(String email, String nick, String pass) throws SQLException {
		super();

		this.email = email;

		HashMap<String, Object> columnasSacar = new HashMap<String, Object>();
		columnasSacar.put("email", email);
		columnasSacar.put("pass", pass);
		columnasSacar.put("nick", nick);

		DAO.insert("user", columnasSacar);

		this.email = email;
		this.nick = nick;
	}

	public String getEmail() {
		return email;
	}

	// Modificar setEmail (Lo pide ejercicio 3)

	public void setEmail(String email) throws SQLException {

		HashMap<String, Object> datosAModificar = new HashMap<String, Object>();
		datosAModificar.put("email", email);

		HashMap<String, Object> restricciones = new HashMap<String, Object>();
		restricciones.put("email", this.email);

		DAO.actualizar("user", datosAModificar, restricciones);
		this.email = email;
	}

	public String getNick() {
		return nick;
	}

	// Modificar setNick (Lo pide ejercicio 3)

	public void setNick(String nick) throws SQLException {

		HashMap<String, Object> datosAModificar = new HashMap<String, Object>();
		datosAModificar.put("nick", nick);

		HashMap<String, Object> restricciones = new HashMap<String, Object>();
		restricciones.put("nick", this.nick);

		DAO.actualizar("user", null, null);
		this.nick = nick;
	}

	public String toString() {
		return "\n\tEmail: " + email + "\n\tNick: " + nick;
	}

}
