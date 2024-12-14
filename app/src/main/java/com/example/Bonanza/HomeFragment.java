package com.example.Bonanza;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Bonanza.adapter.PopulerAdapter;

import java.util.ArrayList;
import java.util.List;

public class  HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PopulerAdapter populerAdapter;
    private List<String> itemNames;
    private List<Integer> itemImages;
    private List<Integer> itemSold;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inisialisasi RecyclerView
        recyclerView = view.findViewById(R.id.PopulerRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Mempersiapkan data
        prepareData();

        // Inisialisasi adapter dan set ke RecyclerView
        populerAdapter = new PopulerAdapter(getContext(), itemNames, itemImages, itemSold);
        recyclerView.setAdapter(populerAdapter);

        return view;
    }

    private void prepareData() {
        itemNames = new ArrayList<>();
        itemImages = new ArrayList<>();
        itemSold = new ArrayList<>();

        // Tambahkan data produk (Nama, Gambar, Jumlah Terjual)
        itemNames.add("Coffee");
        itemImages.add(R.drawable.menu1); // Ganti dengan ID gambar yang sesuai
        itemSold.add(50);

        itemNames.add("Tea");
        itemImages.add(R.drawable.menu2); // Ganti dengan ID gambar yang sesuai
        itemSold.add(30);

        itemNames.add("Milk");
        itemImages.add(R.drawable.menu3); // Ganti dengan ID gambar yang sesuai
        itemSold.add(40);

        itemNames.add("Water");
        itemImages.add(R.drawable.menu4); // Ganti dengan ID gambar yang sesuai
        itemSold.add(40);
    }
}
