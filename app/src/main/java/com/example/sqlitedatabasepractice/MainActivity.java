package com.example.sqlitedatabasepractice;

import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainThreadCallBackInterface{


    EditText nameEditText, dobEditText;
    ListView resultListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getHold();

        String[] parameters=new String[3];
        parameters[0]="fetch";
        BackgroundThread bgThread = new BackgroundThread(this);
        bgThread.execute(parameters);
    }

    void getHold(){
        nameEditText=findViewById(R.id.name);
        dobEditText=findViewById(R.id.dob);
        resultListView=findViewById(R.id.stage_to_show);
    }

    public void onPressed(View view) {

        String[] parameters=new String[3];
        parameters[0]="insert";
        parameters[1]=nameEditText.getText().toString();
        parameters[2]=dobEditText.getText().toString();
        BackgroundThread bgThread = new BackgroundThread(this);
        bgThread.execute(parameters);
    }

    @Override
    public void onCallback(String[] result,boolean errorAtAnyPoint){
        if(result!=null) {
            resultListView.setVisibility(View.VISIBLE);
            resultListView.setAdapter(new ListViewAdapter(this,result.length,result));
        }else{
            resultListView.setVisibility(View.INVISIBLE);
        }

        if(!errorAtAnyPoint){
            nameEditText.setText("");
            dobEditText.setText("");
            nameEditText.setPressed(true);
            dobEditText.setPressed(false);
        }
    }

    static class BackgroundThread extends AsyncTask<String ,Integer ,String[]>{

        boolean error=false;
        MainThreadCallBackInterface context;

        public BackgroundThread(MainThreadCallBackInterface context) {
            this.context = context;
        }

        @Override
        protected String[] doInBackground(String... strings) {
            List<String> wholeData=new ArrayList<>();
            DataBaseAccess dataBaseAccess = new DataBaseAccess((MainActivity)context);
            DataBaseAccess.DAO dao = dataBaseAccess.getDAOObject();
            switch(strings[0]){
                case "insert":
                    FriendsData friendsData=new FriendsData();
                    friendsData.setName(strings[1]);
                    friendsData.setDOB(strings[2]);
                    //execute code for Insertion:
                    try {
                        dao.insertData(friendsData);
                    }catch(SQLException exception){
                        Log.e("Error : SQLException :",exception.getLocalizedMessage() );
                        error=true;
                    }
                case "fetch":
                    //Execute code for Retrieval:
                    Cursor cursor=null;
                    try {
                        cursor=dao.retrieveData();
                    }catch(SQLException exception){
                        Log.e("Error : SQLException :",exception.getLocalizedMessage() );
                        error=true;
                    }
                    if(cursor!=null)
                        while(cursor.moveToNext()){
                            wholeData.add(cursor.getString(0)+" : "+cursor.getString(1));
                        }
                    cursor.close();
                    break;
            }
            return wholeData.toArray(new String[0]);
        }

        @Override
        protected void onPostExecute(String[] strings) {
            context.onCallback(strings,error);
        }
    }
}
