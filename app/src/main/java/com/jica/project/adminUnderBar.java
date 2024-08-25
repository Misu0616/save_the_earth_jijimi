package com.jica.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;


public class adminUnderBar extends Fragment {
    ImageButton btncamera, btnHome, btnGallery, btnSetting;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View underbar =  inflater.inflate(R.layout.fragment_under_menu, container, false);


        btncamera = (ImageButton) underbar.findViewById(R.id.camerabtn);
        btnHome = (ImageButton) underbar.findViewById(R.id.homebtn);
        btnGallery = (ImageButton) underbar.findViewById(R.id.gallerybtn);
        btnSetting = (ImageButton) underbar.findViewById(R.id.settingbtn);

        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "camera 페이지 성공", Toast.LENGTH_SHORT).show();
                Intent camera = new Intent(getActivity(), SearchListActivity.class);
                camera.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(camera);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "home 페이지 성공", Toast.LENGTH_SHORT).show();
                Intent home = new Intent(getActivity(), MyTreeActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(home);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "gallery 페이지 성공", Toast.LENGTH_SHORT).show();
                Intent gallery = new Intent(getActivity(), admin_AddList.class);
                gallery.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(gallery);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그아웃
                firebaseAuth.signOut();

                Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

                // 로그인 화면으로 이동
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        return underbar;
    }

}