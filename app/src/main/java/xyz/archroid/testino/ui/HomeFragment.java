package xyz.archroid.testino.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xyz.archroid.testino.Adapter.ExamsAdapter;
import xyz.archroid.testino.Data.GetExamsController;
import xyz.archroid.testino.Data.TestinoAPI;
import xyz.archroid.testino.Helper.PrefrenceManager;
import xyz.archroid.testino.Model.Exam;
import xyz.archroid.testino.R;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageView iv_profile;
    private TextView tv_welcome;

    private ImageView btn_addExam;

    private ExamsAdapter examsAdapter;

    private TestinoAPI.getExamsCallback getExamCallback;

    public HomeFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        iv_profile = view.findViewById(R.id.iv_profile);
        tv_welcome = view.findViewById(R.id.tv_welcome);
        btn_addExam = view.findViewById(R.id.btn_addExam);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        btn_addExam.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddExamActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


        getExamCallback = new TestinoAPI.getExamsCallback() {
            @Override
            public void onResponse(Boolean isSuccessful, List<Exam> exams, String error) {
                if (isSuccessful) {
                    if (exams != null) {
                        examsAdapter = new ExamsAdapter(exams, getContext());
                        recyclerView.setAdapter(examsAdapter);

                    }

                } else {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(String cause) {
                Toast.makeText(getContext(), cause, Toast.LENGTH_SHORT).show();

            }
        };

        GetExamsController getExamsController = new GetExamsController(getExamCallback);
        getExamsController.start(String.valueOf(PrefrenceManager.getInstance(getContext()).getUsername()));

        tv_welcome.setText(" " + PrefrenceManager.getInstance(getContext()).getUsername());


        return view;
    }
}