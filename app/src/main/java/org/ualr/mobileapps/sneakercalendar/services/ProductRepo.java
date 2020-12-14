package org.ualr.mobileapps.sneakercalendar.services;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ProductRepo {
    private static final String COLLECTION_NAME = "releases";
    private final FirebaseFirestore firestore;

    public ProductRepo(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }


    public Query getNewReleases() {
        return firestore.collection(COLLECTION_NAME)
                .orderBy("release_date");
    }
}
