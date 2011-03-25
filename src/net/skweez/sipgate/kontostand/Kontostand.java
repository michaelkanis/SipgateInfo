package net.skweez.sipgate.kontostand;

import java.net.URI;
import java.util.*;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;
import org.xmlrpc.android.XMLRPCFault;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class Kontostand extends Activity {
    /** Called when the activity is first created. */
	
	public static final String PREFS_NAME = "com.skweez.net.sipgate.kontostand.pref";
	
	private URI uri;
	private XMLRPCClient client;
	private TextView tv;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	private void account_setup() {
		Intent myIntent = new Intent();
		myIntent.setClassName("net.skweez.sipgate.kontostand", "net.skweez.sipgate.kontostand.Account_setup");
		startActivity(myIntent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.account_setup:
	        account_setup();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        
        
        
        tv = new TextView(this);
        tv.setText("Trying to get your vat...");
        setContentView(tv);
                
        uri = URI.create("https://samurai.sipgate.net/RPC2");
        client = new XMLRPCClient(uri, settings.getString("username", ""),  settings.getString("password", ""));
                
        XMLRPCMethod method = new XMLRPCMethod("samurai.BalanceGet", new XMLRPCMethodCallback() {
			public void callFinished(Object result) {
				Log.d("Test", "callFinished");
				
				HashMap map = (HashMap)result;
				
				String totalIncludingVat = ((HashMap)map.get("CurrentBalance")).get("TotalIncludingVat").toString();
				String currency = ((HashMap)map.get("CurrentBalance")).get("Currency").toString();
				
				tv.setText( totalIncludingVat + " "+ currency);
			}
		});
		method.call();
    }
    
    interface XMLRPCMethodCallback {
		void callFinished(Object result);
	}
	
	class XMLRPCMethod extends Thread {
		private String method;
		private Object[] params;
		private Handler handler;
		private XMLRPCMethodCallback callBack;
		public XMLRPCMethod(String method, XMLRPCMethodCallback callBack) {
			this.method = method;
			this.callBack = callBack;
			handler = new Handler();
		}
		public void call() {
			call(null);
		}
		public void call(Object[] params) {
			this.params = params;
			Log.d("Test", "Calling "+uri.getHost());
			start();
		}
		@Override
		public void run() {
    		try {
    			final Object result = client.callEx(method, params);
    			handler.post(new Runnable() {
					public void run() {
						callBack.callFinished(result);
					}
    			});
    		} catch (final XMLRPCFault e) {
    			handler.post(new Runnable() {
					public void run() {
						Log.d("Test", "error", e);
					}
    			});
    		} catch (final XMLRPCException e) {
    			handler.post(new Runnable() {
					public void run() {
						Log.d("Test", "error", e);
					}
    			});
    		}
		}
	}
}