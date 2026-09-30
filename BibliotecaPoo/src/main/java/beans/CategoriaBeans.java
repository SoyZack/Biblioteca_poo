/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

/**
 *
 * @author Uptim
 */
public class CategoriaBeans {
    private int idCategoria;
    private String nombreCategoria;
    
    public CategoriaBeans(){
    }
    public CategoriaBeans(int idCategoria, String nombreCategoria){
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
    }
    
    public int getIdCategoria(){
        return idCategoria;
    }
    
    public void setIdCategoria(int idCategoria){
        this.idCategoria = idCategoria;
    }
    
    public String getNombreCategoria(){
        return nombreCategoria;
    }
    
    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
    
    @Override
    public String toString(){
        return nombreCategoria;
    }
}
