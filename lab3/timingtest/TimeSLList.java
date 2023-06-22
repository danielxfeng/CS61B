package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        final int START = 1000;
        final int END = 128000;
        final int INCREMENT = 2;
        final int OPS = 10000;

        AList<Integer> ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();

        int target = START;

        while (target <= END) {
            SLList<Integer> exam = new SLList<>();
            int i = 0;
            while (i < target) {
                exam.addLast(i);
                i ++;
            }
            Stopwatch sw = new Stopwatch();
            int k = 0;
            while (k < OPS) {
                exam.getLast();
                k++;
            }
            double elapsedTime = sw.elapsedTime();

            ns.addLast(target);
            times.addLast(elapsedTime);
            opCounts.addLast(OPS);
            target *= INCREMENT;
        }
        printTimingTable(ns, times, opCounts);
    }

}
