package ru.org.sevn.alib.db;

import android.database.Cursor;

public interface Cursor2Object<T> {
	T getObject(Cursor obj);
}
