package com.magizdev.gobbler;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

public class SDCardUtil {

	static final String IMAGE_FILE_NAME = "temp.png";

	static boolean isSDCardWriteable() {
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageWriteable = true;
		}

		return mExternalStorageWriteable;
	}

	private Context context;

	public SDCardUtil(Context context) {
		this.context = context;
	}

	public String getImageFileFullName() {
		String path = context.getExternalFilesDir(null).getAbsolutePath();
		File file = new File(path, IMAGE_FILE_NAME);
		return file.getPath();
	}

	public boolean saveBitmap(Bitmap bitmap) {
		boolean success = false;
		File file = null;
		FileOutputStream outputStream = null;
		try {
			file = new File(getImageFileFullName());
			outputStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
			outputStream.close();
			success = true;
		} catch (Exception e) {
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e2) {
				}
			}
		}

		return success;

	}
}
