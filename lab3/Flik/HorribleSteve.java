public class HorribleSteve {
    public static void main(String [] args) {
        int i, j;
        for (i = 0, j = 0; i < 500; ++i, ++j) {
            if (!Flik.isSameNumber(i, j)) {
                break; // break exits the for loop!
            }
        }
        System.out.println("i is " + i);
    }
}
