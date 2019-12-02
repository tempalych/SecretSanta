package com.tempalych.secretsanta.controller;

import com.tempalych.secretsanta.domain.Gift;
import com.tempalych.secretsanta.domain.User;
import com.tempalych.secretsanta.repository.GiftRepository;
import com.tempalych.secretsanta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class UserController {
    @Autowired
    UserRepository repo;

    @Autowired
    GiftController giftController;

    public User findByName(String name){
        for (User user:repo.findAll()) {
            if (user.getName().equals(name)){
                return user;
            }
        }
        return null;
    }

    public User newUser(String name, String phone, String preferences){
        System.out.println("newUser with params: "+
                "name = " + name +
                " phone = " + phone +
                " preferences = " + preferences);
        User newUser = repo.save(new User(name, phone, preferences));
        return newUser;
    }

    public List<String> findAll(){
        List<String> userUI = new ArrayList<>();
        for (User user:repo.findAll()
             ) {
            userUI.add(user.getName());
        }
        return userUI;
    }

    public List<User> getUserList(){
        System.out.println("getUserListController");
        return repo.findAll();
    }

    public String shuffle(){
        giftController.repo.deleteAll();
        List<User> users = repo.findAll();
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

            giftController.newGift(user1.getName(), user2.getName());

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

    public String getGifter(String username){
        return giftController.getGifter(username);
    }


}
