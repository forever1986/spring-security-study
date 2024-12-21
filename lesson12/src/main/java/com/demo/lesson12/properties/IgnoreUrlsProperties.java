package com.demo.lesson12.properties;

import com.demo.lesson12.entity.WhiteDTO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties("security")
public class IgnoreUrlsProperties {

    private List<WhiteDTO> ignoreUrls = new ArrayList<>();

    public RequestMatcher[] getRequestMatcher(){
        List<RequestMatcher> requestMatchers = new ArrayList<>();
        ignoreUrls.stream().map(whiteDTO->new AntPathRequestMatcher(whiteDTO.getUrl(), whiteDTO.getHttpMethod())).forEach(requestMatchers::add);
        return requestMatchers.toArray(new RequestMatcher[0]);
    }
}
