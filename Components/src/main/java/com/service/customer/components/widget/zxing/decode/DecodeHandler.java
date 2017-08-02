package com.service.customer.components.widget.zxing.decode;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.service.customer.components.R;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.widget.zxing.camera.CameraManager;
import com.service.customer.components.widget.zxing.camera.PlanarYUVLuminanceSource;
import com.service.customer.components.widget.zxing.listener.OnDecodeHandlerListener;

import java.util.Hashtable;

final class DecodeHandler extends Handler {

    private final OnDecodeHandlerListener mListener;
    private final MultiFormatReader multiFormatReader;

    DecodeHandler(OnDecodeHandlerListener listener, Hashtable<DecodeHintType, Object> hints) {
        this.mListener = listener;
        multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);
    }


    @Override
    public void handleMessage(Message message) {
        if (message.what == R.id.decode) {
            decode((byte[]) message.obj, message.arg1, message.arg2);
        }
        if (message.what == R.id.quit) {
            Looper.myLooper().quit();
        }
    }

    private void decode(byte[] data, int width, int height) {
        long start = System.currentTimeMillis();
        Result rawResult = null;

        // modify here
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                rotatedData[x * height + height - y - 1] = data[x + y * width];
        }
        int tmp = width; // Here we are swapping, that's the difference to #11
        width = height;
        height = tmp;
        PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(rotatedData, width, height);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            rawResult = multiFormatReader.decodeWithState(bitmap);
        } catch (ReaderException re) {
            // continue
        } finally {
            multiFormatReader.reset();
        }

        if (rawResult != null) {
            long end = System.currentTimeMillis();
            LogUtil.getInstance().print("Found barcode (" + (end - start) + " ms):\n" + rawResult.toString());
            Message message = Message.obtain(mListener.getHandler(), R.id.decode_succeeded, rawResult);
            Bundle bundle = new Bundle();
            bundle.putParcelable(DecodeThread.BARCODE_BITMAP, source.renderCroppedGreyscaleBitmap());
            message.setData(bundle);
            message.sendToTarget();
        } else {
            Message message = Message.obtain(mListener.getHandler(), R.id.decode_failed);
            message.sendToTarget();
        }
    }

}
