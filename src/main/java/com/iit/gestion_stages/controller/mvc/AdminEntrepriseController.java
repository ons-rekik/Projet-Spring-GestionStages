package com.iit.gestion_stages.controller.mvc;

import com.iit.gestion_stages.controller.mvc.form.EntrepriseForm;
import com.iit.gestion_stages.dto.EntrepriseDto;
import com.iit.gestion_stages.exception.BusinessException;
import com.iit.gestion_stages.exception.ResourceNotFoundException;
import com.iit.gestion_stages.service.EntrepriseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/entreprises")
public class AdminEntrepriseController {

    private static final String VUE_LISTE = "admin/entreprises/liste";
    private static final String VUE_FORM = "admin/entreprises/form";
    private static final String REDIRECT_LISTE = "redirect:/admin/entreprises";

    private final EntrepriseService entrepriseService;

    public AdminEntrepriseController(EntrepriseService entrepriseService) {
        this.entrepriseService = entrepriseService;
    }

    @GetMapping
    public String liste(@RequestParam(required = false) String q, Model model) {
        List<EntrepriseDto> entreprises = (q != null && !q.isBlank())
                ? entrepriseService.rechercherParNom(q)
                : entrepriseService.lister();
        model.addAttribute("entreprises", entreprises);
        model.addAttribute("q", q);
        return VUE_LISTE;
    }

    @GetMapping("/nouveau")
    public String nouveau(Model model) {
        model.addAttribute("entreprise", new EntrepriseForm());
        return VUE_FORM;
    }

    @GetMapping("/{id}/modifier")
    public String modifier(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("entreprise", EntrepriseForm.from(entrepriseService.obtenir(id)));
            return VUE_FORM;
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Entreprise introuvable");
            return REDIRECT_LISTE;
        }
    }

    @PostMapping
    public String enregistrer(@Valid @ModelAttribute("entreprise") EntrepriseForm form,
                              BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return VUE_FORM;
        }
        try {
            if (form.getId() == null) {
                entrepriseService.creer(form.toCreateDto());
                ra.addFlashAttribute("success", "Entreprise ajoutée avec succès");
            } else {
                entrepriseService.modifier(form.getId(), form.toCreateDto());
                ra.addFlashAttribute("success", "Entreprise modifiée avec succès");
            }
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Entreprise introuvable");
        }
        return REDIRECT_LISTE;
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes ra) {
        try {
            entrepriseService.supprimer(id);
            ra.addFlashAttribute("success", "Entreprise supprimée");
        } catch (BusinessException ex) {
            // message métier du service : entreprise liée à des stages
            ra.addFlashAttribute("error", ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Entreprise introuvable");
        }
        return REDIRECT_LISTE;
    }
}