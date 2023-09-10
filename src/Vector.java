public class Vector {
    String[] array;

    public Vector(String[] array) {
        this.array = array;
    }

    public void Modify(String[] row) {
        for (int i = 0; i < row.length; i++) {
            this.array[i] = row[i];
        }
    }

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
