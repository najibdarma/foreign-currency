package com.shopee.foreign.currency.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopee.foreign.currency.entity.ExchangeCurrency;
import com.shopee.foreign.currency.entity.ExchangeCurrencyId;

public interface ExchangeCurrencyIdRepository extends PagingAndSortingRepository<ExchangeCurrencyId, ExchangeCurrency>{
		
}
