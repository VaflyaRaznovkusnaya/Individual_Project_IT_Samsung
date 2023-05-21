package com.example.cardsraid;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;

public class CardDeck {
    private ArrayList<Card> cards;
    private Card[] player_1_hand = new Card[6];
    private Card[] player_2_hand = new Card[6];
    private Card[] player_3_hand = new Card[6];



    public CardDeck(){
        this.cards = new ArrayList<>();

        for (int i = 0; i < 48; i++){
            Card temp_card = new Card(((i % 12) + 1));
            this.cards.add(temp_card);
        }
        Collections.shuffle(this.cards);

        for (int i = 0; i < 4; i++){
            player_1_hand[i] = this.cards.get(0);
            this.cards.add(player_1_hand[i]);
            this.cards.remove(0);
        }
        for (int i = 0; i < 4; i++){
            player_2_hand[i] = this.cards.get(0);
            this.cards.add(player_2_hand[i]);
            this.cards.remove(0);
        }
        for (int i = 0; i < 4; i++){
            player_3_hand[i] = this.cards.get(0);
            this.cards.add(player_3_hand[i]);
            this.cards.remove(0);
        }

    }

    public int calculate_win(int PvP, int battle_type, int player_card_num){
        int player_value;
        int nancy_value;
        int carl_value;
        switch(battle_type){
            case 1: // Health
                player_value = player_1_hand[player_card_num-1].get_Health();
                nancy_value = player_2_hand[0].get_Health();
                carl_value = player_3_hand[0].get_Health();
                break;
            case 2: // Strength
                player_value = player_1_hand[player_card_num-1].get_Strength();
                nancy_value = player_2_hand[0].get_Strength();
                carl_value = player_3_hand[0].get_Strength();
                break;
            case 3: // Magic
                player_value = player_1_hand[player_card_num-1].get_Magica();
                nancy_value = player_2_hand[0].get_Magica();
                carl_value = player_3_hand[0].get_Magica();
                break;
            case 4: // Agility
                player_value = player_1_hand[player_card_num-1].get_Agility();
                nancy_value = player_2_hand[0].get_Agility();
                carl_value = player_3_hand[0].get_Agility();
                break;
            default:
                player_value = 0;
                nancy_value = 0;
                carl_value = 0;
                break;
        }
        if (PvP/10 == 1){
            //player is initializing
            //Right is receiver
            if(PvP%10 == 2){
                //Nancy
                if (player_value > nancy_value){
                    //player won
                    return 18;
                } else if (player_value < nancy_value) {
                    //player loose
                    return 19;
                }
                else{
                    //no-one won
                    return 10;
                }
            }
            else if(PvP%10 == 3){
                //Carl
                if (player_value > carl_value){
                    //player won
                    return 18;
                } else if (player_value < carl_value) {
                    //player loose
                    return 19;
                }
                else{
                    //no-one won
                    return 10;
                }
            }
        }
        else if(PvP/10 == 2){
            //Nancy
            //Right is receiver
            //Right is receiver
            if(PvP%10 == 1){
                //Nancy
                if (nancy_value > player_value){
                    //Nancy won
                    return 28;
                } else if (nancy_value < player_value) {
                    //Nancy loose
                    return 29;
                }
                else{
                    //no-one won
                    return 20;
                }
            }
            else if(PvP%10 == 3){
                //Carl
                if (nancy_value > carl_value){
                    //player won
                    return 28;
                } else if (nancy_value < carl_value) {
                    //player loose
                    return 29;
                }
                else{
                    //no-one won
                    return 20;
                }
            }
        }
        else if(PvP/10 == 3){
            //Carl
            //Right is receiver
            //Right is receiver
            if(PvP%10 == 2){
                //Nancy
                if (carl_value > nancy_value){
                    //player won
                    return 38;
                } else if (carl_value < nancy_value) {
                    //player loose
                    return 39;
                }
                else{
                    //no-one won
                    return 30;
                }
            }
            else if(PvP%10 == 1){
                //Carl
                if (carl_value > player_value){
                    //player won
                    return 38;
                } else if (carl_value < player_value) {
                    //player loose
                    return 39;
                }
                else{
                    //no-one won
                    return 30;
                }
            }
        }
        return 0;
    }

    public int get_way_forward(int who, int card_index){
        switch(who){
            case 1:
                return player_1_hand[card_index-1].get_win_walk();
            case 2:
                return player_2_hand[card_index].get_win_walk(); // index 0
            case 3:
                return player_3_hand[card_index].get_win_walk(); // index 0
            default:
                return 0;
        }
    }
    public int get_way_backward(int who, int card_index){
        switch(who){
            case 1:
                return player_1_hand[card_index-1].get_lost_walk();
            case 2:
                return player_2_hand[card_index].get_lost_walk(); // index 0
            case 3:
                return player_3_hand[card_index].get_lost_walk(); // index 0
            default:
                return 0;
        }
    }


    public int get_card_assignment_number (int who, int card_index){
        if (who == 1){
            Card temp = player_1_hand[card_index-1];
            return (temp.get_card_type());
        }
        else if(who == 2){
            Card temp = player_2_hand[card_index-1];
            return (temp.get_card_type());
        }
        else {
            Card temp = player_3_hand[card_index-1];
            return (temp.get_card_type());
        }
    }
    public String add_Card_from_someone(int who, int card_index){
        //1 - player, 2 - enemy2(nancy), 3 - enemy3 (carl)
        if (who == 1){
            player_1_hand[card_index-1] = this.cards.get(0);
            this.cards.add(player_1_hand[card_index-1]);
            this.cards.remove(0);

            return ("player_card" + card_index);
        }
        else if(who == 2){
            player_2_hand[card_index-1] = this.cards.get(0);
            this.cards.add(player_2_hand[card_index-1]);
            this.cards.remove(0);

            return("enemy2_card"+ card_index);
        }
        else {
            player_3_hand[card_index-1] = this.cards.get(0);
            this.cards.add(player_3_hand[card_index-1]);
            this.cards.remove(0);

            return("enemy3_card" + card_index);
        }
    }

    public String take_Card_from_someone(int who, int card_index){
        //1 - player, 2 - enemy2(nancy), 3 - enemy3 (carl)
        if (who == 1){
            return ("player_card" + card_index);
        }
        else if(who == 2){
            return("enemy2_card" + card_index);
        }
        else {
            return("enemy3_card" + card_index);
        }
    }


    public String change_card_assigned (int who, int card_index){
        if (who == 1){
            player_1_hand[card_index-1] = this.cards.get(0);
            this.cards.add(player_1_hand[card_index-1]);
            this.cards.remove(0);

            return ("player_card" + card_index);
        }
        else if(who == 2){
            player_2_hand[0] = this.cards.get(0);
            this.cards.add(player_2_hand[0]);
            this.cards.remove(0);

            return("enemy2_card1");
        }
        else {
            player_3_hand[0] = this.cards.get(0);
            this.cards.add(player_3_hand[0]);
            this.cards.remove(0);

            return("enemy3_card1");
        }
    }
}
