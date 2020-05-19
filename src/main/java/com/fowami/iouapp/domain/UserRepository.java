/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fowami.iouapp.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author fowami
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);
}
