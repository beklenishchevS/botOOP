package com.company;

import com.alibaba.fastjson.JSON;
import com.company.games.BullsCows;
import com.company.games.Goroda;
import com.company.games.MathGame;
import com.company.interfaces.IGame;

import javax.security.auth.login.CredentialException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Bot
{
    private IGame game;
    private IGame[] games = new IGame[]{new MathGame(), new BullsCows(), new Goroda()};
    private Memory memory = new Memory();
    private String answer;
    private String user = null;
    private boolean userChanged = false;
    private boolean waitName = false;
    private Map<String, Type> types = new HashMap<>();

    public Bot()
    {
        types.put("MathGame", MathGame.class);
        types.put("BullsCows", BullsCows.class);
        types.put("Goroda", Goroda.class);
    }


    private IGame createGame(String user)
    {
        try(FileReader reader = new FileReader("/Users/BeklenishevaT/Desktop/bot2/src/main/java/backup/backup.txt"))
        {
            Map<String, String> nameGamePair = new HashMap<>();
            StringBuilder s = new StringBuilder();
            int c;
            while((c=reader.read())!=-1){

                s.append((char)c);
            }
            String[] lines = s.toString().split("\n");
            try {
                int i = 0;
                while (true)
                {
                    if (nameGamePair.get(lines[i+1]) == null)
                        nameGamePair.put(lines[i+1], lines[i]);
                    else
                    {
                        nameGamePair.replace(lines[i+1], lines[i]);
                    }
                    i+=2;
                }
            } catch (Exception ignored)
            {

            }
            String jsonStr = nameGamePair.get(user);
            return JSON.parseObject(jsonStr, getType(user));
        }
        catch(Exception ex){
            return null;
        }
    }

    private boolean tryParseInt(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    public String getAnswer()
    {
        return answer;
    }

    private Integer getNumber(String userChoice)
    {
        if (!(tryParseInt(userChoice) && (Integer.parseInt(userChoice) < games.length)))
        {
            answer = "Введите корректное значение";
            return null;
        }
        return Integer.parseInt(userChoice);
    }

    public void communicate(String msg)
    {
        String text = msg.toLowerCase();
        if ("/start".equals(text))
        {
            answer = "Представься:";
            waitName = true;
            return;
        }
        if (waitName)
        {
            user = text;
            game = createGame(text);
            waitName = false;
            if (game == null)
            {
                sendWelcomeMsg();
                return;
            }
        }
        if ("стат".equals(text))
        {
            answer = getStat();
            return;
        }
        if ("новая".equals(text))
        {
            memory.saveLastGame(game);
            game = null;
            sendWelcomeMsg();
            return;
        }
        if ("последняя".equals(text))
        {
            IGame gameNow = memory.getLastGame();
            memory.saveLastGame(game);
            game = gameNow;
        }
        if ("смена".equals(text))
        {
            game = null;
            communicate("/start");
            return;
        }
        if (game == null)
        {
            sendWelcomeMsg();
            Integer index = getNumber(text);
            if (index != null)
            {
                game = games[index];
                writeType(game.getType());
                start();
                setQuest();
            }
        } else {
            progressInput(text);
            setQuest();
        }
    }

    private void sendWelcomeMsg()
    {
        StringBuilder output = new StringBuilder();
        output.append("Выберите игру:\n");
        for (int i=0; i<games.length; i++)
        {
            output.append(games[i].getName() + "-" + i + "\n");
        }
        answer = output.toString();
    }

    private void start()
    {
        StringBuilder output = new StringBuilder();
        output.append("Если захочешь сменить игру, скажи: новая\n");
        output.append("Если захочешь вернуться к последней игре: последняя\n");
        output.append(game.start());
        answer = output.toString();
    }

    private void progressInput(String text) {
        game.readMessage(text);
        String mess = game.getMessage();
        if (mess == null)
            return;
        answer = mess;
    }

    private void setQuest(){
        String quest = game.getQuest();
        if (quest == null) {
            answer = "Игра окончена";
        } else {
            answer = answer+"\n"+quest;
        }
        saveGame();
    }

    private void saveGame()
    {
        String json = JSON.toJSONString(game);
        try(FileWriter writer = new FileWriter("/Users/BeklenishevaT/Desktop/bot2/src/main/java/backup/backup.txt", true))
        {
            String text = json;
            writer.write(text);
            writer.write("\n");
            try {
                writer.write(user + "\n");
            }
            catch (NullPointerException ex)
            {
                writer.write("null");
            }

            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        try(FileWriter writer = new FileWriter("/Users/BeklenishevaT/Desktop/bot2/src/main/java/backup/stat.txt", true))
        {
            writer.write(user + "\n");
            writer.write(game.getStats());
            writer.flush();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    private void writeType23(String type)
    {
        try(FileWriter writer = new FileWriter("/Users/BeklenishevaT/Desktop/bot2/src/main/java/com/company/TypeBox.java", false))
        {
            String text = "package com.company;\n" +
                    "\n" +
                    "import com.company.games.BullsCows;\n" +
                    "import com.company.games.Goroda;\n" +
                    "import com.company.games.MathGame;\n" +
                    "import java.lang.reflect.Type;\n\n" +
                    "public class TypeBox {\n" +
                    "    public static Class type = "+ type +".class;\n" +
                    "}";
            writer.write(text);
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private String getStat()
    {
        try
        {
            FileReader reader = new FileReader("/Users/BeklenishevaT/Desktop/bot2/src/main/java/backup/stat.txt");
            Map<String, List<String>> lines = new HashMap<>();
            Map<String, Pair<String, Integer>> statsLines = new HashMap<>();
            StringBuilder s = new StringBuilder();
            int c;
            int counter = 0;
            String name = null;
            while((c=reader.read())!=-1){
                if (c == '\n')
                {
                    if (counter % 2 == 0)
                    {
                        name = s.toString();
                        if (lines.get(name) == null)
                            lines.put(name, new ArrayList<>());
                    }
                    else
                    {
                        lines.get(name).add(s.toString());
                    }
                    counter++;
                    s = new StringBuilder();
                }
                else
                {
                    s.append((char)c);
                }
            }
            List<String> userStat = lines.get(user);
            for(String line: userStat)
            {
                String[] tokens = line.split(" ");
                try {
                    if (statsLines.get(tokens[6]) == null)
                    {
                        statsLines.put(tokens[6], new Pair<>(line, Integer.parseInt(tokens[1])));
                    }
                    else
                    {
                        if (Integer.parseInt(tokens[1]) > statsLines.get(tokens[6]).getValue())
                        {
                            statsLines.replace(tokens[6], new Pair<>(line, Integer.parseInt(tokens[1])));
                        }
                    }
                } catch (Exception ignored)
                { }
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (Pair<String, Integer> line: statsLines.values())
            {
                stringBuilder.append(line.getKey());
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }
        catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    private void writeType(String type)
    {
        try(FileWriter writer = new FileWriter("/Users/BeklenishevaT/Desktop/bot2/src/main/java/backup/types.txt", true))
        {
            writer.write(type);
            writer.write("\n");
            try {
                writer.write(user + "\n");
            }
            catch (NullPointerException ex)
            {
                writer.write("null");
            }
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private Type getType(String user)
    {
        try(FileReader reader = new FileReader("/Users/BeklenishevaT/Desktop/bot2/src/main/java/backup/types.txt"))
        {
            Map<String, String> nameGamePair = new HashMap<>();
            StringBuilder s = new StringBuilder();
            int c;
            while((c=reader.read())!=-1){

                s.append((char)c);
            }
            String[] lines = s.toString().split("\n");
            for (int i=0; i < lines.length/2; i++)
            {
                if (nameGamePair.get(lines[i+1]) == null)
                    nameGamePair.put(lines[i+1], lines[i]);
                else
                {
                    nameGamePair.replace(lines[i+1], lines[i]);
                }
            }
            String jsonStr = nameGamePair.get(user);
            return types.get(jsonStr);
        }
        catch(Exception ex){
            return null;
        }
    }
}
