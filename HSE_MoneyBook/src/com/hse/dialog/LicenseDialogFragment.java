package com.hse.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.hse.moneybook.R;

public class LicenseDialogFragment extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity());
		// タイトル非表示
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		// フルスクリーン
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		dialog.setContentView(R.layout.fragment_dialog_license);
		return dialog;
	}
}
