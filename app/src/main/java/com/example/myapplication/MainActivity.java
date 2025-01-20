package com.example.myapplication;

import static com.example.myapplication.Constants.KEY_AGE;
import static com.example.myapplication.Constants.KEY_HEIGHT;
import static com.example.myapplication.Constants.KEY_ID;
import static com.example.myapplication.Constants.KEY_NAME;
import static com.example.myapplication.Constants.KEY_WEIGHT;
import static com.example.myapplication.Constants.TABLE_STUDENTS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.github.javafaker.Faker;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper(this);

        // Set up Spinner for sorting
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSort.setAdapter(adapter);

        binding.spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = (String) parent.getItemAtPosition(position);
                String sortOrder = null;

                switch (selectedOption) {
                    case "По имени":
                        sortOrder = KEY_NAME;
                        break;
                    case "По возрасту":
                        sortOrder = KEY_AGE;
                        break;
                    case "По весу":
                        sortOrder = KEY_WEIGHT;
                        break;
                    case "По росту":
                        sortOrder = KEY_HEIGHT;
                        break;
                }

                // Fetch and display sorted students
                database = dbHelper.getWritableDatabase();
                Cursor cursor = database.query(TABLE_STUDENTS, null, null, null, null, null, sortOrder);
                showStudents(cursor);
                cursor.close();
                dbHelper.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        binding.btRead.setOnClickListener(v -> {
            database = dbHelper.getWritableDatabase();
            Cursor cursor = database.query(TABLE_STUDENTS, null, null, null, null, null, null);
            showStudents(cursor);
            cursor.close();
            dbHelper.close();
        });

        binding.btClear.setOnClickListener(v -> {
            database = dbHelper.getWritableDatabase();
            database.delete(TABLE_STUDENTS, null, null);
            Toast.makeText(this, "Таблица удалена", Toast.LENGTH_SHORT).show();
            binding.tvText.setText("");
            dbHelper.close();
        });

        binding.btAddOne.setOnClickListener(v -> {
            addSomeStudent();
        });

        binding.btUpdate.setOnClickListener(v -> {
            updateStudent();
        });

        binding.btDeleteOne.setOnClickListener(v -> {
            deleteStudent();
        });
    }

    private String formatResource(String id, String name, String age, String weight, String height) {
        return getResources().getString(R.string.result, id, name, age, weight, height);
    }

    private void showStudents(Cursor cursor) {
        database = dbHelper.getWritableDatabase();
        binding.tvText.setText("");

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(KEY_ID);
            int nameIndex = cursor.getColumnIndex(KEY_NAME);
            int ageIndex = cursor.getColumnIndex(KEY_AGE);
            int weightIndex = cursor.getColumnIndex(KEY_WEIGHT);
            int heightIndex = cursor.getColumnIndex(KEY_HEIGHT);

            do {
                binding.tvText.append(formatResource(String.valueOf(cursor.getInt(idIndex)),
                        cursor.getString(nameIndex), cursor.getString(ageIndex),
                        cursor.getString(weightIndex), cursor.getString(heightIndex)));

            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "Данных нет", Toast.LENGTH_SHORT).show();
        }
        dbHelper.close();
    }

    private void updateStudent() {
        database = dbHelper.getWritableDatabase();
        String id = binding.etUpdateId.getText().toString();
        String name = binding.etUpdateName.getText().toString();
        String age = binding.etUpdateAge.getText ().toString();
        String weight = binding.etUpdateWeight.getText().toString();
        String height = binding.etUpdateHeight.getText().toString();

        if (!id.isEmpty() && !name.isEmpty() && !age.isEmpty() && !weight.isEmpty() && !height.isEmpty()) {
            String query = String.format("UPDATE students SET name = '%s', age = '%s', weight = '%s', height = '%s' WHERE _id = %s",
                    name, age, weight, height, id);
            database.execSQL(query);
            Toast.makeText(this, "Данные изменены", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Введите все поля", Toast.LENGTH_SHORT).show();
        }

        binding.etUpdateId.setText("");
        binding.etUpdateName.setText("");
        binding.etUpdateAge.setText("");
        binding.etUpdateWeight.setText("");
        binding.etUpdateHeight.setText("");
        dbHelper.close();
    }

    private void addSomeStudent() {
        database = dbHelper.getWritableDatabase();
        Faker faker = Faker.instance();
        String name = faker.name().firstName();
        String age = String.valueOf(faker.number().numberBetween(20, 80));
        String weight = String.valueOf(faker.number().numberBetween(50, 150));
        String height = String.valueOf(faker.number().numberBetween(150, 200));
        String query = String.format("INSERT INTO students (name, age, weight, height) VALUES ('%s', '%s', '%s', '%s')",
                name, age, weight, height);
        database.execSQL(query);
        dbHelper.close();
    }

    private void deleteStudent() {
        database = dbHelper.getWritableDatabase();
        String number = binding.etDeleteOne.getText().toString();
        if (number.length() != 0) {
            String query = String.format("DELETE FROM students WHERE _id = %s", number);
            database.execSQL(query);
        } else {
            Toast.makeText(this, "Введите id", Toast.LENGTH_SHORT).show();
        }

        binding.etDeleteOne.setText("");
        dbHelper.close();
    }
}