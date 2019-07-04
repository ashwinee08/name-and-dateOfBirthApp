package com.example.sqlitedatabasepractice;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteAccessPermException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseAccess extends SQLiteOpenHelper{

    private static String DATABASE_NAME="NAME_AND_DOBs";
    private static int DB_VERSION=3;

    private DAO dao;

    public DataBaseAccess(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(new DBQueries().CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(new DBQueries().DROP_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        db.execSQL(new DBQueries().DROP_TABLE);
        onCreate(db);
    }

    public DAO getDAOObject(){
        if(dao==null)
        dao=new DAO(this.getReadableDatabase(), this.getWritableDatabase());

        return dao;
    }

    public class DAO
    {
            private DBQueries dbQuery=new DBQueries();
            private SQLiteDatabase sqlDbReadableDatabase;
            private SQLiteDatabase sqlDbWritableDatabase;

            DAO(SQLiteDatabase sqlDbReadableDatabase,SQLiteDatabase sqlDbWritableDatabase){
                this.sqlDbReadableDatabase=sqlDbReadableDatabase;
                this.sqlDbWritableDatabase=sqlDbWritableDatabase;
            }
            public void insertData(FriendsData data) throws SQLException{
                sqlDbWritableDatabase.execSQL(dbQuery.getAugmentedQueryForInsertion(data));
            }

            public void deleteData() throws SQLException{
                sqlDbWritableDatabase.execSQL(dbQuery.DELETE_QUERY);
            }

            public void deleteDataWithSpecificData(String data) throws SQLException{
                  sqlDbWritableDatabase.execSQL(dbQuery.getAugmentedDeleteQuery(data));
              }

            public Cursor updateName(FriendsData data) throws SQLException{
                sqlDbWritableDatabase.execSQL(dbQuery.getAugmentedQueryForUpdate(data));
                String dataValue= (data.getName().isEmpty())? data.getDOB(): data.getName();
                return retrieveSpecificData(dataValue);
            }

            public Cursor retrieveData() throws SQLException{
                return sqlDbReadableDatabase.rawQuery(dbQuery.SELECT_QUERY,null);
            }

            public Cursor retrieveSpecificData(String data) throws SQLException{
                return sqlDbReadableDatabase.rawQuery(dbQuery.getAugmentedQueryForRetrieval(data),null);
            }
        }

        private class DBQueries{

            private final String TABLE_NAME="friendsDetails";
            private final String NAME_COLUMN="friendsName";
            private final String DOB_COLUMN="friendsDOB";
            private final String ID_COLUMN="id";
            //Create query:
            final String CREATE_TABLE="create table "+TABLE_NAME+"( "
                    +NAME_COLUMN+" VARCHAR(70),"
                    +DOB_COLUMN+" varchar(10)"
                    +")";

            //Drop query:
            final String DROP_TABLE="drop table IF EXISTS "+TABLE_NAME;

            //Retrieve query:
            final String SELECT_QUERY="select * from "+ TABLE_NAME;

            final String UPDATE_TABLE="update "+TABLE_NAME+ " set ";

            String getAugmentedQueryForRetrieval(String data){

                 if(!data.contains("-"))
                     return SELECT_QUERY+" where "+ NAME_COLUMN +" = '"+data+"'";
                 else
                     return SELECT_QUERY+" where "+ DOB_COLUMN +"= '"+data+"'";
            }

            String getAugmentedQueryForUpdate(FriendsData data){
                if(!data.getDOB().isEmpty())
                    return UPDATE_TABLE+NAME_COLUMN+" = '"+data.getName()+"' where" + DOB_COLUMN +"= '"+data.getDOB()+"'";
                else
                    return UPDATE_TABLE+DOB_COLUMN+" = '"+data.getDOB()+"' where" + NAME_COLUMN +"= '"+data.getName()+"'";
            }

            //delete:
            final String DELETE_QUERY="delete from "+TABLE_NAME;

            String getAugmentedDeleteQuery(String data){
                if(!data.contains("-")){
                    return DELETE_QUERY+" where "+NAME_COLUMN +" = '"+data+"'";
                }else {
                    return DELETE_QUERY+" where "+ DOB_COLUMN +"= '"+data+"'";
                }
            }

            //insert values:
            private final String INSERT_DATA="insert into "+TABLE_NAME + " values";

            String getAugmentedQueryForInsertion(FriendsData friendsData){
                return INSERT_DATA+"('"+friendsData.getName()+"','"+friendsData.getDOB()+"')";
            }
   }
}
