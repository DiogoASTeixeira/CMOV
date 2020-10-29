package com.feup.acme_cafe.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.security.KeyPairGeneratorSpec;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.feup.acme_cafe.R;
import com.feup.acme_cafe.data.model.Product;
import com.feup.acme_cafe.data.model.User;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.security.auth.x500.X500Principal;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "";
    ProductAdapter adapter;
    private RequestQueue queue;
    User user;
    List<Product> products;
    private String ids[];
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = (User) getIntent().getSerializableExtra("user");
        products = user.getProducts();
        queue = Volley.newRequestQueue(this);
        displayProducts();
        genKeyPair();
    }

    private void displayProducts() {
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayShowHomeEnabled(true);
        }

        adapter = new ProductAdapter(this, R.layout.row, user.getProducts());

        ListView productsList = findViewById(R.id.listview);
        productsList.setAdapter(adapter);

        /*List.setOnItemClickListener((parent, view, position, id) -> {
            openDetails(ids[position]);
        });*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add) {
            Intent i = new Intent(this, NewTransaction.class);
            i.putExtra("user", user);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return(true);
        }
        else if (item.getItemId() == R.id.profile) {
            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra("user", user);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return (true);
        }
        else if (item.getItemId() == R.id.logout) {

            Intent i = new Intent(this, LoginActivity.class);
            try {
                User user = Util.loadUser(getApplicationContext());
                if (user != null)
                    Util.deletefile(getApplicationContext());
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return (true);
        }

        return(super.onOptionsItemSelected(item));
    }

    private void openDetails(String id) {
        Intent i = new Intent(getApplicationContext(), DetailsTransaction.class);
        i.putExtra("TransactionId", id);
        startActivity(i);
    }

    public void genKeyPair() {
        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(Constants.keyname, null);
            if (entry == null) {
                Calendar start = new GregorianCalendar();
                Calendar end = new GregorianCalendar();
                end.add(Calendar.YEAR, 20);
                KeyPairGenerator kgen = KeyPairGenerator.getInstance(Constants.KEY_ALGO, Constants.ANDROID_KEYSTORE);
                AlgorithmParameterSpec spec = new KeyPairGeneratorSpec.Builder(this)
                        .setKeySize(Constants.KEY_SIZE)
                        .setAlias(Constants.keyname)
                        .setSubject(new X500Principal("CN=" + Constants.keyname))
                        .setSerialNumber(BigInteger.valueOf(12121212))
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                kgen.initialize(spec);
                KeyPair kp = kgen.generateKeyPair();
            }
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }

    public PubKey getPubKey() {
        PubKey pkey = new PubKey();
        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(Constants.keyname, null);
            PublicKey pub = ((KeyStore.PrivateKeyEntry)entry).getCertificate().getPublicKey();
            pkey.modulus = ((RSAPublicKey)pub).getModulus().toByteArray();
            pkey.exponent = ((RSAPublicKey)pub).getPublicExponent().toByteArray();
        }
        catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
        return pkey;
    }

    byte[] getPrivExp() {
        byte[] exp = null;

        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(Constants.keyname, null);
            PrivateKey priv = ((KeyStore.PrivateKeyEntry)entry).getPrivateKey();
            exp = ((RSAPrivateKey)priv).getPrivateExponent().toByteArray();
        }
        catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
        if (exp == null)
            exp = new byte[0];
        return exp;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    class ProductAdapter extends ArrayAdapter<Product> {
        private int layoutResource;
        private Context mContext;

        ProductAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
            super(context, resource, objects);
            layoutResource = resource;
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View line = convertView;

            if (line == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(mContext);
                line = vi.inflate(layoutResource, null);
            }

            Product p = getItem(position);

            if (p != null) {
                TextView name = line.findViewById(R.id.prod_name);
                TextView price = line.findViewById(R.id.prod_price);
                ImageView icon = line.findViewById(R.id.icon);

                String url = p.getUrl();
                String parsedUrl = url.substring(2);
                String fullUrl = "../../.." + parsedUrl;

                Uri uri = Uri.parse(fullUrl);

                if (price != null) {
                    price.setText(p.getPrice().toString() + "€");
                }

                if (name != null) {
                    name.setText(p.getName());
                }

                if(url != null) {
                    icon.setImageURI(uri);
                }
            }

            return line;
        }

    }

    class PubKey {
        byte[] modulus;
        byte[] exponent;
    }

    private void setAndShowAlertDialog(String title, String message){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setTitle(title);
        alertDialog=dialog.create();
        alertDialog.show();
    }
}