/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import java.sql.*;
import beans.AutorBeans;
import beans.CategoriaBeans;
import beans.LibroBeans;
import datos.AutorDatos;
import datos.CategoriaDatos;
import datos.LibroDatos;
import util.Conexion;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;

public class frmBiblioteca extends javax.swing.JFrame {

    // DAO / Acceso a datos
    private LibroDatos libroDatos = new LibroDatos();
    private CategoriaDatos categoriaDatos = new CategoriaDatos();
    private AutorDatos autorDatos = new AutorDatos();
    private Connection conexion;

    // Guarda el ID del libro seleccionado en la tabla (-1 indica ninguno)
    private int idLibroSeleccionado = -1;

    public frmBiblioteca() {
        initComponents(); // Generado por la vista de diseño de NetBeans

        // Inicialización de conexión a la base de datos
        this.conexion = Conexion.getConexion(); // Ajusta si el método en Conexion.java se llama distinto

        // Cargar combos y tabla al iniciar la pantalla
        cargarCategorias();
        cargarAutores();
        cargarFiltros();
        listarTabla();

        // Asignación de Listeners a los componentes de AWT/Swing
        btnGuardar.addActionListener(e -> guardarLibro());
        btnEditar.addActionListener(e -> editarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnFiltrar.addActionListener(e -> filtrarLibros());
        btnVertodo.addActionListener(e -> listarTabla());

        // Evento al dar clic sobre las filas de la tabla
        tblLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFila();
            }
        });
    }

    // 1. Cargar combo de Categorías
    private void cargarCategorias() {
        cmbCategoria.removeAllItems();
        List<CategoriaBeans> lista = categoriaDatos.listarCategorias(conexion);
        for (CategoriaBeans cat : lista) {
            cmbCategoria.addItem(cat.getNombreCategoria());
        }
    }

    // 2. Cargar combo de Autores
    private void cargarAutores() {
    cmbAutor.removeAllItems();
    // Se llaman sin parámetros porque tu método no los necesita
    List<AutorBeans> lista = autorDatos.listarAutores(); 
    for (AutorBeans autor : lista) {
        cmbAutor.addItem(autor.getNombre()); // o autor.toString()
    }
}

    // 3. Cargar combo de opciones de Filtro
    private void cargarFiltros() {
        cmbFiltroTipo.removeAllItems();
        cmbFiltroTipo.addItem("Autor");
        cmbFiltroTipo.addItem("Categoría");
    }

    // 4. Llenar la tabla JTable con los registros de la base de datos
    private void listarTabla() {
    DefaultTableModel model = (DefaultTableModel) tblLibros.getModel();
    
    // Configurar columnas de la tabla
    model.setColumnIdentifiers(new String[]{
        "ID", "TITULO", "AUTOR", "AÑO", "CATEGORIA"
    });

    model.setRowCount(0); // Limpiar filas anteriores

    // Asegurar que la conexión no sea nula
    if (this.conexion == null) {
        this.conexion = Conexion.getConexion();
    }

    List<LibroBeans> lista = libroDatos.listarLibros(this.conexion);

    if (lista != null && !lista.isEmpty()) {
        for (LibroBeans l : lista) {
            model.addRow(new Object[]{
                l.getIdLibro(),          // Columna 0: ID (debe traer un entero > 0)
                l.getTitulo(),           // Columna 1: TÍTULO
                l.getNombreAutor(),      // Columna 2: AUTOR
                l.getAnio_Publicacion(),  // Columna 3: AÑO
                l.getNombreCategoria()   // Columna 4: CATEGORÍA
            });
        }
    } else {
        System.out.println("No se encontraron registros de libros en la base de datos.");
    }

    tblLibros.setModel(model);
    tblLibros.revalidate();
    tblLibros.repaint();
}

    // 5. Insertar un libro
    private void guardarLibro() {
        if (!validarCampos()) return;

        LibroBeans libro = new LibroBeans();
        libro.setTitulo(txtTitulo.getText().trim());
        libro.setAnioPublicacion(Integer.parseInt(txtAnio.getText().trim()));
        libro.setId_Autor(cmbAutor.getSelectedIndex() + 1);
        libro.setIdCategoria(cmbCategoria.getSelectedIndex() + 1);

        if (libroDatos.insertar(libro, conexion)) {
            JOptionPane.showMessageDialog(this, "Libro guardado exitosamente.");
            listarTabla();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar el libro.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 6. Actualizar un libro
    private void editarLibro() {
        if (idLibroSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro de la tabla para editar.");
            return;
        }

        if (!validarCampos()) return;

        LibroBeans libro = new LibroBeans();
        libro.setIdLibro(idLibroSeleccionado);
        libro.setTitulo(txtTitulo.getText().trim());
        libro.setAnioPublicacion(Integer.parseInt(txtAnio.getText().trim()));
        libro.setId_Autor(cmbAutor.getSelectedIndex() + 1);
        libro.setIdCategoria(cmbCategoria.getSelectedIndex() + 1);

        if (libroDatos.actualizar(libro, conexion)) {
            JOptionPane.showMessageDialog(this, "Libro actualizado correctamente.");
            listarTabla();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el libro.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 7. Eliminar un libro
    private void eliminarLibro() {
        if (idLibroSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro de la tabla para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas eliminar el libro seleccionado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (libroDatos.eliminar(idLibroSeleccionado, conexion)) {
                JOptionPane.showMessageDialog(this, "Libro eliminado.");
                listarTabla();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el libro.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 8. Filtrar registros en la tabla
    private void filtrarLibros() {
        String tipoFiltro = (String) cmbFiltroTipo.getSelectedItem();
        JOptionPane.showMessageDialog(this, "Filtrando registros por: " + tipoFiltro);
    }

    // Seleccionar fila en JTable y llenar los campos del formulario
    private void seleccionarFila() {
    int fila = tblLibros.getSelectedRow();
    if (fila >= 0) {
        // Columna 0: ID real cargado de la BD
        idLibroSeleccionado = Integer.parseInt(tblLibros.getValueAt(fila, 0).toString());
        
        // Columna 1: TÍTULO
        txtTitulo.setText(tblLibros.getValueAt(fila, 1).toString());
        
        // Columna 3: AÑO (no la columna 2 del Autor)
        txtAnio.setText(tblLibros.getValueAt(fila, 3).toString());

        // Columna 2: AUTOR, Columna 4: CATEGORÍA
        String nombreAutor = tblLibros.getValueAt(fila, 2).toString();
        String nombreCat = tblLibros.getValueAt(fila, 4).toString();

        cmbAutor.setSelectedItem(nombreAutor);
        cmbCategoria.setSelectedItem(nombreCat);
    }
}

    // Resetear entradas de texto y selección
    private void limpiarCampos() {
        txtTitulo.setText("");
        txtAnio.setText("");
        if (cmbAutor.getItemCount() > 0) cmbAutor.setSelectedIndex(0);
        if (cmbCategoria.getItemCount() > 0) cmbCategoria.setSelectedIndex(0);
        idLibroSeleccionado = -1;
        tblLibros.clearSelection();
    }

    // Validación de entradas de texto
   private boolean validarCampos() {
    // 1. Validar que el título no esté vacío
    if (txtTitulo.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingresa el título del libro.");
        return false;
    }

    // 2. Validar que el año sea un número entero y esté en el rango de 1500 a 2026
    try {
        int anio = Integer.parseInt(txtAnio.getText().trim());

        // Comprobación del rango permitido (1500 - 2026)
        if (anio < 1500 || anio > 2026) {
            JOptionPane.showMessageDialog(this, 
                "El año de publicación debe estar entre 1500 y 2026.", 
                "Año fuera de rango", 
                JOptionPane.WARNING_MESSAGE);
            txtAnio.requestFocus();
            return false;
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, 
            "Ingresa un año de publicación válido (número entero).", 
            "Error de entrada", 
            JOptionPane.ERROR_MESSAGE);
        txtAnio.requestFocus();
        return false;
    }

    return true;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlEncabezado = new javax.swing.JPanel();
        lblTituloApp = new java.awt.Label();
        jLabel6 = new javax.swing.JLabel();
        lblRegistro = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        txtTitulo = new java.awt.TextField();
        lblAutor = new javax.swing.JLabel();
        cmbAutor = new javax.swing.JComboBox<>();
        btnGuardar = new java.awt.Button();
        btnEditar = new java.awt.Button();
        btnEliminar = new java.awt.Button();
        btnLimpiar = new java.awt.Button();
        lblAnio = new javax.swing.JLabel();
        txtAnio = new java.awt.TextField();
        lblCategoria = new javax.swing.JLabel();
        cmbCategoria = new javax.swing.JComboBox<>();
        pnlLibros = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbFiltroTipo = new javax.swing.JComboBox<>();
        btnVertodo = new java.awt.Button();
        btnFiltrar = new java.awt.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLibros = new javax.swing.JTable();
        scrollLibros = new java.awt.Scrollbar();
        cmbFiltroTipo1 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblTituloApp.setFont(new java.awt.Font("Ink Free", 2, 36)); // NOI18N
        lblTituloApp.setMinimumSize(new java.awt.Dimension(100, 50));
        lblTituloApp.setText("Biblioteca Digital");

        jLabel6.setFont(new java.awt.Font("Palatino Linotype", 0, 12)); // NOI18N
        jLabel6.setText("Gestión de libros, autores y categorías");

        lblRegistro.setText("REGISTRO DE LIBRO");

        lblTitulo.setText("Título del libro:");
        lblTitulo.setName(""); // NOI18N

        txtTitulo.setName(""); // NOI18N

        lblAutor.setText("Autor:");

        cmbAutor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAutor.setName("cmbAutor"); // NOI18N

        btnGuardar.setLabel("Guardar");
        btnGuardar.setName("btnGuardar"); // NOI18N
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnEditar.setLabel("Editar");
        btnEditar.setName("btnEditar"); // NOI18N

        btnEliminar.setLabel("Eliminar");
        btnEliminar.setName("btnEliminar"); // NOI18N

        btnLimpiar.setLabel("Limpiar");
        btnLimpiar.setName("btnLimpiar"); // NOI18N

        lblAnio.setText("Año de publicación:");

        lblCategoria.setText("Categoría:");

        cmbCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCategoria.setName("cmbCategoria"); // NOI18N

        javax.swing.GroupLayout pnlEncabezadoLayout = new javax.swing.GroupLayout(pnlEncabezado);
        pnlEncabezado.setLayout(pnlEncabezadoLayout);
        pnlEncabezadoLayout.setHorizontalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRegistro)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTituloApp, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTitulo)
                            .addComponent(lblAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(28, 28, 28)
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbAutor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAnio)
                    .addComponent(lblCategoria))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(95, 95, 95))
        );
        pnlEncabezadoLayout.setVerticalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addComponent(lblTituloApp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRegistro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTitulo)
                            .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAnio)))
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)))
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimpiar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAutor)
                            .addComponent(cmbAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCategoria)
                            .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEditar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );

        jLabel8.setText("LIBROS REGISTRADOS");

        jLabel9.setText("Filtrar por:");
        jLabel9.setToolTipText("");

        cmbFiltroTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbFiltroTipo.setName(""); // NOI18N

        btnVertodo.setLabel("Ver todos");
        btnVertodo.setName("btnver todos"); // NOI18N

        btnFiltrar.setLabel("Filtrar");
        btnFiltrar.setName("btnFiltrar"); // NOI18N

        tblLibros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "TITULO", "AUTOR", " AÑO", "CATEGORIA"
            }
        ));
        jScrollPane1.setViewportView(tblLibros);

        cmbFiltroTipo1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbFiltroTipo1.setName(""); // NOI18N

        javax.swing.GroupLayout pnlLibrosLayout = new javax.swing.GroupLayout(pnlLibros);
        pnlLibros.setLayout(pnlLibrosLayout);
        pnlLibrosLayout.setHorizontalGroup(
            pnlLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLibrosLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(pnlLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLibrosLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollLibros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(55, Short.MAX_VALUE))
                    .addGroup(pnlLibrosLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(274, 274, 274)
                        .addComponent(jLabel9)
                        .addGap(27, 27, 27)
                        .addGroup(pnlLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlLibrosLayout.createSequentialGroup()
                                .addComponent(cmbFiltroTipo1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbFiltroTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(pnlLibrosLayout.createSequentialGroup()
                                .addGap(61, 61, 61)
                                .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(btnVertodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );
        pnlLibrosLayout.setVerticalGroup(
            pnlLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLibrosLayout.createSequentialGroup()
                .addGroup(pnlLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLibrosLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(pnlLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLibrosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbFiltroTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbFiltroTipo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnVertodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(pnlLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scrollLibros, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlEncabezado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlLibros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlLibros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmBiblioteca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmBiblioteca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmBiblioteca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmBiblioteca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmBiblioteca().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button btnEditar;
    private java.awt.Button btnEliminar;
    private java.awt.Button btnFiltrar;
    private java.awt.Button btnGuardar;
    private java.awt.Button btnLimpiar;
    private java.awt.Button btnVertodo;
    private javax.swing.JComboBox<String> cmbAutor;
    private javax.swing.JComboBox<String> cmbCategoria;
    private javax.swing.JComboBox<String> cmbFiltroTipo;
    private javax.swing.JComboBox<String> cmbFiltroTipo1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAnio;
    private javax.swing.JLabel lblAutor;
    private javax.swing.JLabel lblCategoria;
    private javax.swing.JLabel lblRegistro;
    private javax.swing.JLabel lblTitulo;
    private java.awt.Label lblTituloApp;
    private javax.swing.JPanel pnlEncabezado;
    private javax.swing.JPanel pnlLibros;
    private java.awt.Scrollbar scrollLibros;
    private javax.swing.JTable tblLibros;
    private java.awt.TextField txtAnio;
    private java.awt.TextField txtTitulo;
    // End of variables declaration//GEN-END:variables
}
