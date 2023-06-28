package com.betaplan.era.exam2.controllers;

import com.betaplan.era.exam2.models.LoginUser;
import com.betaplan.era.exam2.models.Tabele;
import com.betaplan.era.exam2.models.User;
import com.betaplan.era.exam2.services.TabeleService;
import com.betaplan.era.exam2.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import com.betaplan.era.exam2.services.TabeleService;
import com.betaplan.era.exam2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private TabeleService TabeleService;


    @GetMapping("/")
    public String index(Model model, @ModelAttribute("newUser") User newUser,
                        @ModelAttribute("newLogin") User newLogin, HttpSession session) {
        if (session.getAttribute("userId") != null) {
            return "redirect:/dashboard";}

        model.addAttribute("newUser", new User());
        model.addAttribute("newLogin", new LoginUser());

        return "index.jsp";
    }


    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("newUser") User newUser,
                           BindingResult result, Model model, HttpSession session) {

        User user = userService.register(newUser, result);

        if(result.hasErrors()) {
            model.addAttribute("newLogin", new LoginUser());
            return "index.jsp";
        }
        session.setAttribute("userId", user.getId());

        return "redirect:/dashboard";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin,
                        BindingResult result, Model model, HttpSession session) {

        User user = userService.login(newLogin, result);

        if (result.hasErrors() || user == null) {
            model.addAttribute("newUser", new User());
            return "index.jsp";
        }

        session.setAttribute("userId", user.getId());

        return "redirect:/dashboard";

    }



    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        if(session.getAttribute("userId") == null) {
            return "redirect:/logout";
        }
        Long userId = (Long) session.getAttribute("userId");
        User user = userService.findById(userId);

        model.addAttribute("user", user);
        model.addAttribute("unassignedProjects", TabeleService.getUnassignedTables(user));
        model.addAttribute("assignedProjects", TabeleService.getAssignedTables(user));

        return "dashboard.jsp";
    }

    @RequestMapping("/dashboard/join/{id}")
    public String joinTeam(@PathVariable("id") Long id, HttpSession session, Model model) {

        if(session.getAttribute("userId") == null) {
            return "redirect:/logout";
        }
        Long userId = (Long) session.getAttribute("userId");

        Tabele project = TabeleService.findById(id);
        User user = userService.findById(userId);

        user.getTables().add(project);
        userService.updateUser(user);

        model.addAttribute("user", user);
        model.addAttribute("unassignedProjects", TabeleService.getUnassignedTables(user));
        model.addAttribute("assignedProjects", TabeleService.getAssignedTables(user));

        return "redirect:/dashboard";
    }

    @RequestMapping("/dashboard/leave/{id}")
    public String leaveTeam(@PathVariable("id") Long id, HttpSession session, Model model) {

        if(session.getAttribute("userId") == null) {
            return "redirect:/logout";
        }
        Long userId = (Long) session.getAttribute("userId");

        Tabele Tabele = TabeleService.findById(id);
        User user = TabeleService.findById(userId);

        user.getTables().remove(Tabele);
        userService.updateUser(user);

        model.addAttribute("user", user);
        model.addAttribute("unassignedTables", TabeleService.getUnassignedTables(user));
        model.addAttribute("assignedPTables", TabeleService.getAssignedTables(user));

        return "redirect:/dashboard";
    }

    @GetMapping("/Tables/{id}")
    public String viewProject(@PathVariable("id") Long id, HttpSession session, Model model) {

        if(session.getAttribute("userId") == null) {
            return "redirect:/logout";
        }

        Tabele Tabele = TabeleService.findById(id);
        model.addAttribute("project", Tabele;
        return "view_project.jsp";
    }

    @GetMapping("/Tabele/edit/{id}")
    public String openEditProject(@PathVariable("id") Long id, HttpSession session, Model model) {

        if(session.getAttribute("userId") == null) {
            return "redirect:/logout";
        }

        Tabele project = TabeleService.findById(id);
        model.addAttribute("Tabele", Tabele);
        return "edit_Tabele.jsp";
    }

    @PostMapping("/Tabele/edit/{id}")
    public String editProject(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("Tabele") Tabele Tabele,
            BindingResult result,
            HttpSession session) {

        if(session.getAttribute("userId") == null) {
            return "redirect:/logout";
        }
        Long userId = (Long) session.getAttribute("userId");

        User user = userService.findById(userId);

        if(result.hasErrors()) {
            return "edit_project.jsp";
        }else {
            Tabele thisProject = TabeleService.findById(id);
            Tabele.setLead(thisProject.getLead());
            Tabele.setLead(user);
            TabeleService.updateProject(Tabele);
            return "redirect:/dashboard";
        }
    }

    @RequestMapping("/projects/delete/{id}")
    public String deleteProject(@PathVariable("id") Long id, HttpSession session) {

        if(session.getAttribute("userId") == null) {
            return "redirect:/logout";
        }

        Tabele project = TabeleService.findById(id);
        TabeleService.deleteProject(project);

        return "redirect:/dashboard";
    }




}

