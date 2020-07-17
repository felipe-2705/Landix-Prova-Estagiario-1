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
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author felip
 */
public class CadastroCliente extends JFrame {
    // indicadores de campos para o usuario 
    private JLabel titulo = new JLabel("Cadastro Cliente",SwingConstants.CENTER);
    private JLabel nome = new JLabel("Nome: ",SwingConstants.CENTER);
    private JLabel tipoPessoa = new JLabel("Tipo de Pessoa:",SwingConstants.CENTER); // Se é uma pessoa PF ou PJ
    private JLabel limiteCredito = new JLabel("Limite de Credito: ",SwingConstants.CENTER);
    private JLabel vendedor = new JLabel("Vendedor: ",SwingConstants.CENTER);
    
// componentes para inserção ou exibição de dados
    private JTextField Tnome = new JTextField("");
    private JTextField TtipoPessoa= new JTextField("PF ou PJ");
    private JScrollPane ListVendedor;
    private JTextField TlimiteCredito = new JTextField("");

//Botoes de Funcoes da Tela    
    private JButton btnConfirmar = new JButton("Confimar");
    private JButton btnExcluir = new JButton("Excluir");
    private JButton btnCriarVendedor = new JButton("Criar Vendedor");

//Vendedor Selecionado
    private  String vendedorNome="";
    private  boolean clientejacadastrado = false;
    
    public CadastroCliente(){
        
        this.ConfiguraFrame(); // set tamanho de janela,titulo, Layout
        
        this.ConfiguraComponentes();
        
        this.AddVendedoresList();
        
        this.AddComponentesPrincipais();
        
        this.AddBotoes();
        
        this.setVisible(true);
        
    }
    
    public CadastroCliente(String n){
        
        this.ConfiguraFrame(); // set tamanho de janela,titulo, Layout
        
        this.ConfiguraComponentes();
        
        this.AddVendedoresList();
        
        this.AddComponentesPrincipais();
        
        this.AddBotoes();
        
        this.Tnome.setText(n);
        this.setVisible(true);
        
        
    }
    
