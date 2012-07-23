/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unidue.tagrecognition;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author czyrux
 */
public class TagContainer {
    private String _code;
    private ArrayList<Tag> _tags;
    private Color _color;
    private Boolean _followed;
    
    public TagContainer( String code , Color c ) {
        _code = code;
        _color = c;
        _tags = new ArrayList<Tag>();
        _followed = true;
    }
    
    public TagContainer( String code ) {
        _code = code;
        _color = Color.BLACK;
        _tags = new ArrayList<Tag>();
        _followed = true;
    }
    
    public void setCode ( String code) {
        _code = code;
    }
    
    public String getCode () {
        return _code;
    }
    
    public void setColor ( Color c ) {
        _color = c;
    }
    
    public Color getColor () {
        return _color;
    }
    
    public ArrayList<Tag> getTags () {
        return _tags;
    }
    
    public void setTag( Tag t ) {
        _tags.add(t);
    }

    public void setFollowed(Boolean b) {
        _followed = b;
    }
    
    public Boolean getFollowed() {
        return _followed;
    }
}

