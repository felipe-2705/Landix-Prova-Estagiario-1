/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistema_de_cadastro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author felip
 */
public class Conexao_banco_de_dados{
   private String url = "jdbc:postgresql://localhost:5432/landix";
   private String usuario = "postgres";
   private String senha = "1234";
   private Connection connection = null;

   public Conexao_banco_de_dados(){
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
   
   
   
}



