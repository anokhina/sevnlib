/*******************************************************************************
 * Copyright 2016 Veronica Anokhina.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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