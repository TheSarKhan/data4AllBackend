package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.PageBasicDto;
import org.example.dataprotal.dto.PageFullDto;
import org.example.dataprotal.model.page.Page;
import org.example.dataprotal.model.page.Subpage;
import org.example.dataprotal.service.PageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/page")
@RequiredArgsConstructor
public class PageController {
    private final PageService pageService;

    @PostMapping
    @Operation(summary = "page creation",
            description = "Create page and add subpage urls")
    public ResponseEntity<Page> createPage(@RequestBody PageFullDto pageFullDto) {
        final var createdPage = pageService.createPage(pageFullDto);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").build(createdPage.getId());
        return ResponseEntity.created(location).body(createdPage);
    }

    @PutMapping("/{id}")
    @Operation(summary = "page update",
            description = "Update page informations")
    public ResponseEntity<Page> updatePage(@PathVariable Long id, @RequestBody PageBasicDto pageBasicDto) {
        final var updatedPage = pageService.updatePage(id,pageBasicDto);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").build(updatedPage.getId());
        return ResponseEntity.created(location).body(updatedPage);
    }

    @PostMapping("/{id}/add/subPage")
    @Operation(summary = "add subpage",
            description = "only add subpage without update entire information of page")
    public ResponseEntity<Page>addSubPage(@PathVariable Long id,@RequestBody Subpage subpage) {
        final var updatedPage = pageService.addSubPage(id,subpage);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").build(updatedPage.getId());
        return ResponseEntity.created(location).body(updatedPage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>deletePage(@PathVariable Long id) {
        pageService.deletePage(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    @Operation(summary = "Get page informations",
            description = "Returns the page data with name")
    public ResponseEntity<Page> getPageByName(@RequestParam String pageName) {
        return ResponseEntity.ok(pageService.getPageByName(pageName));
    }
}
