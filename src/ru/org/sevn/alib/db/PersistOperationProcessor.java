package ru.org.sevn.alib.db;

import ru.org.sevn.alib.data.ObjStore.ObjStoreException;

public interface PersistOperationProcessor<T> {
	void beforeAdd(T o) throws ObjStoreException;
	void beforeUpdate(T o) throws ObjStoreException;
	void beforeRemove(T o) throws ObjStoreException;
	void onAdd(T o);
	void onUpdate(T o);
	void onRemove(T o);
}
