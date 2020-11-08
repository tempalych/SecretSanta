package com.tempalych.secretsanta.controller;

import com.tempalych.secretsanta.domain.User;
import com.tempalych.secretsanta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    public User newUser(String name, String preferences, int telegramId, String group, long chatId){
        System.out.println("newUser with params: "+
                "name = " + name +
                " preferences = " + preferences +
                " group = " + group +
                "chatId = " + chatId);
        User newUser = userRepository.save(new User(name, preferences, telegramId, group, chatId));
        return newUser;
    }

    public User findByTelegramId(int telegramId) {
        return userRepository.findByTelegramId(telegramId);
    }

    public List<User> findByGroup(String group) {
        return userRepository.findByGroup(group);
    }

    public void updateUserGroup(User user, String group) {
        user.setGroup(group);
        userRepository.save(user);
    }

    public User findById(long id) {
        return userRepository.findById(id);
    }

    public List<String> getRandomEmoji(int count) {
        List<String> result = new ArrayList<>();
        List<String> emojis = new ArrayList<>();
        emojis.add("🐶");
        emojis.add("🐱");
        emojis.add("🐸");
        emojis.add("🐔");
        emojis.add("🐠");
        emojis.add("🦊");
        emojis.add("🐭");
        emojis.add("🐰");
        emojis.add("🐻");
        emojis.add("🐼");
        emojis.add("🐵");
        emojis.add("🐷");
        emojis.add("🐮");
        emojis.add("🐯");
        emojis.add("🐨");
        emojis.add("🐤");

        int cnt = 0;
        while (cnt < count) {
            Random rnd = new Random();
            int index = rnd.nextInt(emojis.size() - 1);
            result.add(emojis.get(index));
            emojis.remove(index);
            cnt ++;
        }
        return result;
    }


//    public String getGifter(String username){
//        return giftController.getGifter(username);
//    }


}
