package com.gmat.terminator.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmat.terminator.R;
import com.gmat.terminator.interfaces.ISectionClickListener;
import com.gmat.terminator.utils.AppUtility;

import java.util.ArrayList;

/**
 * Created by Akanksha on 05-Dec-16.
 */

public class SectionsAdapter extends RecyclerView.Adapter<SectionsAdapter.ViewHolder>  {
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<String> mSectionsArrayList;
    private ISectionClickListener mSectionClickListener;

    public SectionsAdapter(Context inContext, ArrayList<String> inSectionList, ISectionClickListener listener) {
        mContext = inContext;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSectionsArrayList = inSectionList;
        mSectionClickListener = listener;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mSectionsArrayList != null && mSectionsArrayList.size() > 0) {
            final String item = mSectionsArrayList.get(position);
            holder.mSectionName.setText(item);

            holder.mSectionLyt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSectionClickListener.onSectionClicked(item, "");
                }
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mSectionsCard;
        private LinearLayout mSectionLyt;
        private TextView mSectionName;

        public ViewHolder(View view) {
            super(view);
            int gridUnit = (int) (AppUtility.getScreenGridUnit(mContext)/1.5);

            mSectionsCard = (CardView) view.findViewById(R.id.section_card);
            mSectionsCard.setPadding(gridUnit, gridUnit, gridUnit, gridUnit);
            mSectionName = (TextView) view.findViewById(R.id.section_label);
            AppUtility.setRobotoMediumFont(mContext, mSectionName, Typeface.NORMAL);

            mSectionLyt = (LinearLayout) view.findViewById(R.id.card_linear_lyt);
            mSectionLyt.setPadding(gridUnit, gridUnit, gridUnit, gridUnit);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sections_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if(mSectionsArrayList != null){
            return mSectionsArrayList.size();
        }
        return 0;
    }
}
