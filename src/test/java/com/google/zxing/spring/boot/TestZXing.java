package com.google.zxing.spring.boot;
/*package com.jeekit.zxing;

import java.io.IOException;
import java.util.Hashtable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class TestZXing extends Activity {
	// ===========================================================
	// Constants
	// ===========================================================
	*//** ��ɶ�ά��ͼƬ��С *//*
	private static final int QRCODE_SIZE = 300;
	*//** ͷ��ͼƬ��С *//*
	private static final int PORTRAIT_SIZE = 55;

	// ===========================================================
	// Fields
	// ===========================================================
	*//** ͷ��ͼƬ *//*
	private Bitmap portrait;

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// ������ʾ���ά���view
		ImageView image1 = (ImageView) findViewById(R.id.image1);
		image1.setImageBitmap(createQRCodeBitmap());

		// ������ʾ��ͷ��Ķ�ά���view
		ImageView image2 = (ImageView) findViewById(R.id.image2);
		// ��ʼ��ͷ��
		portrait = initProtrait("abc.jpg");
		// ������ά��
		Bitmap qr = createQRCodeBitmap();
		createQRCodeBitmapWithPortrait(qr, portrait);
		image2.setImageBitmap(qr);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	*//**
	 * ��ʼ��ͷ��ͼƬ
	 *//*
	private Bitmap initProtrait(String url) {
		try {
			// ������ô�asset�м���ͼƬabc.jpg
			Bitmap portrait = BitmapFactory.decodeStream(getAssets().open(url));

			// ��ԭ��ͼƬѹ����ʾ��С
			Matrix mMatrix = new Matrix();
			float width = portrait.getWidth();
			float height = portrait.getHeight();
			mMatrix.setScale(PORTRAIT_SIZE / width, PORTRAIT_SIZE / height);
			return Bitmap.createBitmap(portrait, 0, 0, (int) width,
					(int) height, mMatrix, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	*//**
	 * ����QR��ά��ͼƬ
	 *//*
	private Bitmap createQRCodeBitmap() {
		// ��������QR��ά�����
		Hashtable<EncodeHintType, Object> qrParam = new Hashtable<EncodeHintType, Object>();
		// ����QR��ά��ľ��?�𡪡�����ѡ�����H����
		qrParam.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// ���ñ��뷽ʽ
		qrParam.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		// �趨��ά����������ݣ������Ҳ�����΢���ĵ�ַ
		String content = "sinaweibo://userinfo?uid=2568190010";

		// ���QR��ά����ݡ�������ֻ�ǵõ�һ����true��false��ɵ�����
		// ����˳��ֱ�Ϊ���������ݣ��������ͣ����ͼƬ��ȣ����ͼƬ�߶ȣ����ò���
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
					BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, qrParam);

			// ��ʼ���ö�ά����ݴ���BitmapͼƬ���ֱ���Ϊ�ڰ���ɫ
			int w = bitMatrix.getWidth();
			int h = bitMatrix.getHeight();
			int[] data = new int[w * h];

			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					if (bitMatrix.get(x, y))
						data[y * w + x] = 0xff000000;// ��ɫ
					else
						data[y * w + x] = -1;// -1 �൱��0xffffffff ��ɫ
				}
			}

			// ����һ��bitmapͼƬ��������ߵ�ͼƬЧ��ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			// ������Ķ�ά����ɫ���鴫�룬���ͼƬ��ɫ
			bitmap.setPixels(data, 0, w, 0, 0, w, h);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	*//**
	 * �ڶ�ά���ϻ���ͷ��
	 *//*
	private void createQRCodeBitmapWithPortrait(Bitmap qr, Bitmap portrait) {
		// ͷ��ͼƬ�Ĵ�С
		int portrait_W = portrait.getWidth();
		int portrait_H = portrait.getHeight();

		// ����ͷ��Ҫ��ʾ��λ�ã���������ʾ
		int left = (QRCODE_SIZE - portrait_W) / 2;
		int top = (QRCODE_SIZE - portrait_H) / 2;
		int right = left + portrait_W;
		int bottom = top + portrait_H;
		Rect rect1 = new Rect(left, top, right, bottom);

		// ȡ��qr��ά��ͼƬ�ϵĻ��ʣ���Ҫ�ڶ�ά��ͼƬ�ϻ������ǵ�ͷ��
		Canvas canvas = new Canvas(qr);

		// ��������Ҫ���Ƶķ�Χ��С��Ҳ����ͷ��Ĵ�С��Χ
		Rect rect2 = new Rect(0, 0, portrait_W, portrait_H);
		// ��ʼ����
		canvas.drawBitmap(portrait, rect2, rect1, null);
	}
}
*/