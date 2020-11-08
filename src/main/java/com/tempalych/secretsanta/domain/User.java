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

    @Column(name = "telegram_id")
    private int telegramId;

    @Column(name = "name")
    private String name;

    @Column(name = "preferences")
    private String preferences;

    @Column(name = "\"group\"")
    private String group;

    @Column(name = "chat_id")
    private long chatId;

    public User(){}

    public User(String name, String preferences, int telegramId, String group, long chatId) {
        this.name = name;
        this.preferences = preferences;
        this.telegramId = telegramId;
        this.group = group;
        this.chatId = chatId;
    }
}