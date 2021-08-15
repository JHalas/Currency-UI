package core.model;

import java.util.List;

public class CurrencyRate {
    private long timeInSeconds;
    private List<SingleCurrencyRate> rates;

    public CurrencyRate(long timeInSeconds, List<SingleCurrencyRate> rates) {
        this.timeInSeconds = timeInSeconds;
        this.rates = rates;
    }

    public long getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(long timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public List<SingleCurrencyRate> getRates() {
        return rates;
    }

    public void setRates(List<SingleCurrencyRate> rates) {
        this.rates = rates;
    }
}
