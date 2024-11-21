package contention.benchmark.workload.thread.loops.builders;

import contention.abstractions.DataStructure;
import contention.benchmark.workload.args.generators.abstractions.ArgsGeneratorBuilder;
import contention.benchmark.workload.args.generators.builders.DefaultArgsGeneratorBuilder;
import contention.benchmark.workload.stop.condition.StopCondition;
import contention.benchmark.workload.thread.loops.abstractions.ThreadLoopBuilder;
import contention.benchmark.workload.thread.loops.impls.DefaultThreadLoop;
import contention.benchmark.workload.thread.loops.impls.MultiDefaultThreadLoop;
import contention.benchmark.workload.thread.loops.parameters.RatioThreadLoopParameters;

import java.lang.reflect.Method;

import static contention.benchmark.tools.StringFormat.indentedTitle;
import static contention.benchmark.tools.StringFormat.indentedTitleWithData;

public class CycleThreadLoopBuilder extends ThreadLoopBuilder {
    public int paramSize;
    public RatioThreadLoopParameters[] parameters;

    public ArgsGeneratorBuilder argsGeneratorBuilder = new DefaultArgsGeneratorBuilder();

    public CycleThreadLoopBuilder setRatios(int index, RatioThreadLoopParameters ratio) {
        assert (index < paramSize);

        parameters[index] = ratio;
        return this;
    }

    public CycleThreadLoopBuilder setWriteRatio(double writeRatio, int i) {
        parameters[i].setWriteRatio(writeRatio);
        return this;
    }

    public CycleThreadLoopBuilder setInsertRatio(double insertRatio, int i) {
        parameters[i].insertRatio = insertRatio;
        return this;
    }

    public CycleThreadLoopBuilder setRemoveRatio(double removeRatio, int i) {
        parameters[i].removeRatio = removeRatio;
        return this;
    }

    public CycleThreadLoopBuilder setWriteAllsRatio(double writeAllsRatio, int i) {
        parameters[i].writeAllsRatio = writeAllsRatio;
        return this;
    }

    public CycleThreadLoopBuilder setSnapshotsRatio(double snapshotsRatio, int i) {
        parameters[i].snapshotsRatio = snapshotsRatio;
        return this;
    }

    public CycleThreadLoopBuilder setArgsGeneratorBuilder(ArgsGeneratorBuilder argsGeneratorBuilder) {
        this.argsGeneratorBuilder = argsGeneratorBuilder;
        return this;
    }

    public CycleThreadLoopBuilder setParamSize(int paramSize) {
        parameters = new RatioThreadLoopParameters [paramSize];
        this.paramSize = paramSize;
        return this;
    }

    @Override
    public CycleThreadLoopBuilder init(int range) {
        argsGeneratorBuilder.init(range);
        return this;
    }

    @Override
    public MultiDefaultThreadLoop build(int threadId, DataStructure<Integer> dataStructure,
                                        Method[] methods, StopCondition stopCondition) {
        return new MultiDefaultThreadLoop(threadId, dataStructure, methods, stopCondition,
                parameters, argsGeneratorBuilder.build(), paramSize);
    }

    @Override
    public StringBuilder toStringBuilder(int indents) {
        return new StringBuilder()
                .append(indentedTitleWithData("Type", "Cycle", indents))
                .append(indentedTitle("ArgsGenerator", indents))
                .append(argsGeneratorBuilder.toStringBuilder(indents + 1));
    }
}
