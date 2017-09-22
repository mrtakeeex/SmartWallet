package aut.bme.hu.smartwallet;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Gergő on 2016.11.03..
 */
public class Spendings extends AppCompatActivity {
    private EditText spendingNameText;
    private EditText spendingAmountText;
    private Button addSpendingButton;
    private LinearLayout listOfSpendings;
    private TextView sumOfSpendings;
    private CheckBox spending_checkBox;
    private int sumSpendings;

    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spendings);

        spendingNameText = (EditText) findViewById(R.id.spending_name);
        spendingAmountText = (EditText) findViewById(R.id.spending_amount);
        addSpendingButton = (Button) findViewById(R.id.save_spending_button);
        listOfSpendings = (LinearLayout) findViewById(R.id.list_of_spending_rows);
        sumOfSpendings = (TextView) findViewById(R.id.sum_spendings);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Listákból beolvasni a listOfSpendings-be
        for(int i = 0; i < MainActivity.Names.size(); i++){
            if(MainActivity.Amounts.get(i) < 0){
                View rowItem = inflater.inflate(R.layout.row_spendings, null);

                TextView rowSpendingName = (TextView) rowItem.findViewById(R.id.row_spending_name);
                TextView rowSpendingAmount = (TextView) rowItem.findViewById(R.id.row_spending_amount);

                sumSpendings += (MainActivity.Amounts.get(i));

                rowSpendingName.setText(MainActivity.Names.get(i));
                rowSpendingAmount.setText(MainActivity.Amounts.get(i).toString());

                spendingNameText.setText("");
                spendingAmountText.setText("");

                listOfSpendings.addView(rowItem);

                MainActivity.currentBudgetValueField.setText(MainActivity.getCurrentBudgetSum() + "");
                sumOfSpendings.setText(sumSpendings + "");
            }
        }



        addSpendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spendingNameText.getText().toString().isEmpty() || spendingAmountText.getText().toString().isEmpty()) {
                    Snackbar.make(listOfSpendings, R.string.error_message_emptyfield, Snackbar.LENGTH_LONG).show();
                    return;
                }

                View rowItem = inflater.inflate(R.layout.row_spendings, null);

                TextView rowSpendingName = (TextView) rowItem.findViewById(R.id.row_spending_name);
                TextView rowSpendingAmount = (TextView) rowItem.findViewById(R.id.row_spending_amount);

                int spent = (-1) * Integer.parseInt(spendingAmountText.getText().toString());
                sumSpendings += spent;
                MainActivity.sum += spent;

                rowSpendingName.setText(spendingNameText.getText().toString());
                rowSpendingAmount.setText(spent + "");

                // Listákhoz hozzáadni
                MainActivity.Names.add(spendingNameText.getText().toString());
                MainActivity.Amounts.add(spent);

                DateFormat df = new SimpleDateFormat("dd. MM. yyyy, HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                MainActivity.Dates.add(date);

                // Menteni a fájlba
                MainActivity.writeToFile(spendingNameText.getText().toString(), MainActivity.FILE_NAMES, getApplicationContext());
                MainActivity.writeToFile(spent + "", MainActivity.FILE_AMOUNTS, getApplicationContext());
                MainActivity.writeToFile(date, MainActivity.FILE_DATES, getApplicationContext());

                listOfSpendings.addView(rowItem);
                Toast.makeText(Spendings.this, R.string.message_added, Toast.LENGTH_SHORT).show();

                MainActivity.setCurrentBudgetSum(MainActivity.getCurrentBudgetSum() - Integer.parseInt(spendingAmountText.getText().toString()));
                MainActivity.currentBudgetValueField.setText(MainActivity.getCurrentBudgetSum() + "");

                sumOfSpendings.setText(sumSpendings + "");
                spendingNameText.setText("");
                spendingAmountText.setText("");
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spendings, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_all) {
            MainActivity.setCurrentBudgetSum(MainActivity.getCurrentBudgetSum() - sumSpendings);
            MainActivity.currentBudgetValueField.setText(MainActivity.getCurrentBudgetSum() + "");

            listOfSpendings.removeAllViews();
            sumSpendings = 0;
            sumOfSpendings.setText(sumSpendings + "");

            for(int i = 0; i < MainActivity.Names.size(); i++){
                if(MainActivity.Amounts.get(i) < 0){
                    MainActivity.Names.remove(i);
                    MainActivity.Amounts.remove(i);
                    MainActivity.Dates.remove(i);
                    // Ha remove-olunk, a size is csökken 1-el, így nem kell az i++
                    i--;
                }
            }

            // Törlöm a már meglévő fájlokat, hogy ne legyen keveredés
            File dir1 = getFilesDir();
            File file1 = new File(dir1, MainActivity.FILE_NAMES);
            File dir2 = getFilesDir();
            File file2 = new File(dir2, MainActivity.FILE_AMOUNTS);
            File dir3 = getFilesDir();
            File file3 = new File(dir3, MainActivity.FILE_DATES);
            boolean deleted = (file1.delete() && file2.delete() && file3.delete());

            // Mentem a felülírott listákat
            for (int i = 0; i < MainActivity.Names.size(); i++) {
                MainActivity.writeToFile(MainActivity.Names.get(i), MainActivity.FILE_NAMES, getApplicationContext());
                MainActivity.writeToFile(MainActivity.Amounts.get(i).toString(), MainActivity.FILE_AMOUNTS, getApplicationContext());
                MainActivity.writeToFile(MainActivity.Dates.get(i), MainActivity.FILE_DATES, getApplicationContext());
            }

            Toast.makeText(Spendings.this, R.string.message_deleted, Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
