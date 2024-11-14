package e_com.bichitra.e_com02092024.utils;

import e_com.bichitra.e_com02092024.model.Users;
import e_com.bichitra.e_com02092024.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private UserRepository userRepository;

    public String loggedInEmail(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        final Users users = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User name found with username " + authentication.getName()));
        return users.getEmail();
    }

    public Long loggedInUserId(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        final Users users = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User name found with username " + authentication.getName()));

        return users.getUserId();

    }
    public Users loggedInUsername(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        final Users users = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User name found with username " + authentication.getName()));
        return users;

    }


}




















