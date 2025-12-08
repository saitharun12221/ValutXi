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
        //conversions ExchangeRateApiResponse CachedRate
    }
}
