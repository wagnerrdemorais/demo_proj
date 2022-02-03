package com.demo.controller;

import com.demo.config.bean.ConfigBean;
import com.demo.service.FileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RestController
public class ConfigController {

    private final FileService fileService;

    public ConfigController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/xml")
    public String createXmlFile() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String xml = xmlMapper.writeValueAsString(new ConfigBean());

        Path path = fileService.createFile("config", ".xml", List.of(xml));
        return path.toString();
    }

    @PostMapping("/xml")
    public ConfigBean readXmlFile(@RequestParam(name = "xml") String xml) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        ConfigBean value = xmlMapper.readValue(xml, ConfigBean.class);
        return value;
    }

    @PostMapping("/updateXml")
    public String readXmlFile(@RequestBody ConfigBean xml) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String test = xmlMapper.writeValueAsString(xml);

        Path path = fileService.createFile("config", ".xml", List.of(test));
        return path.toString();
    }


}
