package sanq.ractrl.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import sanq.ractrl.core.Const;
import sanq.ractrl.core.Prefs;
import sanq.ractrl.core.Sender;
import sanq.ractrl.core.Const.Direct;
import sanq.ractrl.core.WifiControl;

import java.util.*;

public class Main extends Activity implements View.OnClickListener, ViewSwitcher.ViewFactory ,View.OnLongClickListener,View.OnTouchListener {
    private static Context context;




    private ImageButton cmdPrev;
    private ImageButton cmdNext;
    private ImageButton imgOnOff;
    private ImageButton imgPlus;
    private ImageButton imgMinus;
    private ImageButton imgServerShutdown;
    private ImageButton imgSystemShutdown;
    private ImageButton imgSystemReboot;
    private ImageButton imgSystemCancel;


    private View viewCategory;
    private TextSwitcher txtCategory;

    private int idxCatLayuot = -1 ;
    //todo: if posible put into resourse file
    private List<CategoryLayout> layuots = new ArrayList<CategoryLayout>();

     /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = getApplicationContext();

        //Wifi auto start;
        Control.checkAutoStartWifi();


        // buttons
        cmdPrev = (ImageButton) findViewById(R.id.cmdPrev);
        cmdPrev.setOnClickListener(this);
        cmdNext = (ImageButton) findViewById(R.id.cmdNext);
        cmdNext.setOnClickListener(this);



        //  text switcher
        txtCategory = (TextSwitcher) findViewById(R.id.txtCategory);
        txtCategory.setFactory(this);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.fade_out);
        txtCategory.setInAnimation(in);
        txtCategory.setOutAnimation(out);


        layuots.add(new CategoryLayout(R.layout.volume, "Volume"));
        layuots.add(new CategoryLayout(R.layout.system, "System"));
        layuots.add(new CategoryLayout(R.layout.server, "Server"));


        scrollCategory(Direct.LEFT);
        }


    public static Context getAppContext() {
        return Main.context;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.quit:
                finish();
                return true;
            case R.id.settings:
                startActivity(new Intent(this, Prefs.class));
               return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId() ) {
            //scroll
            case R.id.cmdPrev   :
             scrollCategory(Direct.LEFT);
            break;
            case R.id.cmdNext   :
                scrollCategory(Direct.RIGHT);
            break;

           //commands
            case R.id.imgOnOff:
                String tag = (String) imgOnOff.getTag();
                if (tag.equals("on")) {
                    if (Sender.SendMessage(Const.TypeCommand.VOLUME_MIN.getValue(),true )) {
                        imgOnOff.setTag("off");
                        imgOnOff.setImageResource(R.drawable.volumeoff);
                    }
                } else {
                    if (Sender.SendMessage( Const.TypeCommand.VOLUME_PREV.getValue(),true)) {
                        imgOnOff.setTag("on");
                        imgOnOff.setImageResource(R.drawable.volumeon);
                    }
                }
                break;
            case R.id.imgMinus:
                Sender.SendMessage(Const.TypeCommand.VOLUME_DOWN.getValue() );
            break;
            case R.id.imgPlus:
                Sender.SendMessage(Const.TypeCommand.VOLUME_UP.getValue());
            break;

            case R.id.imgServerShutdown:
                Sender.SendMessage(Const.TypeCommand.SERVER_SHUTDOWN.getValue() );
            break;
            case R.id.imgSystemShutdown:
                Sender.SendMessage(Const.TypeCommand.SYSTEM_SHUTDOWN.getValue());
            break;
            case R.id.imgSystemReboot:
                Sender.SendMessage(Const.TypeCommand.SYSTEM_REBOOT.getValue());
            break;
            case R.id.imgSystemCancel:
                Sender.SendMessage(Const.TypeCommand.SYSTEM_CANCEL.getValue());
            break;

        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.imgPlus  : Control.longVolumeStart(Const.TypeCommand.VOLUME_UP)  ; break;
            case R.id.imgMinus : Control.longVolumeStart(Const.TypeCommand.VOLUME_DOWN); break;
        }
        return false;
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if  ((v.getId() == R.id.imgMinus) || (v.getId() == R.id.imgPlus)) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Control.longVolumeStop();
            }
        }
        return false;
    }

    private void scrollCategory(Direct direct) {
        if (direct == Direct.LEFT) {
            idxCatLayuot++;
        } else {
            idxCatLayuot--;
        }

        if (idxCatLayuot >= layuots.size()) {
            idxCatLayuot = 0;
        } else if (idxCatLayuot < 0) {
            idxCatLayuot = layuots.size() - 1;
        }

        changerLayout(layuots.get(idxCatLayuot).getId());
        txtCategory.setText(layuots.get(idxCatLayuot).getName());
    }



    private void changerLayout(int viewId ){
        ViewGroup searchViewHolder = (ViewGroup)findViewById(R.id.layCategory);
        if (viewCategory != null) {
            searchViewHolder.removeView(viewCategory);
        }
        viewCategory = getLayoutInflater().inflate(viewId, null);
        searchViewHolder.addView(viewCategory);
        switch (viewId ){
            case R.layout.volume:
                imgPlus = (ImageButton) findViewById(R.id.imgPlus);
                imgPlus.setOnClickListener(this);
                imgPlus.setOnLongClickListener(this);
                imgPlus.setOnTouchListener(this);

                imgMinus = (ImageButton) findViewById(R.id.imgMinus);
                imgMinus.setOnClickListener(this);
                imgMinus.setOnLongClickListener(this);
                imgMinus.setOnTouchListener(this);

                imgOnOff = (ImageButton) findViewById(R.id.imgOnOff);
                imgOnOff.setOnClickListener(this);
                break;
            case R.layout.system:
                imgSystemShutdown = (ImageButton) findViewById(R.id.imgSystemShutdown);
                imgSystemShutdown.setOnClickListener(this);
                imgSystemReboot = (ImageButton) findViewById(R.id.imgSystemReboot);
                imgSystemReboot.setOnClickListener(this);
                imgSystemCancel= (ImageButton) findViewById(R.id.imgSystemCancel);
                imgSystemCancel.setOnClickListener(this);
                break;
            case R.layout.server:
                imgServerShutdown = (ImageButton) findViewById(R.id.imgServerShutdown);
                imgServerShutdown.setOnClickListener(this);
                break;
        }

    }



    @Override
    public View makeView() {
        TextView t = new TextView(this);
        t.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
        t.setTextSize(35F);
        t.setTextColor(Color.GRAY);
        return t;
    }


    @Override
    protected void  onDestroy() {
       // Log.d(Const.TAG, "Exiting...");
        Control.checkAutoCloseWifi();
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    class CategoryLayout{
        private  int id;
        private String name;
        public CategoryLayout(int id, String name) {
            this.id = id;
            this.name = name;
        }
        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
    }




}
