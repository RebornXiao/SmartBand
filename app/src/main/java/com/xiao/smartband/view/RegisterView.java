package com.xiao.smartband.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;

import com.xiao.smartband.MyApplication;
import com.xiao.smartband.R;

public class RegisterView extends Dialog implements OnClickListener{
	private Button btnsure,btncancel;
	private RegisterView view;
	private static PTEventHander ptHander;
	private int value;
	public RegisterView(Context context) {
		super(context);
	}
	public RegisterView(Context context, int theme) {
		super(context, theme);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bracelet_register_view);
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
        WheelView wheel = getWheel(R.id.passw_1);
        wheel.setAdapter(new NumericWheelAdapter(0, 200));
        wheel.setCurrentItem((int)(Math.random() * 10));
        
        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
        wheel.setCyclic(true);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
        view = RegisterView.this;
    }
    
    /**
     * Returns wheel by Id
     * @param id the wheel Id
     * @return the wheel with passed Id
     */
    private WheelView getWheel(int id) {
    	return (WheelView) findViewById(id);
    }
    
    /**
     * Tests entered PIN
     * @return true
     */
//    private boolean testPin(int v1, int v2, int v3, int v4) {
//    	return testWheelValue(R.id.passw_1, v1) && testWheelValue(R.id.passw_2, v2) &&
//    		testWheelValue(R.id.passw_3, v3) && testWheelValue(R.id.passw_4, v4);
//    }
    private boolean testPin(int v1) {
    	return testWheelValue(R.id.passw_1, v1);
    }
    
    /**
     * Tests wheel value
     * @param id the wheel Id
     * @param value the value to test
     * @return true if wheel value is equal to passed value
     */
    private boolean testWheelValue(int id, int value) {
    	System.out.println("~~~~~~~~~~~~~~~~~~~");
    	System.out.println(getWheel(id).getCurrentItem());
    	return getWheel(id).getCurrentItem() == value;
    }
    
    /**
     * Mixes wheel
     * @param id the wheel id
     */
    private void mixWheel(int id) {
        WheelView wheel = getWheel(id);
       // wheel.scroll(-25 + (int)(Math.random() * 50), 2000);
        wheel.scroll(10, 21000);
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
				if(MyApplication.registerValue == 1){
					msg.what = 1;
					
				}else if(MyApplication.registerValue == 2){
					msg.what = 2;
					
				}else if(MyApplication.registerValue == 3){
					msg.what = 3;
					
				}else if(MyApplication.registerValue == 4){
					msg.what = 4;
					
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
