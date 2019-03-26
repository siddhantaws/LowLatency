/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.springbasics.jdbctemplate;

/**
 *
 * @author mohit
 */
public class Movie {
    
    private String title;

    /**
     * Get the value of title
     *
     * @return the value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the value of title
     *
     * @param title new value of title
     */
    public void setTitle(String title) {
        this.title = title;
    }

        private String leadActor;

    /**
     * Get the value of leadActor
     *
     * @return the value of leadActor
     */
    public String getLeadActor() {
        return leadActor;
    }

    /**
     * Set the value of leadActor
     *
     * @param leadActor new value of leadActor
     */
    public void setLeadActor(String leadActor) {
        this.leadActor = leadActor;
    }
    private String leadActress;

    /**
     * Get the value of leadActress
     *
     * @return the value of leadActress
     */
    public String getLeadActress() {
        return leadActress;
    }

    /**
     * Set the value of leadActress
     *
     * @param leadActress new value of leadActress
     */
    public void setLeadActress(String leadActress) {
        this.leadActress = leadActress;
    }

        private String type;

    /**
     * Get the value of type
     *
     * @return the value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the value of type
     *
     * @param type new value of type
     */
    public void setType(String type) {
        this.type = type;
    }

        private int id;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(int id) {
        this.id = id;
    }

    public Movie(String title, String leadActor, String leadActress, String type) {
        this.title = title;
        this.leadActor = leadActor;
        this.leadActress = leadActress;
        this.type = type;
    }

    public Movie() {
    }

    @Override
    public String toString() {
        return "Movie{" + "title=" + title + ", leadActor=" + leadActor + ", leadActress=" + leadActress + ", type=" + type + ", id=" + id + '}';
    }
    
    
}
