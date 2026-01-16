package com.example.chat.category.service;

import com.example.chat.category.dto.CategoryResponse;
import com.example.chat.category.entity.Category;
import com.example.chat.category.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;  //가짜 레포지토리 생성
    @InjectMocks
    private CategoryService categoryService;    // 가짜 레포지토리를 주입받을 서비스

    @BeforeEach
    void setup() {

    }

    @Test
    @DisplayName("그룹 ID로 카테고리 목록을 조회하면 정확한 사이즈의 DTO 리스트를 반환한다.")
    void getCategorizedChannels_Success() {
        // given : 테스트 환경 준비
        Long groupId = 1L;

        // 가짜 데이터 생성
        Category category1 = Category.create("카테고리1", false, null);
        Category category2 = Category.create("카테고리2", false, null);
        List<Category> mockCategory = List.of(category1, category2);

        given(categoryRepository.findAllByGroupIdWithChannels(groupId))
                .willReturn(mockCategory);

        // when : 테스트할 메서드 실행
        List<CategoryResponse> result = categoryService.getCategorizedChannels(groupId);

        // then : 테스트 결과 검증
        // 리스트 사이즈 검증
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("카테고리1");

        // groupId 검증
        verify(categoryRepository).findAllByGroupIdWithChannels(groupId);
        System.out.println("result.size() = " + result.size());
    }
}