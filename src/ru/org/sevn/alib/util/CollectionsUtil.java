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
package ru.org.sevn.alib.util;

import java.util.Collection;
import java.util.List;

public class CollectionsUtil {
    
    public static <T> void copy(Collection<T> dest, Collection<T> src) {
        for(T o : src) {
            dest.add(o);
        }
    }
    
    public static <T> void moveListItem(final List<T> list, final int from, final int to) {
        int incrmnt = -1;

        if (from < to) {
            incrmnt = 1;
        }

        T fromObj = list.get(from);

        for (int i = from; i != to; i += incrmnt) {
            list.set(i, list.get(i + incrmnt));
        }

        list.set(to, fromObj);
    }
}
