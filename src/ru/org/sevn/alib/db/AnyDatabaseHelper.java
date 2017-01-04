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
package ru.org.sevn.alib.db;

import java.sql.SQLException;
import java.util.HashMap;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import ru.org.sevn.alib.data.ObjStore;

public abstract class AnyDatabaseHelper extends OrmLiteSqliteOpenHelper {

	private HashMap<Class, Dao> hashdao = new HashMap<>();
	private HashMap<Class, RuntimeExceptionDao> rehashdao = new HashMap<>();
	private HashMap<Class, ObjStore> hashstore = new HashMap<>();
	
	private Context context;
	public AnyDatabaseHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion, int configFileId) {
		super(context, databaseName, factory, databaseVersion, configFileId);
		this.context = context;
	}

	
	public Context getContext() {
		return context;
	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
	 * value.
	 */
	
	public <OS extends ObjStore<T>, T> OS getObjStore(Class<T> clazz) throws SQLException {
		OS ret = (OS)hashstore.get(clazz);
		if (ret == null) {
			ret = (OS)new SimpleObjStore<T>(getObjDao(clazz));
			hashstore.put(clazz, ret);
		}		
		return ret;
	}
	public <D extends Dao<T, Long>, T> D getObjDao(Class<T> clazz) throws SQLException {
		D ret = (D)hashdao.get(clazz);
		if (ret == null) {
			ret = getDao(clazz);
			hashdao.put(clazz, ret);
		}
		return ret;
	}

	/**
	 * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
	 * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
	 */
	public <D extends RuntimeExceptionDao<T, Long>, T> D getREObjDao(Class<T> clazz) throws SQLException {
		D ret = (D)rehashdao.get(clazz);
		if (ret == null) {
			ret = getRuntimeExceptionDao(clazz);
			rehashdao.put(clazz, ret);
		}
		return ret;
	}
	
	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		hashdao.clear();
		rehashdao.clear();
		hashstore.clear();
	}
}
