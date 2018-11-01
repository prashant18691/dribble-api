package com.prs.dribbleapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger {

    @Bean
    public Docket dribbleApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("com.prs.dribbleapi.controller"))
                .paths(PathSelectors.any())
                .build()
                .tags(new Tag("Dribble API", "API to save job details using apache kafka"))
                .apiInfo(info());
    }

    private ApiInfo info() {
        ApiInfo apiInfo = new ApiInfo(
                "Dribble API","Rest API based excel upload to save and search job details","1.1","Open-source",
                 new Contact("Prashant Upadhya","https://github"
                + ".com/prashant18691","prashant18691@gmail.com"),"Apache Version 2.0","\"https://www.apache"
                + ".org/licenses/LICENSE-2.0\"");
        return apiInfo;
    }
}
