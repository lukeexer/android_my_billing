package luke.exe.mybilling;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public ArrayList<String> str;
    public ArrayAdapter adapter;
    public int clickitem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview = findViewById(R.id.listview);
        str = new ArrayList<String>();

        readFile();

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, str);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickitem = position;
            }
        });

        Button add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText item = findViewById(R.id.item);
                EditText price = findViewById(R.id.price);
                if (!item.getText().toString().equals("") && !item.getText().toString().equals("")) {
                    str.add(item.getText().toString() + ": " + price.getText().toString());
                    adapter.notifyDataSetChanged();

                    writeFile();
                }
            }
        });

        Button del = findViewById(R.id.del);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickitem >= 0) {
                    str.remove(clickitem);
                    adapter.notifyDataSetChanged();

                    writeFile();
                }
                clickitem = -1;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        switch (item_id) {
            case R.id.quit:
                System.exit(0);
                break;
            default:
                return false;
        }
        return true;
    }

    void writeFile() {
        File dir = this.getExternalFilesDir(null);
        File outFile = new File(dir, "billing.txt");
        String content = "this is an outer storage test";
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"));
            for (int i = 0; i < str.size(); i++) {
                writer.write(str.get(i));
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            Log.d("MyBilling", "File write fail!");
        }
    }

    void readFile() {
        File dir = this.getExternalFilesDir(null);
        File inFile = new File(dir, "billing.txt");
        StringBuilder data = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "utf-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                str.add(line);
            }
            reader.close();
        } catch (Exception e) {
            Log.d("MyBilling", "File read fail!");
        }
    }
}