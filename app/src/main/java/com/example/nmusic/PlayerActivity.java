package com.example.nmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class PlayerActivity extends AppCompatActivity {
    Button playbtn,btnnxt,btnprev,btnff,btnfr,suf,skipth;
    TextView txtsname,txtstart,txtsstop;
    SeekBar seekmusic;
    ImageView imageView;

    String sname;
    public static final String EXTRA_NAME="song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateseekbar;
    Boolean shuffle=false,skipthh=false;
    Random rand=new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

//        Setup the Action Bar
        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        Initialize all variable
        playbtn=findViewById(R.id.playbtn);
        btnnxt=findViewById(R.id.btnnext);
        btnprev=findViewById(R.id.btnprev);
        btnff=findViewById(R.id.btnff);
        btnfr=findViewById(R.id.btnfr);
        imageView=findViewById(R.id.imageview);
        suf=findViewById(R.id.shuffle);
        skipth=findViewById(R.id.skipth);
        txtsname=findViewById(R.id.txtsn);
        txtstart=findViewById(R.id.txtsstart);
        txtsstop=findViewById(R.id.txtsstop);
        seekmusic=findViewById(R.id.seekbar);

//        Initializing intent for getting the song list,current song name etc
        Intent i=getIntent();
        Bundle bundle=i.getExtras();

        mySongs=(ArrayList)bundle.getParcelableArrayList("songs");
        String songName=i.getStringExtra("songName");
        int duration=bundle.getInt("duration",0);
        position=bundle.getInt("pos",0);
        txtsname.setSelected(true);
        sname=mySongs.get(position).getName();
        txtsname.setText(sname);

//        Initializing the Uri and Mediaplayer fo current song
        Uri uri=Uri.parse(mySongs.get(position).toString());
        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.seekTo(duration);
        mediaPlayer.start();
        startAllMethod();
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
                    playbtn.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                }
                else{
                    playbtn.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }

            }
        });
        btnnxt.setOnClickListener(new View.OnClickListener() {
//            This is for next button action
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(shuffle){
//                    if shuffle is on the next song will randomly choose from the list of song
                    position =rand.nextInt(mySongs.size());
                }
                else {
//                    if shuffle is not on than only next song will play when clicked on next button
                    position = (position + 1) % mySongs.size();
                }
                Uri u=Uri.parse(mySongs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                if(skipthh)
                {
                    if(mediaPlayer.getDuration()<20000)
                    {
                        btnnxt.performClick();
                    }
                    else{
                        sname=mySongs.get(position).getName();
                        txtsname.setText(sname);
                        mediaPlayer.start();
                        playbtn.setBackgroundResource(R.drawable.ic_pause);
                        startAllMethod();
                    }
                }
                else{
                    sname=mySongs.get(position).getName();
                    txtsname.setText(sname);
                    mediaPlayer.start();
                    playbtn.setBackgroundResource(R.drawable.ic_pause);
                    startAllMethod();}
            }
        });
        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position=((position-1)<0)?(mySongs.size()-1):(position-1);
                Uri u=Uri.parse(mySongs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                if(skipthh)
                {
                    if(mediaPlayer.getDuration()<20000)
                    {
                        btnprev.performClick();
                    }
                    else{
                        sname=mySongs.get(position).getName();
                        txtsname.setText(sname);
                        mediaPlayer.start();
                        playbtn.setBackgroundResource(R.drawable.ic_pause);
                        startAllMethod();
                    }
                }
                else{
                    sname=mySongs.get(position).getName();
                    txtsname.setText(sname);
                    mediaPlayer.start();
                    playbtn.setBackgroundResource(R.drawable.ic_pause);
                    startAllMethod();}
            }


        });

        btnff.setOnClickListener(new View.OnClickListener() {
//            this will simply fast forward the song by 10 sec
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
                }
            }
        });

        btnfr.setOnClickListener(new View.OnClickListener() {
//            this will simply do fast backward the song by 10 sec
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
                }
            }
        });
    }
    public void startAnimation(View view){
        ObjectAnimator animator=ObjectAnimator.ofFloat(imageView,"rotation",0f,360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }
    public String creattime(int duration)
    {
//        This method is used for changing the time format from milisecond to minute and second
        String time="";
        int min=duration/1000/60;
        int sec=duration/1000%60;
        time+=min+":";
        if(sec<10)
        {
            time+=0;
        }
        time+=sec;
        return time;
    }
    public void onComplete(){
//        This method recursivily used when the song is completed the next song will automatic started
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnnxt.performClick();
                onComplete();
            }
        });
    }
    public void updateseek(){

//        This will update seekbar after every 600 ms
        updateseekbar=new Thread()
        {
            @Override
            public void run() {
                int totalDuration=mediaPlayer.getDuration();
                int currentPosition=0;
                while (currentPosition<totalDuration)
                {
                    try {
                        sleep(600);
                        currentPosition=mediaPlayer.getCurrentPosition();
                        seekmusic.setProgress(currentPosition);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
//        setting the maximum of seekbar
        seekmusic.setMax(mediaPlayer.getDuration());
        updateseekbar.start();
//        This is change the background of seekbar
        seekmusic.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        seekmusic.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);

//        This action invoked when anyone touches the seekbar
        seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });
    }
    public void settime(){

//        This method is used for setting and updating the remain time of song and current time of song
        final Handler handler=new Handler();
        final int delay=1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String remaintime=creattime(mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition());
                txtsstop.setText(remaintime);
                String currentime=creattime(mediaPlayer.getCurrentPosition());
                txtstart.setText(currentime);
                handler.postDelayed(this,delay);
            }
        },delay);
    }
    public void startAllMethod(){
//        This method simply call all method which is used whenever a new song is playing
        updateseek();
        startAnimation(imageView);
        settime();
        onComplete();
    }
    public void shuffle(View view){
//        This method simply disable and enable the shuffle function of music
        if(shuffle)
        {
            shuffle=false;
            suf.setBackgroundResource(R.drawable.repeat);
            Toast.makeText(this, "Shuffle Off", Toast.LENGTH_SHORT).show();
        }
        else{
            shuffle=true;
            suf.setBackgroundResource(R.drawable.shuffle);
            Toast.makeText(this, "Shuffle On", Toast.LENGTH_SHORT).show();
        }

    }
    public void skipth(View view){
//        This method is used for skip song less than 20 sec
        if(skipthh){
            skipthh=false;
            Toast.makeText(this, "Now Play 20 second low dur", Toast.LENGTH_SHORT).show();
        }
        else{
            skipthh=true;
            Toast.makeText(this, "less than 20 sec dur not play", Toast.LENGTH_SHORT).show();
        }
    }
}