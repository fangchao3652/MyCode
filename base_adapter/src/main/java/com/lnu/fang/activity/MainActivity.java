package com.lnu.fang.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.lnu.fang.adapter.MyAdapter;
import com.lnu.fang.adapter.MyAdapterWithCommonViewHolder;
import com.lnu.fang.bean.Bean;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    ListView listView;
    List<Bean> mdatas;
    MyAdapterWithCommonViewHolder adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        initData();
        listView.setAdapter(adapter);
    }

    void initview() {
        listView = (ListView) findViewById(R.id.listview);
    }

    void initData() {
        mdatas = new ArrayList<>();
        Bean bean = new Bean("Android新技能1", "androdi  打造万能的adapter", "2012-12-12", "10086");
        mdatas.add(bean);
        bean = new Bean("Android新技能2", "androdi  打造万能的adapter", "2012-12-12", "10086");
        mdatas.add(bean);
        bean = new Bean("Android新技能3", "androdi  打造万能的adapter", "2012-12-12", "10086");
        mdatas.add(bean);
        bean = new Bean("Android新技能4", "androdi  打造万能的adapter", "2012-12-12", "10086");
        mdatas.add(bean);
        bean = new Bean("Android新技能5", "androdi  打造万能的adapter", "2012-12-12", "10086");
        mdatas.add(bean);
        bean = new Bean("Android新技能6", "androdi  打造万能的adapter", "2012-12-12", "10086");
        mdatas.add(bean);
        bean = new Bean("Android新技能7", "androdi  打造万能的adapter", "2012-12-12", "10086");
        mdatas.add(bean);
        bean = new Bean("Android新技能8", "androdi  打造万能的adapter", "2012-12-12", "10086");
        mdatas.add(bean);

        adapter=new MyAdapterWithCommonViewHolder(this,mdatas);
    }

}
