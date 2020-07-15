package com.naver.naverspeech.client;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.coremedia.iso.boxes.Container;
import com.google.android.material.textfield.TextInputEditText;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.naver.naverspeech.client.utils.AudioWriterPCM;
import com.naver.speech.clientapi.SpeechRecognitionResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Conference extends Activity {
   private Cookie cookie =(Cookie) getApplication();
    //퍼미션 위한 변수
    String[] permission = new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};


    int f_count = 0; // 0 : 녹음이 처음시작임을 알림 // 1: 녹음 실행 중임을 알림
    int record_flag = 0; // 0: 녹음버튼 1: 일시정지
    int file_count = 0; // sttrecoding1,sttrecoding2....이런식으로 녹음 임시파일들이 서로 겹쳐지지않게하기위함
    String FilePath = null; // 파일저장 절대경로(내부저장소) + 파일명
    String resultFile; // 녹음 임시파일 병합(머지) 후 결과물
    ArrayList<String> list_stt; //STT 텍스트
    ArrayList<String> outputSttList; // 녹음파일 병합 할 리스트
    ProgressBar p_gradient;
    TextView start_time;
    P_Thread p_thread;


    private static final String TAG = Conference.class.getSimpleName();
    private static final String CLIENT_ID = "8kaszkw9k7";
    // 1. "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    // 2. build.gradle (Module:app)에서 패키지명을 실제 개발자센터 애플리케이션 설정의 '안드로이드 앱 패키지 이름'으로 바꿔 주세요

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;

    private JSONArray jsonArray = new JSONArray();
    private JSONObject jsonObject = new JSONObject();
    private Context context;
    private String timestamp; // 대화 타임스탬프
    private String partitionText; // 대화 끊어서 보내기
    private String mResult; // STT 텍스트
    private String subject;
    private String room;
    private String syear;
    private String sday;
    private String smon;
    private String sdow;
    private String stime;
    private String mid;
    private String note;
    private long tstart_time;
    private long tend_time;
    private String FinalResult;
    private boolean sendtalk =false;
    private boolean join_con_flag =false;
    private String mname;
    private String where;
    private TextView txtResult; // STT 텍스트
    private ImageButton btnRecord; // 녹음
    private ImageButton btnPause; // 일시정지
    private Button btnSave; // 저장
//    private ImageButton btnPlay;  // 재생
    private TextView con_subject;
    private TextView con_room;
    private TextView con_date;
    private ArrayList<Integer> invited_fnums = new ArrayList<Integer>();
    private ImageButton c_gohome;
//    private View view;
//    private Activity root;
    private AudioWriterPCM writer; //open APi

    private Handler handler3; // rec_handler
    private Handler handler2; // pcm_handler

    // Handle speech recognition Messages.
    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                String tv3 = "";
                if(list_stt.size()==0){
                    tv3 ="";
                }else{
                    for(int i=0; i<list_stt.size(); i++){
                        tv3 += list_stt.get(i)+ " ";
                    }
                }
                // Now an user can speak.
                txtResult.setText(tv3);
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                System.out.println("이것은 clientReady");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                System.out.println("이것은 audioRecording");
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
                String tv ="";
                if(list_stt.size() == 0){
                    tv = "";
                }else{
                    for(int i=0; i<list_stt.size(); i++){
                        tv +=list_stt.get(i)+" ";
                    }
                }
                mResult = (String) (msg.obj);
                partitionText = (String) (msg.obj);
                txtResult.setText(tv+mResult);
                System.out.println("이것은 partialResult");
                System.out.println("tv+mResult: "+tv+mResult);
                System.out.println("mResult: " +mResult);
                System.out.println("mResult: " +tv);
                SimpleDateFormat s = new SimpleDateFormat("hhmmss");
                String format = s.format(new Date());
                System.out.println("파티셜리절트 "+format);
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for I/System.out: 말안할때 122545speech.

                SpeechRecognitionResult speechRecognitionResult = (SpeechRecognitionResult) msg.obj;
                List<String> results = speechRecognitionResult.getResults();
                String a = speechRecognitionResult.getResults().get(0);
                StringBuilder strBuf = new StringBuilder();
                for(String result : results) {
                    strBuf.append(result);
                    strBuf.append("\n");
                }
                String tv2 ="";
                mResult = a.toString();
                if(list_stt.size() ==0){
                    txtResult.setText(mResult);
                }else{
                    for(int i=0; i<list_stt.size(); i++){
                        tv2 +=list_stt.get(i)+" ";
                    }
                    txtResult.setText(tv2+mResult);
                    FinalResult = tv2+mResult;
                    System.out.println("이거슨 결과 텍스트: "+FinalResult);
                }
                System.out.println("이것은 finalResult");

