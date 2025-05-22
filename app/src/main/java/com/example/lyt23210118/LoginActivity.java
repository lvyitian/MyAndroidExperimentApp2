package com.example.lyt23210118;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LoginActivity extends BaseInteroperableActivity<LoginActivity> {
    public final ConcurrentHashMap<String,String> users=new ConcurrentHashMap<>();
    {
        this.users.put("lyt","123123lyt");
        this.users.put("b","bbb");
        this.users.put("c","ccc");
    }
    public EditText textBox_username,textBox_password;
    public Button button_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.initializeComponentFields();
        this.button_login.setOnClickListener(v->{
            if(Objects.equals(this.users.get(textBox_username.getText().toString().trim()),this.textBox_password.getText().toString())){
                BaseInteroperableActivity.getOrCreateInstanceAndThen(this,OperateActivity.class,t->t.select(ManageActivity.getWrappedList(t.users).stream().map(u->u.name).collect(Collectors.toList()).indexOf(this.textBox_username.getText().toString()))).ifPresent(i->{
                    i.select(ManageActivity.getWrappedList(i.users).stream().map(u->u.name).collect(Collectors.toList()).indexOf(this.textBox_username.getText().toString()));
                    Intent intent=new Intent(this,i.getClass());
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    this.startActivity(intent);
                });
            }else Toast.makeText(this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        Optional.ofNullable(BaseInteroperableActivity.getInstance(ManageActivity.class)).ifPresent(Activity::finish);
        super.onResume();
    }
}
