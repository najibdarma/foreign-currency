package com.shopee.foreign.currency.service.implementaiton;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopee.foreign.currency.dto.DailyExchangeRateWithVariance;
import com.shopee.foreign.currency.dto.Rate;
import com.shopee.foreign.currency.dto.WeeklyExchangeRate;
import com.shopee.foreign.currency.entity.DailyExchangeRate;
import com.shopee.foreign.currency.entity.ExchangeCurrency;
import com.shopee.foreign.currency.entity.ExchangeCurrencyId;
import com.shopee.foreign.currency.repository.DailyExchangeRateRepository;
import com.shopee.foreign.currency.repository.ExchangeCurrencyIdRepository;
import com.shopee.foreign.currency.service.ForeignCurrencyService;

@Service("ForeignCurrencyService")
@Transactional(readOnly = true)
public class ForeignCurrencyServiceImpl implements ForeignCurrencyService {

	@Autowired
	private DailyExchangeRateRepository dailyExchangeRateRepository;
	
	@Autowired
	private ExchangeCurrencyIdRepository exchangeCurrencyIdRepository;
	
	@Override
	public List<ExchangeCurrencyId> getAllExchangeCurrency() {
		return (List<ExchangeCurrencyId>) exchangeCurrencyIdRepository.findAll();
	}

	@Override
	public DailyExchangeRateWithVariance getDailyExchangeCurrencyFromLast7Records(ExchangeCurrencyId exchangeCurrencyId) {
		Sort sort = new Sort(Direction.DESC, "dailyExchangeRateKey.date");
		Pageable pageable = PageRequest.of(0, 7, sort);
		Page<DailyExchangeRate> page = dailyExchangeRateRepository.findByDailyExchangeRateKeyExchangeCurrencyId(exchangeCurrencyId, pageable);
		List<Rate> rates = new ArrayList<>();
		if(page.getTotalElements() > 0) {
			List<DailyExchangeRate> dailyExchangeRates = page.getContent();
			Iterator<DailyExchangeRate> i = dailyExchangeRates.iterator();			
			DailyExchangeRate dailyExchangeRate = i.next();
			double rate = dailyExchangeRate.getRate();
			rates.add(new Rate(rate, dailyExchangeRate.getDailyExchangeRateKey().getDate()));
			double min = rate;
			double max = rate;
			double sum = rate;
			while(i.hasNext()) {
				dailyExchangeRate = i.next();
				rates.add(new Rate(rate, dailyExchangeRate.getDailyExchangeRateKey().getDate()));
				rate = dailyExchangeRate.getRate();
				sum += rate;
				if(min > rate)
					min = rate;
				if(max < rate)
					max = rate;
			}
			return new DailyExchangeRateWithVariance(exchangeCurrencyId.getExchangeCurrency(), max-min, sum/dailyExchangeRates.size(), rates);
		} else {
			throw new ResourceNotFoundException();
		}
	}

	@Override
	@Transactional(readOnly = false)
	public DailyExchangeRate saveDailyExchangeRate(DailyExchangeRate dailyExchangeRate) {
		return dailyExchangeRateRepository.save(dailyExchangeRate);
	}

	@Override
	@Transactional(readOnly = false)
	public ExchangeCurrencyId saveExchangeCurrency(ExchangeCurrencyId exchangeCurrencyId) {
		return exchangeCurrencyIdRepository.save(exchangeCurrencyId);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteExchangeCurrency(ExchangeCurrencyId exchangeCurrencyId) {
		dailyExchangeRateRepository.deleteByDailyExchangeRateKeyExchangeCurrencyId(exchangeCurrencyId);
		exchangeCurrencyIdRepository.delete(exchangeCurrencyId);
	}

	@Override
	public List<WeeklyExchangeRate> getAllDailyExchangeCurrencyFromLast7Days(Date date) {
		List<DailyExchangeRate> dailyExchangeRates = (List<DailyExchangeRate>) dailyExchangeRateRepository.findAll();
		Map<ExchangeCurrencyId, List<DailyExchangeRate>> filteredDailyExchangeRates = this.filterByExchangeCurrencyAndDate(dailyExchangeRates, date);
		List<WeeklyExchangeRate> weeklyExchangeRates = new ArrayList<>();
		
		filteredDailyExchangeRates.entrySet().stream().forEach(x -> {
			List<DailyExchangeRate> dailyExchangeRate = x.getValue();
			int size = dailyExchangeRate.size();
			boolean isSufficient = size == 7;
			weeklyExchangeRates.add(new WeeklyExchangeRate(x.getKey().getExchangeCurrency().getCurrencyFrom(), x.getKey().getExchangeCurrency().getCurrencyTo(), 
					isSufficient ? this.getRateByDate(dailyExchangeRates, date) : Double.NaN, isSufficient ? this.sumRate(dailyExchangeRate)/size : Double.NaN));
		});
		
		return weeklyExchangeRates;
	}

	private Map<ExchangeCurrencyId, List<DailyExchangeRate>> filterByExchangeCurrencyAndDate(List<DailyExchangeRate> dailyExchangeRates, Date date){
		Date startDate = Date.valueOf(date.toLocalDate().minusDays(7));
		
		Map<ExchangeCurrencyId, List<DailyExchangeRate>> group = dailyExchangeRates.stream().collect(Collectors.groupingBy(
			a ->
				new ExchangeCurrencyId(
					new ExchangeCurrency(a.getDailyExchangeRateKey().getExchangeCurrencyId().getExchangeCurrency().getCurrencyFrom(), 
							a.getDailyExchangeRateKey().getExchangeCurrencyId().getExchangeCurrency().getCurrencyTo()))
				));
		
		long endTime = date.getTime();
		long startTime = startDate.getTime();
		group.values().stream().forEach(listDailyExchangeRate -> 
			listDailyExchangeRate.retainAll(
					listDailyExchangeRate.stream().filter(y -> {
						long b = y.getDailyExchangeRateKey().getDate().getTime();
						return b <= endTime && b > startTime;
					}).collect(Collectors.toList()))
		);
		
		return group;
	}
	
	private double getRateByDate(List<DailyExchangeRate> dailyExchangeRates, Date date) {
		List<DailyExchangeRate> latestRate = dailyExchangeRates.stream().filter(x -> x.getDailyExchangeRateKey().getDate().equals(date)).collect(Collectors.toList());
		return latestRate.get(0).getRate();
	}
	
	private double sumRate(List<DailyExchangeRate> dailyExchangeRates) {
		double sum = 0.0;
		for(DailyExchangeRate dailyExchangeRate: dailyExchangeRates) {
			sum+=dailyExchangeRate.getRate();
		}
		return sum;
	}
}
