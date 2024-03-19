package com.StockAnalyser.StockAnalyser.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/userApi")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailSenderService senderService;


    @PostMapping("/userRegistration")
    public ResponseEntity<User> postDetails(@RequestBody User user) {
        if (user != null && user.getEmail() != null && !user.getEmail().isEmpty()) {
            try {
                senderService.sendSimpleEmail(user.getEmail(),
                        "Registration successful!!!",
                        "Dear friend,\n\nThis is an auto-generated mail. Just to inform that you used this email address to register my personal website.\n\nThanks and good luck. \n\nBest,\nChen Jui-Tai" );
                System.out.println("Email sent successfully to: " + user.getEmail());
            } catch (Exception e) {
                System.err.println("Failed to send email to: " + user.getEmail());
                e.printStackTrace();
            }
        } else {
            System.err.println("Invalid user or email address");
        }
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.CREATED);
    }
     



    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(){
        return userService.allUsers();
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getSinglUser(@PathVariable String username) {
        return new ResponseEntity<>(userService.SingleUserByName(username), HttpStatus.OK);
    }


    @PostMapping("/userLogin")
    public ResponseEntity<String> checkLogin(@RequestBody User enteredUser){
        User retrievedUser = userService.SingleUserByName(enteredUser.getUsername());

        if(retrievedUser != null && userService.checkPassword(retrievedUser, enteredUser.getPassword())){
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }



}
