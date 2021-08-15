package core.fetcher;

import core.model.CurrencyRate;
import core.model.SingleCurrencyRate;
import core.service.CurrenciesCaller;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class    CurrencyRateFetcher {

    private final int taskIntervalInSeconds;
    private final ScheduledExecutorService executor;
    private final TreeMap<Long, List<SingleCurrencyRate>> ratesHistory = new TreeMap<>();

    public CurrencyRateFetcher(int taskIntervalInSeconds) {
        this.taskIntervalInSeconds = taskIntervalInSeconds;
        this.executor = Executors.newScheduledThreadPool(1);
        runRatesFetcher();
    }

    public void stop() {
        this.executor.shutdown();
    }

    private void runRatesFetcher() {
        executor.scheduleAtFixedRate(() -> {
            List<SingleCurrencyRate> allCurrenciesRates = CurrenciesCaller.getAllCurrenciesRates();
            ratesHistory.put(System.currentTimeMillis() / 1000, allCurrenciesRates);
        }, 0, taskIntervalInSeconds, TimeUnit.SECONDS);
    }

    public synchronized List<CurrencyRate> filterRates(long fromInSeconds, long toInSeconds) {
        return ratesHistory.entrySet().stream()
                .filter(e -> e.getKey() >= fromInSeconds && e.getKey() <= toInSeconds)
                .map(e -> new CurrencyRate(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    public synchronized List<CurrencyRate> filterRates(long fromInSeconds, long toInSeconds, String baseCode) {
        List<CurrencyRate> filteredRates = filterRates(fromInSeconds, toInSeconds);
        filteredRates.forEach(r -> r.getRates().removeIf(rr -> !rr.getBaseCode().equals(baseCode)));
        return filteredRates;
    }

    public synchronized CurrencyRate getLastRates() {
        Map.Entry<Long, List<SingleCurrencyRate>> ratesEntry = ratesHistory.lastEntry();
        return new CurrencyRate(ratesEntry.getKey(), ratesEntry.getValue());
    }

    public synchronized CurrencyRate getLastRates(String baseCode) {
        Map.Entry<Long, List<SingleCurrencyRate>> ratesEntry = ratesHistory.lastEntry();
        Long timeInSeconds = ratesEntry.getKey();

        List<SingleCurrencyRate> filteredRates = ratesEntry.getValue().stream()
                .filter(scr -> scr.getBaseCode().equals(baseCode))
                .collect(Collectors.toList());

        return new CurrencyRate(timeInSeconds, filteredRates);
    }
}
