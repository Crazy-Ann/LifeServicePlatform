package com.service.customer.net;

import com.service.customer.base.net.BaseRequest;

public class Request extends BaseRequest {

    private static Request request;

    private Request() {
        // cannot be instantiated
    }

    public static synchronized Request getInstance() {
        if (request == null) {
            request = new Request();
        }
        return request;
    }

    public static void releaseInstance() {
        if (request != null) {
            request = null;
        }
    }

//    public RequestParameter generateRequestParameters(String method, String bizContent,  File file) {
//        RequestParameter parameter = new RequestParameter();
//        parameter.setRequestBody(new MultipartBody.Builder()
//                                         .setType(MultipartBody.FORM)
//                                         .addPart(Headers.of("Content-Disposition", "form-data; name=\"" + ParameterKey.Credentials.JSON_DATA + "\""),
//                                                  RequestBody.create(null, SecurityUtil.encryptAES(
//                                                          generateRequestParameters(method, bizContent, mchUid, DeviceUtil.getInstance().getDeviceId(BaseApplication.getInstance()), DeviceUtil.getInstance().getDeviceInfo(BaseApplication.getInstance(), false, BuildConfig.VERSION_CODE), String.valueOf(BuildConfig.VERSION_CODE), new SimpleDateFormat(Regex.DATE.getRegext(), Locale.getDefault()).format(new Date(System.currentTimeMillis()))).toString()
//                                                          , BaseApplication.getInstance().getEncryptKey(), isEncrypt)))
//                                         .addPart(Headers.of("Content-Disposition", "form-data; name=\"" + ParameterKey.Credentials.ZIP_DATA + "\""),
//                                                  RequestBody.create(MediaType.parse("image/png"), file))
//                                         .build());
//        return parameter;
//    }
}
