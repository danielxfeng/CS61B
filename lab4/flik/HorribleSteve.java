package flik;

public class HorribleSteve {
    public static void main(String [] args) throws Exception {
        int i = 0;
        int j = 0;
        while (i < 500) {
            if (!Flik.isSameNumber(i, j)) {
                throw new RuntimeException(String.format("i : %d not same as j : %d ??", i, j));
            }
            System.out.println("i is " + i);
            i++;
            j++;
        }
    }
}
