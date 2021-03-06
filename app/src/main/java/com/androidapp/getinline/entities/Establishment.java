package com.androidapp.getinline.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Establishment implements Parcelable {

    /**
     * The Establishment Id.
     */
    private String _id;

    /**
     * Establishment icon
     */
    private String urlPhoto;

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
     * Establishment attendance attendingTime average
     */
    private String attendingTime;

    /**
     * Establishment website
     */
    private String website;

    /**
     * Establishment constructor
     * //* @param establishmentIcon Establishment Icon
     *
     * @param establishmentName    Establishment Name
     * @param establishmentWebSite Establishment WebSite
     * @param establishmentEmail   Establishment Email
     * @param establishmentId      Establishment Id
     * @param estQueueSize         Establishment Queue Size
     * @param attendingTime        Establishment Attendance Time Average
     */
    public Establishment(final String urlPhoto, final String establishmentName, final String establishmentWebSite, final String establishmentEmail, final String establishmentId,
                         final String estQueueSize, final String attendingTime) {
        setUrlPhoto(urlPhoto);
        setName(establishmentName);
        setWebSite(establishmentWebSite);
        setEmail(establishmentEmail);
        set_id(establishmentId);
        setSize(estQueueSize);
        setAttendingTime(attendingTime);
    }

    /**
     * Establishment constructor
     */
    public Establishment() {
    }

    /**
     * Method to get Establishment Id
     *
     * @return Establishment Id
     */
    public final String get_id() {
        return _id;
    }

    /**
     * Method to set Establishment Id
     *
     * @param establishmentId New establishment _id
     */
    public final void set_id(final String establishmentId) {
        this._id = establishmentId;
    }

    /**
     * Method to get Establishment icon
     * @return Establishment icon
     */
    public String getUrlPhoto(){
        return urlPhoto;
    }

    /**
     * Method to set Establishment icon
     * @param urlPhoto New establishment urlPhoto
     */
    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    /**
     * Method to get Establishment name
     *
     * @return Establishment name
     */
    public final String getName() {
        return name;
    }

    /**
     * Method to set Establishment name
     *
     * @param name New establishment name
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * Method to get Establishment email
     *
     * @return Establishment email
     */
    public final String getEmail() {
        return email;
    }

    /**
     * Method to set Establishment email
     *
     * @param email New establishment email
     */
    public final void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Method to get Establishment queue size
     *
     * @return Establishment queue size
     */
    public final String getSize() {
        return queueSize;
    }

    /**
     * Method to set Establishment queue size
     *
     * @param queueSize New establishment size
     */
    public final void setSize(final String queueSize) {
        this.queueSize = queueSize;
    }

    /**
     * Method to set Establishment attendance attendingTime average
     *
     * @param attendingTime New attendance attendingTime average
     */
    public void setAttendingTime(String attendingTime) {
        this.attendingTime = attendingTime;
    }

    /**
     * Method to get Establishment attendance attendingTime average
     *
     * @return Establishment attendance attendingTime average
     */
    public String getAttendingTime() {
        return attendingTime;
    }

    /**
     * Method to get Establishment Web Site
     *
     * @return Establishment website
     */
    public String getWebSite() {
        return website;
    }

    /**
     * Method to set Establishment Web Site
     *
     * @param website New Establishment website
     */
    public void setWebSite(String website) {
        this.website = website;
    }

//    @Override
//    public final String toString() {
//        return "Establishment: " + getName() + "\n _id: " + get_id()
//                + " Email: " + getEmail() + "QueueSize: " + getSize() + "\n";
//    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Establishment establishment = (Establishment) obj;

        return _id == establishment._id && name.equals(establishment.name) && email.equals(establishment.email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.urlPhoto);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.queueSize);
        dest.writeString(this.attendingTime);
        dest.writeString(this.website);
    }

    protected Establishment(Parcel in) {
        this._id = in.readString();
        this.urlPhoto = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.queueSize = in.readString();
        this.attendingTime = in.readString();
        this.website = in.readString();
    }

    public static final Creator<Establishment> CREATOR = new Creator<Establishment>() {
        @Override
        public Establishment createFromParcel(Parcel source) {
            return new Establishment(source);
        }

        @Override
        public Establishment[] newArray(int size) {
            return new Establishment[size];
        }
    };
}
