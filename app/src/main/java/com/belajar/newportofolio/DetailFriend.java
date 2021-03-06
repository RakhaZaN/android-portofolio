package com.belajar.newportofolio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailFriend extends AppCompatActivity {

    private EditText etName, etDay, etText;
    private ImageView pp;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch sEmail;
    private RadioButton male, female;
    private RadioGroup gen;
    private CheckBox cbHome, cbSch;
    private SimpleDateFormat sdf;

    private DbHandler mydb;
    int updateId;

    public static final int CAMERA_REQUEST = 1;
    public static final int SELECT = 2;
    String currphotopath = "default";
    File photoFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_friend);

        etName = findViewById(R.id.txtName);
        etDay = findViewById(R.id.txtday);
        etText = findViewById(R.id.sometxt);
        sEmail = findViewById(R.id.sEmail);
        male = findViewById(R.id.rbmale);
        female = findViewById(R.id.rbfemale);
        gen = findViewById(R.id.rg);
        cbHome = findViewById(R.id.cbHome);
        cbSch = findViewById(R.id.cbSch);
        pp = findViewById(R.id.imageView);
        sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        mydb = new DbHandler(this);


        load();
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, SELECT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Bundle data = getIntent().getExtras();

        if (data != null) {
            int valId = data.getInt("id");
            if (valId > 0) {
                getMenuInflater().inflate(R.menu.manipulate_menu, menu);
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.item1:
//                make new
                return true;
            case R.id.item2:
//                delete
                AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setMessage("Wanna delete from friends list")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int check = mydb.delData(updateId);
                                if (check == 1) {
                                    Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_LONG).show();
                                    Intent home = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(home);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Something Wrong, Try Again !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("cancel", null);
                AlertDialog b = ab.create();
                b.setTitle("Are you sure..");
                b.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void showDateDialog(View view) {

        Calendar cal = Calendar.getInstance();
        DatePickerDialog dp = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(i, i1, i2);
                etDay.setText(sdf.format(newDate.getTime()));
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dp.show();

    }

    public void takeImage(View view) {
        checkPermission(Manifest.permission.CAMERA, CAMERA_REQUEST);
        AlertDialog.Builder choosen = new AlertDialog.Builder(this)
                .setTitle("Choosen Image")
                .setItems(R.array.uploadImage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
//                                Mengakses Camera
                                Intent fromcamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (fromcamera.resolveActivity(getPackageManager()) != null) {
//                                    create file
                                    try {
                                        photoFile = createImageFile();
                                    } catch (IOException e) {
                                        Toast.makeText(getApplicationContext(), "Failed to create photo file", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

//                                    Lanjut jika file berhasil dibuat
                                    if (photoFile != null) {
                                        Uri photoUri = FileProvider.getUriForFile(getApplicationContext(),
                                                "com.belajar.portofolio.fileprovider", photoFile);
                                        fromcamera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                        startActivityForResult(fromcamera, CAMERA_REQUEST);
                                    }
                                }
                                break;
                            case 1:
//                                Mengakses Gallery
                                Intent togallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(togallery, SELECT);

                                break;
                            case 2:
//                                To Default
                                pp.setImageResource(R.drawable.ic_smile);
                                currphotopath = "default";
                                break;
                        }
                    }
                });
        choosen.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                Bitmap bMapScaled = Bitmap.createScaledBitmap(takenImage, 100, 100, true);
                currphotopath = photoFile.getAbsolutePath();
                pp.setImageDrawable(null);
                pp.setImageBitmap(bMapScaled);
                Toast.makeText(this, "Picture saved ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SELECT && resultCode == RESULT_OK) {
            assert data != null;
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (selectedImage != null) {
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    currphotopath = cursor.getString(columnIndex);
                    pp.setImageDrawable(null);
                    pp.setImageBitmap(BitmapFactory.decodeFile(currphotopath));
                    cursor.close();
                }
            }
        }

    }

    public void save(View view) {
        Bundle extras = getIntent().getExtras();

        String valname = etName.getText().toString();
        String valdate = etDay.getText().toString();
        String valtext = etText.getText().toString();
        String valemail = sEmail.isChecked() ? "on" : "off";
        String valgender = male.isChecked() ? "male" : "female";
        String valhome = cbHome.isChecked() ? "check" : "";
        String valsch = cbSch.isChecked() ? "check" : "";

        if (extras != null) {
            int valId = extras.getInt("id");
            Intent back = new Intent(this, MainActivity.class);
            if (valId > 0) {
                boolean update = mydb.updateFriend(updateId, valname, valemail, valdate, valgender, valhome,valsch,valtext, currphotopath);
                if (update) {
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    startActivity(back);
                } else {
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            } else {
                boolean insert = mydb.addFriend(valname,valemail,valdate,valgender,valhome,valsch,valtext,currphotopath);
                if (insert) {
                    Toast.makeText(getApplicationContext(), "Added new friend", Toast.LENGTH_SHORT).show();
                    startActivity(back);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to Add", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void load() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int valId = extras.getInt("id");

            if (valId > 0) {
                Cursor rs = mydb.getData(valId);
                updateId = valId;
                rs.moveToFirst();

                String name = rs.getString(rs.getColumnIndex(DbHandler.NAME));
                String date = rs.getString(rs.getColumnIndex(DbHandler.BIRTH));
                String text = rs.getString(rs.getColumnIndex(DbHandler.SOMETEXT));
                String email = rs.getString(rs.getColumnIndex(DbHandler.EMAIL));
                String gender = rs.getString(rs.getColumnIndex(DbHandler.GENDER));
                String home = rs.getString(rs.getColumnIndex(DbHandler.F_HOME));
                String sch = rs.getString(rs.getColumnIndex(DbHandler.F_SCH));
                String ppath = rs.getString(rs.getColumnIndex(DbHandler.PATH));

                if (!rs.isClosed()) {
                    rs.close();
                }

                etName.setText(name);
                etDay.setText(date);
                etText.setText(text);
                sEmail.setChecked(email.equals("on"));
                gen.check(gender.equals("male") ? R.id.rbmale : R.id.rbfemale);
                cbHome.setChecked(home.equals("check"));
                cbSch.setChecked(sch.equals("check"));
                if (!ppath.equals("default")) {
//                    show picture
                    pp.setImageDrawable(null);
                    File imgFile = new File(ppath);
                    Bitmap takenImage = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                    Bitmap bMapScaled = Bitmap.createScaledBitmap(takenImage, 100, 100, true);
                    pp.setImageBitmap(takenImage);
                } else {
                    pp.setImageResource(R.drawable.ic_smile);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
//        Create file name
        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "FRIEND_" + ts + "_";
        File storeDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                filename, ".jpg", storeDir
        );
//        Save Path
        currphotopath = image.getAbsolutePath();
        return image;
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(DetailFriend.this, new String[] { permission }, requestCode);

        } // else {
//            Toast.makeText(DetailFriend.this, "Permission already granted", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Showing the toast message
                Toast.makeText(getApplicationContext(), "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == SELECT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }
}