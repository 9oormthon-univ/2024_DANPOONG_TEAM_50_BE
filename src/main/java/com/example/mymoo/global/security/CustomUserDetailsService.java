package com.example.mymoo.global.security;

import com.example.mymoo.domain.account.entity.Account;
import com.example.mymoo.domain.account.repository.AccountRepository;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return CustomUserDetails.builder()
            .accountId(account.getId())
            .email(account.getEmail())
            .password(account.getPassword())
            .authorities(
                Collections.singletonList(
                    new SimpleGrantedAuthority(
                        account.getRole().getAuthority() // "ADMIN"|"STORE"|"CHILD"|"DONATOR"
                    )
                )
            )
            .build();
    }
}
