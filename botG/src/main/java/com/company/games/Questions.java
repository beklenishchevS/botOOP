package com.company.games;


import com.company.Pair;
import java.util.Random;

public class Questions {


    public Pair<Integer, String> GenerateQuest(){
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
            case(0):
            {
                answer = a + b;
                questText = a + operations[0] + b;
                break;
            }
            case(1):
            {
                answer = a - b;
                questText = a + operations[1] + b;
                break;
            }
            case(2):
            {
                answer = a * b;
                questText = a + operations[2] + b;
                break;
            }
            case (3): {
                answer=b;
                if (a==0) a=1;
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
}
