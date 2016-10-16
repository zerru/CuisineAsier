package lejarza.asier.cuisineasier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Asier on 16/10/2016.
 */
public class Show_Photo extends AppCompatActivity {

    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Bundle bundle = getIntent().getExtras();
        String id_string = bundle.getString("id");
        id = Integer.parseInt(id_string);
        String url = bundle.getString("url");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //Barra de herramientas
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("id: " + id);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setLogo(android.R.drawable.ic_menu_gallery);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //ImageView imageView = (ImageView) findViewById(R.id.imageView);
        new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(url);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //MENU click: The action bar will automatically handle clicks
        switch (item.getItemId()) {
            case android.R.id.home: //Atrás
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
