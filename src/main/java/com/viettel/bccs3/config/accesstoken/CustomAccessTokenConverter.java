package com.viettel.bccs3.config.accesstoken;


import com.viettel.bccs3.domain.dto.ActionUserDTO;
import com.viettel.core.context.ThreadLocalContext;
import io.eventuate.javaclient.commonimpl.JSonMapper;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {
    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
        OAuth2Authentication authentication = super.extractAuthentication(claims);
        var actionUserDTO = JSonMapper.objectMapper.convertValue(claims.get("actionUser"), ActionUserDTO.class);
        ThreadLocalContext.put("currentActionUser", actionUserDTO);
        ActionUserHolder.setActionUser(actionUserDTO);
        return authentication;
    }

}
