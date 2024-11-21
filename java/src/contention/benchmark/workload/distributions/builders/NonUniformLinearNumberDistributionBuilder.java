package contention.benchmark.workload.distributions.builders;

import contention.benchmark.workload.distributions.abstractions.Distribution;
import contention.benchmark.workload.distributions.abstractions.DistributionBuilder;
import contention.benchmark.workload.distributions.impls.NonUniformLinearNumberDistribution;

import static contention.benchmark.tools.StringFormat.indentedTitleWithData;

public class NonUniformLinearNumberDistributionBuilder implements DistributionBuilder {
    private int range;

    @Override
    public Distribution build(int range) {
        this.range = range;
        return new NonUniformLinearNumberDistribution(range);
    }

    @Override
    public StringBuilder toStringBuilder(int indents) {
        return new StringBuilder()
                .append(indentedTitleWithData("Type", "NonUniformLinearNumber", indents))
                .append(indentedTitleWithData("Max range", range, indents));
    }
}
