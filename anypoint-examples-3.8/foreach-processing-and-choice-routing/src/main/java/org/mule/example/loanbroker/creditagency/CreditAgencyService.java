/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.example.loanbroker.creditagency;

import org.mule.example.loanbroker.model.CreditProfile;
import org.mule.example.loanbroker.model.Customer;

import javax.jws.WebService;


/**
 * <code>CreditAgencyService</code> the service that provides a credit score for a
 * customer.
 */
@WebService
public interface CreditAgencyService
{
    CreditProfile getCreditProfile(Customer customer);
}
