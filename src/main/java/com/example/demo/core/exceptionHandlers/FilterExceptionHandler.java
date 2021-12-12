package com.example.demo.core.exceptionHandlers;

import com.example.demo.core.exceptions.HttpException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilterExceptionHandler implements ErrorWebExceptionHandler {
  	private final DataBufferWriter bufferWriter;
  	@Override
  	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
  		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    	String appError = ex.getMessage();
    	if (ex instanceof HttpException) {
    	    status = ((HttpException) ex).getCode();
			log.warn(String.format("%s , StackTrace: %s", appError, ex.getStackTrace().toString()));
    	} else {
    	    log.error(appError, ex);
    	}
    	if (exchange.getResponse().isCommitted()) {
    	    return Mono.error(ex);
    	}
    	exchange.getResponse().setStatusCode(HttpStatus.valueOf(status));
    	return bufferWriter.write(exchange.getResponse(), appError);
  	}
}