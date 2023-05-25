/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxfarmacia.controladores;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxfarmacia.JavaFXFarmacia;
import javafxfarmacia.interfaz.INotificacionOperacion;
import javafxfarmacia.modelo.dao.PromocionDAO;
import javafxfarmacia.modelo.pojo.Promocion;
import javafxfarmacia.modelo.pojo.PromocionRespuesta;
import javafxfarmacia.utils.Constantes;
import javafxfarmacia.utils.Utilidades;

/**
 * FXML Controller class
 *
 * @author jasie
 */
public class FXMLPromocionesController implements Initializable, INotificacionOperacion {
    @FXML
    private TableView<Promocion> tvPromociones;
    @FXML
    private TableColumn colDescripcion;
    @FXML
  
    private TableColumn colFechaInicio;
    @FXML
    private TableColumn colfechaTermino;
    
    @FXML
    private TableColumn colProducto;
    @FXML
    private TableColumn colPrecioFinal;
    
    private ObservableList <Promocion> promociones;
    private ObservableList <Promocion> promocionesBusqueda;
    @FXML
    private TextField tfBusqueda;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacion();
    }  
    
    private void configurarTabla(){
        colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
        colProducto.setCellValueFactory(new PropertyValueFactory("productosPromo"));
        colPrecioFinal.setCellValueFactory(new PropertyValueFactory("precioFinal"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory("fechaInicio"));
        colfechaTermino.setCellValueFactory(new PropertyValueFactory("fechaTermino"));

        
    }
    private void cargarInformacion(){
        promociones = FXCollections.observableArrayList();
        PromocionRespuesta respuestaBD = PromocionDAO.obtenerInformacionPromociones();
        switch(respuestaBD.getCodigoRespuesta()){
            case Constantes.ERROR_CONEXION:
                Utilidades.mostrarDialogoSimple("Sin conexión", "Lo sentimos, por "
                        + "el momento no hay conexion", Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                Utilidades.mostrarDialogoSimple("Error al cargar los datos", "Hubo un error al cargar los datos"
                        + " inténtelo más tarde", Alert.AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                promociones.addAll(respuestaBD.getPromociones());
                tvPromociones.setItems(promociones);
                break;
             
                
             
        }
    }
    
      private void buscarProducto(KeyEvent event) {
        String busqueda = tfBusqueda.getText();
        promocionesBusqueda = FXCollections.observableArrayList();
        PromocionRespuesta respuestaBD = PromocionDAO.obtenerInformacionBusqueda(busqueda);
        switch(respuestaBD.getCodigoRespuesta()){
            case Constantes.ERROR_CONEXION:
                Utilidades.mostrarDialogoSimple("Sin conexion", 
                        "No se pudo conectar con la base de datos. Intente de nuevo o hágalo más tarde",
                        Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                Utilidades.mostrarDialogoSimple("Error al cargar los datos", 
                        "Hubo un error al cargar la información por favor inténtelo de nuevo más tarde",
                        Alert.AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                promocionesBusqueda.addAll(respuestaBD.getPromociones());
                tvPromociones.setItems(FXCollections.observableList(promocionesBusqueda));
                break;
        }
    }

    @FXML
    private void clicRegistrarPromocion(ActionEvent event) {
        Stage escenarioFormulario = new Stage();
        escenarioFormulario.setScene(Utilidades.inicializaEscena("vistas/FXMLRegistroPromocion.fxml"));
        escenarioFormulario.setTitle("Formulario");
        escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
        escenarioFormulario.showAndWait();
    }

    @FXML
    private void clicModiificarPromocion(ActionEvent event) {
        int posicion = tvPromociones.getSelectionModel().getSelectedIndex();
        if(posicion != -1){
            irFormulario(true,promociones.get(posicion));
        }else{
            Utilidades.mostrarDialogoSimple("Atención","Por favor selecciona "
                    + "una promoción para poder editar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicEliminarPromocion(ActionEvent event) {
    }


    @FXML
    private void clicVolverVentana(MouseEvent event) {
        Stage escenarioPrincipal = (Stage) tfBusqueda.getScene().getWindow();
        escenarioPrincipal.close();
    }
    
    private void irFormulario(boolean esEdicion, Promocion promocion){
        try{
            FXMLLoader accesoControlador = new FXMLLoader
                (JavaFXFarmacia.class.getResource("vistas/FXMLRegistroPromocion.fxml"));
            Parent vista;
            vista = accesoControlador.load();
            
            FXMLRegistroPromocionController formulario = accesoControlador.getController();
            formulario.inicializarInformacionFormulario(esEdicion,promocion,this);
            
            Stage escenarioFormulario = new Stage();
            escenarioFormulario.setScene(new Scene(vista));
            escenarioFormulario.setTitle("Formulario: registro de promoción");
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();         
            
        }catch(IOException ex){
            Logger.getLogger(FXMLRegistroPromocionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void notificarOperacionGuardar(String descripcionPromocion) {
        Utilidades.mostrarDialogoSimple("Notificacion","Se registró de forma "
                + "exitosa la promoción", Alert.AlertType.INFORMATION);
        cargarInformacion();
    }

    @Override
    public void notificarOperacionActualizar(String descripcionPromocion) {
        Utilidades.mostrarDialogoSimple("Notificación","Se actualizaron "
                + "los datos de la promocion", Alert.AlertType.INFORMATION);
        cargarInformacion();
    }
    
    
    
}
