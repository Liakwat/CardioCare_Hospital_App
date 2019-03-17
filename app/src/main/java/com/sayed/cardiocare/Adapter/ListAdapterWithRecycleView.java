package com.sayed.cardiocare.Adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sayed.cardiocare.Models.Doctors;
import com.sayed.cardiocare.R;

import java.util.List;

/**
 * Created by anildeshpande on 8/19/17.
 */

public class ListAdapterWithRecycleView extends RecyclerView.Adapter<ListAdapterWithRecycleView.PersonViewHolder> {

    private static final String TAG = ListAdapterWithRecycleView.class.getSimpleName();

    private List<Doctors> postList;
    private Context context;

    public ListAdapterWithRecycleView(Context context, List<Doctors> postList){
        this.postList = postList;
        this.context=context;
    }


    //LAyout inflater
    @Override
    public PersonViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.post_view_layout,parent,false);
        return new PersonViewHolder(view);
    }


    //DATABINDING
    @Override
    public void onBindViewHolder(final PersonViewHolder holder, int position) {

//        Post post = postList.get(position);

//        holder.textViewTitle.setText(post.getTitle());
//        holder.textViewBody.setText(post.getBody());

        //if u use arrayList
        //holder.fundTextView.setText(fund.get(position).toString());
        //holder.nameTextView.setText(name.get(position));

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class PersonViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewTitle;
        public TextView textViewBody;

        public PersonViewHolder(View view){
            super(view);
            textViewTitle = (TextView)view.findViewById(R.id.titleTv);
            textViewBody=(TextView)view.findViewById(R.id.bodyTv);

        }
    }
}
