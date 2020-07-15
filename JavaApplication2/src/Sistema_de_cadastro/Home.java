/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistema_de_cadastro;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;


/**
 *
 * @author felip
 */
public class Home extends JFrame{
    private JScrollPane listVendedor; // posicao 0,1
    private JScrollPane listClientes; // posicao 1,1 
    private JLabel textVendedor;
    private JLabel textCliente;
    private JButton btnManutencaoVendedor;
    private JButton btnManutencaoCliente;
    private JButton btnCriarVendedor;
    private JButton btnCriarCliente;
    private JPanel [][] panelHolder = new JPanel[3][2]; // nao tem a posição 0,1 nem a 1,1. pois estas sao scrollpane.
    private ArrayList<JButton> botoesVendedores = new ArrayList(); // Guarda os Vendedores pesquisados no banco de dados.
    public Home(){
       
        this.setResizable(false);
        this.setTitle("Home");
       // this.setBounds(500,250,300,310);
        this.setBounds(500, 250, 500, 410);
        GridLayout gridlayout= new GridLayout(3,2,10,50);
        
        this.setLayout(new BorderLayout());
        this.add(new JLabel("Home",SwingConstants.CENTER),BorderLayout.NORTH);
        JPanel panel = new JPanel();
        panel.setLayout(gridlayout);
        this.add(panel,BorderLayout.CENTER);
        
        
        for(int i = 0;i<3;i++)
        {
            for(int j= 0;j<2;j++){
                if(i==0 && j==1){
                 this.listVendedor = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // a lista se move apenas verticalmente   
                 panel.add(this.listVendedor);
                 
                }else if(i==1 && j==1){
                  this.listClientes = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                 panel.add(this.listClientes);
                }else{
                this.panelHolder[i][j]=new JPanel();
                panel.add(panelHolder[i][j]);
                }
            }
        }
        
        
        
      //botões 
      
      this.btnManutencaoVendedor = new JButton("Editar Vendedor");
      this.btnManutencaoCliente = new JButton("Editar Cliente");
      this.btnCriarVendedor = new JButton("Criar Vendedor");
      this.btnCriarCliente = new JButton("Criar Cliente");
      this.panelHolder[2][0].setLayout(new BorderLayout(2,2));
      this.panelHolder[2][0].add(this.btnManutencaoVendedor,BorderLayout.PAGE_START);
      this.panelHolder[2][0].add(this.btnCriarVendedor,BorderLayout.PAGE_END);
      this.panelHolder[2][1].setLayout(new BorderLayout());
      this.panelHolder[2][1].add(this.btnManutencaoCliente,BorderLayout.PAGE_START);
      this.panelHolder[2][1].add(this.btnCriarCliente,BorderLayout.PAGE_END);
        
      // opção do Vendedor
      this.textVendedor = new JLabel();
      this.textVendedor.setText("Vendedor");
      this.panelHolder[1][0].add(this.textVendedor);
  
      
      
      //opcao do Cliente
      
      this.textCliente = new JLabel();
      this.textCliente.setText("Cliente");
      this.panelHolder[0][0].add(this.textCliente);
     
      
      
      
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.setVisible(true);

    }
    
    private void BuscaVendedores(){
        Conexao_banco_de_dados conexao = new Conexao_banco_de_dados();
        if(conexao.getConnection()== null){
            System.out.println("Não Foi Possivel Conectar ao Servidor");
            return;
        }
        try{
        Statement s = conexao.getConnection().createStatement();
        ResultSet res = s.executeQuery("select DSNOME from VENDEDORES order by DSNOME"); // pesquisa os nomes dos vendedores no banco de dados.
        
        
        while(res.next()){
          JButton b = new JButton(res.getString("DSNOME"));  
          this.botoesVendedores.add(b);   // adiciona os botoes na lista de botoes para que possa adicionar actionlistener depois
          
          this.listVendedor.add(b);  // adiciona os botoes no Scrollpane para serem exibidos ao usuario
         
        }
                }catch(Exception e){
                    
                    e.printStackTrace();
                }
    }
    
    
    
    public static void main(String args[]){
    Home h = new Home();
}
}