//              if(  addMeeting(tstart_time,tend_time,FinalResult)){
//                  Log.d("addMeeting","finalResult");
//        }

                list_stt.add(mResult);
                btnRecord.setEnabled(true);
                btnPause.setEnabled(false);
                btnRecord.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.INVISIBLE);
                p_thread.work = false;
                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                mResult = "Error code : " + msg.obj.toString();
                txtResult.setText(mResult);
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }
                SimpleDateFormat ss = new SimpleDateFormat("hhmmss");
                String formats = ss.format(new Date());
                System.out.println("말안할때 "+formats);
                Instant current = Instant.now();
                tend_time = current.getEpochSecond();
                //partitionText 이거를 서버로 보내고

                addMeeting(tstart_time,tend_time,partitionText);
                Log.d("addMeeting","말안할때"+":"+partitionText);
                partitionText =null;

                //partitionText 초기화
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conference);
        c_gohome= (ImageButton) findViewById(R.id.c_gohome);
        c_gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Conference.this,Conference_pre.class);
                startActivity(intent);
            }
        });
        context=this;
        //권한체크
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        Intent intent = getIntent();
        if (intent.getBooleanExtra("con_pre",false)==true) {
            subject = intent.getStringExtra("subject");
            room = intent.getStringExtra("meeting_room");
            syear = intent.getStringExtra("year");
            smon = intent.getStringExtra("month");
            sday = intent.getStringExtra("day");
            sdow = intent.getStringExtra("dow");
            mid = intent.getStringExtra("mid");
            note = intent.getStringExtra("note");
            invited_fnums = intent.getIntegerArrayListExtra("invited_fnums");
            stime = intent.getStringExtra("time");
            Log.d("컨퍼런스 미드 실행순서","컨퍼런스");
        }

        outputSttList = new ArrayList<String>();
        resultFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/output"+subject+".mp4";

        con_subject = (TextView) findViewById(R.id.conference_save_subject);
        con_room = (TextView) findViewById(R.id.conference_save_room);
        con_date = (TextView) findViewById(R.id.conference_save_date);
        con_subject.setText(subject);
        con_room.setText(room);
        con_date.setText(syear+"-"+smon+"-"+sday);


        Intent join_con_Intent = getIntent();
        join_con_flag=join_con_Intent.getBooleanExtra("flag",false);
        if(join_con_flag==true){
            mname = join_con_Intent.getStringExtra("mname");
            where = join_con_Intent.getStringExtra("where");
            String smon = join_con_Intent.getStringExtra("smon");
            String sday = join_con_Intent.getStringExtra("sday");
            String syear = join_con_Intent.getStringExtra("syear");
            mid = join_con_Intent.getStringExtra("choice_mid");
            con_date.setText(syear+"-"+smon+"-"+sday);
            con_subject.setText(mname);
            con_room.setText(where);

        }


        txtResult = (TextView) findViewById(R.id.txt_result);
        btnRecord = (ImageButton) findViewById(R.id.btn_record);
        btnPause = (ImageButton) findViewById(R.id.btn_pause);
        btnSave = (Button) findViewById(R.id.btn_finish) ;
