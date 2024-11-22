package com.example.mymoo.domain.child.service;

import com.example.mymoo.domain.child.dto.request.ChildReqeustDTO;
import com.example.mymoo.domain.child.entity.Child;

public interface ChildService {
    Child createChild(ChildReqeustDTO request);
}
