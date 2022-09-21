package com.edu.ulab.app.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ListUserBookResponses {
    private List<UserBookResponse> userBookResponses;
}
