package com.deepsekharghoshmusicplayer.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView listView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                        Toast.makeText(MainActivity.this, "run time permission given", Toast.LENGTH_SHORT).show();

//                        GET THE NAMES
                        ArrayList<File> mysongs = fetchsongs(Environment.getExternalStorageDirectory());
                        String [] items = new String[mysongs.size()];
                        for (int i = 0; i < mysongs.size(); i++)
                        {
                            items[i] =mysongs.get(i).getName().replace(".mp3","");
                        }
//                        USE LIST VIEW FOR DISPLAY
                        ArrayAdapter <String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items);
                        listView.setAdapter(adapter);

//                        ON CLICK NEXT PAGE
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(MainActivity.this, Playsong.class);
                                String currentsong = listView.getItemAtPosition(i).toString();
                                intent.putExtra("songlist",mysongs);
                                intent.putExtra("currentsong",currentsong);
                                intent.putExtra("position",i);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

//    FETCH SONGS
    public ArrayList<File> fetchsongs(File file){
        ArrayList arrayList= new ArrayList();
        File [] songs = file.listFiles();
        if(songs!=null)
        {
            for (File myfile : songs)
            {
                if(!myfile.isHidden() && myfile.isDirectory())
                arrayList.addAll(fetchsongs(myfile));
                else{
                    if(myfile.getName().endsWith(".mp3") && !myfile.getName().startsWith("."))
                        arrayList.add(myfile);
                }
            }
        }
        return arrayList;
    }
}