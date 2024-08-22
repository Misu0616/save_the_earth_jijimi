package com.jica.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyGalleryActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private RecyclerView recyclerViewList;
    private ImageAdapter imageAdapter;
    private List<ImageModel> imageList;
    private List<ImagePicModel> imagepicList;
    private ImagePicAdapter imagepicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gallery);

        // RecyclerView 초기화
        initRecyclerViews();


        // 데이터 로드
       // loadData();
    }

    private void initRecyclerViews() {
        recyclerViewList = findViewById(R.id.doneList);
        int numberOfColumns = 2;

        recyclerViewList.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        imageList = new ArrayList<>();
        imageAdapter = new ImageAdapter(imageList);
        recyclerViewList.setAdapter(imageAdapter);

        imagepicList = new ArrayList<>();
        imagepicAdapter = new ImagePicAdapter(imagepicList);
        recyclerViewList.setAdapter(imagepicAdapter);

        // 데이터 로드
        //loadData();
        loadImagesFromFirestore();
    }

    private void loadData() {
        // Firebase 사용자 인증 초기화
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        String memEmail = firebaseAuth.getCurrentUser().getEmail();
        String safeEmail = memEmail != null ? memEmail.replace(".", ",") : "";

        Map<String, ImageModel> imageMap = new HashMap<>();

        String userId = FirebaseAuth.getInstance().getUid();
        Log.e("noAnswer", "userId     ---- > " + userId);
        if (userId == null) {
            Log.e("Firestore", "User ID is null");
            return;
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.e("noAnswer", "열림?11");
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.e("noAnswer", "열림?22");

                    String downloadUrl = document.getString("downloadUrl");
                    Log.e("noAnswer", "downloadUrl : " + downloadUrl);

                    if (downloadUrl != null) {
                        //ImageModel imageModel = new ImageModel(null,null, false, downloadUrl); // 다른 필드는 null 또는 기본값으로 설정
                        //imageList.add(imageModel);
                        for(int i = 0; i<downloadUrl.length(); i++){
                            imageList.get(i).setImgURL(downloadUrl);
                        }
                        Log.e("noAnswer", "imageList ???? " + imageList);
                    } else {
                        Log.e("noAnswer", "downloadUrl is null");
                    }
                }
                imageAdapter.notifyDataSetChanged();
            } else {
                // 오류 처리
                Log.e("Firestore", "Error getting documents: ", task.getException());
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("memberInfo").child(safeEmail).child("imageInfo");

        // Firebase Realtime Database에서 데이터 로드
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageModel activity = snapshot.getValue(ImageModel.class);
                    Log.e("noAnswer", "activity : " + activity);
                    Log.e("noAnswer", "getImgURL : " + activity.getImgURL());
                    if (activity != null && activity.getImgURL() != null) {
                        imageMap.put(activity.getImgURL(), activity);
                        imageList.add(activity);
                    } else {
                        Log.e("noAnswer", "activity is null or imgURL is null");
                    }
                }

                // Firestore에서 이미지 URL 로드
               // loadImageUrlsFromFirestore(imageMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", databaseError.getMessage());
            }
        });
    }

    private void loadImagesFromFirestore() {
        String userId = FirebaseAuth.getInstance().getUid();
        Log.e("noAnswer", "userId     ---- > " + userId);
        if (userId == null) {
            Log.e("Firestore", "User ID is null");
            return;
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.e("noAnswer", "열림?11");
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.e("noAnswer", "열림?22");

                    String downloadUrl = document.getString("downloadUrl");
                Log.e("noAnswer", "downloadUrl : " + downloadUrl);

                if (downloadUrl != null) {
                    //ImageModel imageModel = new ImageModel(null,null, false, downloadUrl); // 다른 필드는 null 또는 기본값으로 설정
                    //imageList.add(imageModel);
                    for(int i=0; i<downloadUrl.length(); i++){
                    imageList.get(0).setImgURL(downloadUrl);
                    }
                    Log.e("noAnswer", "imageList ====== " + imageList);
                    ImagePicModel imagepicModel = new ImagePicModel(downloadUrl); // 다른 필드는 null 또는 기본값으로 설정
                    imagepicList.add(imagepicModel);
                    Log.e("noAnswer", "imagepicList ???? " + imagepicList);
                } else {
                    Log.e("noAnswer", "downloadUrl is null");
                }
            }
                imageAdapter.notifyDataSetChanged();
            } else {
                // 오류 처리
                    Log.e("Firestore", "Error getting documents: ", task.getException());
                }
            });

    }
}