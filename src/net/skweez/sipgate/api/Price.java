package net.skweez.sipgate.api;

/**
 * 
 * @author mks
 * @author $Author: mks $
 * @version $Rev: 9 $
 * @levd.rating RED Rev:
 */
public class Price {

	/** Amount of money in Currency that is available at user's account. */
	private final Double amount;

	/** Currency code as defined by the ISO 4217 standard. */
	private final String currency;

	public Price(Double amount, String currency) {
		this.amount = amount;
		this.currency = currency;
	}

	/**
	 * Returns currency code as defined by the ISO 4217 standard (e.g. "USD" or
	 * "EUR").
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * Returns amount of money in <i>currency</i> specified by
	 * {@link #getCurrency()}.
	 */
	public Double getAmount() {
		return amount;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return new StringBuilder().append(amount).append(" ").append(currency)
				.toString();
	}
}
