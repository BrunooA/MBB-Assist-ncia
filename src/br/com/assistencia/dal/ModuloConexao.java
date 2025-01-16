package br.com.assistencia.dal;

import java.sql.*;

public class ModuloConexao {

    //metodo responsavel por estabelecer a conexão com o banco
    public static Connection conector() {
        java.sql.Connection conexao = null;
        // a linha abaixo "chama" o driver
        String driver = "com.mysql.cj.jdbc.Driver";
        //armazenando informaçoes referente ao banco
        String url = "jdbc:mysql://localhost:3306/mbb";
        String user = "root";
        String password = "";

        //Estabelecendo a conexão com o banco
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (ClassNotFoundException | SQLException e) {
            //a linha abaixo serve de apoio para esclarecer o erro
            System.out.println(e);
            return null;
        }
    }
    
    public static void fecharConexao(Connection conn, PreparedStatement pst, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {

        }
    }
}
