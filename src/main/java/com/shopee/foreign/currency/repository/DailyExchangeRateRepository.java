package com.shopee.foreign.currency.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopee.foreign.currency.entity.DailyExchangeRate;
import com.shopee.foreign.currency.entity.DailyExchangeRateKey;
import com.shopee.foreign.currency.entity.ExchangeCurrencyId;

public interface DailyExchangeRateRepository extends PagingAndSortingRepository<DailyExchangeRate, DailyExchangeRateKey>{
	void deleteByDailyExchangeRateKeyExchangeCurrencyId(ExchangeCurrencyId exchangeCurrencyId);
	
	Page<DailyExchangeRate> findByDailyExchangeRateKeyExchangeCurrencyId(ExchangeCurrencyId exchangeCurrencyId, Pageable pageable);
}
