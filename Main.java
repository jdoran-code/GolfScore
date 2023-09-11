import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;



public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        Vector pars = new Vector(new String[19]);
        String course;
        int numHoles = 0;
        String section;

        while(pars.array.length == 19) {
            course = getCourse();
            numHoles = getHoles();
            if (numHoles == 9) {
                section = getSection();
            } else {
                section = "";
            }
            getGame(pars, course, numHoles, section);

            if (pars.array.length == 19) {
                System.out.println("Course not on file, try again.");
            }
        }

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

    public static String getCourse() {
        String course;
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter a course: ");
        course = scan.nextLine();
        return course;
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

    public static void getGame(Vector pars, String course, int numHoles, String section) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("coursePars.csv"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] row = line.split(",");
            pars.Modify(row);

            if (pars.array[0].equals(course)) {
                pars.getPars(numHoles, section);
                break;
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
            } else {
                return false;
            }
        }
    }

    public static void game(int numHoles, int[] unitArray, int[] scoreArray, int teamSize, Vector pars) {
        Scanner scan = new Scanner(System.in);
        int par;
        for (int h = 1; h <= numHoles; h++) {
            par = Integer.parseInt(pars.array[h - 1]);
            System.out.println("Hole " + h + " Par " + par);
            int score;
            for (int t = 0; t <= unitArray.length - 1; t++) {
                for (int p = 1; p <= teamSize; p++) {
                    printUnit(t, p, teamSize, unitArray);
                    score = scan.nextInt();
                    scoreType(score, par);
                    scoreArray[t] += score;
                }
            }
        }
    }

    public static void printUnit(int t, int p, int teamSize, int[] unitArray) {
        if (teamSize == 1) {
            System.out.print("Player " + unitArray[t] + " Score: ");
        }
        else {
            System.out.print("Team " + unitArray[t] + " Player " + p + " Score: ");
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

    public static int bestPlayer(int numUnits, int[] scoreArray, int teamSize, int numHoles, Vector pars) {
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
            if (scoreArray[u - 1] == bestScore) {
                if (u != winner) {
                    winner = suddenDeath(u, winner, teamSize, numHoles, pars);
                    break;
                }
            }
        }

        return winner;
    }

    public static String getUnit(int teamSize) {
        if (teamSize == 1) {
            return "Player";
        }
        else {
            return "Team";
        }
    }
    
    public static int suddenDeath(int p1, int p2, int teamSize, int numHoles, Vector pars) {
        String unit = getUnit(teamSize);

        System.out.println(unit + "s " + p1 + " and " + p2 + " are tied for first place!");
        int[] scoreArray = new int[2];
        int[] unitArray = {p1, p2};
        int round = 1;
        Vector suddenDeathPars = new Vector(new String[1]);

        while (scoreArray[0] == scoreArray[1]) {
            for (int h = 1; h <= numHoles; h++) {
                suddenDeathPars.array[0] = pars.array[h - 1];
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