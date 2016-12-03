package com.trelokopoi.dentist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trelokopoi.dentist.util.AsyncApiCall;
import com.trelokopoi.dentist.util.AsyncApiCallOnTaskCompleted;
import com.trelokopoi.dentist.util.WebApi;
import com.trelokopoi.dentist.util.WebInterface;

import org.json.JSONObject;

public class NewProductActivity extends Activity  implements AsyncApiCallOnTaskCompleted {

    private Integer ADD_PRODUCT = 0;
    private String prodName, prodUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        final EditText new_prod_name = (EditText) findViewById(R.id.name_ed);
        final EditText unit = (EditText) findViewById(R.id.unit_ed);
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_prod_name.getText().toString().trim().equals("") || unit.getText().toString().trim().equals("")) {
                    Toast.makeText(NewProductActivity.this, "Fill the product name & sugar per 100 gr/ml in order to Submit new product", Toast.LENGTH_SHORT).show();
                } else {
                    prodName = new_prod_name.getText().toString();
                    prodUnit = unit.getText().toString();
                    new AsyncApiCall(ADD_PRODUCT, NewProductActivity.this, false).execute(WebApi.addProduct(new_prod_name.getText().toString(), unit.getText().toString()));
                }
            }
        });

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(NewProductActivity.this, AddProductActivity.class);
//                startActivity(i);
//                finish();
                dialog();
            }
        });

    }

    private void dialog() {
        final Dialog thedialog = new Dialog(NewProductActivity.this);
        thedialog.requestWindowFeature(1);
        thedialog.setContentView(R.layout.lock_app_dialog);
        thedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        thedialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        thedialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        thedialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        thedialog.show();
//        TextView text = (TextView) thedialog.findViewById(R.id.txt_dia);
//        Button btn = (Button) thedialog.findViewById(R.id.btn_yes);
    }

    @Override
    public void onTaskCompleted(int thread, String result) {

        JSONObject jsonResult = WebInterface.validateJSON(NewProductActivity.this, result);

        if (jsonResult != null) {
            if (thread == ADD_PRODUCT) {
                String success = jsonResult.optString("success", "0");
                if (success.equals("1")) {
                    String productId = jsonResult.optString("productId", "0");
                    Intent i = new Intent(NewProductActivity.this, AddProductActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("productName", prodName);
                    extras.putString("productId", productId);
                    extras.putString("unit",prodUnit);
                    i.putExtras(extras);
                    startActivity(i);
                    finish();
                }
            }
        }
    }

    @Override
    public void onTaskCompleted(int thread, Bundle vars, String result) {

    }
}
