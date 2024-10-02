package dev.gfxv.lab1.controller;

import dev.gfxv.lab1.dao.Chapter;
import dev.gfxv.lab1.repository.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

    private final ChapterRepository chapterRepository;

    @Autowired
    public HomeController(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    @GetMapping("/test")
    public String test(
        @RequestParam(name = "name", required = false, defaultValue = "world") String name,
        Model model
    ) {
        model.addAttribute("name", name);
        return "test";
    }

    @PostMapping("/new")
    public void newChapter(
        @RequestBody Chapter chapter
    ) {

        Chapter ch = new Chapter();
        ch.setName(chapter.getName());
        ch.setWorld(chapter.getWorld());
        ch.setMarinesCount(chapter.getMarinesCount());
        ch.setParentLegion(chapter.getParentLegion());

        chapterRepository.save(ch);
    }
}
