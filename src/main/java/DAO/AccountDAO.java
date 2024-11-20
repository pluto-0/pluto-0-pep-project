package DAO;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    public boolean idExists(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE account_id = ?;";
            PreparedStatement query = connection.prepareStatement(sql);

            query.setInt(1, account_id);
            ResultSet results = query.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    
    private boolean accountExists(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ?;";
            PreparedStatement query = connection.prepareStatement(sql);

            query.setString(1, account.getUsername());
            ResultSet results = query.executeQuery();
            if (results.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public Account addAccount(Account account) {
        if (account.getPassword().length() < 4 || account.getPassword().length() > 255 || account.getUsername().length() > 255 || account.getUsername().length() == 0) {
            return null;
        }
        if (accountExists(account)) {
            return null;
        }

        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.executeUpdate();
            ResultSet results = statement.getGeneratedKeys();
            if (results.next()) {
                int account_id = (int) results.getInt(1);
                return new Account(account_id, account.getUsername(), account.getPassword());
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account login(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                int id = results.getInt("account_id");
                String username = results.getString("username");
                String password = results.getString("password");
                return new Account(id, username, password);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
