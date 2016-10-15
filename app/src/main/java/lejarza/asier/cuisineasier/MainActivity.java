package lejarza.asier.cuisineasier;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lejarza.asier.cuisineasier.adapter.Adapter_Group;
import lejarza.asier.cuisineasier.adapter.models.Model_Group;

public class MainActivity extends AppCompatActivity {

    private static Context mContext;
    boolean back_pressed;
    int ExitPressed = 0;
    private RecyclerView mRecyclerView;
    Adapter_Group mAdapter;
    private List<Model_Group> mModels_id;
    private List<Model_Group> mModels_date;
    private List<Model_Group> mModels_name;
    private List<Model_Group> mModels_image;

    //GET Variables:
    private ProgressDialog progress;
    String str = "";
    ArrayList<String> photo_id = new ArrayList<String>();
    ArrayList<String> earth_date = new ArrayList<String>();
    ArrayList<String> camera_full_name = new ArrayList<String>();
    ArrayList<String> image_source = new ArrayList<String>();
    int size_data = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //Barra de herramientas
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("  " + getResources().getString(R.string.app_name)); //Para cambiar correctamente el idioma del titulo
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setLogo(R.drawable.logo);
        actionBar.setDisplayUseLogoEnabled(true);

        MainActivity.mContext = getApplicationContext();

        if(haveNetworkConnection()){
            sendGetRequest();
        }else{
            show_No_Network_Dialog();
        }


        mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL_LIST); //Divider
        mRecyclerView.addItemDecoration(itemDecoration);


        registerForContextMenu(mRecyclerView); //Para que aparezca el menu con el longclick
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, mRecyclerView,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View childView, int position) {
                        //int itemPosition = mRecyclerView.getChildLayoutPosition(childView);
                        //Toast.makeText(childView.getContext(), "position = " + itemPosition, Toast.LENGTH_SHORT).show();
/*
                        Intent start = new Intent(MainActivity.this, Store_View.class);
                        Model_Group name_model;

                        name_model = mModels.get(position);

                        String name = name_model.getText();

                        Model_Group model_value;
                        model_value = mModels_value.get(position);
                        String value_string = model_value.getText();
                        int store_value = Integer.parseInt(value_string);

                        start.putExtra("store_id", value_string);
                        startActivity(start);*/
                    }

                    @Override public void onLongItemClick(View childView, int position) {
                        Toast.makeText(childView.getContext(), "large = " + position, Toast.LENGTH_SHORT).show();
                    }
                })
        );





        //For testing:
        /*mModels_id = new ArrayList<>(); //ListArray
        mModels_date = new ArrayList<>(); //ListArray
        mModels_name = new ArrayList<>(); //ListArray
        mModels_image = new ArrayList<>(); //ListArray
        for(int i = 0; i < size_data; i++) {
            mModels_id.add(new Model_Group("1"));
            mModels_date.add(new Model_Group("2016/10/15"));
            mModels_name.add(new Model_Group("my_camera"));
            mModels_image.add(new Model_Group("http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01000/opgs/edr/fcam/FLB_486265257EDR_F0481570FHAZ00323M_.JPG"));
        }//*/


        back_pressed = false; //Al pulsarlo 3 veces: Sale de la app
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {       //MENU:  definimos el menú de opciones
        getMenuInflater().inflate(R.menu.menu_main, menu); // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {           //CLICKS
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:   // Respond to the action bar's Up/Home button
                onBackPressed();
                return true;

            case R.id.action_exit:
                show_Exit_Dialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    public static Context getAppContext() {  //Para saber el contexto de la clase principal (También: **declarar mContext al principio ** onCreate de la clase)
        return MainActivity.mContext;
    }

    @Override
    public void onBackPressed() {
        this.ExitPressed++;
        if (ExitPressed == 3) {
            finish();
            System.exit(0);
            //super.onBackPressed();    //Sale de la app
            return;
        }else if(ExitPressed == 2){
            Toast.makeText(MainActivity.getAppContext(), MainActivity.getAppContext().getString(R.string.exit_message_back_again), Toast.LENGTH_SHORT).show();
        }else {
            back_pressed = true;
            show_Exit_Dialog();
        }
        return;
    }




    public void show_Exit_Dialog() {
        android.app.AlertDialog.Builder exit_dialogBuilder = new android.app.AlertDialog.Builder(this);
        exit_dialogBuilder.setTitle(getResources().getString(R.string.exit_title));
        exit_dialogBuilder.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        if (back_pressed) {
            exit_dialogBuilder.setMessage(getResources().getString(R.string.exit_message) + '\n' + '\n' + getResources().getString(R.string.exit_message_back) + '\n');
        }else{
            exit_dialogBuilder.setMessage(getResources().getString(R.string.exit_message) + '\n');
        }

        exit_dialogBuilder.setCancelable(true); //Cancelable con el boton de atrás
        exit_dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                if (back_pressed) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {     //Añado un Timer
                            ExitPressed = 0;
                            back_pressed = false;
                        }
                    }, 3000);
                }

                if (ExitPressed != 0) {
                    ExitPressed = 2;
                    back_pressed = false;
                    Toast.makeText(MainActivity.getAppContext(), MainActivity.getAppContext().getString(R.string.exit_message_back_again), Toast.LENGTH_SHORT).show();
                }

            }
        });

        exit_dialogBuilder.setPositiveButton(Html.fromHtml("<b>" + getString(R.string.dialog_button_exit) + "<b>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
                System.exit(0);
            }
        });
        exit_dialogBuilder.setNegativeButton(getString(R.string.dialog_button_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ExitPressed = 0;
                back_pressed = false;
            }
        });
        Dialog b = exit_dialogBuilder.create();
        if (back_pressed) {
            b.setCanceledOnTouchOutside(false); //No cerrar la ventana al tocar fuera de ella
        }

        b.show();
        TextView msgTxt = (TextView) b.findViewById(android.R.id.message);
        msgTxt.setTextSize(20); //Tamaño del texto
    }

    public void show_No_Network_Dialog() {
        android.app.AlertDialog.Builder exit_dialogBuilder = new android.app.AlertDialog.Builder(this);
        exit_dialogBuilder.setTitle(getResources().getString(R.string.no_network_title));
        exit_dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        exit_dialogBuilder.setMessage(getResources().getString(R.string.no_network_message) + '\n');


        exit_dialogBuilder.setCancelable(false); //Cancelable con el boton de atrás

        exit_dialogBuilder.setPositiveButton(Html.fromHtml("<b>" + getString(R.string.dialog_button_exit) + "<b>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
                System.exit(0);
            }
        });

        Dialog b = exit_dialogBuilder.create();

        b.show();
        TextView msgTxt = (TextView) b.findViewById(android.R.id.message);
        msgTxt.setTextSize(20); //Tamaño del texto
    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    // To get the data from the API, with GET
    public void sendGetRequest() {
        new GetClass(this).execute();
    }

    private class GetClass extends AsyncTask<String, Void, Void> {

        private final Context context;

        public GetClass(Context c){
            this.context = c;
        }

        protected void onPreExecute(){
            progress = new ProgressDialog(this.context);
            progress.setMessage(getString(R.string.msg_loading));
            progress.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=DEMO_KEY");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                String urlParameters = "fizz=buzz";
                connection.setRequestMethod("GET");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

                int responseCode = connection.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";

                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();
                String result = responseOutput.toString();

                output.append(responseOutput.toString());

                ArrayList<String> store_categories_all = new ArrayList<String>();
                try {
                    JSONObject json = new JSONObject(result); // convert String to JSONObject
                    JSONArray general_photos = json.getJSONArray("photos"); // get main array
                    if (general_photos != null) {
                        size_data = general_photos.length();
                        mModels_id = new ArrayList<>(); //ListArray
                        mModels_date = new ArrayList<>(); //ListArray
                        mModels_name = new ArrayList<>(); //ListArray
                        mModels_image = new ArrayList<>(); //ListArray
                        for (int i=0; i < size_data; i++){
                            mModels_id.add(new Model_Group(general_photos.getJSONObject(i).getString("id")));
                            mModels_date.add(new Model_Group(general_photos.getJSONObject(i).getString("earth_date")));
                            mModels_image.add(new Model_Group(general_photos.getJSONObject(i).getString("img_src")));

                            JSONObject camera_info = general_photos.getJSONObject(i).getJSONObject("camera");
                            mModels_name.add(new Model_Group(camera_info.getString("full_name")));
                        }

                        mAdapter = new Adapter_Group(MainActivity.this, mModels_id, mModels_date, mModels_name, mModels_image);

                    }
                } catch (JSONException e) {
                    // Do something with the exception
                }

                int cat_size = store_categories_all.size();
                str = Integer.toString(cat_size);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //outputView.setText(str);
                        progress.dismiss();
                        mRecyclerView.setAdapter(mAdapter);

                    }
                });

            } catch (MalformedURLException e) {
                // Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

    }




}