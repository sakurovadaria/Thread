package com.Thread.service;

import com.Thread.model.Avatar;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AvatarService {
    void uploadAvatar(Long Id, MultipartFile avatarFile) throws IOException;

    Avatar findAvatar(Long Id);

    Page<Avatar> getAllAvatars(int page, int size);
}

