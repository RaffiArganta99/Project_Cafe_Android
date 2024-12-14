package com.example.Bonanza.utilitas;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    public static String formatToRupiah(double value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return format.format(value).replace("Rp", "Rp").replace(",00", "");
    }
}
