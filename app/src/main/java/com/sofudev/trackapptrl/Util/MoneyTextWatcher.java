package com.sofudev.trackapptrl.Util;

import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class MoneyTextWatcher implements TextWatcher {
    private static final Locale locale = new Locale("id", "ID");
    private static final DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
    private final WeakReference<EditText> editTextWeakReference;

    public MoneyTextWatcher(EditText editText) {
        editTextWeakReference = new WeakReference<>(editText);
        formatter.setMaximumFractionDigits(2);

        DecimalFormatSymbols symbol = new DecimalFormatSymbols(locale);
        symbol.setCurrencySymbol(symbol.getCurrencySymbol() + " ");
        formatter.setDecimalFormatSymbols(symbol);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void afterTextChanged(Editable editable) {
        EditText editText = editTextWeakReference.get();
        if (editText == null || editText.getText().toString().isEmpty()) {
            return;
        }
        editText.removeTextChangedListener(this);

//        BigDecimal parsed = parseCurrencyValue(editText.getText().toString());
        BigDecimal parsed = parseCurrencyValue(editText.getText().toString().replace(",", "."));
        String formatted = formatter.format(parsed);

        editText.setText(formatted);
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static BigDecimal parseCurrencyValue(String value) {
        try {

            String replaceRegex = String.format("[%s,.\\s]",
                    Objects.requireNonNull(formatter.getCurrency()).getSymbol(locale));
            String currencyValue = value.replaceAll(replaceRegex, "");
            return new BigDecimal(currencyValue);

//            String replaceRegex = "";

//            replaceRegex = String.format("^[0-9]*\\,[0-9][0-9]$",
//                        Objects.requireNonNull(formatter.getCurrency()).getSymbol(locale));

//            String currencyValue = value.replaceAll(replaceRegex, "");
//            return new BigDecimal(currencyValue);
        } catch (Exception e) {
            Log.e("App", e.getMessage(), e);
        }
        return BigDecimal.valueOf(0);
    }
}
