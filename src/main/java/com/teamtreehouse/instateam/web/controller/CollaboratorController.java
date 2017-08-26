package com.teamtreehouse.instateam.web.controller;

import com.teamtreehouse.instateam.model.Collaborator;
import com.teamtreehouse.instateam.model.Role;
import com.teamtreehouse.instateam.service.CollaboratorService;
import com.teamtreehouse.instateam.service.RoleService;
import com.teamtreehouse.instateam.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CollaboratorController {
    @Autowired
    CollaboratorService collaboratorService;

    @Autowired
    RoleService roleService;

    String attributeName = "org.springframework.validation.BindingResult.collaborator";

    @RequestMapping("/collaborators")
    public String listCollaborators(Model model) {
        List<Collaborator> collaborators = collaboratorService.findAll();
        List<Role> roles = roleService.findAll();
        Map<Collaborator, Role> collaboratorRoleMap = getCollaboratorRoleMap(collaborators);
        if (!model.containsAttribute("collaborator")) {
            model.addAttribute("collaborator", new Collaborator());
        }
        model.addAttribute("collaboratorRoleMap", collaboratorRoleMap);
        model.addAttribute("roles", roles);
        return "collaborators";
    }

    @RequestMapping(value = "/collaborators", method = RequestMethod.POST)
    public String addCollaborator(@Valid Collaborator collaborator, BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("collaborator", collaborator);
            redirectAttributes.addFlashAttribute(attributeName, result);
        } else {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("The collaborator has been created",
                    FlashMessage.Status.SUCCESS));
            collaboratorService.save(collaborator);
        }
        return "redirect:/collaborators";
    }

    @RequestMapping("/collaborators/{id}")
    public String editCollaboratorForm(@PathVariable Integer id, Model model) {
        if (!model.containsAttribute("collaborator")) {
            Collaborator collaborator = collaboratorService.findById(id);
            model.addAttribute("collaborator", collaborator);
        }
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        return "collaborator_detail";
    }

    @RequestMapping(value = "/collaborators/{id}", method = RequestMethod.POST)
    public String editCollaborator(@PathVariable Integer id, @Valid Collaborator collaborator, BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("collaborator", collaborator);
            redirectAttributes.addFlashAttribute(attributeName, result);
            return String.format("redirect:/collaborators/%s", id);
        }
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("The collaborator has been updated",
                FlashMessage.Status.SUCCESS));
        collaboratorService.save(collaborator);
        return "redirect:/collaborators";
    }

    private Map<Collaborator, Role> getCollaboratorRoleMap(List<Collaborator> collaborators) {
        Map<Collaborator, Role> collaboratorRoleMap = new HashMap<>();
        for (Collaborator collaborator : collaborators) {
            Role role = collaborator.getRole();
            if (role == null) {
                role = new Role();
                role.setName("Undefined");
            }
            collaboratorRoleMap.put(collaborator, role);
        }
        return collaboratorRoleMap;
    }
}
