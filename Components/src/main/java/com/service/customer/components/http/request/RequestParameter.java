package com.service.customer.components.http.request;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.service.customer.components.BuildConfig;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.http.CustomHttpClient;
import com.service.customer.components.http.listener.OnHttpRequestTaskListener;
import com.service.customer.components.http.model.FileWrapper;
import com.service.customer.components.http.model.Parameter;

import java.io.File;
import java.util.List;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RequestParameter {

    public final Headers.Builder builder = new Headers.Builder();
    private final List<Parameter> parameters = Lists.newArrayList();
    private final List<Parameter> files = Lists.newArrayList();

    public OnHttpRequestTaskListener onHttpRequestTaskListener;
    private String httpTaskKey;
    private RequestBody requestBody;
    private boolean isJsonFormat;
    private JSONObject jsonObject;
    private boolean isUrlEncode = true;
    public CacheControl cacheControl;
    private boolean isMultipart;

    public RequestParameter() {
        this(null);
    }

    public RequestParameter(OnHttpRequestTaskListener listener) {
        this.onHttpRequestTaskListener = listener;
        initialize();
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public RequestBody getRequestBody() {
        RequestBody body = null;
        if (isJsonFormat) {
            if (jsonObject == null) {
                JSONObject object = new JSONObject();
                for (Parameter parameter : parameters) {
                    object.put(parameter.getKey(), parameter.getValue());
                }
                body = RequestBody.create(MediaType.parse(Regex.JSON_TYPE.getRegext()), object.toJSONString());
            } else {
                body = RequestBody.create(MediaType.parse(Regex.JSON_TYPE.getRegext()), jsonObject.toJSONString());
            }
        } else if (requestBody != null) {
            body = requestBody;
        } else if (files != null && files.size() > 0) {
            boolean hasData = false;
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (Parameter parameter : parameters) {
                String key = parameter.getKey();
                String value = parameter.getValue();
                builder.addFormDataPart(key, value);
                hasData = true;
            }
            for (Parameter parameter : files) {
                String key = parameter.getKey();
                FileWrapper fileWrapper = parameter.getFileWrapper();
                if (fileWrapper != null) {
                    builder.addFormDataPart(key, fileWrapper.getFileName(), RequestBody.create(fileWrapper.getMediaType(), fileWrapper.getFile()));
                    hasData = true;
                }
            }
            if (hasData) {
                body = builder.build();
            }
        } else {
            if (isMultipart) {
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                for (Parameter parameter : parameters) {
                    String key = parameter.getKey();
                    String value = parameter.getValue();
                    builder.addFormDataPart(key, value);
                }
                body = builder.build();
            } else {
                FormBody.Builder builder = new FormBody.Builder();
                for (Parameter parameter : parameters) {
                    builder.add(parameter.getKey(), parameter.getValue());
                }
                body = builder.build();
            }
        }
        return body;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public void setRequestBody(MediaType mediaType, String string) {
        setRequestBody(RequestBody.create(mediaType, string));
    }

    public void setRequestBody(String mediaType, String string) {
        setRequestBody(MediaType.parse(mediaType), string);
    }

    public void setRequestBody(String string) {
        setRequestBody(MediaType.parse(Regex.STRING_TYPE.getRegext()), string);
    }

    public boolean isJsonFormat() {
        return isJsonFormat;
    }

    public void setJsonFormat(boolean jsonFormat) {
        isJsonFormat = jsonFormat;
    }

    public void setMultipart(boolean multipart) {
        isMultipart = multipart;
    }

    public boolean isUrlEncode() {
        return isUrlEncode;
    }

    public void setUrlEncode(boolean urlEncode) {
        isUrlEncode = urlEncode;
    }

    public void setJsonObject(JSONObject object) {
        isJsonFormat = true;
        this.jsonObject = object;
    }

    private void initialize() {
        builder.add(Regex.CHARSET.getRegext(), Regex.UTF_8.getRegext());
        List<Parameter> parameters = CustomHttpClient.getInstance().getParameters();
        if (parameters != null && parameters.size() > 0) {
            this.parameters.addAll(parameters);
        }

        Headers headers = CustomHttpClient.getInstance().getHeaders();
        if (headers != null && headers.size() > 0) {
            for (int i = 0; i < headers.size(); i++) {
                builder.add(headers.name(i), headers.value(i));
            }
        }

        if (onHttpRequestTaskListener != null) {
            httpTaskKey = onHttpRequestTaskListener.getHttpTaskKey();
        }
    }

    public String getHttpTaskKey() {
        return httpTaskKey;
    }

    /********************** Header *************************/

    public void addHeader(String line) {
        builder.add(line);
    }

    public void addHeader(String key, String value) {
        if (TextUtils.isEmpty(value)) {
            value = Regex.NONE.getRegext();
        }
        if (!TextUtils.isEmpty(key)) {
            builder.add(key, value);
        }
    }

    public void addHeader(String key, int value) {
        addHeader(key, String.valueOf(value));
    }

    public void addHeader(String key, long value) {
        addHeader(key, String.valueOf(value));
    }

    public void addHeader(String key, float value) {
        addHeader(key, String.valueOf(value));
    }

    public void addHeader(String key, double value) {
        addHeader(key, String.valueOf(value));
    }

    public void addHeader(String key, boolean value) {
        addHeader(key, String.valueOf(value));
    }

    /********************** Parameter *************************/

    public void addFormDataParameter(String key, String value) {
        if (TextUtils.isEmpty(value)) {
            value = Regex.NONE.getRegext();
        }
        Parameter part = new Parameter(key, value);
        if (!TextUtils.isEmpty(key) && !parameters.contains(part)) {
            parameters.add(part);
        }
    }

    public void addFormDataParameter(String key, long value) {
        addFormDataParameter(key, String.valueOf(value));
    }

    public void addFormDataParameter(String key, float value) {
        addFormDataParameter(key, String.valueOf(value));
    }

    public void addFormDataParameter(String key, double value) {
        addFormDataParameter(key, String.valueOf(value));
    }

    public void addFormDataParameter(String key, boolean value) {
        addFormDataParameter(key, String.valueOf(value));
    }

    public void addFormDataParameter(String key, File file, String contentType) {
        if (isFileAvalable(file)) {
            MediaType mediaType = MediaType.parse(contentType);
            addFormDataParameter(key, new FileWrapper(file, mediaType));
        }
    }

    public void addFormDataParameter(String key, File file, MediaType mediaType) {
        if (isFileAvalable(file)) {
            addFormDataParameter(key, new FileWrapper(file, mediaType));
        }
    }


    public void addFormDataParameter(String key, FileWrapper fileWrapper) {
        if (!TextUtils.isEmpty(key) && fileWrapper != null) {
            if (isFileAvalable(fileWrapper.getFile())) {
                files.add(new Parameter(key, fileWrapper));
            }
        }
    }

    public void addFormDataParameter(String key, File file) {
        if (isFileAvalable(file)) {
            if (file.getName().toLowerCase().lastIndexOf(Regex.PNG.getRegext()) > 0) {
                addFormDataParameter(key, file, Regex.IMAGE_PNG_TYPE1.getRegext());
            } else if (file.getName().toLowerCase().lastIndexOf(Regex.JPG.getRegext()) > 0 || file.getName().toLowerCase().lastIndexOf(Regex.JPEG.getRegext()) > 0) {
                addFormDataParameter(key, file, Regex.IMAGE_JPEG_TYPE.getRegext());
            } else {
                addFormDataParameter(key, new FileWrapper(file, null));
            }
        }
    }

    public void addFormDataParameter1(String key, List<File> files) {
        for (File file : files) {
            if (isFileAvalable(file)) {
                addFormDataParameter(key, file);
            }
        }
    }

    public void addFormDataParameter2(String key, List<FileWrapper> fileWrappers) {
        for (FileWrapper fileWrapper : fileWrappers) {
            addFormDataParameter(key, fileWrapper);
        }
    }

    public void addFormDataParameter(String key, List<File> files, MediaType mediaType) {
        for (File file : files) {
            if (isFileAvalable(file)) {
                addFormDataParameter(key, new FileWrapper(file, mediaType));
            }
        }
    }

    public void addFormDataParameters(List<Parameter> parameters) {
        this.parameters.addAll(parameters);
    }

    private boolean isFileAvalable(File file) {
        return !(file == null || !file.exists() || file.length() == 0);
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            StringBuilder result = new StringBuilder();
            if (isJsonFormat) {
                JSONObject object = new JSONObject();
                for (Parameter parameter : parameters) {
                    object.put(parameter.getKey(), parameter.getValue());
                }
                result.append(object.toJSONString());
            } else {
                for (Parameter parameter : parameters) {
                    if (result.length() > 0) {
                        result.append(Regex.AND.getRegext());
                    }
                    result.append(parameter.getKey());
                    result.append(Regex.EQUALS.getRegext());
                    result.append(parameter.getValue());
                }
            }
            for (Parameter parameter : files) {
                String key = parameter.getKey();
                if (result.length() > 0) {
                    result.append(Regex.AND.getRegext());
                }
                result.append(key);
                result.append(Regex.EQUALS.getRegext());
                result.append("FILE");
            }
            if (jsonObject != null) {
                result.append(jsonObject.toJSONString());
            }
            return result.toString();
        } else {
            return super.toString();
        }
    }
}
