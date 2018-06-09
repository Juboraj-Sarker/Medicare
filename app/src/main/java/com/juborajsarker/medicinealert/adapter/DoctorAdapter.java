package com.juborajsarker.medicinealert.adapter;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.juborajsarker.medicinealert.R;
import com.juborajsarker.medicinealert.activity.doctors.DetailsDoctorActivity;
import com.juborajsarker.medicinealert.activity.doctors.EditDoctorActivity;
import com.juborajsarker.medicinealert.database.DoctorDatabase;
import com.juborajsarker.medicinealert.model.DoctorModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.MyViewHolder> {

    private Context context;
    private List<DoctorModel> doctorModelList;
    private RecyclerView recyclerView;
    private DoctorAdapter adapter;
    String searchKeyword;
    private TextView messageTV;
    private Activity activity;

    public static final int MY_PERMISSIONS_REQUEST_CALL = 55;

    int counter = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView doctorNameTV, doctorPhoneTV;
        public ImageView callIV;
        public CardView fullChildCV;

        public LinearLayout additionalLAYOUT, copyPhoneLAYOUT,  viewDetailsLAYOUT, editLAYOUT, deleteLAYOUT, sendSmsLAYOUT, sendEmailLAYOUT;



        public MyViewHolder(View view){

            super(view);

            doctorNameTV = (TextView) view.findViewById(R.id.doctor_name_TV);
            doctorPhoneTV = (TextView) view.findViewById(R.id.doctor_phone_TV);
            callIV = (ImageView) view.findViewById(R.id.call_IV);
            fullChildCV = (CardView) view.findViewById(R.id.fullChildCV);

            additionalLAYOUT = (LinearLayout) view.findViewById(R.id.additional_LAYOUT);
            copyPhoneLAYOUT = (LinearLayout) view.findViewById(R.id.copy_phone_LAYOUT);
            viewDetailsLAYOUT = (LinearLayout) view.findViewById(R.id.view_details_LAYOUT);
            editLAYOUT = (LinearLayout) view.findViewById(R.id.edit_LAYOUT);
            deleteLAYOUT = (LinearLayout) view.findViewById(R.id.delete_LAYOUT);
            sendSmsLAYOUT = (LinearLayout) view.findViewById(R.id.send_sms_LAYOUT);
            sendEmailLAYOUT = (LinearLayout) view.findViewById(R.id.send_email_LAYOUT);

        }

    }


    public DoctorAdapter(Context context,  List<DoctorModel> doctorModelList,
                         RecyclerView recyclerView, DoctorAdapter adapter, String searchKeyword, TextView messageTV, Activity activity) {

        this.context = context;
        this.doctorModelList = doctorModelList;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.searchKeyword = searchKeyword;
        this.messageTV = messageTV;
        this.activity = activity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_doctor, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final DoctorModel doctorModel = doctorModelList.get(position);

        holder.doctorNameTV.setText(doctorModel.getName());
        holder.doctorPhoneTV.setText(doctorModel.getPhoneNumber());



        holder.callIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeCall(doctorModel.getPhoneNumber());
            }
        });

        holder.fullChildCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                counter ++;

                if (counter % 2 == 1){

                    holder.additionalLAYOUT.setVisibility(View.VISIBLE);

                }else if (counter % 2 == 0){

                    holder.additionalLAYOUT.setVisibility(View.GONE);
                }

            }
        });


        holder.fullChildCV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                counter ++;

                if (counter % 2 == 1){

                    holder.additionalLAYOUT.setVisibility(View.VISIBLE);

                }else if (counter % 2 == 0){

                    holder.additionalLAYOUT.setVisibility(View.GONE);
                }


                return false;
            }
        });


        holder.copyPhoneLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                copyPhone(doctorModel.getPhoneNumber());
            }
        });





        holder.viewDetailsLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = doctorModel.getId();
                Intent intent = new Intent(context, DetailsDoctorActivity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);

            }
        });



        holder.editLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, EditDoctorActivity.class);
                intent.putExtra("id", doctorModel.getId());
                context.startActivity(intent);

            }
        });


        holder.deleteLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



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

                                deleteFile(doctorModel, recyclerView, adapter, context);


                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();


            }
        });


        holder.sendSmsLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendSms(doctorModel.getPhoneNumber());

            }
        });


        holder.sendEmailLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (doctorModel.getEmail().equals("null")){

                    Toast.makeText(context, "No email Address found", Toast.LENGTH_SHORT).show();

                }else {

                    sendEmail(doctorModel.getEmail());
                }

            }
        });

    }




    @Override
    public int getItemCount() {

        return doctorModelList.size();
    }


    private void makeCall(String phoneNumber) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        if (!checkCallPermission()) {

            checkCallPermission();
            Toast.makeText(context, "Please give call permission to make call", Toast.LENGTH_SHORT).show();

        }else {

            context.startActivity(callIntent);
        }

    }


    private void sendSms(String phoneNumber) {

        Uri uri = Uri.parse("smsto:"+ phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", "");
        context.startActivity(intent);

    }


    private void sendEmail(String email) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Type email here");
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));

    }


    private void copyPhone(String phone) {


        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipe = ClipData.newPlainText("phone", phone);

        if (clipboard != null) {
            clipboard.setPrimaryClip(clipe);
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        }

    }


    private void deleteFile(DoctorModel model, RecyclerView recyclerView, DoctorAdapter adapter,  Context context) {

        DoctorDatabase doctorDatabase = new DoctorDatabase(context);
        doctorDatabase.deleteDoctor(model);

        List<DoctorModel> doctorModelList = new ArrayList<>();
        doctorModelList = doctorDatabase.getAllDoctor();

        DoctorAdapter adapters = new DoctorAdapter(context, doctorModelList, recyclerView, adapter, "", messageTV, activity);
        adapters.notifyDataSetChanged();
        recyclerView.setAdapter(adapters);
        adapters.notifyDataSetChanged();

        if (doctorModelList.size() == 0){

            recyclerView.setVisibility(View.GONE);
            messageTV.setVisibility(View.VISIBLE);


        }
    }


    private boolean checkCallPermission() {


        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL);


            return false;

        } else {

            return true;
        }
    }

}
