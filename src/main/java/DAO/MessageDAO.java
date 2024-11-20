package DAO;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Model.Message;
import Service.AccountService;
import Util.ConnectionUtil;

import java.util.ArrayList;
import java.util.List;

//import org.junit.runners.model.Statement;

public class MessageDAO {
    
    public Message insertMessage(Message message) {
        AccountService accountService = new AccountService();
        if (message.getMessage_text().length() == 0 || message.getMessage_text().length() > 255 || !accountService.idExists(message.getPosted_by())) {
            return null;
        }

        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet results = preparedStatement.getGeneratedKeys();
            if (results.next()) {
                int message_id = (int) results.getInt(1);
                return new Message(message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                int id = results.getInt("message_id");
                int poster = results.getInt("posted_by");
                String text = results.getString("message_text");
                long time_posted = results.getLong("time_posted_epoch");

                messages.add(new Message(id, poster, text, time_posted));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessage(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement query = connection.prepareStatement(sql);
            query.setInt(1, message_id);
            ResultSet results = query.executeQuery();
            while (results.next()) {
                int found_id = results.getInt("message_id");
                int poster = results.getInt("posted_by");
                String text = results.getString("message_text");
                long time_posted = results.getLong("time_posted_epoch");
                return new Message(found_id, poster, text, time_posted);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessage(int message_id) {
        Message deleted_message = getMessage(message_id);
        if (deleted_message == null) {
            return null;
        }
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, message_id);
            statement.executeUpdate();
            return deleted_message;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message updateMessage(int message_id, String new_text) {
        if (new_text.length() == 0 || new_text.length() > 255 || getMessage(message_id) == null) {
            return null;
        }
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, new_text);
            statement.setInt(2, message_id);
            statement.executeUpdate();
            return getMessage(message_id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAccountMessages(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement query = connection.prepareStatement(sql);
            query.setInt(1, account_id);
            ResultSet results = query.executeQuery();
            while (results.next()) {
                int message_id = results.getInt("message_id");
                int poster = results.getInt("posted_by");
                String text = results.getString("message_text");
                long date_posted = results.getLong("time_posted_epoch");
                messages.add(new Message(message_id, poster, text, date_posted));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
