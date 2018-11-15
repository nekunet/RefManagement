package jp.ac.shizuoka.inf.cs.cs14012.ref_management;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    FoodDataArrayAdapter foodDataArrayAdapter = null;
    final static int addRequestCode = 1111;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //アプリを起動して最初に読まれる関数
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView titleView = (TextView) findViewById(R.id.titleView); //タイトルのテキスト
        titleView.setTextColor(Color.BLUE);                             //タイトルの色指定
        titleView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);

        BootstrapButton addButton = (BootstrapButton) findViewById(R.id.button1);

        BootstrapButton searchFoodButton = (BootstrapButton)findViewById(R.id.serachFoodButton);
        searchFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FoodSearch.class); // 画面指定
                ArrayList<FoodData> sendData = new ArrayList<FoodData>();
                for(int i= 0;i < foodDataArrayAdapter.getCount();i++){
                    sendData.add(foodDataArrayAdapter.getItem(i));
                }
                intent.putExtra("food_data_base",sendData);
                startActivity(intent);                     //  画面を開く
            }
        });

        ArrayList<FoodData> objects = new ArrayList<FoodData>();
        foodDataArrayAdapter = new FoodDataArrayAdapter(this,
                0,
                objects,
                R.layout.content_listview2,
                R.id.itemName,
                R.id.itemNumber,
                R.id.itemDate,
                R.id.itemCheckBox);

        TextView emptyTextView = new TextView(this);
        emptyTextView.setText("No items found");
        // 中央寄せにする
        emptyTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        emptyTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        listView = (ListView) findViewById(R.id.listView);
        //listView.setBackgroundColor(Color.argb(20,30,30,30));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView tempList = (ListView) parent;
                FoodData selectedItem = (FoodData) tempList.getItemAtPosition(position);    //クリックした場所のアイテムを取得
                //Toast.makeText(getApplicationContext(), selectedItem.getName(), Toast.LENGTH_LONG).show();

                selectedItem.setEditId(position);                                     // 編集用IDの設定
                Intent intent = new Intent(getApplicationContext(), Add.class);      // 画面指定
                intent.putExtra("request_data", selectedItem);                       // 編集用データの送信
                int requestCode = 1111;
                startActivityForResult(intent, addRequestCode);                      // 画面を開く

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListView tempList = (ListView) parent;
                final FoodData selectedItem = (FoodData) tempList.getItemAtPosition(position);    //クリックした場所のアイテムを取得
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("削除確認");
                builder.setMessage(selectedItem.getName() + "を削除してもよろしいですか?");
                builder.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                foodDataArrayAdapter.remove(selectedItem);
                            }
                        });
                builder.setNegativeButton("Cancel", null);
                builder.show();
                return true;
            }
        });

        listView.setAdapter(foodDataArrayAdapter);
        listView.setEmptyView(emptyTextView);

        loadFoodData();
        insertionFoodSort();
        notificationFood();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveFoodData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_CANCELED) {
            Log.d("AddResult", "RESULT_OK");
            if (data != null) {
                if (data.getSerializableExtra("response_data") != null) {
                    if (requestCode == addRequestCode) {
                        // 追加アクティビティからの返却値を受け取る

                        FoodData recievedData = (FoodData) data.getSerializableExtra("response_data");
                        Log.d("AddResult", "RECIEVED : " + recievedData.getName() + "(" + recievedData.getUseByDateString() + ")");
                        if(recievedData.getEditId() == -1) {
                            foodDataArrayAdapter.add(recievedData);
                        }else{
                            foodDataArrayAdapter.remove(foodDataArrayAdapter.getItem(recievedData.getEditId()));
                            foodDataArrayAdapter.add(recievedData);
                        }
                        insertionFoodSort();
                    }
                }
                else{
                    Log.d("AddResult", "FOOD DATA : NULL");
                }
            } else {
                Log.d("AddResult", "INTENT : NULL");
            }
        }
    }


    public void onButton1(View v) { //Button1を押したとkの処理
        Intent intent = new Intent(this, Add.class);                        // 画面指定
        FoodData requestData = new FoodData();
        intent.putExtra("request_data",requestData);                        //編集用データの送信(空 編集用ID = -1)
        int requestCode = 1111;
        startActivityForResult(intent, addRequestCode);                     //  画面を開く
    }

    public void onButton3(View v) { //Searchbuttonを押したとkの処理
        Intent intent = new Intent(this, Search.class); // 画面指定
        String sendData = getSerchKeyword();
        intent.putExtra("search_data",sendData);
        startActivity(intent);                     //  画面を開く
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    String getSerchKeyword(){
        String result = "";
        for(int i = 0;i < foodDataArrayAdapter.getCount();i++){
            if(foodDataArrayAdapter.getItem(i).getSearchFlag() == true){
                result = result + foodDataArrayAdapter.getItem(i).getName() + " ";
            }
        }
        return result;
    }

    void saveFoodData(){
        try{
            OutputStream out = openFileOutput("foodData.txt",MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));
            for(int i = 0;i < foodDataArrayAdapter.getCount();i++) {
                Log.d("saveFoodData","saving ... no." + Integer.toString(i));
                FoodData f = foodDataArrayAdapter.getItem(i);
                writer.println(f.getName());
                writer.println(f.getNumber());
                writer.println(f.getYear());
                writer.println(f.getMonth());
                writer.println(f.getDate());
                writer.println(f.getNotificationFlag());
                writer.println(f.getSearchFlag());
                writer.println(f.getEditId());
            }
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    void loadFoodData(){
        try {
            // 入力ストリームの生成（文字コード指定）
            InputStream in = openFileInput("foodData.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            // テキストファイルからの読み込み
            String msg;
            int lineCount = 0;
            FoodData f = new FoodData();
            while ( ( msg = br.readLine()) != null ) {
                switch(lineCount){
                    case 0:
                        f.setName(msg);
                        break;
                    case 1:
                        f.setNumber(Integer.parseInt(msg));
                        break;
                    case 2:
                        f.setYear(Integer.parseInt(msg));
                        break;
                    case 3:
                        f.setMonth(Integer.parseInt(msg));
                        break;
                    case 4:
                        f.setDate(Integer.parseInt(msg));
                        break;
                    case 5:
                        f.setNotificationFlag(Boolean.valueOf(msg));
                        break;
                    case 6:
                        f.setSearchFlag(Boolean.valueOf(msg));
                        break;
                    case 7:
                        f.setEditId(Integer.parseInt(msg));
                        break;
                    default:
                        break;
                }
                lineCount++;
                if(lineCount == 8){
                    foodDataArrayAdapter.add(f);
                    f = new FoodData();
                    lineCount = 0;
                }
            }

            // 後始末
            br.close();
            // エラーがあった場合は、スタックトレースを出力
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
    void notificationFood(){
        ArrayList<String>list  = new ArrayList<String>();
        for(int i = 0;i < foodDataArrayAdapter.getCount();i++){
            FoodData f = foodDataArrayAdapter.getItem(i);
            if(f.getNotificationFlag()){
                final Calendar cal = Calendar.getInstance();
                int cYear = cal.get(Calendar.YEAR);
                int cMonth = cal.get(Calendar.MONTH);
                int cDay = cal.get(Calendar.DAY_OF_MONTH);

                int cTime = cYear*365 + (cMonth+1)*30 + cDay;

                int dTime = f.getSerialDate() - cTime;
                if(dTime < 1){
                    list.add(f.getName());
                }
            }
        }
        if(list.size() > 0) {
            CharSequence[] warning_list = list.toArray(new CharSequence[list.size()]);
            AlertDialog.Builder listDlg = new AlertDialog.Builder(this);
            listDlg.setTitle("賞味期限が近づいています");
            listDlg.setItems(
                    warning_list,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // リスト選択時の処理
                            // which は、選択されたアイテムのインデックス
                        }
                    });

            // 表示
            listDlg.create().show();
        }

    }



    //FoodData配列を挿入ソートで整列
    void insertionFoodSort(){

        for(int i = 1;i < foodDataArrayAdapter.getCount();i++){
            int j = i;
            while(j >= 1 && compareFood(j-1,j) > 0){
                swapFood(j,j-1);
                j--;
            }
        }

    }
    int compareFood(int i,int j){
        return foodDataArrayAdapter.getItem(i).getSerialDate() - foodDataArrayAdapter.getItem(j).getSerialDate();
    }
    void swapFood(int i,int j){
        FoodData temp1 = foodDataArrayAdapter.getItem(i);
        FoodData temp2 = foodDataArrayAdapter.getItem(j);
        foodDataArrayAdapter.remove(foodDataArrayAdapter.getItem(j));
        foodDataArrayAdapter.insert(temp1,j);
        foodDataArrayAdapter.remove(foodDataArrayAdapter.getItem(i));
        foodDataArrayAdapter.insert(temp2,i);
    }
}