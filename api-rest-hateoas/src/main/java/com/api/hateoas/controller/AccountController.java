package com.api.hateoas.controller;

import com.api.hateoas.model.Account;
import com.api.hateoas.model.Amount;
import com.api.hateoas.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<List<Account>> getAccounts() {
        List<Account> accounts = accountService.getAll();

        if(accounts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        for(Account account : accounts) {
            account.add(linkTo(methodOn(AccountController.class).getAccount(account.getId())).withSelfRel());
            account.add(linkTo(methodOn(AccountController.class).deposit(account.getId(),null)).withRel("depositos"));
            account.add(linkTo(methodOn(AccountController.class).getAccounts()).withRel(IanaLinkRelations.COLLECTION));
        }

        CollectionModel<Account> modelo = CollectionModel.of(accounts);
        modelo.add(linkTo(methodOn(AccountController.class).getAccounts()).withSelfRel());

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Integer id) {
        try {
            Account account = accountService.get(id);

            account.add(linkTo(methodOn(AccountController.class).getAccount(account.getId())).withSelfRel());
            account.add(linkTo(methodOn(AccountController.class).deposit(account.getId(),null)).withRel("depositos"));
            account.add(linkTo(methodOn(AccountController.class).getAccounts()).withRel(IanaLinkRelations.COLLECTION));

            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch(Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Account> saveAccount(@RequestBody Account account) {
        Account accountBBDD = accountService.save(account);

        accountBBDD.add(linkTo(methodOn(AccountController.class).getAccount(accountBBDD.getId())).withSelfRel());
        accountBBDD.add(linkTo(methodOn(AccountController.class).deposit(accountBBDD.getId(),null)).withRel("depositos"));
        accountBBDD.add(linkTo(methodOn(AccountController.class).getAccounts()).withRel(IanaLinkRelations.COLLECTION));

        return ResponseEntity.created(linkTo(methodOn(AccountController.class).getAccount(accountBBDD.getId())).toUri()).body(accountBBDD);
    }

    @PutMapping
    public ResponseEntity<Account> modifyAccount(@RequestBody Account account) {
        Account accountBBDD = accountService.save(account);

        accountBBDD.add(linkTo(methodOn(AccountController.class).getAccount(accountBBDD.getId())).withSelfRel());
        accountBBDD.add(linkTo(methodOn(AccountController.class).deposit(accountBBDD.getId(),null)).withRel("depositos"));
        accountBBDD.add(linkTo(methodOn(AccountController.class).getAccounts()).withRel(IanaLinkRelations.COLLECTION));

        return new ResponseEntity<>(accountBBDD, HttpStatus.OK);
    }

    @PatchMapping("/{id}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable Integer id, @RequestBody Amount amount) {
        Account accountBBDD = accountService.deposit(amount.getAmount(), id);

        accountBBDD.add(linkTo(methodOn(AccountController.class).getAccount(accountBBDD.getId())).withSelfRel());
        accountBBDD.add(linkTo(methodOn(AccountController.class).deposit(accountBBDD.getId(),null)).withRel("depositos"));
        accountBBDD.add(linkTo(methodOn(AccountController.class).getAccounts()).withRel(IanaLinkRelations.COLLECTION));

        return new ResponseEntity<>(accountBBDD, HttpStatus.OK);
    }

    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable Integer id, @RequestBody Amount amount) {
        Account accountBBDD = accountService.withdraw(amount.getAmount(), id);

        accountBBDD.add(linkTo(methodOn(AccountController.class).getAccount(accountBBDD.getId())).withSelfRel());
        accountBBDD.add(linkTo(methodOn(AccountController.class).deposit(accountBBDD.getId(),null)).withRel("depositos"));
        accountBBDD.add(linkTo(methodOn(AccountController.class).withdraw(accountBBDD.getId(),null)).withRel("retiros"));
        accountBBDD.add(linkTo(methodOn(AccountController.class).getAccounts()).withRel(IanaLinkRelations.COLLECTION));

        return new ResponseEntity<>(accountBBDD, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Integer id) {
        try {
            accountService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

}
