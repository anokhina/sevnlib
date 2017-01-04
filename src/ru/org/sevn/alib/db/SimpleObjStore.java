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

import com.j256.ormlite.dao.Dao;

import ru.org.sevn.alib.data.ObjStore;

public class SimpleObjStore<T> implements ObjStore<T>{

	private final Dao<T, Long> dao;
	private PersistOperationProcessor<T> persistOperationProcessor; 
	
	public PersistOperationProcessor<T> getPersistOperationProcessor() {
		return persistOperationProcessor;
	}

	public void setPersistOperationProcessor(PersistOperationProcessor<T> persistOperationProcessor) {
		this.persistOperationProcessor = persistOperationProcessor;
	}

	public SimpleObjStore(Dao<T, Long> dao) {
		this.dao = dao;
	}
	
	public Dao<T, Long> getDao() {
		return dao;
	}
	
	@Override
	public void add(T obj) throws ObjStoreException {
		try {
			if (persistOperationProcessor != null) {
				persistOperationProcessor.beforeAdd(obj);
			}
			getDao().create(obj);
			updateSize(1);
			if (persistOperationProcessor != null) {
				persistOperationProcessor.onAdd(obj);
			}
		} catch (SQLException e) {
			throw new ObjStoreException(e);
		}
	}

	private void updateSize(int i) throws ObjStoreException {
		if (size == null) {
			size();
		}
		size += i;
	}
	
	@Override
	public void remove(T obj) throws ObjStoreException {
		try {
			if (persistOperationProcessor != null) {
				persistOperationProcessor.beforeRemove(obj);
			}
			getDao().delete(obj);
			updateSize(-1);
			if (persistOperationProcessor != null) {
				persistOperationProcessor.onRemove(obj);
			}
		} catch (SQLException e) {
			throw new ObjStoreException(e);
		}
	}

	@Override
	public void update(T obj) throws ObjStoreException {
		try {
			if (persistOperationProcessor != null) {
				persistOperationProcessor.beforeUpdate(obj);
			}
			getDao().update(obj);
			if (persistOperationProcessor != null) {
				persistOperationProcessor.onUpdate(obj);
			}
		} catch (SQLException e) {
			throw new ObjStoreException(e);
		}
	}

	protected Long size;
	@Override
	public long size() throws ObjStoreException {
		if (size == null) {
			try {
				size = getDao().countOf();
			} catch (SQLException e) {
				throw new ObjStoreException(e);
			}
		}
		if (size != null) {
			return size;
		}
		return 0;
	}	
}
