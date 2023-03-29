package biz.digissance.graalvmdemo.http.admin.party;

import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRoleTypeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class PartyRoleTypeController {

    private final JpaPartyRoleTypeRepository repository;

    public PartyRoleTypeController(final JpaPartyRoleTypeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/party-role-types")
    public String partyRoleTypesView(Model model) {
        model.addAttribute("roleTypes", repository.findAll());
        return "party-role-type/index";
    }
}
