package javafxfarmacia.controladores;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafxfarmacia.modelo.dao.ProductoDAO;
import javafxfarmacia.modelo.dao.TipoProductoDAO;
import javafxfarmacia.modelo.pojo.Producto;
import javafxfarmacia.modelo.pojo.ProductoRespuesta;
import javafxfarmacia.modelo.pojo.Tipo;
import javafxfarmacia.modelo.pojo.TipoRespuesta;
import javafxfarmacia.utils.Constantes;
import javafxfarmacia.utils.Utilidades;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafxfarmacia.interfaz.INotificacionOperacion;
import javafxfarmacia.modelo.dao.PedidoDAO;
import javafxfarmacia.modelo.dao.PedidoDAO.TipoProveedor;
import static javafxfarmacia.modelo.dao.PedidoDAO.guardarPedidoExterno;
import static javafxfarmacia.modelo.dao.PedidoDAO.guardarPedidoInterno;
import javafxfarmacia.modelo.dao.ProductoPedidoDAO;
import javafxfarmacia.modelo.pojo.Pedido;
import javafxfarmacia.modelo.pojo.PedidoRespuesta;
import javafxfarmacia.modelo.pojo.ProductoPedido;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;



public class FXMLGenerarPedidoController implements Initializable {

    private ObservableList<Tipo> tipos;
    @FXML
    private ComboBox<Pedido> cbProveedor;
    @FXML
    private ComboBox<Producto> cbProducto;

    private ObservableList<Producto> productos;
    @FXML
    private TextField tfCantidad;
    @FXML
    private TableView<Producto> tvCarrito;
    @FXML
    private DatePicker dpDiaEntrega;
    private TextField tfBusqueda;
    private ObservableList<Producto> productosBusqueda;

    private ObservableList<Producto> carrito;

    @FXML
    private TableColumn<Producto, Integer> tcCantidad;
    @FXML
    private TableColumn<Producto, String> tcProducto;
    @FXML
    private TableColumn<Producto, Float> tcPrecioUnidad;
    @FXML
    private TableColumn<Producto, Float> tcPrecioFinal;
    @FXML
    private Label txTotal;
    @FXML
    private Button btnEliminar;
    @FXML
    private RadioButton rbInternos;
    @FXML
    private RadioButton rbExternos;
    private ObservableList<Pedido> proveedoresInternos;
    private ObservableList<Pedido> proveedoresExternos;
    private ObservableList<Pedido> todosLosProveedores;
    private String tipoProveedor;
 

 private ObservableList<String> possibleSuggestions;


    @FXML
    private TextField autoTextField;
    private boolean esEdicion;
    private Pedido pedido;
    private INotificacionOperacion interfazNotificacion;


    
    @Override
    
     public void initialize(URL url, ResourceBundle rb) {
    ProductoRespuesta respuestaProductos = ProductoDAO.obtenerInformacionProducto(0);
    if (respuestaProductos.getCodigoRespuesta() == Constantes.OPERACION_EXITOSA) {
        productos = FXCollections.observableArrayList();
        productos.addAll(respuestaProductos.getProductos());

        // Obtener los nombres de los productos y almacenarlos en un ObservableList
        possibleSuggestions = FXCollections.observableArrayList(
            productos.stream()
                .map(Producto::getNombre)
                .collect(Collectors.toList())
        );

        // Asignar las sugerencias al campo de búsqueda
        TextFields.bindAutoCompletion(autoTextField, possibleSuggestions);
    }

        rbInternos.setSelected(true);
        proveedoresInternos = FXCollections.observableArrayList();
        
    // Obtener los proveedores internos utilizando PedidoDAO
    PedidoRespuesta respuesta = PedidoDAO.obtenerProveedoresInternos();
    if (respuesta.getCodigoRespuesta() == Constantes.OPERACION_EXITOSA) {
        ArrayList<Pedido> proveedoresInternosList = respuesta.getPedidos();
        proveedoresInternos.addAll(proveedoresInternosList);
    } else {
Utilidades.mostrarDialogoSimple("Error de conexión", "Error de conexión con la base de datos", Alert.AlertType.ERROR);
    }
    
    proveedoresExternos = FXCollections.observableArrayList();
PedidoRespuesta respuestaExternos = PedidoDAO.obtenerProveedoresExternos();
if (respuestaExternos.getCodigoRespuesta() == Constantes.OPERACION_EXITOSA) {
    ArrayList<Pedido> proveedoresExternosList = respuestaExternos.getPedidos();
    proveedoresExternos.addAll(proveedoresExternosList);
} else {
    Utilidades.mostrarDialogoSimple("Error de conexión", "Error de conexión con la base de datos", Alert.AlertType.ERROR);
}
        makeComboBoxSearchable(cbProducto, Producto::toString);
        cargarInformacionProducto(0);
        carrito = FXCollections.observableArrayList();
        tcCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        tcProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcPrecioUnidad.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        tcPrecioFinal.setCellValueFactory(new PropertyValueFactory<>("precioFinal"));
        btnEliminar.setDisable(true);
        tvCarrito.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Producto>() {
            @Override
            public void changed(ObservableValue<? extends Producto> observable, Producto oldValue, Producto newValue) {
                // Habilita el botón "Eliminar" solo si hay una fila seleccionada
                btnEliminar.setDisable(newValue == null);
            }
        });

