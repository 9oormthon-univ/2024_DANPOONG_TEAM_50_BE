package com.example.mymoo.domain.store.dto.api;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "head")
public class Head {
    private String list_total_count;
    private String RESULT;

}
