package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.PlacesAdapter;
import com.angadi.tripmanagementa.adapters.VolunteersAdapter;
import com.angadi.tripmanagementa.models.AddPlacesResponse;
import com.angadi.tripmanagementa.models.AddVolunteerResponse;
import com.angadi.tripmanagementa.models.MembersResult;
import com.angadi.tripmanagementa.models.PlacesResult;
import com.angadi.tripmanagementa.models.SearchVolunteerResponse;
import com.angadi.tripmanagementa.models.ShowMembersResponse;
import com.angadi.tripmanagementa.models.ShowMembersResult;
import com.angadi.tripmanagementa.models.ShowPlacesResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVolunteersActivity extends AppCompatActivity {

//    @BindView(R.id.loading_layout)
//    View loadingIndicator;
    String mem_id, event_id, volunteer_id, volunteer_name,place_id="";
    @BindView(R.id.btn_addPlace)
    Button btn_addPlace;
    @BindView(R.id.edt_places)
    EditText edt_places;
    @BindView(R.id.recyclerPlaces)
    RecyclerView recyclerPlaces;
    @BindView(R.id.edt_emailMobile)
    EditText edt_emailMobile;
    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.txt)
    TextView textView;
    @BindView(R.id.recyclerVolunteers)
    RecyclerView recyclerVolunteers;
    RecyclerView recyclerView;
