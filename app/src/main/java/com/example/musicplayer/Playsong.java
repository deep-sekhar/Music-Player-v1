package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Playsong extends AppCompatActivity {

    TextView textView;
    ImageView play, previous, next;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String currentsong;
    int position;

//    STOP SONG ON EXIT
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }

//    SEEK BAR THREAD
    SeekBar seekBar;
    Thread updateseek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);

        textView = findViewById(R.id.textView);
        previous = findViewById(R.id.previous);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);

//        GET THE SONGS
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songlist");
        currentsong = intent.getStringExtra("currentsong");
        position = intent.getIntExtra("position",0);
        textView.setText(currentsong);
        textView.setSelected(true);

//        PLAYING MEDIA
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();

//        SEEKBAR METHODS
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

//        SEEKBAR THREADING
        updateseek = new Thread(){
            @Override
            public void run() {
                int pos = 0;
                try {
                    while(pos < mediaPlayer.getDuration())
                    {
                        pos = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(pos);
                        sleep(900);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateseek.start();

//        PLAY PAUSE BUTTONS
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying())
                {
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else{
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (position!=0)
                {
                position--;
                }
            else{
                position = songs.size()-1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                currentsong = songs.get(position).getName().toString().replace(".mp3","");
                textView.setText(currentsong);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (position!=songs.size()-1)
                {
                position++;
                }
            else{
                position = 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                currentsong = songs.get(position).getName().toString().replace(".mp3","");
                textView.setText(currentsong);
            }
        });
    }
}