package net.skweez.sipgate.api;

/**
 * 
 * @author mks
 * @author $Author: mks $
 * @version $Rev: 9 $
 * @levd.rating RED Rev:
 */
public interface ISipgateAPI {

	public Price getBalance();

	public UserUri[] getOwnURIList();
}
