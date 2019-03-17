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
 * Created by Created by sayed on 8/19/17.
 */

public class ListAdapterWithRecycleView extends RecyclerView.Adapter<ListAdapterWithRecycleView.PersonViewHolder> {

    private static final String TAG = ListAdapterWithRecycleView.class.getSimpleName();

    private List<Doctors> DoctorsList;
    private Context context;

    public ListAdapterWithRecycleView(Context context, List<Doctors> list){
        this.DoctorsList = list;
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

        Doctors doctors = DoctorsList.get(position);

        holder.tv_dr_name.setText(doctors.getEmployeeFullName());
        holder.tv_dr_id.setText(doctors.getEmployeeId());
        holder.tv_dr_speciality.setText(doctors.getSpecialityFullName());
        holder.tv_dr_bio.setText(doctors.getShortBio());

        //if u use arrayList
        //holder.fundTextView.setText(fund.get(position).toString());
        //holder.nameTextView.setText(name.get(position));

    }

    @Override
    public int getItemCount() {
        return DoctorsList.size();
    }

    class PersonViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_dr_name;
        public TextView tv_dr_speciality;
        public TextView tv_dr_id;
        public TextView tv_dr_bio;

        public PersonViewHolder(View view){
            super(view);
            tv_dr_name = (TextView)view.findViewById(R.id.dr_name);
            tv_dr_speciality=(TextView)view.findViewById(R.id.dr_speciality);
            tv_dr_id=(TextView)view.findViewById(R.id.dr_employeeId);
            tv_dr_bio=(TextView)view.findViewById(R.id.dr_bio);

        }
    }
}
