package com.service.customer.components.widget.zxing.listener;

import android.graphics.Bitmap;

import com.google.zxing.Result;


/**
 * Created by hupei on 2016/7/1.
 */
public interface OnScannerCompletionListener {
    /**
     * 扫描成功后将调用
     * <pre>
     *     ParsedResultType mediaType = parsedResult.getType();
     *     switch (mediaType) {
     *         case ADDRESSBOOK:
     *             AddressBookParsedResult addressResult = (AddressBookParsedResult) parsedResult;
     *         break;
     *         case URI:
     *              URIParsedResult uriParsedResult = (URIParsedResult) parsedResult;
     *         break;
     *     }
     * </pre>
     *
     * @param rawResult    扫描结果
     * @param barcode      位图
     */
    void OnScannerCompletion(Result rawResult, Bitmap barcode);
}
