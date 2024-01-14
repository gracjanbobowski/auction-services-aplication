package com.example.auctionservicesaplication.model;

import jakarta.persistence.*;
import lombok.Getter;
@Getter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private  String name;


}
