/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fowami.iouapp.data;

import javax.validation.constraints.NotBlank;

/**
 *
 * @author fowami
 */
public class CreateUser {
   @NotBlank(message = "User name is Required")
    private String user; 
 
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
 
}
