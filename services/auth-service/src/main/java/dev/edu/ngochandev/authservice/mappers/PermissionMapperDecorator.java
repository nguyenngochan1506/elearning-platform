package dev.edu.ngochandev.authservice.mappers;

import dev.edu.ngochandev.authservice.dtos.res.PermissionResponseDto;
import dev.edu.ngochandev.authservice.entities.PermissionEntity;
import dev.edu.ngochandev.common.i18n.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class PermissionMapperDecorator implements PermissionMapper {
    @Autowired
    @Qualifier("delegate")
    private PermissionMapper delegate;
    @Autowired
    private Translator translator;

    @Override
    public PermissionResponseDto toResponseDto(PermissionEntity entity) {
        PermissionResponseDto dto = delegate.toResponseDto(entity);

        if (dto != null && entity.getName() != null) {
            String translatedName = translator.translate(entity.getName());
            return PermissionResponseDto.builder()
                    .name(translatedName)
                    .id(entity.getId())
                    .method(entity.getMethod().name())
                    .module(entity.getModule())
                    .endpoint(entity.getApiPath())
                    .build();
        }
        return dto;
    }
}
