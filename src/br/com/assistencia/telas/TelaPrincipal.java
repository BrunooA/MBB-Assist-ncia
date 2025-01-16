package br.com.assistencia.telas;

import java.text.DateFormat;
import java.util.Date;
import br.com.assistencia.dal.ModuloConexao;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JLabel;

public class TelaPrincipal extends javax.swing.JFrame {

    //usando a variavel conexao do DAL
    Connection conexao = null;
    //criando variáveis especiais para conexão com o banco
    //Prepared Statement e ResultSet são fremeworks do pacote java.sql
    //e servem para preparar e executar as instruções SQL
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaPrincipal() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    public JLabel getLblUsuario() {
        return lblUsuario;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnGerenCli = new javax.swing.JButton();
        btnGerenUsu = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnGerenEqui = new javax.swing.JButton();
        btnGerenOs = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblUsuario = new javax.swing.JLabel();
        lblData = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MBB Assistência Técnica");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setForeground(new java.awt.Color(0, 102, 153));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jPanel1AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        btnGerenCli.setBackground(new java.awt.Color(0, 102, 102));
        btnGerenCli.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        btnGerenCli.setForeground(new java.awt.Color(255, 255, 255));
        btnGerenCli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/perfil.png"))); // NOI18N
        btnGerenCli.setText("            Cliente  ");
        btnGerenCli.setToolTipText("cadastro de cliente");
        btnGerenCli.setBorder(null);
        btnGerenCli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGerenCliMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGerenCliMouseExited(evt);
            }
        });
        btnGerenCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerenCliActionPerformed(evt);
            }
        });

        btnGerenUsu.setBackground(new java.awt.Color(0, 102, 102));
        btnGerenUsu.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        btnGerenUsu.setForeground(new java.awt.Color(255, 255, 255));
        btnGerenUsu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/perfil.png"))); // NOI18N
        btnGerenUsu.setText("            Usuário   ");
        btnGerenUsu.setToolTipText("cadastro de usuário");
        btnGerenUsu.setBorder(null);
        btnGerenUsu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGerenUsuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGerenUsuMouseExited(evt);
            }
        });
        btnGerenUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerenUsuActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/logo (2).png"))); // NOI18N

        btnGerenEqui.setBackground(new java.awt.Color(0, 102, 102));
        btnGerenEqui.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        btnGerenEqui.setForeground(new java.awt.Color(255, 255, 255));
        btnGerenEqui.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/dispositivo.png"))); // NOI18N
        btnGerenEqui.setText("    Equipamento   ");
        btnGerenEqui.setToolTipText("cadastro de equipamento");
        btnGerenEqui.setBorder(null);
        btnGerenEqui.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGerenEquiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGerenEquiMouseExited(evt);
            }
        });
        btnGerenEqui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerenEquiActionPerformed(evt);
            }
        });

        btnGerenOs.setBackground(new java.awt.Color(0, 102, 102));
        btnGerenOs.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        btnGerenOs.setForeground(new java.awt.Color(255, 255, 255));
        btnGerenOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/Os.png"))); // NOI18N
        btnGerenOs.setText("Ordem de Serviço");
        btnGerenOs.setToolTipText("cadastro de ordem de serviço");
        btnGerenOs.setBorder(null);
        btnGerenOs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGerenOsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGerenOsMouseExited(evt);
            }
        });
        btnGerenOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerenOsActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "USUÁRIO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        lblUsuario.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblUsuario.setForeground(new java.awt.Color(255, 255, 255));
        lblUsuario.setText("Usuário");

        lblData.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblData.setForeground(new java.awt.Color(255, 255, 255));
        lblData.setText("Data");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblData)
                    .addComponent(lblUsuario))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblUsuario)
                .addGap(12, 12, 12)
                .addComponent(lblData)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGerenEqui, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGerenCli, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGerenUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGerenOs, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(68, 68, 68))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(btnGerenUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(btnGerenCli, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(btnGerenEqui, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(btnGerenOs, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(714, 700));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // as linhas abaixo substituem a label lblData pela data atual do
        // sistema ao iniciar o form
        Date data = new Date();
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        lblData.setText(formatador.format(data));
    }//GEN-LAST:event_formWindowActivated

    private void btnGerenCliMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerenCliMouseEntered
        // TODO add your handling code here:
        btnGerenCli.setBackground(new Color(0, 255, 204));
    }//GEN-LAST:event_btnGerenCliMouseEntered

    private void btnGerenCliMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerenCliMouseExited
        // TODO add your handling code here:
        btnGerenCli.setBackground(new Color(0, 104, 104));
    }//GEN-LAST:event_btnGerenCliMouseExited

    private void btnGerenCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerenCliActionPerformed
        // Abrir a TelaPesquisaCliente
        TelaGerenCliente telPesq = new TelaGerenCliente();
        telPesq.setVisible(true);
    }//GEN-LAST:event_btnGerenCliActionPerformed

    private void btnGerenUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerenUsuActionPerformed
        // Abrir a TelaPesquisaCliente
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                TelaGerenUsuario telUsu = new TelaGerenUsuario();
                telUsu.setVisible(true);
            }
        });
    }//GEN-LAST:event_btnGerenUsuActionPerformed

    private void btnGerenUsuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerenUsuMouseEntered
        // TODO add your handling code here:
        btnGerenUsu.setBackground(new Color(0, 255, 204));
    }//GEN-LAST:event_btnGerenUsuMouseEntered

    private void btnGerenUsuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerenUsuMouseExited
        // TODO add your handling code here:
        btnGerenUsu.setBackground(new Color(0, 104, 104));
    }//GEN-LAST:event_btnGerenUsuMouseExited

    private void btnGerenEquiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerenEquiMouseEntered
        // TODO add your handling code here:
        btnGerenEqui.setBackground(new Color(0, 255, 204));
    }//GEN-LAST:event_btnGerenEquiMouseEntered

    private void btnGerenEquiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerenEquiMouseExited
        // TODO add your handling code here:
        btnGerenEqui.setBackground(new Color(0, 104, 104));
    }//GEN-LAST:event_btnGerenEquiMouseExited

    private void btnGerenOsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerenOsMouseEntered
        // TODO add your handling code here:
        btnGerenOs.setBackground(new Color(0, 255, 204));
    }//GEN-LAST:event_btnGerenOsMouseEntered

    private void btnGerenOsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerenOsMouseExited
        // TODO add your handling code here:
        btnGerenOs.setBackground(new Color(0, 104, 104));
    }//GEN-LAST:event_btnGerenOsMouseExited

    private void btnGerenEquiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerenEquiActionPerformed
        // TODO add your handling code here:
        TelaGerenEquip telEquip = new TelaGerenEquip();
        telEquip.setVisible(true);
    }//GEN-LAST:event_btnGerenEquiActionPerformed

    private void jPanel1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jPanel1AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1AncestorAdded

    private void btnGerenOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerenOsActionPerformed
        // TODO add your handling code here:
        TelaGerenOs telOs = new TelaGerenOs();
        telOs.setVisible(true);
    }//GEN-LAST:event_btnGerenOsActionPerformed

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
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGerenCli;
    private javax.swing.JButton btnGerenEqui;
    private javax.swing.JButton btnGerenOs;
    private javax.swing.JButton btnGerenUsu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblData;
    public static javax.swing.JLabel lblUsuario;
    // End of variables declaration//GEN-END:variables

    void habilitarBotoesUsuario() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /*Object getLblUsuario() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }*/
    void habilitarBotoesCliente() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void habilitarBotoesEquipamento() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void habilitarBotoesOrdemServico() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
