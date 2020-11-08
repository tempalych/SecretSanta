package com.tempalych.secretsanta.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "sessions")
public class Session implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "\"user\"")
    private long user;

    @Column(name = "state")
    private int state;

    @Column(name = "update_time")
    private Date updateTime;

    public Session(long user) {
        this.user = user;
        updateTime = new Date();
    }

    public static enum States {
        NEW_USER,
        REQUESTED_NEW_GROUP_ID,
        COMMON_STATE
    }

    public Session() {

    }

    public void updateState(States state) {
        if (state.equals(States.NEW_USER)) {
            this.setState(0);
        }
        if (state.equals(States.REQUESTED_NEW_GROUP_ID)) {
            this.setState(1);
        }
        if (state.equals(States.COMMON_STATE)) {
            this.setState(2);
        }
    }

    public States readState() {
        if (this.getState() == 0) {
            return States.NEW_USER;
        }
        if (this.getState() == 1) {
            return States.REQUESTED_NEW_GROUP_ID;
        }
        return null;
    }
}