        cbProveedor.setConverter(new StringConverter<Pedido>() {
    @Override
    public String toString(Pedido pedido) {
        return pedido != null ? pedido.getNombre_proveedor(): "";
    }

    @Override
    public Pedido fromString(String string) {
        // Implementación necesaria solo si se requiere la edición del ComboBox
        return null;
    }
    
    });
       
        cbProveedor.setItems(proveedoresInternos);

        todosLosProveedores = FXCollections.observableArrayList();
        todosLosProveedores.addAll(proveedoresInternos);
        todosLosProveedores.addAll(proveedoresExternos);

        actualizarProveedores();

         ToggleGroup proveedoresToggleGroup = new ToggleGroup();
    rbInternos.setToggleGroup(proveedoresToggleGroup);
    rbExternos.setToggleGroup(proveedoresToggleGroup);
    
    proveedoresToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
            actualizarProveedores();
        }
    });
        
    tfCantidad.addEventFilter(KeyEvent.KEY_TYPED, event -> {

        String character = event.getCharacter();

        // Verificar si el carácter ingresado no es un número
        if (!character.matches("[0-9]")) {
            // Consumir el evento para que el carácter no se muestre en el campo de texto
            event.consume();
        }
    });

    
 
    
     }

    private void cargarInformacionProducto(int idProducto) {
        productos = FXCollections.observableArrayList();
        ProductoRespuesta productosBD = ProductoDAO.obtenerInformacionProducto(idProducto);
        switch (productosBD.getCodigoRespuesta
()) {
case Constantes.ERROR_CONEXION:
Utilidades.mostrarDialogoSimple("Error de conexión", "Error de conexión con la base de datos", Alert.AlertType.ERROR);
break;
        case Constantes.ERROR_CONSULTA:
            Utilidades.mostrarDialogoSimple("Error de consulta", "Por el momento no se puede mostrar la información", Alert.AlertType.WARNING);
            break;
        case Constantes.OPERACION_EXITOSA:
            ArrayList<Producto> productosOriginales = productosBD.getProductos();
            HashSet<String> nombresProductos = new HashSet<>();

            for (Producto producto : productosOriginales) {
                if (nombresProductos.add(producto.getNombre())) {
                    productos.add(producto);
                }
            }
            cbProducto.setItems(productos);
            break;
    }
}

@FXML
private void clicAgregar(ActionEvent event) {
    Producto productoSeleccionado = cbProducto.getSelectionModel().getSelectedItem();
    int cantidad = Integer.parseInt(tfCantidad.getText());

    if (productoSeleccionado != null && cantidad > 0) {
        // Crear una nueva instancia de Producto con los mismos datos
        Producto productoEnCarrito = new Producto();
        productoEnCarrito.setIdProducto(productoSeleccionado.getIdProducto());
        productoEnCarrito.setNombre(productoSeleccionado.getNombre());
        productoEnCarrito.setPrecio(productoSeleccionado.getPrecio());
        productoEnCarrito.setCantidad(cantidad);

        // Calcular el precio unitario y el precio final
        float precioUnitario = productoSeleccionado.getPrecio();
        float precioFinal = precioUnitario * cantidad;
        productoEnCarrito.setPrecioUnitario(precioUnitario);
        productoEnCarrito.setPrecioFinal(precioFinal);

        carrito.add(productoEnCarrito);

        actualizarTablaCarrito();

        tfCantidad.clear();
        cbProducto.getSelectionModel().clearSelection();
    }
}

