package br.com.assistencia.telas;

import br.com.assistencia.dal.ModuloConexao;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class TelaGerenCliente extends javax.swing.JFrame {

    //usando a variavel conexao do DAL
    Connection conexao = null;
    //criando variáveis especiais para conexão com o banco
    //Prepared Statement e ResultSet são fremeworks do pacote java.sql
    //e servem para preparar e executar as instruções SQL
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    public TelaGerenCliente() {
        initComponents();
        conexao = ModuloConexao.conector();
        JTableHeader jth = jTabelaCliente.getTableHeader();
        jth.setBackground(new Color(0, 0, 102));
        jth.setForeground(Color.BLACK);
        jth.setFont(new Font("CenturyGothic", Font.BOLD, 18));

        // Exibir os dados automaticamente
        String sql = "SELECT * FROM Cliente ORDER BY id_cliente DESC";
        povoarjTabelaCliente(sql);
    }
    
    private int lerIdCliente() {
        int linha = jTabelaCliente.getSelectedRow();
        int idCliente = Integer.parseInt(jTabelaCliente.getValueAt(linha, 0).toString());

        try {
            String sql = "SELECT id_cliente FROM Usuario WHERE id_ciente = ?";
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idCliente);
            rs = pst.executeQuery();

            if (rs.next()) {
                idCliente = rs.getInt("id_cliente");
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível obter os dados do Banco. Erro: " + e);
        }

        return idCliente;
    }
    //Método para povoar a Tabela
    public void povoarjTabelaCliente(String sql) {
        try {
            PreparedStatement pst = null;
            ResultSet rs = null;

            DefaultTableModel model = (DefaultTableModel) jTabelaCliente.getModel();
            model.setNumRows(0);

            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_cliente"),
                    rs.getString("nome"),
                    rs.getString("cpf_cnpj"),
                    rs.getString("endereco"),
                    rs.getString("telefone"),
                    rs.getString("celular"),
                    rs.getString("email")
                });
            }
            // Fechar o ResultSet e o PreparedStatement
            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível obter os dados do Banco. Erro: " + e);
        }
    }
    //método para Salvar
    private void salvar() {
        String sql = "";
        if (txtCodigo.getText().isEmpty()) {
            sql = "INSERT into Cliente (nome, cpf_cnpj, endereco, telefone, celular, email) values (?, ?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE Cliente SET nome = ?, cpf_cnpj = ?, endereco = ?, telefone = ?, celular = ?, email = ? WHERE id_cliente = ?";
        }
        try {
            //vaçidação dos campos obrigatórios
            if ((txtNomeCli.getText().isEmpty()) || (txtEnderCli.getText().isEmpty()) || (txtTelefCli.getText().isEmpty()) || (txtCelCli.getText().isEmpty()) || (txtEmailCli.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
                //return; // Retornar para evitar a inserção no banco de dados
            } else {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtNomeCli.getText());
                pst.setString(2, txtCpfCnpjCli.getText());
                pst.setString(3, txtEnderCli.getText());
                pst.setString(4, txtTelefCli.getText());
                pst.setString(5, txtCelCli.getText());
                pst.setString(6, txtEmailCli.getText());
                

                if (!txtCodigo.getText().isEmpty()) {
                    pst.setString(7, txtCodigo.getText());
                }

                // Executar a atualização no banco de dados
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    txtNomeCli.setText(null);
                    txtCpfCnpjCli.setText(null);
                    txtEnderCli.setText(null);
                    txtTelefCli.setText(null);
                    txtCelCli.setText(null);
                    txtEmailCli.setText(null);
                    txtCodigo.setText(null);
                    JOptionPane.showMessageDialog(null, "Cliente salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao salvar Cliente!", "Erro", JOptionPane.ERROR_MESSAGE);
                }

                sql = "Select * from Cliente Order By id_cliente Desc";
                povoarjTabelaCliente(sql);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(null, "Cliente já cadastrado.");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao adicionar equipamento: " + e.getMessage());
            }
        }
        atualizarTabelaCliente();
    }
    //Método para Remover
    private void remover() {
        int linhaSelecionada = jTabelaCliente.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um cliente para remover.");
            return;
        }

        int confirma = JOptionPane.showConfirmDialog(null, "Tem Certeza que deseja remover este cliente?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                int idCliente = lerIdCliente(); // Obter o ID do cliente selecionado na tabela

                String sql = "DELETE FROM Cliente WHERE id_cliente = ?";
                pst = conexao.prepareStatement(sql);
                pst.setInt(1, idCliente);

                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente removido com sucesso!");
                }
            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao remover cliente: " + e.getMessage());
            }
        }
        atualizarTabelaCliente();
    }
    //Método para Atualizar a Tabela
    private void atualizarTabelaCliente() {
        DefaultTableModel tableModel = (DefaultTableModel) jTabelaCliente.getModel();
        tableModel.setRowCount(0); // Limpa as linhas existentes na tabela

        try {
            // Execute a consulta SQL para obter os dados atualizados do banco de dados
            String sql = "SELECT id_cliente, nome, cpf_cnpj, endereco, telefone, celular, email FROM Cliente ORDER BY id_cliente DESC";
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            // Adicione as novas linhas com os dados atualizados à tabela
            while (rs.next()) {
                String idCliente = rs.getString("id_cliente");
                String nome = rs.getString("nome");
                String cpfCnpj1 = rs.getString("cpf_cnpj");
                String endereco = rs.getString("endereco");
                String telefone = rs.getString("telefone");
                String celular = rs.getString("celular");
                String email = rs.getString("email");

                tableModel.addRow(new Object[]{idCliente, nome, cpfCnpj1, endereco, telefone, celular, email});
            }

            // Feche o ResultSet e o PreparedStatement
            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível obter os dados do Banco. Erro: " + e);
        }
    }
    //Método para preencher o formulário
    private void preencherForm(String codigo) {

        String sqlPesquisa = "SELECT * FROM Cliente WHERE id_cliente = ?";

        try {
            // Pesquisar o usuário com base no nome
            pst = conexao.prepareStatement(sqlPesquisa);
            pst.setString(1, codigo);
            ResultSet rs = pst.executeQuery();

            // Verificar se o usuário foi encontrado
            if (rs.next()) {
                // Preencher o formulário com os dados do usuário encontrado
                String idCliente = rs.getString("id_cliente");
                String nome = rs.getString("nome");
                String cpfCnpj = rs.getString("cpf_cnpj");
                String endereço = rs.getString("endereco");
                String telefone = rs.getString("telefone");
                String celular = rs.getString("celular");
                String email = rs.getString("email");

                // Atualizar os campos do formulário com os dados do usuário
                txtCodigo.setText(idCliente);
                txtNomeCli.setText(nome);
                txtCpfCnpjCli.setText(cpfCnpj);
                txtEnderCli.setText(endereço);
                txtTelefCli.setText(telefone);
                txtCelCli.setText(celular);
                txtEmailCli.setText(email);
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        txtPesquisaPorNome1 = new javax.swing.JTextField();
        lblusuario10 = new javax.swing.JLabel();
        lblusuario11 = new javax.swing.JLabel();
        txtPesquisaPorCpfcnpj = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabelaCliente = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        lblusuario3 = new javax.swing.JLabel();
        txtEmailCli = new javax.swing.JTextField();
        txtCelCli = new javax.swing.JFormattedTextField();
        lblusuario8 = new javax.swing.JLabel();
        lblusuario7 = new javax.swing.JLabel();
        txtTelefCli = new javax.swing.JFormattedTextField();
        txtEnderCli = new javax.swing.JTextField();
        lblusuario6 = new javax.swing.JLabel();
        txtCpfCnpjCli = new javax.swing.JFormattedTextField();
        lblusuario4 = new javax.swing.JLabel();
        txtNomeCli = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cboCpfCnpj = new javax.swing.JComboBox<>();
        btnSalvar = new javax.swing.JButton();
        btnDeletarUsu = new javax.swing.JButton();
        txtCodigo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lblusuario5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro Cliente");

        jPanel3.setBackground(new java.awt.Color(0, 153, 153));

        txtPesquisaPorNome1.setBackground(new java.awt.Color(204, 204, 204));
        txtPesquisaPorNome1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesquisaPorNome1ActionPerformed(evt);
            }
        });
        txtPesquisaPorNome1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesquisaPorNome1KeyTyped(evt);
            }
        });

        lblusuario10.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario10.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario10.setText("Pesquisa por Nome");

        lblusuario11.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario11.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario11.setText("Pesquisa por CPF");

        txtPesquisaPorCpfcnpj.setBackground(new java.awt.Color(204, 204, 204));
        txtPesquisaPorCpfcnpj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesquisaPorCpfcnpjActionPerformed(evt);
            }
        });
        txtPesquisaPorCpfcnpj.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesquisaPorCpfcnpjKeyTyped(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/logo (2).png"))); // NOI18N

        jTabelaCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nome", "CPF/CNPJ", "Endereço", "Telefone", "Celular", "E-mail"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabelaCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabelaClienteMouseClicked(evt);
            }
        });
        jTabelaCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTabelaClienteKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTabelaCliente);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CADASTRO DE CLIENTE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        lblusuario3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario3.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario3.setText("E-mail");

        txtEmailCli.setBackground(new java.awt.Color(204, 204, 204));
        txtEmailCli.setToolTipText("e-mail do cliente");
        txtEmailCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailCliActionPerformed(evt);
            }
        });

        txtCelCli.setBackground(new java.awt.Color(204, 204, 204));
        try {
            txtCelCli.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) # ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCelCli.setToolTipText("ddd + número de celular do cliente");

        lblusuario8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario8.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario8.setText("Celular");

        lblusuario7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario7.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario7.setText("Telefone");

        txtTelefCli.setBackground(new java.awt.Color(204, 204, 204));
        try {
            txtTelefCli.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) # ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtTelefCli.setToolTipText("ddd + número de celular do cliente");
        txtTelefCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefCliActionPerformed(evt);
            }
        });

        txtEnderCli.setBackground(new java.awt.Color(204, 204, 204));
        txtEnderCli.setToolTipText("endereço");
        txtEnderCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEnderCliActionPerformed(evt);
            }
        });

        lblusuario6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario6.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario6.setText("Endereço");

        txtCpfCnpjCli.setBackground(new java.awt.Color(204, 204, 204));
        try {
            txtCpfCnpjCli.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCpfCnpjCli.setToolTipText("cpf do cliente");
        txtCpfCnpjCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCpfCnpjCliActionPerformed(evt);
            }
        });

        lblusuario4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario4.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario4.setText("*Nome");

        txtNomeCli.setBackground(new java.awt.Color(204, 204, 204));
        txtNomeCli.setToolTipText("nome do cliente");
        txtNomeCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeCliActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("* campos obrigatórios");

        cboCpfCnpj.setBackground(new java.awt.Color(204, 204, 204));
        cboCpfCnpj.setForeground(new java.awt.Color(255, 255, 255));
        cboCpfCnpj.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CPF", "CNPJ" }));

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

        txtCodigo.setBackground(new java.awt.Color(204, 204, 204));
        txtCodigo.setEnabled(false);
        txtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Código");

        lblusuario5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario5.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario5.setText("*");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblusuario3)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblusuario4)
                                        .addComponent(jLabel4))
                                    .addComponent(lblusuario6)
                                    .addComponent(lblusuario7)
                                    .addComponent(lblusuario8)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(lblusuario5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cboCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtCpfCnpjCli, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtNomeCli, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                                            .addComponent(txtEnderCli))
                                        .addGap(25, 25, 25))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtCelCli, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtTelefCli, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txtEmailCli)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnDeletarUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(19, 19, 19))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(66, 66, 66))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblusuario4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCpfCnpjCli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboCpfCnpj)
                            .addComponent(lblusuario5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblusuario6)
                            .addComponent(txtEnderCli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblusuario7)
                            .addComponent(txtTelefCli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addComponent(lblusuario8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCelCli, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmailCli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblusuario3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDeletarUsu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Cliente");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lblusuario10)
                                .addGap(18, 18, 18)
                                .addComponent(txtPesquisaPorNome1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblusuario11)
                                .addGap(30, 30, 30)
                                .addComponent(txtPesquisaPorCpfcnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(371, 371, 371))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(jLabel6))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 862, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel6))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblusuario10)
                            .addComponent(txtPesquisaPorNome1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPesquisaPorCpfcnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblusuario11))))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPesquisaPorNome1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesquisaPorNome1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesquisaPorNome1ActionPerformed

    private void txtPesquisaPorNome1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaPorNome1KeyTyped
        // Pesquisa pelo nome
        String sql = "select * from Cliente where nome LIKE '%"
                + txtPesquisaPorNome1.getText()
                + "%'"
                + " ORDER BY id_cliente DESC";
        povoarjTabelaCliente(sql);
    }//GEN-LAST:event_txtPesquisaPorNome1KeyTyped

    private void txtPesquisaPorCpfcnpjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesquisaPorCpfcnpjActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesquisaPorCpfcnpjActionPerformed

    private void txtPesquisaPorCpfcnpjKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaPorCpfcnpjKeyTyped
        // Pesquisa pelo Cpf_Cnpj
        String sql = "select * from Cliente where cpf_cnpj LIKE '%"
                + txtPesquisaPorCpfcnpj.getText()
                + "%'"
                + " ORDER BY id_cliente DESC";
        povoarjTabelaCliente(sql);
    }//GEN-LAST:event_txtPesquisaPorCpfcnpjKeyTyped

    private void jTabelaClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabelaClienteMouseClicked
        // passar os valores da Jtable para os textfields
        int linha = jTabelaCliente.getSelectedRow();//selecionar a linha jogar na variavel linha
        txtNomeCli.setText(jTabelaCliente.getValueAt(linha, 1).toString());
        preencherForm(jTabelaCliente.getValueAt(linha, 0).toString());
    }//GEN-LAST:event_jTabelaClienteMouseClicked

    private void jTabelaClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabelaClienteKeyTyped

    }//GEN-LAST:event_jTabelaClienteKeyTyped

    private void txtEmailCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailCliActionPerformed

    private void txtTelefCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefCliActionPerformed

    private void txtEnderCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEnderCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEnderCliActionPerformed

    private void txtCpfCnpjCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCpfCnpjCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCpfCnpjCliActionPerformed

    private void txtNomeCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeCliActionPerformed

    private void btnSalvarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalvarMouseClicked
        // Método para salvar usuário
        salvar();
    }//GEN-LAST:event_btnSalvarMouseClicked

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalvarActionPerformed

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

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoActionPerformed

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
            java.util.logging.Logger.getLogger(TelaGerenCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaGerenCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaGerenCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaGerenCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaGerenCliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeletarUsu;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox<String> cboCpfCnpj;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabelaCliente;
    private javax.swing.JLabel lblusuario10;
    private javax.swing.JLabel lblusuario11;
    private javax.swing.JLabel lblusuario3;
    private javax.swing.JLabel lblusuario4;
    private javax.swing.JLabel lblusuario5;
    private javax.swing.JLabel lblusuario6;
    private javax.swing.JLabel lblusuario7;
    private javax.swing.JLabel lblusuario8;
    private javax.swing.JFormattedTextField txtCelCli;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JFormattedTextField txtCpfCnpjCli;
    private javax.swing.JTextField txtEmailCli;
    private javax.swing.JTextField txtEnderCli;
    private javax.swing.JTextField txtNomeCli;
    private javax.swing.JTextField txtPesquisaPorCpfcnpj;
    private javax.swing.JTextField txtPesquisaPorNome1;
    private javax.swing.JFormattedTextField txtTelefCli;
    // End of variables declaration//GEN-END:variables
}
