package com.juborajsarker.medicinealert.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.juborajsarker.medicinealert.R;
import com.juborajsarker.medicinealert.activity.DetailsActivity;
import com.juborajsarker.medicinealert.dataparser.ImageSaver;
import com.juborajsarker.medicinealert.model.MedicineModel;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {

    private Context mContext;
    private Activity activity;
    private List<MedicineModel> medicineModelList;

    String daysInterval ;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, subTitle;
        public ImageView medicineIV, optionIV;
        public CardView fullChildCV;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_TV);
            subTitle = (TextView) view.findViewById(R.id.subtitle);
            medicineIV = (ImageView) view.findViewById(R.id.medicine_IV);
            optionIV = (ImageView) view.findViewById(R.id.overflow);
            fullChildCV = (CardView) view.findViewById(R.id.fullChildCV);



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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MedicineModel medicineModel = medicineModelList.get(position);
        holder.title.setText(medicineModel.getMedicineName());
        holder.subTitle.setText(medicineModel.getFirstSlotTime());

        ImageSaver imageSaver = new ImageSaver(mContext, activity);
        imageSaver.loadImage(medicineModel.getImagePath(), holder.medicineIV, medicineModel.getMedicineType());

        holder.optionIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.optionIV, medicineModel, mContext);
            }
        });

        holder.fullChildCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendDataToDetailsActivity(medicineModel, mContext);
            }
        });
    }


    private void showPopupMenu(View view, MedicineModel medicineModel, Context context) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_medicine, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(medicineModel, context));
        popup.show();
    }


    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        MedicineModel medicineModel;
        Context context;

        public MyMenuItemClickListener(MedicineModel medicineModel, Context context) {
            this.medicineModel = medicineModel;
            this.context = context;
        }

        public MyMenuItemClickListener() {

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_view:{

                    sendDataToDetailsActivity(medicineModel, context);

                    return true;
                }
                case R.id.action_edit:{


                    return true;
                }
                case R.id.action_delete:{


                    return true;
                }
                default:
            }
            return false;
        }
    }

    private void sendDataToDetailsActivity(MedicineModel medicineModel, Context context) {

        String medName = medicineModel.getMedicineName();
        String dateTime = medicineModel.getDate();
        String numberOfSlot = String.valueOf(medicineModel.getNumberOfSlot());
        String firstSlotTime = medicineModel.getFirstSlotTime();
        String secondSlotTime = medicineModel.getSecondSlotTime();
        String thirdSlotTime = medicineModel.getThirdSlotTime();
        String numberOfDays = String.valueOf(medicineModel.getNumberOfDays());
        String startDate = medicineModel.getStartDate();


        if ( (medicineModel.getDaysNameOfWeek().equals("null") ||
                medicineModel.getDaysNameOfWeek().equals(""))
                &&
                ( medicineModel.getDaysInterval() == 0)){

            daysInterval = "EveryDay";

        }else if (medicineModel.getDaysInterval() > 0){

            daysInterval = String.valueOf(medicineModel.getDaysInterval());

        }else if ( ! medicineModel.getDaysNameOfWeek().equals("")
                || ! medicineModel.getDaysNameOfWeek().equals("null")){

            daysInterval = medicineModel.getDaysNameOfWeek();
        }


        String status = medicineModel.getStatus();
        String imagePath = medicineModel.getImagePath();
        String type = medicineModel.getMedicineType();

        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("medName", medName);
        intent.putExtra("dateTime", dateTime);
        intent.putExtra("numberOfSlot",numberOfSlot );
        intent.putExtra("firstSlotTime", firstSlotTime);
        intent.putExtra("secondSlotTime", secondSlotTime);
        intent.putExtra("thirdSlotTime", thirdSlotTime);
        intent.putExtra("numberOfDays", numberOfDays);
        intent.putExtra("startDate", startDate);
        intent.putExtra("daysInterval",daysInterval );
        intent.putExtra("status", status);
        intent.putExtra("imagePath", imagePath);
        intent.putExtra("type", type);

        //    Log.d("DaysInterVal", "Never In");

        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return medicineModelList.size();
    }
}
