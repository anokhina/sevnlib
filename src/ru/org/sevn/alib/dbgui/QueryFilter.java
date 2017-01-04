package ru.org.sevn.alib.dbgui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryFilter {
	private final StringBuilder selection = new StringBuilder();
	private final ArrayList<String> selectionArgs = new ArrayList<>();
	public String getSelection() {
		return selection.toString();
	}
	public String[] getSelectionArgs() {
		return selectionArgs.toArray(new String[selectionArgs.size()]);
	}
	public void addAnd(String opPattern, String[] args) {
		appendOp(" AND ", opPattern, args);
	}
	public void addOr(String opPattern, String[] args) {
		appendOp(" OR ", opPattern, args);
	}
	private void appendOp(String op, String opPattern, String[] args) {
		if (selection.length() > 0) {
			selection.append(op);
		}
		selection.append(opPattern);
		for (String a : args) {
			selectionArgs.add(a);
		}
	}
	public static String eq(String colName) {
		return colName + " = ? ";
	}
	public static String like(String colName) {
		return colName + " like ? ";
	}
	public JSONObject getJSONObject() throws JSONException {
		JSONObject ret = new JSONObject();
		ret.put("selection", getSelection());
		JSONArray arr = new JSONArray();
		for (String s : selectionArgs) {
			arr.put(s);
		}
		ret.put("selectionArgs", arr);
		
		return ret;
	}
	
	public String toString() {
		try {
			return getJSONObject().toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toString();
	}
}