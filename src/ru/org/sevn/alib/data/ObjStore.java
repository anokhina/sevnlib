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
package ru.org.sevn.alib.data;

public interface ObjStore<T> {
	void add(T o) throws ObjStoreException;
	void remove(T o) throws ObjStoreException;
	void update(T o) throws ObjStoreException;
	long size() throws ObjStoreException;
	
	public static class ObjStoreException extends Exception {
		public ObjStoreException(String msg) {
			super(msg);
		}
		public ObjStoreException(String msg, Throwable tr) {
			super(msg, tr);
		}
		public ObjStoreException(Throwable tr) {
			super(tr);
		}
	}
}
