package com.company.games;

import com.company.interfaces.IGame;

import java.util.Date;
import java.util.HashSet;

public class Goroda implements IGame
{
    private String[] cities = new String[]
            {"Амстердам",
            "москва",
            "анадырь",
            "ростов",
            "волгоград",
            "донецк"
            };
    public HashSet<String> usedCities = new HashSet<>();
    public String currentCity;
    public String msg;
    public String lastLetter;
    public Integer gameRequests = 0;
    public String timeOfStart;
    public Boolean is_started = Boolean.FALSE;
    public String type = "Goroda";

    public String start()
    {
        updateCurrentCity("Амстердам");
        msg = "~";
        return "Начнем играть в города!\n" +
                "Я называю город, ты называешь город на последнюю букву моего и так далее...\n" +
                "Как надоест скажи: хватит\n" +
                "Поехали!";
    }

    public String getName()
    {
        return "Города";
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getStats() {
        return "Requests(Cities): " + gameRequests + " start at:" + timeOfStart + "\n";
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
            msg = "Начнем";
            return;
        }
        if (!contains(str))
        {
            msg = "Я не знаю такого города";
            return;
        }
        if (!str.substring(0,1).equals(lastLetter))
        {
            msg = "Вы не правы, вам нужен город на букву " + lastLetter;
            return;
        }
        if (usedCities.contains(str))
        {
            msg = "Этот город уже был";
            return;
        }
        updateCurrentCity(str);
        msg = "Город ОК, далее:";
    }

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public String getQuest()
    {
        return currentCity;
    }

    private boolean contains(String value)
    {
        for (String s : cities)
        {
            if (s.equals(value))
            {
                return true;
            }
        }
        return false;
    }

    private String find(String value)
    {
        for (String city : cities)
        {
            if (city.startsWith(getLastChar(value)) && !usedCities.contains(city))
            {
                return city;
            }
        }
        return null;
    }

    private void updateCurrentCity(String city)
    {
        usedCities.add(city);
        currentCity = find(city);
        lastLetter = getLastChar(currentCity);
    }

    private String getLastChar(String str)
    {
        int l = str.length();
        String lastChar = str.substring(l - 1);
        if (lastChar.equals("ь") || lastChar.equals("ы"))
        {
            lastChar = str.substring(l - 2, l - 1);
        }
        return lastChar;
    }
}
