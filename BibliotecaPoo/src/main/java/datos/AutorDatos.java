/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import beans.AutorBeans;
import util.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Charlie._saa
 */
public class AutorDatos {
    
    public List<AutorBeans> listarAutores() {
        List<AutorBeans> lista = new ArrayList<>();
        String sql = "SELECT id_autor, nombre, nacionalidad FROM autor";
        
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            
            while (rs.next()){
                AutorBeans autor = new AutorBeans();
                autor.setIdAutor(rs.getInt("id_autor"));
                autor.setNombre(rs.getString("nombre"));
                autor.setNacionalidad(rs.getString("nacionalidad"));
                lista.add(autor);
            }
        } catch (SQLException e){
            System.err.println("Error al listar autores: "+ e.getMessage());
        }
        return lista;      
    }
}