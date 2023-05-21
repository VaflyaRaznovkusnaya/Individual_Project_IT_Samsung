package com.example.cardsraid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MainTable extends AppCompatActivity {

    // constants
    //arrays of brick types
    private final Set<Integer> heart_spots = new HashSet<Integer>(Arrays.asList(3,10,15,26,30,37,43));
    private final Set<Integer> broken_spots = new HashSet<Integer>(Arrays.asList(6,13,16,21,24,34,40,45));
    //pos 2,11, 17, 27, 33 - take someone's card
    private final Set<Integer> arr_purple_spots = new HashSet<Integer>(Arrays.asList(2,11,17,27,33));
    //private int[] arr_purple_spots = new int[]{2,11,17,27,33};
    //pos 5 -> start; 12 -> 5; 18 -> 12; 26 -> 18; 35->26; 41 -> 35, 43->41
    private final Set<Integer> arr_black_spots = new HashSet<Integer>(Arrays.asList(5,12,18,26,35,41,43));
    //pos 9 -> 14; 14-> 22; 22 -> 28; 28-> 37; 37-> nowhere
    private final Set<Integer> arr_white_spots = new HashSet<Integer>(Arrays.asList(9,14,22,28,37));
    //
    private final Set<Integer> arr_blue_spots = new HashSet<Integer>(Arrays.asList(6,15,21,29,36,38,42));
    private final Set<Integer> arr_red_spots = new HashSet<Integer>(Arrays.asList(1,10,19,24,32,39,45));
    private final Set<Integer> arr_green_spots = new HashSet<Integer>(Arrays.asList(4,8,16,23,30,40,44));
    private final Set<Integer> arr_yellow_spots = new HashSet<Integer>(Arrays.asList(3,7,13,20,25,31,34));

    //changable stuff on map
    private TextView number_generated;// = findViewById(R.id.Number_show);
    private ImageView image_pawn; //= findViewById(R.id.imageView);
    private ImageView pawn_change_place;
    private ImageView cards_change_on_hand;
    //for opening and closing screens
    private ConstraintLayout show_some_layouts; //look_up_card_field - to confirm card, battle_card_field - to battle
    //private Button temp_button_place; //deny_card, confirm_card, closebuttle
    private TextView temp_text_stuff;

    //can player do anything?
    private int Player_is_active = 0; //It's player's turn started
    private int battle_type = 0; //what battle is - 0 -none, 1 - health, 2 -strength, 3 -magic, 4 -agility
    private int destination_number = 0;
    private int assigned_card = 0;
    private int battle_sides = 0; //1- player, 2- Nancy, 3 - Carl. 13 -Player vs Carl
    private Boolean battle_state = false; //false - 1st turn, tru -second;
    private Boolean Player_have_to_choose_card = false; //He is challenged
    // player -1- cyan
    //
    private int player1_hand_cur_number;
    private int player2_hand_cur_number;
    private int player3_hand_cur_number;
    private int player1_position;
    private int player2_position;
    private int player3_position;

    // CARD DECK
    private CardDeck myCardDeck;// = new CardDeck();
    //

    //Use shared prefferences
    private SharedPreferences sPref;// = getSharedPreferences("game_params",MODE_PRIVATE);
    private MediaPlayer music_media_Player, sound_effects_Player;
    private Random rand_gen = new Random();

    //private ViewGroup whole_table_view;// = findViewById (R.id.whole_table);

    //----------------------------------------------------------------------------------------
    // save and remake info (card value, amount, position of all paws, card NUMBEr for other players)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_table);
        /**/
        player1_hand_cur_number = 4;
        player2_hand_cur_number = 4;
        player3_hand_cur_number = 4;
        player1_position = 0;
        player2_position = 0;
        player3_position = 0;
        Player_is_active = 0;
        image_pawn = findViewById(R.id.imageView);
        number_generated = findViewById(R.id.Number_show);
        myCardDeck = new CardDeck();
        sPref = getSharedPreferences("game_params",MODE_PRIVATE);
        //whole_table_view = findViewById (R.id.paws_placement);

        for (int i = 1; i < 5;i++){

            String name_of_card = "player_card" + String.valueOf(i);
            int id = getResources().getIdentifier(name_of_card,"id", getApplicationContext().getPackageName());
            cards_change_on_hand = findViewById(id);
            int card_value_player = myCardDeck.get_card_assignment_number(1,i);
            assign_picture(card_value_player,cards_change_on_hand);
        }
        //get if its new game

        /*
        Boolean if_new_game = sPref.getBoolean("IsGameNew", true);
        if (if_new_game){
            player1_hand_cur_number = 4;
            player2_hand_cur_number = 4;
            player3_hand_cur_number = 4;
            player1_position = 0;
            player2_position = 0;
            player3_position = 0;
        }
        else{
            //sPref = getSharedPreferences("game_params",MODE_PRIVATE);
            player1_hand_cur_number = sPref.getInt("Player1_handNumb_SAVED", 4);
            player2_hand_cur_number = sPref.getInt("Player2_handNumb_SAVED", 4);
            player3_hand_cur_number = sPref.getInt("Player3_handNumb_SAVED", 4);
            player1_position = sPref.getInt("Player1_position_SAVED",0);
            player2_position = sPref.getInt("Player2_position_SAVED",0);
            player3_position = sPref.getInt("Player3_position_SAVED",0);
            //retrieve card_class
            Gson gson = new Gson();
            String json = sPref.getString("Card_deck_SAVED", "");
            myCardDeck = gson.fromJson(json, CardDeck.class);
        }*/
        music_media_Player = MediaPlayer.create(this, R.raw.main_song);
        music_media_Player.setLooping(true);
        music_media_Player.setVolume(40,40);
        music_media_Player.start();


    }

    @Override
    protected void onPause(){
        super.onPause();
        if (music_media_Player.isPlaying()) {
            music_media_Player.pause();
        }
        //music_media_Player.release();
        //sound_effects_Player.stop();
        //sound_effects_Player.release();

        //save_state();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if (music_media_Player.isPlaying()) {
            music_media_Player.stop();
            music_media_Player.release();
        }
        //music_media_Player.release();
        //music_media_Player = null;
        //sound_effects_Player.release();
        //sound_effects_Player = null;

    }

    @Override
    protected void onResume(){

        super.onResume();
        //music_media_Player = MediaPlayer.create(this, R.raw.main_song);
        if (music_media_Player != null){
            music_media_Player.start();
            //music_media_Player.setLooping(true);
        }

        /*
        //sPref = getSharedPreferences("game_params",MODE_PRIVATE);
        player1_hand_cur_number = sPref.getInt("Player1_handNumb_SAVED", 4);
        player2_hand_cur_number = sPref.getInt("Player2_handNumb_SAVED", 4);
        player3_hand_cur_number = sPref.getInt("Player3_handNumb_SAVED", 4);
        player1_position = sPref.getInt("Player1_position_SAVED",0);
        player2_position = sPref.getInt("Player2_position_SAVED",0);
        player3_position = sPref.getInt("Player3_position_SAVED",0);
        //retrieve card_class
        Gson gson = new Gson();
        String json = sPref.getString("Card_deck_SAVED", "");
        myCardDeck = gson.fromJson(json, CardDeck.class);*/

    }


    public void go_back_to_menu(View v){
    if(Player_is_active == 0){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
        //sPref = getSharedPreferences("game_glob",MODE_PRIVATE);
        //SharedPreferences.Editor edit = sPref.edit();
        //edit.putString("ending", "false");
        //edit.apply();
        //startService(new Intent(this, MusicService.class));
    }

    public void confirm_card_choice(View v){
        //from card confirm layout pressed confirm card

        //open battle layout
        show_some_layouts = findViewById(R.id.battle_card_field);
        show_some_layouts.setVisibility(View.VISIBLE);

        //assign pictures
        //left is initiator
        if (battle_sides/10 == 1){
            //player is initializing
            cards_change_on_hand = findViewById(R.id.battle_cardLeft);
            int card_value = myCardDeck.get_card_assignment_number(1,assigned_card);
            assign_picture(card_value,cards_change_on_hand);
        }
        else if(battle_sides/10 == 2){
            //Nancy
            cards_change_on_hand = findViewById(R.id.battle_cardLeft);
            int card_value = myCardDeck.get_card_assignment_number(2,1);
            assign_picture(card_value,cards_change_on_hand);
        }
        else if(battle_sides/10 == 3){
            //Carl
            cards_change_on_hand = findViewById(R.id.battle_cardLeft);
            int card_value = myCardDeck.get_card_assignment_number(3,1);
            assign_picture(card_value,cards_change_on_hand);
        }
        //Right is receiver
        if (battle_sides%10 == 1){
            //player is receiving
            cards_change_on_hand = findViewById(R.id.battle_cardRight);
            int card_value = myCardDeck.get_card_assignment_number(1,assigned_card);
            assign_picture(card_value,cards_change_on_hand);

        }
        else if(battle_sides%10 == 2){
            //Nancy
            cards_change_on_hand = findViewById(R.id.battle_cardRight);
            int card_value = myCardDeck.get_card_assignment_number(2,1);
            assign_picture(card_value,cards_change_on_hand);
        }
        else if(battle_sides%10 == 3){
            //Carl
            cards_change_on_hand = findViewById(R.id.battle_cardRight);
            int card_value = myCardDeck.get_card_assignment_number(3,1);
            assign_picture(card_value,cards_change_on_hand);
        }

        //player can't choose card OR click next turn
        //Player_is_active = -1;
        Player_have_to_choose_card = false;
        TextView show_up_massages;
        show_up_massages = findViewById(R.id.notification_card_choose);
        show_up_massages.setVisibility(View.INVISIBLE);
        //close this layout
        show_some_layouts = findViewById(R.id.look_up_card_field);
        show_some_layouts.setVisibility(View.GONE);
    }
    public void decline_card_choice(View v){
        //from card confirm layout pressed decline card

        //close layout and make to get other card
        Player_have_to_choose_card = true;

        show_some_layouts = findViewById(R.id.look_up_card_field);
        show_some_layouts.setVisibility(View.GONE);
    }

    public void card1_click(View v){
        if (Player_have_to_choose_card){
            show_some_layouts = findViewById(R.id.look_up_card_field);
            show_some_layouts.setVisibility(View.VISIBLE);

            cards_change_on_hand = findViewById(R.id.imageView3);
            int card_value = myCardDeck.get_card_assignment_number(1,1);
            assign_picture(card_value,cards_change_on_hand);

            assigned_card = 1;
        }
    }
    public void card2_click(View v){
        if (Player_have_to_choose_card){
            show_some_layouts = findViewById(R.id.look_up_card_field);
            show_some_layouts.setVisibility(View.VISIBLE);

            cards_change_on_hand = findViewById(R.id.imageView3);
            int card_value = myCardDeck.get_card_assignment_number(1,2);
            assign_picture(card_value,cards_change_on_hand);

            assigned_card = 2;

        }
    }
    public void card3_click(View v){
        if (Player_have_to_choose_card){
            show_some_layouts = findViewById(R.id.look_up_card_field);
            show_some_layouts.setVisibility(View.VISIBLE);

            cards_change_on_hand = findViewById(R.id.imageView3);
            int card_value = myCardDeck.get_card_assignment_number(1,3);
            assign_picture(card_value,cards_change_on_hand);

            assigned_card = 3;

        }
    }
    public void card4_click(View v){
        if (Player_have_to_choose_card){
            show_some_layouts = findViewById(R.id.look_up_card_field);
            show_some_layouts.setVisibility(View.VISIBLE);

            cards_change_on_hand = findViewById(R.id.imageView3);
            int card_value = myCardDeck.get_card_assignment_number(1,4);
            assign_picture(card_value,cards_change_on_hand);

            assigned_card = 4;

        }
    }
    public void card5_click(View v){
        if (Player_have_to_choose_card){
            show_some_layouts = findViewById(R.id.look_up_card_field);
            show_some_layouts.setVisibility(View.VISIBLE);

            cards_change_on_hand = findViewById(R.id.imageView3);
            int card_value = myCardDeck.get_card_assignment_number(1,5);
            assign_picture(card_value,cards_change_on_hand);

            assigned_card = 5;

        }
    }
    public void card6_click(View v){
        if (Player_have_to_choose_card){
            show_some_layouts = findViewById(R.id.look_up_card_field);
            show_some_layouts.setVisibility(View.VISIBLE);

            cards_change_on_hand = findViewById(R.id.imageView3);
            int card_value = myCardDeck.get_card_assignment_number(1,6);
            assign_picture(card_value,cards_change_on_hand);

            assigned_card = 6;

        }
    }

    public void close_battle_field(View v){
        //change for player card
        //cards_change_on_hand.setVisibility(View.VISIBLE);

        //battle is over OR it just started
        if (battle_state){
            //second
            //change states and close layout
            //change variables
            battle_state = false;
            if (battle_sides%10 == 0){
                //no-one
                temp_text_stuff = findViewById(R.id.noone_wins_text);
                temp_text_stuff.setVisibility(View.INVISIBLE);
                if (battle_sides/10 == 1) {
                    //go to Nancy's turn if it was players
                    Player_is_active = 5;
                }
                else if (battle_sides/10 == 2) {
                    //go to Carl's turn if it was Nancy
                    Player_is_active = 10;
                }
                else{
                    //go to Player's turn if it was Carl
                    Player_is_active = 0;
                }
            }
            else if(battle_sides%10 == 8){
                //win
                temp_text_stuff = findViewById(R.id.conditionLeft);
                temp_text_stuff.setVisibility(View.INVISIBLE);

                temp_text_stuff = findViewById(R.id.conditionRight);
                temp_text_stuff.setVisibility(View.INVISIBLE);

                //set
                //calculate next dest
                if (battle_sides/10 == 1){
                    //it was player
                    destination_number = player1_position + myCardDeck.get_way_forward(1,assigned_card);

                    //change used card for player
                    String card_hand_id = myCardDeck.change_card_assigned(1, assigned_card);
                    int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                    cards_change_on_hand = findViewById(id_player_card);
                    int card_value_player = myCardDeck.get_card_assignment_number(1,assigned_card);
                    assign_picture(card_value_player,cards_change_on_hand);
                }
                else if(battle_sides/10 == 2){
                    //it was Nancy
                    destination_number = player2_position + myCardDeck.get_way_forward(2,0);
                }
                else if(battle_sides/10 == 3){
                    //it was Carl
                    destination_number = player3_position + myCardDeck.get_way_forward(3,0);
                }
                Player_is_active = 15;
            }
            else{
                //loose
                temp_text_stuff = findViewById(R.id.conditionLeft);
                temp_text_stuff.setVisibility(View.INVISIBLE);

                temp_text_stuff = findViewById(R.id.conditionRight);
                temp_text_stuff.setVisibility(View.INVISIBLE);

                //set
                //calculate next dest
                if (battle_sides/10 == 1){
                    //it was player
                    destination_number = player1_position - myCardDeck.get_way_backward(1,assigned_card);

                    //change used card for player
                    String card_hand_id = myCardDeck.change_card_assigned(1, assigned_card);
                    int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                    cards_change_on_hand = findViewById(id_player_card);
                    int card_value_player = myCardDeck.get_card_assignment_number(1,assigned_card);
                    assign_picture(card_value_player,cards_change_on_hand);
                }
                else if(battle_sides/10 == 2){
                    //it was Nancy
                    destination_number = player2_position - myCardDeck.get_way_backward(2,0);
                }
                else if(battle_sides/10 == 3){
                    //it was Carl
                    destination_number = player3_position - myCardDeck.get_way_backward(3,0);
                }

                Player_is_active = 16;
            }
            //Player_is_active = 15;//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            //swap bots cards
            myCardDeck.change_card_assigned(2, 1);
            myCardDeck.change_card_assigned(3, 1);

            //close layout
            show_some_layouts = findViewById(R.id.battle_card_field);
            show_some_layouts.setVisibility(View.GONE);
        }
        else{
            //first
            //calculate the battle
            //left is initiator
            //battle_sides = 10/20/30 - noone, 18 - player won, 19 - player loose, 28,29, 38,39
            battle_sides = myCardDeck.calculate_win(battle_sides,battle_type,assigned_card);
            if (battle_sides%10 == 0){
                //no-one
                temp_text_stuff = findViewById(R.id.noone_wins_text);
                temp_text_stuff.setVisibility(View.VISIBLE);
            }
            else if(battle_sides%10 == 8){
                //win
                temp_text_stuff = findViewById(R.id.conditionLeft);
                temp_text_stuff.setText(R.string.result_card_winner);
                temp_text_stuff.setVisibility(View.VISIBLE);

                temp_text_stuff = findViewById(R.id.conditionRight);
                temp_text_stuff.setText(R.string.result_card_looser);
                temp_text_stuff.setVisibility(View.VISIBLE);
            }
            else{
                //loose
                temp_text_stuff = findViewById(R.id.conditionLeft);
                temp_text_stuff.setText(R.string.result_card_looser);
                temp_text_stuff.setVisibility(View.VISIBLE);

                temp_text_stuff = findViewById(R.id.conditionRight);
                temp_text_stuff.setText(R.string.result_card_winner);
                temp_text_stuff.setVisibility(View.VISIBLE);
            }
            //assign appropriate outcome
            battle_state = true;
        }

    }
    private void assign_picture(int card_value, ImageView image){
        // cards are 9, 10, j, q, k, a. where 9 has index of 0.
        switch (card_value){
            case 0:
                image.setImageResource(R.drawable.card_back);
                break;
            case 1:
                image.setImageResource(R.drawable.card_1_red_necromancer);
                break;
            case 2:
                image.setImageResource(R.drawable.card_2_spooky_vampire);
                break;
            case 3:
                image.setImageResource(R.drawable.card_3_skeleton_mage);
                break;
            case 4:
                image.setImageResource(R.drawable.card_4_banshee);
                break;
            case 5:
                image.setImageResource(R.drawable.card_5_raven_knight);
                break;
            case 6:
                image.setImageResource(R.drawable.card_6_royal_spy);
                break;
            case 7:
                image.setImageResource(R.drawable.card_7_mirror_mage);
                break;
            case 8:
                image.setImageResource(R.drawable.card_8_fairy_archer);
                break;
            case 9:
                image.setImageResource(R.drawable.card_9_flying_snake);
                break;
            case 10:
                image.setImageResource(R.drawable.card_10_lamasu);
                break;
            case 11:
                image.setImageResource(R.drawable.card_11_sea_wolf);
                break;
            case 12:
                image.setImageResource(R.drawable.card_12_gryphons);
                break;
        }
    }

    public void you_loose(MainTable v){

        Intent intent = new Intent(this, ResultScreen.class);
        intent.putExtra("result", false);
        startActivity(intent);
    }
    public void you_win(MainTable v){
        /*
        try {
            TimeUnit.MILLISECONDS.sleep(750);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
        Intent intent = new Intent(this, ResultScreen.class);
        intent.putExtra("result", true);
        startActivity(intent);
    }

    private void save_state(){
        SharedPreferences.Editor edit = sPref.edit();
        edit.putInt("Player1_position_SAVED", player1_position);
        edit.putInt("Player2_position_SAVED", player2_position);
        edit.putInt("Player3_position_SAVED", player3_position);
        edit.putInt("Player1_handNumb_SAVED", player1_hand_cur_number);
        edit.putInt("Player2_handNumb_SAVED", player2_hand_cur_number);
        edit.putInt("Player3_handNumb_SAVED", player3_hand_cur_number);
        //change GameState to not make new game
        edit.putBoolean("IsGameNew", false);
        //save Card Deck by Gson library function
        Gson gson = new Gson();
        String json = gson.toJson(myCardDeck);
        edit.putString("Card_deck_SAVED", json);
        // apply all saves
        edit.apply();
    }
    private void rightful_positions(){
        if (player1_position == 0){
            pawn_change_place = findViewById(R.id.paws_start);
            pawn_change_place.setVisibility(View.VISIBLE);
        }
        else{
            String name = "paws_" + String.valueOf(player1_position);
            int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
            pawn_change_place = findViewById(id);
            pawn_change_place.setImageResource(R.drawable.pawn_cyan);
            pawn_change_place.setVisibility(View.VISIBLE);
        }

        if (player2_position == 0){
            pawn_change_place = findViewById(R.id.paws_start2);
            pawn_change_place.setVisibility(View.VISIBLE);
        }
        else{
            String name = "paws_" + String.valueOf(player2_position);
            int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
            pawn_change_place = findViewById(id);
            pawn_change_place.setImageResource(R.drawable.pawn_purple);
            pawn_change_place.setVisibility(View.VISIBLE);
        }

        if (player3_position == 0){
            pawn_change_place = findViewById(R.id.paws_start3);
            pawn_change_place.setVisibility(View.VISIBLE);
        }
        else{
            String name = "paws_" + String.valueOf(player3_position);
            int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
            pawn_change_place = findViewById(id);
            pawn_change_place.setImageResource(R.drawable.paw_orange);
            pawn_change_place.setVisibility(View.VISIBLE);
        }
    }

    public void click_numbers(View v) {
        //-1 - battle, 0 - change counter and paws, 1 - one step, 2 - calculate hearts/spec. spots, 3 - additional steps 4- battle prepare
        // 5 - Nancy's turn, 10 - Carl's turn

        //calculate number and show it + paw
        if (Player_is_active == 0){
            //Ход игрока
            //you_win(this); //debug!

            /**/
            int turn_move_distance = rand_gen.nextInt(5) + 1;
            image_pawn.setImageResource(R.drawable.pawn_cyan);
            number_generated.setText(String.valueOf(turn_move_distance));
            destination_number = player1_position + turn_move_distance;

            TextView show_up_massages;
            show_up_massages = findViewById(R.id.notification_card_choose);
            show_up_massages.setVisibility(View.INVISIBLE);

            Player_is_active = 1;
        }

        //make a step and not change state and if it's last one, change state (1->2/ 1->3/ 1->4)
        else if (Player_is_active == 1){
            //MAKE ONE STEP
            if (player1_position == 0){
                //if it's first - make it correct
                pawn_change_place = findViewById(R.id.paws_start);
                pawn_change_place.setVisibility(View.INVISIBLE);
                pawn_change_place = findViewById(R.id.paws_1);
                pawn_change_place.setImageResource(R.drawable.pawn_cyan);
                pawn_change_place.setVisibility(View.VISIBLE);
                player1_position = 1;
            }
            else{
                //hide current
                String name = "paws_" + String.valueOf(player1_position);
                int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id);
                pawn_change_place.setVisibility(View.INVISIBLE);

                //show next
                String name2 = "paws_" + String.valueOf((player1_position+1));
                int id2 = getResources().getIdentifier(name2,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id2);
                pawn_change_place.setImageResource(R.drawable.pawn_cyan);
                pawn_change_place.setVisibility(View.VISIBLE);
                player1_position += 1;
            }
            rightful_positions();//!Update positions
            //CHECK if it's castle tile
            if (player1_position == 46){
                you_win(this);
                //return;
            }
            //CHECK if it's last one
            //yes - change to 2nd state
            else if(player1_position == destination_number){
                Player_is_active = 2;
            }
            //no - continue
        }

        //- calculate (broken)hearts and additional movements
        else if (Player_is_active == 2){
            //check hearts
            if(heart_spots.contains(player1_position)){
                //check maximum amount of cards
                if (player1_hand_cur_number < 6){
                    //add card
                    player1_hand_cur_number += 1;
                    String card_hand_id = myCardDeck.add_Card_from_someone(1, player1_hand_cur_number);
                    int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                    cards_change_on_hand = findViewById(id_player_card);
                    int card_value_player = myCardDeck.get_card_assignment_number(1,player1_hand_cur_number);
                    assign_picture(card_value_player,cards_change_on_hand);
                    cards_change_on_hand.setVisibility(View.VISIBLE);
                }
            }
            else if(broken_spots.contains(player1_position)){
                //check the amount
                if (player1_hand_cur_number > 1) {
                    //retrieve
                    player1_hand_cur_number -= 1;
                    String card_hand_id = myCardDeck.take_Card_from_someone(1, player1_hand_cur_number);
                    int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                    cards_change_on_hand = findViewById(id_player_card);
                    cards_change_on_hand.setVisibility(View.GONE);
                }
            }

            //opt: take cards (PURPLE TILE - battle 0, state - next turn 5)
            if (arr_purple_spots.contains(player1_position)){
                //take cards
                if (player1_hand_cur_number < 6){
                    //add card
                    player1_hand_cur_number += 1;
                    String card_hand_id = myCardDeck.add_Card_from_someone(1, player1_hand_cur_number);
                    int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                    cards_change_on_hand = findViewById(id_player_card);
                    int card_value_player = myCardDeck.get_card_assignment_number(1,player1_hand_cur_number);
                    assign_picture(card_value_player,cards_change_on_hand);
                    cards_change_on_hand.setVisibility(View.VISIBLE);
                }
                //randomly choose player
                boolean player_to_take = rand_gen.nextBoolean();
                if (player_to_take){
                    //Nancy
                    if (player2_hand_cur_number > 1) {
                        //retrieve
                        player2_hand_cur_number -= 1;
                        String card_hand_id = myCardDeck.take_Card_from_someone(2, player2_hand_cur_number);
                        int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                        cards_change_on_hand = findViewById(id_player_card);
                        cards_change_on_hand.setVisibility(View.GONE);
                    }
                }
                else{
                    //Carl
                    if (player3_hand_cur_number > 1) {
                        //retrieve
                        player3_hand_cur_number -= 1;
                        String card_hand_id = myCardDeck.take_Card_from_someone(3, player3_hand_cur_number);
                        int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                        cards_change_on_hand = findViewById(id_player_card);
                        cards_change_on_hand.setVisibility(View.GONE);
                    }
                }
                //from random
                battle_type = 0;
                Player_is_active = 5;
            }

            //opt: check if white
            else if(arr_white_spots.contains(player1_position)){
                //go further
                //state next position and change state accordingly
                switch(player1_position){
                    case 9: //to 14
                        destination_number = 14;
                        Player_is_active = 3;
                        break;
                    case 14: //to 22
                        destination_number = 22;
                        Player_is_active = 3;
                        break;
                    case 22:  //to 28
                        destination_number = 28;
                        Player_is_active = 3;
                        break;
                    case 28: //to 37
                        destination_number = 37;
                        Player_is_active = 3;
                        break;
                    case 37: //to 37
                        //Because you can't go any further
                        Player_is_active = 5;
                        break;
                }
            }
            //opt: check if black
            else if(arr_black_spots.contains(player1_position)){
                //go back or to start
                switch(player1_position){
                    case 5: //to start
                        destination_number = 0;
                        Player_is_active = 3;
                        break;
                    case 12: //to 5
                        destination_number = 5;
                        Player_is_active = 3;
                        break;
                    case 18:  //to 12
                        destination_number = 12;
                        Player_is_active = 3;
                        break;
                    case 26: //to 18
                        destination_number = 18;
                        Player_is_active = 3;
                        break;
                    case 35: //to 26
                        destination_number = 26;
                        Player_is_active = 3;
                        break;
                    case 41:  //to 35
                        destination_number = 35;
                        Player_is_active = 3;
                        break;
                    case 43: //to 41
                        destination_number = 41;
                        Player_is_active = 3;
                        break;
                }

            }

            //health 1, 2 -strength, 3 -magic, 4 -agility
            else if(arr_red_spots.contains(player1_position)){
                battle_type = 1;
                Player_is_active = 4;
            }
            //Strength
            else if(arr_green_spots.contains(player1_position)){
                battle_type = 2;
                Player_is_active = 4;
            }
            //magic
            else if(arr_blue_spots.contains(player1_position)){
                battle_type = 3;
                Player_is_active = 4;
            }
            //agility
            else if(arr_yellow_spots.contains(player1_position)){
                battle_type = 4;
                Player_is_active = 4;
            }
        }

        //step more (black/white) when done - change to 3 OR 5
        else if (Player_is_active == 3){
            //check if destined position is further or not
            if (player1_position < destination_number){
                //further
                //hide prev.
                String name = "paws_" + String.valueOf(player1_position);
                int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id);
                pawn_change_place.setVisibility(View.INVISIBLE);

                //show next
                String name2 = "paws_" + String.valueOf((player1_position+1));
                int id2 = getResources().getIdentifier(name2,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id2);
                pawn_change_place.setImageResource(R.drawable.pawn_cyan);
                pawn_change_place.setVisibility(View.VISIBLE);

                player1_position += 1;
            }
            else{
                if (player1_position - 1 == 0){
                    pawn_change_place = findViewById(R.id.paws_1);
                    pawn_change_place.setVisibility(View.INVISIBLE);
                    pawn_change_place = findViewById(R.id.paws_start);
                    pawn_change_place.setVisibility(View.VISIBLE);

                    player1_position = 0;
                }
                else{
                    //hide prev.
                    String name = "paws_" + String.valueOf(player1_position);
                    int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
                    pawn_change_place = findViewById(id);
                    pawn_change_place.setVisibility(View.INVISIBLE);

                    //show next
                    String name2 = "paws_" + String.valueOf((player1_position-1));
                    int id2 = getResources().getIdentifier(name2,"id", getApplicationContext().getPackageName());
                    pawn_change_place = findViewById(id2);
                    pawn_change_place.setImageResource(R.drawable.pawn_cyan);
                    pawn_change_place.setVisibility(View.VISIBLE);

                    player1_position -= 1;
                }
            }
            rightful_positions(); //!Update positions
            //check if it's over
            //out of 3 we can go only to 5 (Nancy's turn)
            if (player1_position == destination_number){
                Player_is_active = 5;
            }
        }
        // after
        else if (Player_is_active == 4){
            //battle after player's turn
            //show notification to player to choose a card
            TextView show_up_massages;
            show_up_massages = findViewById(R.id.notification_card_choose);
            show_up_massages.setVisibility(View.VISIBLE);

            Player_have_to_choose_card = true;

            //generate who vs who is fighting
            boolean player_to_take = rand_gen.nextBoolean();
            if (player_to_take){
                //Player vs Nancy
                battle_sides = 12;
            }
            else{
                //Player vs Carl
                battle_sides = 13;
            }
            Player_is_active = -1;
            //Player_is_active = 5; //!debug

        }
        else if (Player_is_active == 5) {
            //battle is over/no battle, it's Nancy's turn. goes like 0

            int turn_move_distance = rand_gen.nextInt(5) + 1;
            image_pawn.setImageResource(R.drawable.pawn_purple);
            number_generated.setText(String.valueOf(turn_move_distance));
            destination_number = player2_position + turn_move_distance;

            TextView show_up_massages;
            show_up_massages = findViewById(R.id.notification_card_choose);
            show_up_massages.setVisibility(View.INVISIBLE);

            Player_is_active = 6;

        }
        else if (Player_is_active == 6) {
            //it's Nancy's turn. goes like 1

            //MAKE ONE STEP
            if (player2_position == 0){
                //if it's first - make it correct
                pawn_change_place = findViewById(R.id.paws_start2);
                pawn_change_place.setVisibility(View.INVISIBLE);
                pawn_change_place = findViewById(R.id.paws_1);
                pawn_change_place.setImageResource(R.drawable.pawn_purple);
                pawn_change_place.setVisibility(View.VISIBLE);
                player2_position = 1;
            }
            else{
                //hide current
                String name = "paws_" + String.valueOf(player2_position);
                int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id);
                pawn_change_place.setVisibility(View.INVISIBLE);

                //show next
                String name2 = "paws_" + String.valueOf((player2_position+1));
                int id2 = getResources().getIdentifier(name2,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id2);
                pawn_change_place.setImageResource(R.drawable.pawn_purple);
                pawn_change_place.setVisibility(View.VISIBLE);
                player2_position += 1;
            }
            //CHECK if it's castle tile
            rightful_positions(); //!Update positions
            if (player2_position == 46){
                you_loose(this);
            }
            //CHECK if it's last one
            //yes - change to 7th state
            else if(player2_position == destination_number){
                Player_is_active = 7;
            }
            //no - continue
        }
        else if (Player_is_active == 7) {
            //it's Nancy's turn. goes like 2

            //check hearts
            if(heart_spots.contains(player2_position)){
                //check maximum amount of cards
                if (player2_hand_cur_number < 6){
                    //add card
                    player2_hand_cur_number += 1;
                    String card_hand_id = myCardDeck.add_Card_from_someone(2, player2_hand_cur_number);
                    int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                    cards_change_on_hand = findViewById(id_player_card);
                    assign_picture(0,cards_change_on_hand);
                    cards_change_on_hand.setVisibility(View.VISIBLE);
                }
            }
            else if(broken_spots.contains(player2_position)){
                //check the amount
                if (player2_hand_cur_number > 1) {
                    //retrieve
                    player2_hand_cur_number -= 1;
                    String card_hand_id = myCardDeck.take_Card_from_someone(2, player2_hand_cur_number);
                    int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                    cards_change_on_hand = findViewById(id_player_card);
                    cards_change_on_hand.setVisibility(View.GONE);
                }
            }

            //what color of tile
            //opt: take cards (PURPLE TILE - battle 0, state - next turn 10)
            if (arr_purple_spots.contains(player2_position)){
                //take cards
                if (player2_hand_cur_number < 6){
                    //add card
                    player2_hand_cur_number += 1;
                    String card_hand_id = myCardDeck.add_Card_from_someone(2, player2_hand_cur_number);
                    int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                    cards_change_on_hand = findViewById(id_player_card);
                    assign_picture(0,cards_change_on_hand);
                    cards_change_on_hand.setVisibility(View.VISIBLE);
                }
                //randomly choose player
                boolean player_to_take = rand_gen.nextBoolean();
                if (player_to_take){
                    //Player
                    if (player1_hand_cur_number > 1) {
                        //retrieve
                        player1_hand_cur_number -= 1;
                        String card_hand_id = myCardDeck.take_Card_from_someone(1, player1_hand_cur_number);
                        int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                        cards_change_on_hand = findViewById(id_player_card);
                        cards_change_on_hand.setVisibility(View.GONE);
                    }
                }
                else{
                    //Carl
                    if (player3_hand_cur_number > 1) {
                        //retrieve
                        player3_hand_cur_number -= 1;
                        String card_hand_id = myCardDeck.take_Card_from_someone(3, player3_hand_cur_number);
                        int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                        cards_change_on_hand = findViewById(id_player_card);
                        cards_change_on_hand.setVisibility(View.GONE);
                    }
                }
                //from random
                battle_type = 0;
                Player_is_active = 10; //Go to Carls' start
            }
            //opt: check if white
            else if(arr_white_spots.contains(player2_position)){
                //go further
                switch(player2_position){
                    case 9: //to 14
                        destination_number = 14;
                        Player_is_active = 8;
                        break;
                    case 14: //to 22
                        destination_number = 22;
                        Player_is_active = 8;
                        break;
                    case 22:  //to 28
                        destination_number = 28;
                        Player_is_active = 8;
                        break;
                    case 28: //to 37
                        destination_number = 37;
                        Player_is_active = 8;
                        break;
                    case 37: //to 37
                        //Because you can't go any further
                        Player_is_active = 10; //Go to Carls' start
                        break;
                }
            }
            //opt: check if black
            else if(arr_black_spots.contains(player2_position)){
                //go back or to start
                switch(player2_position){
                    case 5: //to start
                        destination_number = 0;
                        Player_is_active = 8;
                        break;
                    case 12: //to 5
                        destination_number = 5;
                        Player_is_active = 8;
                        break;
                    case 18:  //to 12
                        destination_number = 12;
                        Player_is_active = 8;
                        break;
                    case 26: //to 18
                        destination_number = 18;
                        Player_is_active = 8;
                        break;
                    case 35: //to 26
                        destination_number = 26;
                        Player_is_active = 8;
                        break;
                    case 41:  //to 35
                        destination_number = 35;
                        Player_is_active = 8;
                        break;
                    case 43: //to 41
                        destination_number = 41;
                        Player_is_active = 8;
                        break;
                }
            }

            //health 1, 2 -strength, 3 -magic, 4 -agility
            else if(arr_red_spots.contains(player2_position)){
                battle_type = 1;
                Player_is_active = 9;
            }
            //Strength
            else if(arr_green_spots.contains(player2_position)){
                battle_type = 2;
                Player_is_active = 9;
            }
            //magic
            else if(arr_blue_spots.contains(player2_position)){
                battle_type = 3;
                Player_is_active = 9;
            }
            //agility
            else if(arr_yellow_spots.contains(player2_position)){
                battle_type = 4;
                Player_is_active = 9;
            }

        }
        else if (Player_is_active == 8) {
            //it's Nancy's turn. goes like 3
            //check if destined position is further or not
            if (player2_position < destination_number){
                //further
                //hide prev.
                String name = "paws_" + String.valueOf(player2_position);
                int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id);
                pawn_change_place.setVisibility(View.INVISIBLE);

                //show next
                String name2 = "paws_" + String.valueOf((player2_position+1));
                int id2 = getResources().getIdentifier(name2,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id2);
                pawn_change_place.setImageResource(R.drawable.pawn_purple);
                pawn_change_place.setVisibility(View.VISIBLE);

                player2_position += 1;
            }
            else{
                if (player2_position - 1 == 0){
                    pawn_change_place = findViewById(R.id.paws_1);
                    pawn_change_place.setVisibility(View.INVISIBLE);
                    pawn_change_place = findViewById(R.id.paws_start2);
                    pawn_change_place.setVisibility(View.VISIBLE);

                    player2_position = 0;
                }
                else{
                    //hide prev.
                    String name = "paws_" + String.valueOf(player2_position);
                    int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
                    pawn_change_place = findViewById(id);
                    pawn_change_place.setVisibility(View.INVISIBLE);

                    //show next
                    String name2 = "paws_" + String.valueOf((player2_position-1));
                    int id2 = getResources().getIdentifier(name2,"id", getApplicationContext().getPackageName());
                    pawn_change_place = findViewById(id2);
                    pawn_change_place.setImageResource(R.drawable.pawn_purple);
                    pawn_change_place.setVisibility(View.VISIBLE);

                    player2_position -= 1;
                }
            }
            rightful_positions(); //!Update positions
            //check if it's over
            //out of 8 we can go only to 10 (Carl's turn)
            if (player2_position == destination_number){
                Player_is_active = 10;
            }
        }
        else if (Player_is_active == 9) {
            //it's Nancy's turn. goes like 4

            //generate who vs who is fighting
            boolean player_to_take = rand_gen.nextBoolean();
            if (player_to_take){
                //Nancy vs Player
                battle_sides = 21;
                TextView show_up_massages;
                show_up_massages = findViewById(R.id.notification_card_choose);
                show_up_massages.setVisibility(View.VISIBLE);
                Player_have_to_choose_card = true;
            }
            else{
                //Nancy vs Carl
                battle_sides = 23;

                //open battle layout
                show_some_layouts = findViewById(R.id.battle_card_field);
                show_some_layouts.setVisibility(View.VISIBLE);

                //assign pictures
                //left is initiator
                cards_change_on_hand = findViewById(R.id.battle_cardLeft);
                int card_value1 = myCardDeck.get_card_assignment_number(2,1);
                assign_picture(card_value1,cards_change_on_hand);

                //Right is receiver
                cards_change_on_hand = findViewById(R.id.battle_cardRight);
                int card_value2 = myCardDeck.get_card_assignment_number(3,1);
                assign_picture(card_value2,cards_change_on_hand);
            }

            Player_is_active = -1;
            //choose player against
            //if it's bot - open a randomly card battle

            //Player_is_active = 10; //!debug
        }
        else if (Player_is_active == 10) {
            //battle is over/no battle, it's Carl's turn. goes like 0
            int turn_move_distance = rand_gen.nextInt(5) + 1;
            image_pawn.setImageResource(R.drawable.paw_orange);
            number_generated.setText(String.valueOf(turn_move_distance));
            destination_number = player3_position + turn_move_distance;

            TextView show_up_massages;
            show_up_massages = findViewById(R.id.notification_card_choose);
            show_up_massages.setVisibility(View.INVISIBLE);

            Player_is_active = 11;

        }
        else if (Player_is_active == 11) {
            //it's Carl's turn. goes like 1

            //MAKE ONE STEP
            if (player3_position == 0){
                //if it's first - make it correct
                pawn_change_place = findViewById(R.id.paws_start3);
                pawn_change_place.setVisibility(View.INVISIBLE);
                pawn_change_place = findViewById(R.id.paws_1);
                pawn_change_place.setImageResource(R.drawable.paw_orange);
                pawn_change_place.setVisibility(View.VISIBLE);
                player3_position = 1;
            }
            else{
                //hide current
                String name = "paws_" + String.valueOf(player3_position);
                int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id);
                pawn_change_place.setVisibility(View.INVISIBLE);

                //show next
                String name2 = "paws_" + String.valueOf((player3_position+1));
                int id2 = getResources().getIdentifier(name2,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id2);
                pawn_change_place.setImageResource(R.drawable.paw_orange);
                pawn_change_place.setVisibility(View.VISIBLE);
                player3_position += 1;
            }
            //CHECK if it's castle tile
            rightful_positions(); //!Update positions
            if (player3_position == 46){
                you_loose(this);
            }
            //CHECK if it's last one
            //yes - change to 12nd state
            else if(player3_position == destination_number){
                Player_is_active = 12;
            }
            //no - continue
        }
        else if (Player_is_active == 12) {
            //it's Carl's turn. goes like 2

            //check hearts
            if(heart_spots.contains(player3_position)){
                //check maximum amount of cards
                if (player3_hand_cur_number < 6){
                    //add card
                    player3_hand_cur_number += 1;
                    String card_hand_id = myCardDeck.add_Card_from_someone(3, player3_hand_cur_number);
                    int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                    cards_change_on_hand = findViewById(id_player_card);
                    assign_picture(0,cards_change_on_hand);
                    cards_change_on_hand.setVisibility(View.VISIBLE);
                }
            }
            else if(broken_spots.contains(player3_position)){
                //check the amount
                if (player3_hand_cur_number > 1) {
                    //retrieve
                    player3_hand_cur_number -= 1;
                    String card_hand_id = myCardDeck.take_Card_from_someone(3, player3_hand_cur_number);
                    int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                    cards_change_on_hand = findViewById(id_player_card);
                    cards_change_on_hand.setVisibility(View.GONE);
                }
            }

            //what color of tile
            //opt: take cards (PURPLE TILE - battle 0, state - next turn 10)
            if (arr_purple_spots.contains(player3_position)){
                //take cards
                if (player3_hand_cur_number < 6){
                    //add card
                    player3_hand_cur_number += 1;
                    String card_hand_id = myCardDeck.add_Card_from_someone(3, player3_hand_cur_number);
                    int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                    cards_change_on_hand = findViewById(id_player_card);
                    assign_picture(0,cards_change_on_hand);
                    cards_change_on_hand.setVisibility(View.VISIBLE);
                }
                //randomly choose player
                boolean player_to_take = rand_gen.nextBoolean();
                if (player_to_take){
                    //Player
                    if (player1_hand_cur_number > 1) {
                        //retrieve
                        player1_hand_cur_number -= 1;
                        String card_hand_id = myCardDeck.take_Card_from_someone(1, player1_hand_cur_number);
                        int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                        cards_change_on_hand = findViewById(id_player_card);
                        cards_change_on_hand.setVisibility(View.GONE);
                    }
                }
                else{
                    //Nancy
                    if (player2_hand_cur_number > 1) {
                        //retrieve
                        player2_hand_cur_number -= 1;
                        String card_hand_id = myCardDeck.take_Card_from_someone(2, player2_hand_cur_number);
                        int id_player_card = getResources().getIdentifier(card_hand_id,"id", getApplicationContext().getPackageName());
                        cards_change_on_hand = findViewById(id_player_card);
                        cards_change_on_hand.setVisibility(View.GONE);
                    }
                }
                //from random
                battle_type = 0;
                Player_is_active = 0; //Go to Players' start
            }
            //opt: check if white
            else if(arr_white_spots.contains(player3_position)){
                //go further
                switch(player3_position){
                    case 9: //to 14
                        destination_number = 14;
                        Player_is_active = 13;
                        break;
                    case 14: //to 22
                        destination_number = 22;
                        Player_is_active = 13;
                        break;
                    case 22:  //to 28
                        destination_number = 28;
                        Player_is_active = 13;
                        break;
                    case 28: //to 37
                        destination_number = 37;
                        Player_is_active = 13;
                        break;
                    case 37: //to 37
                        //Because you can't go any further
                        Player_is_active = 0; //Go to Players' start
                        break;
                }
            }
            //opt: check if black
            else if(arr_black_spots.contains(player3_position)){
                //go back or to start
                switch(player3_position){
                    case 5: //to start
                        destination_number = 0;
                        Player_is_active = 13;
                        break;
                    case 12: //to 5
                        destination_number = 5;
                        Player_is_active = 13;
                        break;
                    case 18:  //to 12
                        destination_number = 12;
                        Player_is_active = 13;
                        break;
                    case 26: //to 18
                        destination_number = 18;
                        Player_is_active = 13;
                        break;
                    case 35: //to 26
                        destination_number = 26;
                        Player_is_active = 13;
                        break;
                    case 41:  //to 35
                        destination_number = 35;
                        Player_is_active = 13;
                        break;
                    case 43: //to 41
                        destination_number = 41;
                        Player_is_active = 13;
                        break;
                }
            }

            //health 1, 2 -strength, 3 -magic, 4 -agility
            else if(arr_red_spots.contains(player3_position)){
                battle_type = 1;
                Player_is_active = 14;
            }
            //Strength
            else if(arr_green_spots.contains(player3_position)){
                battle_type = 2;
                Player_is_active = 14;
            }
            //magic
            else if(arr_blue_spots.contains(player3_position)){
                battle_type = 3;
                Player_is_active = 14;
            }
            //agility
            else if(arr_yellow_spots.contains(player3_position)){
                battle_type = 4;
                Player_is_active = 14;
            }
        }
        else if (Player_is_active == 13) {
            //it's Carl's turn. goes like 3

            //check if destined position is further or not
            if (player3_position < destination_number){
                //further
                //hide prev.
                String name = "paws_" + String.valueOf(player3_position);
                int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id);
                pawn_change_place.setVisibility(View.INVISIBLE);

                //show next
                String name2 = "paws_" + String.valueOf((player3_position+1));
                int id2 = getResources().getIdentifier(name2,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id2);
                pawn_change_place.setImageResource(R.drawable.paw_orange);
                pawn_change_place.setVisibility(View.VISIBLE);

                player3_position += 1;
            }
            else{
                if (player3_position - 1 == 0){
                    pawn_change_place = findViewById(R.id.paws_1);
                    pawn_change_place.setVisibility(View.INVISIBLE);
                    pawn_change_place = findViewById(R.id.paws_start3);
                    pawn_change_place.setVisibility(View.VISIBLE);

                    player3_position = 0;
                }
                else{
                    //hide prev.
                    String name = "paws_" + String.valueOf(player3_position);
                    int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
                    pawn_change_place = findViewById(id);
                    pawn_change_place.setVisibility(View.INVISIBLE);

                    //show next
                    String name2 = "paws_" + String.valueOf((player3_position-1));
                    int id2 = getResources().getIdentifier(name2,"id", getApplicationContext().getPackageName());
                    pawn_change_place = findViewById(id2);
                    pawn_change_place.setImageResource(R.drawable.paw_orange);
                    pawn_change_place.setVisibility(View.VISIBLE);

                    player3_position -= 1;
                }
            }
            rightful_positions(); //!Update positions
            //check if it's over
            //out of 13 we can go only to 0 (Player's turn)
            if (player3_position == destination_number){
                Player_is_active = 0;
            }
        }
        else if (Player_is_active == 14) {
            //it's Carl's turn. goes like 4

            //choose player against
            //if it's bot - open a randomly card battle
            //generate who vs who is fighting
            boolean player_to_take = rand_gen.nextBoolean();
            if (player_to_take){
                //Carl vs Player
                battle_sides = 31;
                TextView show_up_massages;
                show_up_massages = findViewById(R.id.notification_card_choose);
                show_up_massages.setVisibility(View.VISIBLE);
                Player_have_to_choose_card = true;
            }
            else{
                //Carl vs Nancy
                battle_sides = 32;


                //open battle layout
                show_some_layouts = findViewById(R.id.battle_card_field);
                show_some_layouts.setVisibility(View.VISIBLE);

                //assign pictures
                //left is initiator
                cards_change_on_hand = findViewById(R.id.battle_cardLeft);
                int card_value1 = myCardDeck.get_card_assignment_number(3,1);
                assign_picture(card_value1,cards_change_on_hand);

                //Right is receiver
                cards_change_on_hand = findViewById(R.id.battle_cardRight);
                int card_value2 = myCardDeck.get_card_assignment_number(2,1);
                assign_picture(card_value2,cards_change_on_hand);
            }

            Player_is_active = -1;

            //Player_is_active = 0; //!debug
        }
        else if (Player_is_active == 15) {
            //AFTER_BATTLE - go forward

            //check what battle it was
            if (battle_sides/10 == 1){
                //it was player
                String name = "paws_" + String.valueOf(player1_position);
                int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id);
                pawn_change_place.setVisibility(View.INVISIBLE);

                //show next
                String name2 = "paws_" + String.valueOf((player1_position+1));
                int id2 = getResources().getIdentifier(name2,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id2);
                pawn_change_place.setImageResource(R.drawable.pawn_cyan);
                pawn_change_place.setVisibility(View.VISIBLE);
                player1_position += 1;

                rightful_positions();//!Update positions
                //CHECK if it's castle tile
                if (player1_position == 46){
                    you_win(this);
                    //return;
                }
                //CHECK if it's last one
                //yes - change to Nancy Turn
                else if(player1_position == destination_number){
                    Player_is_active = 5;
                }
            }
            else if(battle_sides/10 == 2){
                //it was Nancy
                String name = "paws_" + String.valueOf(player2_position);
                int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id);
                pawn_change_place.setVisibility(View.INVISIBLE);

                //show next
                String name2 = "paws_" + String.valueOf((player2_position+1));
                int id2 = getResources().getIdentifier(name2,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id2);
                pawn_change_place.setImageResource(R.drawable.pawn_purple);
                pawn_change_place.setVisibility(View.VISIBLE);
                player2_position += 1;

                rightful_positions(); //!Update positions
                if (player2_position == 46){
                    you_loose(this);
                }
                //CHECK if it's last one
                //yes - change to 7th state
                else if(player2_position == destination_number){
                    Player_is_active = 10;
                }
            }
            else if(battle_sides/10 == 3){
                //it was Carl
                //hide current
                String name = "paws_" + String.valueOf(player3_position);
                int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id);
                pawn_change_place.setVisibility(View.INVISIBLE);

                //show next
                String name2 = "paws_" + String.valueOf((player3_position+1));
                int id2 = getResources().getIdentifier(name2,"id", getApplicationContext().getPackageName());
                pawn_change_place = findViewById(id2);
                pawn_change_place.setImageResource(R.drawable.paw_orange);
                pawn_change_place.setVisibility(View.VISIBLE);
                player3_position += 1;

                if (player3_position == 46){
                    you_loose(this);
                }
                //CHECK if it's last one
                //yes - change to 12nd state
                else if(player3_position == destination_number){
                    Player_is_active = 0;
                }
            }
            //Player_is_active = 0; //!debug
        }
        else if (Player_is_active == 16) {
            //AFTER_BATTLE - go back

            if (battle_sides/10 == 1){
                //it was player
                if (player1_position - 1 == 0){
                    pawn_change_place = findViewById(R.id.paws_1);
                    pawn_change_place.setVisibility(View.INVISIBLE);
                    pawn_change_place = findViewById(R.id.paws_start);
                    pawn_change_place.setVisibility(View.VISIBLE);

                    player1_position = 0;
                    Player_is_active = 5;
                }
                else{
                    //hide prev.
                    String name = "paws_" + String.valueOf(player1_position);
                    int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
                    pawn_change_place = findViewById(id);
                    pawn_change_place.setVisibility(View.INVISIBLE);

                    //show next
                    String name2 = "paws_" + String.valueOf((player1_position-1));
                    int id2 = getResources().getIdentifier(name2,"id", getApplicationContext().getPackageName());
                    pawn_change_place = findViewById(id2);
                    pawn_change_place.setImageResource(R.drawable.pawn_cyan);
                    pawn_change_place.setVisibility(View.VISIBLE);

                    player1_position -= 1;
                }
                rightful_positions(); //!Update positions
                //out of 3 we can go only to 5 (Nancy's turn)
                if (player1_position == destination_number){
                    Player_is_active = 5;
                }
            }
            else if(battle_sides/10 == 2){
                //it was Nancy
                if (player2_position - 1 == 0){
                    pawn_change_place = findViewById(R.id.paws_1);
                    pawn_change_place.setVisibility(View.INVISIBLE);
                    pawn_change_place = findViewById(R.id.paws_start2);
                    pawn_change_place.setVisibility(View.VISIBLE);

                    player2_position = 0;
                    Player_is_active = 10;
                }
                else{
                    //hide prev.
                    String name = "paws_" + String.valueOf(player2_position);
                    int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
                    pawn_change_place = findViewById(id);
                    pawn_change_place.setVisibility(View.INVISIBLE);

                    //show next
                    String name2 = "paws_" + String.valueOf((player2_position-1));
                    int id2 = getResources().getIdentifier(name2,"id", getApplicationContext().getPackageName());
                    pawn_change_place = findViewById(id2);
                    pawn_change_place.setImageResource(R.drawable.pawn_purple);
                    pawn_change_place.setVisibility(View.VISIBLE);

                    player2_position -= 1;
                }

                rightful_positions(); //!Update positions
                //check if it's over
                //out of 8 we can go only to 10 (Carl's turn)
                if (player2_position == destination_number){
                    Player_is_active = 10;
                }
            }
            else if(battle_sides/10 == 3){
                //it was Carl
                if (player3_position - 1 == 0){
                    pawn_change_place = findViewById(R.id.paws_1);
                    pawn_change_place.setVisibility(View.INVISIBLE);
                    pawn_change_place = findViewById(R.id.paws_start3);
                    pawn_change_place.setVisibility(View.VISIBLE);

                    player3_position = 0;
                    Player_is_active = 0;
                }
                else{
                    //hide prev.
                    String name = "paws_" + String.valueOf(player3_position);
                    int id = getResources().getIdentifier(name,"id", getApplicationContext().getPackageName());
                    pawn_change_place = findViewById(id);
                    pawn_change_place.setVisibility(View.INVISIBLE);

                    //show next
                    String name2 = "paws_" + String.valueOf((player3_position-1));
                    int id2 = getResources().getIdentifier(name2,"id", getApplicationContext().getPackageName());
                    pawn_change_place = findViewById(id2);
                    pawn_change_place.setImageResource(R.drawable.paw_orange);
                    pawn_change_place.setVisibility(View.VISIBLE);

                    player3_position -= 1;
                }

                rightful_positions(); //!Update positions
                //check if it's over
                //out of 13 we can go only to 0 (Player's turn)
                if (player3_position == destination_number){
                    Player_is_active = 0;
                }
            }

            //choose player against
            //if it's bot - open a randomly card battle

            //Player_is_active = 0; //!debug
        }
    }
}