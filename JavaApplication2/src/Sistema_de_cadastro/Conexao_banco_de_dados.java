/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistema_de_cadastro;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.UUID;

/**
 *
 * @author felip
 */

// Essa classe é um singleton para evitar mutiplas conexões ao banco de dados

public class Conexao_banco_de_dados{
   private String url = "jdbc:postgresql://localhost:5432/landix";
   private String usuario = "postgres";
   private String senha = "1234";
   private Connection connection = null;
   private static Conexao_banco_de_dados cb=null;
  
  public static Conexao_banco_de_dados getConexao_banco_de_dados(){
      if(cb == null){
          cb = new Conexao_banco_de_dados();
      }
      
      return cb;
  }
  
  private  Conexao_banco_de_dados(){
 try{
    Class.forName("org.postgresql.Driver");
    
    connection = (Connection)DriverManager.getConnection(url, usuario, senha);
}catch(ClassNotFoundException ex){
    
    ex.printStackTrace();
    
}catch(SQLException e){
    e.printStackTrace();
}   
}
   
   public Connection getConnection(){

       return this.connection;
       
   }
   
   public void cadastrarVendedor(String nome,int cdtab,Date dtnasc){
        
         String sql = "INSERT  INTO bancolandix.vendedores (\"CDVEND\",\"DSNOME\",\"CDTAB\",\"DTNASC\") VALUES (?,?,?,?)";
         UUID cd = UUID.randomUUID();
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setObject(1, cd.toString());
            ps.setString(2,nome);
            ps.setInt(3, cdtab);
            ps.setDate(4, dtnasc);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         
    }
   public void EditarVendedor(String nome,int cdtab,Date dtnasc){
   
     
       
       try{
           
       PreparedStatement ps =this.connection.prepareStatement("UPDATE bancolandix.vendedores SET \"DSNOME\" = ?,\"CDTAB\" = ?,\"DTNASC\"= ? WHERE \"DSNOME\"="+"\'"+nome+"\'" );
       ps.setString(1,nome);
       ps.setInt(2,cdtab);
       ps.setDate(3,dtnasc );
       ps.executeUpdate();
       ps.close();
       
       
       }catch(SQLException ex){
           
           ex.printStackTrace();
       }
       
       }
   public void cadastrarCliente(String nome,String idtipo,String CDVEND,double n){
   
       //DecimalFormat df  = new DecimalFormat("##.###############");
       UUID cd = UUID.randomUUID();
       try{
           
       PreparedStatement ps =this.connection.prepareStatement("INSERT INTO bancolandix.clientes (\"CDCL\",\"DSNOME\",\"IDTIPO\",\"CDVEND\",\"DSLIM\") VALUES (?,?,?,?,?)");
       ps.setObject(1,cd.toString());
       ps.setString(2,nome);
       ps.setString(3, idtipo);
       ps.setString(4,CDVEND );
       ps.setDouble(5,n);
       ps.executeUpdate();
       ps.close();
       
       
       }catch(SQLException ex){
           
           ex.printStackTrace();
       }
       
       }
   public void EditarCliente(String nome,String idtipo,String CDVEND,double n){
   
     
       
       try{
           
       PreparedStatement ps =this.connection.prepareStatement("UPDATE bancolandix.clientes SET \"DSNOME\" = ?,\"IDTIPO\" = ?,\"CDVEND\"= ?,\"DSLIM\" =? WHERE \"DSNOME\"="+"\'"+nome+"\'" );
       ps.setString(1,nome);
       ps.setString(2, idtipo);
       ps.setString(3,CDVEND );
       ps.setDouble(4,n);
       ps.executeUpdate();
       ps.close();
       
       
       }catch(SQLException ex){
           
           ex.printStackTrace();
       }
       
       }
   
   
   public String[] verificaVendedor(String nome){
       String [] dados =  new String[3]; // dados serao retornados no formato de string
       
       if(nome == ""){
           
           return null;
       }
       
       try{
           Statement s = this.connection.createStatement();
           ResultSet rs = s.executeQuery("SELECT \"DSNOME\",\"CDTAB\",\"DTNASC\" FROM bancolandix.vendedores WHERE \"DSNOME\" = "+"\'"+nome+"\'");
          
          
          
          if(rs.next()){
           dados[0]= rs.getString("DSNOME");
           dados[1]= Integer.toString(rs.getInt("CDTAB"));
           dados[2]= rs.getDate("DTNASC").toString();
          }else{
              return null;
          }
       s.close();
       rs.close();
       }catch(SQLException ex){
           ex.printStackTrace();
       }
      
       return dados;
   }
   public String[] verificaCliente(String nome){
       String [] dados =  new String[3]; // dados serao retornados no formato de string
       
       if(nome == ""){
           
           return null;
       }
       
       try{
           Statement s = this.connection.createStatement();
           ResultSet rs = s.executeQuery("SELECT \"DSNOME\",\"IDTIPO\",\"DSLIM\" FROM bancolandix.clientes WHERE \"DSNOME\" = "+"\'"+nome+"\'");
          
          
          
          if(rs.next()){
           dados[0]= rs.getString("DSNOME");
           dados[1]= rs.getString("IDTIPO");
           dados[2]= Double.toString(rs.getDouble("DSLIM"));
          }else{
              return null;
          }
       s.close();
       rs.close();
       }catch(SQLException ex){
           ex.printStackTrace();
       }
      
       return dados;
   }
   
