package aut.bme.hu.smartwallet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LayoutInflater inflater;
    private static int currentBudgetSum;
    public static TextView currentBudgetValueField;
    public static LinearLayout historyList;
    private Button openHistoryButton;
    private Button openIncomesButton;
    private Button openSpendingsButton;
    private Button saveToFile;

    public static List<String> Names;
    public static List<Integer> Amounts;
    public static List<String> Dates;

    public static final String FILE_NAMES = "names.txt";
    public static final String FILE_AMOUNTS = "amounts.txt";
    public static final String FILE_DATES = "dates.txt";

    public static int sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Names = new ArrayList<String>();
        Amounts = new ArrayList<Integer>();
        Dates = new ArrayList<String>();

        try {
            addNamesFromFile(FILE_NAMES, getApplicationContext());
            addAmountsFromFile(FILE_AMOUNTS, getApplicationContext());
            addDatesFromFile(FILE_DATES, getApplicationContext());
        } catch (Exception e) {
            Log.e("Main activity", "Can not read file: " + e.toString());
            e.printStackTrace();
        }

        openHistoryButton = (Button) findViewById(R.id.openHistoryButton);
        openIncomesButton = (Button) findViewById(R.id.openIncomesButton);
        openSpendingsButton = (Button) findViewById(R.id.openSpendingsButton);

        currentBudgetValueField = (TextView) findViewById(R.id.currentBudgetValue);
        historyList = (LinearLayout) findViewById(R.id.list_of_history_rows);

        sum = 0;
        for (int i = 0; i < Amounts.size(); i++) {
            sum += Amounts.get(i);
        }

        setCurrentBudgetSum(sum);
        currentBudgetValueField.setText(getCurrentBudgetSum() + "");

        openHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, History.class);
                startActivity(intent);
            }
        });

        openIncomesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Incomes.class);
                startActivity(intent);
            }
        });

        openSpendingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Spendings.class);
                startActivity(intent);
            }
        });
    }

    public static int getCurrentBudgetSum() {
        return currentBudgetSum;
    }

    public static void setCurrentBudgetSum(int value) {
        currentBudgetSum = value;
    }

    public static void writeToFile(String data, String fileName, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_APPEND));
            outputStreamWriter.append(data);
            outputStreamWriter.append("\n");
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static void addNamesFromFile(String fileName, Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null) {
                    Names.add(receiveString);
                }

                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("Main activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("Main activity", "Can not read file: " + e.toString());
        }
    }

    public static void addDatesFromFile(String fileName, Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                //StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    Dates.add(receiveString);
                }

                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("Main activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("Main activity", "Can not read file: " + e.toString());
        }
    }

    public static void addAmountsFromFile(String fileName, Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                //StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    Amounts.add(Integer.parseInt(receiveString));
                }

                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("Main activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("Main activity", "Can not read file: " + e.toString());
        }
    }
}
