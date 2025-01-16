package br.com.assistencia.telas;

import br.com.assistencia.dal.ModuloConexao;
import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.awt.*;

public class TelaGerenOsCadastro extends javax.swing.JDialog {

    // Usando a variável conexão do DAL
    Connection conexao = null;
    // Criando variáveis especiais para conexão com o banco
    // Prepared Statement e ResultSet são frameworks do pacote java.sql
    // e servem para preparar e executar as instruções SQL
    PreparedStatement pst = null;
    ResultSet rs = null;
    String codCliente = "";
    String codTec = "";
    String codEquip = "";
    Frame OwnerFrame;

    public TelaGerenOsCadastro(Frame owner) {
        super(owner, "Diálogo", true);
        OwnerFrame = owner;
        setLocationRelativeTo(owner);
        initComponents();
        conexao = ModuloConexao.conector();

        // Exibir os dados automaticamente
        String sql = "SELECT OS.id_os, Cliente.nome AS nome_cliente, Usuario.nome AS nome_usuario, OS.servico, OS.val_tot, OS.form_pag, OS.situacao, OS.observacoes "
                + "FROM OS "
                + "INNER JOIN Cliente ON OS.id_cliente = Cliente.id_cliente "
                + "INNER JOIN Usuario ON OS.id_usuario = Usuario.id_usuario "
                + "ORDER BY OS.id_os DESC";
    }

    private void salvar() {

        String sql = "";
        if (txtCodigoOs.getText().isEmpty()) { // Se estiver vazio, é pq esta criando o item
            sql = "insert into OS (id_usuario, id_cliente, id_equip, servico, val_tot, form_pag, observacoes, situacao)"
                    + "values(?, ?, ?, ?,?,?,?,?);";

        } else {
            sql = "UPDATE OS SET  id_usuario = ?, id_cliente= ?, id_equip= ?, servico= ?, val_tot= ?, form_pag= ?, observacoes= ?, situacao= ? WHERE id_os = ?";
        }

        String codigoOS = txtCodigoOs.getText();
        //codCliente;
        //codTec;
        //codEquip;
        String descricaoServico = txtDescricaoServico.getText();
        String valor = txtValor.getText();
        String formaPagamento = cboFormaPagamento.getSelectedItem().toString();
        String observacoes = txtObservacoes.getText();
        String situacao = cboSituacao.getSelectedItem().toString();

        if (codCliente.isEmpty() || codTec.isEmpty() || codEquip.isEmpty() || valor.isEmpty() || descricaoServico.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
            return;
        }

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, codTec);
            pst.setString(2, codCliente);
            pst.setString(3, codEquip);
            pst.setString(4, descricaoServico);
            pst.setString(5, valor);
            pst.setString(6, formaPagamento);
            pst.setString(7, observacoes);
            pst.setString(8, situacao);

            if (!txtCodigoOs.getText().isEmpty()) {
                pst.setString(9, codigoOS);
            }

