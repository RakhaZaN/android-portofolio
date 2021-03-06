package com.belajar.newportofolio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IntegratedApi extends AppCompatActivity {

    Spinner ket;
    EditText et_name, et_email, et_phone, etID;
    ProgressBar pb;
    Button btnSave;
    ListView listView;

    //    Request Code
    private final int CODE_GET_REQUEST = 1024;
    private final int CODE_POST_REQUEST = 1025;

    boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integrated_api);

//        Initialize
        etID = findViewById(R.id.etId);
        ket = findViewById(R.id.status_list);
        et_name = findViewById(R.id.input_name);
        et_email = findViewById(R.id.input_email);
        et_phone = findViewById(R.id.input_phone);
        pb = findViewById(R.id.progressbar);
        btnSave = findViewById(R.id.button);
        listView = findViewById(R.id.arr_list);

        btnSave.setOnClickListener(this::onClick);
        readData();
    }

    private void onClick(View view) {
//        if isUpdating is true
        if (isUpdating)
            updateData();
        else
            buatData();
    }

    private void readData() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void buatData() {
        String name = et_name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String keterangan = ket.getSelectedItem().toString();

//        validating the inputs
        if (TextUtils.isEmpty(name)) {
            et_name.setError("Name Cannot be Null");
            et_name.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            et_email.setError("Email Cannot be Null");
            et_email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            et_phone.setError("Phone Cannot be Null");
            et_phone.requestFocus();
            return;
        }

//        if passes
        HashMap<String, String> params = new HashMap<>();
        params.put("nama", name);
        params.put("email", email);
        params.put("nohp", phone);
        params.put("keterangan", keterangan);

//        Calling the create API
        PerformNetworkRequest req = new PerformNetworkRequest(Api.URL_CREATE, params, CODE_POST_REQUEST);
        req.execute();

        et_name.setText("");
        et_email.setText("");
        et_phone.setText("");
        ket.setSelection(0);
    }

    public void updateData() {
        String id = etID.getText().toString();
        String name = et_name.getText().toString();
        String email = et_email.getText().toString();
        String phone = et_phone.getText().toString();
        String keterangan = ket.getSelectedItem().toString();

        if (TextUtils.isEmpty(name)) {
            et_name.setError("Please enter name");
            et_name.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            et_email.setError("Please enter email");
            et_email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            et_phone.setError("Please enter phone number");
            et_phone.requestFocus();
            return;
        }


        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("nama", name);
        params.put("email", email);
        params.put("nohp", phone);
        params.put("keterangan", keterangan);


        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE, params, CODE_POST_REQUEST);
        request.execute();

        btnSave.setText("save");

        et_name.setText("");
        et_email.setText("");
        et_phone.setText("");
        ket.setSelection(0);

        isUpdating = false;
    }

    public void deleteData(int id) {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE + "&id=" + id, null, CODE_GET_REQUEST);
        request.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //        The url where needed to sending the request
        String url;

        List<Biodata> biodataList = new ArrayList<>();

        //        The params
        HashMap<String, String> params;

        //        The request code to define wheter it is a GET or POST
        int reqCode;

        //        Constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int reqCode) {
            this.reqCode = reqCode;
            this.params = params;
            this.url = url;
        }

        //        When the task started displaying a progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        //        This methode will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pb.setVisibility(View.GONE);

            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
//                    refreshing the list after every operation
//                    so we get an updated list
//                    we will create this method right now it is commented
//                    because we haven't created it yet
                    Toast.makeText(IntegratedApi.this, "Success", Toast.LENGTH_SHORT).show();
                    refreshDataku(object.getJSONArray("heroes"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void refreshDataku(JSONArray data) throws JSONException {
//            clearing previous data
            biodataList.clear();

//            traversing through all the items in the json array
//            the json we got from the response
            for (int i = 0; i < data.length(); i++) {
//                getting each hero object
                JSONObject object = data.getJSONObject(i);
                System.out.println(object);

//                Adding to the list
                biodataList.add(new Biodata(
                        object.getInt("id"),
                        object.getString("name"),
                        object.getString("email"),
                        object.getString("nohp"),
                        object.getString("keterangan")
                ));
            }

//            Creating the adapter and setting it to the listview
            DataAdapter adapter = new DataAdapter(biodataList);
            listView.setAdapter(adapter);
        }

        //        the network operation will be performed in background
        @Override
        protected String doInBackground(Void... voids) {
            ReqHandler reqHandler = new ReqHandler();
            if (reqCode == CODE_POST_REQUEST)
                return reqHandler.sendPostRequest(url, params);
            else if (reqCode == CODE_GET_REQUEST)
                return reqHandler.sendGetRequest(url);
            return null;
        }

    }

    class DataAdapter extends ArrayAdapter<Biodata> {
        List<Biodata> list;

        //        constructor to get the tlist
        public DataAdapter(List<Biodata> biodataList) {
            super(IntegratedApi.this, R.layout.layout_data_list, biodataList);
            this.list = biodataList;
        }

        //        Returning list item
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View lvItem = inflater.inflate(R.layout.layout_data_list, null, true);

//            getting textview display name
            TextView tvName = lvItem.findViewById(R.id.txtName);
            ImageView ic = lvItem.findViewById(R.id.imageView);
//            update and delete
            TextView tvUpdate = lvItem.findViewById(R.id.txtUpdate);
            TextView tvDelete = lvItem.findViewById(R.id.txtDelete);

            final Biodata biodata = list.get(position);
//            Change display name
            tvName.setText(biodata.getName());

//            Choose display icon
            switch (biodata.getKet().trim()) {
                case "teman":
                    ic.setImageResource(R.drawable.ic_user);
                    break;
                case "sahabat":
                    ic.setImageResource(R.drawable.ic_user_checklist);
                    break;
                case "cuma kenal":
                    ic.setImageResource(R.drawable.ic_user_history);
                    break;
                case "gakenal":
                    ic.setImageResource(R.drawable.ic_user_cancel);
                    break;
                case "no comment":
                    ic.setImageResource(R.drawable.ic_user_slash);
                    break;
            }

//            attaching click listener to update
            tvUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    when updating
//                    make the isUpdate as true
                    isUpdating = true;

//                    Set the selected hero to the elements
                    etID.setText(String.valueOf(biodata.getId()));
                    et_name.setText(biodata.getName());
                    et_email.setText(biodata.getEmail());
                    et_phone.setText(biodata.getPhone());
                    ket.setSelection(((ArrayAdapter<String>) ket.getAdapter()).getPosition(biodata.getKet()));

//                    make the button text to update
                    btnSave.setText("update");
                }
            });

//            when the user selected delete
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    display alert confirmation before deleting
                    AlertDialog.Builder confirm = new AlertDialog.Builder(IntegratedApi.this);

                    confirm.setTitle("Delete " + biodata.getName())
                            .setMessage("Are you sure want to delete it?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    if the choice is yes
//                                    method is commentd becaouse it is not created yet
                                    deleteData(biodata.getId());
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

            return lvItem;
        }
    }
}