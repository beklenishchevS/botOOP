package com.company.games;
import com.company.interfaces.IGame;

import java.sql.Time;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Random;

public class BullsCows implements IGame {
    private int bulls = 0;
    private int cows = 0;
    private boolean is_incorrect = false;
    public int[] task = new int[4];
    private boolean is_finished = false;
    public Integer gameRequests = 0;
    public String timeOfStart;
    public Boolean is_started = Boolean.FALSE;
    public String type = "BullsCows";


    public BullsCows()
    {
        task = GenerateTask();
    }

    private int[] GenerateTask(){
        Random random = new Random();
        int[] res = new int[4];
        for(int i = 0; i < 4; i++){
            int a = 0;
            loop: while (true)
            {
                a = random.nextInt(10);
                for (int j = 0; j < 4; j++) {
                    if (res[j] == a)
                        continue loop;
                }
                res[i] = a;
                break;
            }
        }
        return res;
    }
    @Override
    public void readMessage(String str) {
        if (!(is_started))
        {
            Date date = new Date();
            timeOfStart = date.toString();
            is_started = Boolean.TRUE;
        }
        gameRequests++;
        if ("/start".equals(str))
        {
            return;
        }
        bulls = 0;
        cows = 0;
        if ("последняя".equals(str))
        {
            return;
        }
        if (str.length() != 4){
            is_incorrect = true;
            return;
        }
        is_incorrect = false;
        int[] answ = new int[4];
        String[] cansw = str.split("");
        for (int i = 0; i < 4; i++){
            answ[i] = Integer.parseInt(cansw[i]);
        }
        for (int i = 0; i < 4; i++){
            boolean c = false;
            for (int j = 0; j < 4; j++){
                if (answ[i] == task[j])
                    c = true;
            }
            if (c){
                if (answ[i] == task[i])
                    bulls++;
                else
                    cows++;
            }
        }
        if (bulls == 4)
            is_finished = true;
    }

    @Override
    public String getMessage() {
        if (is_finished)
            return "Молодец!";
        if (is_incorrect)
            return "Введите корректное значение, число из 4 различных цифр";
        return "~~~~~~~~~~~~~~~~~~~";
    }

    @Override
    public String getQuest() {
        return "Быков: " + bulls + " Коров: " + cows;
    }

    @Override
    public String start() {
        return "Игра Быки и Коровы!\n" +
                "Я загадал число из 4 цифр, угадай его! Быки - угаданные цифры на своих местах,\n" +
                "Коровы - угаданные цифры не на своих местах \n" +
                "Как надоест скажи: хватит\n" +
                "Поехали!";
    }

    @Override
    public String getName() {
        return "Быки и коровы";
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getStats() {
        return "Requests(BullCows): " + gameRequests + " start at:" + timeOfStart + "\n";
    }


    private void PrintAnswer()
    {
        for (int i=0; i<4; i++)
        {
            System.out.print(task[i]);
        }
    }
}
