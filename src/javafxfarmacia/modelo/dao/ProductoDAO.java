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
                                "cantidad, presentacion, nombreSucursal \n" +
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
    
    public static ProductoRespuesta obtenerInformacionBusqueda(String busqueda){
        ProductoRespuesta respuesta = new ProductoRespuesta();
        respuesta.setCodigoRespuesta(Constantes.OPERACION_EXITOSA);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String consulta = "SELECT * FROM producto WHERE nombre LIKE ?;";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setString(1, busqueda);
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
}