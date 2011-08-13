package net.skweez.sipgate.api;

import java.util.List;



/**
 * 
 * @author mks
 * @author $Author: mks $
 * @version $Rev: 9 $
 * @levd.rating RED Rev:
 */
public interface ISipgateAPI {

	public Price getBalance();
	
	public List<Call> getHistoryByDate();

	public UserUri[] getUserUriList();

	public UserName getUserName();
}
