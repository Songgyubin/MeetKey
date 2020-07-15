package com.naver.naverspeech.client;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static androidx.core.content.FileProvider.getUriForFile;

public class Camera_popup_signup extends Activity {
    private final int ALBUM_CODE = 1112;
    private final int CAMERA_CODE = 1111;
    private Uri photoUri;
    private String currentPhotoPath; //실제 사진 파일 경로
    private String mImageCaptureName; // 이미지 이름
    private int send_exifOrientation;
    private int send_exifDegree;

    private Bitmap send_bitmap;
    private ImageView imageView;

    private TextView save_filename;

    private Button getcamera;
    private Button getalbum;
    private File file;

    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonS3 s3;
    TransferUtility transferUtility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.camera_popup_signup);
        imageView = (ImageView) findViewById(R.id.sign_save_image);
        getcamera = (Button) findViewById(R.id.sign_getcamera);
        getalbum = (Button) findViewById(R.id.sign_getalbum);
        save_filename =(TextView) findViewById(R.id.sign_save_filename);
        Log.d("패키지이름",getPackageName());
        // Amazon Cognito 인증 공급자를 초기화합니다
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-2:79f69e5c-b233-45a0-9440-ab01bb2bb592", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );

        s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");

        transferUtility = new TransferUtility(s3, getApplicationContext());

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_getcamera:
                selectPhoto();
                break;

            case R.id.sign_getalbum:
                selectAlbum();
                break;

            case R.id.sign_camera_upload:

                TransferObserver observer = transferUtility.upload(

                        "haniumtest",     /* 업로드 할 버킷 이름 */
                        file.getName(),    /* 버킷에 저장할 파일의 이름 */
                        file        /* 버킷에 저장할 파일  */

                );

                Intent intent = new Intent(Camera_popup_signup.this,Sign_Up.class);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(),"업로드완료", //Toast.LENGTH_LONG).show();
                break;
        }


    }

    public void selectPhoto() {
        String state = Environment.getExternalStorageState();


        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();

                } catch (IOException ex) {

                }
                if (photoFile != null) {
                    photoUri = getUriForFile(this, getPackageName(), photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    file = photoFile;
                    startActivityForResult(intent, CAMERA_CODE);

                }
            }

        }
    }

    public File createImageFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory() + "/path/");
        if (!dir.exists()) {
            dir.mkdirs();
        }


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageCaptureName = timeStamp + ".png";
        save_filename.setText(mImageCaptureName);


        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/path/"
                + mImageCaptureName);
        currentPhotoPath = storageDir.getAbsolutePath();
        save_filename.setText(mImageCaptureName);
        return storageDir;

    }

    public void getPictureForPhoto() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(currentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation;
        int exifDegree;

        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        } else {
            exifDegree = 0;
        }
        imageView.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
    }

    public void selectAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, ALBUM_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case ALBUM_CODE:
                    sendPicture(data.getData()); //갤러리에서 가져오기
                    file = new File(getRealPathFromURI(data.getData()));

                    break;
                case CAMERA_CODE:
                    getPictureForPhoto(); //카메라에서 가져오기

                    break;

                default:
                    break;
            }

        }
    }

    public void sendPicture(Uri imgUri) {

        String imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            send_exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            send_exifDegree = exifOrientationToDegrees(send_exifOrientation);

            send_bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
            imageView.setImageBitmap(rotate(send_bitmap, send_exifDegree));//이미지 뷰에 비트맵 넣기
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = managedQuery(imgUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            String imgPath = cursor.getString(column_index);
            String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

            save_filename.setText(imgName);
        }catch (Exception e){
            Log.e("사진경로",imagePath);
        }
    }

    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public Bitmap rotate(Bitmap src, float degree) {

// Matrix 객체 생성
        Matrix matrix = new Matrix();
// 회전 각도 셋팅
        matrix.postRotate(degree);
// 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }
    private void sign_Image_send(){

        // 데이터 생성
        HashMap<String, String> params = new HashMap<String, String>();
//        rotate(send_bitmap, send_exifDegree) 이미지 보낼꺼 주소소


        // volley 패키지를 이용하여 통신
        final RequestQueue queue = Volley.newRequestQueue(Camera_popup_signup.this);
        final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/upload2";

        // 일단 스타트
        queue.start();

        // 요청 만들기
        // 파라미터: 메소드 방식, 주소, 데이터, 응답 이벤트, 에러 이벤트
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    // 제대로 된 응답이 들어왔을 시
                    public void onResponse(JSONObject response) {
                        try {
                            // message를 가지고 있으면 에러난 거
                            if (response.has("message")) {
                                // 오류를 사용자에게 알리도록 고칠 것.
                                // message에 있는 내용을 알릴 것.
                                Log.e("회원가입 실패", response.getString("message"));
                                //Toast.makeText(getApplicationContext(),response.getString("message"),//Toast.LENGTH_LONG).show();
                            } else { // message 안가지고 있으면 user 정보 통째로 건네줌
                                // 성공하면 어디로 보낼까
                                Log.v("회원가입 성공", "success");
                                //Toast.makeText(getApplicationContext(),"회원가입성공",//Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // 오류를 사용자에게 알리도록 고칠 것.
                            // 알 수 없는 원인.
                            // e.printStackTrace();
                            Log.e("error", e.toString());
                            //Toast.makeText(getApplicationContext(),"알수없는 이유로 실패",//Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // 응답코드 200 이외의 코드, 즉 서버에서 다른 문제로 에러가 났을 시
                    public void onErrorResponse(VolleyError error) {
                        // 오류를 사용자에게 알리도록 고칠 것.
                        Log.e("회원가입 실패", "알 수 없는 이유로 실패");
                        //Toast.makeText(getApplicationContext(),"알수없는 이유로 실패",//Toast.LENGTH_LONG).show();
                    }
                });

        // 만들어논 요청을 큐에 추가함
        queue.add(jsonObjectRequest);
    }

    }

