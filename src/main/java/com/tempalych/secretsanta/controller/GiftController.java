package com.tempalych.secretsanta.controller;

import com.tempalych.secretsanta.domain.Gift;
import com.tempalych.secretsanta.domain.User;
import com.tempalych.secretsanta.repository.GiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GiftController {

    @Autowired
    GiftRepository repo;

    public Gift newGift(String userGives, String userTakes){
        System.out.println("newGift with params: "+
                "userGives = " + userGives +
                " userTakes = " + userTakes);
        Gift newGift = repo.save(new Gift(userGives, userTakes));
        return newGift;
    }

    public String getGifter(String user){
        for (Gift gift:repo.findAll()) {
            if (gift.getUserGives().equals(user)){
                return gift.getUserTakes();
            }
        }
        return null;
    }
}
