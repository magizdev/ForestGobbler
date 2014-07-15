package com.magizdev.babyoneday.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.activeandroid.serializer.TypeSerializer;

public final class ImageSerializer extends TypeSerializer {
	public Class<?> getDeserializedType() {
		return Bitmap.class;
	}

	public Class<?> getSerializedType() {
		return byte[].class;
	}

	public byte[] serialize(Object data) {
		if (data == null) {
			return null;
		}
		
		ByteArrayOutputStream bao = new ByteArrayOutputStream();

		((Bitmap) data).compress(Bitmap.CompressFormat.PNG, 100, bao);
		return bao.toByteArray();
	}

	public Bitmap deserialize(Object data) {
		if (data == null) {
			return null;
		}
		byte[] datab = (byte[])data;
		return BitmapFactory.decodeByteArray(datab, 0, datab.length);
	}
}