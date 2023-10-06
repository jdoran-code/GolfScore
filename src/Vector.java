/*
 * Vector class
 *
 * A supporting object for the Main class that initializes
 * an array whose size and makeup can be changed in various
 * ways. Inspired by the Vector library of the C++ language.
 */

public class Vector {
    String[] array;

    public Vector(String[] array) {
        this.array = array;
    }

    /*
     * Modify(String[] row)
     *
     * Modifies the vector such that its
     * contents match those of the input array
     */
    public void Modify(String[] row) {
        for (int i = 0; i < row.length; i++) {
            this.array[i] = row[i];
        }
    }

    /*
     * getPars(int numHoles, String section)
     *
     * Reduces the Vector so that it shows only the hole
     * pars of the game, in order, based on the user's
     * desired number of holes and course section
     */
    public void getPars(int numHoles, String section) {
        int start;
        String[] newArray = new String[numHoles];

        if (section.equals("back")) {
            start = 10;
        } else {
            start = 1;
        }

        for (int i = 0; i < numHoles; i++) {
            newArray[i] = this.array[start + i];
        }

        this.array = newArray;
    }
}
