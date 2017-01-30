package moe.haruue.walkee.ui.mode;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.Window;

import moe.haruue.walkee.R;
import moe.haruue.walkee.config.Const;
import moe.haruue.walkee.util.SPUtils;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class TimePreferenceFragment extends PreferenceFragmentCompat {

    private SwitchPreferenceCompat customizeTimePreference;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_time);
        customizeTimePreference = (SwitchPreferenceCompat) findPreference(getString(R.string.pref_key_customize_time_switch));
        reloadTimePreference();
        refreshSummaryOn();
        customizeTimePreference.setOnPreferenceChangeListener((preference, newValue) -> {
            if (customizeTimePreference.isEnabled()) {
                customizeTimePreference.setEnabled(false);
                if (!customizeTimePreference.isChecked()) {
                    showAskStartTimeDialog();
                } else {
                    customizeTimePreference.setEnabled(true);
                }
            }
            return true;
        });
    }

    public void reloadTimePreference() {
        long startTimeMillis = (long) SPUtils.get(getContext(), Const.SPKEY_CUSTOMIZE_TIME_START, 0L);
        long endTimeMillis = (long) SPUtils.get(getContext(), Const.SPKEY_CUSTOMIZE_TIME_END, 0L);
        startHour = calcHour(startTimeMillis);
        startMinute = calcMinute(startTimeMillis);
        endHour = calcHour(endTimeMillis);
        endMinute = calcMinute(endTimeMillis);
    }

    public void refreshSummaryOn() {
        customizeTimePreference.setSummaryOn(getString(
                R.string.format_summary_customize_time_on,
                addPreZero(startHour) + ":" + addPreZero(startMinute),
                addPreZero(endHour) + ":" + addPreZero(endMinute)));
    }

    public void showAskStartTimeDialog() {
        TimePickerDialog dialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute) -> {
                    startHour = hourOfDay;
                    startMinute = minute;
                    showAskEndTimeDialog();
                }, startHour, startMinute, true);
        dialog.setMessage(getString(R.string.title_customize_time_start));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setOnCancelListener(d -> onTimePreferenceDialogCancel());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showAskEndTimeDialog() {
        TimePickerDialog dialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute) -> {
                    endHour = hourOfDay;
                    endMinute = minute;
                    saveCustomizeTime();
                }, endHour, endMinute, true);
        dialog.setMessage(getString(R.string.title_customize_time_end));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setOnCancelListener(d -> onTimePreferenceDialogCancel());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void saveCustomizeTime() {
        long startTimeMillis = calcTimeMillis(startHour, startMinute);
        long endTimeMillis = calcTimeMillis(endHour, endMinute);
        SPUtils.set(getContext(), Const.SPKEY_CUSTOMIZE_TIME_START, startTimeMillis);
        SPUtils.set(getContext(), Const.SPKEY_CUSTOMIZE_TIME_END, endTimeMillis);
        customizeTimePreference.setChecked(true);
        reloadTimePreference();
        refreshSummaryOn();
        customizeTimePreference.setEnabled(true);
    }

    public static long calcTimeMillis(int hour, int minute) {
        return hour * 3_600_000 + minute * 60_000;
    }

    public static int calcHour(long timeMillis) {
        return (int) (timeMillis / 3_600_000);
    }

    public static int calcMinute(long timeMillis) {
        return (int) ((timeMillis - calcHour(timeMillis) * 3_600_000) / 60_000);
    }

    public static String addPreZero(int number) {
        if (number < 10) return "0" + number;
        return "" + number;
    }

    public void onTimePreferenceDialogCancel() {
        reloadTimePreference();
        customizeTimePreference.setChecked(false);
        customizeTimePreference.setEnabled(true);
    }

}
