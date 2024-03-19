package com.StockAnalyser.StockAnalyser.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
public class UserService {

    @Autowired //otherwise will get an error
    private UserRepository userRepository;



    public User addUser(User user){
        return userRepository.save(user);
    }//Add a new user

    public List<User> allUsers(){
        return userRepository.findAll();
    }

    public User SingleUserByName(String name){
        Optional<User> userOptional = userRepository.findByUsername(name);
        return userOptional.orElse(null);
    }

    public User SingleUserByEmail(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElse(null);
    }

    public boolean checkPassword(User user, String enteredPassword){
        return user.getPassword().equals(enteredPassword);
    }


}