//        btnPlay = (ImageButton) findViewById(R.id.btn_play);
        p_gradient = (ProgressBar) findViewById(R.id.progress);
        start_time = (TextView) findViewById(R.id.start_time);
        handler2 = new Handler();
        list_stt = new ArrayList<String>();

        btnRecord.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.INVISIBLE);
        btnRecord.setEnabled(true);
        btnPause.setEnabled(false);


        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, handler, CLIENT_ID);

        p_gradient.setProgress(0);
    }

    //권한 체크
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_record: {
                    if (f_count == 0) {
                        Instant current = Instant.now();
                        tstart_time = current.getEpochSecond();
                        System.out.println("시작 시점" + timestamp);
                        System.out.println("서버로보낼시점" + timestamp);
                        handler3 = new Handler();
                        p_thread = new P_Thread();
                        p_thread.start();
                        p_thread.stop = false;
                        p_thread.work = true;
                        Log.e("btnRecord 처음시작", " " + p_thread.getState().toString() + p_thread.work);


                    } else if (f_count == 1) {
                        p_thread.stop = false;
                        p_thread.work = true;
                        Log.e("btnRecord 재시작", " " + p_thread.getState().toString() + "이씨" + p_thread.work);
                        SimpleDateFormat s = new SimpleDateFormat("hhmmss");
                        String format = s.format(new Date());
                        timestamp = s.format(new Date());
                        System.out.println("재시작 " + format);
                        System.out.println("재시작 " + timestamp);
                        Instant current = Instant.now();
                        tstart_time = current.getEpochSecond();
                    }
                    f_count = 1;
                    Log.e("thread_status: ", "" + p_thread.getState() + Integer.toString(f_count) + "이씨" + p_thread.work);

                    if (!naverRecognizer.getSpeechRecognizer().isRunning()) {
                        // Start button is pushed when SpeechRecognizer's state is inactive.
                        // Run SpeechRecongizer by calling recognize().
                        mResult = "";
                        txtResult.setText("Connecting...");
                        naverRecognizer.recognize();
                    } else {
                        Log.e(TAG, "stop and wait Final Result");
                        naverRecognizer.getSpeechRecognizer().stop();
                    }


                btnRecord.setEnabled(false);
                    btnPause.setEnabled(true);

                    btnRecord.setVisibility(View.INVISIBLE);
                    btnPause.setVisibility(View.VISIBLE);

                    AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    am.requestAudioFocus(focusChangeListener,
                            AudioManager.STREAM_MUSIC,
                            AudioManager.AUDIOFOCUS_GAIN);
                break;
            }

            case R.id.btn_pause: {
                Log.e("btnPause 일시정지"," "+p_thread.getState().toString()+"이씨"+p_thread.work);
                p_thread.work =false;
                naverRecognizer.getSpeechRecognizer().stop();
                System.out.println("일시정지 했을 때"+txtResult.getText());
                try {
                    file_count += 1;
                    FilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sttrecording";
                    String nowFile = FilePath + file_count + ".mp4";
                    encodeSingleFile(nowFile);
                    outputSttList.add(nowFile);
                    Log.e("TAG", "" + Integer.toString(outputSttList.size()));

                } catch (Exception e) {
                    Log.e(TAG, "Exception while creating tmp file1", e);
                }
                Instant current = Instant.now();
                tend_time = current.getEpochSecond();

//                addMeeting(tstart_time,tstart_time+1,partitionText);
//                Log.d("addMeeting","일시정지"+":"+partitionText);
                btnRecord.setEnabled(true);
                btnPause.setEnabled(false);
                btnRecord.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.INVISIBLE);
                break;
            }
            case  R.id.btn_finish: {
                try{
                    SimpleDateFormat s = new SimpleDateFormat("hhmmss");
                    String format = s.format(new Date());
                    System.out.println("저장버튼누른 시점"+format);
                    System.out.println("정지 했을 때"+txtResult.getText());
                    startMerge(outputSttList);
                    Intent intent = new Intent(Conference.this, Save_Conference.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("r_path", resultFile);
                    intent.putExtra("content", txtResult.getText());

                    if(join_con_flag==true){
                        intent.putExtra("subject",mname);
                        intent.putExtra("meeting_room",where);
                    }
                    else{
                    intent.putExtra("subject",subject);
                    intent.putExtra("meeting_room",room);
                    intent.putExtra("mid",mid);
                    }
                    intent.putExtra("year",syear);
                    intent.putExtra("month",smon);
                    intent.putExtra("day",sday);
                    intent.putExtra("dow",sdow);

                    f_count =0;
                    p_thread.work = false;
                    p_thread.stop = true;
                    startActivity(intent);
//                    //Toast.makeText(getApplicationContext(), "저장버튼", //Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Log.e(TAG, "Exception while creating tmp file", e);
                } finally {
                    onBtnReset();
                }
                addMeeting(tstart_time,tend_time,partitionText);
                Log.d("addMeeting","끝"+":"+partitionText);
                break;
            }



        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // NOTE : initialize() must be called on start time.
        naverRecognizer.getSpeechRecognizer().initialize();
        System.out.println("onStart다");
    }

    @Override
    public void onResume() {
        super.onResume();
        mResult = "";
        txtResult.setText("");
        System.out.println("onResume이다");
    }

    @Override
    public void onStop() {
        super.onStop();
        // NOTE : release() must be called on stop time.
        naverRecognizer.getSpeechRecognizer().release();
        System.out.println("onStop이다");
    }

    // Declare handler for handling SpeechRecognizer thread's Messages.
    static class RecognitionHandler extends Handler {
        private final WeakReference<Conference> mActivity;

        RecognitionHandler(Conference activity) {
            mActivity = new WeakReference<Conference>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Conference activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }


    private AudioManager.OnAudioFocusChangeListener focusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {
                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                            // Lower the volume while ducking.
                            break;
                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):

                            break;
                        case (AudioManager.AUDIOFOCUS_GAIN):
                            break;
                        default:
                            break;
                    }
                }
            };

    private void encodeSingleFile(final String outputPath) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(encodeTask(1, outputPath));
    }

    private Runnable encodeTask(final int numFiles, final String outputPath) { // pcm 파일 인코딩
        return new Runnable() {
            @Override
            public void run() {
                try {
                    final PCMEncoder pcmEncoder = new PCMEncoder(48000, 16000, 1);
                    pcmEncoder.setOutputPath(outputPath);
                    pcmEncoder.prepare();
                    File directory = new File("storage/emulated/0/NaverSpeechTest/Test.pcm");
                    for (int i = 0; i < numFiles; i++) {
                        Log.d(TAG, "Encoding: " + i);
                        InputStream inputStream = new FileInputStream(directory);
                        inputStream.skip(44);
                        pcmEncoder.encode(inputStream, 16000);
                    }
                    pcmEncoder.stop();
                    handler2.post(new Runnable() {
                        @Override
                        public void run() {
//                            //Toast.makeText(getApplicationContext(), "Encoded file to: " + outputPath, //Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (IOException e) {
                    Log.e(TAG, "Cannot create FileInputStream", e);
                }
            }
        };
    }

    private class P_Thread extends Thread {
        private int progressStatus = 0;
        public boolean stop = false;
        public boolean work = true;

        public void run() {
            while (progressStatus < 60 && !Thread.currentThread().isInterrupted()) {
                if (work) {
                    try {
                        progressStatus++;
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Update the progress bar
                    handler3.post(new Runnable() {
                        public void run() {
                            p_gradient.setProgress(progressStatus);
                            start_time.setText("00:" + String.format("%02d", progressStatus));
                        }
                    });
                } else {
                    Log.e("End STT", " " + p_thread.getState().toString()+p_thread.work);
                    if (Thread.currentThread().getState().equals(State.RUNNABLE)) {
                        try {
                            Thread.sleep(800);
                        } catch (Exception e) {
                        }
                    }
                    if (stop) {
                        Log.e("hello", " " + Thread.currentThread().getState());
                        progressStatus = 0;
                        handler3.post(new Runnable() {
                            public void run() {
                                p_gradient.setProgress(progressStatus);
                                start_time.setText("00:" + String.format("%02d", progressStatus));
                            }
                        });
                        break;
                    }
                }
            }
            Log.e("끝", "끝");
            progressStatus = 0;
            stop = false;
            work = true;
            Instant current = Instant.now();
            tend_time = current.getEpochSecond();
            addMeeting(tstart_time,tend_time,partitionText);

        }
    }

    public void startMerge(ArrayList<String> outputFileList)throws IOException // mp4parser library
    {
        Movie[] inMovies = new Movie[outputFileList.size()];
        try
        {
            Log.e("file_size", ""+ Integer.toString(outputFileList.size()));
            for(int a = 0; a < outputFileList.size(); a++)
            {
                inMovies[a] = MovieCreator.build(outputFileList.get(a));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        List<Track> audioTracks = new LinkedList<Track>();
        for (Movie m : inMovies)
        {
            for (Track t : m.getTracks())
            {
                if (t.getHandler().equals("soun"))
                {
                    audioTracks.add(t);
                }
            }
        }

        Movie output = new Movie();
        if (audioTracks.size() > 0)
        {
            try
            {
                output.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        Container out = new DefaultMp4Builder().build(output);
        FileChannel fc = null;
        try
        {
            fc = new FileOutputStream(new File(resultFile)).getChannel();
            Log.e("output",resultFile);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        try
        {
            out.writeContainer(fc);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            fc.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void onBtnReset(){
        outputSttList.clear();
        file_count =0;
        txtResult.setText("");
        list_stt.clear();
        p_gradient.setProgress(0);
    }
//

    private boolean addMeeting(long t_start_time, long t_end_time, String talk){

        Cookie cookie = (Cookie) getApplication();
        HashMap<String, Object> params = new HashMap<String,Object>();

            ArrayList<String> talks = new ArrayList<String>();
            talks.add(String.valueOf(cookie.getCookie()));
            talks.add(String.valueOf(t_start_time));
            talks.add(String.valueOf(t_end_time));
            talks.add(talk);
//        ArrayList<String> mid = new ArrayList<String>();
//        mid.add("5b9f47639cd6bf66224b0219");

        params.put("mid",cookie.getMid());
        params.put("talk", talks);
        Log.d("회의 보내졌나: "," "+talks + " "+talks.size());
        final RequestQueue queue = Volley.newRequestQueue(Conference.this);
        final String url = "https://kz2hltyjb2.execute-api.ap-northeast-2.amazonaws.com/test/meet/talk";

        // 일단 시작
        queue.start();

        // 요청 만들기
        // 파라미터: 메소드 방식, 주소, 데이터, 응답 이벤트, 에러 이벤트

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    // 제대로 된 응답이 들어왔을 시
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.has("message")) { // 메세지 가지고 있음 에러난거
                                // 오류를 사용자에게 알리도록 고칠 것.
                                // message에 있는 내용을 알릴 것.
                                Log.e("회의 실패", response.getString("message"));
//                                //Toast.makeText(getApplicationContext(),response.getString("message"), //Toast.LENGTH_LONG).show();
                            } else { // 메세지 없으면 성공한거
                                Log.i("response", response.toString());
                                Cookie cookie = (Cookie) getApplication();
                                cookie.setCookie(Integer.parseInt(response.getString("unum")));

                                Log.i("cookie", String.valueOf(cookie.getCookie()));

//                                //Toast.makeText(getApplicationContext(),"회의 성공", //Toast.LENGTH_LONG).show();

                            }
                        } catch(JSONException e){
                            // 오류를 사용자에게 알리도록 고칠 것.
                            // 알 수 없는 원인.
                            // e.printStackTrace();
//                            //Toast.makeText(getApplicationContext(),"알 수 없는 이유로 실패", //Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // 응답코드 200 이외의 코드, 즉 서버에서 다른 문제로 에러가 났을 시
                    public void onErrorResponse(VolleyError error) {
                        // 오류를 사용자에게 알리도록 고칠 것.
                        Log.e("회의 실패", "인터넷 연결을 확인해주세요.");
//                        //Toast.makeText(getApplicationContext(),"인터넷 연결을 확인해주세요", //Toast.LENGTH_LONG).show();
                    }
                });
        // 요청 큐에 추가하기
        queue.add(jsonObjectRequest);

    return true;

    }



}
