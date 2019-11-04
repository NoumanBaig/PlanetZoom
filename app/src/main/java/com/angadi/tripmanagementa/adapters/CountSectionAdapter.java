package com.angadi.tripmanagementa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.DashboardResult;
import com.angadi.tripmanagementa.models.QrCodeUniqueId;
import com.angadi.tripmanagementa.viewholder.CountFooterViewHolder;
import com.angadi.tripmanagementa.viewholder.CountHeaderViewHolder;
import com.angadi.tripmanagementa.viewholder.CountItemViewHolder;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

public class CountSectionAdapter extends SectionedRecyclerViewAdapter<CountHeaderViewHolder,
        CountItemViewHolder,
        CountFooterViewHolder> {

//    onClickListener onClickListener;
    protected Context context = null;
    List<DashboardResult> dashboardResultList;

    public CountSectionAdapter(Context context, List<DashboardResult> dashboardResultList) {
        this.context = context;
        this.dashboardResultList = dashboardResultList;
    }

//    public interface onClickListener{
//        void onClick(View view, int position, String id);
//    }
//    public void setClickListener(onClickListener onClickListener){
//        this.onClickListener=onClickListener;
//    }

    @Override
    protected int getItemCountForSection(int section) {
        return dashboardResultList.get(section).getQrCodeUniqueId().size();
    }

    @Override
    protected int getSectionCount() {
        return dashboardResultList.size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    protected LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }

    @Override
    protected CountHeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.view_count_header, parent, false);
        return new CountHeaderViewHolder(view);
    }

    @Override
    protected CountFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.view_count_footer, parent, false);
        return new CountFooterViewHolder(view);
    }

    @Override
    protected CountItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.view_count_item, parent, false);
        return new CountItemViewHolder(context,view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(CountHeaderViewHolder holder, int section) {
        holder.render(dashboardResultList.get(section).getQcaaCatName());
    }

    @Override
    protected void onBindSectionFooterViewHolder(CountFooterViewHolder holder, int section) {
        holder.render("Footer " + (section));
    }

//    protected int[] colors = new int[]{0xfff44336, 0xff2196f3, 0xff009688, 0xff8bc34a, 0xffff9800};

    @Override
    protected void onBindItemViewHolder(CountItemViewHolder holder, int section, int position) {
        holder.render(dashboardResultList.get(section).getQrCodeUniqueId().get(position).getQcaaCodeIdSecure(),
                dashboardResultList.get(section).getQrCodeUniqueId().get(position).getQcaaCodeIdSecureLink(),context.getResources().getColor(R.color.white));
    }
}