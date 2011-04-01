package net.skweez.sipgate;

import java.util.Observable;
import java.util.Observer;

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
public class CallsListActivity extends ListActivity implements Observer {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		refresh();
	}

	/** Update the view when the balance object updated itself. */
	public void update(final Observable observable, final Object data) {
		if (observable instanceof CallHistory) {
		}
	}

	private void refresh() {
		CallHistory history = new CallHistory();
		history.addObserver(this);
		history.startRefresh();
	}

}
