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
    private ArrayList<Tag> _tags;
    private Color _color;
    
    public TagContainer( Color c ) {
        _color = c;
        _tags = new ArrayList<Tag>();
    }
    
    public TagContainer() {
        _color = Color.BLACK;
        _tags = new ArrayList<Tag>();
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
}
