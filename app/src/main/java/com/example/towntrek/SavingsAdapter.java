package com.example.towntrek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class SavingsAdapter extends BaseAdapter {

    private Context context;
    private List<String> days;

    public SavingsAdapter(Context context, List<String> days) {
        this.context = context;
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item_savings, parent, false);
        }

        TextView dayTextView = convertView.findViewById(R.id.dayTextView);
        EditText amountEditText = convertView.findViewById(R.id.amountEditText);

        String day = days.get(position);
        dayTextView.setText(day);

        // You can add logic to save the amount for each day here

        return convertView;
    }
}
