package trancongtien.com.doan.houses.houseDetail.rooms.roomDetail.tenants;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import trancongtien.com.doan.R;
import trancongtien.com.doan.houses.houseDetail.rooms.roomDetail.RoomDetailSystem;
import trancongtien.com.doan.model.Houses;
import trancongtien.com.doan.model.Rooms;
import trancongtien.com.doan.model.Tenants;

public class AddTenant extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    Houses houses;
    Rooms rooms;

    ImageView imgBack, img_addTenant;
    TextInputEditText textInputEdt_tenantName, textInputEdt_phoneNumber, textInputEdt_tenantEmail;

    TextView txt_selectDob, txt_selectNgayCapCMND, txt_showRentHouse, txt_showRentRoom;

    EditText edt_noiSinh, edt_soCMND, edt_noiCapCMND;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tenant);

        initUI();
        init();
    }
    private void init()
    {
        houses = getIntent().getParcelableExtra("Data_House_Parcelable");
        rooms  = getIntent().getParcelableExtra("Data_Room_Parcelable");

        txt_showRentHouse.setText(houses.gethName());
        txt_showRentRoom.setText(rooms.getrName());

        img_addTenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeAddTtenant();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToRoomDetail();
            }
        });

        txt_selectDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(txt_selectDob);
            }
        });


        txt_selectNgayCapCMND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(txt_selectNgayCapCMND);
            }
        });
    }

    private void executeAddTtenant() {
        String tName = textInputEdt_tenantName.getText().toString().trim();
        String tPhoneNumber = textInputEdt_phoneNumber.getText().toString().trim();
        String tEmail = textInputEdt_tenantEmail.getText().toString().trim();
        String tRentHouse = txt_showRentHouse.getText().toString().trim();
        String tRentRoom = txt_showRentRoom.getText().toString().trim();
        String tDob = txt_selectDob.getText().toString().trim();
        String tNoiSinh = edt_noiSinh.getText().toString().trim();
        String tSoCMND = edt_soCMND.getText().toString().trim();
        String tNgayCapCMND = txt_selectNgayCapCMND.getText().toString().trim();
        String tNoiCapCMND = edt_noiCapCMND.getText().toString().trim();


        if (tName.equals(""))
        {
            Toast.makeText(this, "Error : Nhập tên người thuê", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (tPhoneNumber.equals(""))
        {
            Toast.makeText(this, "Error : Nhập số điện thoại người thuê", Toast.LENGTH_SHORT).show();
            return;
        }


        // Get current Datetime
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());

        String tenantId = "tenant_" + currentDateAndTime + "_" + firebaseUser.getUid();
        Tenants tenants = new Tenants(tenantId, houses.gethId(), rooms.getId(), tName, tPhoneNumber, tRentHouse,
                tRentRoom, tEmail, tDob, tNoiSinh,  tSoCMND,tNgayCapCMND, tNoiCapCMND );

        myRef.child("tenants").child(firebaseUser.getUid()).child(tenantId).setValue(tenants);

        Toast.makeText(this, "Thêm người thuê Thành Công !", Toast.LENGTH_SHORT).show();
        backToRoomDetail();
    }


    private void backToRoomDetail() {
        Intent intent = new Intent(AddTenant.this, RoomDetailSystem.class);
        intent.putExtra("Data_RoomOfHouse_Parcelable", houses);
        intent.putExtra("Data_Room_Parcelable", rooms);

        startActivity(intent);

        AddTenant.this.finish();
    }

    /*******************************************************
     *
     * (Related to) Date Picker
     *
     ******************************************************* */
    private void datePicker(TextView showPickTime)
    {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);


        // Implement date picker to get user's choice date
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddTenant.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int Myear, int Mmonth, int MdayOfMonth) {
                String FinalDate = (MdayOfMonth + "/" + (Mmonth + 1) + "/" + (Myear) );

                showPickTime.setText(FinalDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }



    private void initUI() {
        imgBack     = findViewById(R.id.imgBack);
        img_addTenant = findViewById(R.id.img_addTenant);

        textInputEdt_tenantName     = findViewById(R.id.textInputEdt_tenantName);
        textInputEdt_phoneNumber    = findViewById(R.id.textInputEdt_phoneNumber);
        textInputEdt_tenantEmail    = findViewById(R.id.textInputEdt_tenantEmail);

        txt_selectDob           = findViewById(R.id.txt_selectDob);
        txt_selectNgayCapCMND   = findViewById(R.id.txt_selectNgayCapCMND);
        txt_showRentHouse  = findViewById(R.id.txt_showRentHouse);
        txt_showRentRoom   = findViewById(R.id.txt_showRentRoom);

        edt_noiSinh    = findViewById(R.id.edt_noiSinh);
        edt_soCMND     = findViewById(R.id.edt_soCMND);
        edt_noiCapCMND = findViewById(R.id.edt_noiCapCMND);

    }



    @Override
    public void onBackPressed() {
        backToRoomDetail();

        super.onBackPressed();
    }
}