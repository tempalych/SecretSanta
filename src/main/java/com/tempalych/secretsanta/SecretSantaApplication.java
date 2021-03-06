package com.tempalych.secretsanta;

import com.tempalych.secretsanta.bot.SantaBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@SpringBootApplication()
public class SecretSantaApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();

		SpringApplication.run(SecretSantaApplication.class, args);
	}


}
