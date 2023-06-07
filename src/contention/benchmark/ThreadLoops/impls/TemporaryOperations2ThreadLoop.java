package contention.benchmark.ThreadLoops.impls;

import contention.abstractions.DataStructure;
import contention.benchmark.keygenerators.abstractions.KeyGenerator;
import contention.benchmark.ThreadLoops.abstractions.ThreadLoopAbstract;
import contention.benchmark.ThreadLoops.parameters.TemporaryOperationsThreadLoopParameters;

import java.lang.reflect.Method;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class TemporaryOperations2ThreadLoop extends ThreadLoopAbstract {
    private KeyGenerator keygen;
    private long time;
    private int pointer;
    private final TemporaryOperationsThreadLoopParameters parameters;

    /**
     * The random number
     */
    protected Random rand = new Random();
    private final double[][] cdf;

    public TemporaryOperations2ThreadLoop(int myThreadNum, DataStructure<Integer> dataStructure,
//                                          CompositionalMap<Integer, Integer> bench,
                                          Method[] methods,
                                          KeyGenerator keygen, TemporaryOperationsThreadLoopParameters parameters) {
        super(myThreadNum, dataStructure, methods);
        this.keygen = keygen;
        this.time = 0;
        this.pointer = 0;
        this.parameters = parameters;

        this.cdf = new double[parameters.tempOperCount][4];

        for (int i = 0; i < parameters.tempOperCount; i++) {
            cdf[i][0] = 0;//parameters.numWriteAlls;
            cdf[i][1] = parameters.numInserts[i];
            cdf[i][2] = cdf[i][1] + parameters.numErases[i];
            cdf[i][3] = cdf[i][2];// + parameters.numSnapshots;
        }
    }

    public TemporaryOperations2ThreadLoop(int myThreadNum, DataStructure<Integer> dataStructure,
                                         Method[] methods,
                                         TemporaryOperationsThreadLoopParameters parameters) {
        this(myThreadNum, dataStructure, methods, parameters.keyGeneratorBuilder.getKeyGenerator(), parameters);
    }

    private void update_pointer() {
        if (time >= parameters.opTimes[pointer]) {
            time = 0;
            ++pointer;
            if (pointer >= parameters.tempOperCount) {
                pointer = 0;
            }
        }
        ++time;
    }

    @Override
    public void step() {
        double coin = rand.nextDouble();
        if (coin < cdf[pointer][0]) { // 1. should we run a writeAll operation?
            // todo something very strange

            int key = keygen.nextGet();

            // init a collection
            Vector<Integer> vec = new Vector<Integer>(key);
            vec.add(key / 2); // accepts duplicate

            if (removeAll(vec)) {
                update_pointer();
            }
        } else if (coin < cdf[pointer][1]) { // 2. should we run an insert
            int key = keygen.nextInsert();
            if (insert(key) == null) {
                update_pointer();
            }
        } else if (coin < cdf[pointer][2]) { // 3. should we run a remove
            int key = keygen.nextRemove();
            if (remove(key) != null) {
                update_pointer();
            }
        } else if (coin < cdf[pointer][3]) { // 4. should we run a readAll operation?
            size();
            update_pointer();
        } else { //if (coin < cdf[3]) { // 5. then we should run a readSome operation
            int key = keygen.nextGet();
            get(key);
            update_pointer();
        } //else {
//                sleep(Parameters.sleepTime);
        // warmup для определения sleepTime
//            }
    }

    @Override
    public void prefill(AtomicInteger prefillSize) {
        while (prefillSize.get() > 0) {
            int curPrefillSize = prefillSize.decrementAndGet();
            int v = keygen.nextPrefill();
            if (curPrefillSize < 0 || dataStructure.insert(v) != null) {
                prefillSize.incrementAndGet();
            }
        }
    }

}
