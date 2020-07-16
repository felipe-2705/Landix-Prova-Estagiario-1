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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CadastroVendedor extends JFrame {
    // Campos para indicar ao usuario a utilidade de cada campo na tela
    private JLabel titulo = new JLabel("Cadastro Vendedor",SwingConstants.CENTER);
    private JLabel nome = new JLabel("Nome: ",SwingConstants.CENTER);
    private JLabel codTab = new JLabel("Cod. Tabela de Precos: ",SwingConstants.CENTER);
    private JLabel data   = new JLabel("Data.Nasc: ",SwingConstants.CENTER);
    private JLabel Clientes = new JLabel("Clientes: ",SwingConstants.CENTER);
    
    // Caixas de texto para a inseção das informações
    private JTextField Tnome = new JTextField("");
    private JTextField TcodTab = new JTextField("");
    private JTextField Tdata =  new JTextField("aaaa-mm-dd");
    private JScrollPane Tcliente=new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    //Botoes de Funcoes 
    
    private JButton btnConfirmar = new JButton("Confirmar");
    private JButton btnExcluir = new JButton("Excluir");
    private JButton btnCriarCliente = new JButton("Criar Cliente");
    
    private boolean vendedorcadastrado = false;
    
    public CadastroVendedor(){
        this.configurarFrame();
        this.ConfiguraComponentes();
        
        this.AddComponentes();
        this.AddBotoes();
        
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
      
          
    }
    
    private void configurarFrame(){
        this.setTitle("LDXPS");
        this.setBounds(500, 250, 300, 310);
        this.setLayout(new BorderLayout());
        // topo
        this.add(this.titulo,BorderLayout.NORTH);
    }
    
    private void AddComponentes(){
        GridLayout grid = new GridLayout(4,2,5,10);
        JPanel panel = new JPanel();
        panel.setLayout(grid);  // painel central
        //adicionando componentes ao painel central
        
        panel.add(this.nome);
        panel.add(this.Tnome);
        panel.add(this.codTab);
        panel.add(this.TcodTab);
        panel.add(this.data);
        panel.add(this.Tdata);
        panel.add(this.Clientes);
        panel.add(this.Tcliente);
        this.add(panel,BorderLayout.CENTER); // adiciona o painel ao centro da tela
    }
    
    private void ConfiguraComponentes(){
        int h=20;
        int w=187;
        Dimension d = new Dimension(w,h);
        this.Tnome.setPreferredSize(d);
        this.Tnome.addFocusListener(new FocusListenerVerificaVendedor(this.Tnome,this.TcodTab,this.Tdata,this.Tcliente));
        this.TcodTab.setPreferredSize(d);
        this.Tdata.setPreferredSize(d);
    }
    
    private void AddBotoes(){
        FlowLayout flow = new FlowLayout();
        JPanel  p = new JPanel();
        p.setLayout(flow);
        
        this.btnConfirmar.addActionListener(new ActionListenerConfirmar(this.Tnome,this.TcodTab,this.Tdata));
        p.add(this.btnConfirmar);
        this.btnCriarCliente.addActionListener(new ActionListenerEditarCliente());
        p.add(this.btnCriarCliente);
        this.btnExcluir.addActionListener(new ActionListenerExcluirVendedor(this.Tnome));
        p.add(this.btnExcluir);
        
        this.add(p,BorderLayout.SOUTH);
    }
    
    private void AddClientesList(String vendedor){
        ResultSet rs = getConexao_banco_de_dados().BuscaClientes(vendedor);
        if(rs == null)return;
        
        try{
             JPanel p = new JPanel();
             p.setPreferredSize(new Dimension(140,500));
             p.setBackground(Color.WHITE);
             
        while(rs.next()){
            JButton btncl = new JButton(rs.getString("DSNOME"));
            btncl.setPreferredSize(new Dimension(140,20));
            btncl.setBackground(Color.WHITE);
            btncl.setOpaque(true);
          
            p.add(btncl);
            
        }
        this.Tcliente.getViewport().add(p);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    class ActionListenerConfirmar implements ActionListener{
        JTextField tnome;
        JTextField tcodtab;
        JTextField tdat;
        ActionListenerConfirmar(JTextField tnome, JTextField tcodtab, JTextField tdat){
           this.tnome=tnome;
           this.tcodtab =tcodtab;
           this.tdat=tdat;
       }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String nome = this.tnome.getText();
            int codTab = Integer.parseInt(this.tcodtab.getText());
            Date dat = Date.valueOf(this.tdat.getText());
            Conexao_banco_de_dados con = getConexao_banco_de_dados();
            if(CadastroVendedor.this.vendedorcadastrado){
                con.EditarVendedor(nome, codTab, dat);
                JPanel panel = new JPanel();
                JOptionPane.showMessageDialog( panel," Vendedor editado com sucesso");
            }else{
            con.cadastrarVendedor(nome, codTab, dat);
            JPanel panel = new JPanel();
             JOptionPane.showMessageDialog( panel," Vendedor cadastrado com sucesso");
            }
        }
        
        
    }
    
    class FocusListenerVerificaVendedor implements FocusListener{
        
       
        JTextField tnome;
        JTextField tcodTab;
        JTextField tdat;
        JScrollPane tcliente;
        Conexao_banco_de_dados cn = getConexao_banco_de_dados();
        
        FocusListenerVerificaVendedor(JTextField nome,JTextField codtab,JTextField dat,JScrollPane Tcliente){
            this.tnome = nome;
            this.tcodTab = codtab;
            this.tdat  = dat;
            this.tcliente = Tcliente;
        }
        

        @Override
        public void focusGained(FocusEvent e) {
       
        }

        @Override
        public void focusLost(FocusEvent e) {
          String [] resultado;
      
            resultado = cn.verificaVendedor(this.tnome.getText());
            
            if(resultado == null)
            {
                CadastroVendedor.this.vendedorcadastrado=false;
                return;
            }
            
            CadastroVendedor.this.vendedorcadastrado=true;
            this.tcodTab.setText(resultado[1]);
            this.tdat.setText(resultado[2]);
            CadastroVendedor.this.AddClientesList(this.tnome.getText());
        }
        
    }
    
    class ActionListenerExcluirVendedor implements ActionListener{
        JTextField nome;
        Conexao_banco_de_dados cn = getConexao_banco_de_dados();
        ActionListenerExcluirVendedor(JTextField vnome){
            this.nome=vnome;
            
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            cn.ExcluirVendedor(nome.getText());
            JPanel panel = new JPanel();
           JOptionPane.showMessageDialog( panel," Vendedor Deletado com sucesso");
        }
        
    }
    
     class ActionListenerEditarCliente implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            CadastroCliente c = new CadastroCliente();
        }
        
    }
    
    static public void main(String args[]){
        
        CadastroVendedor c = new CadastroVendedor();
    }
}
