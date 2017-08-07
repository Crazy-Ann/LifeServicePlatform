package com.service.customer.net;

import com.service.customer.base.net.BaseRequest;
import com.service.customer.components.http.model.FileWrapper;
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
        return generateRequestParameters(type, bizContent, null, token, isJson);
    }

    public RequestParameter generateRequestParameters(String type, HashMap<String, String> bizContent, HashMap<String, FileWrapper> files, String token, boolean isJson) {
        return formatParameters(generateRequestParameters(type, bizContent, token), files, isJson);
    }
}
