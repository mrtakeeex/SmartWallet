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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Gergő on 2016.11.03..
 */
public class Incomes extends AppCompatActivity {

    private EditText incomeNameText;
    private EditText incomeAmountText;
    private Button addIncomeButton;
    private LinearLayout listOfIncomes;
    private TextView sumOfIncomes;

    private LayoutInflater inflater;
    private int sumIncomes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incomes);

        incomeNameText = (EditText) findViewById(R.id.income_name);
        incomeAmountText = (EditText) findViewById(R.id.income_amount);
        addIncomeButton = (Button) findViewById(R.id.save_income_button);
        listOfIncomes = (LinearLayout) findViewById(R.id.list_of_income_rows);
        sumOfIncomes = (TextView) findViewById(R.id.sum_incomes);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i = 0; i < MainActivity.Names.size(); i++){
            if(MainActivity.Amounts.get(i) >= 0){
                View rowItem = inflater.inflate(R.layout.row_incomes, null);

                TextView rowSpendingName = (TextView) rowItem.findViewById(R.id.row_income_name);
                TextView rowSpendingAmount = (TextView) rowItem.findViewById(R.id.row_income_amount);

                sumIncomes += (MainActivity.Amounts.get(i));

                //MainActivity.setCurrentBudgetSum(MainActivity.Amounts.get(i));

                rowSpendingName.setText(MainActivity.Names.get(i));
                rowSpendingAmount.setText(MainActivity.Amounts.get(i).toString());

                incomeNameText.setText("");
                incomeAmountText.setText("");

                listOfIncomes.addView(rowItem);

                //MainActivity.setCurrentBudgetSum(MainActivity.getCurrentBudgetSum() + MainActivity.Amounts.get(i));
                MainActivity.currentBudgetValueField.setText(MainActivity.getCurrentBudgetSum() + "");
                sumOfIncomes.setText(sumIncomes + "");
            }
        }

        addIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (incomeNameText.getText().toString().isEmpty() || incomeAmountText.getText().toString().isEmpty()) {
                    Snackbar.make(listOfIncomes, R.string.error_message_emptyfield, Snackbar.LENGTH_LONG).show();
                    return;
                }

                View rowItem = inflater.inflate(R.layout.row_incomes, null);
                TextView rowIncomeName = (TextView) rowItem.findViewById(R.id.row_income_name);
                TextView rowIncomeAmount = (TextView) rowItem.findViewById(R.id.row_income_amount);

                sumIncomes+= Integer.parseInt(incomeAmountText.getText().toString());
                MainActivity.sum += Integer.parseInt(incomeAmountText.getText().toString());

                rowIncomeName.setText(incomeNameText.getText().toString());
                rowIncomeAmount.setText(incomeAmountText.getText().toString());

                // Listákhoz hozzáadni
                MainActivity.Names.add(incomeNameText.getText().toString());
                MainActivity.Amounts.add(Integer.parseInt(incomeAmountText.getText().toString()));

                DateFormat df = new SimpleDateFormat("dd. MM. yyyy, HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                MainActivity.Dates.add(date);

                // Menteni a fájlba
                MainActivity.writeToFile(incomeNameText.getText().toString(), MainActivity.FILE_NAMES, getApplicationContext());
                MainActivity.writeToFile(incomeAmountText.getText().toString(), MainActivity.FILE_AMOUNTS, getApplicationContext());
                MainActivity.writeToFile(date, MainActivity.FILE_DATES, getApplicationContext());

                listOfIncomes.addView(rowItem);
                Toast.makeText(Incomes.this, R.string.message_added, Toast.LENGTH_SHORT).show();

                MainActivity.setCurrentBudgetSum(MainActivity.getCurrentBudgetSum() + Integer.parseInt(incomeAmountText.getText().toString()));
                MainActivity.currentBudgetValueField.setText(MainActivity.getCurrentBudgetSum() + "");

                sumOfIncomes.setText(sumIncomes + "");

                incomeNameText.setText("");
                incomeAmountText.setText("");
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_income, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_all) {
            MainActivity.setCurrentBudgetSum(MainActivity.getCurrentBudgetSum() - sumIncomes);
            MainActivity.currentBudgetValueField.setText(MainActivity.getCurrentBudgetSum() + "");
            listOfIncomes.removeAllViews();
            sumIncomes = 0;
            sumOfIncomes.setText(sumIncomes + "");

            for(int i = 0; i < MainActivity.Names.size(); i++){
                if(MainActivity.Amounts.get(i) >= 0){
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

            Toast.makeText(Incomes.this, R.string.message_deleted, Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
