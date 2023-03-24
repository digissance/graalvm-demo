package biz.digissance.graalvmdemo.http;

import biz.digissance.graalvmdemo.domain.party.PartyService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final PartyService partyService;

    public RegisterController(final PartyService partyService) {
        this.partyService = partyService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // create model object to store form data
//        UserDto user = new UserDto();
        model.addAttribute("user", new PersonDTO());
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(
            @Valid @ModelAttribute("user") PersonDTO userDto,
            BindingResult result,
            Model model) {
//        User existingUser = userService.findUserByEmail(userDto.getEmail());
        partyService.register(userDto);
//        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
//            result.rejectValue("email", null,
//                    "There is already an account registered with the same email");
//        }

//        if(result.hasErrors()){
//            model.addAttribute("user", userDto);
        return "redirect:/register?success";
//        }

//        userService.saveUser(userDto);
//        return "redirect:/register?success";
    }
}
