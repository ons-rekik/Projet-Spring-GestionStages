package com.iit.gestion_stages.controller.mvc;

import com.iit.gestion_stages.controller.mvc.form.EtapeForm;
import com.iit.gestion_stages.dto.EtapeDto;
import com.iit.gestion_stages.exception.BusinessException;
import com.iit.gestion_stages.exception.ResourceNotFoundException;
import com.iit.gestion_stages.service.EtapeService;
import com.iit.gestion_stages.service.StageService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Espace enseignant : suivi et validation des étapes de progression d'un stage.
 * Une étape déclarée « terminée » par l'étudiant peut être validée par l'encadrant.
 */
@Controller
@RequestMapping("/enseignant/stages/{stageId}/etapes")
public class EnseignantEtapeController {

    private static final String VUE = "enseignant/etapes";
    private static final String REDIRECT_ETUDIANTS = "redirect:/enseignant/etudiants";

    private final StageService stageService;
    private final EtapeService etapeService;

    public EnseignantEtapeController(StageService stageService, EtapeService etapeService) {
        this.stageService = stageService;
        this.etapeService = etapeService;
    }

    @GetMapping
    public String liste(@PathVariable Long stageId, Model model, RedirectAttributes ra) {
        try {
            charger(stageId, model);
            model.addAttribute("etape", new EtapeForm());
            return VUE;
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Stage introuvable");
            return REDIRECT_ETUDIANTS;
        }
    }

    @PostMapping
    public String ajouter(@PathVariable Long stageId,
                          @Valid @ModelAttribute("etape") EtapeForm form,
                          BindingResult result, Model model, RedirectAttributes ra) {
        try {
            if (result.hasErrors()) {
                charger(stageId, model);
                return VUE;
            }
            etapeService.creer(stageId, form.toCreateDto());
            ra.addFlashAttribute("success", "Étape ajoutée avec succès");
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Stage introuvable");
            return REDIRECT_ETUDIANTS;
        }
        return redirectEtapes(stageId);
    }

    @PostMapping("/{id}/valider")
    public String valider(@PathVariable Long stageId, @PathVariable Long id, RedirectAttributes ra) {
        try {
            boolean appartientAuStage = etapeService.listerParStage(stageId).stream()
                    .anyMatch(e -> e.id().equals(id));
            if (!appartientAuStage) {
                throw new ResourceNotFoundException("Étape", id);
            }
            etapeService.valider(id);
            ra.addFlashAttribute("success", "Étape validée");
        } catch (BusinessException ex) {
            // ex : étape non terminée ou déjà validée
            ra.addFlashAttribute("error", ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Étape introuvable");
        }
        return redirectEtapes(stageId);
    }

    /** Charge le stage, ses étapes et la progression (étapes validées / total). */
    private void charger(Long stageId, Model model) {
        List<EtapeDto> etapes = etapeService.listerParStage(stageId);
        long validees = etapes.stream().filter(EtapeDto::validee).count();
        int pourcentage = etapes.isEmpty() ? 0 : (int) (validees * 100 / etapes.size());

        model.addAttribute("stage", stageService.obtenir(stageId));
        model.addAttribute("etapes", etapes);
        model.addAttribute("nbValidees", validees);
        model.addAttribute("pourcentage", pourcentage);
    }

    private String redirectEtapes(Long stageId) {
        return "redirect:/enseignant/stages/" + stageId + "/etapes";
    }
}