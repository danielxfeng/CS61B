/** Class that prints the Collatz sequence starting from a given number.
 *  @author Daniel Feng
 */
public class Collatz {

    /**
     * This method return the next Collatz number.
     * @param n The start number of collatz number.
     * @return The next Collatz number.
     * */
    public static int nextNumber(int n) {
        if (n % 2 == 0) {
            return n / 2;
        }
        return n * 3 + 1;
    }

    /**
     * This method print the Collatz number from 5
     * @param args null
     */
    public static void main(String[] args) {
        int n = 5; // the number in Lab requirement
        System.out.print(n + " ");
        while (n != 1) {
            n = nextNumber(n);
            System.out.print(n + " ");
        }
    }

}

