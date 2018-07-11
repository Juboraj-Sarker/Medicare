package com.juborajsarker.medicare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.model.DonorModel;

import java.util.List;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.MyViewHolder> {

    private Context context;
    private List<DonorModel> donorModelList;
    private RecyclerView recyclerView;
    private DonorAdapter adapter;

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


    public DonorAdapter (Context context, List<DonorModel> donorModelList, RecyclerView recyclerView, DonorAdapter adapter){

        this.context = context;
        this.donorModelList = donorModelList;
        this.recyclerView = recyclerView;
        this.adapter = adapter;

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

                Toast.makeText(context, "Call", Toast.LENGTH_SHORT).show();
            }
        });


        holder.viewDetailsLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Details", Toast.LENGTH_SHORT).show();

            }
        });


        holder.copyPhoneLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Copy Phone", Toast.LENGTH_SHORT).show();

            }
        });



        holder.sendSmsLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Send SMS", Toast.LENGTH_SHORT).show();

            }
        });


        holder.sendEmailLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Send Email", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public int getItemCount() {

        return donorModelList.size();
    }
}
