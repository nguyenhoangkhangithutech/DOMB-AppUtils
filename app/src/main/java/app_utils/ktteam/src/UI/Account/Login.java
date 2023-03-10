package app_utils.ktteam.src.UI.Account;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import app_utils.ktteam.src.Apis.ApiService;
import app_utils.ktteam.src.Apis.Prototypes.DataUserApiResponse;
import app_utils.ktteam.src.Models.TaiKhoan;
import app_utils.ktteam.src.R;
import app_utils.ktteam.src.UI.Homes.NavigationMain;
import app_utils.ktteam.src.Utils.InformationUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {

    EditText edtSdtLogin, edtPasswordLogin;
    TextView txtForgotPass, txtRegister, txtMessageStateLogin;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();


    }

    public void init(){
        edtSdtLogin = findViewById(R.id.edtSdtLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        txtForgotPass = findViewById(R.id.txtForgotPass);
        txtRegister = findViewById(R.id.txtRegister);
        btnLogin = findViewById(R.id.btnLogin);
        txtMessageStateLogin = findViewById(R.id.txtNotifyStateLogin);

    }
    public void callApi(View view) {
        String matKhau = edtPasswordLogin.getText().toString().trim();
        String soDienThoai = edtSdtLogin.getText().toString().trim();
        if(matKhau.length()== 0 || soDienThoai.length()==0)
        {
            txtMessageStateLogin.setText("Sai số điện thoại hoặc mật khẩu");
            return;
        }
       ApiService.apiService.login(new TaiKhoan(matKhau,soDienThoai)).enqueue(new Callback<DataUserApiResponse>() {
           @Override
           public void onResponse(Call<DataUserApiResponse> call, Response<DataUserApiResponse> response) {
               DataUserApiResponse res = response.body();

               if(res != null && res.isSuccess())
               {

                  try {
                      InformationUtil.writeToFile(res.getData().getHoTen().trim(),  openFileOutput(InformationUtil.FileHoTen, MODE_PRIVATE));
                      InformationUtil.writeToFile(res.getData().getDiaChi().trim(), openFileOutput(InformationUtil.FileDiaChi, MODE_PRIVATE));
                      InformationUtil.writeToFile(res.getData().getNumberPhone().trim(),  openFileOutput(InformationUtil.FileSDT, MODE_PRIVATE));
                      InformationUtil.writeToFile(res.getData().getEmail().trim(), openFileOutput(InformationUtil.FileEmail, MODE_PRIVATE));
                      InformationUtil.writeToFile(res.getData().getUid().toString().trim(), openFileOutput(InformationUtil.FileUid, MODE_PRIVATE));
                      InformationUtil.writeToFile(res.getData().getAvatar(), openFileOutput(InformationUtil.FileAvatar, MODE_PRIVATE));

                      Intent intent = new Intent(Login.this, NavigationMain.class);
                      startActivity(intent);
                  }catch (Exception e)
                  {
                      Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_LONG);
                  }
                    return;
               }

               txtMessageStateLogin.setText("Sai số điện thoại hoặc mật khẩu");
           }

           @Override
           public void onFailure(Call<DataUserApiResponse> call, Throwable t) {
               System.out.println(t.getMessage());
           }
       });


    }

    public void ClickRegister(View view){
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
        return;
    }

    public  void ClickForgetPass(View view)
    {
        Intent intent = new Intent(Login.this, ForgotPassword.class);
        startActivity(intent);
        return;
    }




}