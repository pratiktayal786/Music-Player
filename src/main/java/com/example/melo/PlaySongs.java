package com.example.melo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySongs extends AppCompatActivity {

    //declaring variables
    TextView currenSong;
    ImageView prev, play, next;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String songName;
    SeekBar seekBar;
    int position;
    Thread updateSeek;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_songs);

        //getting the view of xml to java object
        currenSong = findViewById(R.id.currentSong);
        prev = findViewById(R.id.prev);
        play =  findViewById(R.id.play);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        songs = (ArrayList)bundle.getParcelableArrayList("songList");

        songName = intent.getStringExtra("currentSong");
        currenSong.setText(songName);
        currenSong.setSelected(true);
        //mediaPlayer code
        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(PlaySongs.this, uri);
        mediaPlayer.start();
        //seekbar code
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        //for continuous update of the seekBar
        updateSeek = new Thread() {
            @Override
            public void run() {
                int currentPosition = 0;

                try {
                    while(currentPosition < mediaPlayer.getDuration())
                    {
                        currentPosition = mediaPlayer.getCurrentPosition()  ;
                        seekBar.setProgress(currentPosition);
                        Thread.sleep(800);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();
        //end of seekbar code

        //Onclick for previous button
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();

                if(position!= 0)
                {
                    --position;
                }
                else
                {
                    position = songs.size() - 1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(PlaySongs.this, uri);
                mediaPlayer.start();
                songName = songs.get(position).getName().toString();
                currenSong.setText(songName);
                play.setImageResource(R.drawable.pause2);
            }
        });

        //Onclick for play button
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying())
                {
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else
                {
                    play.setImageResource(R.drawable.pause2);
                    mediaPlayer.start();
                }
            }
        });

        //Onclick for next button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position != songs.size() - 1)
                {
                    ++position;
                }
                else
                {
                    position = 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(PlaySongs.this, uri);
                mediaPlayer.start();
                songName = songs.get(position).getName().toString();
                currenSong.setText(songName);
                play.setImageResource(R.drawable.pause2);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                play.setImageResource(R.drawable.play);
            }
        });

    }

    //destroying everything
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

}
//End of code for playsongs.java class