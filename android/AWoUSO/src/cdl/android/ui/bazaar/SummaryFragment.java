package cdl.android.ui.bazaar;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.support.v4.app.*;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;
import cdl.android.R;
import cdl.android.general.ServerResponse;
import cdl.android.general.SummaryItem;
import cdl.android.server.ApiHandler;


public class SummaryFragment extends Fragment {
	private ArrayList<SummaryItem> mItems;
	private Bundle bundle;
	private View view;
	private ListView mListView;
	
	public SummaryFragment(){
		this.bundle = new Bundle();
		
	}
	
	public void setBundle(Bundle bundle){
		this.bundle.putInt("playerID", bundle.getInt("playerID"));
	}
	
	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		//super.onCreate(savedInstanceState);
		view =  inflater.inflate(R.layout.summary, container, false);
		
	    mListView = (ListView) view.findViewById(android.R.id.list);
		mListView.setEmptyView(view.findViewById(android.R.id.empty));
		
		mItems = new ArrayList();

		ServerResponse resp = ApiHandler.get(ApiHandler.baseURL + "bazaar/inventory/", view.getContext());

		if(resp.getStatus() == false){
		
			Toast.makeText(view.getContext(), resp.getError() , Toast.LENGTH_SHORT).show();
		
		}else{
			try {
				Log.d("type", resp.getData().toString());
				JSONArray spellData = (JSONArray) ((JSONObject)resp.getData()).get("spells_available");
			
				System.out.println(spellData.length());
				for(int i = 0; i < spellData.length(); i++){
					SummaryItem summaryItem = new SummaryItem();
					
					try{
						
						summaryItem.parseSpellsAvailable(spellData.getJSONObject(i));
						mItems.add(summaryItem);

					}catch(JSONException e){
						Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
		
		
		final Context con = view.getContext();
		bundle.putString("action", "castspell");
		
		mListView.setAdapter(new SummaryAdapter(con, mItems, bundle));
		
		return view;
		
	}

	
	public void reset(){
		mItems.clear();
		
		ServerResponse resp = ApiHandler.get(ApiHandler.baseURL + "bazaar/inventory/", view.getContext());
		Log.d("lololo", "mesaj1");
		if(resp.getStatus() == false){
			Log.d("lololo", "mesaj2");
			Toast.makeText(view.getContext(), resp.getError() , Toast.LENGTH_SHORT).show();
		
		}else{
			try {
				JSONArray spellData = (JSONArray) ((JSONObject)resp.getData()).get("spells_available");
			
				Log.d("lololo", "mesaj3");
				
				for(int i = 0; i < spellData.length(); i++){
					SummaryItem summaryItem = new SummaryItem();
					
					try{
						
						summaryItem.parseSpellsAvailable(spellData.getJSONObject(i));
						mItems.add(summaryItem);

					}catch(JSONException e){
						Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			
			Log.d("lololo", "mesaj4");
		}
		
		final Context con = view.getContext();
		
		Log.d("lololo", "mesaj5");
		bundle.putString("action", "castspell");
		
		Log.d("lololo", "mesaj6");
		mListView.setAdapter(new SummaryAdapter(con, mItems, bundle));
		Log.d("lololo", "mesaj7");
	}
	
	public static void castSpell(int playerID, int spellID,Context context, Method method){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		
		nameValuePairs.add(new BasicNameValuePair("spell", "" + spellID));
		nameValuePairs.add(new BasicNameValuePair("days", "" + 3));
		
		Object answer = null;
		try{
			answer = ApiHandler.sendPost(ApiHandler.baseURL + "player/" + playerID + "/cast/", nameValuePairs, context);
			Log.d("aici", ApiHandler.baseURL + "player/" + playerID + "/cast/");
		} catch(Exception e){
			//Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
		} finally {
			if(((ServerResponse) answer).getStatus() == true){
				Toast.makeText(context, "Witchcraft has been done", Toast.LENGTH_SHORT).show();
				method.handleStuff();
			} else {
				Toast.makeText(context, "Spell casting failed", Toast.LENGTH_SHORT).show();
				Log.d("Eroare spell", "" + ((ServerResponse) answer).getError());
			}
			
		}
	}	
	

}

interface Method{
	public void handleStuff();
}