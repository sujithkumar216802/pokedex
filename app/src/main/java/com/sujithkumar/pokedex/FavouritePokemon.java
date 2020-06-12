package com.sujithkumar.pokedex;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "favourite_pokemon")
public class FavouritePokemon {

    String evolution;
    String abilities;
    String moves;
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int height;
    private int weight;
    private int baseexperience;
    private int hp;
    private int attack;
    private int defence;
    private int specialattack;
    private int specialdefence;
    private int speed;

    public String getEvolution() {
        return evolution;
    }

    public String getAbilities() {
        return abilities;
    }

    public String getMoves() {
        return moves;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getBaseexperience() {
        return baseexperience;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefence() {
        return defence;
    }

    public int getSpecialattack() {
        return specialattack;
    }

    public int getSpecialdefence() {
        return specialdefence;
    }

    public int getSpeed() {
        return speed;
    }

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;


    public FavouritePokemon(String evolution, String abilities, String moves, int id, String name, int height, int weight, int baseexperience, int hp, int attack, int defence, int specialattack, int specialdefence, int speed, byte[] image) {
        this.evolution = evolution;
        this.abilities = abilities;
        this.moves = moves;
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.baseexperience = baseexperience;
        this.hp = hp;
        this.attack = attack;
        this.defence = defence;
        this.specialattack = specialattack;
        this.specialdefence = specialdefence;
        this.speed = speed;
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

}
