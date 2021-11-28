package com.example.melo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);

        //Dexter is a simple library to request permission from the user
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                Toast.makeText(MainActivity.this, "Runtime Permission Granted", Toast.LENGTH_SHORT).show();
                //getting all the files from the external storage
                ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());
                //items will contain the files name in string format to display in the listView
                String [] items = new String[mySongs.size()];
                //loop to get files' name into string variable items[]
                for(int i = 0; i < mySongs.size(); i++)
                {
                    items[i] = mySongs.get(i).getName().replace(".mp3", "");
                }
                //creating arrayadapter to show list view
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(MainActivity.this, PlaySongs.class);
                        String currentSong = listView.getItemAtPosition(i).toString();
                        intent.putExtra("songList", mySongs);
                        intent.putExtra("currentSong", currentSong);
                        intent.putExtra("position", i);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(MainActivity.this, "Runtime Permission Denied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                //if request got rejected then again ask for it
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    //method to fetch all the .mp3 files from the phone
    public ArrayList<File> fetchSongs(File file)
    {
        ArrayList<File> arrayList = new ArrayList<File>();
        File [] songs = file.listFiles();
        if(songs != null)
        {
            for(File myfiles : songs)
            {
                if(!myfiles.isHidden() && myfiles.isDirectory())
                {
                    arrayList.addAll(fetchSongs(myfiles));
                }
                else
                {
                    if(myfiles.getName().endsWith(".mp3") && !myfiles.getName().startsWith("."))
                    {
                        arrayList.add(myfiles);
                    }
                }
            }
        }
        return arrayList;
    }
}