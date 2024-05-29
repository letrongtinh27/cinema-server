package com.edu.hcmuaf.springserver.service;

import com.edu.hcmuaf.springserver.entity.Category;
import com.edu.hcmuaf.springserver.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

}
