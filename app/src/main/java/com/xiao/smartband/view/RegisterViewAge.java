package com.xiao.smartband.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xiao.smartband.MyApplication;
import com.xiao.smartband.R;

public class RegisterViewAge extends Dialog implements OnClickListener{
	private Button btnsure,btncancel;
	private RegisterViewAge view;
	private static PTEventHander ptHander;
	private int value;
	public RegisterViewAge(Context context) {
		super(context);
	}
	public RegisterViewAge(Context context, int theme) {
		super(context, theme);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bracelet_regi_view);
		initWheel();
	}

    // Wheel scrolled flag
    private boolean wheelScrolled = false;
    
    // Wheel scrolled listener
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
        }
        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled = false;
        }
    };

    // Wheel changed listener
    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
        	value = newValue;
            if (!wheelScrolled) {
            }
        }
    };

    /**
     * Initializes wheel
     */
    private void initWheel() {
    	btnsure = (Button)findViewById(R.id.btn_sure);
    	btncancel = (Button)findViewById(R.id.btn_cancel);
    	btnsure.setOnClickListener(this);
    	btncancel.setOnClickListener(this);
        WheelView wheel = getWheel(R.id.wvAge);
        String countries[] = new String[] {"男", "女"};
        wheel.setVisibleItems(2);
        wheel.setAdapter(new ArrayWheelAdapter<String>(countries));
        wheel.addChangingListener(changedListener);
        
    //    wheel.addScrollingListener(scrolledListener);
      //  wheel.setCyclic(true);
       // wheel.setInterpolator(new AnticipateOvershootInterpolator());
        view = RegisterViewAge.this;
    }
    
    /**
     * Returns wheel by Id
     * @param id the wheel Id
     * @return the wheel with passed Id
     */
    private WheelView getWheel(int id) {
    	return (WheelView) findViewById(id);
    }
	public void StartListenDel(final PTEventHander Hander){
			ptHander = Hander;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sure:
			/*发送命令*/
			if(ptHander != null){
				Message msg = new Message();
				if(MyApplication.registerValue == 2){
					msg.what = 2;
					
				}
				msg.arg1 = value;
				ptHander.sendMessage(msg);
			}
			view.dismiss();
			break;

		case R.id.btn_cancel:
			view.dismiss();
			break;
		}
	}
}
