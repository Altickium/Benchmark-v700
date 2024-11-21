package contention.benchmark.workload.distributions.impls;

import contention.benchmark.workload.distributions.abstractions.Distribution;

public class NonUniformLinearNumberDistribution implements Distribution {

    private final int maxRange;
    private int cur = 0;

    public NonUniformLinearNumberDistribution(int range){
        this.maxRange = range;
    }

    @Override
    public int next() {
        if (cur == maxRange) {
            cur = 0;
        }
        return cur++;
    }
}
