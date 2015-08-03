package com.samynarrainen.tetera.android;

import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.samynarrainen.tetera.Tetera;

public class AndroidLauncher extends AndroidApplication {

    public Tetera GAME;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(GAME = new Tetera(), config);
	}

    @Override
    protected void onPause() {
        super.onPause();
        GAME.gameState = Tetera.GameState.PAUSE;
    }

    @Override
    protected void onResume() {
        super.onResume();
        GAME.gameState = Tetera.GameState.RUNNING; //TODO resuming element

    }
}