private void actualizarTablaCarrito() {
    // Crear una lista observable a partir de la lista 'carrito'
    ObservableList<Producto> listaCarrito = FXCollections.observableArrayList(carrito);

    // Calcular el precio final para cada producto y actualizar la lista 'carrito'
    for (Producto producto : listaCarrito) {
        float precioFinal = producto.getPrecioUnitario() * producto.getCantidad();
        producto.setPrecioFinal(precioFinal);
    }

    // Asignar la lista observable a la tabla
    tvCarrito.setItems(listaCarrito);

    float sumaPrecios = 0;

    // Calcular la suma de los precios finales
    for (Producto producto : listaCarrito) {
        sumaPrecios += producto.getPrecioFinal();
    }

    // Mostrar la suma en el Label txTotal
    txTotal.setText("$" + String.valueOf(sumaPrecios) + " MXN");
}

private void actualizarProveedores() {
    if (rbInternos.isSelected()) {
        cbProveedor.setItems(proveedoresInternos);
        tipoProveedor = "interno";
    } else if (rbExternos.isSelected()) {
        cbProveedor.setItems(proveedoresExternos);
        tipoProveedor = "externo"; 
    
    }
}


@FXML
private void clicGenerar(ActionEvent event) {
    // Obtener la fecha de entrega seleccionada
    LocalDate fechaEntrega = dpDiaEntrega.getValue();

    // Verificar que se haya seleccionado una fecha de entrega
    if (fechaEntrega == null) {
        Utilidades.mostrarDialogoSimple("Error", "Por favor, seleccione una fecha de entrega", Alert.AlertType.ERROR);
        return;
    }

    // Crear el objeto Pedido con la fecha de pedido y fecha de entrega
    Pedido proveedorSeleccionado = cbProveedor.getSelectionModel().getSelectedItem();
    if (proveedorSeleccionado == null) {
        Utilidades.mostrarDialogoSimple("Error", "Por favor, seleccione un proveedor", Alert.AlertType.ERROR);
        return;
    }

    // Obtener los productos del carrito
    ObservableList<Producto> productosCarrito = tvCarrito.getItems();

    // Verificar que el carrito no esté vacío
    if (productosCarrito.isEmpty()) {
        Utilidades.mostrarDialogoSimple("Error", "El carrito está vacío", Alert.AlertType.ERROR);
        return;
    }


        Pedido pedidoNuevo = new Pedido();
        pedidoNuevo.setFecha_pedido(Utilidades.obtenerFechaActual());
        pedidoNuevo.setFecha_entrega(fechaEntrega.toString());

      if (esEdicion){
                if (rbExternos.isSelected()){
                    pedidoNuevo.setIdPedido(pedido.getIdPedido());
                    int idProveedor = PedidoDAO.IdentificadorProveedor(cbProveedor.getValue().toString());              
                    pedidoNuevo.setIdProveedor(idProveedor);
                    actualizarPedidoExterno(pedidoNuevo);
             
              
           
      }
      
      if ( rbInternos.isSelected()){
                pedidoNuevo.setIdPedido(pedido.getIdPedido());
                 int idProveedores = PedidoDAO.IdentificadorProveedor(cbProveedor.getValue().toString());              
                pedidoNuevo.setIdSucursal(idProveedores);
                System.out.println("idpedido" + pedido.getIdPedido() + "id sucursal" + pedidoNuevo.getIdSucursal());
                actualizarPedidoInterno(pedidoNuevo);
            

      }   
      }else{
      
      if (rbExternos.isSelected()){
          pedidoNuevo.setIdProveedor(proveedorSeleccionado.getIdProveedor());
          guardarPedidoExterno(pedidoNuevo);
          Pedido pedidoTemporal = PedidoDAO.obtenerUltimoPedidoGuardado();
                int idPedido = pedidoTemporal.getIdPedido();
                //TO DO:
                registrarProductosPromocion( idPedido);
                
           
      }
      
      if ( rbInternos.isSelected()){
              pedidoNuevo.setIdSucursal(proveedorSeleccionado.getIdSucursal());
          guardarPedidoInterno(pedidoNuevo);
           Pedido pedidoTemporal = PedidoDAO.obtenerUltimoPedidoGuardado();
               int idPedido = pedidoTemporal.getIdPedido();
                //TO DO:
                registrarProductosPromocion( idPedido);
      
      }   
      
    // Limpiar el carrito
    carrito.clear();
    
    actualizarTablaCarrito();
    cbProveedor.getSelectionModel().clearSelection();
    dpDiaEntrega.setValue(null);

    // Mostrar mensaje de éxito
    Utilidades.mostrarDialogoSimple("Pedido Generado", "El pedido ha sido generado exitosamente", Alert.AlertType.INFORMATION);

      }
}
    
