package com.dmitriy_vusyk.pecode_test_app.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dmitriy_vusyk.pecode_test_app.presenter.MainPresenterImpl;
import com.dmitriy_vusyk.pecode_test_app.R;

import static com.dmitriy_vusyk.pecode_test_app.constants.Constants.ARGUMENT_PAGE_LABEL;
import static com.dmitriy_vusyk.pecode_test_app.constants.Constants.ARGUMENT_PAGE_NUMBER;

public class PageFragment extends Fragment implements View.OnClickListener {

    private ImageButton ibCreateNotification;
    private ImageButton ibAddFragment;
    private ImageButton ibDeleteFragment;
    private TextView tvFragmentNumber;
    private TextView tvCreateNotification;
    private ImageView ivOval;
    private int fragmentId;
    private int label;
    private MainPresenterImpl presenter;

    public int getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(int fragmentId) {
        this.fragmentId = fragmentId;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public void setPresenter(MainPresenterImpl presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        fragmentId = getArguments().getInt(ARGUMENT_PAGE_NUMBER, 0);
        label = getArguments().getInt(ARGUMENT_PAGE_LABEL, 0);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        ivOval = view.findViewById(R.id.image_rect_oval);
        ibCreateNotification = view.findViewById(R.id.btn_create_notification);
        ibCreateNotification.setOnClickListener(this);
        ibAddFragment = view.findViewById(R.id.btn_plus);
        ibAddFragment.bringToFront();
        ibAddFragment.setOnClickListener(this);
        ibDeleteFragment = view.findViewById(R.id.btn_minus);
        ibDeleteFragment.bringToFront();
        ibDeleteFragment.setOnClickListener(this);
        tvFragmentNumber = view.findViewById(R.id.fragment_number);
        tvFragmentNumber.setText(String.valueOf(label+1));
        tvCreateNotification = view.findViewById(R.id.tv_create_notification);

        if (fragmentId == 0) {
            ibDeleteFragment.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_create_notification:
                presenter.createNotification();
                break;
            case R.id.btn_plus:
                presenter.createFragment();
                break;
            case R.id.btn_minus:
                presenter.removeFragment();
                break;
        }
    }
}
