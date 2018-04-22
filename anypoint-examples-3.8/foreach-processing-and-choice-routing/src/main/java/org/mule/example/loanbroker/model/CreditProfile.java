/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.example.loanbroker.model;

import java.io.Serializable;

/**
 * <code>CreditProfile</code> is a dummy finance profile for a customer
 */
public class CreditProfile implements Serializable
{
    /**
     * Serial version
     */
    private static final long serialVersionUID = -5924690191061177417L;

    private int creditScore;
    private int creditHistory;

    public CreditProfile()
    {
        super();
    }

    @Override
    public String toString()
    {
        return "creditScore: " + creditScore + ", creditHistory: " + creditHistory;
    }

    public int getCreditScore()
    {
        return creditScore;
    }

    public void setCreditScore(int creditScore)
    {
        this.creditScore = creditScore;
    }

    public int getCreditHistory()
    {
        return creditHistory;
    }

    public void setCreditHistory(int creditHistory)
    {
        this.creditHistory = creditHistory;
    }

}