            // Executar a atualização
            int result = pst.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Ordem de serviço salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao salvar Ordem de serviço!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            // Fechar PreparedStatement
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    // Tratar erro ao fechar PreparedStatement
                }
            }
        }
    }

    private void remover() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover esta Ordem de Serviço?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM OS WHERE id_os=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtCodigoOs.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {

                    JOptionPane.showMessageDialog(null, "Ordem de Serviço removida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao remover Ordem de Serviço!", "Erro", JOptionPane.ERROR_MESSAGE);

                }
                dispose();

            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao remover Equipamento!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void PreecherForm(String codOS) {

        try {
            PreparedStatement pst = null;

            String sql = "SELECT OS.id_os,Equipamento.id_equip AS codEquip, Cliente.id_cliente AS codCliente, Cliente.nome AS nome_cliente,Usuario.id_usuario as codUsuario, Usuario.nome AS nome_usuario, Equipamento.tipo,Equipamento.modelo,Equipamento.marca, OS.servico, OS.val_tot, OS.form_pag, OS.situacao, OS.observacoes "
                    + "FROM OS INNER JOIN Cliente ON OS.id_cliente = Cliente.id_cliente INNER JOIN Usuario ON OS.id_usuario = Usuario.id_usuario INNER JOIN Equipamento ON OS.id_equip = Equipamento.id_equip WHERE OS.id_os = ?";

            pst = conexao.prepareStatement(sql);

            pst.setInt(1, Integer.parseInt(codOS));
            rs = pst.executeQuery();

            while (rs.next()) {

                codCliente = rs.getString("codCliente");
                codTec = rs.getString("codUsuario");
                codEquip = rs.getString("codEquip");

                txtCodigoOs.setText(rs.getString("id_os"));
                txtCliente.setText(rs.getString("nome_cliente"));
                txtTecnico.setText(rs.getString("nome_usuario"));
                txtEquipamento.setText(rs.getString("tipo") + " - " + rs.getString("modelo") + " - " + rs.getString("marca"));
                txtDescricaoServico.setText(rs.getString("servico"));
                txtValor.setText(rs.getString("val_tot"));
                cboFormaPagamento.setSelectedItem(rs.getString("form_pag"));
                cboSituacao.setSelectedItem(rs.getString("situacao"));
                txtObservacoes.setText(rs.getString("observacoes"));

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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtCodigoOs = new javax.swing.JTextField();
        txtCliente = new javax.swing.JTextField();
        txtTecnico = new javax.swing.JTextField();
        txtValor = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnCliente = new javax.swing.JButton();
        btnTecnico = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        cboFormaPagamento = new javax.swing.JComboBox<>();
        cboSituacao = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtObservacoes = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescricaoServico = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        txtEquipamento = new javax.swing.JTextField();
        btnEquipamento = new javax.swing.JButton();
        btnDeletarUsu = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ordem de Serviço");

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Código");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("*Cliente");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("*Técnico");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Descrição do");

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("*Valor");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Situação");

        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Forma de");

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("*Observações");

        txtCodigoOs.setBackground(new java.awt.Color(204, 204, 204));
        txtCodigoOs.setEnabled(false);

        txtCliente.setBackground(new java.awt.Color(204, 204, 204));
        txtCliente.setEnabled(false);

        txtTecnico.setBackground(new java.awt.Color(204, 204, 204));
        txtTecnico.setEnabled(false);
        txtTecnico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTecnicoActionPerformed(evt);
            }
        });

        txtValor.setBackground(new java.awt.Color(204, 204, 204));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Serviço");

        jLabel10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Pagamento");

        btnCliente.setBackground(new java.awt.Color(0, 102, 102));
        btnCliente.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        btnCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnCliente.setText("Selecionar");
        btnCliente.setBorder(null);
        btnCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteActionPerformed(evt);
            }
        });

        btnTecnico.setBackground(new java.awt.Color(0, 102, 102));
        btnTecnico.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        btnTecnico.setForeground(new java.awt.Color(255, 255, 255));
        btnTecnico.setText("Selecionar");
        btnTecnico.setBorder(null);
        btnTecnico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTecnicoActionPerformed(evt);
            }
        });

        btnSalvar.setBackground(new java.awt.Color(0, 102, 102));
        btnSalvar.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        btnSalvar.setForeground(new java.awt.Color(255, 255, 255));
        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/salvar-arquivo (1).png"))); // NOI18N
        btnSalvar.setToolTipText("salvar dados");
        btnSalvar.setBorder(null);
        btnSalvar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalvarMouseClicked(evt);
            }
        });
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        cboFormaPagamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dinheiro", "Pix", "Cartão de Crédito ", "Cartão de Débito", "Boleto" }));

        cboSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Novo", "Em andamento", "Aguardando peças", "Concluído", "Pedido Cancelado" }));

        txtObservacoes.setBackground(new java.awt.Color(204, 204, 204));
        txtObservacoes.setColumns(20);
        txtObservacoes.setRows(5);
        jScrollPane1.setViewportView(txtObservacoes);

        txtDescricaoServico.setBackground(new java.awt.Color(204, 204, 204));
        txtDescricaoServico.setColumns(20);
        txtDescricaoServico.setRows(5);
        jScrollPane2.setViewportView(txtDescricaoServico);

        jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("*Equipamento");

        txtEquipamento.setBackground(new java.awt.Color(204, 204, 204));
        txtEquipamento.setEnabled(false);
        txtEquipamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEquipamentoActionPerformed(evt);
            }
        });

        btnEquipamento.setBackground(new java.awt.Color(0, 102, 102));
        btnEquipamento.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        btnEquipamento.setForeground(new java.awt.Color(255, 255, 255));
        btnEquipamento.setText("Selecionar");
        btnEquipamento.setBorder(null);
        btnEquipamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEquipamentoMouseClicked(evt);
            }
        });
        btnEquipamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEquipamentoActionPerformed(evt);
            }
        });

        btnDeletarUsu.setBackground(new java.awt.Color(0, 102, 102));
        btnDeletarUsu.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        btnDeletarUsu.setForeground(new java.awt.Color(255, 255, 255));
        btnDeletarUsu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/lixeira-de-reciclagem (1).png"))); // NOI18N
        btnDeletarUsu.setToolTipText("deletar dados");
        btnDeletarUsu.setBorder(null);
        btnDeletarUsu.setBorderPainted(false);
        btnDeletarUsu.setRequestFocusEnabled(false);
        btnDeletarUsu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeletarUsuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDeletarUsuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDeletarUsuMouseExited(evt);
            }
        });
        btnDeletarUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletarUsuActionPerformed(evt);
            }
        });

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("* campos obrigatórios");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel11)
                            .addComponent(jLabel4)
                            .addComponent(jLabel9)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addComponent(jLabel10)
                            .addComponent(jLabel6))
                        .addGap(55, 55, 55)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnDeletarUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtCodigoOs, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTecnico)
                                    .addComponent(txtEquipamento)
                                    .addComponent(txtCliente))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnEquipamento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnCliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txtValor)
                            .addComponent(cboFormaPagamento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboSituacao, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE))))
                .addContainerGap(55, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCodigoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEquipamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEquipamento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cboFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cboSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeletarUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(121, 121, 121))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 742, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.LINE_END);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtTecnicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTecnicoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTecnicoActionPerformed

    private void btnClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteActionPerformed
        // Método para Chamar a Tela do Cliente

        java.awt.EventQueue.invokeLater(() -> {
            TelaGerenOsSelecionarCli telCliOs = new TelaGerenOsSelecionarCli(OwnerFrame);
            telCliOs.setVisible(true);

            codCliente = telCliOs.getCodCliSelecionado();
            String nomeCliente = telCliOs.getNomeCliSelecionado();

            txtCliente.setText(nomeCliente);

        });
    }//GEN-LAST:event_btnClienteActionPerformed

    private void btnTecnicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTecnicoActionPerformed
        // Método para Chamar a Tela do Técnico

        java.awt.EventQueue.invokeLater(() -> {
            TelaGerenOsSelecionarTec telTecOs = new TelaGerenOsSelecionarTec(OwnerFrame);
            telTecOs.setVisible(true);

            codTec = telTecOs.getCodTecSelecionado();
            String nomeTec = telTecOs.getNomeTecSelecionado();

            txtTecnico.setText(nomeTec);
        });

    }//GEN-LAST:event_btnTecnicoActionPerformed

    private void btnSalvarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalvarMouseClicked
        salvar();
    }//GEN-LAST:event_btnSalvarMouseClicked

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        //atualizar();
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void txtEquipamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEquipamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEquipamentoActionPerformed

    private void btnEquipamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEquipamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEquipamentoActionPerformed

    private void btnEquipamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEquipamentoMouseClicked
        java.awt.EventQueue.invokeLater(() -> {
            TelaGerenOsSelecionarEquip telEquipOs = new TelaGerenOsSelecionarEquip(OwnerFrame);
            telEquipOs.setVisible(true);

            codEquip = telEquipOs.getCodEquipSelecionado();
            String nomeCliente = telEquipOs.getNomeEquipSelecionado();

            txtEquipamento.setText(nomeCliente);

        });
    }//GEN-LAST:event_btnEquipamentoMouseClicked

    private void btnDeletarUsuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeletarUsuMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeletarUsuMouseClicked

    private void btnDeletarUsuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeletarUsuMouseEntered
        // TODO add your handling code here:
        btnDeletarUsu.setBackground(new Color(0, 255, 204));
    }//GEN-LAST:event_btnDeletarUsuMouseEntered

    private void btnDeletarUsuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeletarUsuMouseExited
        // TODO add your handling code here:
        btnDeletarUsu.setBackground(new Color(0, 104, 104));
    }//GEN-LAST:event_btnDeletarUsuMouseExited

    private void btnDeletarUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletarUsuActionPerformed
        // TODO add your handling code here:
        remover();
    }//GEN-LAST:event_btnDeletarUsuActionPerformed

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
            java.util.logging.Logger.getLogger(TelaGerenOsCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCliente;
    private javax.swing.JButton btnDeletarUsu;
    private javax.swing.JButton btnEquipamento;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnTecnico;
    private javax.swing.JComboBox<String> cboFormaPagamento;
    private javax.swing.JComboBox<String> cboSituacao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtCodigoOs;
    private javax.swing.JTextArea txtDescricaoServico;
    private javax.swing.JTextField txtEquipamento;
    private javax.swing.JTextArea txtObservacoes;
    private javax.swing.JTextField txtTecnico;
    private javax.swing.JTextField txtValor;
    // End of variables declaration//GEN-END:variables
}
