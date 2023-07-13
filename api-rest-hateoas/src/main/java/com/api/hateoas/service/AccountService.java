package com.api.hateoas.service;

import com.api.hateoas.exception.AccountNotFoundException;
import com.api.hateoas.model.Account;
import com.api.hateoas.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account get(Integer id) {
        return accountRepository.findById(id).get();
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public void delete(Integer id) throws AccountNotFoundException {
        if(accountRepository.existsById(id)) {
            throw new AccountNotFoundException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }

    public Account deposit(float amount, Integer id) {
        accountRepository.updateAmount(amount, id);
        return accountRepository.findById(id).get();
    }

    public Account withdraw(float amount, Integer id) {
        accountRepository.updateAmount(-amount, id);
        return accountRepository.findById(id).get();
    }

}
