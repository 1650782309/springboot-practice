package com.example.service;

import com.example.vo.Result;
import com.example.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> fingTagsByArtticleId(Long articleId);

    Result hots(int limit);

    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);
}
