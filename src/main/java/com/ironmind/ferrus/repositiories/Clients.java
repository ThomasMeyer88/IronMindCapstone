package com.ironmind.ferrus.repositiories;


import com.ironmind.ferrus.model.Client;
import org.springframework.data.repository.CrudRepository;

public interface Clients extends CrudRepository<Client, Long> {
    Client findByUsername(String username);

}
