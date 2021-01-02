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


public class socialworkLogCardAdapter extends FirestoreRecyclerAdapter<StudentModel, socialworkLogCardAdapter.certificateCardHolder> {
    private socialworkLogCardAdapter.itemOnClickListener listener;

    public socialworkLogCardAdapter(@NonNull FirestoreRecyclerOptions<StudentModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull socialworkLogCardAdapter.certificateCardHolder holder, int position, @NonNull StudentModel model) {

        holder.agencyName.setText(model.getAgency_name());
        holder.workHours.setText("Hours : " + model.getWork_hours());
        holder.checkIn.setText("From : " +model.getCheckIn());
        holder.checkOut.setText("to : " +model.getCheckOut());
        holder.work_desc.setText("Caption : " +model.getWork_desc());
        holder.date.setText("Date : " +model.getDate());
        holder.added_on.setText("Updated on : " +model.getAdded_on());
        Picasso.get().load(model.getCertificate())
                .centerCrop()
                .resize(400,300)
                .into(holder.certificate, new com.squareup.picasso.Callback(){

                    @Override
                    public void onSuccess() {
                        Log.i("Info","Image loaded!! yayayyyy-------------");
                    }

                    @Override
                    public void onError(Exception e) {

                        Log.i("Info","errooooorrrr "+e.getLocalizedMessage());
                    }
                });


    }

    @NonNull
    @Override
    public socialworkLogCardAdapter.certificateCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.social_work_certificate,
                parent,false);
        return new socialworkLogCardAdapter.certificateCardHolder(v);
    }

    class certificateCardHolder extends RecyclerView.ViewHolder{

        TextView agencyName, workHours, work_desc,date, added_on, checkIn, checkOut;
        ImageView certificate;

        public certificateCardHolder(@NonNull View itemView) {
            super(itemView);

            agencyName = itemView.findViewById(R.id.agency_name);
            workHours = itemView.findViewById(R.id.work_hours);
            work_desc=itemView.findViewById(R.id.work_desc);
            date=itemView.findViewById(R.id.date);
            added_on=itemView.findViewById(R.id.added_on);
            certificate = itemView.findViewById(R.id.certificate);
            checkIn = itemView.findViewById(R.id.checkInTime);
            checkOut = itemView.findViewById(R.id.checkOutTime);

            certificate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!= RecyclerView.NO_POSITION && listener!=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }

                }
            });

        }
    }

    public interface itemOnClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(socialworkLogCardAdapter.itemOnClickListener listener){
        this.listener = listener;
    }

}
