package org.ualr.mobileapps.sneakercalendar.models;

import android.net.Uri;

import com.google.firebase.firestore.PropertyName;

import java.util.Date;

public class Favorite {
    private String description;
    private String imageUrl;
    private String name;
    private float price;
    private Date releaseDate;
    private String userId;



    public Favorite() {}

    public Favorite(String name, Date releaseDate, String imageUrl, String description,
                    String userId, float price) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
        this.userId = userId;
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
        this.imageUrl = imageUrl;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @PropertyName("release_date")
    public Date getReleaseDate() {
        return releaseDate;
    }

    @PropertyName("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    public Product toProduct() {
        return new Product(name, releaseDate, Uri.parse(imageUrl), description, price);
    }

    @PropertyName("user_id")
    public String getUserId() {
        return userId;
    }

}
