/*
 * GolfScore.java
 * 
 * Created by: Justin Doran
 * 
 * This program manages and keeps score for a game of golf. The user can 
 * choose to play on a number of public golf courses in the greater Boston area
 * and player any subset of consecutive holes on these courses. The game will also 
 * revert to a sudden death mode in the event that two players or teams are tied
 * at the end of the game. It should be noted that most of the code in the files 
 * HashTable.java, Queue.java, LLQueue.java, and ChainedHashTable.java is not mine,
 * save for a few small modifications. These files were developed by the faculty and 
 * staff of the course CS 112 at Boston University, and they provide the GolfScore program
 * with a number of useful data structures. The file coursePars.csv contains the pars
 * of the holes at each golf course that can be played on the program. The data on 
 * this file was found on the course's websites. 
 */

import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class GolfScore {
    static Scanner scan = new Scanner(System.in);
    
    /*
     * Manages the game by interacting with the players on each hole,
     * keeping track of scores and printing out final scores along with 
     * the winner at the end
     */
    public static void game(LLQueue<String> coursePars, int start, int finish, 
    int teamSize, int[] scoreArray) {
        LLQueue<String> other = new LLQueue<String>();
        String parString;
        int par;
        int score;

        for (int h = 1; h <= 18; h++) {
            parString = coursePars.remove();
            other.insert(parString);
            if (h >= start && h <= finish) {
                par = Integer.parseInt(parString);
                System.out.println("Hole " + h + " Par " + par);
                System.out.println();
                for (int t = 0; t < scoreArray.length; t++) {
                    for (int p = 0; p < teamSize; p++) {
                        printPlayer(t, p, teamSize);
                        score = scan.nextInt();
                        scoreType(score, par);
                        scoreArray[t] += score;
                        System.out.println();
                    }
                }
            }
        }

        for (int h = 1; h <= 18; h++) {
            parString = other.remove();
            coursePars.insert(parString);
        }

        int bestTeam = 1;
        int bestScore = scoreArray[0];

        System.out.println("Final Scores:");
        for (int t = 0; t < scoreArray.length; t++) {
            score = scoreArray[t];
            if (teamSize == 1) {
                System.out.println("Player " + (t + 1) + ": " + score);
            } else {
                System.out.println("Team " + (t + 1) + ": " + score);
            }

            if (score < bestScore) {
                bestScore = score;
                bestTeam = t + 1;
            }
        }
        System.out.println();

        for (int t = 0; t < scoreArray.length; t++) {
            if (scoreArray[t] == bestScore && t + 1 != bestTeam) {
                bestTeam = suddenDeath((t + 1), bestTeam, teamSize, coursePars, 
                start, finish);
                break;
            }
        }

        if (teamSize == 1) {
            System.out.println("Player " + bestTeam + " wins!");
        } else {
            System.out.println("Team " + bestTeam + " wins!");
        }
        System.out.println();
    }

    /*
     * A method that prints the prompt for each player to enter their score
     * based on the format of the game (team or single player mode)
     */
    public static void printPlayer(int t, int p, int teamSize) {
        if (teamSize == 1) {
            System.out.print("Player " + (t + 1) + " Score: ");
        }
        else {
            System.out.print("Team " + (t + 1) + " Player " + (p + 1) + " Score: ");
        }
    }

    /*
     * A method that prints the type of score a player got on a hole
     * based on the score itself along with the hole's par
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
     * Management method for the sudden death mode of the game; oversees
     * a game between two tied teams/players, plays the same order of holes that were
     * played in the initial game, and stops as soon as the two teams/players are
     * no longer tied
     */
    public static int suddenDeath(int t1, int t2, int teamSize, LLQueue<String> coursePars, 
    int start, int finish) {
        if (teamSize == 1) {
            System.out.println("Players " + t1 + " and " + t2 + " are tied!");
        } else {
            System.out.println("Teams " + t1 + " and " + t2 + " are tied!");
        }
        System.out.println();

        LLQueue<String> other = new LLQueue<String>();
        int[] scoreArray = new int[2];
        int[] teamArray = {(t1 - 1), (t2 - 1)};
        int round = 1;
        String parString;
        int score;
        int par;

        while (scoreArray[0] == scoreArray[1]) {
            System.out.println("Sudden Death Round " + round);
            System.out.println();
            for (int h = 1; h <= 18; h++) {
                parString = coursePars.remove();
                other.insert(parString);
                if (h >= start && h <= finish && scoreArray[0] == scoreArray[1]) {
                    par = Integer.parseInt(parString);
                    System.out.println("Hole " + h + " Par " + par);
                    System.out.println();
                    for (int t = 0; t < scoreArray.length; t++) {
                        for (int p = 0; p < teamSize; p++) {
                            printPlayer(teamArray[t], p, teamSize);
                            score = scan.nextInt();
                            scoreType(score, par);
                            scoreArray[t] += score;
                            System.out.println();
                        }
                    }
                }
            }

            for (int h = 1; h <= 18; h++) {
                parString = other.remove();
                coursePars.insert(parString);
            }

            round++;
        }

        if (scoreArray[0] < scoreArray[1]) {
            return t1;
        } else {
            return t2;
        }
    }

    /*
     * Management method for the setup of the game; prompts the user
     * to choose a course, a specific subset of consecutive holes, a 
     * format for the game (teams or players), along with the number of 
     * teams/players. Sets up the parameters for the game method based on
     * how the user answers. 
     */
    public static void setup(ChainedHashTable courses) {
        String courseName;
        LLQueue<String> coursePars;
        courseName = scan.nextLine();

        do {
            System.out.print("Enter a course: ");
            courseName = scan.nextLine();
            coursePars = courses.remove(courseName);

            if (coursePars == null) {
                System.out.println("Course not on file, try again.");
            }
            System.out.println();
        } while (coursePars == null);
        
        int start = 0;
        int finish = 0;
        int input;

        do {
            System.out.print("Enter the number of your starting hole: ");
            input = scan.nextInt();

            if (input < 1 || input > 18) {
                System.out.println("Invalid input, try again.");
            } else {
                start = input;
            }
            System.out.println();
        } while (input < 1 || input > 18);

        do {
            System.out.print("Enter the number of your final hole: ");
            input = scan.nextInt();

            if (input < 1 || input > 18 || input < start) {
                System.out.println("Invalid input, try again.");
            } else {
                finish = input;
            }
            System.out.println();
        } while (input < 1 || input > 18 || input < start);
        
        boolean wantTeams = wantTeams();
        int teamSize;
        int[] scoreArray;

        if (wantTeams) {
            System.out.print("Number of teams: ");
            scoreArray = new int[scan.nextInt()];
            System.out.print("Number of players per team: ");
            teamSize = scan.nextInt();
        }
        else {
            System.out.print("Number of players: ");
            scoreArray = new int[scan.nextInt()];
            teamSize = 1;
        }
        System.out.println();

        game(coursePars, start, finish, teamSize, scoreArray);
        
        String parString;
        for (int h = 1; h <= 18; h++) {
            parString = coursePars.remove();
            courses.insert(courseName, parString);
        }
    }

    /*
     * A helper method that returns true if the user wants
     * team mode and false if they want single player mode
     */
    public static boolean wantTeams() {
        int choice;
        do {
            System.out.println("1. Team mode");
            System.out.println("2. Single player mode");
            System.out.print("Select a number: ");
            choice = scan.nextInt();

            if (choice != 1 && choice != 2) {
                System.out.println("Invalid input, try again.");
            }
            System.out.println();
        } while (choice != 1 && choice != 2);

        if (choice == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    public static void main(String[] args) throws IOException {
        ChainedHashTable courses = new ChainedHashTable(20);
        BufferedReader reader = new BufferedReader(new FileReader("coursePars.csv"));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] entry = line.split(",");
            for (int i = 1; i < entry.length; i++) {
                courses.insert(entry[0], entry[i]);
            }
        }

        int choice = 0;
        while (choice != 2) {
            System.out.println("1. Play game");
            System.out.println("2. Quit");
            System.out.print("Select a number: ");

            choice = scan.nextInt();

            if (choice == 1) {
                System.out.println();
                setup(courses);
            } else if (choice != 2) {
                System.out.println("Invalid input, try again.");
                System.out.println();
            }
        }

        System.out.println();
        System.out.println("Goodbye!");
    }
}