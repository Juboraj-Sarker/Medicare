package com.juborajsarker.medicinealert.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.juborajsarker.medicinealert.R;
import com.juborajsarker.medicinealert.dataparser.ImageSaver;
import com.juborajsarker.medicinealert.model.MedicineModel;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {

    private Context mContext;
    private Activity activity;
    private List<MedicineModel> medicineModelList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, subTitle;
        public ImageView medicineIV, optionIV;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_TV);
            subTitle = (TextView) view.findViewById(R.id.subtitle);
            medicineIV = (ImageView) view.findViewById(R.id.medicine_IV);
            optionIV = (ImageView) view.findViewById(R.id.overflow);

        }
    }


    public MedicineAdapter(Context mContext, List<MedicineModel> medicineAdapterList,Activity activity) {
        this.mContext = mContext;
        this.medicineModelList = medicineAdapterList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicine_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        MedicineModel medicineModel = medicineModelList.get(position);
        holder.title.setText(medicineModel.getMedicineName());
        holder.subTitle.setText(medicineModel.getDate() + " songs");

        // loading album cover using Glide library
       // Glide.with(mContext).load(medicineModel.getImagePath()).into(holder.medicineIV);

        ImageSaver imageSaver = new ImageSaver(mContext, activity);
        imageSaver.loadImage(medicineModel.getImagePath(), holder.medicineIV);

        holder.optionIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.optionIV);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_medicine, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_view:
                    Toast.makeText(mContext, "View", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_edit:
                    Toast.makeText(mContext, "Edit", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_delete:
                    Toast.makeText(mContext, "Delete", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return medicineModelList.size();
    }
}
