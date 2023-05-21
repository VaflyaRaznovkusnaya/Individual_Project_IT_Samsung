package com.example.cardsraid;

public class Card {
    private int Magica; //blue
    private int Health; //red
    private int Agility; // yellow
    private int Strength; //green
    private int win_walk;
    private int lost_walk;
    private int card_type;

    public Card(int type){
        this.card_type = type;
        switch (type) {
            case 1:
                this.Magica = 3;
                this.Health = 8;
                this.Agility = 5;
                this.Strength = 5;
                this.lost_walk = 5;
                this.win_walk = 6;
                break;
            case 2:
                this.Magica = 6;
                this.Health = 7;
                this.Agility = 5;
                this.Strength = 2;
                this.lost_walk = 7;
                this.win_walk = 5;
                break;
            case 3:
                this.Magica = 5;
                this.Health = 2;
                this.Agility = 2;
                this.Strength = 9;
                this.lost_walk = 5;
                this.win_walk = 3;
                break;
            case 4:
                this.Magica = 7;
                this.Health = 4;
                this.Agility = 6;
                this.Strength = 2;
                this.lost_walk = 4;
                this.win_walk = 2;
                break;
            case 5:
                this.Magica = 2;
                this.Health = 9;
                this.Agility = 4;
                this.Strength = 7;
                this.lost_walk = 2;
                this.win_walk = 3;
                break;
            case 6:
                this.Magica = 1;
                this.Health = 6;
                this.Agility = 9;
                this.Strength = 6;
                this.lost_walk = 4;
                this.win_walk = 8;
                break;
            case 7:
                this.Magica = 9;
                this.Health = 1;
                this.Agility = 6;
                this.Strength = 3;
                this.lost_walk = 5;
                this.win_walk = 4;
                break;
            case 8:
                this.Magica = 7;
                this.Health = 3;
                this.Agility = 8;
                this.Strength = 4;
                this.lost_walk = 3;
                this.win_walk = 5;
                break;
            case 9:
                this.Magica = 4;
                this.Health = 2;
                this.Agility = 8;
                this.Strength = 4;
                this.lost_walk = 4;
                this.win_walk = 7;
                break;
            case 10:
                this.Magica = 8;
                this.Health = 5;
                this.Agility = 3;
                this.Strength = 4;
                this.lost_walk = 6;
                this.win_walk = 4;
                break;
            case 11:
                this.Magica = 2;
                this.Health = 8;
                this.Agility = 4;
                this.Strength = 6;
                this.lost_walk = 5;
                this.win_walk = 8;
                break;
            case 12:
                this.Magica = 3;
                this.Health = 5;
                this.Agility = 7;
                this.Strength = 3;
                this.lost_walk = 8;
                this.win_walk = 6;
                break;
        }

    }

    public int get_Magica(){
        return this.Magica;
    }

    public int get_Health(){
        return this.Health;
    }

    public int get_Strength(){
        return this.Strength;
    }

    public int get_Agility(){
        return this.Agility;
    }

    public int get_lost_walk(){
        return this.lost_walk;
    }

    public int get_win_walk(){
        return this.win_walk;
    }

    public int get_card_type(){return this.card_type;}

}
