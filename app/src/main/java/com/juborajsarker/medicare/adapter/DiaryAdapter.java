package com.juborajsarker.medicare.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.activity.diary.DetailsDiaryActivity;
import com.juborajsarker.medicare.activity.diary.EditDiaryActivity;
import com.juborajsarker.medicare.database.DiaryDatabase;
import com.juborajsarker.medicare.model.DiaryModel;

import java.util.ArrayList;
import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.MyViewHolder> {

    private Context context;
    private List<DiaryModel> diaryModelList;
    private RecyclerView recyclerView;
    private DiaryAdapter adapter;
    String searchKeyword;
    private TextView messageTV;
    private Activity activity;

    public static final int MY_PERMISSIONS_REQUEST_CALL = 55;

    int counter = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView titleTV, descriptionTV, dateTV, timeTV;
        public CardView fullChildCV;
        public LinearLayout additionalLAYOUT, viewDetailsLAYOUT, editLAYOUT, deleteLAYOUT;





        public MyViewHolder(View view){

            super(view);

            titleTV = (TextView) view.findViewById(R.id.title_TV);
            descriptionTV = (TextView) view.findViewById(R.id.description_TV);
            dateTV = (TextView) view.findViewById(R.id.date_TV);
            timeTV = (TextView) view.findViewById(R.id.time_TV);

            fullChildCV = (CardView) view.findViewById(R.id.fullChildCV);

            additionalLAYOUT = (LinearLayout) view.findViewById(R.id.additional_LAYOUT);
            viewDetailsLAYOUT = (LinearLayout) view.findViewById(R.id.view_details_LAYOUT);
            editLAYOUT = (LinearLayout) view.findViewById(R.id.edit_LAYOUT);
            deleteLAYOUT = (LinearLayout) view.findViewById(R.id.delete_LAYOUT);



        }

    }





    public DiaryAdapter(Context context,  List<DiaryModel> diaryModelList,
                         RecyclerView recyclerView, DiaryAdapter adapter, String searchKeyword, TextView messageTV, Activity activity) {

        this.context = context;
        this.diaryModelList = diaryModelList;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.searchKeyword = searchKeyword;
        this.messageTV = messageTV;
        this.activity = activity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_diary, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final DiaryModel diaryModel = diaryModelList.get(position);

        holder.titleTV.setText(diaryModel.getTitle());
        holder.descriptionTV.setText(diaryModel.getDescription());
        holder.dateTV.setText(diaryModel.getDate());
        holder.timeTV.setText(diaryModel.getTime());






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


        holder.viewDetailsLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DetailsDiaryActivity.class);
                intent.putExtra("id", diaryModel.getId());
                context.startActivity(intent);

            }
        });


        holder.editLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, EditDiaryActivity.class);
                intent.putExtra("id", diaryModel.getId());
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

                                deleteFile(diaryModel, recyclerView, adapter, context);


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







    }




    @Override
    public int getItemCount() {

        return diaryModelList.size();
    }

    private void deleteFile(DiaryModel diaryModel, RecyclerView recyclerView, DiaryAdapter adapter, Context context) {

        DiaryDatabase diaryDatabase = new DiaryDatabase(context);
        diaryDatabase.deleteDiary(diaryModel);

        List<DiaryModel> diaryModelList = new ArrayList<>();
        diaryModelList = diaryDatabase.getAllDiary();

        DiaryAdapter adapters = new DiaryAdapter(context, diaryModelList, recyclerView, adapter, "", messageTV, activity);

        adapters.notifyDataSetChanged();
        recyclerView.setAdapter(adapters);
        adapters.notifyDataSetChanged();

        if (diaryModelList.size() == 0){

            recyclerView.setVisibility(View.GONE);
            messageTV.setVisibility(View.VISIBLE);


        }
    }

}
