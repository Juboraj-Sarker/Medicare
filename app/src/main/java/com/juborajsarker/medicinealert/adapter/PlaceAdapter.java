package com.juborajsarker.medicinealert.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juborajsarker.medicinealert.R;
import com.juborajsarker.medicinealert.activity.DetailsPlaceActivity;
import com.juborajsarker.medicinealert.model.place.Result;

import java.util.ArrayList;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.CustomViewHolder> {

    private List<Result> resultList = new ArrayList<>();
    private Context context;
    double currentLat, currentLng;
    int spinnerPosition;


    public PlaceAdapter(List<Result> placeList, Context context, double currentLat, double currentLng, int spinnerPosition) {

        this.resultList = placeList;
        this.context = context;
        this.currentLat = currentLat;
        this.currentLng = currentLng;
        this.spinnerPosition = spinnerPosition;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_place, parent, false);

        return new CustomViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        Result result = resultList.get(position);
        holder.name.setText(result.getName());
        holder.address.setText(result.getVicinity());
        holder.rating.setText(String.valueOf(result.getRating()));

        switch (spinnerPosition){

            case 1:{

                holder.placeIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pharmecy));
                break;

            }case 2:{

                holder.placeIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hospital));
                break;

            }
        }



        holder.fullItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //https://maps.googleapis.com/maps/api/place/photo?photoreference=PHOTO_REFERENCE&sensor=false&maxheight=MAX_HEIGHT&maxwidth=MAX_WIDTH&key=YOUR_API_KEY

                Intent intent = new Intent(context, DetailsPlaceActivity.class);
                intent.putExtra("nameValue", resultList.get(position).getName());
                intent.putExtra("addressValue", resultList.get(position).getVicinity());
                intent.putExtra("ratingValue", resultList.get(position).getRating());
                intent.putExtra("lat", resultList.get(position).getGeometry().getLocation().getLat());
                intent.putExtra("lng", resultList.get(position).getGeometry().getLocation().getLng());


                intent.putExtra("currentLat", currentLat);
                intent.putExtra("currentLng", currentLng);



                try {

                    intent.putExtra("available", resultList.get(position).getOpeningHours().getOpenNow());
                    intent.putExtra("error", false);

                }catch (Exception e){

                    intent.putExtra("available", false);
                    intent.putExtra("error", true);
                }
                intent.putExtra("placeId", resultList.get(position).getPlaceId());
                context.startActivity(intent);

            }
        });

    }



    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView name, address, rating;
        public LinearLayout fullItem;
        ImageView placeIV;

        public CustomViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.nameTV);
            address = (TextView) view.findViewById(R.id.addressTV);
            rating = (TextView) view.findViewById(R.id.ratingTV);
            fullItem = (LinearLayout) view.findViewById(R.id.fullItem);
            placeIV = (ImageView) view.findViewById(R.id.placeImageView);

        }
    }
}
