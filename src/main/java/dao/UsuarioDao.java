/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static conexion.Conexion.getConnection;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {

    public boolean validarUsuario(Usuario usu) {
        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usuario WHERE email=? AND password=? AND estado=1");
            ps.setString(1, usu.getEmail());
            ps.setString(2, usu.getPassword());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usu.setIdUsuario(rs.getInt("idUsuario"));
                usu.setRol(rs.getString("rol"));
                usu.setEstado(rs.getInt("estado"));
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Usuario> listarUsuarios() {
        List<Usuario> listaUsuarios = new ArrayList<Usuario>();
        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT idUsuario, codigo, email, password, rol, estado FROM usuario ORDER BY idUsuario DESC");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Usuario user = new Usuario();
                user.setIdUsuario(rs.getInt("idUsuario"));
                user.setCodigo(rs.getString("codigo"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRol(rs.getString("rol"));
                user.setEstado(rs.getInt("estado"));
                listaUsuarios.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaUsuarios;
    }
    
    // Registro actualizado
    public static int registrarUsuario(Usuario usu) {
        int idRegistrado = 0;
        try {
            Connection con = getConnection();
            String query = "INSERT INTO usuario (email,password,rol,estado) VALUES (?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, usu.getEmail());
            ps.setString(2, usu.getPassword());
            ps.setString(3, usu.getRol());
            ps.setInt(4, usu.getEstado());
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idRegistrado = rs.getInt(1);
                
                String codigo = "U" + String.format("%04d", idRegistrado);
                
                ps = con.prepareStatement("UPDATE usuario SET codigo=? WHERE idUsuario=?");
                ps.setString(1, codigo);
                ps.setInt(2, idRegistrado);
                ps.executeUpdate();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idRegistrado;
    }
    
    // Actualizar registro para insertar codigo
    /*public static int AgregarCodigo(int idUsuario, String code) {
        int estado = 0;
        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE usuario SET codigo=? WHERE idUsuario=?");
            ps.setString(1, code);
            ps.setInt(2, idUsuario);
            estado = ps.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
        return estado;
    }*/
    
    public boolean validarEmail(String email) {
        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usuario WHERE email=?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
            
        } catch (Exception e) {

        }
        return false;
    }
    
    public boolean validarUsuarioInactivo(Usuario usu) {
        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usuario WHERE email=? AND password=? AND estado=0");
            ps.setString(1, usu.getEmail());
            ps.setString(2, usu.getPassword());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usu.setIdUsuario(rs.getInt("idUsuario"));
                usu.setRol(rs.getString("rol"));
                usu.setEstado(rs.getInt("estado"));
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
