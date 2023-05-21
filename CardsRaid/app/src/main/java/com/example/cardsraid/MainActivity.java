package com.example.cardsraid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView rules_view;
    private MediaPlayer menu_music_Player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rules_view = (TextView) findViewById(R.id.rules_text);

        menu_music_Player = MediaPlayer.create(this, R.raw.main_menu_music);
        menu_music_Player.setLooping(true);
        menu_music_Player.setVolume(60,60);
        menu_music_Player.start();
    }

    public void start_game(View v){

        //sPref = getSharedPreferences("game_glob",MODE_PRIVATE);
        //SharedPreferences.Editor edit = sPref.edit();
        //edit.putString("ending", "false");
        //edit.apply();
        //startService(new Intent(this, MusicService.class));
        Intent intent = new Intent(this, MainTable.class);
        startActivity(intent);
    }

    public void show_rules(View v){
        if (rules_view.getVisibility() == View.VISIBLE){
            rules_view.setVisibility(View.INVISIBLE);
        }
        else{
            rules_view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        //music_media_Player = MediaPlayer.create(this, R.raw.main_song);
        if (menu_music_Player != null){
            menu_music_Player.seekTo(0);
            menu_music_Player.start();
            //music_media_Player.setLooping(true);
        }
    }
    @Override
    protected void onStop(){
        super.onStop();
        if (menu_music_Player.isPlaying()) {
            menu_music_Player.stop();
            menu_music_Player.release();
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        if (menu_music_Player.isPlaying()) {
            menu_music_Player.pause();
        }
    }
}