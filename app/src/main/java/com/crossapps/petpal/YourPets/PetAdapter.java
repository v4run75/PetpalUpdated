package com.crossapps.petpal.YourPets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.crossapps.petpal.GlideApp;
import com.crossapps.petpal.R;
import com.crossapps.petpal.Server.Response.PetResponseData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class PetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<PetResponseData> modelFeedArrayList = new ArrayList<>();


    public PetAdapter(Context context, ArrayList<PetResponseData> modelFeedArrayList) {

        this.context = context;
        this.modelFeedArrayList = modelFeedArrayList;


    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pets, parent, false);
        return new CategoryHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        final PetResponseData data = modelFeedArrayList.get(i);
        CategoryHolder holder = (CategoryHolder) viewHolder;

//        GlideApp.with(context).load(data.getImage()).transition(DrawableTransitionOptions.withCrossFade()).into(holder.cover);

        GlideApp.with(context).load(data.getImage()).into(holder.cover);
        holder.title.setText(data.getName());

        holder.categoriesContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*

                Bundle bundle=new Bundle();
//                bundle.putSerializable("subcategories",data.getSubCategory());
                bundle.putString("title",data.getName());
                bundle.putString("subcat_id",data.getId());
                Intent intent=new Intent(context, ViewProductList.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
*/

            }
        });

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return modelFeedArrayList.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {

        LinearLayout categoriesContainer;
        ImageView cover;
        TextView title;


        CategoryHolder(View itemView) {
            super(itemView);
            categoriesContainer = itemView.findViewById(R.id.categoriesContainer);
            cover = itemView.findViewById(R.id.cover);
            title = itemView.findViewById(R.id.title);
        }
    }

}
