package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        final int START = 1000;
        final int END = 128000;
        final int INCREMENT = 2;

        AList<Integer> ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();

        int target = START;

        while (target <= END) {
            AList<Integer> exam = new AList<>();
            int i = 0;
            Stopwatch sw = new Stopwatch();
            while (i < target) {
                exam.addLast(i);
                i ++;
            }
            ns.addLast(target);
            times.addLast(sw.elapsedTime());
            opCounts.addLast(i);
            target *= INCREMENT;
        }
        printTimingTable(ns, times, opCounts);



    }
}
