package tongji.edu.util;

import tongji.edu.activity.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 
 * @author zlj
 *
 */
public class MyDialogFactory {

	public static Dialog creatRequestDialog(final Context context, String tip) {

		final Dialog dialog = new Dialog(context, R.style.dialog);
		dialog.setContentView(R.layout.dialog_layout);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		int width = Utils.getScreenWidth(context);
		lp.width = (int) (0.6 * width);

		TextView titleTxtv = (TextView) dialog.findViewById(R.id.tvLoad);
		if (tip == null || tip.length() == 0) {
			titleTxtv.setText("正在发送请求");
		} else {
			titleTxtv.setText(tip);
		}

		return dialog;
	}

	
	public static void toastDialog(Context context, String title, String msg) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
				.setPositiveButton("确定", null).create().show();
	}

	public static void toastNetWorkStatus(final Context context) {
		new AlertDialog.Builder(context)
				.setTitle("提示")
				.setMessage("网络连接未打开")
				.setPositiveButton("前往打开",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										android.provider.Settings.ACTION_WIRELESS_SETTINGS);
								context.startActivity(intent);
							}
						}).setNegativeButton("ȡ��", null).create().show();
	}
	
	
}
