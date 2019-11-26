package com.example.lab2_mobiledevelopment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import de.hdodenhof.circleimageview.CircleImageView;
import com.squareup.picasso.Picasso;



public class ContactsFragment extends Fragment {

    private View ViewContacts;
    private RecyclerView theContactList;

    private DatabaseReference RefContacts, RefUsers;
    private FirebaseAuth nAuthorize;
    private String currUserID;
    private FirebaseRecyclerAdapter adapter;


    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewContacts = inflater.inflate(R.layout.fragment_contacts, container, false);

        theContactList = ViewContacts.findViewById(R.id.contacts_list);
        theContactList.setLayoutManager(new LinearLayoutManager(getContext()));

        nAuthorize = FirebaseAuth.getInstance();
        currUserID = nAuthorize.getCurrentUser().getUid();

        RefContacts = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currUserID);
        RefUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        return ViewContacts;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Contacts>().setQuery(RefContacts, Contacts.class).build();

        FirebaseRecyclerAdapter<Contacts, ContactsViewHolder> adapter = new FirebaseRecyclerAdapter<Contacts, ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int position, @NonNull Contacts model) {
                String userIDs = getRef(position).getKey();

                RefUsers.child(userIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("image")) {
                            String imageUser = dataSnapshot.child("image").getValue().toString();
                            String nameProfile = dataSnapshot.child("name").getValue().toString();
                            String statusProfile = dataSnapshot.child("status").getValue().toString();

                            holder.nameUser.setText(nameProfile);
                            holder.statusUser.setText(statusProfile);
                            Picasso.get().load(imageUser).placeholder(R.drawable.image_profile).into(holder.imageProfile);
                        } else {
                            String nameProfile = dataSnapshot.child("name").getValue().toString();
                            String statusProfile = dataSnapshot.child("status").getValue().toString();

                            holder.nameUser.setText(nameProfile);
                            holder.statusUser.setText(statusProfile);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int x) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display_users_layout, viewGroup, false);
                ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                return viewHolder;
            }
        };

        theContactList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView nameUser, statusUser;
        CircleImageView imageProfile;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            nameUser = itemView.findViewById(R.id.profile_user_name);
            statusUser = itemView.findViewById(R.id.status_user);
            imageProfile = itemView.findViewById(R.id.profile_user_image);
        }
    }
}


