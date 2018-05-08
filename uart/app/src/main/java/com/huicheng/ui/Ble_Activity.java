package com.huicheng.ui;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.huicheng.service.*;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.graphics.Color;

import com.huicheng.R;
import com.huicheng.service.BluetoothLeService;

/**
 * 特别说明：HC_BLE助手是广州汇承信息科技有限公司独自研发的手机APP，方便用户调试08蓝牙模块。
 * 本软件只能支持安卓版本4.3并且有蓝牙4.0的手机使用。
 * 另外对于自家的05、06模块，要使用另外一套蓝牙2.0的手机APP，用户可以在汇承官方网的下载中心自行下载。
 * 本软件提供代码和注释，免费给购买汇承08模块的用户学习和研究，但不能用于商业开发，最终解析权在广州汇承信息科技有限公司。
 * **/

/**
 * @Description:  TODO<Ble_Activity实现连接BLE,发送和接受BLE的数据>
 * @author  广州汇承信息科技有限公司
 * @data:  2014-10-20 下午12:12:04
 * @version:  V1.0
 */
public class Ble_Activity extends Activity implements OnClickListener {

	private final static String TAG = Ble_Activity.class.getSimpleName();
	//蓝牙4.0的UUID,其中0000ffe1-0000-1000-8000-00805f9b34fb是广州汇承信息科技有限公司08蓝牙模块的UUID
	public static String HEART_RATE_MEASUREMENT = "0000ffe1-0000-1000-8000-00805f9b34fb";
	public static String EXTRAS_DEVICE_NAME = "DEVICE_NAME";;
	public static String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
	public static String EXTRAS_DEVICE_RSSI = "RSSI";
	//蓝牙连接状态
	private boolean mConnected = false;
	private String status = "disconnected";
	//蓝牙名字
	private String mDeviceName;
	//蓝牙地址
	private String mDeviceAddress;
	//蓝牙信号值
	private String mRssi;
	private Bundle b;
	public String rev_str = "";
	public String temp_str= "";
	public  String temp0_str= "";
	private String temp1_str= "";
	private String temp2_str= "";
	private String temp3_str= "";
	private String temp4_str= "";
	private String temp5_str= "";
	//蓝牙service,负责后台的蓝牙服务
	private static BluetoothLeService mBluetoothLeService;
	//文本框，显示接受的内容
//	private TextView rev_tv, connect_state;
	private TextView  connect_state;
	private TextView  T1;
	private TextView  T2;
	private TextView  T3;
	private TextView  T4;
	private TextView  T5;
	private TextView  T6;
	//发送按钮
	private Button send_btn;
	//发送按钮
	private Button send_btn0;
	//发送按钮
	private Button send_btn1;
	//发送按钮
	private Button send_btn2;
	//发送按钮
	private Button send_btn11;
	//发送按钮
	private Button send_btn12;
	//文本编辑框
	private EditText send_et;

	private EditText send_et1;

	private ScrollView rev_sv;

	private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
	//蓝牙特征值
	private static BluetoothGattCharacteristic target_chara = null;
	public static  byte[] revDataForCharacteristic;
	private Handler mhandler = new Handler();
	private Handler myHandler = new Handler()
	{
		// 2.重写消息处理函数
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				// 判断发送的消息
				case 1:
				{
					// 更新View
					String state = msg.getData().getString("connect_state");
					connect_state.setText(state);

					break;
				}

			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ble_activity);
		b = getIntent().getExtras();
		//从意图获取显示的蓝牙信息
		mDeviceName = b.getString(EXTRAS_DEVICE_NAME);
		mDeviceAddress = b.getString(EXTRAS_DEVICE_ADDRESS);
		mRssi = b.getString(EXTRAS_DEVICE_RSSI);

