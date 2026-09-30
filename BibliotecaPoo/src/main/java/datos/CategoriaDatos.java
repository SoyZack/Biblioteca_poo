/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;
import beans.CategoriaBeans;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Uptim
 */
public class CategoriaDatos {
 
    // 1. Obtener todas las categorías
    public List<CategoriaBeans> listarCategorias(Connection conexion) {
        List<CategoriaBeans> lista = new ArrayList<>();
        String sql = "SELECT id_Categoria, nombre_categoria FROM categoria";

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CategoriaBeans cat = new CategoriaBeans();
                cat.setIdCategoria(rs.getInt("id_Categoria"));
                cat.setNombreCategoria(rs.getString("nombre_categoria"));
                lista.add(cat);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar categorías: " + e.getMessage());
        }

        return lista;
    }

    // 2. Obtener una categoría por su ID
    public CategoriaBeans obtenerPorId(int idCategoria, Connection conexion) {
        String sql = "SELECT idCategoria, nombre_categoria FROM categoria WHERE idCategoria = ?";
        CategoriaBeans categoria = null;

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    categoria = new CategoriaBeans();
                    categoria.setIdCategoria(rs.getInt("idCategoria"));
                    categoria.setNombreCategoria(rs.getString("nombre_categoria"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar categoría por ID: " + e.getMessage());
        }

        return categoria;
    }

    // 3. Insertar una nueva categoría
    public boolean insertar(CategoriaBeans categoria, Connection conexion) {
        String sql = "INSERT INTO categoria (nombre_categoria) VALUES (?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNombreCategoria());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar categoría: " + e.getMessage());
            return false;
        }
    }

    // 4. Actualizar una categoría existente
    public boolean actualizar(CategoriaBeans categoria, Connection conexion) {
        String sql = "UPDATE categoria SET nombre_categoria = ? WHERE idCategoria = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNombreCategoria());
            stmt.setInt(2, categoria.getIdCategoria());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar categoría: " + e.getMessage());
            return false;
        }
    }

    // 5. Eliminar una categoría por ID
    public boolean eliminar(int idCategoria, Connection conexion) {
        String sql = "DELETE FROM categoria WHERE idCategoria = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar categoría: " + e.getMessage());
            return false;
        }
    }
}
