package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.UserService;
import com.projectlove.lovable_clone.dto.auth.UserProfileResponse;
import com.projectlove.lovable_clone.error.ResourceNotFoundException;
import com.projectlove.lovable_clone.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
//first we tell our spring what username it should take what pw how does it access our db i.e from this method->loadUserByUsername and for
// definning this method we implement  UserDetailsService(UserService is our application interface right) and in method there is a rule
//the rule is we should return an object of UserDetails but what i did is my User entity implements UserDetails therefore if i return User/UserDetails its
//essentially the same thing na so this is how spring sec does the whole thing but we just give access to our
//User db this way
public class    UserServiceImpl implements UserService, UserDetailsService {

    UserRepository userRepository;

    @Override
    public UserProfileResponse getProfile(Long userId) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("user",username));

    }

//    UserServiceImpl implements UserDetailsService, which is the key link between Spring Security and your database:
//    when AuthenticationManager needs to authenticate a username/password, it delegates to the UserDetailsService and calls loadUserByUsername(username), which uses userRepository.findByUsername(username) to fetch the user from the DB.
//    Although the method's return type is UserDetails, you return a User object because your User class implements UserDetails; therefore, a User is-a UserDetails and can be returned through upcasting/runtime polymorphism.
//    Spring Security then gets the UserDetails containing the username and stored password hash and uses it to verify the supplied password.
//    So the flow is: AuthenticationManager → AuthenticationProvider → UserDetailsService → loadUserByUsername() → UserRepository → User (as UserDetails) → password verification → authenticated.


}
