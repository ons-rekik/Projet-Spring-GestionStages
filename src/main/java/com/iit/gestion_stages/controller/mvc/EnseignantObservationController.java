package com.iit.gestion_stages.controller.mvc;

import com.iit.gestion_stages.controller.mvc.form.ObservationForm;
import com.iit.gestion_stages.dto.ObservationDto;
import com.iit.gestion_stages.dto.StageDto;
import com.iit.gestion_stages.exception.BusinessException;
import com.iit.gestion_stages.exception.ResourceNotFoundException;
import com.iit.gestion_stages.service.ObservationService;
import com.iit.gestion_stages.service.StageService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;

/**
 * Espace enseignant : ajout d'observations et de recommandations sur un stage.
 * L'auteur de l'observation est l'enseignant encadrant du stage.
 */
@Controller
@RequestMapping("/enseignant/stages/{stageId}/observations")
public class EnseignantObservationController {

    private static final String VUE = "enseignant/observation-form";
    private static final String REDIRECT_ETUDIANTS = "redirect:/enseignant/etudiants";

    private final StageService stageService;
    private final ObservationService observationService;

    public EnseignantObservationController(StageService stageService,
                                           ObservationService observationService) {
        this.stageService = stageService;
        this.observationService = observationService;
    }

    @GetMapping
    public String liste(@PathVariable Long stageId, Model model, RedirectAttributes ra) {
        try {
            charger(stageId, model);
            model.addAttribute("observation", new ObservationForm());
            return VUE;
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Stage introuvable");
            return REDIRECT_ETUDIANTS;
        }
    }

    @PostMapping
    public String ajouter(@PathVariable Long stageId,
                          @Valid @ModelAttribute("observation") ObservationForm form,
                          BindingResult result, Model model, RedirectAttributes ra) {
        try {
            StageDto stage = stageService.obtenir(stageId);
            if (result.hasErrors()) {
                charger(stageId, model);
                return VUE;
            }
            observationService.ajouter(stageId, form.toCreateDto(stage.encadrantId()));
            ra.addFlashAttribute("success", "Observation ajoutée avec succès");
        } catch (BusinessException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Stage introuvable");
            return REDIRECT_ETUDIANTS;
        }
        return redirectObservations(stageId);
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long stageId, @PathVariable Long id, RedirectAttributes ra) {
        try {
            boolean appartientAuStage = observationService.listerParStage(stageId).stream()
                    .anyMatch(o -> o.id().equals(id));
            if (!appartientAuStage) {
                throw new ResourceNotFoundException("Observation", id);
            }
            observationService.supprimer(id);
            ra.addFlashAttribute("success", "Observation supprimée");
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Observation introuvable");
        }
        return redirectObservations(stageId);
    }

    /** Charge le stage et ses observations (les plus récentes d'abord). */
    private void charger(Long stageId, Model model) {
        List<ObservationDto> observations = observationService.listerParStage(stageId).stream()
                .sorted(Comparator.comparing(ObservationDto::id).reversed())
                .toList();
        model.addAttribute("stage", stageService.obtenir(stageId));
        model.addAttribute("observations", observations);
    }

    private String redirectObservations(Long stageId) {
        return "redirect:/enseignant/stages/" + stageId + "/observations";
    }
}