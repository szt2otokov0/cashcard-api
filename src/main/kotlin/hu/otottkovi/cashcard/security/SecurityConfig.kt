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
                .hasRole("CARD-OWNER")
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
            .roles("CARD-OWNER")
            .build()
        val poorMan = users.username("not a card owner")
            .password(passwordEncoder.encode("qwerty"))
            .roles("NON-OWNER")
            .build()
        val another = users.username("Patrick")
            .password(passwordEncoder.encode("20041209"))
            .roles("CARD-OWNER")
            .build()
        return InMemoryUserDetailsManager(me,poorMan,another)
    }

    @Bean
    fun passwordEncoder():PasswordEncoder{
        return BCryptPasswordEncoder()
    }
}