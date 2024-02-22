package hu.otottkovi.cashcard.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http:HttpSecurity):SecurityFilterChain{
        http.authorizeHttpRequests {
            it.requestMatchers("/cashcards/**")
                .authenticated()
        }.csrf{
                it.disable()
        }.httpBasic(Customizer.withDefaults())
        return http.build()
    }

    @Bean
    fun testOnlyUsers(passwordEncoder: PasswordEncoder):UserDetailsService{
        val users = User.builder()
        val me = users.username("Tomika")
            .password(passwordEncoder.encode("password"))
            .roles()
            .build()
        return InMemoryUserDetailsManager(me)
    }

    @Bean
    fun passwordEncoder():PasswordEncoder{
        return BCryptPasswordEncoder()
    }
}