package org.philadelphiagamelab.scavunt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

//TODO: Create listView with list of active games and way to add new games with code,
//TODO: ability to start/load game already on list

public class ChooseGame extends Activity {

    //private static final String TEST_GAME_NAME = "test";
    private static final String TEST_GAME_NAME = "AaronTest1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_game);

        Button startTestGame = (Button)findViewById(R.id.test_game);
        startTestGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loadGame = new Intent(getApplicationContext(), LoadGame.class);
                //Here LoadGame is told load game with name test
                loadGame.putExtra(LoadGame.PHP_TAG_GAME_NAME, TEST_GAME_NAME);
                startActivity(loadGame);
            }
        });
    }
}
