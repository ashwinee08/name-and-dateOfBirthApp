package com.example.sqlitedatabasepractice;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter implements View.OnClickListener{

    private int count;
    private Context context;
    private TextView resultRow;
    private String[] data;

    public ListViewAdapter(Context context,int count,String[] data) {
        this.context=context;
        this.count=count;
        this.data=data;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_view_adapter,parent, false);
        resultRow=view.findViewById(R.id.result_row);
        resultRow.setText(data[position]);
        view.findViewById(R.id.edit).setOnClickListener(this);
        view.findViewById(R.id.delete).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.edit:
                break;
            case R.id.delete:
                break;
        }
    }
}
