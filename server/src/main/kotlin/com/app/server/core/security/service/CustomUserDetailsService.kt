package com.app.server.core.security.service

import com.app.server.common.exception.NotFoundException
import com.app.server.core.security.info.CustomUserDetails.Companion.create
import com.app.server.user.application.repository.UserRepository
import com.app.server.user.domain.model.User
import com.app.server.user.exception.UserExceptionCode
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user: User = userRepository.findByEmail(email)
            ?: throw NotFoundException(UserExceptionCode.NOT_FOUND_USER)
        return create(user)
    }

    fun loadUserByUserId(userId: Long): UserDetails {
        val user: User = userRepository.findByIdAndRefreshTokenNotNull(userId)
            ?: throw NotFoundException(UserExceptionCode.NOT_FOUND_USER)
        return create(user)
    }
}
