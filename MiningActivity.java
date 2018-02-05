package andbook.example.myapplication;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import java.util.TimerTask;

public class MiningActivity extends AppCompatActivity {
    final boolean DEBUG = Boolean.getBoolean("debug");
    String searchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mining);

        Button start = (Button)findViewById(R.id.start);


        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //여기서 화면 돌아가게
                //public abstract Blockchain getBlockChain();
                //원래는 blockchain에서 block정보 가져와야하는것

                //SQLiteDatabase db = openOrCreateDatabase("coin.db",MODE_PRIVATE,null);
                //db.openOrCreateDatabase("coin.db",null);

                byte[] hash = null;
                int   numberofzeros = 0;

                searchName = "Block";
                searchTable(searchName); //여기서 db에  insert한 값 불러와야 하는데

                int nonce = mineHash(hash, numberofzeros);//여기에 hash값이 들어가야하는데

                //Toast.makeText(MiningActivity.this, "hash="+ hash + "numberofzeros ="+numberofzeros, Toast.LENGTH_SHORT).show();

                Toast.makeText(MiningActivity.this, "nonce="+ nonce, Toast.LENGTH_SHORT).show();
                } //Onclick하면 실행하는 곳


        });
     }
    public int mineHash(byte[] SHA256, int numberofZerosInPrefix){
        final int nonce = ProofofWork.solve(SHA256,numberofZerosInPrefix);
        if(DEBUG){
            String status = "CANCELLED";
            if(nonce >=0) {
                status = "SOLVED";
                System.err.println( status + ".nonce=" + nonce);
            }
        }
        return nonce;
    }
    private void searchTable(String searchName){
       // byte[] hash =null;
        //int   numberofzeros = 0;
        //String[] columns = {"hash", "numberofzeros"};

        //cursor = db.query(searchName, columns, "name = hash", null, null,null, null);
        //String sqlQueryTbl = "SELECT * FROM " + Block;

        SQLiteDatabase db = openOrCreateDatabase("coin.db",MODE_PRIVATE,null);
        Cursor cursor =db.rawQuery("SELECT * FROM Block",null);

        while(cursor.moveToNext()){
            byte[] hash= cursor.getBlob(0);
            int numberofzeros = cursor.getInt(1);
        }

    }



}



