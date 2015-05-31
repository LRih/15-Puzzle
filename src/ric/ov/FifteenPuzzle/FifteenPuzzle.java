package ric.ov.FifteenPuzzle;

import android.graphics.Point;

import java.io.Serializable;

public final class FifteenPuzzle implements Serializable
{
    //========================================================================= VARIABLES
    private final int[][] _squares;
    private int _moves = 0;

    //========================================================================= INITIALIZE
    public FifteenPuzzle(int gridSize)
    {
        _squares = new int[gridSize][gridSize];
        int id = 0;
        for (int y = 0; y < gridSize; y++)
        {
            for (int x = 0; x < gridSize; x++)
            {
                if (x == gridSize - 1 && y == gridSize - 1) _squares[x][y] = -1;
                else _squares[x][y] = id;
                id++;
            }
        }
    }

    //========================================================================= FUNCTIONS
    public final void click(int x, int y)
    {
        if (isEmpty(x, y)) return;
        Point emptyPt = emptyPt();
        if (x == emptyPt.x) // same column
        {
            int direction = (y - emptyPt.y) / Math.abs(y - emptyPt.y);
            for (int modY = emptyPt.y; modY != y; modY += direction)
                swap(x, modY + direction, x, modY);
            _moves += Math.abs(y - emptyPt.y);
        }
        else if (y == emptyPt.y) // same row
        {
            int direction = (x - emptyPt.x) / Math.abs(x - emptyPt.x);
            for (int modX = emptyPt.x; modX != x; modX += direction)
                swap(modX + direction, y, modX, y);
            _moves += Math.abs(x - emptyPt.x);
        }
    }
    public final void scramble(int moves)
    {
        do
        {
            Point emptyPt = emptyPt();
            Point clickPt = new Point();
            for (int i = 0; i < moves; i++)
            {
                if (rand(0, 1) == 1) // row swap
                    clickPt.set(emptyPt.x, rand(0, size() - 1));
                else // column swap
                    clickPt.set(rand(0, size() - 1), emptyPt.y);
                emptyPt.set(clickPt.x, clickPt.y); // set new empty point
                click(clickPt.x, clickPt.y);
            }
        } while (isSolved());
        _moves = 0;
    }
    private void swap(int x1, int y1, int x2, int y2)
    {
        int tmp = getID(x1, y1);
        setID(x1, y1, getID(x2, y2));
        setID(x2, y2, tmp);
    }

    private static int rand(int min, int max)
    {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    //========================================================================= PROPERTIES
    public final int getID(int x, int y)
    {
        return _squares[x][y];
    }
    private void setID(int x, int y, int id)
    {
        _squares[x][y] = id;
    }
    private int size()
    {
        return _squares.length;
    }
    public final int moves()
    {
        return _moves;
    }

    public final boolean isSolved()
    {
        int id = 0;
        for (int y = 0; y < size(); y++)
        {
            for (int x = 0; x < size(); x++)
            {
                if (x == size() - 1 && y == size() - 1) return true;
                else if (getID(x, y) != id) return false;
                id++;
            }
        }
        throw new RuntimeException("Unreachable code reached.");
    }
    private boolean isEmpty(int x, int y)
    {
        return _squares[x][y] == -1;
    }

    private Point emptyPt()
    {
        for (int y = 0; y < size(); y++)
        {
            for (int x = 0; x < size(); x++)
                if (isEmpty(x, y)) return new Point(x, y);
        }
        throw new RuntimeException("Unreachable code reached.");
    }
}
