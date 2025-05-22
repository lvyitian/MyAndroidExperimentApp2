package com.example.lyt23210118;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class ManageActivity extends BaseInteroperableActivity<ManageActivity> {
    public int selected=Objects.requireNonNull(BaseInteroperableActivity.getInstance(OperateActivity.class)).selected;
    public Button button_return;
    public ListView listView_students;
    public FrameLayout fragment_container;
    public StudentInfoFragment fragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_manage);
        this.initializeComponentFields();
        this.listView_students.setAdapter(new ArrayAdapter<User>(this,R.layout.list_view_layout_student, getWrappedList(Objects.requireNonNull(BaseInteroperableActivity.getInstance(OperateActivity.class)).users)){
            @Override
            public View getView(int pos, View contentView, ViewGroup parent){
                if(contentView==null)contentView= LayoutInflater.from(this.getContext()).inflate(R.layout.list_view_layout_student,parent,false);
                TextView v=contentView.findViewById(R.id.list_view_layout_textView_studentName);
                v.setText(this.getItem(pos).name);
                return contentView;
            }
        });
        this.listView_students.setOnItemClickListener((parent,view,position,id)->{
            parent.setSelection(this.selected=position);
            this.switchTo(R.layout.student_info);
        });
        this.button_return.setOnClickListener(v->this.finish());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        this.switchTo(R.layout.student_info);
        super.onResume();
    }

    public static <T> CopyOnWriteArrayList<T> getWrappedList(CopyOnWriteArraySet<T> set){
        try {
            Field f = CopyOnWriteArraySet.class.getDeclaredField("al");
            f.setAccessible(true);
            return (CopyOnWriteArrayList<T>) f.get(set);
        }catch(Throwable t){throw new RuntimeException(t);}
    }
    public void switchTo(int layout){
        FragmentTransaction transaction=this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,this.fragment=new StudentInfoFragment(layout));
        this.fragment.onCreateCallbacks.add(i->{
            new Thread(()->this.runOnUiThread(()->{
                TextView name=this.fragment.getView().findViewById(R.id.student_info_name);
                TextView gender=this.fragment.getView().findViewById(R.id.student_info_gender);
                TextView hobbies=this.fragment.getView().findViewById(R.id.student_info_hobbies);
                name.setText("姓名: "+getWrappedList(Objects.requireNonNull(BaseInteroperableActivity.getInstance(OperateActivity.class)).users).get(this.selected).name);
                gender.setText("性别: "+getWrappedList(Objects.requireNonNull(BaseInteroperableActivity.getInstance(OperateActivity.class)).users).get(this.selected).gender);
                hobbies.setText("爱好: "+getWrappedList(Objects.requireNonNull(BaseInteroperableActivity.getInstance(OperateActivity.class)).users).get(this.selected).hobbies.stream().collect(Collectors.joining(" ")));
            })).start();
        });
        transaction.commit();
    }
}
