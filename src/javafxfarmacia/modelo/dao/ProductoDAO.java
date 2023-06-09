/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxfarmacia.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxfarmacia.modelo.ConexionBD;
import javafxfarmacia.modelo.pojo.Producto;
import javafxfarmacia.modelo.pojo.ProductoRespuesta;
import javafxfarmacia.utils.Constantes;

/**
 *
 * @author kikga
 */
public class ProductoDAO {
    public static ProductoRespuesta obtenerInformacionProducto(){
        ProductoRespuesta respuesta = new ProductoRespuesta();
        respuesta.setCodigoRespuesta(Constantes.OPERACION_EXITOSA);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String consulta = "select idProducto, nombre, fechaVencimiento, precio, ventaControlada, sucursal_idSucursal, \n" +
                                "cantidad, presentacion, nombreSucursal, foto \n" +
                                "from producto \n" +
                                "inner join sucursal on idSucursal = sucursal_idSucursal order by fechaVencimiento asc;";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Producto> productosConsulta = new ArrayList();
                while(resultado.next()){
                    Producto producto = new Producto();
                    producto.setIdProducto(resultado.getInt("idProducto"));
                    producto.setFechaVencimiento(resultado.getString("fechaVencimiento"));
                    producto.setNombre(resultado.getString("nombre"));
                    producto.setPrecio(resultado.getDouble("precio"));
                    producto.setVentaControlada(resultado.getBoolean("ventaControlada"));
                    producto.setIdSucursal(resultado.getInt("sucursal_idSucursal"));
                    producto.setNombreSucursal(resultado.getString("nombreSucursal"));
                    producto.setCantidad(resultado.getInt("cantidad"));
                    producto.setPresentacion(resultado.getString("presentacion"));
                    producto.setFoto(resultado.getBytes("foto"));
                    productosConsulta.add(producto);
                }
                respuesta.setProductos(productosConsulta);
                conexionBD.close();
            } catch (SQLException e) {
                respuesta.setCodigoRespuesta(Constantes.ERROR_CONSULTA);
            }
        }else{
            respuesta.setCodigoRespuesta(Constantes.ERROR_CONEXION);
        }
        return respuesta;
    }
    public static ProductoRespuesta obtenerInformacionProductoSucursal(){
        int sucursal = 1;
        ProductoRespuesta respuesta = new ProductoRespuesta();
        respuesta.setCodigoRespuesta(Constantes.OPERACION_EXITOSA);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String consulta = "select idProducto, nombre, fechaVencimiento, precio, ventaControlada, sucursal_idSucursal, \n" +
                                "cantidad, presentacion, nombreSucursal \n" +
                                "from producto \n" +
                                "inner join sucursal on idSucursal = sucursal_idSucursal "+
                                 "where sucursal.idSucursal = ? order by fechaVencimiento asc;";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setInt(1, sucursal);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Producto> productosConsulta = new ArrayList();
                while(resultado.next()){
                    Producto producto = new Producto();
                    producto.setIdProducto(resultado.getInt("idProducto"));
                    producto.setFechaVencimiento(resultado.getString("fechaVencimiento"));
                    producto.setNombre(resultado.getString("nombre"));
                    producto.setPrecio(resultado.getDouble("precio"));
                    producto.setVentaControlada(resultado.getBoolean("ventaControlada"));
                    producto.setIdSucursal(resultado.getInt("sucursal_idSucursal"));
                    producto.setNombreSucursal(resultado.getString("nombreSucursal"));
                    producto.setCantidad(resultado.getInt("cantidad"));
                    producto.setPresentacion(resultado.getString("presentacion"));
                    productosConsulta.add(producto);
                }
                respuesta.setProductos(productosConsulta);
                conexionBD.close();
            } catch (SQLException e) {
                respuesta.setCodigoRespuesta(Constantes.ERROR_CONSULTA);
            }
        }else{
            respuesta.setCodigoRespuesta(Constantes.ERROR_CONEXION);
        }
        return respuesta;
    }
    
    public static ProductoRespuesta obtenerInformacionProductoAdqui(int idProducto){
   ProductoRespuesta respuesta = new ProductoRespuesta();
    Connection conexionBD = ConexionBD.abrirConexionBD();
      if(conexionBD != null){
            try{
                String consulta = "SELECT idProducto,nombre,precio FROM producto";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList <Producto> productos = new ArrayList();
                
                while(resultado.next()){
                    Producto producto = new Producto();
                    producto.setIdProducto(resultado.getInt("idProducto"));
                    producto.setNombre(resultado.getString("nombre"));
                    producto.setPrecio(resultado.getFloat("precio"));
                    productos.add(producto);
                    
                
                }
                respuesta.setProductos(productos);
                respuesta.setCodigoRespuesta(Constantes.OPERACION_EXITOSA);

                conexionBD.close();
          
                
            }catch(SQLException ex){
                respuesta.setCodigoRespuesta(Constantes.ERROR_CONSULTA);
            }
        }else{
            respuesta.setCodigoRespuesta(Constantes.ERROR_CONEXION);
        }
        return respuesta;
   
    }
    
    
    
    
    
    
    
    public static ProductoRespuesta obtenerInformacionBusqueda(String busqueda){
        ProductoRespuesta respuesta = new ProductoRespuesta();
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
                try{
                    String consulta = "SELECT idProducto,nombre FROM producto";
                    PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                    prepararSentencia.setString(1, "%" + busqueda + "%");
                    ResultSet resultado = prepararSentencia.executeQuery();
                    ArrayList<Producto> productosConsulta = new ArrayList();
                    while(resultado.next()){
                        Producto producto = new Producto();
                        producto.setIdProducto(resultado.getInt("idProducto"));
                        producto.setFechaVencimiento(resultado.getString("fechaVencimiento"));
                        producto.setNombre(resultado.getString("nombre"));
                        producto.setPrecio(resultado.getDouble("precio"));
                        producto.setVentaControlada(resultado.getBoolean("ventaControlada"));
                        producto.setIdSucursal(resultado.getInt("sucursal_idSucursal"));
                        producto.setNombreSucursal(resultado.getString("nombreSucursal"));
                        producto.setCantidad(resultado.getInt("cantidad"));
                        producto.setPresentacion(resultado.getString("presentacion"));
                        productosConsulta.add(producto);
                    }
                    respuesta.setProductos(productosConsulta);
                    conexionBD.close();
                } catch (SQLException e) {
                    respuesta.setCodigoRespuesta(Constantes.ERROR_CONSULTA);
                }
            }else{
                respuesta.setCodigoRespuesta(Constantes.ERROR_CONEXION);
            }
        return respuesta;
    }
    
    public static int guardarProducto(Producto productoNuevo){
        int respuesta;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String sentencia = "insert into producto(nombre, fechaVencimiento, precio, "
                        + "ventaControlada, sucursal_idSucursal, cantidad, presentacion, foto)"
                        + " values (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setString(1, productoNuevo.getNombre());
                if(productoNuevo.getFechaVencimiento() != null)
                    prepararSentencia.setString(2, productoNuevo.getFechaVencimiento());
                else
                    prepararSentencia.setNull(2, java.sql.Types.VARCHAR);
                prepararSentencia.setDouble(3, productoNuevo.getPrecio());
                prepararSentencia.setBoolean(4, productoNuevo.isVentaControlada());
                prepararSentencia.setInt(5, productoNuevo.getIdSucursal());
                prepararSentencia.setInt(6, productoNuevo.getCantidad());
                prepararSentencia.setString(7, productoNuevo.getPresentacion());
                if(productoNuevo.getFoto() != null)
                    prepararSentencia.setBytes(8, productoNuevo.getFoto());
                else
                    prepararSentencia.setNull(8, java.sql.Types.LONGVARBINARY);
                int filasAfectadas = prepararSentencia.executeUpdate();
                respuesta = (filasAfectadas == 1) ?
                        Constantes.OPERACION_EXITOSA : Constantes.ERROR_CONSULTA;
                conexionBD.close();
            } catch (SQLException e) {
                respuesta = Constantes.ERROR_CONSULTA;
            }
        }else{
            respuesta = Constantes.ERROR_CONEXION;
        }
        return respuesta;
    }
    
    public static int modificarProducto(Producto productoEdicion){
        int respuesta;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String sentencia = "update producto set nombre = ?, fechaVencimiento = ?,"
                        + " precio = ?, ventaControlada = ?, sucursal_idSucursal = ?, "
                        + "cantidad = ?, presentacion = ?, foto = ? where idProducto = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setString(1, productoEdicion.getNombre());
                if(productoEdicion.getFechaVencimiento() != null)
                    prepararSentencia.setString(2, productoEdicion.getFechaVencimiento());
                else
                    prepararSentencia.setNull(2, java.sql.Types.VARCHAR);
                prepararSentencia.setDouble(3,productoEdicion.getPrecio());
                prepararSentencia.setBoolean(4, productoEdicion.isVentaControlada());
                prepararSentencia.setInt(5, productoEdicion.getIdSucursal());
                prepararSentencia.setInt(6, productoEdicion.getCantidad());
                prepararSentencia.setString(7,productoEdicion.getPresentacion());
                if(productoEdicion.getFoto() != null)
                    prepararSentencia.setBytes(8, productoEdicion.getFoto());
                else
                    prepararSentencia.setNull(8, java.sql.Types.LONGVARBINARY);
                prepararSentencia.setInt(9, productoEdicion.getIdProducto());
                int filasAfectadas = prepararSentencia.executeUpdate();
                respuesta = (filasAfectadas == 1) ?
                        Constantes.OPERACION_EXITOSA : Constantes.ERROR_CONSULTA;
                conexionBD.close();
            } catch (SQLException e) {
                respuesta = Constantes.ERROR_CONSULTA;
            }
        }else{
            respuesta = Constantes.ERROR_CONEXION;
        }
        return respuesta;
    }
    
    public static ProductoRespuesta obtenerInformacionPedido(int idPedido){
        ProductoRespuesta respuesta = new ProductoRespuesta();
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
                try{
                    String consulta = "SELECT producto.nombre, producto.precio,  producto_pedido.idProducto, producto_pedido.cantidad \n" +
                "FROM producto_pedido \n" +
                "INNER JOIN producto ON producto.idProducto = producto_pedido.idProducto\n" +
                " WHERE idPedido = ?;";
                    PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                    prepararSentencia.setInt(1, idPedido);
                    ResultSet resultado = prepararSentencia.executeQuery();
                    ArrayList <Producto> productos = new ArrayList();
                    while(resultado.next()){
                        Producto producto = new Producto();
                        producto.setIdProducto(resultado.getInt("idProducto"));
                        producto.setNombre(resultado.getString("nombre"));
                        producto.setPrecioUnitario(resultado.getInt("precio"));
                        producto.setCantidad(resultado.getInt("cantidad"));
                        productos.add(producto);
                        System.out.println("ID PRODUCTO: "+producto.getIdProducto());
                    
                    }
                    respuesta.setProductos(productos);
                    respuesta.setCodigoRespuesta(Constantes.OPERACION_EXITOSA);

                    conexionBD.close();
            
                    
                }catch(SQLException ex){
                    respuesta.setCodigoRespuesta(Constantes.ERROR_CONSULTA);
                }
            }else{
                respuesta.setCodigoRespuesta(Constantes.ERROR_CONEXION);
            }
        return respuesta;
    }
    
    public static int eliminarProducto(int idProducto){
        int respuesta;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try{
                String sentencia = "delete from producto where idProducto = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setInt(1, idProducto);
                int filasAfectadas = prepararSentencia.executeUpdate();
                respuesta = (filasAfectadas == 1) ? 
                        Constantes.OPERACION_EXITOSA : Constantes.ERROR_CONSULTA;
                conexionBD.close();
            }catch(SQLException e){
                respuesta = Constantes.ERROR_CONSULTA;
            }
        }else{
            respuesta = Constantes.ERROR_CONEXION;
        }
        return respuesta;
    }
}
