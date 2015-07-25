package com.wellsen.arithmetictest.model;

import java.util.Random;

/**
 * Created by admin on 7/19/2015.
 */
public class RandomEquation {

    private static final int MAX_CHOICES = 4;

    private static final int DIFFICULTY_EASY = 10;
    private static final int DIFFICULTY_MEDIUM = 20;
    private static final int DIFFICULTY_HARD = 40;
    private static final int DIFFICULTY_DEFAULT = DIFFICULTY_MEDIUM;

    private int maxOperandValue;

    public int[] answers = new int[MAX_CHOICES];
    public int answer;

    public String equation;
    private int firstOperand;
    private int secondOperand;
    private char[] operators = new char[] { '+', '-', '*', '/' };
    private char operator;



    private Random random;

    public RandomEquation(int difficulty) {
        maxOperandValue = setDifficultyLevel(difficulty);

        random = new Random();
        int r = random.nextInt(operators.length);
        operator = operators[r];

        firstOperand = random.nextInt(maxOperandValue + 1);
        secondOperand = random.nextInt(maxOperandValue + 1);

        answer = buildEquation(firstOperand, secondOperand, operator);
        answers = buildAnswers(operator);
    }

    private int setDifficultyLevel(int diff) {
        switch (diff) {
            case DIFFICULTY_EASY:
                return DIFFICULTY_EASY;
            case DIFFICULTY_MEDIUM:
                return DIFFICULTY_MEDIUM;
            case DIFFICULTY_HARD:
                return DIFFICULTY_HARD;
            default:
                return DIFFICULTY_DEFAULT;
        }
    }

    private int buildEquation(int first,
                               int second,
                               char opr) {
        if (opr == '/') {
            while (second == 0) {
                second = random.nextInt(maxOperandValue + 1);
            }
            first *= second;
            firstOperand = first;
            secondOperand = second;
        }

        equation = first + " "
                + opr + " "
                + second;

        switch (opr) {
            case '+':
                return first + second;
            case '-':
                return first - second;
            case '*':
                return first * second;
            case '/':
                return first / second;
            default:
                return 0;
        }
    }

    private int[] buildAnswers(char opr) {
        int[] ans = new int[MAX_CHOICES];
        int randomIndex = random.nextInt(MAX_CHOICES);
        ans[randomIndex] = answer;
        int offset;

        switch (opr) {
            case '+':
                offset = 1;
                while (randomIndex - offset >= 0) {
                    ans[randomIndex - offset] = answer - offset;
                    offset++;
                }

                offset = 1;
                while (randomIndex + offset < MAX_CHOICES) {
                    ans[randomIndex + offset] = answer + offset;
                    offset++;
                }
                break;
            case  '-':
                offset = 1;
                while (randomIndex - offset >= 0) {
                    ans[randomIndex - offset] = answer - offset;
                    offset++;
                }

                offset = 1;
                while (randomIndex + offset < MAX_CHOICES) {
                    ans[randomIndex + offset] = answer + offset;
                    offset++;
                }
                break;
            case '*':
                offset = 1;
                while (randomIndex - offset >= 0) {
                    ans[randomIndex - offset] = answer - offset * secondOperand;
                    offset++;
                }

                offset = 1;
                while (randomIndex + offset < MAX_CHOICES) {
                    ans[randomIndex + offset] = answer + offset * secondOperand;
                    offset++;
                }
                break;
            case '/':
                offset = 1;
                while (randomIndex - offset >= 0) {
                    ans[randomIndex - offset] = answer - offset;
                    offset++;
                }

                offset = 1;
                while (randomIndex + offset < MAX_CHOICES) {
                    ans[randomIndex + offset] = answer + offset;
                    offset++;
                }
                break;
        }

        return ans;
    }
}
