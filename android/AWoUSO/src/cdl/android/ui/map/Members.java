package cdl.android.ui.map;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cdl.android.R;
import cdl.android.general.ServerResponse;
import cdl.android.server.ApiHandler;

public class Members extends Fragment {
	private ArrayList<MemberItem> mItems;
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	View view = inflater.inflate(R.layout.members, container, false);
    	
    	ListView mListView = null;
        mListView = (ListView) view.findViewById(R.id.members_list);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));
    
        TextView serie = (TextView) getActivity().findViewById(R.id.serie);
		String numeSerie = serie.getText().toString();
		
		String serieId = null;
		if (numeSerie.equals("CA")){
			serieId = "2";
		}
		else if (numeSerie.equals("CB")){
			serieId = "3";
		} else serieId = "4";
		
		ServerResponse resp = ApiHandler.getArray(
    			ApiHandler.raceURL + serieId + "/members/", view.getContext());
       	mItems = new ArrayList<MemberItem>();
          	
    	if (resp.getStatus() == false) {
    		Toast.makeText(view.getContext(), resp.getError(), Toast.LENGTH_SHORT).show();
    	} else {
    		try {
    			JSONArray arr = resp.getArrayData();
    			for (int i = 0; i < arr.length(); i++) {
    				MemberItem mes = new MemberItem();
    				mes.parseContent(arr.getJSONObject(i));
    				mItems.add(0, mes); // ordered alphabetically
   				}
   			} catch (JSONException e) {
    				Toast.makeText(view.getContext(), "Server response format error.",
    						Toast.LENGTH_SHORT).show();
    			}
    	}
    	
    	mListView.setAdapter(new MemberAdapter(this.getActivity().getApplicationContext(), mItems));
    
    	return view;
    }
	

}
