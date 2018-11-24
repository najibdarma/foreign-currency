package com.shopee.foreign.currency.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "daily_exchange_rate")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DailyExchangeRate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6713759418280722697L;
	
	@Id
	@EmbeddedId
	private DailyExchangeRateKey dailyExchangeRateKey;
	
	private double rate;
}
