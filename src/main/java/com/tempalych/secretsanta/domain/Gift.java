package com.tempalych.secretsanta.domain;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "gifts")
public class Gift implements Serializable{
    private static final long serialVersionUID = 5832063776451490808L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "usergives")
    private String userGives;

    @Column(name = "usertakes")
    private String userTakes;

    public Gift(){}

    public Gift(String userGives, String userTakes) {
        this.userGives = userGives;
        this.userTakes = userTakes;
    }
}
