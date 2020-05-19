/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fowami.iouapp.controller;

import com.fowami.iouapp.data.CreateIou;
import com.fowami.iouapp.data.CreateUser;
import com.fowami.iouapp.data.IouData;
import com.fowami.iouapp.data.UserData;
import com.fowami.iouapp.data.Users;
import com.fowami.iouapp.domain.Iou;
import com.fowami.iouapp.domain.IouRepository;
import com.fowami.iouapp.domain.User;
import com.fowami.iouapp.domain.UserRepository;
import com.fowami.iouapp.exception.BadRequestException;
import com.fowami.iouapp.exception.UserNotFoundException;
import io.swagger.annotations.Api;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author fowami
 */
@Api
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;
    private final IouRepository iouRepository;

    public UserController(UserRepository userRepository, IouRepository iouRepository) {
        this.userRepository = userRepository;
        this.iouRepository = iouRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        List<User> list = userRepository.findAll();
        Users users = new Users();
//       {"users":["Adam","Bob"]} 
        list.forEach(x -> {
            users.getUsers().add(x.getName());
        });

        return ResponseEntity.ok(users);
    }

    @PostMapping("/add")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUser user) {

        if (userRepository.findByName(user.getUser()).isPresent()) {
            throw new BadRequestException("User with name " + user.getUser() + " already exists");
        }
        User newUser = new User(user.getUser());
        User savedUser = userRepository.save(newUser);

        return ResponseEntity.ok(toUserData(savedUser));
    }

    @PostMapping("/iou")
    public ResponseEntity<?> createIOU(@Valid @RequestBody CreateIou iou) {

        User lender = userRepository.findByName(iou.getLender())
                .orElseThrow(() -> new UserNotFoundException("Lender with name " + iou.getLender() + " Not Found"));
        User borrower = userRepository.findByName(iou.getBorrower())
                .orElseThrow(() -> new UserNotFoundException("Lender with name " + iou.getBorrower() + " Not Found"));

        Iou lendIo = new Iou(Iou.Type.lend, iou.getAmount());
        lendIo.setIouUser(borrower.getName());
        lender.addIou(lendIo);
        
        

        Iou borrowIo = new Iou(Iou.Type.borrow, iou.getAmount());
        borrowIo.setIouUser(lender.getName());
        borrower.addIou(borrowIo);

        List<User> users = userRepository.saveAll(Arrays.asList(lender, borrower));

        return ResponseEntity.ok(
                users.stream()
                        .map(x -> toUserData(x))
                        .collect(Collectors.toList())
        );
    }

    private UserData toUserData(User user) {
        UserData data = new UserData();
        data.setName(user.getName());
        //
        data.setOwes(user.getIous()
                .stream()
                .filter(x -> x.getType() == Iou.Type.lend)
                .map(x -> new IouData(x.getIouUser(), x.getAmount()))
                .collect(Collectors.toList())
        );
        data.setOwedBy(user.getIous()
                .stream()
                .filter(x -> x.getType() == Iou.Type.borrow)
                .map(x -> new IouData(x.getIouUser(), x.getAmount()))
                .collect(Collectors.toList())
        );
        data.updateBalance();
        return data;
    }

    private User findUser(String name) {
        return userRepository.findByName(name).orElseThrow(() -> new UserNotFoundException("User " + name + " Not Found"));
    }

}
