package com.juborajsarker.medicare.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.activity.blood.DonorDetailsActivity;
import com.juborajsarker.medicare.model.DonorModel;

import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.MyViewHolder> {

    private Context context;
    private List<DonorModel> donorModelList;
    private RecyclerView recyclerView;
    private DonorAdapter adapter;
    private Activity activity;

    public static final int MY_PERMISSIONS_REQUEST_CALL = 55;

    int counter = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTV, bgTV, cityTV;
        public LinearLayout fullChildCV;
        public ImageView callIV;
        public LinearLayout additionalLAYOUT, viewDetailsLAYOUT, copyPhoneLAYOUT, sendSmsLAYOUT, sendEmailLAYOUT ;

        public MyViewHolder(View view){

            super(view);

            nameTV = view.findViewById(R.id.nameTV);
            bgTV = view.findViewById(R.id.bg_TV);
            cityTV = view.findViewById(R.id.city_TV);
            callIV = view.findViewById(R.id.call_IV);

            fullChildCV = view.findViewById(R.id.fullChildCV);

            additionalLAYOUT = view.findViewById(R.id.additional_LAYOUT);
            viewDetailsLAYOUT = view.findViewById(R.id.view_details_LAYOUT);
            copyPhoneLAYOUT = view.findViewById(R.id.copy_phone_LAYOUT);
            sendSmsLAYOUT = view.findViewById(R.id.send_sms_LAYOUT);
            sendEmailLAYOUT = view.findViewById(R.id.send_email_LAYOUT);
        }
    }


    public DonorAdapter (Context context, List<DonorModel> donorModelList, RecyclerView recyclerView, DonorAdapter adapter, Activity activity){

        this.context = context;
        this.donorModelList = donorModelList;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.activity = activity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_donor, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final DonorModel donorModel = donorModelList.get(position);

        holder.nameTV.setText(donorModel.getName());
        holder.bgTV.setText(donorModel.getBloodGroup());
        holder.cityTV.setText(donorModel.getCity());


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


        holder.callIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DonorModel donorModel = donorModelList.get(position);
                makeCall(donorModel.getPhoneNumber());
            }
        });


        holder.viewDetailsLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DonorModel donorModel = donorModelList.get(position);
                Intent intent = new Intent(context, DonorDetailsActivity.class);
                intent.putExtra("name", donorModel.getName());
                intent.putExtra("phone", donorModel.getPhoneNumber());
                intent.putExtra("email", donorModel.getEmail());
                intent.putExtra("bloodGroup", donorModel.getBloodGroup());
                intent.putExtra("city", donorModel.getCity());
                context.startActivity(intent);


            }
        });


        holder.copyPhoneLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DonorModel donorModel = donorModelList.get(position);
                copyPhone(donorModel.getPhoneNumber());

            }
        });



        holder.sendSmsLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DonorModel donorModel = donorModelList.get(position);
                sendSms(donorModel.getPhoneNumber());

            }
        });


        holder.sendEmailLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DonorModel donorModel = donorModelList.get(position);

                if (!donorModel.getEmail().equals("Not Found")){

                    sendEmail(donorModel.getEmail());

                }else {

                    Toast.makeText(context, "No Email Found", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {

        return donorModelList.size();
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


    private void copyPhone(String phone) {


        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipe = ClipData.newPlainText("phone", phone);

        if (clipboard != null) {
            clipboard.setPrimaryClip(clipe);
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
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
