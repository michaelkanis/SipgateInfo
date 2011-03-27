package net.skweez.sipgate.api;

import java.text.NumberFormat;
import java.util.Currency;

/**
 * 
 * @author mks
 * @author $Author: mks $
 * @version $Rev: 9 $
 * @levd.rating RED Rev:
 */
public class Price {

	/** Formats the money amount as a currency. */
	private final NumberFormat format = NumberFormat.getCurrencyInstance();

	/** Amount of money in Currency that is available at user's account. */
	private final Double amount;

	/**
	 * @param currency
	 *            the currency code in ISO 4217 format.
	 * @throws IllegalArgumentException
	 *             if the currency code is not a supported ISO 4217 currency
	 *             code.
	 */
	public Price(Double amount, String currency) {
		this.amount = amount;
		format.setCurrency(Currency.getInstance(currency));
	}

	/** Returns the amount of money. */
	public Double getAmount() {
		return amount;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(format.format(amount)).append(" ");
		return builder.toString();
	}
}
