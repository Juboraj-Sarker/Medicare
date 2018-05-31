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
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.juborajsarker.medicinealert.R;
import com.juborajsarker.medicinealert.activity.DetailsAppointmentActivity;
import com.juborajsarker.medicinealert.activity.EditAppointmentActivity;
import com.juborajsarker.medicinealert.broadcastReceiver.AlarmReceiver;
import com.juborajsarker.medicinealert.database.AppointmentDatabase;
import com.juborajsarker.medicinealert.model.AppointmentModel;

import java.util.ArrayList;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> {

    private Context mContext;
    private Activity activity;
    private List<AppointmentModel> appointmentModelList;
    private RecyclerView recyclerView;
    AppointmentAdapter adapter;
    String searchKeyword;



    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView titleTV, doctorNameTV, dateTV, timeTV, locationTV;
        public ImageView  optionIV;
        public CardView fullChildCV;

        public MyViewHolder(View view) {
            super(view);
            titleTV = (TextView) view.findViewById(R.id.appointment_title_TV);
            doctorNameTV = (TextView) view.findViewById(R.id.doctor_name_TV);
            dateTV = (TextView) view.findViewById(R.id.date_TV);
            timeTV = (TextView) view.findViewById(R.id.time_TV);
            locationTV = (TextView) view.findViewById(R.id.location_TV);
            optionIV = (ImageView) view.findViewById(R.id.option_IV);
            fullChildCV = (CardView) view.findViewById(R.id.fullChildCV);



        }
    }



    public AppointmentAdapter(Context context, List<AppointmentModel> appointmentModelList, Activity activity,
                              RecyclerView recyclerView, AppointmentAdapter appointmentAdapter, String searchKeyword){


        this.mContext = context;
        this.appointmentModelList = appointmentModelList;
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.adapter = appointmentAdapter;
        this.searchKeyword = searchKeyword;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final AppointmentModel appointmentModel = appointmentModelList.get(position);
        holder.titleTV.setText(appointmentModel.getAppointmentTitle());
        holder.doctorNameTV.setText(appointmentModel.getDoctorName());
        holder.dateTV.setText(appointmentModel.getDate());
        holder.timeTV.setText(appointmentModel.getTime());
        holder.locationTV.setText(appointmentModel.getLocation());


        holder.optionIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopupMenu(holder.optionIV, appointmentModel, mContext, activity, recyclerView, adapter, searchKeyword);
            }
        });

        holder.fullChildCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailsAppointmentActivity.class);
                intent.putExtra("id", appointmentModel.getId());
                mContext.startActivity(intent);

            }
        });

        holder.fullChildCV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                showPopupMenu(holder.fullChildCV, appointmentModel, mContext, activity, recyclerView, adapter, searchKeyword);
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return appointmentModelList.size();
    }



    private void showPopupMenu(View view, AppointmentModel appointmentModel, Context context,
                               Activity activity, RecyclerView recyclerView, AppointmentAdapter adapter, String searchKeyword){

        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_medicine, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(appointmentModel, context, activity,
                 recyclerView, adapter, searchKeyword));

        popup.show();

    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        AppointmentModel appointmentModel;
        Context context;
        Activity activity;
        RecyclerView recyclerView;
        AppointmentAdapter adapter;
        String searchKeyword;

        public MyMenuItemClickListener(AppointmentModel appointmentModel, Context context, Activity activity,
                                        RecyclerView recyclerView, AppointmentAdapter adapter, String searchKeyword) {

            this.appointmentModel = appointmentModel;
            this.context = context;
            this.activity = activity;
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

                    Intent intent = new Intent(context, DetailsAppointmentActivity.class);
                    intent.putExtra("id", appointmentModel.getId());
                    context.startActivity(intent);
                    return true;
                }
                case R.id.action_edit:{

                    Intent intent = new Intent(context, EditAppointmentActivity.class);
                    intent.putExtra("id", appointmentModel.getId());
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

                                    deleteData(appointmentModel, recyclerView, adapter, context, activity, appointmentModelList, searchKeyword);

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

    private void deleteData(AppointmentModel appointmentModel, RecyclerView recyclerView, AppointmentAdapter adapter,
                            Context context, Activity activity, List<AppointmentModel> appointmentModelList, String searchKeyword) {

        deleteAlarm(appointmentModel, activity, context);

        AppointmentDatabase database = new AppointmentDatabase(context);
        database.deleteAppointment(appointmentModel);

        appointmentModelList = new ArrayList<>();
        appointmentModelList = database.getSelectedAppointment(searchKeyword);
        adapter = new AppointmentAdapter(context, appointmentModelList, activity, recyclerView, adapter, searchKeyword);

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void deleteAlarm(AppointmentModel appointmentModel, Activity activity, Context context) {

        int requestCode = appointmentModel.getRequestCode();

        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        alarmManager.cancel(pendingIntent);


    }


}
