package core.service;

import core.fetcher.CurrencyRateFetcher;
import core.model.CurrencyRate;
import core.model.SingleCurrencyRate;
import core.util.TimeUtil;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


public class CurrenciesRatesService {

    private final CurrencyRateFetcher ratesFetcher;

    public CurrenciesRatesService(int ratesFetchIntervalInSeconds) {
        this.ratesFetcher = new CurrencyRateFetcher(ratesFetchIntervalInSeconds);
    }

    public void printRealtimeRates() throws IOException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            CurrencyRate lastRates = ratesFetcher.getLastRates();
            printCurrencyRates(lastRates.getTimeInSeconds(), lastRates.getRates());
            System.out.println("Click any to exit...");
        }, 0, 5, TimeUnit.SECONDS);

        System.in.read();
        executor.shutdown();
    }

    public void printFilteredRates(long from, long to) {
        printFilteredRates(from, to, "");
    }

    public void printFilteredRates(long from, long to, String baseCode) {
        List<CurrencyRate> currencyRates = baseCode.isEmpty() ?
                ratesFetcher.filterRates(from, to) : ratesFetcher.filterRates(from, to, baseCode);

        currencyRates.forEach(cr -> printCurrencyRates(cr.getTimeInSeconds(), cr.getRates()));
    }

    private static void printCurrencyRates(long time, List<SingleCurrencyRate> allCurrenciesRates) {
        final AtomicReference<String> lastBaseCode = new AtomicReference<>("");
        StringBuilder sb = new StringBuilder();
        sb.append("|------CURRENCIES------|\n");
        sb.append("|-").append(TimeUtil.formatTime(time)).append("--|\n");
        sb.append("|------").append(time).append("-------|\n");

        allCurrenciesRates.forEach((cr -> {
            if (!cr.getBaseCode().equals(lastBaseCode.get()) || lastBaseCode.get().isEmpty())
                sb.append("|----------------------|\n");

            sb.append(" ").append(cr.getBaseCode()).append("  /  ").append(cr.getQuoteCode())
                    .append("  =  ").append(cr.getRate()).append("\n");

            lastBaseCode.set(cr.getBaseCode());
        }));
        sb.append("|----------------------|");
        System.out.println(System.lineSeparator().repeat(4));
        System.out.println(sb);
    }

    public void stop() {
        ratesFetcher.stop();
    }
}
