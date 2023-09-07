import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;

//Hello

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        Vector courses = new Vector();
        Vector pars = new Vector();

        BufferedReader reader = new BufferedReader(new FileReader("courses.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            courses.grow(line);
        }

        BufferedReader reader2 = new BufferedReader(new FileReader("pars.txt"));
        while ((line = reader2.readLine()) != null) {
            pars.grow(line);
        }

        System.out.print("Name of Course: ");
        String course = scan.nextLine();
        int courseIndex = courses.findIndex(course);

        int numHoles = getHoles();

        String section;
        if (numHoles == 9) {
            section = getSection();
        } else {
            section = null;
        }

        pars.getCourse(courseIndex, numHoles, section);

        int numCompetitors;
        int teamSize;
        if (wantTeams()) {
            System.out.print("Number of teams: ");
            numCompetitors = scan.nextInt();
            System.out.print("Number of players per team: ");
            teamSize = scan.nextInt();
        }
        else {
            System.out.print("Number of players: ");
            numCompetitors = scan.nextInt();
            teamSize = 1;
        }

        int[] scoreArray = new int[numCompetitors];
        int[] competitorArray = new int[numCompetitors];
        for (int i = 0; i <= numCompetitors - 1; i++) {
            competitorArray[i] = i + 1;
        }

        game(numHoles, competitorArray, scoreArray, teamSize, pars);

        System.out.println("Final Scores");
        int winner = bestPlayer(numCompetitors, scoreArray, teamSize, numHoles, pars);
        String winnerType = getCompetitor(teamSize);
        System.out.println(winnerType + " " + winner + " wins!");
    }

    public static int getHoles() {
        Scanner scan = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.println("1. 9 Holes");
            System.out.println("2. 18 Holes");
            System.out.print("Select 1 or 2: ");
            choice = scan.nextInt();
            if (choice == 1) {
                return 9;
            } else if (choice == 2) {
                return 18;
            }
        }
    }

    public static String getSection() {
        Scanner scan = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.println("1. Front nine");
            System.out.println("2. Back nine");
            System.out.print("Select a number: ");
            choice = scan.nextInt();
            if (choice == 1) {
                return "front";
            } else if (choice == 2) {
                return "back";
            }
        }
    }

    public static boolean wantTeams() {
        Scanner scan = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.println("1. Team mode");
            System.out.println("2. Single player mode");
            System.out.print("Select a number: ");
            choice = scan.nextInt();
            if (choice == 1) {
                return true;
            }
            else if (choice == 2) {
                return false;
            }
        }
    }

    public static void game(int numHoles, int[] competitorArray, int[] scoreArray, int teamSize, Vector pars) {
        Scanner scan = new Scanner(System.in);
        int par;
        for (int h = 1; h <= numHoles; h++) {
            par = Integer.parseInt(pars.array[h - 1]);
            System.out.println("Hole " + h + " Par " + par);
            int score;
            for (int t = 0; t <= competitorArray.length - 1; t++) {
                for (int p = 1; p <= teamSize; p++) {
                    printCompetitor(t, p, teamSize, competitorArray);
                    score = scan.nextInt();
                    scoreType(score, par);
                    scoreArray[t] += score;
                }
            }
        }
    }

    public static void printCompetitor(int t, int p, int teamSize, int[] competitorArray) {
        if (teamSize == 1) {
            System.out.print("Player " + competitorArray[t] + " Score: ");
        }
        else {
            System.out.print("Team " + competitorArray[t] + " Player " + p + " Score: ");
        }
    }

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

    public static int bestPlayer(int competitors, int[] array, int teamSize, int numHoles, Vector pars) {
        int bestScore = array[0];
        int winner = 1;
        int score;
        String competitor = getCompetitor(teamSize);

        System.out.println(competitor + " 1: " + bestScore);
        for (int c = 2; c <= competitors; c++) {
            score = array[c - 1];
            System.out.println(competitor + " " + c + ": " + score);
            if (score < bestScore) {
                bestScore = score;
                winner = c;
            }
        }

        for (int c = 1; c <= competitors; c++) {
            if (array[c - 1] == bestScore) {
                if (c != winner) {
                    winner = suddenDeath(c, winner, teamSize, numHoles, pars);
                    break;
                }
            }
        }

        return winner;
    }

    public static String getCompetitor(int teamSize) {
        if (teamSize == 1) {
            return "Player";
        }
        else {
            return "Team";
        }
    }

    public static int suddenDeath(int p1, int p2, int teamSize, int numHoles, Vector pars) {
        Scanner scan = new Scanner(System.in);
        String competitor = getCompetitor(teamSize);

        System.out.println(competitor + "s " + p1 + " and " + p2 + " are tied for first place!");
        int[] scoreArray = new int[2];
        int[] competitorArray = {p1, p2};
        int round = 1;
        Vector suddenDeathPars = new Vector();
        suddenDeathPars.grow(pars.array[0]);

        while (scoreArray[0] == scoreArray[1]) {
            for (int h = 1; h <= numHoles; h++) {
                suddenDeathPars.array[0] = pars.array[h - 1];
                System.out.println("Sudden Death Round " + round);
                game(1, competitorArray, scoreArray, teamSize, suddenDeathPars);
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