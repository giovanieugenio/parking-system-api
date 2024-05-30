package com.park.api.repositories.projection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ClientProjection {

    Long getId();
    String getName();
    String getCpf();

}