//    @BindView(R.id.spinner)
//    Spinner spinner;
//    ArrayList<String> arr_ids,arr_spinner_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_volunteers);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getIntent().getExtras() != null) {
            mem_id = getIntent().getStringExtra("mem_id");
            event_id = getIntent().getStringExtra("str_id");
            assert event_id != null;
            Log.e("event_id", event_id);
            Log.e("mem_id", mem_id);
            String title = getIntent().getStringExtra("mem_title");
            getSupportActionBar().setTitle("Add " + title);
            getPlaces(event_id);
            getVolunteers(event_id,mem_id);
        } else {
            getSupportActionBar().setTitle("Add Volunteers");
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick({R.id.btn_addPlace,R.id.img_search,R.id.txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_addPlace:
                if (edt_places.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please add Place", Toast.LENGTH_SHORT).show();
                } else {
                    addPlaces(event_id, edt_places.getText().toString());
                }
                break;
            case R.id.img_search:
                validateVolunteer();
                break;
            case R.id.txt:
                if (place_id.equalsIgnoreCase("")){
                    Toast.makeText(this, "Please select place", Toast.LENGTH_SHORT).show();
                }else {
                    addVolunteer(volunteer_id, event_id, "",place_id,mem_id);
                }
                break;
            default:
                break;
        }
    }

//    private void setSpinner(){
//        spinner.setVisibility(View.VISIBLE);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arr_spinner_items);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                place_id = arr_ids.get(i);
//                Log.e("place_id",place_id);
//                addVolunteer(volunteer_id, event_id, "",place_id);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }

    private void addPlaces(String event_id, String place) {
        MyProgressDialog.show(AddVolunteersActivity.this,"Loading...");
        String token = Prefs.with(AddVolunteersActivity.this).getString("token", "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AddPlacesResponse> call = apiInterface.addPlaces("true", token, event_id, place);
        call.enqueue(new Callback<AddPlacesResponse>() {
            @Override
            public void onResponse(Call<AddPlacesResponse> call, Response<AddPlacesResponse> response) {
                Log.e("addPlaces", new Gson().toJson(response));
               MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Toast.makeText(AddVolunteersActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    if (response.body().getMessage().equalsIgnoreCase("Place added!")){
                    getPlaces(event_id);
//                    }
                } else {
                    Toast.makeText(AddVolunteersActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddPlacesResponse> call, Throwable t) {
                Log.e("addPlaces", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }

    private void getPlaces(String event_id) {
        MyProgressDialog.show(AddVolunteersActivity.this,"Loading...");
        String token = Prefs.with(AddVolunteersActivity.this).getString("token", "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ShowPlacesResponse> call = apiInterface.showPlaces("true", event_id);
        call.enqueue(new Callback<ShowPlacesResponse>() {
            @Override
            public void onResponse(Call<ShowPlacesResponse> call, Response<ShowPlacesResponse> response) {
                Log.e("getPlaces", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    List<PlacesResult> resultList = response.body().getResults();
                    setPlacesAdapter(resultList);
                } else {
                    Toast.makeText(AddVolunteersActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ShowPlacesResponse> call, Throwable t) {
                Log.e("getPlaces", "" + t);
               MyProgressDialog.dismiss();
            }
        });
    }

    private void setPlacesAdapter(List<PlacesResult> resultList) {
        recyclerPlaces.setLayoutManager(new LinearLayoutManager(this));
        PlacesAdapter placesAdapter = new PlacesAdapter(this, resultList);
        recyclerPlaces.setAdapter(placesAdapter);

        placesAdapter.setClickListener(new PlacesAdapter.PlacesClickListener() {
            @Override
            public void onClick(View view, int position, String id,String uid) {
                place_id = id;
                Log.e("place_id",place_id);
            }
        });
    }

    private void validateVolunteer() {
        if (edt_emailMobile.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter Email / Mobile", Toast.LENGTH_SHORT).show();
        } else {
            searchVolunteer(edt_emailMobile.getText().toString());
        }
    }

    private void searchVolunteer(String login_id) {
        MyProgressDialog.show(AddVolunteersActivity.this,"Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SearchVolunteerResponse> call = apiInterface.searchVolunteer("true", login_id);
        call.enqueue(new Callback<SearchVolunteerResponse>() {
            @Override
            public void onResponse(Call<SearchVolunteerResponse> call, Response<SearchVolunteerResponse> response) {
                Log.e("searchVolunteer", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    volunteer_id = response.body().getUraId();
                    Log.e("volunteer_id",volunteer_id);
                    volunteer_name = response.body().getUraFname();
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(volunteer_name);
                } else {
                    Toast.makeText(AddVolunteersActivity.this, "" + response.body().getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchVolunteerResponse> call, Throwable t) {
                Log.e("searchVolunteer", "" + t);
               MyProgressDialog.dismiss();
            }
        });

    }


    private void addVolunteer(String uid, String event_id, String about,String places_id,String mem_id) {
        MyProgressDialog.show(AddVolunteersActivity.this,"Loading...");
        String token = Prefs.with(AddVolunteersActivity.this).getString("token", "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AddVolunteerResponse> call = apiInterface.addVolunteer("true", token, uid, event_id, about,places_id,mem_id);
        call.enqueue(new Callback<AddVolunteerResponse>() {
            @Override
            public void onResponse(Call<AddVolunteerResponse> call, Response<AddVolunteerResponse> response) {
                Log.e("addVolunteer", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Toast.makeText(AddVolunteersActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    textView.setText("");
                    edt_emailMobile.setText("");
                    place_id="";
                    textView.setVisibility(View.GONE);
                    getVolunteers(event_id,mem_id);
                } else {
                    Toast.makeText(AddVolunteersActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddVolunteerResponse> call, Throwable t) {
                Log.e("addVolunteer", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }

    private void getVolunteers(String event_id,String mem_id) {
        Log.e("event_id",event_id);
        Log.e("mem_id",mem_id);
        MyProgressDialog.show(AddVolunteersActivity.this,"Loading...");
        String token = Prefs.with(AddVolunteersActivity.this).getString("token", "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ShowMembersResponse> call = apiInterface.showMembers("true",token,event_id,mem_id);
        call.enqueue(new Callback<ShowMembersResponse>() {
            @Override
            public void onResponse(Call<ShowMembersResponse> call, Response<ShowMembersResponse> response) {
                Log.e("getVolunteers", new Gson().toJson(response));
               MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
//                    Toast.makeText(AddVolunteersActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.body().getCheckResponse().equalsIgnoreCase("VOLUNTEERS")){
                        List<ShowMembersResult> resultList = response.body().getResults();
                        setVolunteersAdapter(resultList);
                    }

                } else {
                    Toast.makeText(AddVolunteersActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ShowMembersResponse> call, Throwable t) {
                Log.e("getVolunteers", "" + t);
                MyProgressDialog.dismiss();
            }
        });

    }

    private void setVolunteersAdapter(List<ShowMembersResult> resultList) {
        recyclerVolunteers.setLayoutManager(new LinearLayoutManager(this));
        VolunteersAdapter volunteersAdapter = new VolunteersAdapter(this, resultList);
        recyclerVolunteers.setAdapter(volunteersAdapter);


        volunteersAdapter.setClickListener(new VolunteersAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position, String id,String volunteerName,String placeName,String placeID,String uId) {
                Log.e("placeID",placeID);
                Log.e("placeName",placeName);
                Log.e("uId",uId);

                showEditDialog(volunteerName,uId);
            }
        });

    }


    private void showEditDialog(String volunteerName,String uid){
        final Dialog alertDialog=new Dialog(AddVolunteersActivity.this,android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.edit_volunteer_dialog);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView title=(TextView)alertDialog.findViewById(R.id.dialog_txt);
        TextView txt_name=(TextView)alertDialog.findViewById(R.id.txt_name);
        TextView ok=(TextView)alertDialog.findViewById(R.id.dialog_btn);
        recyclerView = (RecyclerView)alertDialog.findViewById(R.id.recyclerView);
        getPlacesEditVolunteer(event_id);
        txt_name.setText("Volunteer Name: "+volunteerName);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

                if (place_id.equalsIgnoreCase("")){
                    Toast.makeText(AddVolunteersActivity.this, "Please select place", Toast.LENGTH_SHORT).show();
                }else {
                    updateVolunteer(uid, event_id, "",place_id,mem_id);
                }
            }
        });
        alertDialog.show();
    }

    private void getPlacesEditVolunteer(String event_id) {
        MyProgressDialog.show(AddVolunteersActivity.this,"Loading...");
        String token = Prefs.with(AddVolunteersActivity.this).getString("token", "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ShowPlacesResponse> call = apiInterface.showPlaces("true", event_id);
        call.enqueue(new Callback<ShowPlacesResponse>() {
            @Override
            public void onResponse(Call<ShowPlacesResponse> call, Response<ShowPlacesResponse> response) {
                Log.e("getPlacesEdit", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    List<PlacesResult> resultList = response.body().getResults();
                    setPlacesAdapterEdit(resultList);
                } else {
                    Toast.makeText(AddVolunteersActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ShowPlacesResponse> call, Throwable t) {
                Log.e("getPlacesEdit", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }

    private void setPlacesAdapterEdit(List<PlacesResult> resultList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PlacesAdapter placesAdapter = new PlacesAdapter(this, resultList);
        recyclerView.setAdapter(placesAdapter);

        placesAdapter.setClickListener(new PlacesAdapter.PlacesClickListener() {
            @Override
            public void onClick(View view, int position, String id, String uid) {
                place_id = id;
                Log.e("place_id",place_id);
            }
        });
    }

    private void updateVolunteer(String uid, String event_id, String about,String places_id,String mem_id) {
        Log.e("uid-->",uid);
        Log.e("event_id-->",event_id);
        Log.e("places_id-->",places_id);
        Log.e("mem_id-->",mem_id);
        MyProgressDialog.show(AddVolunteersActivity.this,"Loading...");
        String token = Prefs.with(AddVolunteersActivity.this).getString("token", "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AddVolunteerResponse> call = apiInterface.updateVolunteer("true", token, uid, event_id, about,places_id,mem_id,"true");
        call.enqueue(new Callback<AddVolunteerResponse>() {
            @Override
            public void onResponse(Call<AddVolunteerResponse> call, Response<AddVolunteerResponse> response) {
                Log.e("updateVolunteer", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Toast.makeText(AddVolunteersActivity.this, "Volunteer Updated", Toast.LENGTH_SHORT).show();
//                    textView.setText("");
//                    edt_emailMobile.setText("");
                    place_id="";
//                    textView.setVisibility(View.GONE);
                    getVolunteers(event_id,mem_id);
                } else {
                    Toast.makeText(AddVolunteersActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddVolunteerResponse> call, Throwable t) {
                Log.e("updateVolunteer", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }


}
