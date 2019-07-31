package com.sofudev.trackapptrl.Custom;

public class CustomSpinner {
    public int id = 0;
    public String name = "";
    public String abbrev = "";

    public CustomSpinner(int _id, String _name, String _abbrev )
    {
        id = _id;
        name = _name;
        abbrev = _abbrev;
    }

    public String toString()
    {
        return( name + " (" + abbrev + ")" );
    }
}
