package ml.hele.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ml.hele.app.api.OnPostPreExecute;
import ml.hele.app.api.RetrieveCategories;
import ml.hele.app.api.RetrieveList;

public class HomeActivity extends AppCompatActivity implements OnPostPreExecute<ArrayList<Destination>>, TextWatcher {
    ProgressDialog progress;
    ListView destinationsList;
    //Spinner categoriesOptions;

    ArrayList<Destination> destinationArrayList;
    //ArrayList<String> categoriesArrayList;
    DestinationArrayAdapter destinationAdapter;
    //ArrayAdapter<String> categoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //categoriesArrayList = new ArrayList<String>();
        destinationArrayList = new ArrayList<Destination>();

        destinationsList = (ListView) findViewById(R.id.placelist);
       // categoriesOptions = (Spinner) findViewById(R.id.category_selector);

        destinationAdapter = new DestinationArrayAdapter(this,R.layout.list_item, destinationArrayList);
       // categoriesAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.spinner_category_name, categoriesArrayList);

        destinationsList.setAdapter(destinationAdapter);
       // categoriesOptions.setAdapter(categoriesAdapter);

        destinationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) view.findViewById(R.id.name);
                String title = text.getText().toString();
                text = (TextView) view.findViewById(R.id.id);
                String ID = text.getText().toString();
                Toast.makeText(HomeActivity.this, ID+" : "+title, Toast.LENGTH_SHORT).show();
            }
        });

        fetch();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetch(String...  params){
        new RetrieveList(this).execute();

      /*  new RetrieveCategories(new OnPostPreExecute<ArrayList<String>>() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(ArrayList<String> categories, String errorMessage) {
                Log.d("Category Content Loaded", "Content length: " + categories.size());
                categoriesArrayList = categories;
                Log.d("List: ", categoriesArrayList.toString());
                categoriesAdapter = new ArrayAdapter<String>(HomeActivity.this, R.layout.spinner_item, R.id.spinner_category_name, categoriesArrayList);
                Log.d("Adapter: ", categoriesAdapter.toString());
                categoriesOptions.setAdapter(categoriesAdapter);
            }
        }).execute();*/
    }

    @Override
    public void onPreExecute() {
        progress = ProgressDialog.show(HomeActivity.this, "Retriving Destinations List", "Retreiving destinations List from Hele Servers", true, false);

    }

    @Override
    public void onPostExecute(ArrayList<Destination> destinations, String errorMessage) {
        progress.dismiss();
        if(errorMessage!=null){
            Log.d("Content Failed to Load", errorMessage);
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Error Occured")
                    .setMessage("Error: " + errorMessage)
                    .setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    }).show();
        }

        Log.d("Content Loaded", "Content length: " + destinations.size());
        destinationArrayList = destinations;
        Log.d("List: ", destinationArrayList.toString());
        destinationAdapter = new DestinationArrayAdapter(HomeActivity.this, R.layout.list_item, destinationArrayList);
        Log.d("Adapter: ", destinationAdapter.toString());
        destinationsList.setAdapter(destinationAdapter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}