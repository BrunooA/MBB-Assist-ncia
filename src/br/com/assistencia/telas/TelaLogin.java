package br.com.assistencia.telas;

import br.com.assistencia.dal.ModuloConexao;
import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class TelaLogin extends javax.swing.JFrame {

    //usando a variavel conexao do DAL
    Connection conexao = null;
    //criando variáveis especiais para conexão com o banco
    //Prepared Statement e ResultSet são fremeworks do pacote java.sql
    //e servem para preparar e executar as instruções SQL
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaLogin() {
        initComponents();

        txtsenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void KeyPressed(java.awt.event.KeyEvent evt) {
                txtsenhaKeyPressed(evt);
            }
        });

        //estabelecendo a conexao com o banco sempre neste ponto
        conexao = ModuloConexao.conector();
        //a estrutura abaixo muda o icone de acordo com o status da conexao
        System.out.println(conexao);

        if (conexao != null) {

            lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/dbok.png")));
        } else {
            lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/dberror.png")));
        }
        setUndecorated(true);
        txtusuario.addActionListener(this::btnLoginActionPerformed);
    }
    //Método para Logar
    public void logar() {
        // lógica principal para pesquisar no banco de dados
        String sql = "select * from usuario where login=? and senha =?";
        Logger logger = Logger.getLogger(TelaLogin.class.getName());

        try {
            // as linhas abaixo preparam a consulta ao banco em função do que foi digitado nas caixas de texto.
            // o '?' é substituído pelo conteúdo das variáveis
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtusuario.getText());
            pst.setString(2, new String(txtsenha.getPassword()));

            // a linha abaixo executa a query (consulta)
            rs = pst.executeQuery();
            if (rs.next()) {
                String senhaBanco = rs.getString("senha");
                String senhaDigitada = new String(txtsenha.getPassword());

                if (senhaBanco.equals(senhaDigitada)) {
                    // Senha correta, abrir a tela principal
                    TelaPrincipal principal = new TelaPrincipal();
                    principal.setVisible(true);
                    this.dispose();

                    // Verifica o perfil do usuário
                    String perfil = rs.getString("perfil");

                    if (perfil.equals("atend")) {
                        principal.getLblUsuario().setText(rs.getString("nome"));
                        principal.getLblUsuario().setForeground(new Color(0, 255, 255)); // Define a cor como ciano claro
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Senha incorreta");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuário inválido");
                // Adicionado o return aqui para impedir a abertura da tela principal
            }

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (SQLException e) {
            System.out.println("Erro ao executar a consulta: " + e.getMessage());
            logger.log(Level.SEVERE, "Erro ao executar a consulta", e);
            JOptionPane.showMessageDialog(null, "Erro ao executar a consulta: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblusuario = new javax.swing.JLabel();
        lblsenha = new javax.swing.JLabel();
        txtsenha = new javax.swing.JPasswordField();
        lblStatus = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        txtFecharLogin = new javax.swing.JLabel();
        txtusuario = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("X-System-Login");
        setBackground(new java.awt.Color(0, 0, 0));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/logo (2).png"))); // NOI18N

        lblusuario.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        lblusuario.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario.setText("Usuário");

        lblsenha.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        lblsenha.setForeground(new java.awt.Color(255, 255, 255));
        lblsenha.setText("Senha");

        txtsenha.setBackground(new java.awt.Color(204, 204, 204));
        txtsenha.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtsenha.setToolTipText("digite a senha do usuario");
        txtsenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsenhaActionPerformed(evt);
            }
        });
        txtsenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtsenhaKeyPressed(evt);
            }
        });

        lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/dbok.png"))); // NOI18N

        btnLogin.setBackground(new java.awt.Color(0, 102, 102));
        btnLogin.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("LOGIN");
        btnLogin.setToolTipText("logar");
        btnLogin.setBorder(null);
        btnLogin.setBorderPainted(false);
        btnLogin.setRequestFocusEnabled(false);
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLoginMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLoginMouseExited(evt);
            }
        });
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        txtFecharLogin.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        txtFecharLogin.setForeground(new java.awt.Color(255, 255, 255));
        txtFecharLogin.setText("X");
        txtFecharLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtFecharLoginMouseClicked(evt);
            }
        });

        txtusuario.setBackground(new java.awt.Color(204, 204, 204));
        txtusuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtusuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblusuario, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblsenha, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtsenha, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(108, 108, 108)
                                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblStatus))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)
                                .addGap(97, 97, 97))
                            .addComponent(txtusuario, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(79, 79, 79))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(txtFecharLogin)
                        .addGap(15, 15, 15))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtFecharLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(94, 94, 94)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(lblusuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtusuario, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(lblsenha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtsenha, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStatus))
                .addContainerGap(131, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.LINE_START);

        setSize(new java.awt.Dimension(475, 661));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // Limpar o campo de senha
        txtsenha.setText("");
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnLoginMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginMouseEntered
        // TODO add your handling code here:
        btnLogin.setBackground(new Color(0, 255, 204));
    }//GEN-LAST:event_btnLoginMouseEntered

    private void btnLoginMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginMouseExited
        // TODO add your handling code here:
        btnLogin.setBackground(new Color(0, 104, 104));
    }//GEN-LAST:event_btnLoginMouseExited

    private void txtFecharLoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFecharLoginMouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_txtFecharLoginMouseClicked

    private void txtusuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtusuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtusuarioActionPerformed

    private void txtsenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsenhaActionPerformed
        // Obter a senha digitada
        String senha = new String(txtsenha.getPassword());

        // Verificar se a senha está vazia\\\
        if (senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, digite a senha.");
            return;
        }
        // Executar ação com base na senha
        if (senha.equals("senhaCorreta")) {
            // Senha correta, fazer algo
            System.out.println("Senha correta!");
        } else {
            // Senha incorreta, mostrar mensagem de erro
            System.out.println("Senha incorreta!");
        }

        // Limpar o campo de senha
        txtsenha.setText("");
    }//GEN-LAST:event_txtsenhaActionPerformed

    private void txtsenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsenhaKeyPressed
        // Verificar se a tecla Enter foi pressionada e, em caso afirmativo, 
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            // Chamar o método logar() para efetuar o login
            logar();
            // Limpar o campo de senha
            txtsenha.setText("");
        }
    }//GEN-LAST:event_txtsenhaKeyPressed

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
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new TelaLogin().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblsenha;
    private javax.swing.JLabel lblusuario;
    private javax.swing.JLabel txtFecharLogin;
    private javax.swing.JPasswordField txtsenha;
    private javax.swing.JTextField txtusuario;
    // End of variables declaration//GEN-END:variables
}
