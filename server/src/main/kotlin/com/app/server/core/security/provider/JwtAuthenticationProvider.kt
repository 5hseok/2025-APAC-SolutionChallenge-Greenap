package com.app.server.core.security.provider

import com.app.server.common.exception.BadRequestException
import com.app.server.core.security.JwtAuthenticationToken
import com.app.server.core.security.enums.SecurityExceptionCode
import com.app.server.core.security.info.CustomUserDetails
import com.app.server.core.security.info.JwtUserInfo
import com.app.server.core.security.service.CustomUserDetailsService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider(
    private val customUserDetailsService: CustomUserDetailsService
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {

        if (authentication is JwtAuthenticationToken) {
            val jwtUserInfo = JwtUserInfo(id = authentication.principal as Long)

            val userDetails = customUserDetailsService.loadUserByUserId(jwtUserInfo.id) as CustomUserDetails

            return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        }

        throw BadRequestException(SecurityExceptionCode.INVALID_AUTHENTICATION)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == JwtAuthenticationToken::class.java
    }
}