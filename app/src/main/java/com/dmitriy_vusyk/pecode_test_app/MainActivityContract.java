package com.dmitriy_vusyk.pecode_test_app;

public interface MainActivityContract {

    interface View {

        void setPresenter(Presenter presenter);
    }

    interface Presenter {

        MyFragment createFragment();

        void removeFragment();

        void setPagerCurrentItem(int fragmentId);

        void createNotification();

        void removeNotification();

        void removeAllNotificationsForFragment(int fragmentPosition);
    }
}
