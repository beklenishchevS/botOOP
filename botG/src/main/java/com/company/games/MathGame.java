package com.company.games;

import com.company.Pair;
import com.company.interfaces.IGame;


import java.util.Date;
import java.util.Random;


public class MathGame implements IGame
{
    public String out;
    public String quest;
    public int res;
    public Integer gameRequests = 0;
    public String timeOfStart;
    public Boolean is_started = Boolean.FALSE;
    public String type = "MathGame";

    public String start()
    {
        Pair<Integer, String> pair = GenerateQuest();
        quest = pair.getValue();
        res = pair.getKey();
        return "Привет, я пришу тебе задачку, а ты пишешь мне ответ.\n"+
        " Как надоест скажи: \" хватит \"";
    }

    public String getName()
    {
        return "Математика";
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getStats() {
        return "Requests(MathGame): " + gameRequests + " start at:" + timeOfStart + "\n";
    }

    private Pair<Integer, String> GenerateQuest()
    {
        Random random = new Random();
        String[] operations = new String[4];
        operations[0] = "+";
        operations[1] = "-";
        operations[2] = "*";
        operations[3] = "/";
        int a = random.nextInt(50);
        int b = random.nextInt(50);
        int operation = random.nextInt(5);
        int answer = 0;
        String questText = "";
        switch (operation)
        {
            case (0):
            {
                answer = a + b;
                questText = a + operations[0] + b;
                break;
            }
            case (1):
            {
                answer = a - b;
                questText = a + operations[1] + b;
                break;
            }
            case (2):
            {
                answer = a * b;
                questText = a + operations[2] + b;
                break;
            }
            case (3):
            {
                answer = b;
                if (a == 0) a = 1;
                questText = a * b + operations[3] + a;
                break;
            }
            case (4):
            {
                answer = a + a * b - b * b;
                questText = a + operations[0] + a + operations[2] + b + operations[1] + b + operations[2] + b;
            }
        }
        return new Pair<Integer, String>(answer, questText);
    }

    @Override
    public void readMessage(String str)
    {
        if (!(is_started))
        {
            Date date = new Date();
            timeOfStart = date.toString();
            is_started = Boolean.TRUE;
        }
        gameRequests++;
        out = yMessage(str, res);
        Pair<Integer, String> pair = GenerateQuest();
        res = pair.getKey();
        quest = pair.getValue();
    }

    @Override
    public String getMessage()
    {
        return out;
    }

    public String getQuest()
    {
        return quest;
    }

    private String yMessage(String userAnsw, Integer result)
    {
        if (userAnsw.equals(Integer.toString(result)))
        {
            return "Правильно!";
        }
        if (userAnsw.equals("хватит"))
        {
            return null;
        }
        if (userAnsw.equals("\\help"))
        {
            return "Привет, я пришу тебе задачку, а ты пишешь мне ответ." +
                    " Как надоест скажи: \" хватит \", а напомить правила можно командой \\help";

        }
        return "Нет, правильный ответ " + result;
    }
}
