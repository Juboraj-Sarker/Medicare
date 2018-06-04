package com.juborajsarker.medicinealert.adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.juborajsarker.medicinealert.R;
import com.juborajsarker.medicinealert.activity.medicines.DetailsMedicineActivity;
import com.juborajsarker.medicinealert.activity.medicines.EditMedicineActivity;
import com.juborajsarker.medicinealert.broadcastReceiver.AlarmReceiver;
import com.juborajsarker.medicinealert.database.AlarmDatabase;
import com.juborajsarker.medicinealert.database.MedicineDatabase;
import com.juborajsarker.medicinealert.dataparser.ImageSaver;
import com.juborajsarker.medicinealert.model.AlarmModel;
import com.juborajsarker.medicinealert.model.MedicineModel;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {

    private Context mContext;
    private Activity activity;
    private List<MedicineModel> medicineModelList;
    private String tableName;
    private RecyclerView recyclerView;
    MedicineAdapter adapter;
    String searchKeyword;

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


    public MedicineAdapter(Context mContext, List<MedicineModel> medicineAdapterList, Activity activity,
                           String tableName, RecyclerView recyclerView, MedicineAdapter adapter, String searchKeyword) {
        this.mContext = mContext;
        this.medicineModelList = medicineAdapterList;
        this.activity = activity;
        this.tableName = tableName;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.searchKeyword = searchKeyword;
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
                showPopupMenu(holder.optionIV, medicineModel, mContext, tableName, activity, recyclerView, adapter, searchKeyword);
            }
        });

        holder.fullChildCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendDataToDetailsActivity(medicineModel, mContext);
            }
        });

        holder.fullChildCV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                showPopupMenu(holder.fullChildCV, medicineModel, mContext, tableName, activity, recyclerView, adapter, searchKeyword);

                return false;
            }
        });
    }


    private void showPopupMenu(View view, MedicineModel medicineModel, Context context, String tableName,
                               Activity activity, RecyclerView recyclerView, MedicineAdapter adapter, String searchKeyword) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_medicine, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(medicineModel, context, activity,
                tableName, recyclerView, adapter, searchKeyword));

        popup.show();
    }


    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        MedicineModel medicineModel;
        Context context;
        Activity activity;
        String tableName;
        RecyclerView recyclerView;
        MedicineAdapter adapter;
        String searchKeyword;

        public MyMenuItemClickListener(MedicineModel medicineModel, Context context, Activity activity,
                                       String tableName, RecyclerView recyclerView, MedicineAdapter adapter, String searchKeyword) {
            this.medicineModel = medicineModel;
            this.context = context;
            this.activity = activity;
            this.tableName = tableName;
            this.recyclerView = recyclerView;
            this.adapter = adapter;
            this.searchKeyword = searchKeyword;
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

                    Intent intent = new Intent(context, EditMedicineActivity.class);
                    intent.putExtra("tableName", tableName);
                    intent.putExtra("id", medicineModel.getId());
                    context.startActivity(intent);

                    return true;
                }
                case R.id.action_delete:{



                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Continue with deletion")
                            .setMessage("Are you sure you want to really delete this data?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    deleteFile(medicineModel, context, medicineModelList, activity, tableName,
                                            recyclerView, adapter, searchKeyword );;
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();






                    return true;
                }
                default:
            }
            return false;
        }
    }

    private void deleteFile(MedicineModel medicineModel, Context context, List<MedicineModel> medicineModelList,
                            Activity activity, String tableName, RecyclerView recyclerView, MedicineAdapter adapter,
                            String searchKeyword) {


        MedicineDatabase databaseHelper = new MedicineDatabase(context);
        databaseHelper.deleteData(medicineModel, tableName);

        medicineModelList.clear();
        medicineModelList = databaseHelper.getSelectedList(searchKeyword, tableName);
        adapter = new MedicineAdapter(context, medicineModelList, activity, tableName,
                recyclerView, adapter, searchKeyword);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();




        AlarmDatabase alarmDatabase = new AlarmDatabase(context);
        String searchKeywordForAlarm = medicineModel.getMedicineName() + medicineModel.getUniqueCode();
        List<AlarmModel> alarmModelList = alarmDatabase.getSelectedAlarm(searchKeywordForAlarm);
        AlarmModel alarmModel = alarmModelList.get(0);

        int firstRC = alarmModel.getFirstSlotRequestCode();
        int secondRC = alarmModel.getSecondSlotRequestCode();
        int thirdRC = alarmModel.getThirdSlotRequestCode();

        if (firstRC <=0 ){

            Log.d("msg","no need for do anything");

        }else {

            AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("medName", medicineModel.getMedicineName());
            intent.putExtra("imagePath", medicineModel.getImagePath());
            intent.putExtra("mealStatus", medicineModel.getMedicineMeal());
            intent.putExtra("time", medicineModel.getDate() + " " + medicineModel.getFirstSlotTime());
            intent.putExtra("medType", medicineModel.getMedicineType());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, firstRC, intent, 0);
            alarmManager.cancel(pendingIntent);

        }


        if (secondRC <=0){

            Log.d("msg","no need for do anything");

        }else {

            AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("medName", medicineModel.getMedicineName());
            intent.putExtra("imagePath", medicineModel.getImagePath());
            intent.putExtra("mealStatus", medicineModel.getMedicineMeal());
            intent.putExtra("time", medicineModel.getDate() + " " + medicineModel.getSecondSlotTime());
            intent.putExtra("medType", medicineModel.getMedicineType());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, secondRC, intent, 0);
            alarmManager.cancel(pendingIntent);

        }



        if (thirdRC <=0){

            Log.d("msg","no need for do anything");

        }else {

            AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("medName", medicineModel.getMedicineName());
            intent.putExtra("imagePath", medicineModel.getImagePath());
            intent.putExtra("mealStatus", medicineModel.getMedicineMeal());
            intent.putExtra("time", medicineModel.getDate() + " " + medicineModel.getThirdSlotTime());
            intent.putExtra("medType", medicineModel.getMedicineType());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, thirdRC, intent, 0);
            alarmManager.cancel(pendingIntent);

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

        Intent intent = new Intent(context, DetailsMedicineActivity.class);
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
