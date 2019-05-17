/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.easylist.daos;

import br.com.easylist.entidades.Produto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProdutoDAO {

    private Connection conexao;
    private String url = "jdbc:mysql://localhost/easylist"; //Nome da base de dados
    private String user = "root"; //nome do usuário do MySQL
    private String password = ""; //senha do MySQL

    public ProdutoDAO() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexao = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int save(Produto produto) throws SQLException {
        String sql = "INSERT INTO produto VALUES (null,?,?,?,?,?)";
        PreparedStatement preparedStatement;
        preparedStatement = conexao.prepareStatement(sql);
        preparedStatement.setString(1, produto.getNome());
        preparedStatement.setString(2, produto.getValor());
        preparedStatement.setString(3, produto.getMercado());
        preparedStatement.setString(4, produto.getDescricao());
        preparedStatement.setString(5, produto.getComprovante());
        return preparedStatement.executeUpdate();
    }

    public int update(Produto produto) throws SQLException {
        String sql = "UPDATE produto SET  nome = ?, valor = ?, mercado = ?, descricao = ?, comprovante = ? WHERE id = ?";
        PreparedStatement preparedStatement = conexao.prepareStatement(sql);
        preparedStatement.setString(1, produto.getNome());
        preparedStatement.setString(2, produto.getValor());
        preparedStatement.setString(3, produto.getMercado());
        preparedStatement.setString(4, produto.getDescricao());
        preparedStatement.setString(5, produto.getComprovante());
        preparedStatement.setInt(6, produto.getId());
        return preparedStatement.executeUpdate();
    }

    public List<Produto> selects(String nome) throws SQLException {
        String sql = null;
        if (nome == null || nome.isEmpty()) {
            sql = "SELECT * FROM produto";
        } else {
            sql = "SELECT * FROM produto WHERE nome like '%" + nome + "%'";
        }
        PreparedStatement preparedStatement = conexao.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Produto> results = new ArrayList();
        while (resultSet.next()) {
            Produto produto = new Produto();
            produto.setId(resultSet.getInt("id"));
            produto.setNome(resultSet.getString("nome"));
            results.add(produto);
        }
        return results;
    }

    public Produto select(int id) throws SQLException {
        String sql = "SELECT * FROM produto WHERE id = ?";
        PreparedStatement preparedStatement = conexao.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Produto produto = null;
        if (resultSet.next()) {
            produto = new Produto();
            produto.setId(resultSet.getInt("id"));
            produto.setNome(resultSet.getString("nome"));
            return produto;
        } else {
            return null;
        }
    }

    public boolean closeConnection() {
        try {
            conexao.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Produto delete(int id) throws SQLException {
        Produto produto = this.select(id);
        if (produto != null) {
            String sql = "DELETE FROM produto WHERE id = ?";
            PreparedStatement preparedStatement = conexao.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            return produto;
        } else {
            return null;
        }
    }

}
