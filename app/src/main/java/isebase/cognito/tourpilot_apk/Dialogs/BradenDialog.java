package isebase.cognito.tourpilot_apk.Dialogs;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Data.Answer.Answer;
import isebase.cognito.tourpilot_apk.Data.Employment.Employment;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BradenDialog extends DialogFragment{

	protected BaseDialogListener listener;
	
	private String answerName;
	private Answer answer;
	private int answerNumber;
	private Map<String, List<String>> subQuestions;
	private LinearLayout ll1;
	private LinearLayout ll2;
	private LinearLayout ll3;
	private List<CheckBox> checkBoxes;
	private List<LinearLayout> layouts;
	private LinearLayout currentLayout;
	private CheckBoxListener chbListener;
	private TextView tvOther1;
	private TextView tvOther2;
	private Employment newEmployment;
	public LinearLayout getCurrentLayout() {
		return currentLayout;
	}
	
	public BradenDialog(String answerName, Map<String, List<String>> subQuestions, Answer answer, int answerNumber) {
		this.answerName = answerName;
		this.subQuestions = subQuestions;
		this.answer = answer;
		this.answerNumber = answerNumber;
		newEmployment = HelperFactory.getHelper().getEmploymentDAO().loadAll((int)Option.Instance().getEmploymentID());

	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_braden, null);
		adb.setView(view);
		adb.setTitle(answerName);
		chbListener = new CheckBoxListener();
		initControls(view);
		fillUp();
		
		adb.setPositiveButton(StaticResources.getBaseContext().getString(R.string.ok),
				new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							listener.onDialogPositiveClick(BradenDialog.this);
						}
					});
		adb.setNegativeButton(StaticResources.getBaseContext().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						listener.onDialogNegativeClick(BradenDialog.this);
					}
				});		
		return adb.create();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (BaseDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement BaseDialogListener");
		}
	}
	
	private void initControls(View view) {
		ll1 = (LinearLayout) view.findViewById(R.id.linear_layout_able_give_data);
		ll2 = (LinearLayout) view.findViewById(R.id.linear_layout_has_any_pain);
		ll3 = (LinearLayout) view.findViewById(R.id.linear_layout_test_1);
		layouts = new ArrayList<LinearLayout>();
		layouts.add(ll1);
		layouts.add(ll2);
		layouts.add(ll3);
		tvOther1 = new TextView(StaticResources.getBaseContext());
		tvOther1.setText(StaticResources.getBaseContext().getString(R.string.oder));
		tvOther1.setTextColor(Color.BLACK);
		tvOther2 = new TextView(StaticResources.getBaseContext());
		tvOther2.setText(StaticResources.getBaseContext().getString(R.string.oder));
		tvOther2.setTextColor(Color.BLACK);
		checkBoxes = new ArrayList<CheckBox>();
		for (int i = 0; i < 7; i++)
			checkBoxes.add((new CheckBox(StaticResources.getBaseContext())));
	}
	
	private void fillUp() {
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_0_0)))
			fillUpDialog(1,1,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_0_1)))
			fillUpDialog(2,1,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_0_2)))
			fillUpDialog(2,1,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_0_3)))
			fillUpDialog(1,1,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_1_0)))
			fillUpDialog(2,0,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_1_1)))
			fillUpDialog(2,0,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_1_2)))
			fillUpDialog(1,0,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_1_3)))
			fillUpDialog(2,0,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_2_0)))
			fillUpDialog(1,0,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_2_1)))
			fillUpDialog(3,0,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_2_2)))
			fillUpDialog(3,0,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_2_3)))
			fillUpDialog(2,0,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_3_0)))
			fillUpDialog(1,0,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_3_1)))
			fillUpDialog(2,0,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_3_2)))
			fillUpDialog(1,0,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_3_3)))
			fillUpDialog(1,0,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_4_0)))
			fillUpDialog(4,1,2);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_4_1)))
			fillUpDialog(3,1,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_4_2)))
			fillUpDialog(3,1,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_4_3)))
			fillUpDialog(4,0,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_5_0)))
			fillUpDialog(5,0,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_5_1)))
			fillUpDialog(4,0,0);
		if (answerName.equals(StaticResources.getBaseContext().getString(R.string.braden_answer_5_2)))
			fillUpDialog(3,0,0);
	}
	
	private void fillUpDialog(int firstCount, int secondCount, int thirdCount) {
		int count = 0;
		count = fillUpCheckBoxes(firstCount, count, ll1);
		if (secondCount == 0)
			return;
		ll2.addView(tvOther1);
		count = fillUpCheckBoxes(secondCount, count, ll2);
		if (thirdCount == 0)
			return;
		ll3.addView(tvOther2);
		count = fillUpCheckBoxes(thirdCount, count, ll3);
	}
	
	private int fillUpCheckBoxes(int checkBoxesCount, int totalCount, LinearLayout linearLayout) {
		for (int i = 0; i < checkBoxesCount; i++) {
			checkBoxes.get(totalCount).setText(subQuestions.get(answerName).get(totalCount));
			checkBoxes.get(totalCount).setGravity(Gravity.FILL);
			checkBoxes.get(totalCount).setOnCheckedChangeListener(chbListener);
			checkBoxes.get(totalCount).setTextColor(Color.BLACK);
			checkBoxes.get(totalCount).setHintTextColor(Color.BLACK);

			ColorStateList colorStateList = new ColorStateList(
					new int[][]{
							new int[]{-android.R.attr.state_checked}, // unchecked
							new int[]{android.R.attr.state_checked} , // checked
					},
					new int[] {

							Color.BLACK //disabled
							,Color.GREEN //enabled

					}
			);
			checkBoxes.get(totalCount).setButtonTintList(colorStateList);

			if (answer != null && ((RelativeLayout)linearLayout.getParent()).getChildAt(answer.getBradenLevel()).equals(linearLayout) && answerNumber == answer.getBradenAnswer())
				for (int j = 0; j < answer.getBradenCheckedIndexes().length; j++)
					if (i == answer.getBradenCheckedIndexes()[j])
						checkBoxes.get(totalCount).setChecked(true);
			checkBoxes.get(totalCount).setEnabled(!newEmployment.isDone());
			linearLayout.addView(checkBoxes.get(totalCount));
			totalCount++;
		}
		return totalCount;
	}
	
	public class CheckBoxListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (!isChecked)
				return;
			currentLayout = (LinearLayout) buttonView.getParent();
			clearAnotherLevelCheckBoxes(currentLayout != null ? currentLayout : answer.getBradenLevel() == 0 ? ll1 : answer.getBradenLevel() == 1 ? ll2 : ll3);			
		}		
	}
	
	private void clearAnotherLevelCheckBoxes(LinearLayout checkedLinearLayout) {
		for (LinearLayout layout : layouts)
			if (!layout.equals(checkedLinearLayout) && layout.getChildCount() > 0)
				for(int i = 0; i < layout.getChildCount(); i++)
					if (layout.getChildAt(i) instanceof CheckBox)
						((CheckBox)layout.getChildAt(i)).setChecked(false);
	}
}
