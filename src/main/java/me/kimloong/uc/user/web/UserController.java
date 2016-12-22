package me.kimloong.uc.user.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by closer on 16-9-4.
 */
@RestController
public class UserController {

    @RequestMapping("/users/home")
    public String home() {
        return "Hello World";
    }
}
