package com.gmat.terminator.adapter;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.gmat.terminator.R;
import com.gmat.terminator.utils.AppUtility;

import java.util.ArrayList;

/**
 * Created by Akanksha on 17-Jan-17.
 */

public class AddSectionNameAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mSectionsArraylist;
    private LayoutInflater mLayoutInflater;

    public AddSectionNameAdapter(Context inContext, ArrayList<String> inSectionsArrayList) {
        mContext = inContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        mSectionsArraylist = inSectionsArrayList;
    }

    @Override
    public int getCount() {
        if(mSectionsArraylist != null) {
            return mSectionsArraylist.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if(mSectionsArraylist != null) {
            mSectionsArraylist.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        if(mSectionsArraylist != null) {
            mSectionsArraylist.get(i).hashCode();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.add_sections_item, viewGroup, false);
            int gridUnit = AppUtility.getScreenGridUnitBy32(mContext);
            convertView.setPadding(0, 0, 0, 0);
            ViewHolder holder = ininitializeViewHolder(convertView, gridUnit, position);

            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.ref = position;

        setViewHolder(holder, position);

        return convertView;
    }

    private void setViewHolder(final ViewHolder holder, int position) {
        holder.mSectionLabel.setText("Section " + (position + 1));
        holder.mSectionsEditTxt.setText(mSectionsArraylist.get(position));

        //textwatcher for section name edittext
        holder.mSectionsEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //update data in arraylist
                mSectionsArraylist.set(holder.ref, s.toString());
            }
        });
    }

    private ViewHolder ininitializeViewHolder(View convertView, int gridUnit, int position) {
        ViewHolder holder = new ViewHolder();

        holder.mSectionsInputLyt = (TextInputLayout) convertView.findViewById(R.id.input_layout_sections);
        holder.mSectionsEditTxt = (EditText) convertView.findViewById(R.id.section_name);
        holder.mSectionLabel = (TextView) convertView.findViewById(R.id.section_count);
        return holder;
    }

    public void addMoreSections(ArrayList<String> inSectionsArraylist) {
        mSectionsArraylist = inSectionsArraylist;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private TextView mSectionLabel;
        private TextInputLayout mSectionsInputLyt;
        private EditText mSectionsEditTxt;
        private int ref;

    }
}
