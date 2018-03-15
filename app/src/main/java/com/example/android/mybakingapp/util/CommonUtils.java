package com.example.android.mybakingapp.util;


import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class CommonUtils {
    private String mMessage;

    public String getMessage() {
        return mMessage;
    }

    public static CommonUtils parseError(Response<?> response) {
        Converter<ResponseBody, CommonUtils> converter = Client.
                retrofit.responseBodyConverter(CommonUtils.class, new Annotation[0]);

        CommonUtils error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new CommonUtils();
        }
        return error;
    }
}
