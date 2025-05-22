package com.example.lyt23210118;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class OperateActivity extends BaseInteroperableActivity<OperateActivity> {
    public CopyOnWriteArraySet<User> users=new CopyOnWriteArraySet<>();
    {
        this.users.add(new User("lyt","男",new CopyOnWriteArraySet<>(Arrays.asList("音乐","阅读"))));
        this.users.add(new User("b","男",new CopyOnWriteArraySet<>(Arrays.asList("运动","音乐"))));
        this.users.add(new User("c","女",new CopyOnWriteArraySet<>(Arrays.asList("阅读"))));
    }
    public int selected;
    public RadioGroup radioGroup_gender;
    public Button button_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate);
        this.initializeComponentFields();
        this.button_submit.setOnClickListener(v->{
            ManageActivity.getWrappedList(users).get(this.selected).gender=((RadioButton)this.radioGroup_gender.findViewById(this.radioGroup_gender.getCheckedRadioButtonId())).getText().toString();
            ManageActivity.getWrappedList(users).get(this.selected).hobbies= this.getCheckBoxes().filter(CheckBox::isChecked).map(CheckBox::getText).map(Object::toString).collect(Collectors.toCollection(CopyOnWriteArraySet::new));
            BaseInteroperableActivity.getOrCreateInstanceAndThen(this,ManageActivity.class,i->i.destroyCallbacks.add(i2->
                this.select(i2.selected)
                )).ifPresent(i->{
                Intent intent=new Intent(this,i.getClass());
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                this.startActivity(intent);
            });
        });
        this.select(this.selected);
    }
    public void select(int selected){
        this.selected=selected;
        IntStream.range(0,this.radioGroup_gender.getChildCount()).mapToObj(this.radioGroup_gender::getChildAt).filter(RadioButton.class::isInstance).map(RadioButton.class::cast).filter(i3->Objects.equals(i3.getText().toString(),ManageActivity.getWrappedList(this.users).get(this.selected).gender)).findFirst().ifPresent(i3->i3.setChecked(true));
        this.getCheckBoxes().forEach(i->i.setChecked(false));
        this.getCheckBoxes().filter(i3->ManageActivity.getWrappedList(this.users).get(this.selected).hobbies.contains(i3.getText().toString())).forEach(i3->i3.setChecked(true));
    }
    public Stream<CheckBox> getCheckBoxes(){
        return Stream.of(R.id.class.getDeclaredFields()).map(i->{
            try{
                i.setAccessible(true);
                return OperateActivity.this.findViewById((int)i.get(null));
            }catch(Throwable t){
                //ignored
                return null;
            }
        }).filter(i->i instanceof CheckBox).map(CheckBox.class::cast);
    }

}
