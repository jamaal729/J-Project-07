package com.teamtreehouse.instateam.web.controller;

import com.teamtreehouse.instateam.model.Collaborator;
import com.teamtreehouse.instateam.model.Project;
import com.teamtreehouse.instateam.model.Role;
import com.teamtreehouse.instateam.service.CollaboratorService;
import com.teamtreehouse.instateam.service.ProjectService;
import com.teamtreehouse.instateam.service.RoleService;
import com.teamtreehouse.instateam.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @Autowired
    RoleService roleService;

    @Autowired
    CollaboratorService collaboratorService;

    String attributeName = "org.springframework.validation.BindingResult.project";

    @RequestMapping("/index")
    public String listAllProjects(Model model) {
        List<Project> projects = projectService.findAll();
        model.addAttribute("projects", projects);
        return "index";
    }

    @RequestMapping("/add")
    public String newProjectForm(Model model) {
        List<Role> roles = roleService.findAll();
        if (!model.containsAttribute("project")) {
            model.addAttribute("project", new Project());
        }
        model.addAttribute("roles", roles);
        return "edit_project";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addProject(@Valid Project project, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(attributeName, result);
            redirectAttributes.addFlashAttribute("project", project);
            return "redirect:/add";
        }

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("The project has been created",
                FlashMessage.Status.SUCCESS));
        projectService.save(project);
        return "redirect:/index";
    }

    @RequestMapping("/project/{id}")
    public String projectDetail(@PathVariable Integer id, Model model) {
        Project project = projectService.findById(id);
        Map<Role, Collaborator> roleCollaboratorMap = getRoleCollaboratorMap(project);
        model.addAttribute("project", project);
        model.addAttribute("roleCollaboratorMap", roleCollaboratorMap);
        return "project_detail";
    }

    @RequestMapping("/project/{id}/edit")
    public String editProjectForm(
            @PathVariable Integer id, Model model) {
        if (!model.containsAttribute("project")) {
            Project project = projectService.findById(id);
            model.addAttribute("project", project);
        }
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        return "edit_project";
    }

    @RequestMapping(value = "/project/{id}/edit", method = RequestMethod.POST)
    public String editProject(
            @PathVariable Integer id, @Valid Project project, BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(attributeName, result);
            redirectAttributes.addFlashAttribute("project", project);
            return String.format("redirect:/project/%s/edit", id);
        }
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("The project has been updated",
                FlashMessage.Status.SUCCESS));
        projectService.save(project);

        return String.format("redirect:/project/%s", id);
    }

    @RequestMapping("/project/{id}/collaborators")
    public String assignProjectCollaboratorsForm(
            @PathVariable Integer id, Model model) {
        Project project = projectService.findById(id);
        List<Collaborator> collaborators = collaboratorService.findAll();
        Map<Role, List<Collaborator>> roleCollaborators = getRoleListMap(project, collaborators);
        model.addAttribute("project", project);
        model.addAttribute("roleCollaborators", roleCollaborators);

        return "project_collaborators";
    }

    @RequestMapping(value = "/project/{id}/collaborators", method = RequestMethod.POST)
    public String addCollaborators(
            @RequestParam Map<String, String> params, @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {
        Project project = projectService.findById(id);
        addCollaborators(params, project);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("The collaborators have been allocated",
                FlashMessage.Status.SUCCESS));
        projectService.save(project);

        return String.format("redirect:/project/%s", id);
    }

    private Map<Role, Collaborator> getRoleCollaboratorMap(Project project) {
        List<Role> rolesNeededList = project.getRolesNeeded();
        List<Collaborator> collaboratorsList = project.getCollaborators();
        Map<Role, Collaborator> roleCollaboratorMap = new LinkedHashMap<>();

        for (Role requiredRole : rolesNeededList) {
            roleCollaboratorMap.put(requiredRole,
                    collaboratorsList.stream()
                            .filter((c) -> c.getRole().getId().equals(requiredRole.getId()))
                            .findFirst()
                            .orElseGet(() -> {
                                Collaborator unassigned = new Collaborator();
                                unassigned.setName("Unassigned");
                                return unassigned;
                            }));
        }
        return roleCollaboratorMap;
    }

    private Map<Role, List<Collaborator>> getRoleListMap(Project project, List<Collaborator> collaborators) {
        Map<Role, List<Collaborator>> roleCollaborators = new HashMap<>();
        for (Role role : project.getRolesNeeded()) {
            List<Collaborator> coList = new ArrayList<>();
            for (Collaborator collaborator : collaborators) {
                if (collaborator.getRole() != null && collaborator.getRole().getId().equals(role.getId())) {
                    coList.add(collaborator);
                }
            }
            roleCollaborators.put(role, coList);
        }
        return roleCollaborators;
    }

    private void addCollaborators(Map<String, String> params, Project project) {
        project.setCollaborators(new ArrayList<>());
        params.forEach((key, value) -> {
            int collaboratorId = Integer.parseInt(value);

            // Already matched collaborator
            Collaborator collaborator = collaboratorService.findById(collaboratorId);
            List<Collaborator> collaborators = project.getCollaborators();
            if (collaborators
                    .stream()
                    .anyMatch(c -> c.getId().equals(collaborator.getId()))) {
                return;
            }
            // Match collaborator
            project.getCollaborators().add(collaborator);
        });
    }
}
