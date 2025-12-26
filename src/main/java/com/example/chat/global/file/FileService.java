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
        // file이 null이면 null 반환 (기본 이미지 적용)
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 1. 업로드 경로
        String uploadDir = fileProperties.getUploadPath();
        log.info("uploadDir : {}", uploadDir);

        // 2. 원본 파일 이름
        String originalFilename = file.getOriginalFilename();

        // 2-1. 기존 파일 삭제 : 업로드경로 + 원본 파일 이름
        new File(uploadDir + oldFilename).delete();

        // 3. 저장 파일 이름 : UUID + 원본 파일 이름
        String savedFileName = UUID.randomUUID().toString() + "_" + originalFilename;

        // 4. 파일 만들기 : 업로드 경로 + 새로운 파일 이름
        File saveFile = new File(uploadDir + savedFileName);

        if (!saveFile.exists()) saveFile.mkdirs();  // 폴더가 없으면 상위 폴더까지 만듬.

        // 5. 파일 저장
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
