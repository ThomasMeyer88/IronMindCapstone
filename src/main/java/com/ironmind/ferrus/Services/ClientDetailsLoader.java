package com.ironmind.ferrus.Services;

import com.ironmind.ferrus.model.Client;
import com.ironmind.ferrus.model.ClientWithRoles;
import com.ironmind.ferrus.repositiories.Clients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientDetailsLoader implements UserDetailsService {

    private final Clients clients;

    public ClientDetailsLoader(Clients clients) {
        this.clients = clients;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clients.findByUsername(username);
        if (client == null) {
            throw new UsernameNotFoundException("No user found for " + username);
        }

        return new ClientWithRoles(client);
    }
}
