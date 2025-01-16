package br.com.assistencia.telas;

import br.com.assistencia.dal.ModuloConexao;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaGerenOsSelecionarEquip extends javax.swing.JDialog {
    // Usando a variável conexão do DAL
    Connection conexao = null;
    // Criando variáveis especiais para conexão com o banco
    // Prepared Statement e ResultSet são frameworks do pacote java.sql
    // e servem para preparar e executar as instruções SQL
    PreparedStatement pst = null;
    ResultSet rs = null;
    String codEquipSelecionado= ""; 
    String nomeEquipSelecionado= "";

    public TelaGerenOsSelecionarEquip(Frame owner) {
        super(owner, "Diálogo", true);
        setLocationRelativeTo(owner);
        initComponents();
        conexao = ModuloConexao.conector();
        JTableHeader jth = jTableCli.getTableHeader();
        jth.setBackground(new Color(0, 0, 102));
                jth.setForeground(Color.BLACK);
        jth.setFont(new Font("CenturyGothic", Font.BOLD, 18));

        // Exibir os dados automaticamente
        String sql = "SELECT * FROM Equipamento ORDER BY tipo ";
        povoarjTabelaEquipamentos(sql, "");
    }
    
    public void povoarjTabelaEquipamentos(String sql,String filtro ) {
        try {
            PreparedStatement pst = null;
            ResultSet rs = null;

            DefaultTableModel model = (DefaultTableModel) jTableCli.getModel();
            model.setNumRows(0);

            pst = conexao.prepareStatement(sql);
            if (!filtro.isEmpty() & !filtro.isBlank() ) {
                  pst.setString(1,filtro);
            }
          
            rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_equip"),
                    rs.getString("tipo"),
                    rs.getString("modelo"),    
                    rs.getString("marca"),

 
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCli = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtPesquisa = new javax.swing.JTextField();
        btnPesquisa = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Selecionar Cliente");

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));

        jTableCli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Tipo", "Modelo", "Marca"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableCli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCliMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableCli);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Selecione o Equipamento");

        txtPesquisa.setBackground(new java.awt.Color(204, 204, 204));

        btnPesquisa.setBackground(new java.awt.Color(0, 102, 102));
        btnPesquisa.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        btnPesquisa.setForeground(new java.awt.Color(255, 255, 255));
        btnPesquisa.setText("Pesquisar");
        btnPesquisa.setBorder(null);
        btnPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 798, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel1)
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.LINE_END);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTableCliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCliMouseClicked
        
        int linha = jTableCli.getSelectedRow();//selecionar a linha jogar na variavel linha
        codEquipSelecionado =  jTableCli.getValueAt(linha, 0).toString();
        nomeEquipSelecionado =  jTableCli.getValueAt(linha, 1).toString() +  " - " 
                                +jTableCli.getValueAt(linha, 2).toString() + " - "
                                +jTableCli.getValueAt(linha, 3).toString() + " - "; // Tipo - Modelo - Marca
        
      

        dispose();
    }//GEN-LAST:event_jTableCliMouseClicked

    private void btnPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisaActionPerformed
          
         String filtro = txtPesquisa.getText().isEmpty() ? "" : "'%"+txtPesquisa.getText() +"%'";
         String sql = "SELECT * FROM Equipamento " + (!txtPesquisa.getText().isEmpty() ?  "WHERE tipo LIKE "+filtro+" OR modelo LIKE "+filtro+"  OR marca LIKE "+filtro :"") + " ORDER BY tipo  " ;
          
        
          povoarjTabelaEquipamentos(sql,"");
    }//GEN-LAST:event_btnPesquisaActionPerformed

   
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
            java.util.logging.Logger.getLogger(TelaGerenOsSelecionarEquip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
             
            }
        });
    }
    
   public String getCodEquipSelecionado()
   {
       return codEquipSelecionado;
   }
    public String getNomeEquipSelecionado()
   {
       return nomeEquipSelecionado;
   }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPesquisa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableCli;
    private javax.swing.JTextField txtPesquisa;
    // End of variables declaration//GEN-END:variables
}
