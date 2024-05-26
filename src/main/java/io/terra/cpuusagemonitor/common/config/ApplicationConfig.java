package io.terra.cpuusagemonitor.common.config;


import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration()
            .setFieldMatchingEnabled(true) //필드 일치를 활성화할지 여부 설정
            .setPropertyCondition(Conditions.isNotNull())  //source의 특정 필드가 Null이 아닐 때만 매핑하고 싶다면
            .setAmbiguityIgnored(true) //둘 이상의 소스 속성과 일치하는 대상 속성을 무시할지 여부 설정
            .setImplicitMappingEnabled(true); //암시적 매핑을 활성화할지 여부를 설정
        return mapper;
    }


}
