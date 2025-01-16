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

public final class TelaGerenUsuario extends javax.swing.JFrame {

    // Usando a variável conexão do DAL
    Connection conexao = null;
    // Criando variáveis especiais para conexão com o banco
    // Prepared Statement e ResultSet são frameworks do pacote java.sql
    // e servem para preparar e executar as instruções SQL
    PreparedStatement pst = null;
    ResultSet rs = null;
    //Método 
    public TelaGerenUsuario() {
        initComponents();
        conexao = ModuloConexao.conector();
        JTableHeader jth = jTabelaUsuario.getTableHeader();
        jth.setBackground(new Color(0, 0, 102));
        jth.setForeground(Color.BLACK);
        jth.setFont(new Font("CenturyGothic", Font.BOLD, 18));

        // Exibir os dados automaticamente
        String sql = "SELECT * FROM Usuario ORDER BY id_usuario DESC";
        povoarjTabelaUsuario(sql);
    }
    //Método para ler o id usuário
    private int lerIdUsuario() {
        int linha = jTabelaUsuario.getSelectedRow();
        int idUsuario = Integer.parseInt(jTabelaUsuario.getValueAt(linha, 0).toString());

        try {
            String sql = "SELECT id_usuario FROM Usuario WHERE id_usuario = ?";
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idUsuario);
            rs = pst.executeQuery();

            if (rs.next()) {
                idUsuario = rs.getInt("id_usuario");
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível obter os dados do Banco. Erro: " + e);
        }

        return idUsuario;
    }
    //Método para povoar a Tabela
    public void povoarjTabelaUsuario(String sql) {
        try {
            PreparedStatement pst = null;
            ResultSet rs = null;

            DefaultTableModel model = (DefaultTableModel) jTabelaUsuario.getModel();
            model.setNumRows(0);

            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_usuario"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("Endereco"),
                    rs.getString("celular"),
                    rs.getString("email"),
                    rs.getString("Login"),
                    rs.getString("Senha"),
                    rs.getString("Perfil")
                });
            }
            // Fechar o ResultSet e o PreparedStatement
            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível obter os dados do Banco. Erro: " + e);
        }
    }
    //Método para Salvar
    private void salvar() {
        String sql = "";
        if (txtCodigo.getText().isEmpty()) { // Se estiver vazio, é pq esta criando o item
            sql = "INSERT INTO Usuario (nome, cpf, endereco, celular, email, login, senha, perfil) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        } else {
            sql = "UPDATE Usuario SET nome = ?, cpf = ?, endereco = ?, celular = ?, email = ?, login = ?, senha = ?, perfil = ? WHERE id_usuario = ?";
        }
        try {
            // Validação dos campos obrigatórios
            if ((txtNomeUsu.getText().isEmpty()) || (txtEnderUsu.getText().isEmpty()) || (txtCelularUsu.getText().isEmpty()) || (txtEmailUsu.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {

                // Realizar a atualização dos dados
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtNomeUsu.getText());
                pst.setString(2, txtCpfUsu.getText());
                pst.setString(3, txtEnderUsu.getText());
                pst.setString(4, txtCelularUsu.getText());
                pst.setString(5, txtEmailUsu.getText());
                pst.setString(6, txtLogin.getText());
                pst.setString(7, txtSenha.getText());
                pst.setString(8, (String) cboUsuPerfil.getSelectedItem());
                

                if (!txtCodigo.getText().isEmpty()) {
                    pst.setString(9, txtCodigo.getText());
                }

                // Executar a atualização no banco de dados
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    txtNomeUsu.setText(null);
                    txtCpfUsu.setText(null);
                    txtEnderUsu.setText(null);
                    txtCelularUsu.setText(null);
                    txtEmailUsu.setText(null);
                    txtLogin.setText(null);
                    txtSenha.setText(null);
                    cboUsuPerfil.setSelectedItem(null);
                    txtCodigo.setText(null);
                    JOptionPane.showMessageDialog(null, "Usuário salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao salvar Usuário!", "Erro", JOptionPane.ERROR_MESSAGE);
                }

                sql = "Select * from Usuario Order By id_usuario Desc";
                povoarjTabelaUsuario(sql);
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(null, "Usuario já cadastrado.");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao adicionar equipamento: " + e.getMessage());
            }
        }
    }
    //Método para Remover
    private void remover() {
        int linhaSelecionada = jTabelaUsuario.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um usuario para remover.");
            return;
        }

        int confirma = JOptionPane.showConfirmDialog(null, "Tem Certeza que deseja remover este usuário?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                int idUsuario = lerIdUsuario(); // Obter o ID do Usuario selecionado na tabela

                String sql = "DELETE FROM Usuario WHERE id_usuario = ?";
                pst = conexao.prepareStatement(sql);
                pst.setInt(1, idUsuario);

                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário removido com sucesso!");
                }
            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao remover usuario: " + e.getMessage());
            }
        }
        atualizarTabelaUsuario();
    }
    //Método para atualizar a Tabela
    private void atualizarTabelaUsuario() {
        DefaultTableModel tableModel = (DefaultTableModel) jTabelaUsuario.getModel();
        tableModel.setRowCount(0); // Limpa as linhas existentes na tabela

        try {
            // Execute a consulta SQL para obter os dados atualizados do banco de dados
            String sql = "SELECT id_usuario, nome, cpf, endereco, celular, email, login, senha, perfil FROM Usuario ORDER BY id_usuario DESC";
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            // Adicione as novas linhas com os dados atualizados à tabela
            while (rs.next()) {
                String idUsuario = rs.getString("id_usuario");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                String endereco = rs.getString("endereco");
                String celular = rs.getString("celular");
                String email = rs.getString("email");
                String login = rs.getString("login");
                String senha = rs.getString("senha");
                String perfil = rs.getString("perfil");

                tableModel.addRow(new Object[]{idUsuario, nome, cpf, endereco, celular, email, login, senha, perfil});
            }

            // Feche o ResultSet e o PreparedStatement
            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível obter os dados do Banco. Erro: " + e);
        }
    }

    private void preencherForm(String codigo) {

        String sqlPesquisa = "SELECT * FROM Usuario WHERE id_usuario = ?";

        try {
            // Pesquisar o usuário com base no nome
            pst = conexao.prepareStatement(sqlPesquisa);
            pst.setString(1, codigo);
            ResultSet rs = pst.executeQuery();

            // Verificar se o usuário foi encontrado
            if (rs.next()) {
                // Preencher o formulário com os dados do usuário encontrado
                String idUsuario = rs.getString("id_usuario");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                String endereco = rs.getString("endereco");
                String celular = rs.getString("celular");
                String email = rs.getString("email");
                String login = rs.getString("login");
                String senha = rs.getString("senha");
                String perfil = rs.getString("perfil");

                // Atualizar os campos do formulário com os dados do usuário
                txtCodigo.setText(idUsuario);
                txtNomeUsu.setText(nome);
                txtCpfUsu.setText(cpf);
                txtEnderUsu.setText(endereco);
                txtCelularUsu.setText(celular);
                txtEmailUsu.setText(email);
                txtLogin.setText(login);
                txtSenha.setText(senha);
                cboUsuPerfil.setSelectedItem(perfil);
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabelaUsuario = new javax.swing.JTable();
        txtPesquisaPorCpf1 = new javax.swing.JTextField();
        txtPesquisaPorNome = new javax.swing.JTextField();
        lblusuario10 = new javax.swing.JLabel();
        lblusuario11 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnDeletarUsu = new javax.swing.JButton();
        lblusuario3 = new javax.swing.JLabel();
        txtEmailUsu = new javax.swing.JTextField();
        txtCelularUsu = new javax.swing.JFormattedTextField();
        lblusuario8 = new javax.swing.JLabel();
        txtEnderUsu = new javax.swing.JTextField();
        lblEnderUsu = new javax.swing.JLabel();
        lblCpfUsu = new javax.swing.JLabel();
        lblusuario4 = new javax.swing.JLabel();
        txtNomeUsu = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cboUsuPerfil = new javax.swing.JComboBox<>();
        txtCpfUsu = new javax.swing.JFormattedTextField();
        txtLogin = new javax.swing.JTextField();
        txtSenha = new javax.swing.JTextField();
        lblusuario5 = new javax.swing.JLabel();
        lblusuario6 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        lblusuario7 = new javax.swing.JLabel();
        btnSalvar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro Usuário");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        jTabelaUsuario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nome", "Cpf", "Endereço", "Celular", "E-mail", "Login", "Senha", "Perfil"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabelaUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabelaUsuarioMouseClicked(evt);
            }
        });
        jTabelaUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTabelaUsuarioKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTabelaUsuario);

        txtPesquisaPorCpf1.setBackground(new java.awt.Color(204, 204, 204));
        txtPesquisaPorCpf1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesquisaPorCpf1ActionPerformed(evt);
            }
        });
        txtPesquisaPorCpf1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesquisaPorCpf1KeyTyped(evt);
            }
        });

        txtPesquisaPorNome.setBackground(new java.awt.Color(204, 204, 204));
        txtPesquisaPorNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesquisaPorNomeActionPerformed(evt);
            }
        });
        txtPesquisaPorNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesquisaPorNomeKeyTyped(evt);
            }
        });

        lblusuario10.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario10.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario10.setText("Pesquisa por Nome");

        lblusuario11.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario11.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario11.setText("Pesquisa por CPF");

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CADASTRO DE USUÁRIO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

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

        lblusuario3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario3.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario3.setText("E-mail");

        txtEmailUsu.setBackground(new java.awt.Color(204, 204, 204));
        txtEmailUsu.setToolTipText("e-mail do cliente");
        txtEmailUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailUsuActionPerformed(evt);
            }
        });

        txtCelularUsu.setBackground(new java.awt.Color(204, 204, 204));
        try {
            txtCelularUsu.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) # ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCelularUsu.setToolTipText("ddd + número de celular do cliente");

        lblusuario8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario8.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario8.setText("*Celular");

        txtEnderUsu.setBackground(new java.awt.Color(204, 204, 204));
        txtEnderUsu.setToolTipText("endereço");
        txtEnderUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEnderUsuActionPerformed(evt);
            }
        });

        lblEnderUsu.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblEnderUsu.setForeground(new java.awt.Color(255, 255, 255));
        lblEnderUsu.setText("Endereço");

        lblCpfUsu.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblCpfUsu.setForeground(new java.awt.Color(255, 255, 255));
        lblCpfUsu.setText("*Cpf");

        lblusuario4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario4.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario4.setText("*Nome");

        txtNomeUsu.setBackground(new java.awt.Color(204, 204, 204));
        txtNomeUsu.setToolTipText("nome completo do cliente");
        txtNomeUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeUsuActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("* campos obrigatórios");

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("*Perfil");

        cboUsuPerfil.setBackground(new java.awt.Color(204, 204, 204));
        cboUsuPerfil.setForeground(new java.awt.Color(255, 255, 255));
        cboUsuPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "atend", "tecn" }));

        txtCpfUsu.setBackground(new java.awt.Color(204, 204, 204));
        try {
            txtCpfUsu.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        txtLogin.setBackground(new java.awt.Color(204, 204, 204));

        txtSenha.setBackground(new java.awt.Color(204, 204, 204));
        txtSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSenhaActionPerformed(evt);
            }
        });

        lblusuario5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario5.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario5.setText("*Login");

        lblusuario6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario6.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario6.setText("*Senha");

        txtCodigo.setBackground(new java.awt.Color(204, 204, 204));
        txtCodigo.setEnabled(false);

        lblusuario7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblusuario7.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario7.setText("Código");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblusuario7, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblusuario4)
                                .addComponent(lblCpfUsu)
                                .addComponent(lblusuario3)
                                .addComponent(lblusuario6))
                            .addGap(5, 5, 5)))
                    .addComponent(lblEnderUsu)
                    .addComponent(lblusuario8)
                    .addComponent(lblusuario5)
                    .addComponent(jLabel2))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtNomeUsu, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmailUsu, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(txtCpfUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 115, Short.MAX_VALUE))
                            .addComponent(txtEnderUsu, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(43, 43, 43))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtLogin, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboUsuPerfil, javax.swing.GroupLayout.Alignment.LEADING, 0, 135, Short.MAX_VALUE)
                            .addComponent(txtSenha, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCelularUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeletarUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))
                    .addComponent(jLabel1))
                .addGap(35, 35, 35))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblusuario7)
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNomeUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblusuario4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCpfUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCpfUsu))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEnderUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEnderUsu))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblusuario8)
                    .addComponent(txtCelularUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmailUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblusuario3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblusuario5)
                    .addComponent(txtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblusuario6))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cboUsuPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(btnDeletarUsu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/assistencia/icones/logo (2).png"))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Usuário");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 938, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblusuario10)
                                .addGap(18, 18, 18)
                                .addComponent(txtPesquisaPorNome, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblusuario11)
                                .addGap(18, 18, 18)
                                .addComponent(txtPesquisaPorCpf1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)))
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel10)
                        .addGap(28, 28, 28)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblusuario11)
                                .addComponent(txtPesquisaPorCpf1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblusuario10)
                                .addComponent(txtPesquisaPorNome, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 588, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );

        getContentPane().add(jPanel2);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPesquisaPorCpf1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesquisaPorCpf1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesquisaPorCpf1ActionPerformed

    private void txtPesquisaPorCpf1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaPorCpf1KeyTyped
        // Pesquisa pelo Cpf
        String sql = "select * from Usuario where cpf LIKE '%"
                + txtPesquisaPorCpf1.getText()
                + "%'"
                + " ORDER BY id_usuario DESC";
        povoarjTabelaUsuario(sql);
    }//GEN-LAST:event_txtPesquisaPorCpf1KeyTyped

    private void txtPesquisaPorNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesquisaPorNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesquisaPorNomeActionPerformed

    private void txtPesquisaPorNomeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaPorNomeKeyTyped
        // Pesquisa pelo nome
        String sql = "select * from Usuario where nome LIKE '%"
                + txtPesquisaPorNome.getText()
                + "%'"
                + " ORDER BY id_usuario DESC";
        povoarjTabelaUsuario(sql);
    }//GEN-LAST:event_txtPesquisaPorNomeKeyTyped

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

    private void txtNomeUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeUsuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeUsuActionPerformed

    private void txtEmailUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailUsuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailUsuActionPerformed

    private void txtEnderUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEnderUsuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEnderUsuActionPerformed

    private void jTabelaUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabelaUsuarioMouseClicked
        // passar os valores da Jtabela para os textfields
        int linha = jTabelaUsuario.getSelectedRow();//selecionar a linha jogar na variavel linha
        txtNomeUsu.setText(jTabelaUsuario.getValueAt(linha, 1).toString());
        preencherForm(jTabelaUsuario.getValueAt(linha, 0).toString());
    }//GEN-LAST:event_jTabelaUsuarioMouseClicked

    private void jTabelaUsuarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabelaUsuarioKeyTyped

    }//GEN-LAST:event_jTabelaUsuarioKeyTyped

    private void btnDeletarUsuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeletarUsuMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_btnDeletarUsuMouseClicked

    private void txtSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSenhaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSenhaActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnSalvarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalvarMouseClicked
        // Método para salvar usuário
        //se tiver o código na tela ele vai ter que editar o usuario
        salvar();


    }//GEN-LAST:event_btnSalvarMouseClicked

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
            java.util.logging.Logger.getLogger(TelaGerenUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new TelaGerenUsuario().setVisible(true);
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeletarUsu;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox<String> cboUsuPerfil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabelaUsuario;
    private javax.swing.JLabel lblCpfUsu;
    private javax.swing.JLabel lblEnderUsu;
    private javax.swing.JLabel lblusuario10;
    private javax.swing.JLabel lblusuario11;
    private javax.swing.JLabel lblusuario3;
    private javax.swing.JLabel lblusuario4;
    private javax.swing.JLabel lblusuario5;
    private javax.swing.JLabel lblusuario6;
    private javax.swing.JLabel lblusuario7;
    private javax.swing.JLabel lblusuario8;
    private javax.swing.JFormattedTextField txtCelularUsu;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JFormattedTextField txtCpfUsu;
    private javax.swing.JTextField txtEmailUsu;
    private javax.swing.JTextField txtEnderUsu;
    private javax.swing.JTextField txtLogin;
    private javax.swing.JTextField txtNomeUsu;
    private javax.swing.JTextField txtPesquisaPorCpf1;
    private javax.swing.JTextField txtPesquisaPorNome;
    private javax.swing.JTextField txtSenha;
    // End of variables declaration//GEN-END:variables
}
