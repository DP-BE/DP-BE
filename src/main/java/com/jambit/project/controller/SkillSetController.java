package com.jambit.project.controller;

import com.jambit.project.domain.entity.SkillSet;
import com.jambit.project.dto.SkillSetDto;
import com.jambit.project.service.SkillSetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/skill")
@Slf4j
public class SkillSetController {

    private final SkillSetService skillSetService;

    @GetMapping("/list")
    public ResponseEntity<List<SkillSetDto>> getAllProject(){
        List<SkillSetDto> skillSets = skillSetService.findAllSkillSetList();
        return new ResponseEntity<>(skillSets, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> create(@RequestBody SkillSetDto skillSetDto) {
        Long registerId = skillSetService.create(skillSetDto);
        if (registerId != null) {
            return new ResponseEntity<>(registerId, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{skill_id}")
    public ResponseEntity<Void> delete(@PathVariable("skill_id") Long skill_id) {
        skillSetService.delete(skill_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
