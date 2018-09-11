package captain.wonjong.sms_retriever;

import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements SmsReceiver.OTPReceiveListener {

    private Context mContext;
    private SmsReceiver smsReceiver;
    private TextView mTvOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        smsReceiver = new SmsReceiver();
        mTvOtp = findViewById(R.id.tv_otp);

        // 해쉬값 알고싶으면 아래 주석 해제
        // Util.getAppSignatures(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SmsRetrieverClient client = SmsRetriever.getClient(this /* context */);
        Task<Void> task = client.startSmsRetriever();

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
                registerReceiver(smsReceiver, intentFilter);
                Log.e("testest", "onSuccess");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("testest", "onFailure" + e.toString());
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(smsReceiver);
    }


    @Override
    public void onOTPReceived(String msg) {
        mTvOtp.setText("OTP Number : " +  msg);
    }

    @Override
    public void onOTPTimeOut() {
        mTvOtp.setText("Timeout");
    }
}
