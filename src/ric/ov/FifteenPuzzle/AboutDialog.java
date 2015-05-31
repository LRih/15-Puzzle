package ric.ov.FifteenPuzzle;

import android.app.AlertDialog;
import android.content.Context;

public final class AboutDialog
{
    //========================================================================= INITIALIZE
    private AboutDialog()
    {
        throw new AssertionError();
    }

    //========================================================================= FUNCTIONS
    public static void Show(final Context context, int iconID, String title, String message)
    {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setIcon(iconID);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }
}
