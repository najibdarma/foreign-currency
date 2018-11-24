package com.shopee.foreign.currency.dto;

import java.util.List;

import com.shopee.foreign.currency.entity.ExchangeCurrency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DailyExchangeRateWithVariance {
	private ExchangeCurrency exchangeCurrency;
	private double variance;
	private double avg;
	private List<Rate> rate;
}
