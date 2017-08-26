package com.teamtreehouse.instateam.web.controller;

import com.teamtreehouse.instateam.model.Role;
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
import java.util.List;

@Controller
public class RoleController {
    @Autowired
    RoleService roleService;

    String attributeName = "org.springframework.validation.BindingResult.role";

    @RequestMapping("/roles")
    public String listAllRoles(Model model) {
        List<Role> roles = roleService.findAll();
        if (!model.containsAttribute("role")) {
            model.addAttribute("role", new Role());
        }
        model.addAttribute("roles", roles);
        return "roles";
    }

    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public String addRole(@Valid Role role, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(attributeName, result);
            redirectAttributes.addFlashAttribute("role", role);
        } else {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("The role has been created",
                    FlashMessage.Status.SUCCESS));
            roleService.save(role);
        }
        return "redirect:/roles";
    }

    @RequestMapping("/roles/{id}")
    public String editRoleForm(@PathVariable Integer id, Model model) {
        if (!model.containsAttribute("role")) {
            Role role = roleService.findById(id);
            model.addAttribute("role", role);
        }
        return "role_detail";
    }

    @RequestMapping(value = "/roles/{id}", method = RequestMethod.POST)
    public String editRole(@PathVariable Integer id, @Valid Role role, BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(attributeName, result);
            redirectAttributes.addFlashAttribute("role", role);
            return String.format("redirect:/roles/%s", id);
        }
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("The role has been updated",
                FlashMessage.Status.SUCCESS));
        roleService.save(role);
        return "redirect:/roles";
    }
}
