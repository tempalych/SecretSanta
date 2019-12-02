package com.tempalych.secretsanta.bot;

import com.tempalych.secretsanta.controller.GiftController;
import com.tempalych.secretsanta.controller.UserController;
import com.tempalych.secretsanta.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

@Component
public class SantaBot extends TelegramLongPollingBot {

    private long chatId;
    private String username;

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    @Autowired
    UserController userController;

    @Autowired
    GiftController giftController;

    @Override
    public void onUpdateReceived(Update update) {
        update.getUpdateId();
        chatId = update.getMessage().getChatId();
        username = update.getMessage().getChat().getUserName();
        if (username == null) {
            username = update.getMessage().getChat().getFirstName()+update.getMessage().getChat().getLastName();
        }

        String request = update.getMessage().getText();
        String response = "";

        if ("Участники".equals(request)) {
            for (User user: userController.getUserList()) {
                response += "@" + user.getName() + "\n";
            }
            if (response == "") {
                response = "Участников пока нет";
            }
        }

        if ("Перемешать".equals(request)){
            String res = userController.shuffle();
            response = "Перемешано: \n" + res;
        }

        if ("Я в деле".equals(request)){
            User user = userController.findByName(username);
            if (user == null){
                user = userController.newUser(username, "", "");
                response = "Отлично, вы добавлены, " + user.getName();
            } else {
                response = "Отлично, мы про вас помним, " + user.getName();
            }
            String gifter = userController.getGifter(username);
            if (gifter != null) {
                response += ". Вы - Санта для @" + gifter;
            } else {
                response += ". Ждите жеребьёвки.";
            }
        }

        if ("/start".equals(request)){
            response = "Привет, " + username;
        }

        if ("Кому я дарю?".equals(request)){
            String gifter = userController.getGifter(username);
            if (gifter != null) {
                response = "Вы - Санта для @" + gifter;
            } else {
                response = "Ждите жеребьёвки";
            }
        }


        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());

        getUserInfoButtons(username);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        sendMessage.setText(response);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    void getUserInfoButtons(String username) {
        ArrayList keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        keyboard.clear();
        keyboardFirstRow.clear();
        keyboardSecondRow.clear();
        keyboardFirstRow.add("Я в деле");
        keyboardFirstRow.add("Участники");
        keyboardSecondRow.add("Кому я дарю?");
        if ("artemmalov".equals(username)) {
            keyboardSecondRow.add("Перемешать");
        }

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    @Override
    public String getBotUsername() {
        return "SecretSantaEbobot";
    }

    @Override
    public String getBotToken() {
        return "1020557033:AAEbsFIDLiNXT0b_kA-UFnUMKyK30qB17wA";
    }
}
