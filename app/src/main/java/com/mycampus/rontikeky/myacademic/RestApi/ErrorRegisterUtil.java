package com.mycampus.rontikeky.myacademic.RestApi;

import com.mycampus.rontikeky.myacademic.Response.PresenceResponse;
import com.mycampus.rontikeky.myacademic.Response.PresenceUserResponse;
import com.mycampus.rontikeky.myacademic.ResponseError.RegisterUserErrorResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Anggit on 29/09/2017.
 */

public class ErrorRegisterUtil {

    public static RegisterUserErrorResponse parseError(Response<?> response) {
            Converter<ResponseBody, RegisterUserErrorResponse> converter =
                    ServiceGenerator.retrofit()
                            .responseBodyConverter(RegisterUserErrorResponse.class, new Annotation[0]);

            RegisterUserErrorResponse error;

            try {
                error = converter.convert(response.errorBody());
            } catch (IOException e) {
                return new RegisterUserErrorResponse();
            }

            return error;

    }

    public static PresenceUserResponse parseErrorPresence(Response<?> response) {
        Converter<ResponseBody, PresenceUserResponse> converter =
                ServiceGenerator.retrofit()
                        .responseBodyConverter(PresenceUserResponse.class, new Annotation[0]);

        PresenceUserResponse error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new PresenceUserResponse();
        }

        return error;

    }

}
