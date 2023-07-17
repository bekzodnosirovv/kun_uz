package com.example.service;

import com.example.dto.CategoryDTO;
import com.example.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO create(CategoryDTO dto) {
        return null;
    }

    public void update(Integer id) {
    }

    public void delete(Integer id) {
    }

    public List<CategoryDTO> getAll() {
        return null;
    }

    public List<CategoryDTO> getByLan(String lan) {
        return null;
    }

}
