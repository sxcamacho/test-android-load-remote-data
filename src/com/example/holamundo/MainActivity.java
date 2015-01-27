package com.example.holamundo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private ListView list;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		list = (ListView)findViewById(R.id.listView1);
		list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int posicion,
			long arg3) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "Ha pulsado el elemento " + posicion, Toast.LENGTH_SHORT).show();
			}
		});
	
		CargarListView nuevaTarea = new CargarListView(this);
		nuevaTarea.execute();
	}
	
private class CargarListView extends AsyncTask<Void, Void, ArrayAdapter<String>>{
	
	Context context;
	ProgressDialog pDialog;
	InputStream is = null;
	String ip = "http://www.sebastiancamacho.com/lerose/servicio.php";
	String line = null;
	String result = null;
	
	public CargarListView(Context context){
		this.context = context;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		Log.i("AsyncTask PreExecute", "Entra en PreExecute");
		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Cargando Lista");
		pDialog.setCancelable(true);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.show();
	}
	
	@Override
	protected ArrayAdapter<String> doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		
		String[] sistemas = null;
		
		try{
			Thread.sleep(2000);
			sistemas = webserviceCall();
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		Log.i("doInBackground", "Crea el Adaptador");
		/*
		String[] sistemas = {"Ubuntu", "Android", "iOS", "Windows", "Mac OSX",
				"Google Chrome OS", "Debian", "Mandriva", "Solaris", "Unix"};
		*/
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, sistemas);
		return adaptador;
	}
	
	@Override
	protected void onPostExecute(ArrayAdapter<String> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		list.setAdapter(result);
		pDialog.dismiss();
	}
	
	private String[] webserviceCall() {
		
		String[] values = null;
		
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = null;
			httpGet = new HttpGet(ip);
			HttpResponse httpResponse = null;

		    try {
			    httpResponse = httpClient.execute(httpGet);
			    HttpEntity getResponseEntity = httpResponse.getEntity();
			    is = getResponseEntity.getContent();
			} catch (IOException e) {
				httpGet.abort();
			}
		}
		catch (Exception e) {
			Log.e("Lista", e.toString());
		}
		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			
			while((line = reader.readLine()) != null) {
				
				sb.append(line + "\n");
			}
			
			is.close();
			result = sb.toString();			
		}
		catch (Exception e) {
			Log.e("Lista", e.toString());
		}
		try {
			
			JSONArray ja = new JSONArray(result);
			JSONObject jo = null;
			
			values = new String[ja.length()];
						
			for(int i=0; i<ja.length(); i++) {
				
				jo = ja.getJSONObject(i);
				values[i] = jo.getString("name");
				Log.d("Lista", values[i]);
			}
			
			
		}catch (Exception e) {
			Log.e("Lista", e.toString());
		}
		
		return values;
	}
	
}

}


