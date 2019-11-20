package com.angadi.tripmanagementa.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.EventDetailsActivity;
import com.angadi.tripmanagementa.adapters.GalleryAdapter;
import com.angadi.tripmanagementa.models.ProfileDislikeResponse;
import com.angadi.tripmanagementa.models.ProfileFavResponse;
import com.angadi.tripmanagementa.models.ProfileGallery;
import com.angadi.tripmanagementa.models.ProfileLikeResponse;
import com.angadi.tripmanagementa.models.ProfileRatingResponse;
import com.angadi.tripmanagementa.models.ProfileResponse;
import com.angadi.tripmanagementa.models.ProfileStatusResponse;
import com.angadi.tripmanagementa.models.QrScanResponse;
import com.angadi.tripmanagementa.models.ScanEventQrResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Constants;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class ScanProfileDialogFragment extends DialogFragment {

    public static final String TAG = "example_dialog";

    private Toolbar toolbar;
    RecyclerView recyclerView;
    EditText edt_search;
    View loading;
    private String title, qr_code_id, qr_code_type, token, qr_url, user_id;
    private String str_location, str_mobile, str_email, str_website, str_whatsApp, str_facebook, str_youtube, str_instagram, str_linkedin, str_profile_id, str_rating, str_qr_code_id, str_name;
    private ProfileDialogListener mListener;
    View view;
    @BindView(R.id.imageView)
    SimpleDraweeView imageView;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_cat)
    TextView txt_cat;
    @BindView(R.id.txt_mobile)
    TextView txt_mobile;
    @BindView(R.id.txt_email)
    TextView txt_email;
    @BindView(R.id.txt_website)
    TextView txt_website;
    @BindView(R.id.txt_address)
    TextView txt_address;
    @BindView(R.id.progress)
    AVLoadingIndicatorView progressBar;
    @BindView(R.id.img_fav)
    ImageView img_fav;
    @BindView(R.id.img_like)
    ImageView img_like;
    @BindView(R.id.img_dislike)
    ImageView img_dislike;
    @BindView(R.id.txt_fav)
    TextView txt_fav;
    @BindView(R.id.txt_like)
    TextView txt_like;
    @BindView(R.id.txt_dislike)
    TextView txt_dislike;
    @BindView(R.id.txt_ratings)
    TextView txt_ratings;
    @BindView(R.id.txt_ratings_avg)
    TextView txt_ratings_avg;
    @BindView(R.id.layout_main)
    LinearLayout layout_main;
    @BindView(R.id.layout_loading)
    LinearLayout layout_loading;
    double screenInches;
    BitMatrix result;
    boolean mLike, mDislike, mFavorite;
    Bitmap bitmap, bitmapQrborder;
    @BindView(R.id.recyclerViewGallery)
    RecyclerView recyclerViewGallery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    public static ScanProfileDialogFragment newInstance(String title, String qr_code_id, String qr_code_type, String qr_url, String user_id, ProfileDialogListener listener) {
        ScanProfileDialogFragment dialogFragment = new ScanProfileDialogFragment();
        dialogFragment.title = title;
        dialogFragment.qr_code_id = qr_code_id;
        dialogFragment.qr_code_type = qr_code_type;
        dialogFragment.qr_url = qr_url;
        dialogFragment.user_id = user_id;
        dialogFragment.mListener = listener;
        return dialogFragment;
    }


    public interface DialogListener {
        void updateResult(View view, int position, String id, String name, Integer cost, Integer tax);
    }

    public interface ProfileDialogListener {
        public void onProfilePositiveClick(DialogFragment dialog);
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            this.mListener = (OnCompleteListener)activity;
//        }
//        catch (final ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
//            MyProgressDialog.show(getActivity(), "Loading...");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.scan_profile_fragment, container, false);
        ButterKnife.bind(this, view);
        toolbar = view.findViewById(R.id.toolbar);
        progressBar.setVisibility(View.VISIBLE);
        layout_loading.setVisibility(View.VISIBLE);
        layout_main.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        try {
            token = Prefs.with(getActivity()).getString("token", "");
            Log.e("token", token);
            Log.e("user_id", user_id);
            Log.e("qr_code_id", qr_code_id);
            Log.e("qr_code_type", qr_code_type);
            Log.e("qr_url", qr_url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  Toast.makeText(getActivity(), ""+qr_code_id, Toast.LENGTH_SHORT).show();
//        toolbar.inflateMenu(R.menu.example_dialog);
//        toolbar.setOnMenuItemClickListener(item -> {
//            dismiss();
//            return true;
//        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onProfilePositiveClick(ScanProfileDialogFragment.this);
                }
                dismiss();
            }
        });

        getProfile(qr_code_id);

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null) {
            mListener.onProfilePositiveClick(ScanProfileDialogFragment.this);
        }
        dialog.dismiss();
    }


    private void getProfile(String qr_id) {
//        String ps2 = "dGVjaFBhC3M=";
        String val2 = null;
        byte[] tmp2 = Base64.decode(qr_id, Base64.DEFAULT);
        try {
            val2 = new String(tmp2, "UTF-8");
            Log.e("val2",val2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProfileResponse> responseCall = apiInterface.getScanProfile("true", token, val2);
        responseCall.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                Log.e("profile_res", new Gson().toJson(response));
                progressBar.setVisibility(View.GONE);
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        layout_loading.setVisibility(View.GONE);
                        layout_main.setVisibility(View.VISIBLE);
                        displayTexts(response);
                    } else {
                        Toast.makeText(getActivity(), response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.e("profile_res", "" + t);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void displayTexts(Response<ProfileResponse> response) {
        assert response.body() != null;

        if (!response.body().getUraFname().equalsIgnoreCase("")) {
            txt_name.setText(response.body().getUraFname());
        }
        if (!response.body().getUraBizEmail().equalsIgnoreCase("")) {
            txt_email.setText(response.body().getUraBizEmail());
        }
        if (!response.body().getUraAddress().equalsIgnoreCase("")) {
            txt_address.setText(response.body().getUraAddress());
        }
        if (!response.body().getUraWebsite().equalsIgnoreCase("")) {
            txt_website.setText(response.body().getUraWebsite());
        }
        if (!response.body().getUraBizPhone().equalsIgnoreCase("")) {
            txt_mobile.setText(response.body().getUraBizPhone());
        }

        Log.e("getUraImg", "--->" + response.body().getUraImg());
        if (response.body().getUraImg().equalsIgnoreCase("NULL")) {
            imageView.setImageResource(R.drawable.ic_placeholder);
        } else {
            imageView.setImageURI(Constants.BASE_URL + response.body().getUraImg());
        }
        str_mobile = response.body().getUraBizPhone();
        str_location = response.body().getUraAddress();
        str_email = response.body().getUraBizEmail();
        str_website = response.body().getUraWebsite();
        str_whatsApp = response.body().getUraWhatsapp();
        str_instagram = response.body().getUraInstagram();
        str_facebook = response.body().getUraFacebook();
        str_youtube = response.body().getUraYoutube();
        str_linkedin = response.body().getUraLinkedin();
        str_profile_id = response.body().getUraId();
        str_qr_code_id = response.body().getUraCodeIdSecureLink();
        str_name = response.body().getUraFname();

        List<ProfileGallery> galleryList = response.body().getUraGallerys();
        recyclerViewGallery.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        GalleryAdapter galleryAdapter = new GalleryAdapter(getActivity(), galleryList,"scan");
        recyclerViewGallery.setAdapter(galleryAdapter);

        getStatus(str_profile_id);
    }

    @OnClick({R.id.layout_fav, R.id.layout_like, R.id.layout_dislike, R.id.layout_share, R.id.layout_ratings, R.id.layout_mobile,
            R.id.layout_email, R.id.layout_website, R.id.layout_location, R.id.layout_whatsApp, R.id.layout_facebook, R.id.layout_youtube,
            R.id.layout_instagram, R.id.layout_linkedIn, R.id.btn_reach_us})
    public void onLayoutClick(View view) {
        switch (view.getId()) {
            case R.id.layout_fav:
                mFavorite = !mFavorite;
                if (mFavorite) {
                    postFavs(str_profile_id, "1");
                } else {
                    postFavs(str_profile_id, "0");
                }
                break;
            case R.id.layout_like:
                mLike = !mLike;
                if (mLike) {
                    postLikes(str_profile_id, "1");
                } else {
                    postLikes(str_profile_id, "0");
                }
                break;
            case R.id.layout_dislike:
                mDislike = !mDislike;
                if (mDislike) {
                    postDislikes(str_profile_id, "1");
                } else {
                    postDislikes(str_profile_id, "0");
                }
                break;
            case R.id.layout_share:
                shareProfileQR(str_qr_code_id, str_name);
                break;
            case R.id.layout_ratings:
                showRatingsDialog();
                break;
            case R.id.layout_mobile:
                if (!str_mobile.equalsIgnoreCase("")) {
                    callPhoneNumber(str_mobile);
                }
                break;
            case R.id.layout_email:
                if (!str_email.equalsIgnoreCase("")) {
                    startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + str_email)));
                }
                break;
            case R.id.layout_website:
                if (!str_website.equalsIgnoreCase("")) {
                    openUri(str_website);
                }
                break;
            case R.id.layout_location:
                if (!str_location.equalsIgnoreCase("")) {
                    setLocation(str_location);
                }
                break;
            case R.id.layout_whatsApp:
                if (!str_whatsApp.equalsIgnoreCase("")) {
                    whatsApp(str_whatsApp);
                }
                break;
            case R.id.layout_facebook:
                if (!str_facebook.equalsIgnoreCase("")) {
                    openUri(str_facebook);
                }
                break;
            case R.id.layout_youtube:
                if (!str_youtube.equalsIgnoreCase("")) {
                    openUri(str_youtube);
                }
                break;
            case R.id.layout_instagram:
                if (!str_instagram.equalsIgnoreCase("")) {
                    openUri(str_instagram);
                }
                break;
            case R.id.layout_linkedIn:
                if (!str_linkedin.equalsIgnoreCase("")) {
                    openUri(str_linkedin);
                }
                break;
            case R.id.btn_reach_us:
                if (!str_email.equalsIgnoreCase("")){
                    sendReachUsMail(str_email,str_name);
                }else {
                    Toast.makeText(getActivity(), "Email not found", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void callPhoneNumber(String phoneNumber) {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + phoneNumber));
                            startActivity(intent);
//                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel:", phoneNumber, null));
//                            startActivity(intent);
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void setLocation(String address) {
        String map = "http://maps.google.co.in/maps?q=" + address;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(intent);
    }

    private void whatsApp(String number) {
        PackageManager pm = getActivity().getPackageManager();
        boolean isInstalled = isPackageInstalled("com.whatsapp", pm);

        if (isInstalled) {
            // Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.whatsapp");
            // startActivity(launchIntent);
            Intent i = new Intent(Intent.ACTION_VIEW);

            try {
                String url = "https://api.whatsapp.com/send?phone=" + "+91" + number + "&text=" + URLEncoder.encode("", "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(pm) != null) {
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getActivity(), "WhatsApp is not installed in this device", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {

        boolean found = true;

        try {

            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {

            found = false;
        }

        return found;
    }

    private void openUri(String uri) {
        if (!uri.equals("")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + uri));
            startActivity(intent);
        }
    }

    private void getStatus(String pro_id) {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProfileStatusResponse> call = apiInterface.profileStatus("true", token, pro_id);
        call.enqueue(new Callback<ProfileStatusResponse>() {
            @Override
            public void onResponse(Call<ProfileStatusResponse> call, Response<ProfileStatusResponse> response) {
                Log.e("getStatus", new Gson().toJson(response));
                progressBar.setVisibility(View.GONE);
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        setFavsAndRatings(response);
                    } else {
                        Toast.makeText(getActivity(), response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProfileStatusResponse> call, Throwable t) {
                Log.e("getStatus", "" + t);
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void setFavsAndRatings(Response<ProfileStatusResponse> response) {
        assert response.body() != null;
        if (response.body().getDislike().equalsIgnoreCase("1")) {
            img_dislike.setImageResource(R.drawable.ic_thumb_down);
            mDislike = false;
        } else {
            img_dislike.setImageResource(R.drawable.ic_thumb_down_yellow);
            mDislike = true;
        }

        if (response.body().getLike().equalsIgnoreCase("1")) {
            img_like.setImageResource(R.drawable.ic_thumb_up);
            mLike = false;
        } else {
            img_like.setImageResource(R.drawable.ic_thumb_up_yellow);
            mLike = true;
        }

        if (response.body().getFav().equalsIgnoreCase("1")) {
            img_fav.setImageResource(R.drawable.ic_favorite);
            mFavorite = false;
        } else {
            img_fav.setImageResource(R.drawable.ic_favorite_yellow);
            mFavorite = true;
        }

        txt_like.setText(response.body().getLikeCnt() + " Likes");
        txt_dislike.setText(response.body().getDislikeCnt() + " Dislikes");
        txt_fav.setText(response.body().getFavCnt() + " Favorites");
        txt_ratings.setText(response.body().getRateCnt() + " Ratings");
        txt_ratings_avg.setText(response.body().getRateAvg());
    }

    private void postLikes(String pro_id, String pro_type) {
        MyProgressDialog.show(getActivity(), "Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProfileLikeResponse> call = apiInterface.profileLike("true", token, pro_id, pro_type);
        call.enqueue(new Callback<ProfileLikeResponse>() {
            @Override
            public void onResponse(Call<ProfileLikeResponse> call, Response<ProfileLikeResponse> response) {
                Log.e("postLikes", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
//                        if (response.body().getMessage().equalsIgnoreCase("Inserted!")){
//                            Toast.makeText(getActivity(), "Like Successful", Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(getActivity(), "Like Successful", Toast.LENGTH_SHORT).show();
//                        }
                        getStatus(str_profile_id);
                    } else {
                        Toast.makeText(getActivity(), "Like Failure", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProfileLikeResponse> call, Throwable t) {
                Log.e("postLikes", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }

    private void postFavs(String pro_id, String pro_type) {
        MyProgressDialog.show(getActivity(), "Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProfileFavResponse> call = apiInterface.profileFav("true", token, pro_id, pro_type);
        call.enqueue(new Callback<ProfileFavResponse>() {
            @Override
            public void onResponse(Call<ProfileFavResponse> call, Response<ProfileFavResponse> response) {
                Log.e("postFavs", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        getStatus(str_profile_id);
                    } else {
                        Toast.makeText(getActivity(), "Like Failure", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProfileFavResponse> call, Throwable t) {
                Log.e("postFavs", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }

    private void postDislikes(String pro_id, String pro_type) {
        MyProgressDialog.show(getActivity(), "Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProfileDislikeResponse> call = apiInterface.profileDisLike("true", token, pro_id, pro_type);
        call.enqueue(new Callback<ProfileDislikeResponse>() {
            @Override
            public void onResponse(Call<ProfileDislikeResponse> call, Response<ProfileDislikeResponse> response) {
                Log.e("postDislikes", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        getStatus(str_profile_id);
                    } else {
                        Toast.makeText(getActivity(), "Like Failure", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProfileDislikeResponse> call, Throwable t) {
                Log.e("postDislikes", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }

    private void showRatingsDialog() {
        final Dialog alertDialog = new Dialog(getActivity(), android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_ratings);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RatingBar ratingBar = alertDialog.findViewById(R.id.ratingBar);
        Button submit = alertDialog.findViewById(R.id.btn_submit);

//        LayerDrawable stars=(LayerDrawable)ratingBar.getProgressDrawable();
//        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                str_rating = String.valueOf(ratingBar.getRating());
                Log.e("str_rating", str_rating);
                postRatings(str_profile_id, str_rating);
            }
        });
        alertDialog.show();
    }

    private void postRatings(String pro_id, String pro_rate) {
        MyProgressDialog.show(getActivity(), "Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProfileRatingResponse> call = apiInterface.profileRating("true", token, pro_id, pro_rate);
        call.enqueue(new Callback<ProfileRatingResponse>() {
            @Override
            public void onResponse(Call<ProfileRatingResponse> call, Response<ProfileRatingResponse> response) {
                Log.e("postRatings", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        getStatus(str_profile_id);
                    } else {
                        Toast.makeText(getActivity(), "Like Failure", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProfileRatingResponse> call, Throwable t) {
                Log.e("postRatings", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }


    private void shareProfileQR(String str_qr_id, String bitmap_name) {
        getScreenResolution();
        try {
            bitmapQrborder = writeTextOnDrawable(R.drawable.new_pro_frame, bitmap_name).getBitmap();
            bitmap = encodeAsBitmap(str_qr_id);
            shareQr();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void getScreenResolution() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double wi = (double) width / (double) dm.xdpi;
        double hi = (double) height / (double) dm.ydpi;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        screenInches = Math.sqrt(x + y);
    }

    Bitmap encodeAsBitmap(String list) throws WriterException {
        Log.e("-----------------", String.valueOf(screenInches));

        try {
            Log.e("screenInches---->", String.valueOf(screenInches));
            if (screenInches > 5.0 && screenInches < 5.5) {
                if (screenInches < 5.3){
                    Log.e("first", "5.3-->");
                    result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 1250, 1250, null);
                }else {
                    Log.e("first", "--->");
                    result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 800, 800, null);
                }
            } else if (screenInches > 5.5 && screenInches < 6.0) {
                Log.e("second", "--->");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 1000, 1000, null);
            } else if (screenInches > 6.0) {
                Log.e("third", "--->");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 1200, 1200, null);
            } else if (screenInches < 5.0 && screenInches > 4.0) {
                Log.e("fourth", "--->");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 700, 700, null);
            } else {
                Log.e("else", "--->");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 800, 800, null);
            }
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

    private void shareQr() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Bitmap bitmap_share = mergeBitmaps(bitmap, bitmapQrborder);
                            shareQrCodeImage(bitmap_share);
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Log.e("Permission", "denied");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    private void shareQrCodeImage(Bitmap mBitmap) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "PlanetZoom Profile");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);


        OutputStream outstream;
        try {
            outstream = getActivity().getContentResolver().openOutputStream(uri);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Profile"));
    }

    public Bitmap mergeBitmaps(Bitmap overlay, Bitmap bitmap) {

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        Log.e("bitmap_height", "" + height);
        Log.e("bitmap_width", "" + width);

        Bitmap combined = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(bitmap, new Matrix(), null);

        int centreX = (canvasWidth - overlay.getWidth()) / 2;
        int centreY = (canvasHeight - overlay.getHeight()) / 2;
        canvas.drawBitmap(overlay, centreX, centreY, null);

        return combined;
    }

    private BitmapDrawable writeTextOnDrawable(int drawableId, String text) {
        Typeface montserrat_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Bold.OTF");

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTypeface(montserrat_bold);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(18 * getResources().getDisplayMetrics().density);
        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);
        Canvas canvas = new Canvas(bm);
        canvas.drawText(text, 200, 80, paint);
        return new BitmapDrawable(getResources(), bm);
    }

    private void sendReachUsMail(String str_email,String fName){
        String[] recipients = str_email.split(",");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, fName);
        intent.putExtra(Intent.EXTRA_TEXT, "Tried to reach you");

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }
}
