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

/**
 * Provides the credit profile for a customer.
 */
public class DefaultCreditAgency implements CreditAgencyService
{
     public CreditProfile getCreditProfile(Customer customer)
     {
         CreditProfile cp = new CreditProfile();
         cp.setCreditHistory(getCreditHistoryLength(customer.getSsn()));
         cp.setCreditScore(getCreditScore(customer.getSsn()));
         return cp;
     }

     protected int getCreditScore(int ssn)
     {
         int credit_score;

         credit_score = (int)(Math.random() * 600 + 300);

         return credit_score;
     }

     protected int getCreditHistoryLength(int ssn)
     {
         int credit_history_length;

         credit_history_length = (int)(Math.random() * 19 + 1);

         return credit_history_length;
     }
}
