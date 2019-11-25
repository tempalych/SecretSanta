package com.tempalych.secretsanta.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = -2343243243242432341L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "preferences")
    private String preferences;

    public User(){};

    public User(String name, String phone, String preferences) {
        this.name = name;
        this.phone = phone;
        this.preferences = preferences;
    }


}
