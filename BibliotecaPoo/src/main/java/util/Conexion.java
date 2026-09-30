/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Uptim
 */
public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca_db?useSSL=false&allowPublicKeyRetieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    public static Connection getConexion(){
        Connection con = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e){
            System.err.println("Error; Driver de Mysql no encontrado ->" + e.getMessage());
        } catch (SQLException e){
            System.err.println("Error ase de datos ->" + e.getMessage());
        }
        return con;
    }  
}
