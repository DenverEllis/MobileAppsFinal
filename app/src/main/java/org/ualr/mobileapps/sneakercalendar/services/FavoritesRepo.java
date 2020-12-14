package org.ualr.mobileapps.sneakercalendar.services;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.ualr.mobileapps.sneakercalendar.models.Favorite;
import org.ualr.mobileapps.sneakercalendar.models.Product;

public class FavoritesRepo {
    private static final String COLLECTION_NAME = "favorites";
    private final FirebaseFirestore firestore;
    private final FirebaseAuth firebaseAuth;

    public FavoritesRepo(FirebaseFirestore firestore, FirebaseAuth auth) {
        this.firestore = firestore;
        this.firebaseAuth = auth;
    }

    public Query getFavorites() {
        if (firebaseAuth.getCurrentUser() != null) {
            return firestore.collection(COLLECTION_NAME)
                    .whereEqualTo("user_id", firebaseAuth.getCurrentUser().getUid())
                    .orderBy("release_date");
        } else {
            return null;
        }
    }

    public Task<QuerySnapshot> findFavoriteByName(String productName) {
        if (firebaseAuth.getCurrentUser() != null) {
            return firestore.collection(COLLECTION_NAME)
                    .whereEqualTo("user_id", firebaseAuth.getCurrentUser().getUid())
                    .whereEqualTo("name", productName)
                    .get();
        } else {
            return null;
        }
    }

    public Task<Void> removeFavorite(String favoriteId) {
        if (firebaseAuth.getCurrentUser() != null) {
            return firestore.collection(COLLECTION_NAME)
                    .document(favoriteId)
                    .delete();
        } else {
            return null;
        }
    }

    public Task<DocumentReference> addFavorite(String userId, Product product) {
        return firestore.collection(COLLECTION_NAME)
                .add(new Favorite(product.getName(),
                        product.getReleaseDate(),
                        product.getImageUrl().toString(),
                        product.getDescription(),
                        userId,
                        product.getPrice()));
    }
}
