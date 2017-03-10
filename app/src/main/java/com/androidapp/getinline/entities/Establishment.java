package com.androidapp.getinline.entities;


import android.graphics.drawable.Drawable;

public class Establishment {

    /**
     * The Establishment Id.
     */
    private String id;

    private Drawable icon;

    /**
     * The Establishment Name.
     */
    private String name;
    /**
     * The Establishment email.
     */
    private String email;

    private String queueSize;

    private String time;

    private String website;

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

    public final String getId() {
        return id;
    }

    public final void setId(final String establishmentId) {
        this.id = establishmentId;
    }

    public Drawable getIcon(){
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final String getEmail() {
        return email;
    }

    public final void setEmail(final String email) {
        this.email = email;
    }

    public final String getSize() {
        return queueSize;
    }

    public final void setSize(final String queueSize) {
        this.queueSize = queueSize;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
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

    public String getWebSite(){
        return website;
    }

    public void setWebSite(String website) {
        this.website = website;
    }
}
