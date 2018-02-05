package andbook.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.
        widget.Button;
import android.view.View;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import static java.sql.DriverManager.println;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
   // private MyDBHelper mDBHelper;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // mDBHelper = new MyDBHelper(this);
        mButton = (Button) findViewById(R.id.mining);

        SQLiteDatabase db;
        //ContentValues values;
        //String[] projection = {"_id", "hash", "numberofzeros"};
        Cursor cur;
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MiningActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        String databaseName = "Coin.db";
        createDatabase(databaseName);
        Toast.makeText(this, "test db", Toast.LENGTH_SHORT).show();

        String tableName = "Block";
        createTable(tableName);
        int count = insertRecord(tableName);
        println(count + "records inserted.");


        //createTable("Transaction");
        // createTable("info");

    }

    private void createDatabase(String name){
        println("creating database [" + name + "]");
        //Toast.makeText(this, "creating", Toast.LENGTH_SHORT).show();

        boolean databaseCreated;

        try{
            db = openOrCreateDatabase(name, MODE_PRIVATE, null);
            databaseCreated = true;
            Toast.makeText(this, " real creating", Toast.LENGTH_SHORT).show();

        }catch(Exception ex){
            ex.printStackTrace();
            println("database is not created.");
        }
    }

    private void createTable(String name){
        boolean tableCreated;
        Toast.makeText(this, "table creating", Toast.LENGTH_SHORT).show();
       // String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS Block (hash BLOB, numberofzeros INTEGER,from TEXT, timestamp INTEGER, nonce INTEGER, blockLength INTEGER, tid INTEGER, prev BLOB)"; //hash를 BLOB로 할것인가 REAL로 할것인가
        String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS Block (hash BLOB, numberofzeros INTEGER)";
        //String sqlCreateTbl2 = "CREATE TABLE IF NOT EXISTS Transaction (from TEXT, to TEXT, value INTEGER, header TEXT, timestamp INTEGER, inputs TEXT, outputs TEXT)";
        //String sqlCreateTbl3 = "CREATE TABLE IF NOT EXISTS Info (coin INTEGER, ip TEXT, mac TEXT)";
        db.execSQL(sqlCreateTbl);
        //db.execSQL(sqlCreateTbl2);
        //db.execSQL(sqlCreateTbl3);


        Toast.makeText(this, "table create", Toast.LENGTH_SHORT).show();

        tableCreated = true;

    }

    private int insertRecord(String name){
        Toast.makeText(this, "insert to table", Toast.LENGTH_SHORT).show();
        int count = 3;
        String sqlInsert = "INSERT INTO Block (hash, numberofzeros) VALUES ('7982970534e089b839957b7e174725ce1878731ed6d700766e59cb16f1c25e27',3)";
        db.execSQL(sqlInsert);

        Toast.makeText(this, "insert perfect", Toast.LENGTH_SHORT).show();
        return count;
    }





}
