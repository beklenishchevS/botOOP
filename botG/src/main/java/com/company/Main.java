package com.company;


import com.company.interfaces.ICommunicationType;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Scanner;

public class Main {

    public static void main(String[] args)
    {
        System.out.println("Введите желаемый тип общения 't'-Telegram, любой другой ответ - Консоль");
        Scanner input = new Scanner(System.in);
        String type = input.nextLine();
        if ("t".equals(type))
            start(new Telega());
        else
            start(new Console());
    }
    static {
        ApiContextInitializer.init();
    }

    private static void start(ICommunicationType communicationType)
    {
        if (communicationType instanceof Telega)
        {
            Telega telega = (Telega)communicationType;
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            try {
                telegramBotsApi.registerBot(telega);
            } catch (TelegramApiRequestException e) {
                e.printStackTrace();
            }
        }
    }

}
