package com.park.api.web.dto.mapper;

import com.park.api.web.dto.PageableDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableMapper {

    public static PageableDTO toDTO(Page page){
        return new ModelMapper().map(page, PageableDTO.class);
    }
}
