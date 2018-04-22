/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.example.loanbroker.message;

import org.mule.example.loanbroker.model.CreditProfile;
import org.mule.example.loanbroker.model.LoanQuote;

import java.io.Serializable;

/**
 * <code>LoanQuoteRequest</code> represents a customer request for a loan through a
 * loan broker
 */
public class LoanBrokerQuoteRequest implements Serializable
{
    /**
     * Serial version
     */
    private static final long serialVersionUID = 46866005259682607L;

    /** The customer request */
    private CustomerQuoteRequest customerRequest;

    /** credit profile for the customer */
    private CreditProfile creditProfile;

    /** A list of lenders for this request */
    //private Bank[] lenders;

    /** A loan quote from a bank */
    private LoanQuote loanQuote;

    public LoanBrokerQuoteRequest()
    {
        super();
    }

    public CustomerQuoteRequest getCustomerRequest()
    {
        return customerRequest;
    }

    public void setCustomerRequest(CustomerQuoteRequest customerRequest)
    {
        this.customerRequest = customerRequest;
    }

    public CreditProfile getCreditProfile()
    {
        return creditProfile;
    }

    public void setCreditProfile(CreditProfile creditProfile)
    {
        this.creditProfile = creditProfile;
    }

    public LoanQuote getLoanQuote()
    {
        return loanQuote;
    }

    public void setLoanQuote(LoanQuote loanQuote)
    {
        this.loanQuote = loanQuote;
    }
}
