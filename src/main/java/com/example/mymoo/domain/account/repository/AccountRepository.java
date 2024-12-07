package com.example.mymoo.domain.account.repository;

import com.example.mymoo.domain.account.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface AccountRepository extends JpaRepository<Account, Long>, CustomAccountRepository{
    Optional<Account> findByEmail(String email);
    Boolean existsByEmail(String email);
}
