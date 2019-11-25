package com.tempalych.secretsanta.controller;

import com.tempalych.secretsanta.domain.User;
import com.tempalych.secretsanta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController()
public class UserController {
    @Autowired
    UserRepository repo;

    @PostMapping("new")
    public String newUser(@RequestParam String name,
                          @RequestParam String phone,
                          @RequestParam String preferences){
        System.out.println("newUser with params: "+
                "name = " + name +
                "phone = " + phone +
                "preferences = " + preferences);
        repo.save(new User(name, phone, preferences));
        return "Пользователь создан: " + name;
    }

    @GetMapping("all")
    public List<String> findAll(){
        List<String> userUI = new ArrayList<>();
        for (User user:repo.findAll()
             ) {
            userUI.add(user.getName());
        }
        return userUI;
    }

    @RequestMapping("/")
    public String getUserList(){
        System.out.println("getUserListController");
        return "Привет из сахарницы";
    }

    @GetMapping("shuffle")
    public String shuffle(){
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
            result += user1.getName() +
                    " дарит пользователю " + user2.getName() +
                    ", который любит " + user2.getPreferences() + ";\n";
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
