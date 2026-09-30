/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

/**
 *
 * @author Uptim
 */
public class LibroBeans {
    private int id_Libro;
    private String titulo;
    private int anio_Publicacion;
    private int id_Autor;
    private int id_Categoria;
    
    public LibroBeans(){
    }
    
    public LibroBeans(int id_Libro, String titulo, int anio_Publicacion, int id_Autor, int id_Categoria) {
        this.id_Libro = id_Libro;
        this.titulo = titulo;
        this.anio_Publicacion = anio_Publicacion;
        this.id_Autor = id_Autor;
        this.id_Categoria = id_Categoria;
    }
    
    private String nombreAutor;
    private String nombreCategoria;
    
    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    // --- AGREGAR GETTERS Y SETTERS DE NOMBRE CATEGORIA ---
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
    
    public int getIdLibro(){
        return id_Libro;
    }
    
    public void setIdLibro(int Id_Libro){
        this.id_Libro = id_Libro;
    }
    
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
     public int getAnio_Publicacion() {
        return anio_Publicacion;
    }

    public void setAnioPublicacion(int anio_Publicacion) {
        this.anio_Publicacion = anio_Publicacion;
    }
    
     public int getId_Autor() {
        return id_Autor;
    }

    public void setId_Autor(int id_Autor) {
        this.id_Autor = id_Autor;
    }
    //categoria id
    public int getId_Categoria() {
        return id_Categoria;
    }

    public void setIdCategoria(int id_Categoria) {
        this.id_Categoria = id_Categoria;
    }
}
