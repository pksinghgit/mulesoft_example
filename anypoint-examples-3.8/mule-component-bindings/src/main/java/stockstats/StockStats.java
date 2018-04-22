/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package stockstats;

public class StockStats {

	private String closingDate;
	private FinancialStats financialStats = new FinancialStats();
	private SocialStats socialStats = new SocialStats();
	
	public String getClosingDate() {
		return closingDate;
	}
	public void setClosingDate(String closingDate) {
		this.closingDate = closingDate;
	}
	public FinancialStats getFinancialStats() {
		return financialStats;
	}
	public SocialStats getSocialStats() {
		return socialStats;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((closingDate == null) ? 0 : closingDate.hashCode());
		result = prime * result
				+ ((financialStats == null) ? 0 : financialStats.hashCode());
		result = prime * result
				+ ((socialStats == null) ? 0 : socialStats.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StockStats other = (StockStats) obj;
		if (closingDate == null) {
			if (other.closingDate != null)
				return false;
		} else if (!closingDate.equals(other.closingDate))
			return false;
		if (financialStats == null) {
			if (other.financialStats != null)
				return false;
		} else if (!financialStats.equals(other.financialStats))
			return false;
		if (socialStats == null) {
			if (other.socialStats != null)
				return false;
		} else if (!socialStats.equals(other.socialStats))
			return false;
		return true;
	}

}
