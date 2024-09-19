package com.rakbank.userservice.service.model;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedResponse<T> {

    private Long totalRecords;
    private Integer totalPages;
    private Integer pageNumber;
    private Integer pageSize;
    private List<T> records;

}
