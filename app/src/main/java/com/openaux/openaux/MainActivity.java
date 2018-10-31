package com.openaux.openaux;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;


public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "8cb6abc4f339468e83d7a328411f1f59";
    private static final String REDIRECT_URI = "testschema://callback";
    private SpotifyAppRemote mSpotifyAppRemote;



    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Set the connection parameters
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                    .setRedirectUri(REDIRECT_URI)
                    .showAuthView(true)
                .build();

        SpotifyAppRemote.connect(this, connectionParams, new Connector.ConnectionListener() {
            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                mSpotifyAppRemote = spotifyAppRemote;
                android.util.Log.d("MainActivity", "Connected! Yay");
                //Now you can start interacting with the app Remote
                connected();
            }

            @Override
            public void onFailure(Throwable throwable) {
                android.util.Log.e("MainActivity", throwable.getMessage(), throwable);
                //Something went wrong when trying to connect handle errors here
            }
        });
    }

    private void connected() {
        //Play a playlist
        mSpotifyAppRemote.getPlayerApi().play("spotify:track:2zDj4WjbNDmrRL6Z4YjjCA");
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(new Subscription.EventCallback<PlayerState>() {

                    public void onEvent(PlayerState playerState) {
                        final Track track = playerState.track;
                        if (track != null) {
                            android.util.Log.d("MainActivity", track.name + " by " + track.artist.name);
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        //Aaand we will finish up here
    }

}
