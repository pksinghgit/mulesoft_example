/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.example.loanbroker.bank;

import org.mule.example.loanbroker.message.LoanBrokerQuoteRequest;
import org.mule.example.loanbroker.model.LoanQuote;

import javax.jws.WebService;

/**
 * <code>BankService</code> is a representation of a bank form which to obtain loan
 * quotes.
 */
@WebService
public interface BankService
{
    LoanQuote getLoanQuote(LoanBrokerQuoteRequest request);
}
