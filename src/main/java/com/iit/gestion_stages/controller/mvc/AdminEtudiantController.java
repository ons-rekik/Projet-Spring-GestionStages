package com.iit.gestion_stages.controller.mvc;

import com.iit.gestion_stages.controller.mvc.form.EtudiantForm;
import com.iit.gestion_stages.dto.EtudiantDto;
import com.iit.gestion_stages.exception.BusinessException;
import com.iit.gestion_stages.exception.ResourceNotFoundException;
import com.iit.gestion_stages.service.EtudiantService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/etudiants")
public class AdminEtudiantController {

    private static final String VUE_LISTE = "admin/etudiants/liste";
    private static final String VUE_FORM = "admin/etudiants/form";
    private static final String REDIRECT_LISTE = "redirect:/admin/etudiants";

    private final EtudiantService etudiantService;

    public AdminEtudiantController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @GetMapping
    public String liste(@RequestParam(required = false) String q, Model model) {
        List<EtudiantDto> etudiants = etudiantService.lister();
        if (q != null && !q.isBlank()) {
            String recherche = q.trim().toLowerCase();
            etudiants = etudiants.stream()
                    .filter(e -> contient(e.nom(), recherche)
                            || contient(e.prenom(), recherche)
                            || contient(e.matricule(), recherche)
                            || contient(e.filiere(), recherche))
                    .toList();
        }
        model.addAttribute("etudiants", etudiants);
        model.addAttribute("q", q);
        return VUE_LISTE;
    }

    @GetMapping("/nouveau")
    public String nouveau(Model model) {
        model.addAttribute("etudiant", new EtudiantForm());
        return VUE_FORM;
    }

    @GetMapping("/{id}/modifier")
    public String modifier(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("etudiant", EtudiantForm.from(etudiantService.obtenir(id)));
            return VUE_FORM;
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Étudiant introuvable");
            return REDIRECT_LISTE;
        }
    }

    @PostMapping
    public String enregistrer(@Valid @ModelAttribute("etudiant") EtudiantForm form,
                              BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return VUE_FORM;
        }
        try {
            if (form.getId() == null) {
                etudiantService.creer(form.toCreateDto());
                ra.addFlashAttribute("success", "Étudiant ajouté avec succès");
            } else {
                etudiantService.modifier(form.getId(), form.toCreateDto());
                ra.addFlashAttribute("success", "Étudiant modifié avec succès");
            }
        } catch (BusinessException ex) {
            // ex. email déjà utilisé : on reste sur le formulaire
            result.rejectValue("email", "email.deja.utilise", ex.getMessage());
            return VUE_FORM;
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Étudiant introuvable");
        }
        return REDIRECT_LISTE;
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes ra) {
        try {
            etudiantService.supprimer(id);
            ra.addFlashAttribute("success", "Étudiant supprimé");
        } catch (BusinessException ex) {
            // message métier du service : étudiant lié à des stages
            ra.addFlashAttribute("error", ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Étudiant introuvable");
        }
        return REDIRECT_LISTE;
    }

    private boolean contient(String valeur, String recherche) {
        return valeur != null && valeur.toLowerCase().contains(recherche);
    }
}