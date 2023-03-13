package contention.benchmark.keygenerators.parameters;

import contention.abstractions.*;
import contention.benchmark.distributions.parameters.ZipfParameters;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * старички + волна
 * n — g — gx — gk — cp — ca
 * n — количество элементов
 * g (grand) — процент старичков
 * // 100% - g — процент новичков
 * gy — вероятность их вызова
 * // 100% - gx — вероятность вызова новичков
 * gk — на сколько стары наши старички
 * | преподсчёт - перед началом теста делаем gk количество запросов к старичкам
 * cp (child) — распределение вызовов среди новичков
 * // по умолчанию Zipf 1
 * // при желании можно сделать cx — cy
 * ca (child add) — вероятность добавления нового элемента
 * // 100% - gx - ca — чтение новичка, ca добавление нового новичка
 * // при желании можно переработать на "100% - ca — чтение, ca — запись"
 * (то есть брать ca не от общей вероятности, а только при чтении"
 */
public class CreakersAndWaveParameters extends Parameters {
    public double CREAKERS_SIZE = 0;
    public double CREAKERS_PROB = 0;
    public long CREAKERS_AGE = 0;
    public double WAVE_SIZE = 0;
    public DistributionBuilder creakersDistBuilder = new DistributionBuilder();
    public DistributionBuilder waveDistBuilder = new DistributionBuilder(DistributionType.ZIPF)
            .setParameters(new ZipfParameters(1));


    public int creakersLength;
    public int defaultWaveLength;
    public int creakersBegin;
    public AtomicInteger waveBegin;
    public AtomicInteger waveEnd;


    @Override
    public void build() {
        super.build();
        creakersLength = (int) (range * CREAKERS_SIZE);
        creakersBegin = range - creakersLength;
        defaultWaveLength = (int) (range * WAVE_SIZE);
        waveEnd = new AtomicInteger(creakersBegin);
        waveBegin = new AtomicInteger(waveEnd.get() - defaultWaveLength);

        size = (int) (range * this.CREAKERS_SIZE) + (int) (range * this.WAVE_SIZE);
    }

    @Override
    public void parseArg(ParseArgument args) {
        switch (args.getCurrent()) {
            case "-gs", "-cs" -> this.CREAKERS_SIZE = Double.parseDouble(args.getNext());
            case "-gp", "-cp" -> this.CREAKERS_PROB = Double.parseDouble(args.getNext());
            case "-ws" -> this.WAVE_SIZE = Double.parseDouble(args.getNext());
            case "-g-age", "-c-age" -> this.CREAKERS_AGE = Integer.parseInt(args.getNext());
            case "-g-dist", "-c-dist" -> this.creakersDistBuilder.parseDistribution(args);
            case "-w-dist" -> this.waveDistBuilder.parseDistribution(args);
            case "--size", "-i" -> {
                args.getNext();
                System.err.println("CreakersAndWave key generator does not accept prefill size argument. Ignoring...");
            }
            default -> super.parseArg(args);
        }
    }

    @Override
    public StringBuilder toStringBuilder() {
        StringBuilder params = super.toStringBuilder();
        params
//                .append("\n")
//                .append("  Key Generator:           \tCREAKERS_AND_WAVE")
                .append("\n")
                .append("  Creakers size:           \t")
                .append(this.CREAKERS_SIZE)
                .append("\n")
                .append("  Wave size:               \t")
                .append(this.WAVE_SIZE)
                .append("\n")
                .append("  Creakers probability:    \t")
                .append(this.CREAKERS_PROB)
                .append("\n")
                .append("  Creakers age:            \t")
                .append(this.CREAKERS_AGE)
                .append("\n")
                .append("  Creakers distribution:   \t")
                .append(creakersDistBuilder.distributionType)
                .append(creakersDistBuilder.toStringBuilderParameters())
                .append("\n")
                .append("  Wave distribution:       \t")
                .append(waveDistBuilder.distributionType)
                .append(waveDistBuilder.toStringBuilderParameters());

        return params;
    }
}
