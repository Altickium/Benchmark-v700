package contention.benchmark.workload.data.map.abstractions;

public abstract class DataMapBuilder {
    private static int idCounter = 0;
    public int id = idCounter++;

    public abstract DataMapBuilder init(int range);
    public abstract DataMap build();

    public StringBuilder toStringBuilder() {
        return toStringBuilder(1);
    }

    public abstract StringBuilder toStringBuilder(int indents);

    private DataMap dataMap;

    public DataMap getOrBuild() {
        if (dataMap == null)
            dataMap = this.build();
        return dataMap;
    }

    public void reset() {
        dataMap = null;
    }
}
