package com.shopee.foreign.currency.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopee.foreign.currency.dto.DailyExchangeRateWithVariance;
import com.shopee.foreign.currency.dto.WeeklyExchangeRate;
import com.shopee.foreign.currency.entity.DailyExchangeRate;
import com.shopee.foreign.currency.entity.ExchangeCurrencyId;
import com.shopee.foreign.currency.service.ForeignCurrencyService;

@RestController
@RequestMapping("/foreign-currency")
public class ForeignCurrencyController {
	@Autowired
	private ForeignCurrencyService foreignCurrencyService;
	
	@PostMapping("/exchange")
	public ExchangeCurrencyId save(@RequestBody ExchangeCurrencyId exchangeCurrencyId) {
		return foreignCurrencyService.saveExchangeCurrency(exchangeCurrencyId);
	}

	@GetMapping("/exchange")
	public List<ExchangeCurrencyId> getAllExchangeCurrency(){
		return foreignCurrencyService.getAllExchangeCurrency();
	}
	
	@DeleteMapping("/exchange")
	public void deleteExchangeCurrency(ExchangeCurrencyId exchangeCurrencyId) {
		foreignCurrencyService.deleteExchangeCurrency(exchangeCurrencyId);
	}
	
	@PostMapping("/rate/daily")
	public DailyExchangeRate save(@RequestBody DailyExchangeRate dailyExchangeRate) {
		return foreignCurrencyService.saveDailyExchangeRate(dailyExchangeRate);
	}
	
	@GetMapping("/rate/weekly/{date}")
	public List<WeeklyExchangeRate> getLast7Days(@PathVariable Date date){
		return foreignCurrencyService.getAllDailyExchangeCurrencyFromLast7Days(date);
	}
	
	@GetMapping("/rate/last-7-record")
	public DailyExchangeRateWithVariance getDailyExchangeRateWithVariance(ExchangeCurrencyId exchangeCurrencyId){
		return foreignCurrencyService.getDailyExchangeCurrencyFromLast7Records(exchangeCurrencyId);
	}
}
