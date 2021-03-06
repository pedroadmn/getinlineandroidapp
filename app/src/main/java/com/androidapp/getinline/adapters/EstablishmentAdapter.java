package com.androidapp.getinline.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidapp.getinline.R;
import com.androidapp.getinline.entities.Establishment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class EstablishmentAdapter extends RecyclerView.Adapter<EstablishmentAdapter.ViewHolder> {

    /**
     * Establishment List
     */
    private List<Establishment> mEstablishments;

    /**
     * Establishment List filtered
     */
    private static List<Establishment> filterList;

    /**
     * The context
     */
    private Context mContext;

    /**
     * View Holder Pattern to improve the scrolling performance. It makes the scrolling much smoother.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView establishmentIcon;
        TextView establishmentName;
        TextView getEstablishmentWebSite;
        TextView establishmentQueueSize;
        TextView establishmentAverageTime;

        /**
         * The View Holder constructor
         *
         * @param view View
         */
        ViewHolder(View view) {
            super(view);
            establishmentIcon = (ImageView) view.findViewById(R.id.iv_establishment_icon);
            establishmentName = (TextView) view.findViewById(R.id.tv_establishment_name);
            getEstablishmentWebSite = (TextView) view.findViewById(R.id.tv_establishment_website);
            establishmentQueueSize = (TextView) view.findViewById(R.id.tv_line_size);
            establishmentAverageTime = (TextView) view.findViewById(R.id.tv_average_minutes);
        }
    }

    /**
     * Establishment Adapter constructor
     *
     * @param context        Context
     * @param establishments Establishments
     */
    public EstablishmentAdapter(final Context context, final List<Establishment> establishments) {
        this.mContext = context;
        this.mEstablishments = establishments;
        EstablishmentAdapter.filterList = new ArrayList<>();
        EstablishmentAdapter.filterList.addAll(this.mEstablishments);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_establishment, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Establishment establishment = filterList.get(position);

        holder.establishmentName.setText(establishment.getName());
        holder.getEstablishmentWebSite.setText(establishment.getWebSite());
        holder.establishmentQueueSize.setText(establishment.getSize());
        holder.establishmentAverageTime.setText(establishment.getAttendingTime());
        Glide.with(getApplicationContext()).load(establishment.getUrlPhoto())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.establishmentIcon);
    }

    @Override
    public final long getItemId(final int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return (null != filterList ? filterList.size() : 0);
    }

    /**
     * Method that text and filter the Establishment List based on the text
     *
     * @param text Text to filter by
     */
    public void filter(final String text) {

        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {
                    filterList.addAll(mEstablishments);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (Establishment establishment : mEstablishments) {
                        if (establishment.getName().toLowerCase().contains(text.toLowerCase())) {
                            filterList.add(establishment);
                        }
                    }
                }

                // Set on UI Thread
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }
}
