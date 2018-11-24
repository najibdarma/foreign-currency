package com.shopee.foreign.currency.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class DailyExchangeRateKey implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4268942380040267005L;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumns({@JoinColumn(name = "currencyFrom"), @JoinColumn(name = "currencyTo")})
	private ExchangeCurrencyId exchangeCurrencyId;
	
	private Date date;
}