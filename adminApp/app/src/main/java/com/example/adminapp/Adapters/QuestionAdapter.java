package com.example.adminapp.Adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.Models.QuestionsModel;
import com.example.adminapp.Models.SubCategoryModel;
import com.example.adminapp.QuestionsActivity;
import com.example.adminapp.R;
import com.example.adminapp.databinding.RvSubcategoryDesignBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder>{

    Context context;
    ArrayList<QuestionsModel>list;

    private String catId;
    private String subCatId;

    public QuestionAdapter(Context context, ArrayList<QuestionsModel> list, String catId, String subCatId) {
        this.context = context;
        this.list = list;
        this.catId = catId;
        this.subCatId = subCatId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rv_subcategory_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        QuestionsModel categoryModel = list.get(position);

        holder.binding.subCategoryName.setText(categoryModel.getQuestion());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure, you want to delete this category");

                builder.setPositiveButton("Yes",(dialogInterface, i) -> {
                    FirebaseDatabase.getInstance().getReference().child("categories").child(catId).child("subCategories").child(subCatId)
                            .child("questions").child(categoryModel.getKey())
                            .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "daleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                });

                builder.setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RvSubcategoryDesignBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = RvSubcategoryDesignBinding.bind(itemView);
        }
    }
}