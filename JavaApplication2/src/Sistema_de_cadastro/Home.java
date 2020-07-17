/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistema_de_cadastro;

import static Sistema_de_cadastro.Conexao_banco_de_dados.getConexao_banco_de_dados;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;


/**
 *
 * @author felip
 */
public class Home extends JFrame{
    private JScrollPane listVendedor = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // a lista se move apenas verticalmente 
    private JScrollPane listClientes = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JLabel textVendedor = new JLabel("Vendedor:",SwingConstants.CENTER);
    private JLabel textCliente = new JLabel("Cliente:",SwingConstants.CENTER);
    private JButton btnManutencaoVendedor = new JButton("EditarVendedor");
    private JButton btnManutencaoCliente = new JButton("EditarCliente");
    private JButton btnCriarVendedor = new JButton("Criar Vendedor:");
    private JButton btnCriarCliente = new JButton("Criar Cliente");
    private String Svendedor="";
    private String Scliente="";
    
    public Home(){
       
        this.configuraFrame();
        this.AddVendedorList();
        this.AddComponentes();
        this.AddBotoes();
        this.setVisible(true);
       
    }
    
    private void configuraFrame(){
        this.setResizable(false);
        this.setTitle("LDXPS");
        this.setLayout(new BorderLayout());
        this.add(new JLabel("Home",SwingConstants.CENTER),BorderLayout.NORTH);
        this.setBounds(500, 250, 500, 410);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        
    }
    
    private void AddComponentes(){
       
        GridLayout gridlayout= new GridLayout(2,2,10,50);
        JPanel panel = new JPanel();
        panel.setLayout(gridlayout);
        
        
        
        
                 panel.add(this.textVendedor);
                 panel.add(this.listVendedor);
                 panel.add(this.textCliente);
                 panel.add(this.listClientes);
                 
                this.add(panel,BorderLayout.CENTER);
    }
    
    private void AddBotoes(){
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        
        this.btnCriarCliente.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                  CadastroCliente c = new CadastroCliente();   
            }
        
        
        });
        p.add(this.btnCriarCliente);
        
        this.btnCriarVendedor.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               CadastroVendedor c = new CadastroVendedor();
            }
        
        
        });
        
        p.add(this.btnCriarVendedor);
        
        this.btnManutencaoCliente.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               if(Home.this.Scliente == ""){
                   JPanel panel = new JPanel();
                   JOptionPane.showMessageDialog( panel,"Selecione um cliente");               
               }else{ 
                CadastroCliente c = new CadastroCliente(Home.this.Scliente);
               }
            }
        
        
        });
        p.add(this.btnManutencaoCliente);
        
        this.btnManutencaoVendedor.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Home.this.Svendedor =="" || Home.this.Svendedor=="TODOS"){
                    JPanel panel = new JPanel();
                   JOptionPane.showMessageDialog( panel,"Selecione um Vendedor");     
            }else{
                    CadastroVendedor c = new CadastroVendedor(Home.this.Svendedor);
                }
            }
        });
        
        p.add(this.btnManutencaoVendedor);
        
        this.add(p,BorderLayout.SOUTH);
    }
    
    private void AddVendedorList(){
        try{
        ResultSet rs = getConexao_banco_de_dados().BuscaVendedores();
        if(rs==null)return;
        
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(230,500));
        p.setBackground(Color.WHITE);
        while(rs.next()){
            JButton b = new JButton(rs.getString("DSNOME"));
            b.addActionListener(new ActionListenerVendedor(rs.getString("DSNOME")));
            b.setBackground(Color.WHITE);
            b.setPreferredSize(new Dimension(230,20));
            b.setOpaque(true);
            p.add(b);
        }
        JButton b = new JButton("TODOS");
        b.setBackground(Color.WHITE);
        b.setPreferredSize(new Dimension(230,20));
        b.setOpaque(true);
        b.addActionListener(new ActionListenerVendedor("TODOS"));
        p.add(b);
        this.listVendedor.getViewport().add(p);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        
    }
    
    private void AddClienteList(String nome){
    try{
        ResultSet rs = getConexao_banco_de_dados().BuscaClientes(nome);
        if(rs==null)return;
        
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(230,500));
        p.setBackground(Color.WHITE);
        while(rs.next()){
            JButton b = new JButton(rs.getString("DSNOME"));
            b.addActionListener(new ActionListenerCliente(rs.getString("DSNOME")));
            
            b.setBackground(Color.WHITE);
            b.setPreferredSize(new Dimension(230,20));
            b.setOpaque(true);
            p.add(b);
        }
        this.listClientes.getViewport().add(p);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
}
    class ActionListenerVendedor implements ActionListener{
        String nome;
        
        public ActionListenerVendedor(String n){
            this.nome = n;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
           Home.this.AddClienteList(nome);
           Home.this.Svendedor=nome;
           
        }
        
    }
    
    class ActionListenerCliente implements ActionListener{
        String nome;
        String id;
        double lm;
        
        public ActionListenerCliente(String n){
           
           
            this.nome = n;
           
            
            
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
           Home.this.Scliente=nome;
            String[] rs = getConexao_banco_de_dados().verificaCliente(nome);
            this.id = rs[1];
            this.lm = Double.parseDouble(rs[2]);
           JPanel panel = new JPanel();
           
           JOptionPane.showMessageDialog( panel,"Cliente:  \n"
                    +                           "Nome: "+this.nome+"\n"
                    +                           "TIPO de ID: "+this.id+"\n"
                    +                           "limite: "+Double.toString(lm)); 
           
        }
        
    }
    
    
    
    public static void main(String args[]){
    Home h = new Home();
}
}

