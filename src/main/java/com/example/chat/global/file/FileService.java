package com.example.chat.global.file;

import com.example.chat.user.entity.SiteUser;
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
    public String storeFile(MultipartFile file, String oldFilename) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String uploadDir = fileProperties.getUploadPath();
        log.info("uploadDir : {}", uploadDir);

        String originalFilename = file.getOriginalFilename();

        // 기존 파일 삭제
        new File(uploadDir + oldFilename).delete();

        // 사용자들이 같은 이름으로 올릴수도있으므로 + UUID
        String savedFileName = UUID.randomUUID().toString() + "_" + originalFilename;

        // 파일 경로 저장
        File saveFile = new File(uploadDir + savedFileName);

        if (!saveFile.exists()) saveFile.mkdirs();  // 폴더가 없으면 상위 폴더까지 만듬.

        // 서버 지정위치에 파일 저장
        file.transferTo(saveFile);

        // 파일 이름 리턴
        return savedFileName;
    }

    public void deleteProfile(String profile) {
        File file = new File(fileProperties.getUploadPath() + profile);

        if (file.exists()) {
            if (file.delete()) {
                log.info("파일 삭제 성공 : {}", profile);
            } else {
                log.info("파일 삭제 실패 : {}", profile);
            }
        } else {
            log.info("삭제할 파일이 존재하지 않음 (이미 삭제됨) : {}", profile);
        }
    }

    /*
     * 파일을 저장하고 저장된 고유 파일명을 반환합니다.
     * */
}
