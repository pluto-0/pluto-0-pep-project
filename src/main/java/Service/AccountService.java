package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public boolean idExists(int account_id) {
        return accountDAO.idExists(account_id);
    }

    public Account addAccount(Account account) {
        return accountDAO.addAccount(account);
    }

    public Account login(Account account) {
        return accountDAO.login(account);
    }

} 
