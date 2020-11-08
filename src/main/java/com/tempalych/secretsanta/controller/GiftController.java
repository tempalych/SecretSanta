package com.tempalych.secretsanta.controller;

import com.tempalych.secretsanta.domain.Gift;
import com.tempalych.secretsanta.domain.User;
import com.tempalych.secretsanta.repository.GiftRepository;
import com.tempalych.secretsanta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Random;

@Controller
public class GiftController {

    @Autowired
    GiftRepository giftRepository;

    @Autowired
    UserRepository userRepository;

    public Gift newGift(User giver, User receiver) {
        System.out.println("newGift with params: " +
                "giver = " + giver.getName() +
                " receiver = " + receiver.getName());
        Gift newGift = giftRepository.save(new Gift(giver.getId(), receiver.getId()));
        return newGift;
    }

    public User getReceiverByGiver(User giver) {
        Gift gift = giftRepository.getByGiver(giver.getId());
        if (gift != null) {
            return userRepository.findById(giftRepository.getByGiver(giver.getId()).getReceiver());
        }
        else {
            return null;
        }
    }

    public User removeOneUser(User lisa) {
        Gift giftWhereLisaIsGiver = giftRepository.getByGiver(lisa.getId());
        Gift giftWhereLisaIsReceiver = giftRepository.getByReceiver(lisa.getId());
        userRepository.delete(lisa);
        if (giftWhereLisaIsGiver.getReceiver() == giftWhereLisaIsReceiver.getGiver()) {
            // Значит ушедшая Лиза и ее даритель закольцованы - требуется полная перетряска
            shuffle(lisa.getGroup());
            return null;
        } else {
            // требуется всего лишь закольцевать получателя от Лизы с дарителем Лизе
            giftWhereLisaIsReceiver.setReceiver(giftWhereLisaIsGiver.getReceiver());
            giftRepository.delete(giftWhereLisaIsGiver);
            giftRepository.save(giftWhereLisaIsReceiver);
            return userRepository.findById(giftWhereLisaIsReceiver.getGiver());
        }
    }

    public String shuffle(String group){
        giftRepository.deleteAll();
        List<User> users = userRepository.findByGroup(group);
        int[] indexes = new int[users.size()];

        for(int i = 0; i < users.size(); i++){
            indexes[i] = i;
        }
        shuffleArray(indexes);
        String result = "";
        for(int i = 0; i < indexes.length; i++){
            User user1 = users.get(indexes[i]);
            User user2;
            if (i == indexes.length - 1){
                user2 = users.get(indexes[0]);
            } else {
                user2 = users.get(indexes[i + 1]);
            }

            newGift(user1, user2);

            result += "@" + user1.getName() + " дарит пользователю @" + user2.getName() + ";\n";
        }
        return result;
    }

    static void shuffleArray(int[] arr){
        Random rnd = new Random();
        for (int i = arr.length - 1; i > 0; i--){
            int index = rnd.nextInt(i + 1);
            int a = arr[index];
            arr[index] = arr[i];
            arr[i] = a;
        }
    }

}
