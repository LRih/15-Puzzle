package ric.ov.FifteenPuzzle;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceView;

public final class GameView extends SurfaceView
{
    //========================================================================= CONSTANTS
    private static final int GRID_SIZE = 4;

    //========================================================================= VARIABLES
    private final Paint _paint = new Paint();
    private final Paint _paintText = new Paint();
    private OnMoveListener _listenerMove = new OnMoveListener();
    private OnSolveListener _listenerSolve = new OnSolveListener();

    private final float _margin;
    private FifteenPuzzle _puzzle;

    //========================================================================= INITIALIZE
    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setWillNotDraw(false);
        _paintText.setAntiAlias(true);
//        _paintText.setTypeface(Typeface.createFromAsset(context.getAssets(), "prophit.ttf"));
        _paintText.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, context.getResources().getDisplayMetrics()));
        _paintText.setTextAlign(Paint.Align.CENTER);
        _margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
        _puzzle = new FifteenPuzzle(GRID_SIZE);
        scramble();
    }

    //========================================================================= FUNCTIONS
    private void click(int gridX, int gridY)
    {
        _puzzle.click(gridX, gridY);
        invalidate();
        _listenerMove.onMove(_puzzle.moves());
        if (_puzzle.isSolved()) _listenerSolve.onSolve(_puzzle.moves());
    }
    public final void scramble()
    {
        _puzzle.scramble(1000);
        invalidate();
        _listenerMove.onMove(_puzzle.moves());
    }
    public final void setOnMoveListener(OnMoveListener listener)
    {
        _listenerMove = listener;
    }
    public final void setOnSolveListener(OnSolveListener listener)
    {
        _listenerSolve = listener;
    }

    private static int getColor(int squareID)
    {
        if (squareID == -1) return Color.TRANSPARENT;
        else if (squareID < 3) return Color.HSVToColor(new float[] { 0, 1, (float)(.85 + squareID * 0.05) });
        else return Color.HSVToColor(new float[] { (squareID - 3) * 5, 1, 1 });
    }

    //========================================================================= PROPERTIES
    private float boardSize()
    {
        return (getWidth() > getHeight() ? getHeight() : getWidth()) - _margin * 2;
    }

    //========================================================================= EVENTS
    protected final void onDraw(Canvas canvas)
    {
        drawBoard(canvas);
    }
    private void drawBoard(Canvas canvas)
    {
        float size = boardSize();
        float startX = (getWidth() - size) / 2;
        float startY = (getHeight() - size) / 2;
        float squareSize = size / GRID_SIZE;
        _paintText.setColor(Color.WHITE);
        _paint.setColor(Color.DKGRAY);
        canvas.drawRect(startX - 1, startY - 1, startX + size + 1, startY + size + 1, _paint);
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int column = 0; column < GRID_SIZE; column++)
            {
                float x = startX + column * squareSize;
                float y = startY + row * squareSize;
                if (_puzzle.getID(column, row) != -1)
                    drawColoredSquare(canvas, x, y, column, row, squareSize);
            }
        }
    }
    private void drawColoredSquare(Canvas canvas, float x, float y, int column, int row, float squareSize)
    {
        String id = String.valueOf(_puzzle.getID(column, row) + 1);
        _paint.setColor(getColor(_puzzle.getID(column, row)));
        canvas.drawRect(x + 1, y + 1, x + squareSize - 1, y + squareSize - 1, _paint);
        canvas.drawText(id, x + squareSize / 2, y + (squareSize - _paintText.ascent() - _paintText.descent()) / 2, _paintText);
    }

    public final boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            float size = boardSize();
            float startX = (getWidth() - size) / 2;
            float startY = (getHeight() - size) / 2;
            float squareSize = size / GRID_SIZE;
            if (event.getX() >= startX && event.getX() < startX + size && event.getY() >= startY && event.getY() < startY + size)
            {
                int x = (int)((event.getX() - startX) / squareSize);
                int y = (int)((event.getY() - startY) / squareSize);
                click(x, y);
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    protected final Parcelable onSaveInstanceState()
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putSerializable("puzzle", _puzzle);
        return bundle;
    }
    protected final void onRestoreInstanceState(Parcelable state)
    {
        Bundle bundle = (Bundle)state;
        _puzzle = (FifteenPuzzle)bundle.getSerializable("puzzle");
        super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
    }

    //========================================================================= CLASSES
    public static class OnMoveListener
    {
        public void onMove(int moves)
        {
        }
    }
    public static class OnSolveListener
    {
        public void onSolve(int moves)
        {
        }
    }
}
