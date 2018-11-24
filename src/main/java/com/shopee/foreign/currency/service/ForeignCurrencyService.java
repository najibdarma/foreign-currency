package com.shopee.foreign.currency.service;

import java.sql.Date;
import java.util.List;

import com.shopee.foreign.currency.dto.WeeklyExchangeRate;
import com.shopee.foreign.currency.dto.DailyExchangeRateWithVariance;
import com.shopee.foreign.currency.entity.DailyExchangeRate;
import com.shopee.foreign.currency.entity.ExchangeCurrencyId;

public interface ForeignCurrencyService {
	
	DailyExchangeRate saveDailyExchangeRate(DailyExchangeRate dailyExchangeRate);
	
	ExchangeCurrencyId saveExchangeCurrency(ExchangeCurrencyId exchangeCurrencyId);
	
	DailyExchangeRateWithVariance getDailyExchangeCurrencyFromLast7Records(ExchangeCurrencyId exchangeCurrencyId);
	
	void deleteExchangeCurrency(ExchangeCurrencyId exchangeCurrencyId);
	
	List<ExchangeCurrencyId> getAllExchangeCurrency();
	
	List<WeeklyExchangeRate> getAllDailyExchangeCurrencyFromLast7Days(Date date);
}
