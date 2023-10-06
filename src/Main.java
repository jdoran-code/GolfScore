/*
 * Program Name: GolfScore
 *
 * Author: Justin Doran
 * Email: justin.doran2002@gmail.com
 *
 * Description: An application that allows users to keep
 * score in either single-player or team games of golf across ten
 * public courses in the Greater Boston area. The program will
 * determine the winner at the end of the game and oversee a sudden
 * death mode if a two-way tie needs to be broken.
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;



public class Main {
    static Scanner scan = new Scanner(System.in);

    /*
     * main()
     *
     * Executes the program's functionality in order.
     */
    public static void main(String[] args) throws IOException {
        int[] pars;
        String course;
        int numHoles = 0;
        String section;

        course = getCourse();
        numHoles = getHoles();
        if (numHoles == 9) {
            section = getSection();
        } else {
            section = "";
        }

        pars = getGame(course, numHoles, section);

        int numUnits;
        int teamSize;
        if (wantTeams()) {
            System.out.print("Number of teams: ");
            numUnits = scan.nextInt();
            System.out.print("Number of players per team: ");
            teamSize = scan.nextInt();
        }
        else {
            System.out.print("Number of players: ");
            numUnits = scan.nextInt();
            teamSize = 1;
        }

        int[] scoreArray = new int[numUnits];
        int[] unitArray = new int[numUnits];
        for (int i = 0; i <= numUnits - 1; i++) {
            unitArray[i] = i + 1;
        }

        game(numHoles, unitArray, scoreArray, teamSize, pars);

        System.out.println("Final Scores");
        int winner = bestPlayer(numUnits, scoreArray, teamSize, numHoles, pars);
        String winnerType = getUnit(teamSize);
        System.out.println(winnerType + " " + winner + " wins!");
    }

    /*
     * getCourse()
     *
     * Prompts the user to select one of the ten courses, and returns
     * the name of the selected course.
     */
    public static String getCourse() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("coursePars.csv"));
        String course;
        String line;
        int courseNum;
        String[] courseList = new String[10];
        int i = -1;

        while ((line = reader.readLine()) != null) {
            String[] row = line.split(",");
            if (i >= 0) {
                courseList[i] = row[0];
            }
            i++;
        }
        reader.close();

        do {
            for (i = 0; i < courseList.length; i++) {
                System.out.println((i + 1) + ". " + courseList[i]);
            }
            System.out.print("Enter a number: ");

            courseNum = scan.nextInt();
        } while (courseNum < 1 || courseNum > 10);
        course = courseList[courseNum - 1];
        System.out.println(course);
        return course;
    }

    /*
     * getHoles()
     *
     * Prompts the user to choose whether they want to play
     * 9 or 18 holes, and returns the number of holes that
     * they choose.
     */
    public static int getHoles() {
        int choice;
        do {
            System.out.println("1. 9 Holes");
            System.out.println("2. 18 Holes");
            System.out.print("Select 1 or 2: ");
            choice = scan.nextInt();
        } while (choice != 1 && choice != 2);

        return choice * 9;
    }

    /*
     * getSection()
     *
     * Called when the user wants to play a nine-hole
     * game, this method prompts the player to choose
     * which half of the course they want to play and
     * returns a string indicating their choice.
     */
    public static String getSection() {
        int choice;
        do {
            System.out.println("1. Front nine");
            System.out.println("2. Back nine");
            System.out.print("Select a number: ");
            choice = scan.nextInt();
        } while (choice != 1 && choice != 2);

        if (choice == 1) {
            return "front";
        } else {
            return "back";
        }
    }

    /*
     * getGame(Vector pars, String course, int numHoles, String section)
     *
     * Identifies the correct set of pars from the coursePars.csv file
     * based on the course, number of holes, and section that the user wants
     * to play, then returns an array of the pars of the user's desired game
     * in order. 
     */
    public static int[] getGame(String course, int numHoles, String section) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("coursePars.csv"));
        String line;
        int[] pars = new int[9];

        while ((line = reader.readLine()) != null) {
            String[] row = line.split(",");

            if (row[0].equals(course) && numHoles == 18) {
                pars = new int[18];
                for (int i = 1; i < row.length; i++) {
                    pars[i - 1] = Integer.parseInt(row[i]);
                }
            } else if (row[0].equals(course) && numHoles == 9 && section.equals("front")) {
                for (int i = 1; i < row.length - 9; i++) {
                    pars[i - 1] = Integer.parseInt(row[i]);
                }
            } else if (row[0].equals(course) && numHoles == 9 && section.equals("back")) {
                for (int i = 10; i < row.length; i++) {
                    pars[i - 10] = Integer.parseInt(row[i]);
                }
            }
        }

        reader.close();
        return pars;
    }

    /*
     * wantTeams()
     *
     * Prompts the user to choose if they want to play a
     * single-player or team game, and returns a boolean
     * value that will be true if the user chose team mode
     * and false otherwise.
     */
    public static boolean wantTeams() {
        int choice;
        do {
            System.out.println("1. Team mode");
            System.out.println("2. Single player mode");
            System.out.print("Select a number: ");
            choice = scan.nextInt();
        } while (choice != 1 && choice != 2);

        if (choice == 1) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * game(int numHoles, int[] unitArray, int[] scoreArray, int teamSize, Vector pars)
     *
     * Oversees the playing of the game by asking each player to input their
     * score for each hole of the game.
     */
    public static void game(int numHoles, int[] unitArray, int[] scoreArray, int teamSize, int[] pars) {
        int par;
        for (int h = 1; h <= numHoles; h++) {
            par = pars[h - 1];
            System.out.println("Hole " + h + " Par " + par);
            int score;
            for (int t = 0; t <= unitArray.length - 1; t++) {
                for (int p = 1; p <= teamSize; p++) {
                    printPlayer(t, p, teamSize, unitArray);
                    score = scan.nextInt();
                    scoreType(score, par);
                    scoreArray[t] += score;
                }
            }
        }
    }

    /*
     * printPlayer(int t, int p, int teamSize, int[]unitArray)
     *
     * Prints a prompt that asks for the input player of
     * the input team to enter their score. Wording of prompt
     * is adjusted based on whether game is in single-player
     * or team mode.
     */
    public static void printPlayer(int t, int p, int teamSize, int[] unitArray) {
        if (teamSize == 1) {
            System.out.print("Player " + unitArray[t] + " Score: ");
        }
        else {
            System.out.print("Team " + unitArray[t] + " Player " + p + " Score: ");
        }
    }

    /*
     * scoreType()
     *
     * Prints the type of score a player got on a hole
     * based on the input score and the input par.
     */
    public static void scoreType(int score, int par) {
        String[] array = {"a double eagle", "an eagle", "a birdie", "par", "a bogey",
                "a double bogey", "a triple bogey"};
        int adjScore = (score - par) + 3;

        if (score == 1) {
            System.out.println("You got a hole in one!");
        } else if (adjScore <= 6) {
            System.out.println("You got " + array[adjScore] + "!");
        } else {
            System.out.println("Score too high for designation.");
        }
    }

    /*
     * bestPlayer(int numUnits, int[] scoreArray, int teamSize, int numHoles, Vector pars)
     *
     * Prints the final score of each competing unit (player
     * or team) and returns the integer representing the
     * winning unit
     */
    public static int bestPlayer(int numUnits, int[] scoreArray, int teamSize, int numHoles, int[] pars) {
        int bestScore = scoreArray[0];
        int winner = 1;
        int score;
        String unit = getUnit(teamSize);

        System.out.println(unit + " 1: " + bestScore);
        for (int u = 2; u <= numUnits; u++) {
            score = scoreArray[u - 1];
            System.out.println(unit + " " + u + ": " + score);
            if (score < bestScore) {
                bestScore = score;
                winner = u;
            }
        }

        for (int u = 1; u <= numUnits; u++) {
            if (scoreArray[u - 1] == bestScore && u != winner) {
                winner = suddenDeath(u, winner, teamSize, numHoles, pars);
                break;
            }
        }

        return winner;
    }

    /*
     * getUnit(int teamSize)
     *
     * A helper function that returns a string representing
     * the competing unit of the current game (player or
     * team) based on whether the game is in single-player
     * or team mode.
     */
    public static String getUnit(int teamSize) {
        if (teamSize == 1) {
            return "Player";
        }
        else {
            return "Team";
        }
    }

    /*
     * suddenDeath(int p1, int p2, int teamSize, int numHoles, Vector pars)
     *
     * Oversees sudden death mode in the event of a two-way tie. Has the
     * tied players or teams play extra holes in the same order of holes as
     * the regular game until one has a lower score than the other, at which
     * point the integer representing the winning unit will be returned.
     */
    public static int suddenDeath(int p1, int p2, int teamSize, int numHoles, int[] pars) {
        String unit = getUnit(teamSize);

        System.out.println(unit + "s " + p1 + " and " + p2 + " are tied for first place!");
        int[] scoreArray = new int[2];
        int[] unitArray = {p1, p2};
        int round = 1;
        int[] suddenDeathPars = new int[1];

        while (scoreArray[0] == scoreArray[1]) {
            for (int h = 1; h <= numHoles; h++) {
                suddenDeathPars[0] = pars[h - 1];
                System.out.println("Sudden Death Round " + round);
                game(1, unitArray, scoreArray, teamSize, suddenDeathPars);
                if (scoreArray[0] != scoreArray[1]) {
                    break;
                }
                round++;
            }
        }

        if (scoreArray[0] < scoreArray[1]) {
            return p1;
        } else {
            return p2;
        }
    }
}