	    /* 启动蓝牙service */
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
		init();

	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		//解除广播接收器
		unregisterReceiver(mGattUpdateReceiver);
		mBluetoothLeService = null;
	}

	// Activity出来时候，绑定广播接收器，监听蓝牙连接服务传过来的事件
	@Override
	protected void onResume()
	{
		super.onResume();
		//绑定广播接收器
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		if (mBluetoothLeService != null)
		{
			//根据蓝牙地址，建立连接
			final boolean result = mBluetoothLeService.connect(mDeviceAddress);
			Log.d(TAG, "Connect request result=" + result);
		}
	}

	/**
	 * @Title: init
	 * @Description: TODO(初始化UI控件)
	 * @param  无
	 * @return void
	 * @throws
	 */
	private void init()
	{
	//	rev_sv = (ScrollView) this.findViewById(R.id.rev_sv);
	//	rev_tv = (TextView) this.findViewById(R.id.rev_tv);

//		T1= (TextView) this.findViewById(R.id.T1);
//		T2= (TextView) this.findViewById(R.id.T2);
//		T3= (TextView) this.findViewById(R.id.T3);
//		T4= (TextView) this.findViewById(R.id.T4);
		T5= (TextView) this.findViewById(R.id.T5);
		T6= (TextView) this.findViewById(R.id.T6);

		connect_state = (TextView) this.findViewById(R.id.connect_state);

		send_btn = (Button) this.findViewById(R.id.send_btn);

		send_btn0 = (Button) this.findViewById(R.id.send_btn0);
		send_btn1 = (Button) this.findViewById(R.id.send_btn1);
		send_btn2 = (Button) this.findViewById(R.id.send_btn2);

		send_btn11 = (Button) this.findViewById(R.id.send_btn11);
		send_btn12 = (Button) this.findViewById(R.id.send_btn12);

		send_et = (EditText) this.findViewById(R.id.send_et);
		send_et1 = (EditText) this.findViewById(R.id.send_et1);

		connect_state.setText(status);
	/*	T1.setText(status);
		T2.setText(status);
		T3.setText(status);
		T4.setText(status);
		T5.setText(status);
		T6.setText(status);*/

		send_btn.setOnClickListener(this);
		send_btn0.setOnClickListener(this);
		send_btn1.setOnClickListener(this);
		send_btn2.setOnClickListener(this);

		send_btn11.setOnClickListener(this);
		send_btn12.setOnClickListener(this);

	}

	/* BluetoothLeService绑定的回调函数 */
	private final ServiceConnection mServiceConnection = new ServiceConnection()
	{

		@Override
		public void onServiceConnected(ComponentName componentName,
									   IBinder service)
		{
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
					.getService();
			if (!mBluetoothLeService.initialize())
			{
				Log.e(TAG, "Unable to initialize Bluetooth");
				finish();
			}
			// Automatically connects to the device upon successful start-up
			// initialization.
			// 根据蓝牙地址，连接设备
			mBluetoothLeService.connect(mDeviceAddress);

		}

		@Override
		public void onServiceDisconnected(ComponentName componentName)
		{
			mBluetoothLeService = null;
		}

	};

	/**
	 * 广播接收器，负责接收BluetoothLeService类发送的数据
	 */
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action))//Gatt连接成功
			{
				mConnected = true;
				status = "connected";
				//更新连接状态
				updateConnectionState(status);
			//	System.out.println("BroadcastReceiver :" + "device connected");

			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED//Gatt连接失败
					.equals(action))
			{
				mConnected = false;
				//status = "disconnected";
				status = "disconnected";
				//更新连接状态
				updateConnectionState(status);
				System.out.println("BroadcastReceiver :"
					+ "device disconnected");

			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED//发现GATT服务器
					.equals(action))
			{
				// Show all the supported services and characteristics on the
				// user interface.
				//获取设备的所有蓝牙服务
				displayGattServices(mBluetoothLeService
						.getSupportedGattServices());
				System.out.println("BroadcastReceiver :"
						+ "device SERVICES_DISCOVERED");
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action))//有效数据
			{
				//处理发送过来的数据
				displayData(intent.getExtras().getString(
						BluetoothLeService.EXTRA_DATA),intent);
				System.out.println("BroadcastReceiver onData:"
						+ intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
			}
		}
	};

	/* 更新连接状态 */
	private void updateConnectionState(String status)
	{
		Message msg = new Message();
		msg.what = 1;
		Bundle b = new Bundle();
		b.putString("connect_state", status);
		msg.setData(b);
		//将连接状态更新的UI的textview上
		myHandler.sendMessage(msg);
		System.out.println("connect_state:" + status);
	}

	/* 意图过滤器 */
	private static IntentFilter makeGattUpdateIntentFilter()
	{
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter
				.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		return intentFilter;
	}

	/**
	 * @Title: displayData
	 * @Description: TODO(接收到的数据在scrollview上显示)
	 * @param @param rev_string(接受的数据)
	 * @return void
	 * @throws
	 */
	static int count_x=0;
	private void displayData(String rev_string,Intent intent)
	{

		try {
			byte[] data = intent.getByteArrayExtra("BLE_BYTE_DATA");
			if (data == null)
				System.out.println("data is null!!!!!!");
				//GB2312编码
			   rev_string = new String(data, 0, data.length, "UTF-8");
		   }
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rev_str += rev_string;
	//	temp_str=rev_string;
		//更新UI
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				byte [] temper=new byte[100];
				temper=rev_str.getBytes();
				byte [] temper_value1=new byte[100];
				byte [] temper_value2=new byte[100];
				byte [] temper_value3=new byte[100];
				byte [] temper_value4=new byte[100];
				byte [] temper_value5=new byte[100];
				byte [] temper_value6=new byte[100];
				int count_temp=0;
				int count_temp1=0;
				int start=0;
				int stop=0;
				int temp_x=0;
				for(int i=2;i<temper.length-1;i++)
				{
					if(temper[i]==':'&&temper[i-2]=='C')
					{
						start=1;
						temp_x=i;
						if(start==1)
							count_temp=temper[i-1]-'0';
					}
					if(start==1&&count_temp==1)
					{
						if((temper[i+1]>='0'&&temper[i+1]<='9')||(temper[i+1]=='.'))
						temper_value1[i-temp_x]=temper[i+1];
						if(temper[i]==' ')
						{
							start=0;
							temp0_str = new String(temper_value1);
							T1.setText(temp0_str);
							temp0_str="";
						}
					}
					if(start==1&&count_temp==2)
					{
						if((temper[i+1]>='0'&&temper[i+1]<='9')||(temper[i+1]=='.'))
						temper_value2[i-temp_x]=temper[i+1];
						if(temper[i]==' ')
						{
							start=0;
							temp1_str = new String(temper_value2);
							T2.setText(temp1_str);
							temp1_str="";
						}
					}
					if(start==1&&count_temp==3)
					{
						if((temper[i+1]>='0'&&temper[i+1]<='9')||(temper[i+1]=='.'))
						temper_value3[i-temp_x]=temper[i+1];
						if(temper[i]==' ')
						{
							start=0;
							temp2_str = new String(temper_value3);
							T3.setText(temp2_str);
							temp2_str="";
						}
					}
					if(start==1&&count_temp==4)
					{
						if((temper[i+1]>='0'&&temper[i+1]<='9')||(temper[i+1]=='.'))
							temper_value4[i-temp_x]=temper[i+1];
						if(temper[i]==' ')
						{
							start=0;
							temp3_str = new String(temper_value4);
							T4.setText(temp3_str);
							temp3_str="";
						}
					}
					if(start==1&&count_temp==5)
					{
						if((temper[i+1]>='0'&&temper[i+1]<='9')||(temper[i+1]=='.'))
							temper_value5[i-temp_x]=temper[i+1];
						if(temper[i]==' ')
						{
							start=0;
							temp4_str = new String(temper_value5);
							T5.setText(temp4_str);
							temp4_str="";
						}
					}
					if(start==1&&count_temp==6)
					{
						if((temper[i+1]>='0'&&temper[i+1]<='9')||(temper[i+1]=='.'))
							temper_value6[i-temp_x]=temper[i+1];
						if(temper[i]==' ')
						{
							start=0;
							temp5_str = new String(temper_value6);
							T6.setText(temp5_str);
							temp5_str="";
						}
					}
				}
				count_temp=0;
				rev_str="";
			//	System.out.println("rev:" + rev_str);
			//	count_x++;
			//	rev_sv.scrollTo(0, rev_tv.getMeasuredHeight());
			}
		});

	}

	/**
	 * @Title: displayGattServices
	 * @Description: TODO(处理蓝牙服务)
	 * @param 无
	 * @return void
	 * @throws
	 */
	private void displayGattServices(List<BluetoothGattService> gattServices)
	{

		if (gattServices == null)
			return;
		String uuid = null;
		String unknownServiceString = "unknown_service";
		String unknownCharaString = "unknown_characteristic";

		// 服务数据,可扩展下拉列表的第一级数据
		ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();

		// 特征数据（隶属于某一级服务下面的特征值集合）
		ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();

		// 部分层次，所有特征值集合
		mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

		// Loops through available GATT Services.
		for (BluetoothGattService gattService : gattServices)
		{

			// 获取服务列表
			HashMap<String, String> currentServiceData = new HashMap<String, String>();
			uuid = gattService.getUuid().toString();

			// 查表，根据该uuid获取对应的服务名称。SampleGattAttributes这个表需要自定义。

			gattServiceData.add(currentServiceData);

			System.out.println("Service uuid:" + uuid);

			ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();

			// 从当前循环所指向的服务中读取特征值列表
			List<BluetoothGattCharacteristic> gattCharacteristics = gattService
					.getCharacteristics();

			ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

			// Loops through available Characteristics.
			// 对于当前循环所指向的服务中的每一个特征值
			for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics)
			{
				charas.add(gattCharacteristic);
				HashMap<String, String> currentCharaData = new HashMap<String, String>();
				uuid = gattCharacteristic.getUuid().toString();

				if (gattCharacteristic.getUuid().toString()
						.equals(HEART_RATE_MEASUREMENT))
				{
					// 测试读取当前Characteristic数据，会触发mOnDataAvailable.onCharacteristicRead()
					mhandler.postDelayed(new Runnable()
					{

						@Override
						public void run()
						{
							// TODO Auto-generated method stub
							mBluetoothLeService
									.readCharacteristic(gattCharacteristic);
						}
					}, 200);

					// 接受Characteristic被写的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
					mBluetoothLeService.setCharacteristicNotification(
							gattCharacteristic, true);
					target_chara = gattCharacteristic;
					// 设置数据内容
					// 往蓝牙模块写入数据
					// mBluetoothLeService.writeCharacteristic(gattCharacteristic);
				}
				List<BluetoothGattDescriptor> descriptors = gattCharacteristic
						.getDescriptors();
				for (BluetoothGattDescriptor descriptor : descriptors)
				{
					System.out.println("---descriptor UUID:"
							+ descriptor.getUuid());
					// 获取特征值的描述
					mBluetoothLeService.getCharacteristicDescriptor(descriptor);
					// mBluetoothLeService.setCharacteristicNotification(gattCharacteristic,
					// true);
				}

				gattCharacteristicGroupData.add(currentCharaData);
			}
			// 按先后顺序，分层次放入特征值集合中，只有特征值
			mGattCharacteristics.add(charas);
			// 构件第二级扩展列表（服务下面的特征值）
			gattCharacteristicData.add(gattCharacteristicGroupData);

		}

	}
	/**
	 * 将数据分包
	 *
	 * **/
	public int[] dataSeparate(int len)
	{
		int[] lens = new int[2];
		lens[0]=len/20;
		lens[1]=len%20;
		return lens;
	}

	/*
     * 数据发送线程
     *
     * */
	public class sendDataThread implements Runnable{


		public sendDataThread() {
			super();
			new Thread(this).start();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			/* byte[] buff =null;
			 try {
				buff =send_et.getText().toString().getBytes("GB2312");
				System.out.println("buff len:"+buff.length);
				}
				catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 int[] sendDatalens = dataSeparate(buff.length);//高20位
			 for(int i=0;i<sendDatalens[0];i++)
			 {
				  byte[] dataFor20 = new byte[20];
				  for(int j=0;j<20;j++)
				  {
					  dataFor20[j]=buff[i*20+j];
				  }
				  System.out.println("here1");
				  System.out.println("here1:"+new String(dataFor20));
				 target_chara.setValue(dataFor20);//发送高20位
				 mBluetoothLeService.writeCharacteristic(target_chara);//发送高20位
			 }
			 if(sendDatalens[1]!=0)//低20位
			 {
				System.out.println("here2");
			   byte[] lastData = new byte[20];
			   for(int i=0;i<sendDatalens[1];i++)
				   lastData[i]=buff[sendDatalens[0]*20+i];
			   String str=null;
				try {
				str = new String(lastData, 0, sendDatalens[1],"GB2312");
			     }
			catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("here2:"+str+"-------len:"+str.length());

			 target_chara.setValue(lastData);
			 mBluetoothLeService.writeCharacteristic(target_chara);
			 }
			//target_chara.setValue(lastData);
			//mBluetoothLeService.writeCharacteristic(target_chara);
			*/
		}
	}
	public void Send_fa1()
	{
		String lastData ="AT+CMD=01$";
		target_chara.setValue(lastData);
		mBluetoothLeService.writeCharacteristic(target_chara);
	}
	public void Send_fa2()
	{
		String lastData ="AT+CMD=02$";
		target_chara.setValue(lastData);
		mBluetoothLeService.writeCharacteristic(target_chara);
	}
	public void Send_fa3()
	{
		String lastData ="AT+CMD=03$";
		target_chara.setValue(lastData);
		mBluetoothLeService.writeCharacteristic(target_chara);
	}
	public void Send_fa4()
	{
		String lastData ="AT+CMD=04$";
		target_chara.setValue(lastData);
		mBluetoothLeService.writeCharacteristic(target_chara);
	}

	public void Send_fa5()
	{
		String lastData ="AT+CMD=11$";
		target_chara.setValue(lastData);
		mBluetoothLeService.writeCharacteristic(target_chara);
	}
	public void Send_fa6()
	{
		String lastData ="AT+CMD=12$";
		target_chara.setValue(lastData);
		mBluetoothLeService.writeCharacteristic(target_chara);
	}
	public void Send_fa7()
	{
		String lastData ="AT+CMD=13$";
		target_chara.setValue(lastData);
		mBluetoothLeService.writeCharacteristic(target_chara);
	}
	public void Send_fa8()
	{
		String lastData ="AT+CMD=14$";
		target_chara.setValue(lastData);
		mBluetoothLeService.writeCharacteristic(target_chara);
	}
	/*
	 * 发送按键的响应事件，主要发送文本框的数据
	 */
	static boolean key1=false;
	static boolean key2=false;
	static boolean key3=false;
	static boolean key4=false;
	String Settemp="SetT";
	String Settemp1="SetV";
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.send_btn:
			{
				if(key1==false)
				{
					Send_fa1();
					send_btn.setText("ON");
					key1=true;
				}
				else
				{
					Send_fa5();
					send_btn.setText("OFF");
					key1=false;
				}
				break;
			}
			case R.id.send_btn0:
			{
				if(key2==false)
				{
					Send_fa2();
					send_btn0.setText("ON");
					key2=true;
				}
				else
				{
					Send_fa6();
					send_btn0.setText("0FF");
					key2=false;
				}
				break;
			}
			case R.id.send_btn1:
			{
				if(key3==false)
				{
					Send_fa3();
					send_btn1.setText("ON");
					key3=true;
				}
				else
				{
					Send_fa7();
					send_btn1.setText("OFF");
					key3=false;
				}
				break;

			}
			case R.id.send_btn2:
			{

				if(key4==false)
				{
					Send_fa4();
					send_btn2.setText("ON");
					key4=true;
				}
				else
				{
					Send_fa8();
					send_btn2.setText("OFF");
					key4=false;
				}
				break;
			}
			case R.id.send_btn11:
			{
				String temp =send_et.getText().toString();
				temp+=Settemp;
				target_chara.setValue(temp);
				mBluetoothLeService.writeCharacteristic(target_chara);
				break;
			}
			case R.id.send_btn12:
			{
				String temp1 =send_et1.getText().toString();
				temp1+=Settemp1;
				target_chara.setValue(temp1);
				mBluetoothLeService.writeCharacteristic(target_chara);
				break;
			}
			default:break;
		}


	}

}
