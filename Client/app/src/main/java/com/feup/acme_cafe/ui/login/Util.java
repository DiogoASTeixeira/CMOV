package com.feup.acme_cafe.ui.login;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.feup.acme_cafe.R;
import com.feup.acme_cafe.data.model.Product;
import com.feup.acme_cafe.data.model.User;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.List;

public class Util {
    static String ip_address = "192.168.1.77";

    static class ProductAdapter extends ArrayAdapter<Product> {
        private int layoutResource;
        private Context mContext;
        private List<Product> productList;

        ProductAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
            super(context, resource, objects);
            layoutResource = resource;
            mContext = context;
            productList = objects;
        }

        public void updateContent(List<Product> newList) {
            this.productList = newList;
            notifyDataSetChanged();
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
                TextView title = line.findViewById(R.id.prod_name);
                //TextView price = line.findViewById(R.id.total);

                if (title != null) {
                    title.setText(p.getName());
                }

                /*if (price != null) {
                    price.setText(p.getPrice() + "€");
                }*/

            }

            return line;
        }
    }

    public static String[] parseDate(String date) {
        String[] dateaux;
        dateaux=date.split("Z");
        dateaux=dateaux[0].split("T");

        String[] data,hora;
        String dataf,horaf;
        data=dateaux[0].split("-");
        Log.d("data",data[0]);
        String mes;
        switch (data[1]){
            case "1": mes="January";
                break;
            case "2":mes="February";
                break;
            case "3":mes="March";
                break;
            case "4":mes="April";
                break;
            case "5":mes="May";
                break;
            case "6":mes="June";
                break;
            case "7":mes="July";
                break;
            case "8":mes="August";
                break;
            case "9":mes="September";
                break;
            case "10":mes="October";
                break;
            case "11":mes="November";
                break;
            case "12": mes="December";
                break;
            default: mes="Unknown";
        }
        dataf=data[2]+" " +mes+ " " + data[0];

        Log.d("dateaux",dateaux[1]);
        horaf=dateaux[1].substring(0,8);

        String[] returndate = new String[]{dataf, horaf};
        return returndate;
    }

    public static String byteArrayToHex(byte[] ba) {
        StringBuilder sb = new StringBuilder(ba.length * 2);
        for (byte b : ba)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static void saveUser(User user, Context context) throws IOException {
        FileOutputStream fos = context.openFileOutput("user_data", Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(user);
        os.close();
        fos.close();
    }

    public static User loadUser(Context context) throws IOException, ClassNotFoundException {
        Log.d("load user", "entering");
        FileInputStream fis = null;
        try {
            fis = context.openFileInput("user_data");
        }
        catch(FileNotFoundException e){
            Log.d("not found", "not found file");
            return null;
        }
        ObjectInputStream is = new ObjectInputStream(fis);
        User obtainedUser = (User) is.readObject();
        is.close();
        fis.close();

        return obtainedUser;
    }

    public static Boolean deletefile(Context context) throws IOException, ClassNotFoundException {
        Boolean deleted;
        deleted=context.deleteFile("user_data");
        return deleted;
    }
}