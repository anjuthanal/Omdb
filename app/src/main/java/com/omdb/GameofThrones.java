package com.omdb;

import java.io.Serializable;
import java.util.ArrayList;

public class GameofThrones implements Serializable {

    public String Title;
    public String Season;
    public String totalSeasons;
    public ArrayList<Episodes> Episodes;
    public String Response;

    public class Episodes implements Serializable {
        public String Title;
        public String Released;
        public String Episode;
        public String imdbRating;
        public String imdbID;

    }

}
