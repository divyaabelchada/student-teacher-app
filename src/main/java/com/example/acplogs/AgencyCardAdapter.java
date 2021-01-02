package com.example.acplogs;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class AgencyCardAdapter extends FirestoreRecyclerAdapter<AgencyModel,AgencyCardAdapter.agencyCardHolder> {

    private itemOnClickListener agencyListener;

    public AgencyCardAdapter(@NonNull FirestoreRecyclerOptions<AgencyModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AgencyCardAdapter.agencyCardHolder holder, int position, @NonNull AgencyModel model) {

        holder.agencyName.setText(model.getAgency_name());
        holder.agencyLocation.setText(model.getAgencyLocation());
        Picasso.get().load(model.agencyProfile).into(holder.imageViewOne, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

                Log.i("Info",e.getLocalizedMessage());
            }
        });

    }

    @NonNull
    @Override
    public AgencyCardAdapter.agencyCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.agency_cards,
                parent,false);
        return new AgencyCardAdapter.agencyCardHolder(v);
    }

    class agencyCardHolder extends RecyclerView.ViewHolder{

        TextView agencyName,agencyLocation;
        ImageView imageViewOne;

        public agencyCardHolder(@NonNull View itemView) {
            super(itemView);

            agencyName = itemView.findViewById(R.id.agency_name);
            agencyLocation = itemView.findViewById(R.id.agency_location);
            imageViewOne = itemView.findViewById(R.id.imageViewOne);


            //setting onClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!= RecyclerView.NO_POSITION && agencyListener !=null){
                        agencyListener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }

                }
            });

        }
    }
    public interface itemOnClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(itemOnClickListener listener){
        this.agencyListener = listener;
    }
}
