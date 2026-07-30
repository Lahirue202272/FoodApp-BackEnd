package com.phegon.FoodApp.menu.controller;

import com.phegon.FoodApp.menu.dtos.MenuDTO;
import com.phegon.FoodApp.menu.services.MenuService;
import com.phegon.FoodApp.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)//This API accepts form-data + file why need-Because you are sending:menu details (name, price, etc.) AND image file
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<MenuDTO>> createMenu(
            @ModelAttribute @Valid MenuDTO menuDTO, //ModelAttribute:-Converts form-data → Java object
            @RequestPart(value = "imageFile", required = true) MultipartFile imageFile){ //MultipartFile:Represents uploaded file,imageFile:Variable holding uploaded image,@RequestPart:Extracts file from request,required = true:Image must be provided if not error
        menuDTO.setImageFile(imageFile);
        return ResponseEntity.ok(menuService.createMenu(menuDTO));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<MenuDTO>> updateMenu(
            @ModelAttribute @Valid MenuDTO menuDTO,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile){
        menuDTO.setImageFile(imageFile);
        return ResponseEntity.ok(menuService.updateMenu(menuDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<MenuDTO>> getMenuById(@PathVariable Long id){
        return ResponseEntity.ok(menuService.getMenuById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<?>> deleteMenu(@PathVariable Long id){
        return ResponseEntity.ok(menuService.deleteMenu(id));
    }

    @GetMapping
    public ResponseEntity<Response<List<MenuDTO>>> getMenus(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search){
        return ResponseEntity.ok(menuService.getMenus(categoryId,search));
    }
}
