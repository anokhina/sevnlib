/*
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
 */
package ru.org.sevn.alib.data.app;

import java.util.Comparator;

public class AppDetailComparator implements Comparator<AppDetail> {
	
	@Override
	public int compare(AppDetail lhs, AppDetail rhs) {
		return compareLabels(lhs.getExtra().getLabel(), rhs.getExtra().getLabel());
	}
	private int compareLabels(String lc, String rc) {
		if (lc != null && rc != null) {
			return lc.compareTo(rc);
		} else if (lc == null && rc == null) {
			return 0;
		} else if (lc == null) {
			return -1;
		} else {
			return 1;
		}
	}

}
