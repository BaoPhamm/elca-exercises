package com.bao;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AnotherMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the first string: ");
        String firstString = scanner.nextLine();

        System.out.print("Enter the second string: ");
        String secondString = scanner.nextLine();

        System.out.println("result same vowels pattern: "+sameVowelsPatternLongWord(firstString, secondString));
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
                    || c == 'U'){
                returnList.add(c);
            }
        }
        return returnList;

    }
}
