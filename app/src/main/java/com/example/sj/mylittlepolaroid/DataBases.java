package com.example.sj.mylittlepolaroid;

import android.provider.BaseColumns;

/**
 * Created by SJ on 2015-12-05.
 */
public class DataBases {

    public static final class CreateDB implements BaseColumns {

        public static final String IMAGE = "image";
        public static final String DATE = "date";
        public static final String ADDRESS = "address";
        public static final String EXPLAIN = "explain";
        public static final String X = "x";
        public static final String Y = "y";
        public static final String _TABENAME = "polaroid";

        public static final String _CREATE =
                "create table "+_TABENAME+"("
                        +_ID+" integer primary key autoincrement, "
                        +IMAGE+" text not null , "
                        +DATE+" text not null , "
                        +ADDRESS+" text not null , "
                        +EXPLAIN+" text not null , "
                        + X +" double not null , "
                        + Y +" double not null );";
    }
}
