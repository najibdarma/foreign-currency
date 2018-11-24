package com.shopee.foreign.currency.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exchange_currency")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExchangeCurrencyId implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3563262320321965398L;
	
	@EmbeddedId
	private ExchangeCurrency exchangeCurrency;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exchangeCurrency == null) ? 0 : exchangeCurrency.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		ExchangeCurrencyId other = (ExchangeCurrencyId) obj;
		if (exchangeCurrency == null) {
			if (other.exchangeCurrency != null)
				return false;
		} else if (!exchangeCurrency.equals(other.exchangeCurrency)) {
			return false;
		}
		
		return true;
	}
	
}
