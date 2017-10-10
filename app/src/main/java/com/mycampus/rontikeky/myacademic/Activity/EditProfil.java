package com.mycampus.rontikeky.myacademic.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mycampus.rontikeky.myacademic.Config.FontHandler;
import com.mycampus.rontikeky.myacademic.Config.PrefHandler;
import com.mycampus.rontikeky.myacademic.Fragment.RegisteredEventFragment;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Request.ChangeProfileDisplayRequest;
import com.mycampus.rontikeky.myacademic.Request.EditProfileRequest;
import com.mycampus.rontikeky.myacademic.Response.ChangeDisplayProfileResponse;
import com.mycampus.rontikeky.myacademic.Response.EditProfileResponse;
import com.mycampus.rontikeky.myacademic.Response.ProfileEditMahasiswaResponse;
import com.mycampus.rontikeky.myacademic.Response.ProfileResponse;
import com.mycampus.rontikeky.myacademic.Response.ProfileResponseEdit;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGenerator;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGeneratorAuth;
import com.google.gson.Gson;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGeneratorAuth2;
import com.mycampus.rontikeky.myacademic.Util.CircleTransformation;
import com.mycampus.rontikeky.myacademic.Util.ImageFilePath;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfil extends AppCompatActivity {
    EditText username, alamat, lahir, nama, email, password, txtTelp,nim;
    TextView result,viewName;
    Button simpan;
    ImageView ivProfile,ivChange;
    NestedScrollView scrollView;

    String token;
    String extrasSlug;

    PrefHandler prefHandler;
    FontHandler fontHandler;

    int year_x,month_x,day_x;
    static final int DIALOG_ID = 0;

    ProgressDialog pDialog;
    boolean isConnected,valid;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        username = (EditText)findViewById(R.id.txtUsername);
        alamat = (EditText)findViewById(R.id.txtAlamat);
        lahir = (EditText)findViewById(R.id.txtDate);
        nama = (EditText)findViewById(R.id.txtName);
        email = (EditText)findViewById(R.id.txtEmail);
        password = (EditText)findViewById(R.id.txtPassword);
        txtTelp = (EditText)findViewById(R.id.txtTelp);
        result = (TextView)findViewById(R.id.result);
        nim = (EditText)findViewById(R.id.txtNIM);
        viewName = (TextView)findViewById(R.id.viewName);
        ivChange = (ImageView)findViewById(R.id.camera);
        ivProfile = (ImageView)findViewById(R.id.ivProfile);
        simpan = (Button)findViewById(R.id.btnSimpan);

        scrollView = (NestedScrollView)findViewById(R.id.profileScrollView);

        username.setFocusable(false);
        username.setClickable(false);
        email.setFocusable(false);
        email.setClickable(false);

        prefHandler = new PrefHandler(this);
        fontHandler = new FontHandler(this);
        Typeface custom_font = fontHandler.getFont();
        Typeface custom_font_bold = fontHandler.getFontBold();
        viewName.setTypeface(custom_font);
        username.setTypeface(custom_font);
        email.setTypeface(custom_font);
        nama.setTypeface(custom_font);
        txtTelp.setTypeface(custom_font);
        alamat.setTypeface(custom_font);
        lahir.setTypeface(custom_font);
        result.setTypeface(custom_font);
        simpan.setTypeface(custom_font_bold);

        token = prefHandler.getTOKEN_KEY();

        pDialog = new ProgressDialog(EditProfil.this);

        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        getProfile();

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fullscreen = new Intent(EditProfil.this,FullScreenImageActivity.class);
                fullscreen.putExtra("url",prefHandler.getIMAGE_PROFILE_KEY());
                startActivity(fullscreen);
            }
        });

        ivChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 0);
            }
        });

        final Calendar calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);

        showDialogOnClick();

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfil.this, ChangePassword.class);
                startActivity(intent);
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("");
                result.setVisibility(View.GONE);
                result.setBackgroundColor(Color.parseColor("#00ff00"));
                if (validation()){
                    doEditProfile();
                }
            }
        });


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
        }

