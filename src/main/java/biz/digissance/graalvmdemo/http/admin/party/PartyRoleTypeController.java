package biz.digissance.graalvmdemo.http.admin.party;

import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRoleType;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRoleTypeRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/party-role-types")
public class PartyRoleTypeController {

    private final JpaPartyRoleTypeRepository repository;

    public PartyRoleTypeController(final JpaPartyRoleTypeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String partyRoleTypesView(Model model) {
        model.addAttribute("roleTypes", repository.findAll());
        return "party-role-type/index";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("jpaPartyRoleType", new JpaPartyRoleType());
        return "party-role-type/add";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model) {
        final var jpaPartyRoleType = repository.findById(id).orElseThrow();
        model.addAttribute("jpaPartyRoleType", jpaPartyRoleType);
        return "party-role-type/add";
    }

    @PostMapping("/add")
    public String add(@Valid JpaPartyRoleType jpaPartyRoleType, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("jpaPartyRoleType", jpaPartyRoleType);
            return "party-role-type/add";
        }

        repository.save(jpaPartyRoleType);
        return "redirect:/admin/party-role-types";
    }

    @GetMapping("/delete/{id}")
    public String deletePartyRoleType(@PathVariable("id") long id, Model model) {
        final var partyRoleType = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid party role type Id:" + id));
        repository.delete(partyRoleType);
        return "redirect:/admin/party-role-types";
    }
}
