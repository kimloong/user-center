package me.kimloong.uc.user.web;

import me.kimloong.uc.user.model.User;
import me.kimloong.uc.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by KimLoong
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/demo")
    public String home() {
        return "Hello World";
    }

    @Secured("anonymous")
    @RequestMapping(method = RequestMethod.POST)
    public void register(@RequestBody User user) {
        userService.register(user);
    }
}
