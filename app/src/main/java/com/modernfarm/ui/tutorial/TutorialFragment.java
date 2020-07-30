package com.modernfarm.ui.tutorial;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.modernfarm.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class TutorialFragment extends Fragment {
    private View view;
    private YouTubePlayerView playerView, playerView2, playerView3, playerView4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        // Inisialisasi View
        playerView = view.findViewById(R.id.youtube_player_view);
        playerView2 = view.findViewById(R.id.youtube_player_view2);
        playerView3 = view.findViewById(R.id.youtube_player_view3);
        playerView4 = view.findViewById(R.id.youtube_player_view4);

        getLifecycle().addObserver(playerView);
        getLifecycle().addObserver(playerView2);
        getLifecycle().addObserver(playerView3);
        getLifecycle().addObserver(playerView4);

        // Membuat Listerner dimasing masing view dengan menyertakan LINK
        playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                String videoId = "afzmvhnPoHg";
//                youTubePlayer.loadVideo(videoId, 0f);
            }
        });
        playerView2.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                String videoId = "mG4By7kEr0g";
//                youTubePlayer.loadVideo(videoId, 0f);
            }
        });
        playerView3.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                String videoId = "KTeceeEyZyo";
//                youTubePlayer.loadVideo(videoId, 0f);
            }
        });
        playerView4.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                String videoId = "rgFZfTaNOmY";
//                youTubePlayer.loadVideo(videoId, 0f);
            }
        });

        return view;
    }
}