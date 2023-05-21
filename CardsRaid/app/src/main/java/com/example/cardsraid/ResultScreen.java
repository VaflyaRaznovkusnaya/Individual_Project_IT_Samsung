package com.example.cardsraid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

public class ResultScreen extends AppCompatActivity {

    private SharedPreferences sPref;// = getSharedPreferences("game_params",MODE_PRIVATE);

    private TextView myResultText;
    private MediaPlayer sound_effects_Player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);

        sPref = getSharedPreferences("game_params",MODE_PRIVATE);

        //renew game state to start from beginning
        SharedPreferences.Editor edit = sPref.edit();
        edit.putBoolean("IsGameNew", true);
        edit.apply();

        //

        myResultText = findViewById(R.id.game_result_text);

        //getString(R.string.win_text)
        //!!!! Receive game result from table
        Bundle arguments = getIntent().getExtras();
        if (arguments != null){
            boolean result = getIntent().getBooleanExtra("result", false);
            //String result_text = arguments.get("result_text").toString();

            if (result){
                //win
                myResultText.setText(R.string.win_text);
                sound_effects_Player = MediaPlayer.create(this, R.raw.win_sound);
            }
            else{
                myResultText.setText(R.string.loose_text);
                sound_effects_Player = MediaPlayer.create(this, R.raw.loose_sound);
            }
            sound_effects_Player.setVolume(90,90);
            sound_effects_Player.start();
            //login_field.setText(login_reg);
            //password_field.setText(pass_reg);
        }
    }

    public void results_go_to_menu(View v){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (sound_effects_Player.isPlaying()) {
            sound_effects_Player.stop();
        }
    }
}