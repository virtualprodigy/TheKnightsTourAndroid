package com.virtualprodigy.theknightstour.Actitivies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.virtualprodigy.theknightstour.R;
import com.virtualprodigy.theknightstour.Utilities.CalculateKnightMove;

import butterknife.Bind;
import butterknife.ButterKnife;

public class KnightsTourMainActivity extends AppCompatActivity {

    @Bind(R.id.knights_moves_display) TextView displayKnightMoves;
    CalculateKnightMove knightMovements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knights_tour_main);
        ButterKnife.bind(this);
        knightMovements = new CalculateKnightMove(this);
        knightMovements.startingPoint();
        displayKnightMoves.setText(knightMovements.printKnightsMovementPattern());
        //this line of code && android:scrollbars = "vertical" in the xml allow the textview to scroll
        displayKnightMoves.setMovementMethod(new ScrollingMovementMethod());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_knights_tour_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
