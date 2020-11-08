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

    @Column(name = "giver")
    private long giver;

    @Column(name = "receiver")
    private long receiver;

    public Gift(long giver, long receiver) {
        this.giver = giver;
        this.receiver = receiver;
    }

    public Gift() {
    }
}
