package org.springsource.restbucks.order.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Oliver Gierke
 */
@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class OrderAlreadyPaidException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}
