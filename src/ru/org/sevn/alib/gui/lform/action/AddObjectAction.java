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
package ru.org.sevn.alib.gui.lform.action;

import java.util.Arrays;

import ru.org.sevn.alib.gui.AppListViewAdapter.UpdateException;
import ru.org.sevn.alib.gui.lform.ListFormFragment;

public abstract class AddObjectAction<T> extends EditObjectAction<T>{

	protected AddObjectAction(ListFormFragment<T> context, int actionId, int titleId, int messageId) {
		super(context, actionId, titleId, messageId);
	}

	protected T getEdited() {
		return null;
	}
	
	protected void update(T obj) throws UpdateException{
		getListForm().getAppListViewAdapter().add(Arrays.asList(obj));
	}
	
}
