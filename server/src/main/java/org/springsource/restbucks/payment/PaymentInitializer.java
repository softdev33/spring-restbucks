/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springsource.restbucks.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Month;
import java.time.Year;

import javax.annotation.PostConstruct;

import org.javamoney.moneta.Money;
import org.jmolecules.event.annotation.DomainEventHandler;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springsource.restbucks.core.Currencies;
import org.springsource.restbucks.order.LineItem;
import org.springsource.restbucks.order.Order;
import org.springsource.restbucks.order.OrderRepository;

/**
 * Initializing component to create a default {@link CreditCard} in the system.
 *
 * @author Oliver Gierke
 */
@Service
@Slf4j
@RequiredArgsConstructor
class PaymentInitializer {

	private final PaymentService payment;
	private final OrderRepository orders;
	private final CreditCardRepository creditCards;

	@PostConstruct
	public void init() {

		if (creditCards.count() > 0) {
			return;
		}

		CreditCardNumber number = CreditCardNumber.of("1234123412341234");
		CreditCard creditCard = new CreditCard(number, "Oliver Gierke", Month.DECEMBER, Year.of(2099));

		creditCard = creditCards.save(creditCard);

		LOG.info("Credit card {} created!", creditCard);
	}

	@Transactional
	@DomainEventHandler
	public void onStartup(ApplicationReadyEvent event) {

		Order order = orders.save(new Order(new LineItem("Something", Money.of(47.11, Currencies.EURO))));
		payment.pay(order, creditCards.findAll().iterator().next().getNumber());
	}
}
