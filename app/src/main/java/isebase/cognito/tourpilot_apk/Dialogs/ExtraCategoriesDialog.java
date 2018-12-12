package isebase.cognito.tourpilot_apk.Dialogs;

import isebase.cognito.tourpilot_apk.Data.Category.Category;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;

public class ExtraCategoriesDialog extends BaseDialog {

private List<Category> Categories;
	private String[] CategoriesArr;
	private boolean[] selectedCategories;

	private String title;
	
	public ExtraCategoriesDialog(List<Category> categories, String title) {
		this.Categories = categories;
		this.title = title;
		initCategoriesArr();
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setMultiChoiceItems(CategoriesArr, selectedCategories, new OnMultiChoiceClickListener() {
			
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						selectedCategories[which] = isChecked;
					}
			
				})
				.setPositiveButton(isebase.cognito.tourpilot_apk.R.string.ok, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int id) {
						listener.onDialogPositiveClick(ExtraCategoriesDialog.this);
					}
					
				})
				.setNegativeButton(isebase.cognito.tourpilot_apk.R.string.cancel, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int id) {
						listener.onDialogNegativeClick(ExtraCategoriesDialog.this);
					}
					
				});
		return adb.create();
	}
		
	private void initCategoriesArr() {
		CategoriesArr = new String[Categories.size()];
		int counter = 0;
		for (Category patient : Categories)
			CategoriesArr[counter++] = patient.getName(); 
		selectedCategories = new boolean[CategoriesArr.length];
	}
	
	public String getSelectedCategoriesID() {
		String CategoriesID = "";
		for (int i = 0; i < selectedCategories.length; i++)
			if (selectedCategories[i])
				CategoriesID += (CategoriesID.equals("") ? "" : ",") + Categories.get(i).getId();
		return CategoriesID;
	}

}
