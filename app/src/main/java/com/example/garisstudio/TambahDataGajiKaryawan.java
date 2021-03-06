package com.example.garisstudio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TambahDataGajiKaryawan extends AppCompatActivity {
    //variabel untuk datepicker
    private DatePickerDialog datePickerDialog;
    //variabel untuk format tanggal
    private SimpleDateFormat dateFormatter;
    //variabel untuk image view
    private ImageView imgDatePicker;
    //variabel untuk edittext
    private EditText tglgj, nikgj;
    //variabel untuk button
    private Button tmbhgj;
    //variabel untuk string
    String strtglgj, strnikgj;
    //variabl untuk integer
    int success;

    //String untuk alamat server, local host android
    private static String url_insert_gaji = "http://10.0.2.2/GarisStudio/tambahgaji.php";
    //String tag untuk nama pendek dari kelas TambahDataGajiKaryawan
    private static final String TAG = TambahDataGajiKaryawan.class.getSimpleName();
    //String tag untuk sukses
    private static final String TAG_SUCCES = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data_gaji_karyawan);
        //mengeeset judul/title dari activity tambahgajikaryawan
        setTitle("Tambah Data Tanggal Gaji Karyawan");
        //mengganti background pada action bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.biru)));

        //memnaggil format tanggal
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        //set id untuk imageview
        imgDatePicker = findViewById(R.id.dateImgTmbh);
        //set id untuk edittext
        tglgj = findViewById(R.id.EtTanggalGajiTambah);
        nikgj = findViewById(R.id.EtNikGajiTambah);

        //set id untuk button
        tmbhgj = findViewById(R.id.btnSimpanTambahGaji);

        //aksi ketika mengeklik imageview
        imgDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        //aksi ketika mengeklik button
        tmbhgj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpanDataGaji();
            }
        });
    }

    //method untuk menampilkan tanggal
    private void showDateDialog(){
        //memanggil calender
        Calendar newCalender = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,month,dayOfMonth);

                tglgj.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DAY_OF_MONTH));
        //menampilkan tanggal
        datePickerDialog.show();
    }

    public void SimpanDataGaji(){
        //pesan ketika edittext kosong
        if(tglgj.getText().toString().equals("")||nikgj.getText().toString().equals("")){
            Toast.makeText(TambahDataGajiKaryawan.this,"Semua harus diisi data",Toast.LENGTH_SHORT).show();
        }else {
            //membaca data dari edittext
            strtglgj = tglgj.getText().toString();
            strnikgj = nikgj.getText().toString();

            //antrian request menggunakan library volley
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

            //mengirim data ke server
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_insert_gaji, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //membaca pesan dari response
                    Log.d(TAG, "Response: " + response.toString());
                    try {
                        JSONObject jObj = new JSONObject(response);
                        success = jObj.getInt(TAG_SUCCES);

                        //pesan sukses
                        if (success == 1) {
                            Toast.makeText(TambahDataGajiKaryawan.this, "Sukses menyimpan data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        //memunculkan pesan kesalahan
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //membaca pesan eror jika jaringan bermasalah
                    Log.e(TAG, "Error: "+error.getMessage());
                    Toast.makeText(TambahDataGajiKaryawan.this,"Gagal menyimpan data",Toast.LENGTH_SHORT).show();
                }
            }){
                //mengirim data dalam bentuk array map
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<>();
                    //memasukkan data sesuai nama kunci yaitu nama kolom pada tabel database dengan value yang diambil gari edittext
                    params.put("tanggal_gaji",strtglgj);
                    params.put("nik",strnikgj);

                    return params;
                }
            };
            //array dimasukkan ke antrian request
            requestQueue.add(stringRequest);
            //memanggil method callHome
            callHome();
        }
    }

    public void callHome() {
        //intent untuk memanggil activity HomeAdmin
        Intent intent = new Intent(TambahDataGajiKaryawan.this, HomeAdmin.class);
        //berpindah dari TambahDataGajiKaryawan ke HomeAdmin
        startActivity(intent);
        finish();
    }
}