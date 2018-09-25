package br.org.energia.legacy.boot.starter.web;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.slf4j.MDC;

public class CCEEIdHeaderFilter implements Filter, Ordered {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CCEEIdHeaderFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String currentCorrId = httpServletRequest.getHeader(RequestCorrelationId.CORRELATION_ID);

        if (!currentRequestIsAsyncDispatcher(httpServletRequest)) {
            if (currentCorrId == null) {
                currentCorrId = UUID.randomUUID().toString();
                LOGGER.info("No correlationId found in Header. Generated x-cceerequestid: " + currentCorrId);
            } else {
                LOGGER.info("Found correlationId in Header x-cceerequestid: " + currentCorrId);
            }

            RequestCorrelationId.setId(currentCorrId);
            //set MDC for logging
            MDC.put(RequestCorrelationId.CORRELATION_ID, RequestCorrelationId.getId());
        }

        chain.doFilter(httpServletRequest, response);		

        //MDC.put(RequestCorrelationId.CORRELATION_ID, "");
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
	
    private boolean currentRequestIsAsyncDispatcher(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getDispatcherType().equals(DispatcherType.ASYNC);
    }

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE - 1;
	}

}
