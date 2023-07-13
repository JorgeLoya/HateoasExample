package com.api.hateoas;

import com.api.hateoas.model.Account;
import com.api.hateoas.repository.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@Rollback(value = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testAgregarCuenta() {
        Account account = new Account(123456, "2323");
        Account accountGuardada = accountRepository.save(account);

        Assertions.assertThat(accountGuardada).isNotNull();
        Assertions.assertThat(accountGuardada.getId()).isGreaterThan(0);
    }

}
