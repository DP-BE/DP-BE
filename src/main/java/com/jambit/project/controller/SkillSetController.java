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

    // 모든 기술스택을 불러옴
    @GetMapping("/all")
    public ResponseEntity<List<SkillSetDto>> getAllProject(){
        List<SkillSetDto> skillSets = skillSetService.findAllSkillSetList();
        return new ResponseEntity<>(skillSets, HttpStatus.OK);
    }

    // 기술스택 이름이 포함된 기술들을 찾아줌
    @GetMapping("/list")
    public ResponseEntity<List<SkillSetDto>> getProjectWithName(@RequestParam("skillName") String skillName) {
        List<SkillSetDto> skillSetWithName = skillSetService.findSkillSetWithName(skillName);
        return new ResponseEntity<>(skillSetWithName, HttpStatus.OK);
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
