package biz.digissance.graalvmdemo.http.admin.party;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class PartyRoleTypeController {

    @GetMapping("/party-role-types")
    public String partyRoleTypesView() {
        return "party-role-types";
    }
}
