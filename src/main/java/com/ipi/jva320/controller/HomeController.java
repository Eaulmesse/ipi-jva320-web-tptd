package com.ipi.jva320.controller;

import com.ipi.jva320.exception.SalarieException;
import com.ipi.jva320.model.SalarieAideADomicile;
import com.ipi.jva320.repository.SalarieAideADomicileRepository;
import com.ipi.jva320.service.SalarieAideADomicileService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class HomeController {
    @Autowired
    private SalarieAideADomicileService salarieAideADomicileService;

    @Autowired
    private SalarieAideADomicileRepository salarieAideADomicileRepository;
    @GetMapping(value = "/")
    public String home(final ModelMap model) {
        long nbSalaries = salarieAideADomicileService.countSalaries();
        model.put("title", "Bienvenue dans l'interface d'administration RH ! " + nbSalaries + " employé(s)");
        return "home";
    }

    @GetMapping(value = "/salaries/{id}")
    public String salarie(@PathVariable Long id, final ModelMap model) {
        Optional<SalarieAideADomicile> aideOptional = salarieAideADomicileRepository.findById(id);
        SalarieAideADomicile aide = aideOptional.orElse(null);
        model.addAttribute("salarie", aide);

        return "detail_Salarie";
    }

    @PostMapping("/salaries/{id}")
    public String updateEmployee(@PathVariable Long id, @ModelAttribute("salarie") SalarieAideADomicile salarie) throws SalarieException  {

        salarieAideADomicileService.updateSalarieAideADomicile(salarie);

        return "redirect:/salaries";
    }

    @PostMapping("/salaries/{id}/delete")
    public String deleteEmployee(@PathVariable Long id) throws SalarieException  {

        salarieAideADomicileService.deleteSalarieAideADomicile(id);
        return "redirect:/salaries";
    }



    @GetMapping(value = "/salaries/aide/new")
    public String newSalarie(Model model) {
        model.addAttribute("salarie", new SalarieAideADomicile());
        return "create_Salarie";
    }

    @PostMapping(value ="/salaries/aide/new")
    public String submitForm(@ModelAttribute("salarie") SalarieAideADomicile salarie) throws SalarieException {
        salarieAideADomicileService.creerSalarieAideADomicile(salarie);
        return "redirect:/salaries";
    }

    @GetMapping("/salaries")
    public String listOrSearchSalaries(@RequestParam(required = false) String nom, Model model) {
        if (nom != null && !nom.isEmpty()) {
            SalarieAideADomicile salarie = salarieAideADomicileService.findByNom(nom);
            if (salarie != null) {
                model.addAttribute("salaries", Collections.singletonList(salarie));
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Salarie not found");
            }
        } else {
            Iterable<SalarieAideADomicile> iterableSalaries = salarieAideADomicileRepository.findAll();
            List<SalarieAideADomicile> salaries = StreamSupport.stream(iterableSalaries.spliterator(), false)
                    .collect(Collectors.toList());
            model.addAttribute("salaries", salaries);
        }
        return "list";
    }









}
