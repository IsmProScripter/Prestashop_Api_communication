import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Parser extends AppCompatActivity {

    ListView lv;
    String jsonResult;
    String url = "http://MyWebSite.com/api/products&output_format=JSON&display=full";
    String prestashop_key = "RM6FQ7HBHV654CC82SEFUI18ZHX5FAPU";


    ArrayList<abs_Products> ProductsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_products);
        lv = (ListView) findViewById(R.id.listProducts);
        lv.setOnItemLongClickListener(onlonglistener);
        lv.setOnItemClickListener(listenerList);
        cargarDatos();
        Toast.makeText(getApplicationContext(), "Click for details",
                Toast.LENGTH_SHORT).show();
    }



    public void ProductsLoader(){
        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("products");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("name");
                Integer id_product = jsonChildNode.optInt("id");
                Integer id_image = jsonChildNode.optInt("id_default_image");
                String description_short = jsonChildNode.optString("description_short");
                String description = jsonChildNode.optString("description");
                Double price = jsonChildNode.optDouble("price");
                String id_supplier = jsonChildNode.optString("id_supplier");
                String reference = jsonChildNode.optString("reference");
                String activo= ""+jsonChildNode.optString("active");
                if(activo.equalsIgnoreCase("1"))
                    activo="Sí";
                else
                    activo="No";
                String available = jsonChildNode.optString("available_now");
                ProductsList.add(new abs_Products(id_product, name, id_image, description_short, description, price,
                    id_supplier,reference,activo,available));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();
        }
        lv.setAdapter(new row_list_products(this, ProductsList));
    }

// build hash set for list view
    public void GetCustomers() {
        List<Map<String, String>> clientList = new ArrayList<Map<String, String>>();
        try {
            //lbl1.setText(jsonResult);
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("customers");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String firstname = jsonChildNode.optString("firstname");
                String lastname = jsonChildNode.optString("lastname");
                String outPut = firstname + " " + lastname;
                clientList.add(createClient("clientes", outPut));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, clientList,
                android.R.layout.simple_list_item_1,
                new String[] { "clientes" }, new int[] { android.R.id.text1 });
        lv.setAdapter(simpleAdapter);
    }

    
}
