package com.example.coffeeshopapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coffeeshopapplication.adapter.PopulerAdapter;
import com.example.coffeeshopapplication.databinding.FragmentHomeBinding;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    // Data list item, jumlah terjual, dan gambar
    private List<String> itemList = Arrays.asList("Burger", "Sandwich", "Momo", "Pizza");
    private List<Integer> soldList = Arrays.asList(120, 85, 190, 150); // Jumlah terjual
    private List<Integer> imageList = Arrays.asList(R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4);

    private FragmentHomeBinding binding; // Binding untuk layout fragment_home

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialization atau pengaturan lain bisa dilakukan di sini
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout menggunakan ViewBinding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Membuat instance adapter dengan data item, jumlah terjual, dan gambar
        PopulerAdapter adapter = new PopulerAdapter(itemList, soldList, imageList);

        // Mengatur RecyclerView menggunakan LinearLayoutManager
        binding.PopulerRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Menghubungkan adapter dengan RecyclerView
        binding.PopulerRecycleView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Menghapus binding saat view dihancurkan
        binding = null;
    }
}
