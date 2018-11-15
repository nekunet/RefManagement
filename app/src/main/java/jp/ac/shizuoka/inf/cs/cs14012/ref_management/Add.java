package jp.ac.shizuoka.inf.cs.cs14012.ref_management;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.Calendar;
public class Add extends ActionBarActivity {

    //下の二つは期限のドラムロール入力で使用する
    private DatePickerDialog.OnDateSetListener varDateSetListener;
    static DatePickerDialog dialog;


    //個数のプラマイで使用
    public int N;
    public String str;

    private FoodData foodData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final EditText editName = (EditText)findViewById(R.id.editName);
        final EditText editNumber = (EditText) findViewById(R.id.editNumber);
        final EditText editDeadline = (EditText) findViewById(R.id.editDeadline);
        final CheckBox notificationCheckBox = (CheckBox)findViewById(R.id.checkBox);

        varDateSetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // setボタンが押されたときの処理を書き込む
                editDeadline.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                foodData.setUseByDate(year,monthOfYear + 1,dayOfMonth);
            }
        };

        Intent intent = getIntent();
        foodData = (FoodData) intent.getSerializableExtra("request_data");

        BootstrapButton addButton = (BootstrapButton)findViewById(R.id.ADDbutton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodData.setName(String.valueOf(editName.getText()));
                foodData.setNumber(N);
                foodData.setNotificationFlag(notificationCheckBox.isChecked());

                Intent intent = new Intent(getApplication(),MainActivity.class);
                intent.putExtra("response_data",foodData);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        int mYear;
        int mMonth;
        int mDay;

        if(foodData.getEditId() == -1) {     //新規作成
            // 初期設定日付の取得
            final Calendar cal = Calendar.getInstance();
            mYear = cal.get(Calendar.YEAR);
            mMonth = cal.get(Calendar.MONTH);
            mDay = cal.get(Calendar.DAY_OF_MONTH);
            N = 0;
        }else{                                  //編集
            mYear = foodData.getYear();
            mMonth = foodData.getMonth();
            mDay = foodData.getDate();
            N = foodData.getNumber();
            editName.setText(foodData.getName());
            editNumber.setText(Integer.toString(foodData.getNumber()));
            editDeadline.setText(foodData.getUseByDateString());
            notificationCheckBox.setChecked(foodData.getNotificationFlag());
        }

        dialog = new DatePickerDialog(this, varDateSetListener, mYear, mMonth, mDay);
        dialog.onCreatePanelView(mYear);
        dialog.onCreatePanelView(mMonth);
        dialog.onCreatePanelView(mDay);



        BootstrapButton dateButton = (BootstrapButton)findViewById(R.id.datebutton);
        dateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                dialog.show();
            }
        });

        BootstrapButton cancelButton = (BootstrapButton)findViewById(R.id.CANCELbutton);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

        BootstrapButton plusButton = (BootstrapButton)findViewById(R.id.PLUSbutton);
        plusButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                str = editNumber.getText().toString();
                if(str == null || str.length() == 0){
                    N = 0;
                }else {
                    N = Integer.parseInt(str);
                }
                N = N + 1;
                editNumber.setText(String.valueOf(N));

            }
        });

        BootstrapButton minusButton = (BootstrapButton)findViewById(R.id.MINUSbutton);
        minusButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                EditText editNumber = (EditText) findViewById(R.id.editNumber);
                str = editNumber.getText().toString();
                if(str == null || str.length() == 0){
                    N = 0;
                }else {
                    N = Integer.parseInt(str);
                }
                if(N > 1){
                    N = N - 1;
                }
                editNumber.setText(String.valueOf(N));

            }
        });


    }



}

