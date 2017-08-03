package com.max.utils;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class IntentUtils {

	/**
	 * 判断intent和它的bundle是否为空
	 *
	 * @param intent
	 * @return
	 */
	public static boolean isBundleEmpty(Intent intent) {
		return (intent == null) && (intent.getExtras() == null);
	}

	public static Intent buildAppInstallIntent(String filePath) {
		File file = new File(filePath);
		return buildAppInstallIntent(file);
	}

	public static Intent buildAppInstallIntent(File file) {
		return buildAppInstallIntent(Uri.fromFile(file));
	}

	public static Intent buildAppInstallIntent(Uri uri) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

	public static Intent buildMarketLinkIntent(String packageName) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=" + packageName));
		return intent;
	}

	/**
	 * 创建浏览器Intent
	 * @param url
	 * @return
	 */
	public static Intent buildBrowserLinkIntent(String url) {
		return new Intent("android.intent.action.VIEW", Uri.parse(url));
	}
}
