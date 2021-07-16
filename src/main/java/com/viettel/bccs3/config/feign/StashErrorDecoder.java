package com.viettel.bccs3.config.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

import static feign.FeignException.errorStatus;

@Slf4j
public class StashErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 400) {
            try {
                var error = IOUtils.toString(response.body().asReader());
                return new IllegalArgumentException(error);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } else if (response.status() == 500) {
            try {
                return new Exception(IOUtils.toString(response.body().asReader()));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return errorStatus(methodKey, response);
    }
}
