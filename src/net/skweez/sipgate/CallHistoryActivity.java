package net.skweez.sipgate;

import net.skweez.sipgate.model.CallHistory;
import android.app.ListActivity;
import android.os.Bundle;

/**
 * 
 * @author mks
 * @author $Author: mks $
 * @version $Rev: 9 $
 * @levd.rating RED Rev:
 */
public class CallHistoryActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CallHistory history = new CallHistory();
		setListAdapter(new CallListAdapter(this, history));
		history.startRefresh();
	}

}
