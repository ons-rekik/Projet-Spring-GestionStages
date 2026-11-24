package com.iit.gestion_stages.controller.mvc;

import com.iit.gestion_stages.controller.mvc.form.ReunionForm;
import com.iit.gestion_stages.dto.ReunionDto;
import com.iit.gestion_stages.exception.ResourceNotFoundException;
import com.iit.gestion_stages.service.ReunionService;
import com.iit.gestion_stages.service.StageService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Espace enseignant : gestion des réunions de suivi d'un stage
 * (liste, ajout, modification, suppression).
 */
@Controller
@RequestMapping("/enseignant/stages/{stageId}/reunions")
public class EnseignantReunionController {

    private static final String VUE = "enseignant/reunions";
    private static final String REDIRECT_ETUDIANTS = "redirect:/enseignant/etudiants";

    private final StageService stageService;
    private final ReunionService reunionService;

    public EnseignantReunionController(StageService stageService, ReunionService reunionService) {
        this.stageService = stageService;
        this.reunionService = reunionService;
    }

    @GetMapping
    public String liste(@PathVariable Long stageId, Model model, RedirectAttributes ra) {
        try {
            charger(stageId, model);
            model.addAttribute("reunion", new ReunionForm());
            return VUE;
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Stage introuvable");
            return REDIRECT_ETUDIANTS;
        }
    }

    @GetMapping("/{id}/modifier")
    public String modifier(@PathVariable Long stageId, @PathVariable Long id,
                           Model model, RedirectAttributes ra) {
        try {
            charger(stageId, model);
            model.addAttribute("reunion", ReunionForm.from(reunionDuStage(stageId, id)));
            return VUE;
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Réunion introuvable");
            return redirectReunions(stageId);
        }
    }

    @PostMapping
    public String enregistrer(@PathVariable Long stageId,
                              @Valid @ModelAttribute("reunion") ReunionForm form,
                              BindingResult result, Model model, RedirectAttributes ra) {
        try {
            if (result.hasErrors()) {
                charger(stageId, model);
                return VUE;
            }
            if (form.getId() == null) {
                reunionService.creer(stageId, form.toCreateDto());
                ra.addFlashAttribute("success", "Réunion ajoutée avec succès");
            } else {
                reunionDuStage(stageId, form.getId()); // vérifie l'appartenance au stage
                reunionService.modifier(form.getId(), form.toCreateDto());
                ra.addFlashAttribute("success", "Réunion modifiée avec succès");
            }
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Stage ou réunion introuvable");
        }
        return redirectReunions(stageId);
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long stageId, @PathVariable Long id, RedirectAttributes ra) {
        try {
            reunionDuStage(stageId, id); // vérifie l'appartenance au stage
            reunionService.supprimer(id);
            ra.addFlashAttribute("success", "Réunion supprimée");
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Réunion introuvable");
        }
        return redirectReunions(stageId);
    }

    /** Charge le stage et ses réunions pour la page. */
    private void charger(Long stageId, Model model) {
        model.addAttribute("stage", stageService.obtenir(stageId));
        model.addAttribute("reunions", reunionService.listerParStage(stageId));
    }

    /** Retourne la réunion uniquement si elle appartient bien à ce stage. */
    private ReunionDto reunionDuStage(Long stageId, Long reunionId) {
        return reunionService.listerParStage(stageId).stream()
                .filter(r -> r.id().equals(reunionId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Réunion", reunionId));
    }

    private String redirectReunions(Long stageId) {
        return "redirect:/enseignant/stages/" + stageId + "/reunions";
    }
}