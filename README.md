# Proyecto: Gestor de Usuarios y Mascotas

Este proyecto es una aplicación en Java que implementa un sistema de gestión de usuarios y mascotas, con funcionalidades para registro, inicio de sesión, modificación y eliminación de registros en una base de datos, así como registro de logs para auditoría. El proyecto demuestra cómo interactuar con bases de datos utilizando sentencias SQL y estructuras avanzadas de Java.

## Características Principales

1. **Gestor de Usuarios**
   - Registro de usuarios en la base de datos.
   - Inicio de sesión con verificación de credenciales.
   - Modificación de datos de usuarios existentes (email, contraseña o nickname).
   - Eliminación de usuarios.

2. **Gestor de Mascotas**
   - Eliminación de mascotas asociadas a un usuario.

3. **Registro de Logs**
   - Almacena registros de eventos, como registros e inicios de sesión, en un archivo de texto.
   - Utiliza `BufferedReader` y `FileWriter` para lectura y escritura.

4. **Interacción con Base de Datos**
   - Conexión a una base de datos SQL.
   - Uso de un objeto `DAO` (Data Access Object) para abstracción de operaciones CRUD.

## Tecnologías Utilizadas

- **Lenguaje**: Java
- **Base de Datos**: SQL
- **Manejo de Excepciones**: Personalizadas como `ContraseñaInvalidaException` y `UsuarioNoExiste`.
- **Manejo de Archivos**: Java I/O (`FileWriter`, `BufferedReader`).
- **Estructuras de Datos**:
  - `HashMap`: Almacenar datos clave-valor para queries.
  - `ArrayList`: Almacenar resultados de consultas.
  - `LinkedHashSet`: Garantizar el orden de columnas al consultar.