   public ResultSet BuscaClientes(String vendedor)
   {      
         
       try{
       Statement s = this.connection.createStatement();
       ResultSet vend;
       ResultSet clientes=null;
       if(vendedor == "TODOS"){
         clientes = s.executeQuery("SELECT \"DSNOME\",\"IDTIPO\",\"DSLIM\" FROM bancolandix.clientes");
       }else{
        vend = s.executeQuery("SELECT \"CDVEND\" FROM bancolandix.vendedores WHERE \"DSNOME\" = "+"\'"+vendedor+"\'" );
         if(vend.next()){
          String n = vend.getString("CDVEND");
       
          clientes = s.executeQuery("SELECT \"DSNOME\",\"IDTIPO\",\"DSLIM\" FROM bancolandix.clientes WHERE \"CDVEND\" = "+"\'"+n+"\'");
       }
         
       }
       return clientes;
       }catch(SQLException ex){
           ex.printStackTrace();
       }
       return null;   
   }
   
   public ResultSet BuscaVendedores(){
       try{
           Statement s = this.connection.createStatement();
           ResultSet vend = s.executeQuery("SELECT \"DSNOME\" FROM bancolandix.vendedores");
           return vend;
       }catch(SQLException ex){
           ex.printStackTrace();
       }
       return null;
   }
   
   
   
   public void ExcluirVendedor(String vendedor){
       
       try{
           Statement ps = this.connection.createStatement();
           ps.executeUpdate("DELETE FROM bancolandix.vendedores WHERE \"DSNOME\" = "+"\'"+vendedor+"\'");
           ps.close();
       }catch(SQLException ex){
           ex.printStackTrace();
       }
   }
    public void ExcluirCliente(String cliente){
       
       try{
           Statement ps = this.connection.createStatement();
           ps.executeUpdate("DELETE FROM bancolandix.clientes WHERE \"DSNOME\" = "+"\'"+cliente+"\'");
           ps.close();
       }catch(SQLException ex){
           ex.printStackTrace();
       }
   }
   
   public String getVendedorCD(String nome){
       
       if(nome == ""){
           
           return null;
       }
       
       try{
           Statement s = this.connection.createStatement();
           ResultSet rs = s.executeQuery("SELECT \"CDVEND\" FROM bancolandix.vendedores WHERE \"DSNOME\"="+"\'"+nome+"\'" );
           
           rs.next();
           return rs.getString("CDVEND");
           
       }catch(SQLException ex){
           
           
       }
       return null;
   }
   
}



