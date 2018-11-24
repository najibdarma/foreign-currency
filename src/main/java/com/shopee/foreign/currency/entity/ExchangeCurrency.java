package com.shopee.foreign.currency.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class ExchangeCurrency implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -977707312486363628L;
	
	private String currencyFrom;
	private String currencyTo;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currencyFrom == null) ? 0 : currencyFrom.hashCode());
		result = prime * result + ((currencyTo == null) ? 0 : currencyTo.hashCode());
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
		
		ExchangeCurrency other = (ExchangeCurrency) obj;
		if (currencyFrom == null) {
			if (other.currencyFrom != null)
				return false;
		} else if (!currencyFrom.equals(other.currencyFrom)) {
			return false;
		}
		
		if (currencyTo == null) {
			if (other.currencyTo != null)
				return false;
		} else if (!currencyTo.equals(other.currencyTo)) {
			return false;
		}
		
		return true;
	}
}
