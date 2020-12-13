package org.ualr.mobileapps.sneakercalendar.models;

import android.net.Uri;

import com.google.firebase.firestore.PropertyName;

import java.util.Date;

public class Product {
    private String name;
    private Date releaseDate;
    private Uri imageUrl;

    public Product() {

    }

    public Product(String name, Date releaseDate, Uri imageUrl) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("release_date")
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @PropertyName("image_url")
    public void setImageUrl(String imageUrl) {
        Uri uri = Uri.parse(imageUrl);
        this.imageUrl = uri;
    }

    @PropertyName("release_date")
    public Date getReleaseDate() {
        return releaseDate;
    }

    @PropertyName("image_url")
    public Uri getImageUrl() {
        return imageUrl;
    }
}