//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
    }

    public void showDialogOnClick(){
        lahir = (EditText)findViewById(R.id.txtDate);

        lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID){
            return new DatePickerDialog(this, dpickerdialog, 1998,month_x,day_x);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerdialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month+1;
            day_x = dayOfMonth;
            lahir.setText(year_x+"-"+month_x+"-"+day_x);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case 0:
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        String pathImage = ImageFilePath.getPath(this,data.getData());
                        Log.d("xxx",pathImage);
                        Log.d("xxxxx",getStringImage(bitmap));

                        compressImage(pathImage);

                        Bitmap bm = BitmapFactory.decodeFile(compressImage(pathImage));

                        Log.d("RESPONSE",compressImage(pathImage));
                        sendImageToServer(getStringImage(bm));
                        ivProfile.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
        }
    }

    private void sendImageToServer(String base64Image){
        AcademicClient client = ServiceGeneratorAuth.createService(AcademicClient.class,token);
        ChangeProfileDisplayRequest changeProfileDisplayRequest = new ChangeProfileDisplayRequest(base64Image);
        Call<ChangeDisplayProfileResponse> call = client.doChangeFoto(changeProfileDisplayRequest);

        call.enqueue(new Callback<ChangeDisplayProfileResponse>() {
            @Override
            public void onResponse(Call<ChangeDisplayProfileResponse> call, Response<ChangeDisplayProfileResponse> response) {

            }

            @Override
            public void onFailure(Call<ChangeDisplayProfileResponse> call, Throwable t) {

            }
        });

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //scaleDown(bmp,200,true);
        //Bitmap bitmap = Bitmap.createScaledBitmap(bmp, 200, 200, false);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("Image", "getStringImage: "+encodedImage.trim());
        return encodedImage;
    }

    /**********************************************IMAGE COMPRESSION****************************************************/
    public String compressImage(String imageUri) {

        //String filePath = getRealPathFromURI(imageUri);
        String filePath = imageUri;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {               imgRatio = maxHeight / actualHeight;                actualWidth = (int) (imgRatio * actualWidth);               actualHeight = (int) maxHeight;             } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "BLuCampus/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }       final float totalPixels = width * height;       final float totalReqPixelsCap = reqWidth * reqHeight * 2;       while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
    /**********************************************IMAGE COMPRESSION****************************************************/

    @Override
    protected void onResume() {
        super.onResume();
        getProfile();
    }

    private void getProfile(){
        AcademicClient client = ServiceGeneratorAuth.createService(AcademicClient.class,token);
        Call<ProfileResponse> call = client.getProfile();
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                try{
                    pDialog.dismiss();
                    Log.d("RESPONSE : ", new Gson().toJson(response.body()));
                    username.setText(response.body().getUsername());
                    alamat.setText(response.body().getAlamat());
                    nama.setText(response.body().getNama());
                    lahir.setText(response.body().getTanggalLahir());
                    email.setText(response.body().getEmail());
                    txtTelp.setText(response.body().getTelepon());
                    viewName.setText(response.body().getNama());

                    prefHandler.setIMAGE_PROFILE_KEY(response.body().getFoto());

                    deleteCache(EditProfil.this);
                    Log.d("FOTO",response.body().getFoto());
                    Glide.with(EditProfil.this).load(response.body().getFoto())
                            .centerCrop().error(R.drawable.nopicture).placeholder(R.drawable.nopicture).transform(new CircleTransformation(EditProfil.this))
                            .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ivProfile);

                    if (response.body().getNi().isEmpty()){
                        nim.setVisibility(View.GONE);
                    }else{
                        nim.setVisibility(View.VISIBLE);
                        nim.setEnabled(false);
                        nim.setText(response.body().getNi());
                    }

//                    String jsonObject = new Gson().toJson(response.body());
//
//
//                    Gson gson = new Gson();
//
//                    ProfileResponseEdit mhs = gson.fromJson(jsonObject, ProfileResponseEdit.class);
//
//
//                    if (response.body().mahasiswa != null){
//                        nim = (EditText)findViewById(R.id.txtNIM);
//                        nim.setVisibility(View.VISIBLE);
//                        nim.setText(mhs.mahasiswa.nim);
//                        nim.setEnabled(false);
//                    }else{
//                        nim = (EditText)findViewById(R.id.txtNIM);
//                        nim.setVisibility(View.GONE);
//                    }
                }catch (Exception e){
                    Log.d("CATCH : ",e.toString());
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.d("Failure", t.toString());
            }
        });
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    private void doEditProfile(){
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        AcademicClient client = ServiceGeneratorAuth.createService(AcademicClient.class, token);

        EditProfileRequest request = new EditProfileRequest(nama.getText().toString(),lahir.getText().toString(),alamat.getText().toString(),txtTelp.getText().toString());

        Call<EditProfileResponse> call = client.doEditProfile(request);

        call.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                pDialog.dismiss();
                try{
                    if (response.isSuccessful()){
                        Log.d("DEBUG PROFILE 1",response.body().getMessage());
                        result.setVisibility(View.VISIBLE);
                        result.setBackgroundColor(Color.parseColor("#00ff00"));
                        result.setText(response.body().getMessage());

                        prefHandler.setNAME_KEY(nama.getText().toString());
                    }else{
                        Log.d("DEBUG PROFILE 2",response.body().getMessage());
                    }
                    scrollView.smoothScrollTo(0, 0);
                }catch (Exception e){
                    Log.d("DEBUG PROFILE 4",e.toString());
                    scrollView.smoothScrollTo(0, 0);
                }

            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.d("DEBUG PROFILE 3",t.toString());
                result.setVisibility(View.VISIBLE);
                if (!checkConnection()) {
                    Toast.makeText(getApplicationContext(), "Internet bermasalah!", Toast.LENGTH_LONG).show();
                } else {
                    result.setText("Semua Field Tidak Boleh Kosong");
                }
                scrollView.smoothScrollTo(0, 0);
            }
        });
    }

//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new RegisteredEventFragment(), "Event Terdaftar");
//        viewPager.setAdapter(adapter);
//    }
//
//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private boolean validation(){

        valid = true;

        if (nama.getText().toString().trim().isEmpty() || lahir.getText().toString().trim().isEmpty() || alamat.getText().toString().trim().isEmpty()){
            Log.d("VALIDITY 1", "TIDAK BOLEH ADA FIELD KOSONG");
            result.setVisibility(View.VISIBLE);
            result.setText("Tidak ada Boleh Ada Field Yang Kosong");
            valid = false;
        }

        return valid;
    }

    private boolean checkConnection(){
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnected();
        return isConnected;
    }
}
