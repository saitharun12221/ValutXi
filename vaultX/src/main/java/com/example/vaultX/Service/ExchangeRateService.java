package com.example.vaultX.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
// import static java.rmi.server.LogStream.log;
import java.util.Map;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.vaultX.entity.Wallet;
import com.example.vaultX.enums.CurrencyType;

@Service
public class ExchangeRateService {
    private final RestTemplate restTemplate;
    private final Map<String,CachedRate> rateCache= new HashMap<>();//concurrent is removed
    private static final Long Cache_TTL = 5*10*60; //5min
    private static final String Api_Url = "https://api.exchangerate-api.com/v4/latest/";
    public ExchangeRateService(){
        this.restTemplate=restTemplate;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); //5sec
        factory.setReadTimeout(10000);//10sec
        this.restTemplate.setRequestfactory(factory);
    }
    public BigDecimal getExchangeRate(CurrencyType from, CurrencyType to){
        if (from==to){
            return BigDecimal.ONE;
        }
        String CacheKey = from.name()+'_'+to.name();
        CachedRate cached = rateCache.get(CacheKey);
        if (cached!=null && !cached.isExperied()){
            log.debug ("Returning Cached Rate for {} to {}",from,to);
            return cached.getrate();
        }
        //fetch from api
        BigDecimal rate = fetchFromExchangeRateApi(from,to);
        //cache
        rateCache.put(CacheKey, new CachedRate(rate,System.currentTimeMillis()));//system.currentTimeMills()
        return rate;
    }
    private BigDecimal fetchFromExchangeRateApi(CurrencyType from, CurrencyType to){
        String url = Api_Url + from.name();
        log.info("Fetching exchangerate from : {} ",url);
        ExchangeRateApiResponse response = restTemplate.getForObject(url, ExchangeRateApiResponse.class);
        try {
            if (response == null || response.getrate()==null){
                throw new RuntimeException("Invalid response from Exchangerate");
            }
            Double rate = response.getrate().get(to.name());
            if (rate==null){
                log.warn("Rate not found for {} to {},using FallBack",from,to);
                return getFallbackrate(from,to);
            }
            return BigDecimal.valueOf(rate);
            
        } catch (HttpClientErrorException e) {
            log.error("Http error feteching rate",e.getStatusCode());
            return getFallback(from,to);
        }
    }
    public ExchangeRateApiResponse getAllRates(CurrencyType base){
        String url = Api_Url + base.name();
        try {
            ExchangeRateApiResponse response = restTemplate.getForObject(url,ExchangeRateApiResponse.class);
            if (response!=null){
                CacheallRates(base,response.getrates());
            }
            return response;

        } catch (Exception e) {
            log.error("Failed to fetch all rates for Base Currency {}",base,e);
            return createFallBackresponse(base);
        }
    }
    private void CacheallRates(CurrencyType base, Map<String,Double> rates){
        for (Map.Entry<String,Double> entry:rates.entrySet()){
            try{
                CurrencyType to = CurrencyType.valueOf(entry.getKey());
                String CacheKey = base.name()+'_'+to.name();
                BigDecimal rate = BigDecimal.valueof(entry.getValue());
                rateCache.put(CacheKey,new CachedRate(rate,System.currentTimeMillis()));
            }
            catch(IllegalArgumentException e){
                log.debug ("skipping caching for unsupported currency: {}",entry.getKey());
            }
        }
    }
    public BigDecimal convertCurrency(BigDecimal amount,CurrencyType base, CurrencyType to){
        BigDecimal rate =  getExchangeRate(base, to);
        return amount.multiply(rate).setScale(8,RoundingMode.HALF_UP);//RoundingModeHalf_up
    }
    public Map<String,Object> getWalletinMultipleCurrencies(Wallet wallet, List<CurrencyType> targetCurrencies){
        Map<String,Object> results = new HashMap<>();
        //original currency
        results.put("orignial",Map.of (
            "Currency",wallet.getCurrency(),"Symbol",wallet.getCurrency().getsymbol(),"Balance",wallet.getbalance())
        );
        List<Map<String,Object>> conversion = new ArrayList<>();
        for (CurrencyType target : targetCurrencies){
            if (target! = wallet.getCurrency()){
                BigDecimal convertedamount = convertCurrency(wallet.getbalance(),wallet.getCurrency(),target);
                BigDecimal rate = getExchangeRate(wallet.getCurrency(), conversion.add(Map.of (
                    "currency",target,"symbol",target.getsymbol(),"balance",convertedamount
                )))
                results.put("conversions",conversion);
                return results;
            }
        }
        // ExchangeRateApiResponse CachedRate
    }
    public BigDecimal getfallbackrate(CurrencyType from, CurrencyType to){
        Map<String,BigDecimal> fallbackrates = new HashMap<>();
        fallbackrates.put("USD_EUR",new BigDecimal("0.85"));
        fallbackrates.put("USD_INR",new BigDecimal("90.50"));
        fallbackrates.put("EUR_USD",new BigDecimal("1.18"));
        fallbackrates.put("EUR_INR",new BigDecimal("105.08"));
        fallbackrates.put("INR_USD",new BigDecimal("0.011"));
        fallbackrates.put("INR_EUR",new BigDecimal("0.0095"));
        fallbackrates.put("USD_BTC",new BigDecimal("0.000022"));
        fallbackrates.put("USD_LTC",new BigDecimal("0.013"));
        fallbackrates.put("BTC_USD",new BigDecimal("45000.00"));
        fallbackrates.put("LTC_USD",new BigDecimal("150.00"));
        fallbackrates.put("BTC_LTC",new BigDecimal("1122.70"));
        fallbackrates.put("LTC_BTC",new BigDecimal("0.00089"));
        String key = from.name()+"_" + to.name();
        if (fallbackrates.containsKey(key)){
            log.warn("Using fallbackrate for {} to {}",from,to,fallbackrates.get(key));
            return fallbackrates.get(key);
        }
        String reversekey = to.name()+"_"+from.name();
        if (fallbackrates.containsKey(reversekey)){
            BigDecimal reverserate =fallbackrates.get(reversekey);
            BigDecimal rate = BigDecimal.ONE.divide(reverserate,8,RoundingMode.HALF_UP);
            log.warn ("Using inverse fallbackrate for {} to {}",to ,from,rate);
            return rate;
        }
        log.error("No fallBack rate found for {} to {},using 1.0",from,to);
        return BigDecimal.ONE;
    }
    public ExchangeRateApiResponse setfallbackresponse(CurrencyType base){
        ExchangeRateApiResponse response = new ExchangeRateApiResponse();
        response.setbase("base Currency",base currency.name());
    }
}
