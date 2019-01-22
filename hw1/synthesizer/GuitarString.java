package synthesizer;

//Make sure this class is public
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int cap = (int) Math.round(SR / frequency);
        buffer = new ArrayRingBuffer<>(cap);
        for (int i = 0; i < cap; i++) {
            buffer.enqueue(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        //       Make sure that your random numbers are different from each other.
        int fillCount = buffer.fillCount();
        for (int i = 0; i < fillCount; i++) {
            buffer.dequeue();
        }
        int cap = buffer.capacity();
        for (int i = 0; i < cap; i++) {
            buffer.enqueue(Math.random() - 0.5);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        double newSample = (buffer.dequeue() + buffer.peek()) * DECAY / 2;
        buffer.enqueue(newSample);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }

//    public static void main(String[] args) {
//        GuitarString guitarString = new GuitarString(4410);
//        guitarString.buffer.enqueue(0.2);
//        guitarString.buffer.enqueue(0.4);
//        guitarString.buffer.enqueue(0.6);
//        guitarString.tic();
//        for (Double d : guitarString.buffer) {
//            System.out.print(d + " ");
//        }
//        System.out.println();
//    }
}
