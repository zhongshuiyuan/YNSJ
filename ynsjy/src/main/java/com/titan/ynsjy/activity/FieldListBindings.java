/*
 * Copyright 2016, The Android Open Source Project
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

package com.titan.ynsjy.activity;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.titan.model.TitanField;

import java.util.List;

/**
 * Contains {@link BindingAdapter}s for the {@link TitanField} list.
 */
public class FieldListBindings {


    @SuppressWarnings("unchecked")
    @BindingAdapter({"items"})
    public static void setItems(RecyclerView recyclerView, List<TitanField> items) {
        AdapterListObj adapter = (AdapterListObj) recyclerView.getAdapter();
        if (adapter != null)
        {

            adapter.replaceData(items);
        }
    }
}
