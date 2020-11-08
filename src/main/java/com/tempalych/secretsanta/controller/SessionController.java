package com.tempalych.secretsanta.controller;

import com.tempalych.secretsanta.domain.Session;
import com.tempalych.secretsanta.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SessionController {

    @Autowired
    SessionRepository sessionRepository;

    public Session getSessionByUserId(long userId) {
        for (Session session:sessionRepository.findAll()) {
            if (session.getUser() == userId) {
                return session;
            }
        }
        return sessionRepository.save(new Session(userId));
    }

    public void updateSession(Session session) {
        sessionRepository.save(session);
    }
}
