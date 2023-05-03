package com.bao;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AnotherMain {
    int i;

    AnotherMain(int ii) {
        i = ii;
    }

    public static void main(String[] args) {

//        AnotherMain x = new AnotherMain(7);
//        AnotherMain y = x;
//        System.out.println("x: "+ x.i);
//        System.out.println("y: "+ y.i);
//        System.out.println("i+++++");
//        x.i++;
//        System.out.println("x after: "+ x.i);
//        System.out.println("y after: "+ y.i);

        ////

//        Scanner scanner = new Scanner(System.in);
//
//        System.out.print("Enter the first string: ");
//        String firstString = scanner.nextLine();
//
//        System.out.print("Enter the second string: ");
//        String secondString = scanner.nextLine();
//
//        System.out.println("result same vowels pattern: " + sameVowelsPatternLongWord(firstString, secondString));
    }

    public static boolean sameVowelsPatternLongWord(String s1, String s2) {
        return getVowels(s1).equals(getVowels(s2));
    }

    private static List<Character> getVowels(String s) {
        List<Character> returnList = new ArrayList<>();
        char[] charArr = s.toUpperCase().toCharArray();
        for (int i = 0; i < s.length(); i++) {
            char c = charArr[i];
            if (c == 'A'
                    || c == 'E'
                    || c == 'I'
                    || c == 'O'
                    || c == 'U') {
                returnList.add(c);
            }
        }
        return returnList;

    }
}
