public class Vector {
    String[] array;

    public Vector() {
        this.array = new String[0];
    }

    public void grow(String val) {
        String[] newArray = new String[this.array.length + 1];

        for (int i = 0; i < this.array.length; i++) {
            newArray[i] = this.array[i];
        }

        newArray[newArray.length - 1] = val;
        this.array = newArray;
    }

    public int findIndex(String val) {
        String elem;

        for (int i = 0; i < this.array.length; i++) {
            elem = this.array[i];
            if (elem.equals(val)) {
                return i;
            }
        }

        return -1;
    }

    public void getCourse(int courseIndex, int numHoles, String section) {
        int start;
        String[] newArray = new String[numHoles];

        if (numHoles == 18) {
            start = courseIndex * 18;
        } else if (section == "front") {
            start = courseIndex * 18;
        } else {
            start = (courseIndex * 18) + 9;
        }

        for (int i = 0; i < numHoles; i++) {
            newArray[i] = this.array[start + i];
        }

        this.array = newArray;
    }
}
