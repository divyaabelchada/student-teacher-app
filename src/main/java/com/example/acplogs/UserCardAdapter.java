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

import de.hdodenhof.circleimageview.CircleImageView;

public class UserCardAdapter extends FirestoreRecyclerAdapter<StudentModel, UserCardAdapter.studentCardHolder> {

    private UserCardAdapter.itemOnClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UserCardAdapter(@NonNull FirestoreRecyclerOptions<StudentModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull studentCardHolder holder, int position, @NonNull StudentModel model) {

        holder.studentName.setText(model.getName());
        holder.studentYear.setText(model.getYear());
        holder.studentEmail.setText(model.getEmail());
        holder.studentContact.setText(model.getContact());

        if(model.getProfile()==""){
            holder.studentProfile.setImageResource(R.drawable.ic_profilepic);
        }
        else{
            Picasso.get().load(model.getProfile()).centerCrop().resize(70,70)
                    .into(holder.studentProfile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                            Log.i("Info","error !!! :" +e.getLocalizedMessage());
                        }
                    });
        }



    }

    @NonNull
    @Override
    public studentCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linear_cards,
                parent,false);
        return new studentCardHolder(v);
    }

    class studentCardHolder extends RecyclerView.ViewHolder{

        TextView studentName, studentYear, studentEmail,studentContact;
        CircleImageView studentProfile;

        public studentCardHolder(@NonNull View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.text_one);
            studentYear = itemView.findViewById(R.id.text_two);
            studentEmail = itemView.findViewById(R.id.text_three);
            studentContact = itemView.findViewById(R.id.text_four);
            studentProfile = itemView.findViewById(R.id.profile_image);

            //setting onClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
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
    public void setOnItemClickListener(itemOnClickListener listener){
        this.listener = listener;
    }
}
