package ru.org.sevn.alib.dbgui;

import java.util.LinkedHashMap;
import java.util.Map;

import android.database.Cursor;
import android.database.MergeCursor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import ru.org.sevn.alib.db.CursorMapper;
import ru.org.sevn.alib.db.CursorProducer;

public class ObjectCursorProvider<T> implements  FilterQueryProvider, CursorToStringConverter, AdapterView.OnItemClickListener {
	private LinkedHashMap<String, T> selected = new LinkedHashMap<>();
	private CursorMapper<T> cursorMapper;
	private CursorProducer<CharSequence> cursorProducer;
	private final boolean multiple;
	
	public ObjectCursorProvider(boolean multiple, CursorMapper<T> cursorMapper, CursorProducer<CharSequence> cursorProducer) {
		this.multiple = multiple;
		setCursorMapper(cursorMapper);
		setCursorProducer(cursorProducer);
	}
	
	public CursorMapper<T> getCursorMapper() {
		return cursorMapper;
	}


	public void setCursorMapper(CursorMapper<T> cursorMapper) {
		this.cursorMapper = cursorMapper;
	}


	public CursorProducer<CharSequence> getCursorProducer() {
		return cursorProducer;
	}


	public void setCursorProducer(CursorProducer<CharSequence> cursorProducer) {
		this.cursorProducer = cursorProducer;
	}

	public CharSequence convertToStringObject(T cur) {
		return cursorMapper.convertToStringObject(cur);
	}
	
	@Override
	public CharSequence convertToString(Cursor cursor) {
		return cursorMapper.convertToString(cursor);
	}
	

	@Override
	public Cursor runQuery(CharSequence str) {
		Cursor dbCursor = cursorProducer.getCursor(str);
		int dbCursorCount = dbCursor.getCount(); 
		if (dbCursorCount == 1 && str != null) {
			dbCursor.moveToFirst();
			if (str.equals(convertToString(dbCursor))) {
				setSelected(dbCursor);
			}
		} else if (dbCursorCount == 0) {
			setSelected(null);
		}
		Cursor emptyLine = cursorProducer.getEmptyLineCursor();
		if (emptyLine == null) {
			return dbCursor;
		} else {
			return new MergeCursor(new Cursor[]{emptyLine , dbCursor });
		}
	}

	public void setSelected(Cursor dbCursor) {
		if (!multiple) {
			selected.clear();
		}
		if (dbCursor != null) {
			CharSequence key = convertToString(dbCursor);
			T obj = cursorMapper.getObject(dbCursor);

			selected.put(key.toString(), obj);
	        Log.e("ZZZ----", "+++"+key+":"+obj+":"+selected.size());
		}
	}
	
	public T getSelected() {
		if (selected.size() > 0) {
			for (CharSequence k : selected.keySet()) {
				return selected.get(k);
			}
		}
		return null;
	}

	public Map<String, T> getSelectedCollection() {
		return selected;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ListView lv = (ListView)parent;
		setSelected((Cursor)lv.getAdapter().getItem(position));
	}
	
	public void bind(AutoCompleteTextView parentSpin, SimpleCursorAdapter spadapter) {
		parentSpin.setOnItemClickListener(this);
		spadapter.setFilterQueryProvider(this);
		spadapter.setCursorToStringConverter(this);
	}

}