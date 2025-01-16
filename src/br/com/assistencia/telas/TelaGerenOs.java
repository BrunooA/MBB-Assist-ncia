package br.com.assistencia.telas;

import br.com.assistencia.dal.ModuloConexao;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public final class TelaGerenOs extends javax.swing.JFrame {

    //usando a variavel conexao do DAL
    Connection conexao = null;
    //criando variáveis especiais para conexão com o banco
    //Prepared Statement e ResultSet são fremeworks do pacote java.sql
    //e servem para preparar e executar as instruções SQL
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaGerenOs() {
        initComponents();
        conexao = ModuloConexao.conector();
        //modificar cabeçalho Jtable
        JTableHeader jth = jTabelaOs.getTableHeader();
        jth.setBackground(new Color(0, 0, 102));
        jth.setForeground(Color.BLACK);
        jth.setFont(new Font("CenturyGothic", Font.BOLD, 18));

        // Exibir os dados automaticamente
        String sql = "SELECT OS.id_os, Cliente.nome AS nome_cliente, Usuario.nome AS nome_usuario, OS.servico, OS.val_tot, OS.form_pag, OS.situacao, OS.observacoes "
                + "FROM OS "
                + "INNER JOIN Cliente ON OS.id_cliente = Cliente.id_cliente "
                + "INNER JOIN Usuario ON OS.id_usuario = Usuario.id_usuario "
                + "ORDER BY OS.id_os DESC";
        povoarjTabelaOs(sql);
    }
    
    
    public JTable getJTabelaOs() {
        return jTabelaOs;
    }

    public void povoarjTabelaOs(String sql) {
        try {
            PreparedStatement pst = null;
            ResultSet rs = null;

            DefaultTableModel model = (DefaultTableModel) jTabelaOs.getModel();
            model.setNumRows(0);

            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                
                model.addRow(new Object[]{
                    rs.getString("id_os"),
                    rs.getString("nome_cliente"),
                    rs.getString("nome_usuario"),
                    rs.getString("servico"),
                    rs.getString("val_tot"),
                    rs.getString("form_pag"),
                    rs.getString("situacao"),
                    rs.getString("observacoes")
                });
            }
            // Fechar o ResultSet e o PreparedStatement
            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível obter os dados do Banco. Erro: " + e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabelaOs = new javax.swing.JTable();
        txtPesquisa = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ordem de Serviço");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));

        jButton1.setBackground(new java.awt.Color(0, 153, 153));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/adicionar.png"))); // NOI18N
        jButton1.setText("NOVA");
        jButton1.setBorder(null);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTabelaOs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Cliente", "Técnico", "Serviço", "Valor Total", "Forma de Pagamento", "Situação", "Observações"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabelaOs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabelaOsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTabelaOs);

        txtPesquisa.setBackground(new java.awt.Color(204, 204, 204));
        txtPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesquisaKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Pesquisa");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/lupa.png"))); // NOI18N
        jLabel2.setText("jLabel2");

        jLabel3.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Ordens de Serviço");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1463, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // abre a tela de Cadastro de Ordem de Serviço
        java.awt.EventQueue.invokeLater(() -> {
            TelaGerenOsCadastro telCaOs = new TelaGerenOsCadastro(this);
            telCaOs.setVisible(true);
            
             String sql = "SELECT OS.id_os, Cliente.nome AS nome_cliente, Usuario.nome AS nome_usuario, OS.servico, OS.val_tot, OS.form_pag, OS.situacao, OS.observacoes "
                + "FROM OS "
                + "INNER JOIN Cliente ON OS.id_cliente = Cliente.id_cliente "
                + "INNER JOIN Usuario ON OS.id_usuario = Usuario.id_usuario "
                + "ORDER BY OS.id_os DESC";
              povoarjTabelaOs(sql);
           
        });
    }//GEN-LAST:event_jButton1MouseClicked

    private void txtPesquisaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaKeyTyped
        // Pesquisa pelo nome
        
        String filtro =  "'%" +  txtPesquisa.getText() + "%'";
        String sql = "SELECT OS.id_os, Cliente.nome AS nome_cliente, Usuario.nome AS nome_usuario, OS.servico, OS.val_tot, OS.form_pag, OS.situacao, OS.observacoes " +
                 "FROM OS " +
                 "INNER JOIN Cliente ON OS.id_cliente = Cliente.id_cliente " +
                 "INNER JOIN Usuario ON OS.id_usuario = Usuario.id_usuario " +
                 "WHERE Cliente.nome LIKE " + filtro  +
                 "OR Usuario.nome LIKE  "+ filtro  +
                 "OR OS.servico LIKE  "+ filtro  +
                 "OR OS.form_pag LIKE  "+ filtro  +
                 "OR OS.situacao LIKE  "+ filtro  +
                 "OR OS.observacoes LIKE  "+ filtro  +
                 "ORDER BY OS.id_os DESC";
    povoarjTabelaOs(sql);
    }//GEN-LAST:event_txtPesquisaKeyTyped

    private void jTabelaOsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabelaOsMouseClicked
       int linha = jTabelaOs.getSelectedRow();//selecionar a linha jogar na variavel linha

        String codOS = jTabelaOs.getValueAt(linha, 0).toString();     
       

        
         java.awt.EventQueue.invokeLater(() -> {
            TelaGerenOsCadastro telCaOs = new TelaGerenOsCadastro(this);
            telCaOs.PreecherForm(codOS);
            telCaOs.setVisible(true);
            
            
             String sql = "SELECT OS.id_os, Cliente.nome AS nome_cliente, Usuario.nome AS nome_usuario, OS.servico, OS.val_tot, OS.form_pag, OS.situacao, OS.observacoes "
                + "FROM OS "
                + "INNER JOIN Cliente ON OS.id_cliente = Cliente.id_cliente "
                + "INNER JOIN Usuario ON OS.id_usuario = Usuario.id_usuario "
                + "ORDER BY OS.id_os DESC";
              povoarjTabelaOs(sql);
           
        });

        
        

    }//GEN-LAST:event_jTabelaOsMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaGerenOs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaGerenOs().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabelaOs;
    private javax.swing.JTextField txtPesquisa;
    // End of variables declaration//GEN-END:variables
}
