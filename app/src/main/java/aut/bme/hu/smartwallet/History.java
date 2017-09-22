package aut.bme.hu.smartwallet;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Gergő on 2016.11.02..
 */

public class History extends AppCompatActivity {

    private LayoutInflater inflater;
    private LinearLayout myHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        myHistoryList = (LinearLayout) findViewById(R.id.list_of_history_rows);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < MainActivity.Names.size(); i++) {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowItemForHistory = inflater.inflate(R.layout.row_historys, null);
            TextView rowHistoryName = (TextView) rowItemForHistory.findViewById(R.id.row_history_name);
            rowHistoryName.setText(MainActivity.Names.get(i));
            TextView rowHistoryAmount = (TextView) rowItemForHistory.findViewById(R.id.row_history_amount);

            if (MainActivity.Amounts.get(i) < 0) {
                rowHistoryAmount.setTextColor(Color.rgb(199, 40, 43));  // #c7282b ~ RED
            } else {
                rowHistoryAmount.setTextColor(Color.rgb(48, 182, 71));  // #30b647 ~ GREEN
            }

            rowHistoryAmount.setText(MainActivity.Amounts.get(i).toString());
            TextView rowHistoryDate = (TextView) rowItemForHistory.findViewById(R.id.row_history_date);
            rowHistoryDate.setText(MainActivity.Dates.get(i));
            myHistoryList.addView(rowItemForHistory);
        }
    }
}
