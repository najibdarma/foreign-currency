package com.shopee.foreign.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeeklyExchangeRate {
	private String currencyFrom;
	private String currencyTo;
	private double latestRate;
	private double avg;
}
