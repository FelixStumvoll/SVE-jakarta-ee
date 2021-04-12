package org.urlshortener.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private List<String> errors;

    public static ErrorResponse of(String... errors) {
        var res = new ErrorResponse();
        res.setErrors(Arrays.asList(errors));
        return res;
    }
}
