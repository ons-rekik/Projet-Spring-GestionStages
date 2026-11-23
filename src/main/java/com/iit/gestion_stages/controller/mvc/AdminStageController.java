package com.iit.gestion_stages.controller.mvc;

import com.iit.gestion_stages.controller.mvc.form.StageForm;
import com.iit.gestion_stages.dto.AffectationDto;
import com.iit.gestion_stages.dto.StageDto;
import com.iit.gestion_stages.dto.StatistiquesDto;
import com.iit.gestion_stages.dto.StatutStageDto;
import com.iit.gestion_stages.entity.enums.StatutStage;
import com.iit.gestion_stages.exception.BusinessException;
import com.iit.gestion_stages.exception.ResourceNotFoundException;
import com.iit.gestion_stages.service.EnseignantService;
import com.iit.gestion_stages.service.EntrepriseService;
import com.iit.gestion_stages.service.EtudiantService;
import com.iit.gestion_stages.service.StageService;
import com.iit.gestion_stages.service.StatistiquesService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/stages")
public class AdminStageController {

    private static final String VUE_LISTE = "admin/stages/liste";
    private static final String VUE_FORM = "admin/stages/form";
    private static final String VUE_AFFECTATION = "admin/stages/affectation";
    private static final String REDIRECT_LISTE = "redirect:/admin/stages";

    private final StageService stageService;
    private final StatistiquesService statistiquesService;
    private final EtudiantService etudiantService;
    private final EnseignantService enseignantService;
    private final EntrepriseService entrepriseService;

    public AdminStageController(StageService stageService,
                                StatistiquesService statistiquesService,
                                EtudiantService etudiantService,
                                EnseignantService enseignantService,
                                EntrepriseService entrepriseService) {
        this.stageService = stageService;
        this.statistiquesService = statistiquesService;
        this.etudiantService = etudiantService;
        this.enseignantService = enseignantService;
        this.entrepriseService = entrepriseService;
    }

    // ---------- Liste + statistiques ----------

    @GetMapping
    public String liste(@RequestParam(required = false) StatutStage statut,
                        @RequestParam(required = false) String q,
                        Model model) {
        List<StageDto> stages = (statut != null)
                ? stageService.listerParStatut(statut)
                : stageService.lister();

        if (q != null && !q.isBlank()) {
            String recherche = q.trim().toLowerCase();
            stages = stages.stream()
                    .filter(s -> contient(s.sujet(), recherche)
                            || contient(s.etudiantNomComplet(), recherche)
                            || contient(s.entrepriseNom(), recherche)
                            || contient(s.encadrantNomComplet(), recherche))
                    .toList();
        }

        StatistiquesDto stats = statistiquesService.obtenir();
        model.addAttribute("stages", stages);
        model.addAttribute("stats", stats);
        model.addAttribute("stagesParStatut", stats.stagesParStatut());
        model.addAttribute("statuts", StatutStage.values());
        model.addAttribute("statut", statut);
        model.addAttribute("q", q);
        return VUE_LISTE;
    }

    // ---------- Création / modification ----------

    @GetMapping("/nouveau")
    public String nouveau(Model model) {
        model.addAttribute("stage", new StageForm());
        chargerListes(model);
        return VUE_FORM;
    }

    @GetMapping("/{id}/modifier")
    public String modifier(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("stage", StageForm.from(stageService.obtenir(id)));
            chargerListes(model);
            return VUE_FORM;
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Stage introuvable");
            return REDIRECT_LISTE;
        }
    }

    @PostMapping
    public String enregistrer(@Valid @ModelAttribute("stage") StageForm form,
                              BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            chargerListes(model);
            return VUE_FORM;
        }
        try {
            if (form.getId() == null) {
                stageService.creer(form.toCreateDto());
                ra.addFlashAttribute("success", "Stage ajouté avec succès");
            } else {
                stageService.modifier(form.getId(), form.toCreateDto());
                ra.addFlashAttribute("success", "Stage modifié avec succès");
            }
        } catch (BusinessException ex) {
            // ex. date de fin avant la date de début : on reste sur le formulaire
            result.reject("stage.invalide", ex.getMessage());
            chargerListes(model);
            return VUE_FORM;
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Stage, étudiant ou entreprise introuvable");
        }
        return REDIRECT_LISTE;
    }

    // ---------- Affectation d'un enseignant ----------

    @GetMapping("/{id}/affecter")
    public String affecter(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("stage", stageService.obtenir(id));
            model.addAttribute("enseignants", enseignantService.lister());
            return VUE_AFFECTATION;
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Stage introuvable");
            return REDIRECT_LISTE;
        }
    }

    @PostMapping("/{id}/affecter")
    public String enregistrerAffectation(@PathVariable Long id,
                                         @RequestParam(required = false) Long enseignantId,
                                         RedirectAttributes ra) {
        if (enseignantId == null) {
            ra.addFlashAttribute("error", "Veuillez choisir un enseignant");
            return "redirect:/admin/stages/" + id + "/affecter";
        }
        try {
            stageService.affecterEncadrant(id, new AffectationDto(enseignantId));
            ra.addFlashAttribute("success", "Enseignant affecté au stage");
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Stage ou enseignant introuvable");
        }
        return REDIRECT_LISTE;
    }

    // ---------- Statut ----------

    @PostMapping("/{id}/statut")
    public String changerStatut(@PathVariable Long id,
                                @RequestParam StatutStage statut,
                                RedirectAttributes ra) {
        try {
            stageService.changerStatut(id, new StatutStageDto(statut));
            ra.addFlashAttribute("success", "Statut mis à jour");
        } catch (BusinessException ex) {
            // ex. aucun encadrant affecté
            ra.addFlashAttribute("error", ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Stage introuvable");
        }
        return REDIRECT_LISTE;
    }

    // ---------- Suppression ----------

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes ra) {
        try {
            stageService.supprimer(id);
            ra.addFlashAttribute("success", "Stage supprimé");
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Stage introuvable");
        }
        return REDIRECT_LISTE;
    }

    // ---------- Utilitaires ----------

    private void chargerListes(Model model) {
        model.addAttribute("etudiants", etudiantService.lister());
        model.addAttribute("entreprises", entrepriseService.lister());
    }

    private boolean contient(String valeur, String recherche) {
        return valeur != null && valeur.toLowerCase().contains(recherche);
    }
}