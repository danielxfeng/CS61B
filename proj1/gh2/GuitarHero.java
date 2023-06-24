package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 * Support 37 Keys.
 *
 * @author Daniel Feng
 */
public class GuitarHero {
    private static final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    public GuitarHero() {

    }

    public static void main(String[] args) {

        // Generate the concert which is a sound array mapping to keyboard.
        GuitarString[] concert = new GuitarString[keyboard.length()];
        for (int i = 0; i < keyboard.length(); i++) {

            /**
             * Magic numbers are from: https://sp21.datastructur.es/materials/proj/proj1/proj1
             * The ith character of the string keyboard corresponds to a frequency of 440⋅2(i−24)/12
             * , so that the character ‘q’ is 110Hz, ‘i’ is 220Hz, ‘v’ is 440Hz, and ‘ ‘ is 880Hz.
             */
            concert[i] = new GuitarString( 440.0 * Math.pow(2, (i - 24) / 12.0) / 12.0);
        }
        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();

                int idx = keyboard.indexOf(key);

                if (idx == -1) {
                    continue;
                }
                concert[idx].pluck();
                for (int i = 0; i < 10000; i += 1) {
                    StdAudio.play(concert[idx].sample());
                    concert[idx].tic();
                }
            }
        }
    }
}
