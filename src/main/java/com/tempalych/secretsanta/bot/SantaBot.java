package com.tempalych.secretsanta.bot;

import com.tempalych.secretsanta.controller.GiftController;
import com.tempalych.secretsanta.controller.SessionController;
import com.tempalych.secretsanta.controller.UserController;
import com.tempalych.secretsanta.domain.Session;
import com.tempalych.secretsanta.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class SantaBot extends TelegramLongPollingBot {

    @Autowired
    UserController userController;

    @Autowired
    GiftController giftController;

    @Autowired
    SessionController sessionController;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            boolean newUser = false;
            boolean userDeleted = false;
            int userTelegramId = update.getMessage().getFrom().getId();
            User user = userController.findByTelegramId(userTelegramId);
            String response = "";
            if (user == null) {
                String username = update.getMessage().getChat().getUserName();
                if (username == "" || username == null) {
                    response = "Для участия вам нужно задать имя пользователя в настройках Telegram.";
                    SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
                    sendMessage.setText(response);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else {
                    user = userController.newUser(update.getMessage().getChat().getUserName(),
                            null,
                            userTelegramId,
                            null,
                            update.getMessage().getChatId());
                    newUser = true;
                }
            }

            String messageText = update.getMessage().getText();
            System.out.println(user.getName() + " пишет \"" + update.getMessage().getText() + "\"");

            Session session = sessionController.getSessionByUserId(user.getId());
            if (newUser) {
                session.updateState(Session.States.NEW_USER);
            }


            if ("У меня есть код группы".equals(messageText)) {
                session.updateState(Session.States.REQUESTED_NEW_GROUP_ID);
                response = "Введите ваш код группы";
            } else if ("👨‍👩‍👧‍👧Участники".equals(messageText)) {
                List<User> users = userController.findByGroup(user.getGroup());
                List<String> emojis = userController.getRandomEmoji(users.size());

                int cnt = 0;
                for (User userOfGroup: users) {
                    response += emojis.get(cnt) + " @" + userOfGroup.getName() + "\n";
                    cnt ++;
                }
            } else if ("🔀Перемешать".equals(messageText) && user.getTelegramId() == 67359) {
                giftController.shuffle(user.getGroup());
                for (User userToSendNotice: userController.findByGroup(user.getGroup())) {
                    SendMessage notice = new SendMessage().setChatId(userToSendNotice.getChatId());
                    notice.setText("Произошла жеребьевка. Вы 🎅🏼 для @" +
                            giftController.getReceiverByGiver(userToSendNotice).getName());
                    try {
                        execute(notice);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            } else if ("🙅🏼‍♀️Я больше не играю".equals(messageText)) {
                User newGiver = giftController.removeOneUser(user);
                if (newGiver == null) {
                    for (User userToSendNotice: userController.findByGroup(user.getGroup())) {
                        SendMessage notice = new SendMessage().setChatId(userToSendNotice.getChatId());
                        notice.setText("Пользователь @" + user.getName() +
                                " больше не с нами и пришлось всё перемешать. Теперь вы 🎅🏼 для @" +
                                giftController.getReceiverByGiver(userToSendNotice).getName());
                        try {
                            execute(notice);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    SendMessage notice = new SendMessage().setChatId(newGiver.getChatId());
                    notice.setText("Пользователь @" + user.getName() +
                            " больше не с нами. Теперь вы 🎅🏼 для @" +
                            giftController.getReceiverByGiver(newGiver).getName());
                    try {
                        execute(notice);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            } else if ("🎁Кому я дарю?".equals(messageText)) {
                User receiver = giftController.getReceiverByGiver(user);
                if (receiver != null) {
                    response = "Вы 🎅🏼 для @" + receiver.getName();
                } else {
                    response += "Похоже, что жеребьевка еще не произошла 🤷🏼‍️";
                }
            }
            else {
                if (session.readState() == Session.States.REQUESTED_NEW_GROUP_ID) {
                    userController.updateUserGroup(user, messageText);
                    session.updateState(Session.States.COMMON_STATE);
                }
            }
            sessionController.updateSession(session);



            SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
            if (session.readState() == Session.States.NEW_USER) {
                sendMessage.setReplyMarkup(Keyboard.getKeyboardForNewUser());
            } else if (session.readState() == Session.States.REQUESTED_NEW_GROUP_ID) {
                sendMessage.setReplyMarkup(Keyboard.getKeyboardBlank());
            } else if (userDeleted) {
                sendMessage.setReplyMarkup(Keyboard.getKeyboardBlank());
            }
            else {
                sendMessage.setReplyMarkup(Keyboard.getKeyboardCommon(user.getTelegramId() == 67359));
            }
            if (response == "") {
                response = "🎅🏼";
            }
            sendMessage.setText(response);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
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
