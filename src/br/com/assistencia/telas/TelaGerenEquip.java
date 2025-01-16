package br.com.assistencia.telas;

import br.com.assistencia.dal.ModuloConexao;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class TelaGerenEquip extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaGerenEquip() {
        initComponents();
        conexao = ModuloConexao.conector();
        //modificar cabeçalho Jtable
        JTableHeader jth = jTabelaEquip.getTableHeader();
        jth.setBackground(new Color(0, 0, 102));
        jth.setForeground(Color.BLACK);
        jth.setFont(new Font("CenturyGothic", Font.BOLD, 18));

        // Exibir os dados automaticamente
        String sql = "SELECT * FROM Equipamento ORDER BY id_equip DESC";
        povoarjTabelaEquip(sql);
    }

    private int lerIdEquip() {
        int linha = jTabelaEquip.getSelectedRow();
        int idEquip = Integer.parseInt(jTabelaEquip.getValueAt(linha, 0).toString());

        try {
            String sql = "SELECT id_usuario FROM Usuario WHERE id_usuario = ?";
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idEquip);
            rs = pst.executeQuery();

            if (rs.next()) {
                idEquip = rs.getInt("id_equip");
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível obter os dados do Banco. Erro: " + e);
        }

        return idEquip;
    }

    public void povoarjTabelaEquip(String sql) {
        try {
            DefaultTableModel model = (DefaultTableModel) jTabelaEquip.getModel();
            model.setNumRows(0);

            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_equip"),
                    rs.getString("tipo"),
                    rs.getString("modelo"),
                    rs.getString("marca"),});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível obter os dados do Banco. Erro: " + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar ResultSet/PreparedStatement: " + e);
            }
        }
    }

    private String obterTipoEquip(int idEquip) throws SQLException {
        String queryEquipamento = "SELECT tipo FROM Equipamento WHERE id_equip = ?";
        PreparedStatement pstEquipamento = conexao.prepareStatement(queryEquipamento);
        pstEquipamento.setInt(1, idEquip);
        ResultSet rsEquipamento = pstEquipamento.executeQuery();

        if (rsEquipamento.next()) {
            return rsEquipamento.getString("nome");
        }

        return "";
    }

    private void salvar() {
        String sql = "";
        if (txtCodEquipamento.getText().isEmpty()) { // Se estiver vazio, é pq esta criando o item
            sql = "INSERT INTO Equipamento ( tipo, modelo, marca) VALUES (?, ?, ?)";

        } else {
            sql = "UPDATE Equipamento SET  tipo=?, modelo=?, marca=? WHERE id_equip=?";
        }
        try {
            if ((txtModeloEquip.getText().isEmpty()) || (txtMarcaEquip.getText().isEmpty()) || (cboTipoEquip.getSelectedItem() == null)) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {

                pst = conexao.prepareStatement(sql);
                pst.setString(1, cboTipoEquip.getSelectedItem().toString());
                pst.setString(2, txtModeloEquip.getText());
                pst.setString(3, txtMarcaEquip.getText());

                if (!txtCodEquipamento.getText().isEmpty()) {
                    pst.setString(4, txtCodEquipamento.getText());
                }
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    cboTipoEquip.setSelectedItem(null);
                    txtModeloEquip.setText(null);
                    txtMarcaEquip.setText(null);
                    txtCodEquipamento.setText(null);
                    JOptionPane.showMessageDialog(null, "Equipamento salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao salvar Equipamento!", "Erro", JOptionPane.ERROR_MESSAGE);
                }

                sql = "SELECT * FROM Equipamento ORDER BY id_equip DESC";
                povoarjTabelaEquip(sql);

            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(null, "Equipamento já cadastrado.");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao adicionar equipamento: " + e.getMessage());
            }
        }
    }

    private void remover() {
        int linhaSelecionada = jTabelaEquip.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um equipamento para remover.");
            return;
        }

        int confirma = JOptionPane.showConfirmDialog(null, "Tem Certeza que deseja remover este equipamento?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                int idEquip = lerIdEquip(); // Obter o ID do cliente selecionado na tabela

                String sql = "DELETE FROM Equipamento WHERE id_equip = ?";
                pst = conexao.prepareStatement(sql);
                pst.setInt(1, idEquip);

                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Equipamento removido com sucesso!");
                }
            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao remover Equipamento: " + e.getMessage());
            }
        }
        atualizarTabelaEquip();
    }

    private void atualizarTabelaEquip() {
        DefaultTableModel tableModel = (DefaultTableModel) jTabelaEquip.getModel();
        tableModel.setRowCount(0); // Limpa as linhas existentes na tabela

        try {
            // Execute a consulta SQL para obter os dados atualizados do banco de dados
            String sql = "SELECT id_equip, tipo, modelo, marca  FROM Equipamento ORDER BY id_equip DESC";
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            // Adicione as novas linhas com os dados atualizados à tabela
            while (rs.next()) {
                String idEquip = rs.getString("id_equip");
                String tipo = rs.getString("tipo");
                String modelo = rs.getString("modelo");
                String marca = rs.getString("marca");
                

                tableModel.addRow(new Object[]{idEquip, tipo, modelo, marca});
            }

            // Feche o ResultSet e o PreparedStatement
            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível obter os dados do Banco. Erro: " + e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        txtPesquisaPorMarca = new javax.swing.JTextField();
        lblusuario19 = new javax.swing.JLabel();
        lblusuario20 = new javax.swing.JLabel();
        txtPesquisaPorTipo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        lblusuario27 = new javax.swing.JLabel();
        txtModeloEquip = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        lblusuario28 = new javax.swing.JLabel();
        txtMarcaEquip = new javax.swing.JTextField();
        lblusuario29 = new javax.swing.JLabel();
        cboTipoEquip = new javax.swing.JComboBox<>();
        lblusuario30 = new javax.swing.JLabel();
        txtCodEquipamento = new javax.swing.JTextField();
        btnSalvar3 = new javax.swing.JButton();
        btnDeletarUsu = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTabelaEquip = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel5.setBackground(new java.awt.Color(0, 153, 153));

        txtPesquisaPorMarca.setBackground(new java.awt.Color(204, 204, 204));
        txtPesquisaPorMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesquisaPorMarcaActionPerformed(evt);
            }
        });
        txtPesquisaPorMarca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesquisaPorMarcaKeyTyped(evt);
            }
        });

        lblusuario19.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario19.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario19.setText("Pesquisa por Marca");

        lblusuario20.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario20.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario20.setText("Pesquisa por Tipo");

        txtPesquisaPorTipo.setBackground(new java.awt.Color(204, 204, 204));
        txtPesquisaPorTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesquisaPorTipoActionPerformed(evt);
            }
        });
        txtPesquisaPorTipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesquisaPorTipoKeyTyped(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/logo (2).png"))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Equipamento");

        jPanel7.setBackground(new java.awt.Color(0, 102, 102));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro Equipamento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        lblusuario27.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario27.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario27.setText("*Modelo");

        txtModeloEquip.setBackground(new java.awt.Color(204, 204, 204));
        txtModeloEquip.setToolTipText("escreva o modelo do dispositivo");
        txtModeloEquip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtModeloEquipActionPerformed(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("* campos obrigatórios");

        lblusuario28.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario28.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario28.setText("*Marca");

        txtMarcaEquip.setBackground(new java.awt.Color(204, 204, 204));
        txtMarcaEquip.setToolTipText("marca do equipamento");
        txtMarcaEquip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMarcaEquipActionPerformed(evt);
            }
        });

        lblusuario29.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario29.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario29.setText("*Tipo");

        cboTipoEquip.setBackground(new java.awt.Color(204, 204, 204));
        cboTipoEquip.setForeground(new java.awt.Color(255, 255, 255));
        cboTipoEquip.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aparelho de som", "Caixa de som", "Camera", "Celular", "Computador", "Câmera de ação", "Câmera de segurança", "Desktop", "Fone de ouvido", "Headphone", "Impressora", "Monitor", "Mouse", "Notebook", "Projetor", "Roteador", "Smart Speaker", "Smart TV", "Smartphone", "Smartwatch", "Tablet", "Teclado" }));

        lblusuario30.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario30.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario30.setText("Código");

        txtCodEquipamento.setBackground(new java.awt.Color(204, 204, 204));
        txtCodEquipamento.setToolTipText("escreva o modelo do dispositivo");
        txtCodEquipamento.setEnabled(false);
        txtCodEquipamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodEquipamentoActionPerformed(evt);
            }
        });

        btnSalvar3.setBackground(new java.awt.Color(0, 102, 102));
        btnSalvar3.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        btnSalvar3.setForeground(new java.awt.Color(255, 255, 255));
        btnSalvar3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/salvar-arquivo (1).png"))); // NOI18N
        btnSalvar3.setToolTipText("salvar dados");
        btnSalvar3.setBorder(null);
        btnSalvar3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalvar3MouseClicked(evt);
            }
        });
        btnSalvar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvar3ActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblusuario30)
                    .addComponent(lblusuario28)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel11)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblusuario27)
                                .addComponent(lblusuario29))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(cboTipoEquip, 0, 238, Short.MAX_VALUE)
                                .addComponent(txtModeloEquip)
                                .addComponent(txtCodEquipamento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtMarcaEquip)))
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(btnSalvar3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnDeletarUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblusuario30)
                    .addComponent(txtCodEquipamento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblusuario29)
                    .addComponent(cboTipoEquip, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtModeloEquip, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblusuario27))
                .addGap(37, 37, 37)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMarcaEquip, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblusuario28))
                .addGap(18, 27, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDeletarUsu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalvar3, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabelaEquip.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Tipo", "Modelo", "Marca"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabelaEquip.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabelaEquipMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTabelaEquip);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lblusuario19)
                                .addGap(18, 18, 18)
                                .addComponent(txtPesquisaPorMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblusuario20)
                                .addGap(30, 30, 30)
                                .addComponent(txtPesquisaPorTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel10))
                        .addGap(183, 183, 183)
                        .addComponent(jLabel8))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(31, 31, 31)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblusuario19)
                            .addComponent(txtPesquisaPorMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPesquisaPorTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblusuario20))))
                .addGap(35, 35, 35)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPesquisaPorMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesquisaPorMarcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesquisaPorMarcaActionPerformed

    private void txtPesquisaPorMarcaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaPorMarcaKeyTyped
        // Pesquisa por Marca
        String sql = "select * from Equipamento where marca LIKE '%"
                + txtPesquisaPorMarca.getText()
                + "%'"
                + " ORDER BY marca DESC";

        povoarjTabelaEquip(sql);
    }//GEN-LAST:event_txtPesquisaPorMarcaKeyTyped

    private void txtPesquisaPorTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesquisaPorTipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesquisaPorTipoActionPerformed

    private void txtPesquisaPorTipoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaPorTipoKeyTyped
        // Pesquisa pelo Tipo
        String sql = "select * from Equipamento where tipo LIKE '%"
                + txtPesquisaPorTipo.getText()
                + "%'"
                + " ORDER BY tipo DESC";

        povoarjTabelaEquip(sql);
    }//GEN-LAST:event_txtPesquisaPorTipoKeyTyped

    private void txtModeloEquipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtModeloEquipActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtModeloEquipActionPerformed

    private void txtMarcaEquipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMarcaEquipActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMarcaEquipActionPerformed

    private void txtCodEquipamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodEquipamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodEquipamentoActionPerformed

    private void btnSalvar3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalvar3MouseClicked
        // Método para salvar usuário
        //se tiver o código na tela ele vai ter que editar o usuario
        salvar();
    }//GEN-LAST:event_btnSalvar3MouseClicked

    private void btnSalvar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvar3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalvar3ActionPerformed

    private void jTabelaEquipMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabelaEquipMouseClicked
        // passar os valores da Jtabela para os textfields
        int linha = jTabelaEquip.getSelectedRow();//selecionar a linha jogar na variavel linha

        txtCodEquipamento.setText(jTabelaEquip.getValueAt(linha, 0).toString());
        cboTipoEquip.setSelectedItem(jTabelaEquip.getValueAt(linha, 1).toString());
        txtModeloEquip.setText(jTabelaEquip.getValueAt(linha, 2).toString());
        txtMarcaEquip.setText(jTabelaEquip.getValueAt(linha, 3).toString());
    }//GEN-LAST:event_jTabelaEquipMouseClicked

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaGerenEquip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaGerenEquip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaGerenEquip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaGerenEquip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaGerenEquip().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeletarUsu;
    private javax.swing.JButton btnSalvar3;
    private javax.swing.JComboBox<String> cboTipoEquip;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTabelaEquip;
    private javax.swing.JLabel lblusuario19;
    private javax.swing.JLabel lblusuario20;
    private javax.swing.JLabel lblusuario27;
    private javax.swing.JLabel lblusuario28;
    private javax.swing.JLabel lblusuario29;
    private javax.swing.JLabel lblusuario30;
    private javax.swing.JTextField txtCodEquipamento;
    private javax.swing.JTextField txtMarcaEquip;
    private javax.swing.JTextField txtModeloEquip;
    private javax.swing.JTextField txtPesquisaPorMarca;
    private javax.swing.JTextField txtPesquisaPorTipo;
    // End of variables declaration//GEN-END:variables
}