private void registrarProductosPromocion(int productoNuevo) {
    int tamano = carrito.size() - 1;
    for (int i = 0; i <= tamano; i++) {
        Producto producto = carrito.get(i);


        ProductoPedido productoPedido = new ProductoPedido(
          producto.getIdPedido(),
            /* idProducto */ producto.getIdProducto(),    // Obtén el valor correspondiente del objeto Producto
            /* Cantidad */ producto.getCantidad(),
            /* idProductoPedido */ productoNuevo
        );
        productoPedido.setIdPedido(productoNuevo);
        System.out.println("idpedido "+producto.getIdPedido());
        int codigoRespuesta = ProductoPedidoDAO.guardarProductoPedido(productoPedido);
        switch (codigoRespuesta) {
            case Constantes.ERROR_CONEXION:
                Utilidades.mostrarDialogoSimple("Error de conexión", "Por el momento no hay conexión, "
                        + "por favor inténtalo más tarde", Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                Utilidades.mostrarDialogoSimple("Error de consulta", "Ocurrió un error al registrar los productos ,"
                        + " por favor inténtalo más tarde", Alert.AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                Utilidades.mostrarDialogoSimple("Pedido Generado", "El pedido de los productos  "
                        + "se realizó con éxito", Alert.AlertType.INFORMATION);
                break;
        }
    }
}



@FXML
private void clicEliminar(ActionEvent event) {
    Producto productoSeleccionado = tvCarrito.getSelectionModel().getSelectedItem();
    if (productoSeleccionado != null) {
        // Elimina el producto seleccionado de la tabla
        tvCarrito.getItems().remove(productoSeleccionado);

        // Elimina el producto seleccionado del listado 'carrito'
        carrito.remove(productoSeleccionado);

        // Deshabilita el botón "Eliminar" después de la eliminación
        btnEliminar.setDisable(true);
    }
}






public static <T> void makeComboBoxSearchable(ComboBox<T> comboBox, Function<T, String> toString) {
    comboBox.setConverter(new StringConverter<T>() {
        @Override
        public String toString(T t) {
            return t == null ? "" : toString.apply(t);
        }

        @Override
        public T fromString(String s) {
            return comboBox.getItems().stream()
                    .filter(item -> toString.apply(item).equalsIgnoreCase(s))
                    .findFirst()
                    .orElse(null);
        }
    });

    comboBox.setEditable(false);

    final FilteredList<T> filteredItems = new FilteredList<>(comboBox.getItems(), item -> true);
    comboBox.setItems(filteredItems);

    comboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
        final T selected = comboBox.getSelectionModel().getSelectedItem();
        final TextField editor = comboBox.getEditor();

        Platform.runLater(() -> {
            if (selected == null || !toString.apply(selected).equalsIgnoreCase(editor.getText())) {
                filteredItems.setPredicate(item -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    return toString.apply(item).toLowerCase().contains(newValue.toLowerCase());
                });
            }
        });
    });
}

    @FXML
    private void clicRegresar(MouseEvent event) {
   
       Stage escenarioPrincipal = (Stage) cbProducto.getScene().getWindow();
        escenarioPrincipal.close();
    }

 @FXML
