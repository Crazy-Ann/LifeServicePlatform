package com.service.customer.net;

import com.service.customer.base.net.BaseRequest;
import com.service.customer.components.http.request.RequestParameter;

import java.util.HashMap;

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

    public RequestParameter generateRequestParameters(String type, HashMap<String, String> bizContent, String token, boolean isJson) {
        return formatParameters(generateRequestParameters(type, bizContent, token), isJson);
    }

//    public RequestParameter generateRequestParameters(String type, String token, File file) {
//        RequestParameter parameter = new RequestParameter();
//        parameter.setRequestBody(new MultipartBody.Builder()
//                                         .setType(MultipartBody.FORM)
//                                         .addPart(Headers.of("Content-Disposition", "form-data; name=\"" + RequestParameterKey.FORM_DATA + "\""), generateRequestParameters())
//                                         .addPart(Headers.of("Content-Disposition", "form-data; name=\"" + RequestParameterKey.FILE_DATA + "\""), RequestBody.create(MediaType.parse(Regex.IMAGE_PNG_TYPE2.getRegext()), file))
//                                         .build());
//        return parameter;
//    }
}
