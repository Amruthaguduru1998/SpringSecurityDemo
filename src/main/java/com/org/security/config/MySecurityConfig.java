package com.org.security.config;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                (auth)-> auth
                        .requestMatchers("/home","/","/h2-console/**").permitAll()
                        .requestMatchers("/public/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest()
                        .authenticated())
                .httpBasic(withDefaults());
        // http.sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(MyUserRepo myUserRepo) {
//        UserDetails normalUser = User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("user"))
//                .roles("USER")
//                .build();
//        System.out.println("password for public" + passwordEncoder().encode("user"));
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN","USER")
//                .build();
//        System.out.println("password for admin" + passwordEncoder().encode("admin"));
//
//        return new InMemoryUserDetailsManager(normalUser,admin);
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
               MyUser dbUser =myUserRepo.findByUsername(username);
                System.out.println("dbuser =" + dbUser );
                if(dbUser==null){
                    throw new UsernameNotFoundException("user not found");
                }
                UserDetails userDetails = User.builder()
                .username(dbUser.getUsername())
                .password(dbUser.getPassword())
                .roles(dbUser.getRole().toArray(String[]::new))
                .build();

                System.out.println("springUser =" + userDetails );
                return userDetails;
            }
        };
    }
    @Bean
    CommandLineRunner loadInitialUsersInDb(MyUserRepo repo){
        return args -> {
//            MyUser user1=new MyUser();
//            user1.setUsername("user");
//            user1.setPassword(passwordEncoder().encode("user"));
//            user1.setRole(Set.of("USER"));
//            repo.save(user1);
//
//            MyUser user2=new MyUser();
//            user2.setUsername("admin");
//            user2.setPassword(passwordEncoder().encode("admin"));
//            user2.setRole(Set.of("ADMIN"));
//            repo.save(user2);
//
//            MyUser user3=new MyUser();
//            user3.setUsername("adminsp");
//            user3.setPassword(passwordEncoder().encode("adminsp"));
//            user3.setRole(Set.of("ADMIN"));
//            repo.save(user3);
//
//            System.out.println( "user save in DB");
        };
    }
}


interface MyUserRepo extends JpaRepository<MyUser,Long>{
    MyUser findByUsername(String username);

}

@Entity
class MyUser{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private Set<String> role;
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}