private void clicBuscarProducto(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
        String texto = autoTextField.getText();
        Producto productoSeleccionado = productos.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(texto))
                .findFirst()
                .orElse(null);
        
        if (productoSeleccionado != null) {
            cbProducto.getSelectionModel().select(productoSeleccionado);
            autoTextField.clear();
        }
    }
}


 public void inicializarInformacionFormulario(boolean esEdicion, Pedido pedido, INotificacionOperacion interfazNotificacion){
        this.esEdicion = esEdicion;
        this.pedido = pedido;
        this.interfazNotificacion = interfazNotificacion;
        
        if(esEdicion){
            cargarInformacionPedido();
          /*  actualizarTablaPromocion();*/
        }else{
            
        }
    }

  private void cargarInformacionPedido(){
      
       cbProveedor.setValue(pedido);
       Pedido proveedor = cbProveedor.getValue(); 
      System.out.println("pedido para la lista" + pedido);
       proveedoresExternos = FXCollections.observableArrayList();
      PedidoRespuesta respuestaExternos = PedidoDAO.obtenerProveedoresExternos();
    if (respuestaExternos.getCodigoRespuesta() == Constantes.OPERACION_EXITOSA) {
        ArrayList<Pedido> proveedoresExternosList = respuestaExternos.getPedidos();
         proveedoresExternos.addAll(proveedoresExternosList);
         if (proveedoresExternosList.stream().map(Object::toString).anyMatch(pedido.toString()::equals)) {
            rbExternos.setSelected(true);
                  }
        }
    String fechaEntregaString = pedido.getFecha_entrega();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
    LocalDate fechaEntrega = LocalDate.parse(fechaEntregaString, formatter); 
    dpDiaEntrega.setValue(fechaEntrega); 
      System.out.println("ID PEDIDO ANTES DE CARGAR PRODUCTOS" + pedido.getFecha_entrega() + pedido.getNombre_proveedor() + pedido.getIdPedido());
    cargarProductosPedido();
    
    }

 private void cargarProductosPedido(){
        carrito = FXCollections.observableArrayList();
        System.out.println("IDPEDIDO" + pedido.getIdPedido());
         ProductoRespuesta respuestaBD =  ProductoDAO.obtenerInformacionPedido(pedido.getIdPedido());
      
        carrito.addAll(respuestaBD.getProductos());
        tvCarrito.setItems(carrito);
        actualizarTablaCarrito();
             
    }

 private void actualizarPedidoExterno(Pedido pedidoActualizar){
        int codigoRespuesta = PedidoDAO.modificarPedidoExterno(pedidoActualizar);
        switch(codigoRespuesta){
            case Constantes.ERROR_CONEXION:
                Utilidades.mostrarDialogoSimple("Error de conexion","Por el momento no hay conexión, "
                        + "por favor inténtelo más tarde", Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                Utilidades.mostrarDialogoSimple("Error de consulta", "Ocurrió un error al modificar el pedido,"
                        + " por favor inténtelo más tarde", Alert.AlertType.WARNING); 
                break;
            case Constantes.OPERACION_EXITOSA:
                Utilidades.mostrarDialogoSimple("Pedido modificado", "La actualización de el "
                        + "pedido se realizó con éxito", Alert.AlertType.INFORMATION);
                     ProductoPedidoDAO.eliminarProductoPedido(pedido.getIdPedido());
                    registrarProductosPromocion( pedido.getIdPedido());
                /*cerrarVentana();*/
              
        }
}

 
 
 private void actualizarPedidoInterno(Pedido pedidoActualizar){
        int codigoRespuesta = PedidoDAO.modificarPedidoInterno(pedidoActualizar);
        switch(codigoRespuesta){
            case Constantes.ERROR_CONEXION:
                Utilidades.mostrarDialogoSimple("Error de conexion","Por el momento no hay conexión, "
                        + "por favor inténtelo más tarde", Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                Utilidades.mostrarDialogoSimple("Error de consulta", "Ocurrió un error al modificar el pedido,"
                        + " por favor inténtelo más tarde", Alert.AlertType.WARNING); 
                break;
            case Constantes.OPERACION_EXITOSA:
                Utilidades.mostrarDialogoSimple("Pedido modificado", "La actualización del "
                        + "pedido se realizó con éxito", Alert.AlertType.INFORMATION);
                     ProductoPedidoDAO.eliminarProductoPedido(pedido.getIdPedido());
                    registrarProductosPromocion( pedido.getIdPedido());
                /*cerrarVentana();*/
              
        }
}
 
 
 
}
    