    private void ConfiguraFrame(){
        this.setTitle("LDXPS");
        this.setBounds(500, 250, 400, 310);
        this.setLayout(new BorderLayout());
        
        this.add(this.titulo,BorderLayout.NORTH);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void AddComponentesPrincipais(){
        JPanel panel = new JPanel();
        GridLayout grid = new GridLayout(4,2,5,5);
        panel.setLayout(grid);
        JPanel [][] panelHolder = new JPanel[4][2];
        
        for(int i = 0;i<4;i++){
            
            for(int j=0; j<2;j++){
                if(i==2&&j==1){
                   panel.add(this.ListVendedor);
                   
                }else{
                
                panelHolder[i][j]=new JPanel();
                panel.add(panelHolder[i][j]);
                }
            }
        }
        
        panelHolder[0][0].add(this.nome);
        panelHolder[0][1].add(this.Tnome);
        panelHolder[1][0].add(this.tipoPessoa);
        panelHolder[1][1].add(this.TtipoPessoa);
        panelHolder[2][0].add(this.vendedor);
        panelHolder[3][0].add(this.limiteCredito);
        panelHolder[3][1].add(this.TlimiteCredito);
        
        this.add(panel,BorderLayout.CENTER);
    }
    
    private void AddBotoes(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        
       
        
        this.btnConfirmar.addActionListener(new ActionListenerConfirmar());
        panel.add(this.btnConfirmar);
        this.btnExcluir.addActionListener(new ActionListenerExcluirCliente());
        panel.add(this.btnExcluir);
        
        this.btnCriarVendedor.addActionListener(new ActionListenerEditarVendedor());
        panel.add(this.btnCriarVendedor);
        
        this.add(panel,BorderLayout.SOUTH);
    }
    
    private void AddVendedoresList(){
        Conexao_banco_de_dados cb = getConexao_banco_de_dados();
        
        ResultSet rs = cb.BuscaVendedores();
        if(rs == null){
            return;
        } 
       try{ 
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(180,500));
        p.setBackground(Color.WHITE);
        this.ListVendedor=new JScrollPane(p,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        while(rs.next()){
            JButton btnvend = new JButton(rs.getString("DSNOME"));
            btnvend.setPreferredSize(new Dimension(180,20));
            btnvend.setBackground(Color.WHITE);
            btnvend.setOpaque(true);
            btnvend.addActionListener(new ActionListenerVendList(btnvend));
            //add action listener aqui 
            p.add(btnvend);
            
            
        }
        this.ListVendedor.getViewport().add(p);
       }catch(SQLException ex){
           ex.printStackTrace();
       }
    }
    
    private void ConfiguraComponentes(){
        int h=20;
        int w=187;
        Dimension d = new Dimension(w,h);
        this.Tnome.setPreferredSize(d);
        this.Tnome.addFocusListener(new FocusListenerVerificaCliente());
        this.TtipoPessoa.setPreferredSize(d);
        this.TlimiteCredito.setPreferredSize(d);
    }
    
    class ActionListenerVendList implements ActionListener{
     
          JButton b;
        public ActionListenerVendList(JButton b){
           this.b=b;
      
        }
        @Override
        public void actionPerformed(ActionEvent e) {
             CadastroCliente.this.vendedorNome = this.b.getText();
            
        }
        
        
    }
    
    class ActionListenerConfirmar implements ActionListener{
       
       
       public ActionListenerConfirmar(){
           
       }
       
        @Override
        public void actionPerformed(ActionEvent e) {
            String nome;
            String idtipo;
            String CDVEND;
            double n;
            
           nome = CadastroCliente.this.Tnome.getText();
           idtipo = CadastroCliente.this.TtipoPessoa.getText();
           CDVEND = getConexao_banco_de_dados().getVendedorCD(CadastroCliente.this.vendedorNome);
           n = Double.parseDouble(CadastroCliente.this.TlimiteCredito.getText());
           
           if(CadastroCliente.this.clientejacadastrado){
              getConexao_banco_de_dados().EditarCliente(nome, idtipo, CDVEND, n);
              JOptionPane.showMessageDialog(new JPanel(),"Clinte editado com sucesso");
           }else{
           getConexao_banco_de_dados().cadastrarCliente(nome,idtipo,CDVEND,n);
           JOptionPane.showMessageDialog(new JPanel(),"Clinte Cadastrado com sucesso");
           }
        }
        
    }
    
    class FocusListenerVerificaCliente implements FocusListener{
        
       
        Conexao_banco_de_dados cn = getConexao_banco_de_dados();
        
        FocusListenerVerificaCliente(){
        
        }
        

        @Override
        public void focusGained(FocusEvent e) {
       
        }

        @Override
        public void focusLost(FocusEvent e) {
          String [] resultado;
      
            resultado = cn.verificaCliente(CadastroCliente.this.Tnome.getText());
            
            if(resultado == null)
            {    
                CadastroCliente.this.clientejacadastrado= false;
                 return;
            }
            CadastroCliente.this.clientejacadastrado = true;
            CadastroCliente.this.TtipoPessoa.setText(resultado[1]);
           CadastroCliente.this.TlimiteCredito.setText(resultado[2]);
            
        }
        
    }
    
    class ActionListenerExcluirCliente implements ActionListener{
        
        Conexao_banco_de_dados cn = getConexao_banco_de_dados();
        ActionListenerExcluirCliente(){
            
            
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            cn.ExcluirCliente(CadastroCliente.this.Tnome.getText());
            JPanel panel = new JPanel();
           JOptionPane.showMessageDialog( panel,"Cliente Deletado com sucesso");
        }
        
    }
    
    class ActionListenerEditarVendedor implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            CadastroVendedor c = new CadastroVendedor();
        }
        
    }
    
    static public void main(String args[]){
        CadastroCliente c = new CadastroCliente();
    }
}
