package com.example.hashwaney.im;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.hashwaney.im.Factory.FragmentFactory;
import com.example.hashwaney.im.activity.AddFriendActivity;
import com.example.hashwaney.im.base.BaseActivity;
import com.example.hashwaney.im.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity
        extends BaseActivity
        implements BottomNavigationBar.OnTabSelectedListener
{
    private int[] titles = {R.string.conversation,
                            R.string.contact,
                            R.string.plugin};

    @InjectView(R.id.tv_title)
    TextView            mTvTitle;
    @InjectView(R.id.toolbar)
    Toolbar             mToolbar;
    @InjectView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initToolbar();
        initBottomNavigation();
        initFrgament();


    }



    //初始化toolbar
    private void initToolbar() {
        mToolbar.setTitle("");  //默认没有标题
        setSupportActionBar(mToolbar);  //用toolbar代替actionbar，必须给它设置这么一个属性，声明是用toolbar
        //        getSupportActionBar();
        mTvTitle.setText(titles[0]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//有一个返回键

    }
    //初始化底部导航栏
    private void initBottomNavigation() {
//        mBottomNavigationBar.addItem(new )
//        mBottomNavigationBar.setActiveColor(R.color.btn_pressed);
//        mBottomNavigationBar.setInActiveColor(R.color.btn_default);
//        mBottomNavigationBar.setTabSelectedListener(this);
//        mBottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.conversation_selected_2,"消息"))
//                            .addItem(new BottomNavigationItem(R.mipmap.conversation_selected_2,"联系人"))
//                            .addItem(new BottomNavigationItem(R.mipmap.plugin_selected_2,"动态"))
//                            .invalidate();

        BottomNavigationItem conversationItem =new BottomNavigationItem(R.mipmap.conversation_selected_2,"消息");
        BottomNavigationItem contactItem=new BottomNavigationItem(R.mipmap.contact_selected_2,"联系人");
        BottomNavigationItem pluginItem =new BottomNavigationItem(R.mipmap.plugin_selected_2,"动态");


        mBottomNavigationBar.addItem(conversationItem);
        mBottomNavigationBar.addItem(contactItem);
        mBottomNavigationBar.addItem(pluginItem);

        mBottomNavigationBar.setActiveColor(R.color.btn_pressed);
        mBottomNavigationBar.setInActiveColor(R.color.btn_default);
        mBottomNavigationBar.setTabSelectedListener(this);
        mBottomNavigationBar.initialise();


    }
        //为了在进入到这个界面的时候，首先添加一个fragment，并且让其指定为第一个，进入到这个界面就去加载这个界面的数据
    private void initFrgament() {
        //创建fragment
       // FragmentFactory.createFragment(0);
        //如果Activity中已经有老的的fragment的,先全部移除,避免重影
        FragmentManager     supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction       =supportFragmentManager.beginTransaction();

        for (int i = 0; i <titles.length ; i++) {
            Fragment fragment =  supportFragmentManager.findFragmentByTag(i+"");
            if (fragment!=null){            //进行为空判断，不然会导致空指针异常
                beginTransaction.remove(fragment);
            }
        }
        beginTransaction.commit();
        //默认选中第一个fragment
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content,FragmentFactory.createFragment(0),"0").commit();
        mTvTitle.setText(titles[0]);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//            return super.onCreateOptionsMenu(menu); //return true，是为了展示这个menu的
        //同样的，如何去展示我们这个menu，那么就需要将我们定义好的的menu.xml
        //通过布  局管理器给填充成我们的view
        getMenuInflater().inflate(R.menu.main,menu);
        return true;

    }


    //menu是默认不显示图标的，这个方法调用，是在点击这个menu建，然后展示menu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuBuilder builder = (MenuBuilder) menu;
        builder.setOptionalIconsVisible(true);
        return true;
    }

    //menu的

    //true :代表这个点击事件被相应item消费，拦截下来自己处理了，false，代表没有被消费，按照正常的顺序向下走
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutme:
                Toast.makeText(this, "关于我们", Toast.LENGTH_SHORT)
                     .show();

                break;
            case R.id.share_friend:
                Toast.makeText(this, "分享好友", Toast.LENGTH_SHORT)
                     .show();
                 break;
            case R.id.add_friend:
//              跳转到添加好友界面
                startActivity(AddFriendActivity.class,false);
                break;
            case android.R.id.home:
                finish();

                break;
        }

        return true;
    }

    //条目被选中
    @Override
    public void onTabSelected(int position) {
       //TODO
        /**
         * 首先判断是否存在fragment，
         * 如果没有，就去创建这个fragment，
         * 如果有直接让其显示
         */

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.fl_content,FragmentFactory.createFragment(position),"position");



        BaseFragment fragment = FragmentFactory.createFragment(position);
        if (!fragment.isAdded()){
            fragmentTransaction.add(R.id.fl_content,fragment,""+position);
        }
        fragmentTransaction
                .show(fragment);
        fragmentTransaction.commit();

        mTvTitle.setText(titles[position]);


    }
    //条目未被选中
    @Override
    public void onTabUnselected(int position) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        BaseFragment fragment = FragmentFactory.createFragment(position);
        fragmentTransaction.hide(fragment).commit();
    }
    //条目被再次选中
    @Override
    public void onTabReselected(int position) {

    }
}
