package com.si.broker.security;

import com.si.broker.model.Provider;
import com.si.broker.model.Role;
import com.si.broker.model.User;
import com.si.broker.repositories.IProviderRepository;
import com.si.broker.repositories.IUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final IUserRepository customerRepository;
    private final IProviderRepository providerRepository;

    public MyUserDetailsService(IUserRepository customerRepository, IProviderRepository providerRepository) {
        this.customerRepository = customerRepository;
        this.providerRepository = providerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = customerRepository.findByUsername(s);
        Provider provider = providerRepository.findByUsername(s);

        if (user == null && provider == null)
            throw new UsernameNotFoundException(s);
        else if (user != null && provider == null) { //ako je korisnik
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getUserAuthorities(user));
        } else if (user == null && provider != null) { //ako je provider
            return new org.springframework.security.core.userdetails.User(provider.getUsername(), provider.getPassword(), getProviderAuthorities(provider));
        }

        return null;
    }

    private Collection<? extends GrantedAuthority> getUserAuthorities(User user) {

        List<SimpleGrantedAuthority> authoritiesForCustomer = new ArrayList<>();

        for (Role r : user.getRoles()) {
            authoritiesForCustomer.add(new SimpleGrantedAuthority("ROLE_" + r.getName()));
        }
        return authoritiesForCustomer;
    }

    private Collection<? extends GrantedAuthority> getProviderAuthorities(Provider provider) {

        List<SimpleGrantedAuthority> authoritiesForProvider = new ArrayList<>();
        authoritiesForProvider.add(new SimpleGrantedAuthority("ROLE_PROVIDER"));

        return authoritiesForProvider;
    }
}