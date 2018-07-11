package com.ironmind.ferrus.repositiories;


import com.ironmind.ferrus.model.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Clients extends CrudRepository<Client, Long> {
    Client findByUsername(String username);
    List<Client> findAll();
    List<Client> findAllByCoachId(Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM users LIMIT 1")
    Client first();

}
