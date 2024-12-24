package principal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Scanner;

import clases.Usuario;
import excepciones.ContraseñaInvalidaException;
import excepciones.UsuarioNoExiste;
import utils.DAO;

public class Principal {

	public static void main(String[] args) {

		Usuario usuario = null;
		Scanner sc = new Scanner(System.in);
		byte opcion = -1;
		do {
			System.out.println("Dime la opcion que quiere realizar:" + "\n \t 0 - salir " + "\n\t 1 - Registro"
					+ "\n\t 2 - Login" + "\n\t 3 - Ver Log");
			opcion = Byte.parseByte(sc.nextLine());
			switch (opcion) {
			case 1:
				System.out.println("Dime email");
				String email = sc.nextLine();
				System.out.println("Dime contraseña");
				String pass = sc.nextLine();
				System.out.println("Dime el nombre");
				String nick = sc.nextLine();

				try {
					usuario = new Usuario(email, nick, pass);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				System.out.println("Usuario registrado");

				try {
					FileWriter usuarioRegistrado = new FileWriter("Usuarios.log", true);
					usuarioRegistrado.write("Usuario " + usuario.getEmail() + " registrado con éxito en: "
							+ LocalDateTime.now().toString() + "\n");
					usuarioRegistrado.flush();
					usuarioRegistrado.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case 2:

				boolean todoBien = false;

				while (todoBien == false) {
					try {
						System.out.println("Dime email");
						email = sc.nextLine();
						System.out.println("Dime contraseña");
						pass = sc.nextLine();
						FileWriter usuarioRegistrado;
						usuario = new Usuario(email, pass);
						usuarioRegistrado = new FileWriter("Usuarios.log", true);
						usuarioRegistrado.write(
								"Usuario " + email + " logeado con éxito en: " + LocalDateTime.now().toString() + "\n");
						usuarioRegistrado.flush();
						usuarioRegistrado.close();
						System.out.println("Se lo ha logeado con exito");
						todoBien = true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException | UsuarioNoExiste | ContraseñaInvalidaException e) {
						todoBien = false;
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 3:
				try {
					BufferedReader leerRegistro = new BufferedReader(new FileReader("Usuarios.log"));

					String texto = "";
					String linea = leerRegistro.readLine();

					while (linea != null) {
						texto += linea + "\n";
						linea = leerRegistro.readLine();
					}
					System.out.println(texto);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			}
		} while (opcion != 0);
	}

}
