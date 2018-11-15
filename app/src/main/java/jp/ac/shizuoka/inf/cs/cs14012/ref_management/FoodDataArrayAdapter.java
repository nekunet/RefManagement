package jp.ac.shizuoka.inf.cs.cs14012.ref_management;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

/**
 * Created by cs14055 on 2016/01/30.
 */
public class FoodDataArrayAdapter extends ArrayAdapter<FoodData> {
    private Context context;
    private LayoutInflater inflater;
    private int listItemLayoutId;
    private int itemTextId;
    private int itemNumberId;
    private int itemUseByDateId;
    private int itemSearchCheckBoxId;

    public FoodDataArrayAdapter(Context context,
                                int resource,
                                List<FoodData> objects,
                                int listDataLayoutid,
                                int itemTextId,
                                int itemNumberId,
                                int itemUseByDateId,
                                int itemSearchCheckBoxId) {
        super(context, resource, objects);

        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listItemLayoutId = listDataLayoutid;
        this.itemTextId = itemTextId;
        this.itemNumberId = itemNumberId;
        this.itemUseByDateId = itemUseByDateId;
        this.itemSearchCheckBoxId = itemSearchCheckBoxId;

    }
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(listItemLayoutId, null);
        }

        final FoodData item = this.getItem(position);
        //リストのアイテムデータの取得

        TextView textView = (TextView) convertView.findViewById(itemTextId);
        if (textView != null) {
            textView.setText(item.getName());
            //アイテムデータに設定されたテキストを表示
        }
        TextView numberView = (TextView) convertView.findViewById(itemNumberId);
        if (numberView != null) {
            numberView.setText(Integer.toString(item.getNumber()));
        }
        TextView dateView = (TextView) convertView.findViewById(itemUseByDateId);
        if (dateView != null) {
            dateView.setText(item.getUseByDateString());
        }
        CheckBox searchCheckBox = (CheckBox) convertView.findViewById(itemSearchCheckBoxId);
        if (searchCheckBox != null) {
            searchCheckBox.setChecked(item.getSearchFlag());
        }
        searchCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                // クリック時のチェック状態をトーストで表示
                boolean checked = checkBox.isChecked();
                item.setSearchFlag(checked);
                /*
                Toast.makeText(getContext(),
                        "chk1:" + String.valueOf(checked), Toast.LENGTH_SHORT).show();
                        */
            }
        });


        //警告色設定
        final Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        int cTime = cYear*365 + (cMonth+1)*30 + cDay;

        int fTime = item.getSerialDate();

        int dTime = fTime - cTime;

        Log.d("getView","DEAD LINE = " + String.valueOf(dTime));

        int safeColor = Color.parseColor("#2980b9");
        int warningColor = Color.parseColor("#f1c40f");
        int outColor = Color.parseColor("#e74c3c");

        if(dTime >= 0 && dTime < 7){
            dateView.setBackgroundColor(warningColor);
        }else if(dTime >= 7){
            dateView.setBackgroundColor(safeColor);
        }else{
            dateView.setBackgroundColor(outColor);
        }

        return convertView;
    }
}
