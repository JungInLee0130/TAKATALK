package com.example.chat.global.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    // 실무에서는 S3로 사용. 우선은 로컬 url사용.
    private final FileStorageProperties fileProperties;

    /*
    * 파일을 저장하고 저장된 고유 파일명을 반환합니다.
    * */
    public String storeFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        log.info("uploadDir : {}", fileProperties.getUploadPath());

        String originalFilename = file.getOriginalFilename();
        // 사용자들이 같은 이름으로 올릴수도있으므로 + UUID
        String savedFileName = UUID.randomUUID().toString() + "_" + originalFilename;

        File saveFile = new File(fileProperties.getUploadPath() + savedFileName);

        if (!saveFile.exists()) saveFile.mkdirs();  // 폴더가 없으면 상위 폴더까지 만듬.

        file.transferTo(saveFile);

        return savedFileName;
    }
}
