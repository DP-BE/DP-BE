package com.jambit.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchDto {
    private String type;
    private String payload;
}
