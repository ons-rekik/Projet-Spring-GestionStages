package com.iit.gestion_stages.controller.mvc;

import com.iit.gestion_stages.entity.Entreprise;
import com.iit.gestion_stages.repository.EntrepriseRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/entreprises")
public class AdminEntrepriseController {

    private final EntrepriseRepository entrepriseRepository;

    public AdminEntrepriseController(EntrepriseRepository entrepriseRepository) {
        this.entrepriseRepository = entrepriseRepository;
    }

    @GetMapping
    public String liste(@RequestParam(required = false) String q, Model model) {
        if (q != null && !q.isBlank()) {
            model.addAttribute("entreprises", entrepriseRepository.findByNomContainingIgnoreCase(q));
        } else {
            model.addAttribute("entreprises", entrepriseRepository.findAll());
        }
        model.addAttribute("q", q);
        return "admin/entreprises/liste";
    }

    @GetMapping("/nouveau")
    public String nouveau(Model model) {
        model.addAttribute("entreprise", new Entreprise());
        return "admin/entreprises/form";
    }

    @GetMapping("/{id}/modifier")
    public String modifier(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return entrepriseRepository.findById(id)
                .map(e -> {
                    model.addAttribute("entreprise", e);
                    return "admin/entreprises/form";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("error", "Entreprise introuvable");
                    return "redirect:/admin/entreprises";
                });
    }

    @PostMapping
    public String enregistrer(@Valid @ModelAttribute("entreprise") Entreprise entreprise,
                              BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "admin/entreprises/form";
        }
        boolean creation = entreprise.getId() == null;
        entrepriseRepository.save(entreprise);
        ra.addFlashAttribute("success",
                creation ? "Entreprise ajoutée avec succès" : "Entreprise modifiée avec succès");
        return "redirect:/admin/entreprises";
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes ra) {
        try {
            entrepriseRepository.deleteById(id);
            ra.addFlashAttribute("success", "Entreprise supprimée");
        } catch (DataIntegrityViolationException ex) {
            ra.addFlashAttribute("error",
                    "Impossible de supprimer : cette entreprise est liée à des stages");
        }
        return "redirect:/admin/entreprises";
    }
}