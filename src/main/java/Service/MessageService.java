package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public Message addMessage(Message message) {
        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessage(int message_id) {
        return messageDAO.getMessage(message_id);
    }

    public Message deleteMessage(int message_id) {
        return messageDAO.deleteMessage(message_id);
    }

    public Message updateMessage(int message_id, String new_text) {
        return messageDAO.updateMessage(message_id, new_text);
    }

    public List<Message> getAccountMessages(int account_id) {
        return messageDAO.getAccountMessages(account_id);
    }
}
