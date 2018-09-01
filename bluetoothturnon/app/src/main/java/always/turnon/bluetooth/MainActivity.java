package always.turnon.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    String LightOn = "4";
    //String LightOff = "5";
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    BluetoothSocket bluetoothSocket;
    InputStream input;
    OutputStream output;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
        UUID uuid = UUID.fromString(SPP_UUID);
        textView = (TextView)findViewById(R.id.textView);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//获取默认蓝牙适配器
        if (!bluetoothAdapter.isEnabled()) {
            if(bluetoothAdapter.enable())//检查是否打开蓝牙，如果没有就打开
                textView.setText("蓝牙已打开,未连接设备");
        }
        else{
            textView.setText("蓝牙已打开，未连接设备");
        }


        bluetoothDevice = bluetoothAdapter.getRemoteDevice("98:D3:32:11:4C:31");//通过ＭＡＣ地址获取一个蓝牙设备，这个ＭＡＣ是这个蓝牙模块的
        /*获取ｓｏｃｋｅｔ*/
        try{
            bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
        }catch (Exception e){
            Toast.makeText(MainActivity.this,"error1",Toast.LENGTH_SHORT).show();
        }


        bluetoothAdapter.cancelDiscovery();//关闭搜索，我不知道我这里需不需要，因为我使用ＭＡＣ获取的蓝牙设备
        /*连接蓝牙*/
        try{
            if(!bluetoothSocket.isConnected())
                bluetoothSocket.connect();//这个是会阻塞线程的，如果蓝牙不连接就会在这里等到连接
            Toast.makeText(MainActivity.this,"设备连接成功",Toast.LENGTH_SHORT).show();
            textView.setText("已连接设备");
        }catch (Exception e){
            Toast.makeText(MainActivity.this,"error2",Toast.LENGTH_SHORT).show();
        }



        // Get the BluetoothSocket input and output streams
        try {
            input = bluetoothSocket.getInputStream();
            output = bluetoothSocket.getOutputStream();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this,"Stream error",Toast.LENGTH_SHORT).show();
        }


        // 这里需要try catch一下，以防异常抛出
        try {
            // 判断客户端接口是否为空
            // 获取到输出流，向外写数据
            output = bluetoothSocket.getOutputStream();


            // 判断是否拿到输出流
            if (output != null) {
                // 以utf-8的格式发送出去
                output.write(LightOn.getBytes("UTF-8"));//LightOff is a string which you want to transfer
            }
            // 吐司一下，告诉用户发送成功
            Toast.makeText(this, "打开成功", 0).show();
        } catch (Exception e) {
            // 如果发生异常则告诉用户发送失败
            Toast.makeText(this, "异常，打开失败", 0).show();
        }
    }
}
