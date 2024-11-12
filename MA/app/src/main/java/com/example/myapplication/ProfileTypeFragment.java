package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.myapplication.EventPlannerActivityMandatory;
import com.example.myapplication.OrgActivityMandatory;
import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;

public class ProfileTypeFragment extends Fragment {

    private RadioGroup profileTypeGroup;
    private Button nextButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_type_fragment, container, false);

        profileTypeGroup = view.findViewById(R.id.profile_type_group);
        nextButton = view.findViewById(R.id.button_next);

        nextButton.setOnClickListener(v -> {
            int selectedProfileId = profileTypeGroup.getCheckedRadioButtonId();
            if (selectedProfileId == R.id.radio_event_planner) {
                Intent intent = new Intent(getActivity(), EventPlannerActivityMandatory.class);
                startActivity(intent);
            } else if (selectedProfileId == R.id.radio_product_seller) {
                Intent intent = new Intent(getActivity(), OrgActivityMandatory.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
