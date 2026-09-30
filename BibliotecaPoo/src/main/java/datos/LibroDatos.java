/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import beans.LibroBeans;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDatos {

   public List<LibroBeans> listarLibros(Connection conexion) {
    List<LibroBeans> lista = new ArrayList<>();
    
    // Verificación de seguridad de la conexión
    if (conexion == null) {
        System.err.println("ERROR: La conexión enviada a listarLibros es NULL.");
        return lista;
    }

    // Consulta SQL con INNER JOIN
    String sql = "SELECT l.id_libro, l.titulo, l.anio_publicacion, " +
                 "a.nombre AS nombre_autor, c.nombre_categoria " +
                 "FROM libro l " +
                 "INNER JOIN autor a ON l.id_autor = a.id_autor " +
                 "INNER JOIN categoria c ON l.id_categoria = c.id_categoria";

    try (PreparedStatement ps = conexion.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            LibroBeans libro = new LibroBeans();
            libro.setIdLibro(rs.getInt("id_libro"));
            libro.setTitulo(rs.getString("titulo"));
            libro.setAnioPublicacion(rs.getInt("anio_publicacion"));
            libro.setNombreAutor(rs.getString("nombre_autor")); 
            libro.setNombreCategoria(rs.getString("nombre_categoria"));

            lista.add(libro);
        }
    } catch (SQLException e) {
        System.err.println("Error SQL al listar libros: " + e.getMessage());
        e.printStackTrace(); // Muestra el detalle exacto del error en la consola
    }
    return lista;
}

    // 2. Obtener un libro por su ID
    public LibroBeans obtenerPorId(int idLibro, Connection conexion) {
        String sql = "SELECT id_libro, titulo, anio_publicacion, id_autor, id_categoria FROM libro WHERE id_libro = ?";
        LibroBeans libro = null;

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idLibro);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    libro = new LibroBeans();
                    libro.setIdLibro(rs.getInt("id_libro"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setAnioPublicacion(rs.getInt("anio_publicacion"));
                    libro.setId_Autor(rs.getInt("id_autor"));
                    libro.setIdCategoria(rs.getInt("id_categoria"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar libro por ID: " + e.getMessage());
        }

        return libro;
    }

    // 3. Insertar un nuevo libro
    public boolean insertar(LibroBeans libro, Connection conexion) {
        String sql = "INSERT INTO libro (titulo, anio_publicacion, id_autor, id_categoria) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, libro.getTitulo());
            stmt.setInt(2, libro.getAnio_Publicacion());
            stmt.setInt(3, libro.getId_Autor());
            stmt.setInt(4, libro.getId_Categoria());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar libro: " + e.getMessage());
            return false;
        }
    }

    // 4. Actualizar un libro existente
    public boolean actualizar(LibroBeans libro, Connection conexion) {
        String sql = "UPDATE libro SET titulo = ?, anio_publicacion = ?, id_autor = ?, id_categoria = ? WHERE id_libro = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, libro.getTitulo());
            stmt.setInt(2, libro.getAnio_Publicacion());
            stmt.setInt(3, libro.getId_Autor());
            stmt.setInt(4, libro.getId_Categoria());
            stmt.setInt(5, libro.getIdLibro());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
            return false;
        }
    }

    // 5. Eliminar un libro por ID
    public boolean eliminar(int idLibro, Connection conexion) {
        String sql = "DELETE FROM libro WHERE id_libro = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idLibro);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            return false;
        }
    }
}