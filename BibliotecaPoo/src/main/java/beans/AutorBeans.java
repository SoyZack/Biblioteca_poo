/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

public class AutorBeans{
    private int idAutor;
    private String nombre;
    private String nacionalidad;

/**
 *
 * @author Uptim
 */
// CONSTRUCTOR
public AutorBeans() {
    
}
    public AutorBeans(int idAutor, String nombre, String nacionalidad){
        this.idAutor = idAutor;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
    }    

    public int getIdAutor(){
        return idAutor;
    }

    public void setIdAutor(int idAutor){
        this.idAutor = idAutor;
    }

    public String getNombre(){
        return nombre;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public String getNacionalidad(){
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad){
        this.nacionalidad = nacionalidad;
    }

    @Override
    public String toString() {
        return nombre;
    }
}


