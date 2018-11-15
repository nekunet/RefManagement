package jp.ac.shizuoka.inf.cs.cs14012.ref_management;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cs14055 on 2016/02/02.
 */
public class FoodSearch extends Activity {
    FoodDataArrayAdapter searchFoodArrayAdapter = null;
    ListView searchListView;
    ArrayList<FoodData> resultList;
    ArrayList<FoodData> foodBaseData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodsearch);

        Intent intent = getIntent();
        foodBaseData= new ArrayList<FoodData>((ArrayList<FoodData>)intent.getSerializableExtra("food_data_base"));
        resultList = new ArrayList<FoodData>();

        searchFoodArrayAdapter = new FoodDataArrayAdapter(this,
                0,
                resultList,
                R.layout.content_listview2,
                R.id.itemName,
                R.id.itemNumber,
                R.id.itemDate,
                R.id.itemCheckBox);
        searchListView = (ListView) findViewById(R.id.searchListView);
        searchListView.setAdapter(searchFoodArrayAdapter);


        final EditText searchText2= (EditText)findViewById(R.id.searchText);

        searchText2.setOnKeyListener(new View.OnKeyListener() {

            //コールバックとしてonKey()メソッドを定義
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    String input2 = searchText2.getText().toString();

                    resultList.clear();
                    refineFoodList(input2);

                    return true;
                }

                return false;
            }
        });



        EditText searchText= (EditText)findViewById(R.id.searchText);
        searchText.selectAll();
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                resultList.clear();
                refineFoodList(input);
            }
        });


    }
    public void onBackButton(View v){
        finish();
    }
    public void  onSearchButton(View v){
        Intent intent = new Intent(this, Search.class); // 画面指定
        String sendData = getSerchKeyword();
        intent.putExtra("search_data", sendData);
        startActivity(intent);                     //  画面を開く
    }
    private String getSerchKeyword(){
        String result = "";
        for(int i = 0;i < resultList.size();i++){
            if(resultList.get(i).getSearchFlag() == true){
                result = result + resultList.get(i).getName() + " ";
            }
        }
        return result;
    }

    private void refineFoodList(String search){
        for(int i = 0;i < foodBaseData.size();i++){
            int result = BMMatching.bmMatching(foodBaseData.get(i).getName(), search, 0);
            if(result >= 0){
                Log.d("refineFood", "Matching" + result);
                resultList.add(foodBaseData.get(i));
            }
        }
    }
}
