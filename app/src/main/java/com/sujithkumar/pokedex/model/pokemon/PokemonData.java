package com.sujithkumar.pokedex.model.pokemon;

import com.google.gson.annotations.SerializedName;
import com.sujithkumar.pokedex.model.NameandUrl;

import java.util.List;

public class PokemonData {
    @SerializedName("abilities")
    List<abilities> temp;

    public Integer getId() {
        return id;
    }

    Integer base_experience;
    Integer height;
    Integer id;
    boolean is_default;
    String location_area_encounters;
    @SerializedName("moves")
    List<moves> movess;
    String name;
    Integer order;
    @SerializedName("species")
    NameandUrl species;

    public NameandUrl getSpecies() {
        return species;
    }

    @SerializedName("sprites")
    sprites spritess;
    @SerializedName("stats")
    List<stats> statss;
    @SerializedName("types")
    List<types> typess;
    Integer weight;





    public String getSpritess() {
        return spritess.getFront_default();
    }

    public List<abilities> getAbility() {return temp;}

    public Integer getBase_experience() {
        return base_experience;
    }

    public Integer getHeight() {
        return height;
    }

    public String getLocation_area_encounters() {
        return location_area_encounters;
    }

    public List<moves> getMovess() {
        return movess;
    }

    public String getName() {
        return name;
    }

    public List<stats> getStatss() {
        return statss;
    }

    public Integer getWeight() {
        return weight;
    }
}

class  types{
    Integer slot;
    NameandUrl type;
}

class sprites{
    String back_default;
    String back_female;
    String back_shiny;
    String back_shiny_female;
    String front_default;
    String front_female;
    String front_shiny;
    String front_shiny_female;

    public String getFront_default() {
        return front_default;
    }
}



class game_indices{
    @SerializedName("game_index")
    Integer game_index;
    @SerializedName("version")
    NameandUrl version;
}
