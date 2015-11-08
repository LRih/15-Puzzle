package ric.ov.FifteenPuzzle;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public final class GameActivity extends Activity
{
    //========================================================================= CONSTANTS
    private static final String SAVENAME_PREFERENCES = "settings";

    //========================================================================= VARIABLES
    private TextView _txtCurrent;
    private TextView _txtBest;
    private Button _btnScramble;

    //========================================================================= INITIALIZE
    protected final void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        _txtCurrent = (TextView)findViewById(R.id.txtCurrent);
        _txtBest = (TextView)findViewById(R.id.txtBest);
        _btnScramble = (Button)findViewById(R.id.btnScramble);

        initializeText();
        initializeEvents();
    }
    private void initializeText()
    {
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "prophit.ttf");
//        TextView txtHeading = (TextView)findViewById(R.id.txtHeading);
//        txtHeading.setTypeface(typeface);
//        _txtCurrent.setTypeface(typeface);
//        _txtBest.setTypeface(typeface);

        _txtCurrent.setText(getString(R.string.current, 0));
        _txtBest.setText(getString(R.string.best, loadBest()));
    }
    private void initializeEvents()
    {
        final GameView view = (GameView)findViewById(R.id.gameView);
        view.setOnMoveListener(new GameView.OnMoveListener()
        {
            public final void onMove(int moves)
            {
                if (_btnScramble.getVisibility() != View.VISIBLE)
                    _txtCurrent.setText(getString(R.string.current, moves));
            }
        });
        view.setOnSolveListener(new GameView.OnSolveListener()
        {
            public final void onSolve(int moves)
            {
                _btnScramble.setVisibility(View.VISIBLE);
                if (loadBest() == 0 || moves < loadBest())
                {
                    saveBest(moves);
                    _txtBest.setText(getString(R.string.best, moves));
                }
            }
        });
        _btnScramble.setOnClickListener(new View.OnClickListener()
        {
            public final void onClick(View v)
            {
                _btnScramble.setVisibility(View.INVISIBLE);
                view.scramble();
            }
        });
    }

    //========================================================================= FUNCTIONS
    private int loadBest()
    {
        return getSharedPreferences(SAVENAME_PREFERENCES, Context.MODE_PRIVATE).getInt(getString(R.string.save_best), 0);
    }
    private void saveBest(int value)
    {
        SharedPreferences.Editor editor = getSharedPreferences(SAVENAME_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putInt(getString(R.string.save_best), value);
        editor.commit();
    }

    //========================================================================= EVENTS
    public final boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.game, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public final boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.menuAbout)
        {
            AboutDialog.Show(this, R.drawable.icon, getString(R.string.app_name), "15 Puzzle by Richard L");
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    protected final void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("currentText", _txtCurrent.getText().toString());
        outState.putBoolean("scrambleVisible", _btnScramble.getVisibility() == View.VISIBLE);
    }
    protected final void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        _txtCurrent.setText(savedInstanceState.getString("currentText"));
        _btnScramble.setVisibility(savedInstanceState.getBoolean("scrambleVisible") ? View.VISIBLE : View.INVISIBLE);
    }
}
