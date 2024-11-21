package contention.benchmark.workload.args.generators.builders;

import contention.benchmark.workload.args.generators.abstractions.ArgsGenerator;
import contention.benchmark.workload.args.generators.abstractions.ArgsGeneratorBuilder;
import contention.benchmark.workload.args.generators.impls.DefaultArgsGenerator;
import contention.benchmark.workload.data.map.abstractions.DataMapBuilder;
import contention.benchmark.workload.data.map.builders.IdDataMapBuilder;
import contention.benchmark.workload.distributions.abstractions.DistributionBuilder;
import contention.benchmark.workload.distributions.builders.NonUniformLinearNumberDistributionBuilder;

import static contention.benchmark.tools.StringFormat.indentedTitle;
import static contention.benchmark.tools.StringFormat.indentedTitleWithData;

public class CycleIdArgsGeneratorBuilder implements ArgsGeneratorBuilder {
    public DistributionBuilder distributionBuilder = new NonUniformLinearNumberDistributionBuilder();
    public DataMapBuilder dataMapBuilder = new IdDataMapBuilder();
    transient public int range;

    public CycleIdArgsGeneratorBuilder setDistributionBuilder(DistributionBuilder distributionBuilder) {
        this.distributionBuilder = distributionBuilder;
        return this;
    }

    public CycleIdArgsGeneratorBuilder setDataMapBuilder(DataMapBuilder dataMapBuilder) {
        this.dataMapBuilder = dataMapBuilder;
        return this;
    }

    @Override
    public ArgsGeneratorBuilder init(int range) {
        this.range = range;
        return this;
    }

    @Override
    public ArgsGenerator build() {
        return new DefaultArgsGenerator(
                dataMapBuilder.getOrBuild(),
                distributionBuilder.build(range)
        );
    }

    @Override
    public StringBuilder toStringBuilder(int indents) {
        return new StringBuilder()
                .append(indentedTitleWithData("Type", "CycleDefault", indents))
                .append(indentedTitle("Distribution", indents))
                .append(distributionBuilder.toStringBuilder(indents + 1))
                .append(indentedTitle("DataMap", indents))
                .append(dataMapBuilder.toStringBuilder(indents + 1));
    }
}
