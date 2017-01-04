package ru.org.sevn.alib.db;

import android.database.Cursor;

public interface CursorProducer<T> {
	Cursor getCursor(T param);
	Cursor getEmptyLineCursor();
}