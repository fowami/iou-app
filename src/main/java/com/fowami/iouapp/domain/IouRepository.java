/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fowami.iouapp.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author fowami
 */
public interface IouRepository extends JpaRepository<Iou, Long> {

}
