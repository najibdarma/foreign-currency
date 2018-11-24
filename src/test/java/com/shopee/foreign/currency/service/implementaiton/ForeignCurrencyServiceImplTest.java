package com.shopee.foreign.currency.service.implementaiton;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.shopee.foreign.currency.dto.DailyExchangeRateWithVariance;
import com.shopee.foreign.currency.dto.WeeklyExchangeRate;
import com.shopee.foreign.currency.entity.DailyExchangeRate;
import com.shopee.foreign.currency.entity.DailyExchangeRateKey;
import com.shopee.foreign.currency.entity.ExchangeCurrency;
import com.shopee.foreign.currency.entity.ExchangeCurrencyId;
import com.shopee.foreign.currency.repository.DailyExchangeRateRepository;
import com.shopee.foreign.currency.repository.ExchangeCurrencyIdRepository;

@RunWith(MockitoJUnitRunner.class)
public class ForeignCurrencyServiceImplTest {
	@Mock
	private DailyExchangeRateRepository dailyExchangeRateRepository;
	@Mock
	private ExchangeCurrencyIdRepository exchangeCurrencyIdRepository;
	@InjectMocks
	private ForeignCurrencyServiceImpl foreignCurrencyServiceImpl;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldCallFindAll_whenGetAllExchangeCurrency() {
		foreignCurrencyServiceImpl.getAllExchangeCurrency();
		
		Mockito.verify(exchangeCurrencyIdRepository).findAll();
	}

	@Test
	public void shouldCallSave_whenSaveDailyExchange() {
		DailyExchangeRate dailyExchangeRate = new DailyExchangeRate();
		
		foreignCurrencyServiceImpl.saveDailyExchangeRate(dailyExchangeRate);
		
		Mockito.verify(dailyExchangeRateRepository).save(dailyExchangeRate);
	}

	@Test
	public void shouldCallSave_whenSaveExchangeCurrency() {
		ExchangeCurrencyId exchangeCurrencyId = new ExchangeCurrencyId();
		
		foreignCurrencyServiceImpl.saveExchangeCurrency(exchangeCurrencyId);
		
		Mockito.verify(exchangeCurrencyIdRepository).save(exchangeCurrencyId);
	}

	@Test
	public void shouldCallDeleteDaily_andDeleteExchange_whenDeleteExchangeCurrency() {
		ExchangeCurrencyId exchangeCurrencyId = new ExchangeCurrencyId();
		
		foreignCurrencyServiceImpl.deleteExchangeCurrency(exchangeCurrencyId);
		
		Mockito.verify(dailyExchangeRateRepository).deleteByDailyExchangeRateKeyExchangeCurrencyId(exchangeCurrencyId);
		Mockito.verify(exchangeCurrencyIdRepository).delete(exchangeCurrencyId);
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void shouldThrowResourceNotFoundException_whenGetDailyExchangeCurrencyFromLast7Records() {
		ExchangeCurrencyId exchangeCurrencyId = new ExchangeCurrencyId();
		List<DailyExchangeRate> content = new ArrayList<>();
		Page<DailyExchangeRate> page = new PageImpl<>(content);
		
		Mockito.when(dailyExchangeRateRepository.findByDailyExchangeRateKeyExchangeCurrencyId(
				ArgumentMatchers.eq(exchangeCurrencyId), ArgumentMatchers.any(Pageable.class))).thenReturn(page);
		foreignCurrencyServiceImpl.getDailyExchangeCurrencyFromLast7Records(exchangeCurrencyId);
	}
	
	@Test
	public void shouldReturnDailyExchangeRateWithVariance_whenGetDailyExchangeCurrencyFromLast7Records() {
		// given
		ExchangeCurrencyId exchangeCurrencyId = new ExchangeCurrencyId(new ExchangeCurrency("USD", "IDR"));
		List<DailyExchangeRate> content = new ArrayList<>();
		content.add(new DailyExchangeRate(new DailyExchangeRateKey(exchangeCurrencyId, Date.valueOf("2018-11-21")), 1.70));
		for(int i=6; i>1; i--) {
			content.add(new DailyExchangeRate(new DailyExchangeRateKey(exchangeCurrencyId, Date.valueOf("2018-11-2"+i)), 0.70+i));
		}
		content.add(new DailyExchangeRate(new DailyExchangeRateKey(exchangeCurrencyId, Date.valueOf("2018-11-27")), 0.77));
		Page<DailyExchangeRate> page = new PageImpl<>(content);
		
		// when
		Mockito.when(dailyExchangeRateRepository.findByDailyExchangeRateKeyExchangeCurrencyId(
				ArgumentMatchers.eq(exchangeCurrencyId), ArgumentMatchers.any(Pageable.class))).thenReturn(page);
		DailyExchangeRateWithVariance actual = foreignCurrencyServiceImpl.getDailyExchangeCurrencyFromLast7Records(exchangeCurrencyId);
		
		// then
		assertEquals(6.0, actual.getVariance(),3);
		assertEquals(3.33, actual.getAvg(),3);
		assertEquals("USD", actual.getExchangeCurrency().getCurrencyFrom());
		assertEquals("IDR", actual.getExchangeCurrency().getCurrencyTo());
		assertEquals(7, actual.getRate().size());
	}
	
	@Test
	public void shouldReturnWeeklyExchangeRates_whenGetAllDailyExchangeCurrencyFromLast7days() {
		// given
		ExchangeCurrencyId exchangeCurrencyId = new ExchangeCurrencyId(new ExchangeCurrency("USD", "IDR"));
		List<DailyExchangeRate> content = new ArrayList<>();
		for(int i=9; i>=0; i--) {
			content.add(new DailyExchangeRate(new DailyExchangeRateKey(exchangeCurrencyId, Date.valueOf("2018-11-2"+i)), 0.70+i));
		}

		ExchangeCurrencyId exchangeCurrencyId2 = new ExchangeCurrencyId(new ExchangeCurrency("SSD", "IDR"));
		for(int i=4; i>=0; i--) {
			content.add(new DailyExchangeRate(new DailyExchangeRateKey(exchangeCurrencyId2, Date.valueOf("2018-11-2"+i)), 0.70+i));
		}
		
		// when
		Mockito.when(dailyExchangeRateRepository.findAll()).thenReturn(content);
		List<WeeklyExchangeRate> actual = foreignCurrencyServiceImpl.getAllDailyExchangeCurrencyFromLast7Days(Date.valueOf("2018-11-27"));
		
		// then
		assertEquals("USD", actual.get(0).getCurrencyFrom());
		assertEquals("IDR", actual.get(0).getCurrencyTo());
		assertEquals(7.70, actual.get(0).getLatestRate(),3);
		assertEquals(4.70, actual.get(0).getAvg(),3);
		assertEquals(2, actual.size());
	}
}
