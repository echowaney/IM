package com.example.hashwaney.im.Factory;

import com.example.hashwaney.im.base.BaseFragment;
import com.example.hashwaney.im.fragment.ContactFragment;
import com.example.hashwaney.im.fragment.ConversationFragment;
import com.example.hashwaney.im.fragment.PluginFragment;

/**

 用来创建fragment的工厂类
 工厂模式+单例设计模式
 */

public class FragmentFactory {
    private static ContactFragment      mContactFragment;
    private static ConversationFragment mConversationFragment;
    private static PluginFragment       mPluginFragment;


    public static BaseFragment createFragment(int position) {
        BaseFragment baseFragment = null;
        switch (position) {
            case 0:
                if (mConversationFragment == null) {
                    synchronized (FragmentFactory.class) {
                        if (mConversationFragment == null) {
                            mConversationFragment = new ConversationFragment();
                        }
                    }
                }
                baseFragment=mConversationFragment;
                break;
            case 1:
                if (mContactFragment == null) {
                    synchronized (FragmentFactory.class){

                        if (mContactFragment==null){

                            mContactFragment = new ContactFragment();
                        }
                    }

                    }
                baseFragment =mContactFragment;
                break;


            case 2:
                if (mPluginFragment == null) {
                    synchronized (FragmentFactory.class) {
                        if (mPluginFragment == null) {
                            mPluginFragment = new PluginFragment();
                        }
                    }
                }
                baseFragment=mPluginFragment;
                break;
        }
    return baseFragment;

    }


}
