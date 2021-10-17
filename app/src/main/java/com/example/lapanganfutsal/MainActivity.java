package com.example.lapanganfutsal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lapanganfutsal.model.CabangFutsal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText fieldBranch,fieldCapacity;
    Button btnSave;
    ListView listBranch;
    Spinner selectCategory;
    Character selectedCategory;

    ArrayList<CabangFutsal> branchList = new ArrayList<CabangFutsal>();
    String optionsCategory[] = {"Indoor","Outdoor"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fieldBranch     = findViewById(R.id.fieldBranch);
        fieldCapacity   = findViewById(R.id.fieldCapacity);
        btnSave         = findViewById(R.id.btnSave);
        listBranch      = findViewById(R.id.listBranch);
        selectCategory  = findViewById(R.id.selectCategory);
        selectCategory.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,optionsCategory);
        selectCategory.setAdapter(adapterCategory);

        showData();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateAllColumn())
                {
                    if (!isBranchNameExist())
                    {
                        CabangFutsal newBranch = new CabangFutsal(fieldBranch.getText().toString(),selectedCategory,Integer.parseInt(fieldCapacity.getText().toString()));
                        branchList.add(newBranch);
                        writeDataToStorage();
                        showData();
                    }else{
                        Toast.makeText(getApplicationContext(),"Nama Cabang Sudah Ada",Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Semua Kolom Tidak Boleh Kosong",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    /*
     *
     * Spinner Dropdown Function
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (this.optionsCategory[i].equals("Indoor"))
        {
            this.selectedCategory = 'I';
        }
        else{
            this.selectedCategory = 'O';
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private Boolean validateAllColumn()
    {
        if (fieldBranch.getText().toString().equals("") || String.valueOf(fieldCapacity.getText()).equals(""))
        {
            return false;
        }

        return true;
    }

    private void showData()
    {
        readDataFromStorage();
        String listBranchField = "";
        for(CabangFutsal c:branchList){
            listBranchField += c.getNamaCabang() + ",";
        }

        if(branchList.size() > 0)
        {
            listBranchField = listBranchField.substring(0,listBranchField.length() -1);
        }

        String[] arrayListBranchField = listBranchField.split(",");
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(this,R.layout.list_branches_item,R.id.txtBranchName,arrayListBranchField);
        listBranch.setAdapter(branchAdapter);
    }

    private void writeDataToStorage()
    {
        try {
            FileOutputStream stream        = openFileOutput("datas.txt", Context.MODE_PRIVATE);
            ObjectOutputStream writeStream = new ObjectOutputStream(stream);
            writeStream.writeObject(branchList);
            writeStream.flush();
            writeStream.close();
            stream.close();
        }catch (Exception e)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Input Gagal,Silahkan Coba Lagi");
            alert.setMessage(e.getMessage());
            alert.show();
        }
    }

    private void readDataFromStorage()
    {
        try
        {
            FileInputStream stream = this.openFileInput("datas.txt");
            ObjectInputStream readStream = new ObjectInputStream(stream);
            branchList = (ArrayList<CabangFutsal>) readStream.readObject();
            readStream.close();
        }catch (Exception e){
            if (branchList.size() != 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Kesalahan Saat Membaca Data dari Storage");
                alert.setMessage("Pesan kesalahan: " + e.getMessage());
                alert.show();
            }
        }
    }

    public void handleClickBranchName(View view)
    {
        String branchName = ((TextView)view).getText().toString();

        CabangFutsal cabangFutsal = null;

        for(CabangFutsal c:branchList){
            if (c.getNamaCabang().equals(branchName)){
                cabangFutsal = c;
                break;
            }
        }

        String kategori = cabangFutsal.getKategoriLapangan().equals('I') ? "Indoor" : "Outdoor";

        String detail = String.format("Nama Cabang : %s\n"
        + "Kategori : %s\n" + "Kapasitas / Sisa Tempat : %d"
                ,cabangFutsal.getNamaCabang(),
                kategori,
                cabangFutsal.getJumlahLapangan());

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Rincian Data Cabang Lapangan");
        alert.setMessage(detail);
        alert.show();
    }


    private boolean isBranchNameExist()
    {
        for (CabangFutsal c:branchList)
        {
            if(c.getNamaCabang().toLowerCase(Locale.ROOT).equals(fieldBranch.getText().toString().toLowerCase(Locale.ROOT)))
            {
                return true;
            }
            break;
        }

        return false;
    }


}