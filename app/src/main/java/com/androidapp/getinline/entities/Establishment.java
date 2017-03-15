package com.androidapp.getinline.entities;


import android.graphics.drawable.Drawable;

public class Establishment {

    /**
     * The Establishment Id.
     */
    private String id;

    /**
     * Establishment icon
     */
    private Drawable icon;

    /**
     * The Establishment Name.
     */
    private String name;

    /**
     * The Establishment email.
     */
    private String email;

    /**
     * Establishment queue size
     */
    private String queueSize;

    /**
     * Establishment attendance time average
     */
    private String time;

    /**
     * Establishment website
     */
    private String website;

    /**
     * Establishment constructor
     * @param establishmentIcon Establishment Icon
     * @param establishmentName Establishment Name
     * @param establishmentWebSite Establishment WebSite
     * @param establishmentEmail Establishment Email
     * @param establishmentId Establishment Id
     * @param estQueueSize Establishment Queue Size
     * @param time Establishment Attendance Time Average
     */
    public Establishment(final Drawable establishmentIcon, final String establishmentName, final String establishmentWebSite, final String establishmentEmail, final String establishmentId,
                         final String estQueueSize, final String time) {
        setIcon(establishmentIcon);
        setName(establishmentName);
        setWebSite(establishmentWebSite);
        setEmail(establishmentEmail);
        setId(establishmentId);
        setSize(estQueueSize);
        setTime(time);
    }

    /**
     * Method to get Establishment Id
     * @return Establishment Id
     */
    public final String getId() {
        return id;
    }

    /**
     * Method to set Establishment Id
     * @param establishmentId New establishment id
     */
    public final void setId(final String establishmentId) {
        this.id = establishmentId;
    }

    /**
     * Method to get Establishment icon
     * @return Establishment icon
     */
    public Drawable getIcon(){
        return icon;
    }

    /**
     * Method to set Establishment icon
     * @param icon New establishment icon
     */
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    /**
     * Method to get Establishment name
     * @return Establishment name
     */
    public final String getName() {
        return name;
    }

    /**
     * Method to set Establishment name
     * @param name New establishment name
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * Method to get Establishment email
     * @return Establishment email
     */
    public final String getEmail() {
        return email;
    }

    /**
     * Method to set Establishment email
     * @param email New establishment email
     */
    public final void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Method to get Establishment queue size
     * @return Establishment queue size
     */
    public final String getSize() {
        return queueSize;
    }

    /**
     * Method to set Establishment queue size
     * @param queueSize New establishment size
     */
    public final void setSize(final String queueSize) {
        this.queueSize = queueSize;
    }

    /**
     * Method to set Establishment attendance time average
     * @param time New attendance time average
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Method to get Establishment attendance time average
     * @return Establishment attendance time average
     */
    public String getTime() {
        return time;
    }

    /**
     * Method to get Establishment Web Site
     * @return Establishment website
     */
    public String getWebSite(){
        return website;
    }

    /**
     * Method to set Establishment Web Site
     * @param website New Establishment website
     */
    public void setWebSite(String website) {
        this.website = website;
    }

    @Override
    public final String toString() {
        return "Establishment: " + getName() + "\n id: " + getId()
                + " Email: " + getEmail() + "QueueSize: " + getSize() + "\n";
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Establishment establishment = (Establishment) obj;

        return id == establishment.id && name.equals(establishment.name) && email.equals(establishment.email);
    }
}
