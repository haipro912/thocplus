/*
 * Copyright (C) 2016 CaMnter yuanyu.camnter@gmail.com
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
package com.vttm.mochaplus.feature.mvp.call;

import android.widget.TextView;

import com.camnter.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.camnter.easyrecyclerviewsidebar.sections.EasyImageSection;

/**
 * Description：RoundImageSectionAdapter
 * Created by：CaMnter
 * Time：2016-04-12 00:02
 */
public class RoundImageSectionAdapter extends SectionAdapter {

    @Override public int getEasyImageSection() {
        return EasyImageSection.ROUND;
    }


    /**
     * Set header logic
     *
     * @param contacts contacts
     * @param headerTv headerTv
     * @param viewHolder viewHolder
     * @param position position
     */
    @Override
    public void setHeaderLogic(Contacts contacts, TextView headerTv, EasyRecyclerViewHolder viewHolder, int position) {
        if (position != 0 && !contacts.top) {
            Contacts pre = this.getItem(position - 1);
            if (pre.top || !contacts.getHeader().equals(pre.getHeader())) {
                this.setHeader(true, headerTv, contacts.getHeader());
            } else {
                this.setHeader(false, headerTv, null);
            }
        } else {
            this.setHeader(false, headerTv, null);
        }
    }
}
