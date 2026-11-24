package com.iit.gestion_stages.controller.mvc;

import com.iit.gestion_stages.controller.mvc.form.EnseignantForm;
import com.iit.gestion_stages.dto.EnseignantDto;
import com.iit.gestion_stages.exception.BusinessException;
import com.iit.gestion_stages.exception.ResourceNotFoundException;
import com.iit.gestion_stages.service.EnseignantService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/enseignants")
public class AdminEnseignantController {

    private static final String VUE_LISTE = "admin/enseignants/liste";
    private static final String VUE_FORM = "admin/enseignants/form";
    private static final String REDIRECT_LISTE = "redirect:/admin/enseignants";

    private final EnseignantService enseignantService;

    public AdminEnseignantController(EnseignantService enseignantService) {
        this.enseignantService = enseignantService;
    }

    @GetMapping
    public String liste(@RequestParam(required = false) String q, Model model) {
        List<EnseignantDto> enseignants = enseignantService.lister();
        if (q != null && !q.isBlank()) {
            String recherche = q.trim().toLowerCase();
            enseignants = enseignants.stream()
                    .filter(e -> contient(e.nom(), recherche)
                            || contient(e.prenom(), recherche)
                            || contient(e.specialite(), recherche)
                            || contient(e.departement(), recherche))
                    .toList();
        }
        model.addAttribute("enseignants", enseignants);
        model.addAttribute("q", q);
        return VUE_LISTE;
    }

    @GetMapping("/nouveau")
    public String nouveau(Model model) {
        model.addAttribute("enseignant", new EnseignantForm());
        return VUE_FORM;
    }

    @GetMapping("/{id}/modifier")
    public String modifier(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("enseignant", EnseignantForm.from(enseignantService.obtenir(id)));
            return VUE_FORM;
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Enseignant introuvable");
            return REDIRECT_LISTE;
        }
    }

    @PostMapping
    public String enregistrer(@Valid @ModelAttribute("enseignant") EnseignantForm form,
                              BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return VUE_FORM;
        }
        try {
            if (form.getId() == null) {
                enseignantService.creer(form.toCreateDto());
                ra.addFlashAttribute("success", "Enseignant ajouté avec succès");
            } else {
                enseignantService.modifier(form.getId(), form.toCreateDto());
                ra.addFlashAttribute("success", "Enseignant modifié avec succès");
            }
        } catch (BusinessException ex) {
            // ex. email déjà utilisé : on reste sur le formulaire
            result.rejectValue("email", "email.deja.utilise", ex.getMessage());
            return VUE_FORM;
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Enseignant introuvable");
        }
        return REDIRECT_LISTE;
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes ra) {
        try {
            enseignantService.supprimer(id);
            ra.addFlashAttribute("success", "Enseignant supprimé");
        } catch (BusinessException ex) {
            // message métier du service : enseignant qui encadre des stages
            ra.addFlashAttribute("error", ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Enseignant introuvable");
        }
        return REDIRECT_LISTE;
    }

    private boolean contient(String valeur, String recherche) {
        return valeur != null && valeur.toLowerCase().contains(recherche);
    }
}