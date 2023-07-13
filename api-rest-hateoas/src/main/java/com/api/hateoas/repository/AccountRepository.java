package com.api.hateoas.repository;

import com.api.hateoas.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query("UPDATE Account c SET c.amount=c.amount + ?1 WHERE c.id=?2")
    @Modifying
    void updateAmount(float amount, Integer id);

}
