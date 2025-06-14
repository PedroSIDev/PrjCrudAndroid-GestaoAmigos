package com.example.prjcrud;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MaskTextWatcher implements TextWatcher {

    private final EditText editText;
    private final String mask;
    private boolean isUpdating;
    private String old = "";

    public MaskTextWatcher(EditText editText, String mask) {
        this.editText = editText;
        this.mask = mask;
    }

    public static String unmask(String s) {
        return s.replaceAll("[^0-9]*", "");
    }

    // --- NOVO: Método estático para aplicar a máscara a qualquer string ---
    public static String mask(String mask, String text) {
        String unmasked = unmask(text);
        StringBuilder masked = new StringBuilder();
        int i = 0;
        for (char m : mask.toCharArray()) {
            if (m != '#') {
                masked.append(m);
                continue;
            }
            try {
                masked.append(unmasked.charAt(i));
            } catch (Exception e) {
                break;
            }
            i++;
        }
        return masked.toString();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String str = MaskTextWatcher.unmask(s.toString());
        String mascara = "";
        if (isUpdating) {
            old = str;
            isUpdating = false;
            return;
        }
        int i = 0;
        for (char m : mask.toCharArray()) {
            if ((m != '#' && str.length() > old.length()) || (m != '#' && str.length() < old.length() && str.length() != i)) {
                mascara += m;
                continue;
            }
            try {
                mascara += str.charAt(i);
            } catch (Exception e) {
                break;
            }
            i++;
        }
        isUpdating = true;
        editText.setText(mascara);
        editText.setSelection(mascara.length());
    }

    @Override
    public void afterTextChanged(Editable s) {}
}
