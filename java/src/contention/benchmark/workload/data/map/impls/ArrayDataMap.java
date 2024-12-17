package contention.benchmark.workload.data.map.impls;

import contention.benchmark.workload.data.map.abstractions.DataMap;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArrayDataMap implements DataMap {
    protected final int[] data;

    public ArrayDataMap(List<Integer> dataList) {
        data = dataList.stream().mapToInt(Integer::intValue).toArray();
    }

    public int get(int index) {
        return data[index];
    }
}
