package com.dmitriy_vusyk.pecode_test_app.interfaces;

import com.dmitriy_vusyk.pecode_test_app.view.PageFragment;

public interface MainActivityContract {

    interface View {

        void setPresenter(Presenter presenter);
    }

    interface Presenter {

        PageFragment createFragment();

        void removeFragment();

        void setPagerCurrentItem(int fragmentId);

        void createNotification();

        void removeNotification();

        void removeAllNotificationsForFragment(int fragmentPosition);
    }
}
