package core.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.model.SingleCurrencyRate;
import core.util.RestUtil;

import java.lang.reflect.Type;
import java.util.List;

public class CurrenciesCaller {

    private static final String GET_ALL_CURRENCIES_RATES_URL = "http://localhost:8080/rate/all";
    private static final Gson gson = new Gson();
    private static final Type type = new TypeToken<List<SingleCurrencyRate>>() {
    }.getType();

    private CurrenciesCaller() {
    }

    public static List<SingleCurrencyRate> getAllCurrenciesRates() {
        String responseJson = RestUtil.get(GET_ALL_CURRENCIES_RATES_URL);
        return gson.fromJson(responseJson, type);
    }

}
