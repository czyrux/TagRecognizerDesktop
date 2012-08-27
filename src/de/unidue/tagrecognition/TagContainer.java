package de.unidue.tagrecognition;

import java.awt.Color;
import java.util.ArrayList;

/**
 * @file TagContainer.java
 * @brief Implementation container to hold information about tags during time.
 * @author Antonio Manuel Gutierrez Martinez
 * @version 1.0
 */
public class TagContainer {
    /** Tag code */
    private String _code;
    /** Different tag positions */
    private ArrayList<Tag> _tags;
    /** Colour of tag */
    private Color _color;
    /** Indicates if the tag is followed by the user or not*/
    private Boolean _followed;
    
    /**
     * Constructor
     */
    public TagContainer( String code , Color c ) {
        _code = code;
        _color = c;
        _tags = new ArrayList<Tag>();
        _followed = true;
    }
    
    /**
     * Constructor
     */
    public TagContainer( String code ) {
        _code = code;
        _color = Color.BLACK;
        _tags = new ArrayList<Tag>();
        _followed = true;
    }
    
    /**
     * Set code value
     * @param code 
     */
    public void setCode ( String code) {
        _code = code;
    }
    
    /**
     * Get code value
     * @return code
     */
    public String getCode () {
        return _code;
    }
    
    /**
     * Set color value
     * @param c 
     */
    public void setColor ( Color c ) {
        _color = c;
    }
    
    /**
     * Get color value
     * @return color
     */
    public Color getColor () {
        return _color;
    }
    
    /**
     * Return tags array
     * @return 
     */
    public ArrayList<Tag> getTags () {
        return _tags;
    }
    
    /**
     * Add tag position information
     * @param t 
     */
    public void setTag( Tag t ) {
        _tags.add(t);
    }

    /**
     * Set boolean value
     * @param b 
     */
    public void setFollowed(Boolean b) {
        _followed = b;
    }
    
    /**
     * Get state of followed value
     * @return 
     */
    public Boolean getFollowed() {
        return _followed;
    }
}

