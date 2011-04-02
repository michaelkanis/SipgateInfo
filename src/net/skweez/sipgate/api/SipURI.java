package net.skweez.sipgate.api;

public class SipURI {

	private final char PROTOCOL_SEPARATOR = ':';

	private final char DOMAIN_SEPARATOR = '@';

	private final String protocol;

	private final String number;

	private final String domain;

	public SipURI(String uri) {
		int start = 0;
		int end = uri.indexOf(PROTOCOL_SEPARATOR);
		this.protocol = uri.substring(start, end);

		start = end + 1;
		end = uri.indexOf(DOMAIN_SEPARATOR);
		this.number = uri.substring(start, end);

		start = end + 1;
		this.domain = uri.substring(start);
	}

	public String getUri() {
		return new StringBuilder(protocol).append(PROTOCOL_SEPARATOR)
				.append(number).append(DOMAIN_SEPARATOR).append(domain)
				.toString();
	}

	public String getProtocol() {
		return protocol;
	}

	public String getNumber() {
		return number;
	}

	public String getDomain() {
		return domain;
	}

	@Override
	public String toString() {
		return getNumber();
	}

}
