package contention.benchmark.workload.thread.loops.impls;

import contention.abstractions.DataStructure;
import contention.benchmark.workload.args.generators.abstractions.ArgsGenerator;
import contention.benchmark.workload.stop.condition.StopCondition;
import contention.benchmark.workload.thread.loops.abstractions.ThreadLoop;
import contention.benchmark.workload.thread.loops.parameters.RatioThreadLoopParameters;

import java.lang.reflect.Method;
import java.util.Random;
import java.util.Vector;

public class MultiDefaultThreadLoop extends ThreadLoop {
    private final ArgsGenerator argsGenerator;
    protected Random rand = new Random();

    /**
     * The distribution of methods of an array of distributions as an array of percentiles
     * <p>
     * 0%       cdf[i][0]                cdf[i][2]      100%
     * |--writeAll--|--writeSome--|--readAll--|--readSome--|
     * |-----------write----------|--readAll--|--readSome--| cdf[1]
     */
    private final double[][] cdfs;

    public MultiDefaultThreadLoop(int threadId, DataStructure<Integer> dataStructure,
                             Method[] methods, StopCondition stopCondition,
                             RatioThreadLoopParameters[] parameters, ArgsGenerator argsGenerator, int loopCount) {
        super(threadId, dataStructure, methods, stopCondition);
        /* initialize the method boundaries */
        this.argsGenerator = argsGenerator;
        cdfs = new double[loopCount][4];
        for (int i = 0; i < loopCount; ++i) {
            cdfs[i][0] = parameters[i].writeAllsRatio;
            cdfs[i][1] = cdfs[i][0] + parameters[i].insertRatio;
            cdfs[i][2] = cdfs[i][1] + parameters[i].removeRatio;
            cdfs[i][3] = cdfs[i][2] + parameters[i].snapshotsRatio;
        }
    }

    @Override
    public void step() {
        int key = argsGenerator.nextGet();
        double coin = rand.nextDouble();
        if (coin < cdfs[key - 1][0]) { // 1. should we run a writeAll operation?
            // todo: something very strange

            // init a collection
            Vector<Integer> vec = new Vector<Integer>(key);
            vec.add(key / 2); // accepts duplicate

            executeRemoveAll(vec);
        } else if (coin < cdfs[key - 1][1]) { // 2. should we run an insert
            executeInsert(key);
        } else if (coin < cdfs[key - 1][2]) { // 3. should we run a remove
            executeRemove(key);
        } else if (coin < cdfs[key - 1][3]) { // 4. should we run a readAll operation?
            executeSize();
        } else { //if (coin < cdf[3]) { // 5. then we should run a readSome operation
            executeGet(key);
        }
    }
}
